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
package com.wso2telco.dep.mediator.impl.smsmessaging.southbound;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.axiom.soap.SOAPBody;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.entity.smsmessaging.southbound.InboundSMSMessage;
import com.wso2telco.dep.mediator.entity.smsmessaging.southbound.SouthboundRetrieveResponse;
import com.wso2telco.dep.mediator.impl.smsmessaging.SMSExecutor;
import com.wso2telco.dep.mediator.impl.smsmessaging.SMSHandler;
import com.wso2telco.dep.mediator.internal.APICall;
import com.wso2telco.dep.mediator.internal.ApiUtils;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.service.impl.sms.sb.ValidateSBRetrieveSms;

// TODO: Auto-generated Javadoc
/**
 * The Class SBRetrieveSMSHandler.
 */
public class RetrieveSMSSouthboundHandler implements SMSHandler {

	/** The log. */
	private static Log log = LogFactory.getLog(RetrieveSMSSouthboundHandler.class);

	/** The Constant API_TYPE. */
	private static final String API_TYPE = "smsmessaging";

	/** The occi. */
	private OriginatingCountryCalculatorIDD occi;

	/** The api util. */
	private ApiUtils apiUtil;

	/** The executor. */
	private SMSExecutor executor;

	/**
	 * Instantiates a new SB retrieve sms handler.
	 *
	 * @param executor
	 *            the executor
	 */
	public RetrieveSMSSouthboundHandler(SMSExecutor executor) {
		this.executor = executor;
		occi = new OriginatingCountryCalculatorIDD();
		apiUtil = new ApiUtils();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.mediator.impl.sms.SMSHandler#handle(org.apache.synapse.
	 * MessageContext)
	 */
	@Override
	public boolean handle(MessageContext context) throws CustomException, AxisFault, Exception {

		SOAPBody body = context.getEnvelope().getBody();
		Gson gson = new GsonBuilder().serializeNulls().create();
		Properties prop = new Properties();

		String reqType = "retrive_sms";
		String requestid = UID.getUniqueID(Type.SMSRETRIVE.getCode(), context, executor.getApplicationid());

		int batchSize = 100;

		URL retrieveURL = new URL("http://example.com/smsmessaging/v1" + executor.getSubResourcePath());
		String urlQuery = retrieveURL.getQuery();

		if (urlQuery != null) {
			if (urlQuery.contains("maxBatchSize")) {
				String queryParts[] = urlQuery.split("=");
				if (queryParts.length > 1) {
					if (Integer.parseInt(queryParts[1]) < 100) {
						batchSize = Integer.parseInt(queryParts[1]);
					}
				}
			}
		}

		List<OperatorEndpoint> endpoints = occi.getAPIEndpointsByApp(API_TYPE, executor.getSubResourcePath(),
				executor.getValidoperators());

		log.debug("Endpoints size: " + endpoints.size());

		Collections.shuffle(endpoints);
		int perOpCoLimit = batchSize / (endpoints.size());

		log.debug("Per OpCo limit :" + perOpCoLimit);

		JSONArray results = new JSONArray();

		int execCount = 0;
	 	int forLoopCount=0;
        boolean retryFlag=true;
        
        FileReader fileReader = new FileReader();
        Map<String, String> mediatorConfMap = fileReader.readMediatorConfFile();
        Boolean retry = false;
        retry =Boolean.valueOf(mediatorConfMap.get("retry_on_fail"));
        Integer retryCount = Integer.valueOf(mediatorConfMap.get("retry_count"));
     
		
		ArrayList<String> responses = new ArrayList<String>();
		while ((results.length() < batchSize) &&  (retryFlag==true)) {
	        execCount++;
	        log.debug("SB aEndpoint : "+endpoints.size());	
        	for (int i = 0; i < endpoints.size(); i++) {
        		forLoopCount++;
				log.debug("Default forLoopCount : "+forLoopCount);
                OperatorEndpoint aEndpoint = endpoints.remove(0);
                log.debug("SB aEndpoint : "+aEndpoint.getEndpointref().getAddress()); 
                endpoints.add(aEndpoint);
                String url = aEndpoint.getEndpointref().getAddress();
                APICall ac = apiUtil.setBatchSize(url, body.toString(), reqType, perOpCoLimit);
                
                JSONObject obj = ac.getBody();
                String retStr = null;
               log.debug("Retrieving messages of operator: " + aEndpoint.getOperator());

                if (context.isDoingGET()) {
                    log.debug("Doing makeRequest");
                    retStr = executor.makeRequest(aEndpoint, ac.getUri(), obj.toString(), true, context,false);
                } else {
                	continue;
                }
                log.debug("Retrieved messages of " + aEndpoint.getOperator() + " operator: " + retStr);

                if (retStr != null) {
	 
	                 JSONArray resList = apiUtil.getResults(reqType, retStr);
	 	                if (resList != null) {
	 	                    for (int t = 0; t < resList.length(); t++) {
	 	                        results.put(resList.get(t));
	 	                    }
	 	                    responses.add(retStr);
	 	                }
	 
	                 }
	                 
	                 if (results.length() >= batchSize) {
	     				break;
	     			}                
	         	}
	         	
	         	if (retry==false) {
	 				retryFlag=false;
	 				log.debug("11 Final value of retryFlag :" + retryFlag);
		 			}
		        	
	         	if (execCount>=retryCount) {
	 				retryFlag=false;
	 				log.debug("22 Final value of retryFlag :" + retryFlag);
	 			}
	         	
	         	log.debug("Final value of count :" + execCount);
	            log.debug("Results length of retrieve messages: " + results.length());
		}

		JSONObject paylodObject = apiUtil.generateResponse(context, reqType, results, responses, requestid);
		String strjsonBody = paylodObject.toString();

		/*add resourceURL to the southbound response*/
		SouthboundRetrieveResponse sbRetrieveResponse = gson.fromJson(strjsonBody, SouthboundRetrieveResponse.class);
		if (sbRetrieveResponse!= null && sbRetrieveResponse.getInboundSMSMessageList() != null) {
			String resourceURL = sbRetrieveResponse.getInboundSMSMessageList().getResourceURL();
			InboundSMSMessage[] inboundSMSMessageResponses = sbRetrieveResponse.getInboundSMSMessageList().getInboundSMSMessage();

			for (int i = 0; i < inboundSMSMessageResponses.length; i++) {
				String messageId = inboundSMSMessageResponses[i].getMessageId();
				inboundSMSMessageResponses[i].setResourceURL(resourceURL + "/" + messageId);
			}
			sbRetrieveResponse.getInboundSMSMessageList().setInboundSMSMessage(inboundSMSMessageResponses);
		}

		executor.removeHeaders(context);
		executor.setResponse(context, gson.toJson(sbRetrieveResponse));

		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("messageType", "application/json");

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.mediator.impl.sms.SMSHandler#validate(java.lang.String,
	 * java.lang.String, org.json.JSONObject, org.apache.synapse.MessageContext)
	 */
	@Override
	public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context)
			throws Exception {
		if (!httpMethod.equalsIgnoreCase("GET")) {
			((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 405);
			throw new Exception("Method not allowed");
		}

		if (httpMethod.equalsIgnoreCase("GET")) {
			IServiceValidate validator;
			String urlParts = apiUtil.getAppID(context, "retrive_sms");
			String appID = "";
			String criteria = "";
			String[] param = urlParts.split("/");
			if (param.length == 2) {
				appID = param[0];
				criteria = param[1];
			}

			String[] params = { appID, criteria, "" };
			validator = new ValidateSBRetrieveSms();
			validator.validateUrl(requestPath);
			validator.validate(params);
		}

		return true;
	}
}
