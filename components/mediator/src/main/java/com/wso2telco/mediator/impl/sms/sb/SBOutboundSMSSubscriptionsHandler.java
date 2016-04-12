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
package com.wso2telco.mediator.impl.sms.sb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.dbutils.AxiataDbService;
import com.wso2telco.dbutils.Operatorsubs;
import com.wso2telco.mediator.OperatorEndpoint;
import com.wso2telco.mediator.entity.sb.SBDeliveryReceiptSubscriptionRequest;
import com.wso2telco.mediator.impl.sms.*;
import com.wso2telco.mediator.internal.ApiUtils;
import com.wso2telco.mediator.internal.Type;
import com.wso2telco.mediator.internal.UID;
import com.wso2telco.mediator.internal.Util;
import com.wso2telco.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.service.impl.sms.ValidateCancelSubscription;
import com.wso2telco.oneapivalidation.service.impl.sms.sb.ValidateSBOutboundSubscription;

import java.util.ArrayList;
import java.util.List;
import org.apache.axis2.AxisFault;

import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONException;
import org.json.JSONObject;

 
// TODO: Auto-generated Javadoc
/**
 * The Class SBOutboundSMSSubscriptionsHandler.
 */
public class SBOutboundSMSSubscriptionsHandler implements SMSHandler {

    /** The log. */
    private static Log log = LogFactory.getLog(SBOutboundSMSSubscriptionsHandler.class);
    
    /** The Constant API_TYPE. */
    private static final String API_TYPE = "smsmessaging";
    
    /** The occi. */
    private OriginatingCountryCalculatorIDD occi;
    
    /** The dbservice. */
    private AxiataDbService dbservice;
    
    /** The executor. */
    private SMSExecutor executor;
    
    /** The api utils. */
    private ApiUtils apiUtils;

