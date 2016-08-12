/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.mediator.impl.smsmessaging.northbound;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.entity.smsmessaging.northbound.InboundSMSMessage;
import com.wso2telco.dep.mediator.entity.smsmessaging.northbound.NorthboundRetrieveRequest;
import com.wso2telco.dep.mediator.entity.smsmessaging.northbound.NorthboundRetrieveResponse;
import com.wso2telco.dep.mediator.entity.smsmessaging.northbound.Registrations;
import com.wso2telco.dep.mediator.impl.smsmessaging.*;
import com.wso2telco.dep.mediator.internal.APICall;
import com.wso2telco.dep.mediator.internal.ApiUtils;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.service.impl.sms.nb.ValidateNBRetrieveSms;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class NBRetrieveSMSHandler.
 */
public class RetrieveSMSNorthboundHandler implements SMSHandler {

    /** The log. */
    private static Log log = LogFactory.getLog(RetrieveSMSNorthboundHandler.class);
    
    /** The Constant API_TYPE. */
    private static final String API_TYPE = "smsmessaging";
    
    /** The occi. */
    private OriginatingCountryCalculatorIDD occi;
    
    /** The api util. */
    private ApiUtils apiUtil;
    
    /** The executor. */
    private SMSExecutor executor;

    /**
     * Instantiates a new NB retrieve sms handler.
     *
     * @param executor the executor
     */
    public RetrieveSMSNorthboundHandler(SMSExecutor executor) {
        this.executor = executor;
        occi = new OriginatingCountryCalculatorIDD();
        apiUtil = new ApiUtils();
    }

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.impl.sms.SMSHandler#handle(org.apache.synapse.MessageContext)
     */
    @Override
    public boolean handle(MessageContext context) throws CustomException, AxisFault, Exception {

        SOAPBody body = context.getEnvelope().getBody();
        Gson gson = new GsonBuilder().serializeNulls().create();

        String reqType = "retrive_sms";
        String requestid = UID.getUniqueID(Type.SMSRETRIVE.getCode(), context, executor.getApplicationid());

        int batchSize = 100;

        JSONObject jsonBody = executor.getJsonBody();
        NorthboundRetrieveRequest nbRetrieveRequest = gson.fromJson(jsonBody.toString(), NorthboundRetrieveRequest.class);
        log.debug("-------------------------------------- Retrieve messages sent to your Web application --------------------------------------");
        log.debug("Retrieve northbound request body : " + gson.toJson(nbRetrieveRequest));

        List<OperatorEndpoint> endpoints = occi.getAPIEndpointsByApp(API_TYPE, executor.getSubResourcePath(),
                executor.getValidoperators());

        List<OperatorEndpoint> validEndpoints = new ArrayList<OperatorEndpoint>();
        Registrations[] registrations = nbRetrieveRequest.getInboundSMSMessages().getRegistrations();

        if (nbRetrieveRequest.getInboundSMSMessages().getMaxBatchSize() != null) {
            String requestBodyBatchSize = nbRetrieveRequest.getInboundSMSMessages().getMaxBatchSize();

            if (!requestBodyBatchSize.equals("")) {
                if (Integer.parseInt(requestBodyBatchSize) < 100) {
                    batchSize = Integer.parseInt(requestBodyBatchSize);
                }
            }
        }

        for (OperatorEndpoint operatorEndpoint : endpoints) {

            for (int i = 0; i < registrations.length; i++) {
                if (registrations[i].getOperatorCode().equalsIgnoreCase(operatorEndpoint.getOperator())) {
                    validEndpoints.add(operatorEndpoint);
                    break;
                }
            }
        }

        log.debug("Endpoints size: " + validEndpoints.size());

        Collections.shuffle(validEndpoints);
        int perOpCoLimit = batchSize / (validEndpoints.size());

        log.debug("Per OpCo limit :" + perOpCoLimit);

         
        List<InboundSMSMessage> inboundSMSMessageList = new ArrayList<InboundSMSMessage>();

         
        int count = 0;
         
        ArrayList<String> responses = new ArrayList<String>();
        while (inboundSMSMessageList.size() < batchSize) {
            OperatorEndpoint aEndpoint = validEndpoints.remove(0);
            validEndpoints.add(aEndpoint);
            String url = aEndpoint.getEndpointref().getAddress();
            String getRequestURL = null;
            String criteria = null;
            String operatorCode = null;

            for (int i = 0; i < registrations.length; i++) {
                if (registrations[i].getOperatorCode().equalsIgnoreCase(aEndpoint.getOperator())) {
                	/*create request url for southbound operators*/
                	
                    if (registrations[i].getCriteria() != null) {
                        criteria = registrations[i].getCriteria();
                    }

                    if (criteria == null || criteria.equals("")) {
                        operatorCode = registrations[i].getOperatorCode();
                        getRequestURL = "/" + registrations[i].getRegistrationID() + "/messages?maxBatchSize=" + batchSize;
                        url = url.replace("/messages", getRequestURL);
                        log.debug("Invoke RetrieveSMSHandler of plugin");
                    } else {
                        operatorCode = registrations[i].getOperatorCode();
                        getRequestURL = "/" + registrations[i].getRegistrationID() + "/" + criteria + "/messages?maxBatchSize=" + batchSize;
                        url = url.replace("/messages", getRequestURL);
                        log.debug("Invoke SBRetrieveSMSHandler of plugin");
                    }

                    break;
                }
            }

            APICall ac = apiUtil.setBatchSize(url, body.toString(), reqType, perOpCoLimit);

            JSONObject obj = ac.getBody();
            String retStr = null;
            log.debug("Retrieving messages of operator: " + aEndpoint.getOperator());

            context.setDoingGET(true);
            if (context.isDoingGET()) {
            	
                log.debug("Doing makeGetRequest");
                retStr = executor.makeGetRequest(aEndpoint, ac.getUri(), null, true, context,false);
            } else {
            	
                log.debug("Doing makeRequest");
                retStr = executor.makeRequest(aEndpoint, ac.getUri(), obj.toString(), true, context,false);
            }

            log.debug("Retrieved messages of " + aEndpoint.getOperator() + " operator: " + retStr);

            if (retStr == null) {
                count++;
                if (count == validEndpoints.size()) {
                    log.debug("-------------------------------------------------> Break because count == validEndpoints.size() ------> count :" + count + " validEndpoints.size() :" + validEndpoints.size());
                    break;
                } else {
                    continue;
                }
            }

            /*add criteria and operatorCode to the southbound response*/ 
            NorthboundRetrieveResponse sbRetrieveResponse = gson.fromJson(retStr, NorthboundRetrieveResponse.class);

            if (sbRetrieveResponse != null) {
                InboundSMSMessage[] inboundSMSMessageResponses = sbRetrieveResponse.getInboundSMSMessageList().getInboundSMSMessage();

                for (int i = 0; i < inboundSMSMessageResponses.length; i++) {
                    inboundSMSMessageResponses[i].setCriteria(criteria);
                    inboundSMSMessageResponses[i].setOperatorCode(operatorCode);

                    inboundSMSMessageList.add(inboundSMSMessageResponses[i]);
                }
                sbRetrieveResponse.getInboundSMSMessageList().setInboundSMSMessage(inboundSMSMessageResponses);
                responses.add(gson.toJson(sbRetrieveResponse));
            }

            count++;
            if (count == (validEndpoints.size() * 2)) {
                log.debug("-------------------------------------------------> Break because count == (validEndpoints.size() * 2) ------> count :" + count + " (validEndpoints.size() * 2) :" + validEndpoints.size() * 2);
                break;
            }
        }

        log.debug("Final value of count :" + count);
        log.debug("Results length of retrieve messages: " + inboundSMSMessageList.size());

        JSONObject paylodObject = apiUtil.generateResponse(context, reqType, inboundSMSMessageList, responses, requestid);
        String strjsonBody = paylodObject.toString();

        /*create northbound response. add clientCorrelator and resourceURL*/
        NorthboundRetrieveResponse nbRetrieveResponse = gson.fromJson(strjsonBody, NorthboundRetrieveResponse.class);
        nbRetrieveResponse.getInboundSMSMessageList().setClientCorrelator(nbRetrieveRequest.getInboundSMSMessages().getClientCorrelator());
        String resourceURL = nbRetrieveResponse.getInboundSMSMessageList().getResourceURL();
        InboundSMSMessage[] inboundSMSMessageResponses = nbRetrieveResponse.getInboundSMSMessageList().getInboundSMSMessage();
        for (int i = 0; i < inboundSMSMessageResponses.length; i++) {
            String operatorCode = inboundSMSMessageResponses[i].getOperatorCode();
            String sestinationAddress = inboundSMSMessageResponses[i].getDestinationAddress();
            String messageId = inboundSMSMessageResponses[i].getMessageId();
            String inResourceURL = resourceURL.replace("registrations/", "registrations/" + operatorCode + "/" + sestinationAddress + "/") + "/" + messageId;
            inboundSMSMessageResponses[i].setResourceURL(inResourceURL);
        }
        nbRetrieveResponse.getInboundSMSMessageList().setInboundSMSMessage(inboundSMSMessageResponses);

        executor.removeHeaders(context);
        executor.setResponse(context, gson.toJson(nbRetrieveResponse));
        ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("messageType", "application/json");

        return true;
    }

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.impl.sms.SMSHandler#validate(java.lang.String, java.lang.String, org.json.JSONObject, org.apache.synapse.MessageContext)
     */
    @Override
    public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context) throws Exception {
        if (!httpMethod.equalsIgnoreCase("POST")) {
            ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 405);
            throw new Exception("Method not allowed");
        }

        if (httpMethod.equalsIgnoreCase("POST")) {
            IServiceValidate validator;
            validator = new ValidateNBRetrieveSms();
            validator.validateUrl(requestPath);
            validator.validate(jsonBody.toString());
        }

        return true;
    }
}
