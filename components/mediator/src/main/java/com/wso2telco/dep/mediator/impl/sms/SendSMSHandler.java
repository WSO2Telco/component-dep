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
package com.wso2telco.dep.mediator.impl.sms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.wso2telco.datapublisher.DataPublisherConstants;
import com.wso2telco.dbutils.AxataDBUtilException;
import com.wso2telco.dep.operatorservice.model.OperatorApplicationDTO;
import com.wso2telco.dep.mediator.MSISDNConstants;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.ResponseHandler;
import com.wso2telco.dep.mediator.dao.SMSMessagingDAO;
import com.wso2telco.dep.mediator.entity.SendSMSRequest;
import com.wso2telco.dep.mediator.entity.SendSMSResponse;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.internal.Util;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.service.impl.sms.ValidateSendSms;
import com.wso2telco.subscriptionvalidator.util.ValidatorUtils;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class SendSMSHandler.
 */
public class SendSMSHandler implements SMSHandler {

	/** The log. */
	private Log log = LogFactory.getLog(SendSMSHandler.class);

	/** The Constant API_TYPE. */
	private static final String API_TYPE = "sms";

	/** The occi. */
	private OriginatingCountryCalculatorIDD occi;

	/** The response handler. */
	private ResponseHandler responseHandler;

	/** The executor. */
	private SMSExecutor executor;

	/** The smsMessagingDAO. */
	private SMSMessagingDAO smsMessagingDAO;

