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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;
import org.wso2.carbon.utils.CarbonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.core.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.entity.smsmessaging.northbound.InboundSMSMessage;
import com.wso2telco.dep.mediator.entity.smsmessaging.northbound.InboundSMSMessageList;
import com.wso2telco.dep.mediator.entity.smsmessaging.northbound.NorthboundRetrieveRequest;
import com.wso2telco.dep.mediator.entity.smsmessaging.northbound.NorthboundRetrieveResponse;
import com.wso2telco.dep.mediator.entity.smsmessaging.northbound.Registrations;
import com.wso2telco.dep.mediator.impl.smsmessaging.SMSExecutor;
import com.wso2telco.dep.mediator.impl.smsmessaging.SMSHandler;
import com.wso2telco.dep.mediator.internal.APICall;
import com.wso2telco.dep.mediator.internal.ApiUtils;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.util.FileNames;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.service.impl.smsmessaging.northbound.ValidateNBRetrieveSms;


// TODO: Auto-generated Javadoc
/**
 * The Class NBRetrieveSMSHandler.
 */
public class RetrieveSMSNorthboundHandler implements SMSHandler {

	/** The log. */
private static Log log = LogFactory
		.getLog(RetrieveSMSNorthboundHandler.class);

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
 * @param executor
 *            the executor
 */
public RetrieveSMSNorthboundHandler(SMSExecutor executor) {
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
public boolean handle(MessageContext context) throws CustomException,
		AxisFault, Exception {

	SOAPBody body = context.getEnvelope().getBody();
	Gson gson = new GsonBuilder().serializeNulls().create();

	String reqType = "retrive_sms";
	String requestid = UID.getUniqueID(Type.SMSRETRIVE.getCode(), context,
			executor.getApplicationid());

	int batchSize = 100;

	JSONObject jsonBody = executor.getJsonBody();
	NorthboundRetrieveRequest nbRetrieveRequest = gson.fromJson(
			jsonBody.toString(), NorthboundRetrieveRequest.class);
	log.info("-------------------------------------- Retrieve messages sent to your Web application --------------------------------------"
		 + " Request ID: " + UID.getRequestID(context));
	log.info("Retrieve northbound request body : " + gson.toJson(nbRetrieveRequest)
	     + " Request ID: " + UID.getRequestID(context));


	List<OperatorEndpoint> endpoints = occi.getAPIEndpointsByApp(API_TYPE,
			executor.getSubResourcePath(), executor.getValidoperators());

	List<OperatorEndpoint> validEndpoints = new ArrayList<OperatorEndpoint>();
	Registrations[] registrations = nbRetrieveRequest
			.getInboundSMSMessages().getRegistrations();

	if (nbRetrieveRequest.getInboundSMSMessages().getMaxBatchSize() != null) {
		String requestBodyBatchSize = nbRetrieveRequest
				.getInboundSMSMessages().getMaxBatchSize();

		if (!requestBodyBatchSize.equals("")) {
			if (Integer.parseInt(requestBodyBatchSize) < 100) {
				batchSize = Integer.parseInt(requestBodyBatchSize);
			}
		}
	}

	for (OperatorEndpoint operatorEndpoint : endpoints) {

		for (int i = 0; i < registrations.length; i++) {
			if (registrations[i].getOperatorCode().equalsIgnoreCase(
					operatorEndpoint.getOperator())) {
				validEndpoints.add(operatorEndpoint);
				break;
			}
		}
	}

	log.info("Endpoints size: " + validEndpoints.size());

	Collections.shuffle(validEndpoints);
	int perOpCoLimit = batchSize / (validEndpoints.size());

	log.info("Per OpCo limit :" + perOpCoLimit);

	List<InboundSMSMessage> inboundSMSMessageList = new ArrayList<InboundSMSMessage>();

	int execCount = 0;
	int forLoopCount=0;	
	boolean retryFlag = true;
	FileReader fileReader = new FileReader();
	String file = CarbonUtils.getCarbonConfigDirPath() + File.separator + FileNames.MEDIATOR_CONF_FILE.getFileName();
	Map<String, String> mediatorConfMap = fileReader.readPropertyFile(file);
    Boolean retry = false;
    retry =Boolean.valueOf(mediatorConfMap.get("retry_on_fail"));
    Integer retryCount = Integer.valueOf(mediatorConfMap.get("retry_count"));
 
	
    
	ArrayList<String> responses = new ArrayList<String>();
	while ((inboundSMSMessageList.size() < batchSize)
			&& (retryFlag == true)) {
		execCount++;
		log.info("NB aEndpoint : "+endpoints.size());
		
		for (int i = 0; i < validEndpoints.size(); i++) {
			forLoopCount++;
			log.info("NB forLoopCount : "+forLoopCount);
			OperatorEndpoint aEndpoint = validEndpoints.remove(0);
			log.info("NB aEndpoint : "+aEndpoint.getEndpointref().getAddress()); 
			validEndpoints.add(aEndpoint);
			String url = aEndpoint.getEndpointref().getAddress();
			String getRequestURL = null;
			String criteria = null;
			String operatorCode = null;

			for (int r = 0; r < registrations.length; r++) {
				if (registrations[r].getOperatorCode().equalsIgnoreCase(
						aEndpoint.getOperator())) {
					/* create request url for southbound operators */
					if (registrations[r].getCriteria() != null) {
						criteria = registrations[r].getCriteria();
					}

					if (criteria == null || criteria.equals("")) {
						operatorCode = registrations[r].getOperatorCode();
						log.info("Operator RetrieveSMSHandler"+operatorCode);
						getRequestURL = "/"+ registrations[r].getRegistrationID()+ "/messages?maxBatchSize=" + batchSize;
						url = url.replace("/messages", getRequestURL);
						log.info("Invoke RetrieveSMSHandler of plugin");
					} else {
						operatorCode = registrations[r].getOperatorCode();
						log.info("Operator RetrieveSMSHandler"+operatorCode);
						getRequestURL = "/"+ registrations[r].getRegistrationID()+ "/" + criteria+ "/messages?maxBatchSize=" + batchSize;
						url = url.replace("/messages", getRequestURL);
						log.info("Invoke SBRetrieveSMSHandler of plugin");
					}

					break;
				}

			}

			APICall ac = apiUtil.setBatchSize(url, body.toString(), reqType, perOpCoLimit);//check if request json body incorrect
			JSONObject obj = ac.getBody();
			String retStr = null;
			log.info("Retrieving messages of operator: "+ aEndpoint.getOperator());

			context.setDoingGET(true);
			if (context.isDoingGET()) {
				log.info("Doing makeGetRequest");
				retStr = executor.makeRetrieveSMSGetRequest(aEndpoint,ac.getUri(), null, true, context, false);
			} else {
				log.info("Doing makeRequest");
				retStr = executor.makeRequest(aEndpoint, ac.getUri(),obj.toString(), true, context, false);
			}

			log.info("Retrieved messages of " + aEndpoint.getOperator() + " operator: " + retStr
					+ " Request ID: " + UID.getRequestID(context));

			/* add criteria and operatorCode to the southbound response */
			NorthboundRetrieveResponse sbRetrieveResponse = gson.fromJson(retStr, NorthboundRetrieveResponse.class);

			if (sbRetrieveResponse!= null && sbRetrieveResponse.getInboundSMSMessageList() != null) {
				if (sbRetrieveResponse.getInboundSMSMessageList().getInboundSMSMessage() != null
						&& sbRetrieveResponse.getInboundSMSMessageList().getInboundSMSMessage().length != 0) {
					InboundSMSMessage[] inboundSMSMessageResponses = sbRetrieveResponse.getInboundSMSMessageList().getInboundSMSMessage();
					log.info("001 SECTION");
					for (int t = 0; t < inboundSMSMessageResponses.length; t++) {
						inboundSMSMessageResponses[t].setCriteria(criteria);
						inboundSMSMessageResponses[t].setOperatorCode(operatorCode);
						inboundSMSMessageList.add(inboundSMSMessageResponses[t]);
					}
					sbRetrieveResponse.getInboundSMSMessageList().setInboundSMSMessage(inboundSMSMessageResponses);
					responses.add(gson.toJson(sbRetrieveResponse));

				} else {
					log.info("002 SECTION");
					InboundSMSMessage[] inboundSMSMessageResponses = new InboundSMSMessage[0];
					InboundSMSMessageList inboundSMSMessageListN = new InboundSMSMessageList();
					inboundSMSMessageListN.setInboundSMSMessage(inboundSMSMessageResponses);
					inboundSMSMessageListN.setNumberOfMessagesInThisBatch("0");
					inboundSMSMessageListN.setResourceURL("Not Available");
					inboundSMSMessageListN.setTotalNumberOfPendingMessages("0");
					sbRetrieveResponse.setInboundSMSMessageList(inboundSMSMessageListN);

					for (int k = 0; k < inboundSMSMessageResponses.length; k++) {
						inboundSMSMessageResponses[k].setCriteria(criteria);
						inboundSMSMessageResponses[k].setOperatorCode(operatorCode);
						inboundSMSMessageList.add(inboundSMSMessageResponses[k]);
					}
					sbRetrieveResponse.getInboundSMSMessageList().setInboundSMSMessage(inboundSMSMessageResponses);
					responses.add(gson.toJson(sbRetrieveResponse));
				}
			}

			if (inboundSMSMessageList.size() >= batchSize) {
				break;
			}
		}

		log.info("Final value of count :" + execCount);
		log.info("Results length of retrieve messages: "
				+ inboundSMSMessageList.size());

		if (retry == false) {
			retryFlag = false;
			log.info("11 Final value of retryFlag :" + retryFlag);
		}

		if (execCount >= retryCount) {
			retryFlag = false;
			log.info("22 Final value of retryFlag :" + retryFlag);
		}
	}

	JSONObject paylodObject = apiUtil.generateResponse(context, reqType,
			inboundSMSMessageList, responses, requestid);
	String strjsonBody = paylodObject.toString();

	/* create northbound response. add clientCorrelator and resourceURL */
	NorthboundRetrieveResponse nbRetrieveResponse = gson.fromJson(strjsonBody, NorthboundRetrieveResponse.class);
	nbRetrieveResponse.getInboundSMSMessageList().setClientCorrelator(nbRetrieveRequest.getInboundSMSMessages().getClientCorrelator());
	String resourceURL = nbRetrieveResponse.getInboundSMSMessageList().getResourceURL();
	InboundSMSMessage[] inboundSMSMessageResponses = nbRetrieveResponse.getInboundSMSMessageList().getInboundSMSMessage();
	for (int q = 0; q < inboundSMSMessageResponses.length; q++) {
		String operatorCode = inboundSMSMessageResponses[q].getOperatorCode();
		String sestinationAddress = inboundSMSMessageResponses[q].getDestinationAddress();
		String messageId = inboundSMSMessageResponses[q].getMessageId();
		String inResourceURL = resourceURL.replace("registrations/","registrations/" + operatorCode + "/" + sestinationAddress+ "/") + "/" + messageId;
		inboundSMSMessageResponses[q].setResourceURL(inResourceURL);
	}
	nbRetrieveResponse.getInboundSMSMessageList().setInboundSMSMessage(inboundSMSMessageResponses);

	executor.removeHeaders(context);
	executor.setResponse(context, gson.toJson(nbRetrieveResponse));
	((Axis2MessageContext) context).getAxis2MessageContext().setProperty(
			"messageType", "application/json");

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
public boolean validate(String httpMethod, String requestPath,
		JSONObject jsonBody, MessageContext context) throws Exception {
	if (!httpMethod.equalsIgnoreCase("POST")) {
		((Axis2MessageContext) context).getAxis2MessageContext()
				.setProperty("HTTP_SC", 405);
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