    /**
     * Instantiates a new SB outbound sms subscriptions handler.
     *
     * @param executor the executor
     */
    public SBOutboundSMSSubscriptionsHandler(SMSExecutor executor) {
        this.executor = executor;
        occi = new OriginatingCountryCalculatorIDD();
        dbservice = new AxiataDbService();
        apiUtils = new ApiUtils();
    }

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.impl.sms.SMSHandler#handle(org.apache.synapse.MessageContext)
     */
    @Override
    public boolean handle(MessageContext context) throws CustomException, AxisFault, Exception {
        if (executor.getHttpMethod().equalsIgnoreCase("POST")) {
            return createSubscriptions(context);
        }  

        return false;
    }

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.impl.sms.SMSHandler#validate(java.lang.String, java.lang.String, org.json.JSONObject, org.apache.synapse.MessageContext)
     */
    @Override
    public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context) throws Exception {
        //context.setProperty("mife.prop.operationType", 205);
        IServiceValidate validator;
        if (httpMethod.equalsIgnoreCase("POST")) {
            validator = new ValidateSBOutboundSubscription();
            validator.validateUrl(requestPath);
            validator.validate(jsonBody.toString());
            return true;
        } else if (httpMethod.equalsIgnoreCase("DELETE")) {
            String axiataid = requestPath.substring(requestPath.lastIndexOf("/") + 1);
            String[] params = {axiataid};
            validator = new ValidateCancelSubscription();
            validator.validateUrl(requestPath);
            validator.validate(params);
            return true;
        } else {
            ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 405);
            throw new Exception("Method not allowed");
        }
    }

    /**
     * Creates the subscriptions.
     *
     * @param context the context
     * @return true, if successful
     * @throws Exception the exception
     */
    private boolean createSubscriptions(MessageContext context) throws Exception {

        String requestid = UID.getUniqueID(Type.RETRIVSUB.getCode(), context, executor.getApplicationid());
        Gson gson = new GsonBuilder().serializeNulls().create();

        HashMap<String, String> jwtDetails = apiUtils.getJwtTokenDetails(context);
        JSONObject jsonBody = executor.getJsonBody();
        JSONObject jsondstaddr = jsonBody.getJSONObject("deliveryReceiptSubscription");
        String orgclientcl = "";
        if (!jsondstaddr.isNull("clientCorrelator")) {
            orgclientcl = jsondstaddr.getString("clientCorrelator");
        }

        String serviceProvider = jwtDetails.get("subscriber");
        log.debug("Subscriber Name : " + serviceProvider);

        SBDeliveryReceiptSubscriptionRequest subsrequst = gson.fromJson(jsonBody.toString(), SBDeliveryReceiptSubscriptionRequest.class);
        String origNotiUrl = subsrequst.getDeliveryReceiptSubscription().getCallbackReference().getNotifyURL();
        subsrequst.getDeliveryReceiptSubscription().setClientCorrelator(orgclientcl + ":" + requestid);

        List<OperatorEndpoint> endpoints = occi.getAPIEndpointsByApp(API_TYPE, executor.getSubResourcePath(), executor.getValidoperators());

        Integer axiataid = dbservice.outboundSubscriptionEntry(subsrequst.getDeliveryReceiptSubscription().getCallbackReference().getNotifyURL(), serviceProvider);
        Util.getPropertyFile();
        String subsEndpoint = Util.getApplicationProperty("hubSubsGatewayEndpoint") + "/" + axiataid;
        jsondstaddr.getJSONObject("callbackReference").put("notifyURL", subsEndpoint);
        subsrequst.getDeliveryReceiptSubscription().getCallbackReference().setNotifyURL(subsEndpoint);

        String sbRequestBody = removeResourceURL(gson.toJson(subsrequst));
        List<Operatorsubs> domainsubs = new ArrayList<Operatorsubs>();
        SBDeliveryReceiptSubscriptionRequest subsresponse = null;
        for (OperatorEndpoint endpoint : endpoints) {

             
            String url = endpoint.getEndpointref().getAddress();
            url = url.replace("/subscriptions", "/subscriptionsMultipleOperators");
            log.debug("Delivery notification adaptor request url of " + endpoint.getOperator() + " operator: " + url);

            String notifyres = executor.makeRequest(endpoint, url, sbRequestBody, true, context);
            if (notifyres == null) {
                throw new CustomException("POL0299", "", new String[]{"Error registering subscription"});
            } else {
                subsresponse = gson.fromJson(notifyres, SBDeliveryReceiptSubscriptionRequest.class);
                if (subsrequst.getDeliveryReceiptSubscription() == null) {
                    executor.handlePluginException(notifyres);
                }
                domainsubs.add(new Operatorsubs(endpoint.getOperator(), subsresponse.getDeliveryReceiptSubscription().getResourceURL()));
            }
        }

        boolean issubs = dbservice.outboundOperatorsubsEntry(domainsubs, axiataid);
        String ResourceUrlPrefix = Util.getApplicationProperty("hubGateway");
        subsresponse.getDeliveryReceiptSubscription().setResourceURL(ResourceUrlPrefix + executor.getResourceUrl() + "/" + axiataid);
        JSONObject replyobj = new JSONObject(subsresponse);
        JSONObject replysubs = replyobj.getJSONObject("deliveryReceiptSubscription");
        replysubs.put("clientCorrelator", orgclientcl);
        replysubs.getJSONObject("callbackReference").put("notifyURL", origNotiUrl);
        jsondstaddr.put("resourceURL", ResourceUrlPrefix + executor.getResourceUrl() + "/" + axiataid);

        executor.removeHeaders(context);
        ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 201);
        executor.setResponse(context, replyobj.toString());

        return true;
    }

     
    /**
     * Removes the resource url.
     *
     * @param sbSubsrequst the sb subsrequst
     * @return the string
     */
    private String removeResourceURL(String sbSubsrequst) {
        String sbDeliveryNotificationrequestString = "";
        try {
            JSONObject objJSONObject = new JSONObject(sbSubsrequst);
            JSONObject objDeliveryNotificationRequest = (JSONObject) objJSONObject.get("deliveryReceiptSubscription");
            objDeliveryNotificationRequest.remove("resourceURL");

            sbDeliveryNotificationrequestString = objDeliveryNotificationRequest.toString();
        } catch (JSONException ex) {
            log.error("Error in removeResourceURL" + ex.getMessage());
            throw new CustomException("POL0299", "", new String[]{"Error registering subscription"});
        }
        return "{\"deliveryReceiptSubscription\":" + sbDeliveryNotificationrequestString + "}";
    }

     
}