	/**
	 * Instantiates a new send sms handler.
	 *
	 * @param executor
	 *            the executor
	 */
	public SendSMSHandler(SMSExecutor executor) {
		this.executor = executor;
		occi = new OriginatingCountryCalculatorIDD();
		responseHandler = new ResponseHandler();
		smsMessagingDAO = new SMSMessagingDAO();
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
		String requestid = UID.getUniqueID(Type.SMSSEND.getCode(), context, executor.getApplicationid());
		// append request id to client correlator
		JSONObject jsonBody = executor.getJsonBody();
		JSONObject clientclr = jsonBody.getJSONObject("outboundSMSMessageRequest");
		clientclr.put("clientCorrelator", clientclr.getString("clientCorrelator") + ":" + requestid);

		Gson gson = new GsonBuilder().serializeNulls().create();
		SendSMSRequest subsrequest = gson.fromJson(jsonBody.toString(), SendSMSRequest.class);
		String senderAddress = subsrequest.getOutboundSMSMessageRequest().getSenderAddress();

		if (!ValidatorUtils.getValidatorForSubscription(context).validate(context)) {
			throw new CustomException("SVC0001", "", new String[] { "Subscription Validation Unsuccessful" });
		}
		int smsCount = getSMSMessageCount(
				subsrequest.getOutboundSMSMessageRequest().getOutboundTextMessage().getMessage());
		context.setProperty(DataPublisherConstants.RESPONSE, String.valueOf(smsCount));

		Map<String, SendSMSResponse> smsResponses = smssendmulti(context, subsrequest,
				jsonBody.getJSONObject("outboundSMSMessageRequest").getJSONArray("address"), API_TYPE,
				executor.getValidoperators());
		if (Util.isAllNull(smsResponses.values())) {
			throw new CustomException("POL0257", "Message not delivered %1", new String[] {
					"Request failed. Errors " + "occurred while sending the request for all the destinations." });
		}
		// NB publish
		executor.removeHeaders(context);
		String resPayload = responseHandler.makeSmsSendResponse(context, jsonBody.toString(), smsResponses, requestid);
		storeRequestIDs(requestid, senderAddress, smsResponses);
		executor.setResponse(context, resPayload);
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

		if (!httpMethod.equalsIgnoreCase("POST")) {
			((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 405);
			throw new Exception("Method not allowed");
		}

		context.setProperty(DataPublisherConstants.OPERATION_TYPE, 200);

		IServiceValidate validator = new ValidateSendSms();
		validator.validateUrl(requestPath);
		validator.validate(jsonBody.toString());

		String senderName = jsonBody.getJSONObject("outboundSMSMessageRequest").optString("senderName");

		if (senderName.equals("") || senderName == null || senderName.length() < 10) {
			context.setProperty(DataPublisherConstants.MERCHANT_ID, "");
			context.setProperty(DataPublisherConstants.CATEGORY, "");
			context.setProperty(DataPublisherConstants.SUB_CATEGORY, "");
		} else {
			if (senderName.substring(0, 3).equals("000")) {
				context.setProperty(DataPublisherConstants.MERCHANT_ID, "");
			} else {
				context.setProperty(DataPublisherConstants.MERCHANT_ID, senderName.substring(0, 3));
			}

			if (senderName.substring(3, 6).equals("000")) {
				context.setProperty(DataPublisherConstants.CATEGORY, "");
			} else {
				context.setProperty(DataPublisherConstants.CATEGORY, senderName.substring(3, 6));
			}

			if (senderName.substring(6, 9).equals("000")) {
				context.setProperty(DataPublisherConstants.SUB_CATEGORY, "");
			} else {
				context.setProperty(DataPublisherConstants.SUB_CATEGORY, senderName.substring(6, 9));
			}
		}

		return true;
	}

	/**
	 * Smssendmulti.
	 *
	 * @param smsmc
	 *            the smsmc
	 * @param sendreq
	 *            the sendreq
	 * @param listaddr
	 *            the listaddr
	 * @param apitype
	 *            the apitype
	 * @param operators
	 *            the operators
	 * @return the map
	 * @throws Exception
	 *             the exception
	 */
	private Map<String, SendSMSResponse> smssendmulti(MessageContext smsmc, SendSMSRequest sendreq, JSONArray listaddr,
			String apitype, List<OperatorApplicationDTO> operators) throws Exception {

		OperatorEndpoint endpoint = null;
		String jsonStr;
		String address;
		Map<String, SendSMSResponse> smsResponses = new HashMap<String, SendSMSResponse>();
		for (int i = 0; i < listaddr.length(); i++) {

			SendSMSResponse sendSMSResponse = null;
			address = listaddr.getString(i);

			log.info("id : " + address);
			smsmc.setProperty(MSISDNConstants.USER_MSISDN, address.substring(5));
			endpoint = occi.getAPIEndpointsByMSISDN(address.replace("tel:", ""), apitype, executor.getSubResourcePath(),
					false, operators); // smsSend;

			List<String> sendAdr = new ArrayList<String>();
			sendAdr.add(address);
			sendreq.getOutboundSMSMessageRequest().setAddress(sendAdr);
			jsonStr = new Gson().toJson(sendreq);
			String sending_add = endpoint.getEndpointref().getAddress();
			log.info("sending endpoint found: " + sending_add);

			String responseStr = executor.makeRequest(endpoint, sending_add, jsonStr, true, smsmc);
			sendSMSResponse = parseJsonResponse(responseStr);

			smsResponses.put(address, sendSMSResponse);
		}
		return smsResponses;
	}

	/**
	 * Parses the json response.
	 *
	 * @param responseString
	 *            the response string
	 * @return the send sms response
	 */
	private SendSMSResponse parseJsonResponse(String responseString) {

		Gson gson = new GsonBuilder().create();
		SendSMSResponse smsResponse;
		try {
			smsResponse = gson.fromJson(responseString, SendSMSResponse.class);
			if (smsResponse.getOutboundSMSMessageRequest() == null) {
				return null;
			}
		} catch (JsonSyntaxException e) {
			log.error(e.getMessage(), e);
			return null;
		}
		return smsResponse;
	}

	/**
	 * Store request i ds.
	 *
	 * @param requestID
	 *            the request id
	 * @param senderAddress
	 *            the sender address
	 * @param smsResponses
	 *            the sms responses
	 * @throws Exception
	 */
	private void storeRequestIDs(String requestID, String senderAddress, Map<String, SendSMSResponse> smsResponses)
			throws Exception {
		Map<String, String> reqIdMap = new HashMap<String, String>(smsResponses.size());
		for (Map.Entry<String, SendSMSResponse> entry : smsResponses.entrySet()) {
			SendSMSResponse smsResponse = entry.getValue();
			String pluginReqId = null;
			if (smsResponse != null) {
				String resourceURL = smsResponse.getOutboundSMSMessageRequest().getResourceURL().trim();
				String[] segments = resourceURL.split("/");
				pluginReqId = segments[segments.length - 1];
			}
			reqIdMap.put(entry.getKey(), pluginReqId);
		}
		smsMessagingDAO.insertSmsRequestIds(requestID, senderAddress, reqIdMap);
	}

	/**
	 * Gets the SMS message count.
	 *
	 * @param textMessage
	 *            the text message
	 * @return the SMS message count
	 */
	private int getSMSMessageCount(String textMessage) {

		int smsCount = 0;
		try {
			int count = textMessage.length();
			log.debug("Character count of text message : " + count);
			if (count > 0) {
				int tempSMSCount = count / 160;

				int tempRem = count % 160;

				if (tempRem > 0) {
					tempSMSCount++;
				}
				smsCount = tempSMSCount;

			}
		} catch (Exception e) {
			log.error("error in getSMSMessageCharacterCount : " + e.getMessage());
			return 0;
		}

		log.debug("SMS count : " + smsCount);
		return smsCount;
	}
}
