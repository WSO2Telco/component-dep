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
package com.wso2telco.dep.mediator.impl.smsmessaging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.wso2telco.datapublisher.DataPublisherConstants;
import com.wso2telco.dep.mediator.MSISDNConstants;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.ResponseHandler;
import com.wso2telco.dep.mediator.entity.smsmessaging.QuerySMSStatusResponse;
import com.wso2telco.dep.mediator.internal.Util;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.service.SMSMessagingService;
import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.service.impl.sms.ValidateDeliveryStatus;
import com.wso2telco.subscriptionvalidator.util.ValidatorUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class QuerySMSStatusHandler.
 */
public class QuerySMSStatusHandler implements SMSHandler {

	/** The log. */
	private Log log = LogFactory.getLog(QuerySMSStatusHandler.class);

	/** The Constant API_TYPE. */
	private static final String API_TYPE = "sms";

	/** The occi. */
	private OriginatingCountryCalculatorIDD occi;

	/** The executor. */
	private SMSExecutor executor;

	/** The smsMessagingDAO. */
	private SMSMessagingService smsMessagingService;

	/** The response handler. */
	private ResponseHandler responseHandler;

	/** The sender address. */
	private String senderAddress = null;

	/** The request id. */
	private String requestId = null;

	/**
	 * Instantiates a new query sms status handler.
	 *
	 * @param executor
	 *            the executor
	 */
	public QuerySMSStatusHandler(SMSExecutor executor) {

		occi = new OriginatingCountryCalculatorIDD();
		this.executor = executor;
		smsMessagingService = new SMSMessagingService();
		responseHandler = new ResponseHandler();
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
		context.setProperty(DataPublisherConstants.OPERATION_TYPE, 202);
		if (!httpMethod.equalsIgnoreCase("GET")) {
			((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 405);
			throw new Exception("Method not allowed");
		}
		IServiceValidate validator = new ValidateDeliveryStatus();
		validator.validateUrl(requestPath);

		loadRequestParams();
		validator.validate(new String[] { senderAddress, requestId });

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.mediator.impl.sms.SMSHandler#handle(org.apache.synapse.
	 * MessageContext)
	 */
	@Override
	public boolean handle(MessageContext context) throws Exception {

		Map<String, String> requestIdMap = smsMessagingService.getSMSRequestIds(requestId, senderAddress);
		Map<String, QuerySMSStatusResponse> responseMap = sendStatusQueries(context, requestIdMap, senderAddress);
		if (Util.isAllNull(responseMap.values())) {
			throw new CustomException("SVC0001", "", new String[] { "Could not complete querying SMS statuses" });
		}
		executor.removeHeaders(context);
		String responsePayload = responseHandler.makeQuerySmsStatusResponse(context, senderAddress, requestId,
				responseMap);
		executor.setResponse(context, responsePayload);

		return true;
	}

	/**
	 * Send status queries.
	 *
	 * @param context
	 *            the context
	 * @param requestIdMap
	 *            the request id map
	 * @param senderAddr
	 *            the sender addr
	 * @return the map
	 * @throws Exception
	 *             the exception
	 */
	private Map<String, QuerySMSStatusResponse> sendStatusQueries(MessageContext context,
			Map<String, String> requestIdMap, String senderAddr) throws Exception {

		String resourcePathPrefix = "/outbound/" + URLEncoder.encode(senderAddr, "UTF-8") + "/requests/";
		Map<String, QuerySMSStatusResponse> statusResponses = new HashMap<String, QuerySMSStatusResponse>();
		for (Map.Entry<String, String> entry : requestIdMap.entrySet()) {
			String address = entry.getKey();
			String reqId = entry.getValue();

			if (reqId != null) {
				context.setProperty(MSISDNConstants.USER_MSISDN, address.substring(5));
				OperatorEndpoint endpoint = null;
				String resourcePath = resourcePathPrefix + reqId + "/deliveryInfos";
				if (ValidatorUtils.getValidatorForSubscription(context).validate(context)) {
					endpoint = occi.getAPIEndpointsByMSISDN(address.replace("tel:", ""), API_TYPE, resourcePath, true,
							executor.getValidoperators());
				}
				String sending_add = endpoint.getEndpointref().getAddress();
				log.info("sending endpoint found: " + sending_add);

				String responseStr = executor.makeGetRequest(endpoint, sending_add, resourcePath, true, context);
				QuerySMSStatusResponse statusResponse = parseJsonResponse(responseStr);
				statusResponses.put(address, statusResponse);
			} else {
				statusResponses.put(address, null);
			}
		}
		return statusResponses;
	}

	/**
	 * Parses the json response.
	 *
	 * @param responseString
	 *            the response string
	 * @return the query sms status response
	 */
	private QuerySMSStatusResponse parseJsonResponse(String responseString) {

		Gson gson = new GsonBuilder().create();
		QuerySMSStatusResponse response;
		try {
			response = gson.fromJson(responseString, QuerySMSStatusResponse.class);
			if (response.getDeliveryInfoList() == null) {
				return null;
			}
		} catch (JsonSyntaxException e) {
			return null;
		}
		return response;
	}

	/**
	 * Load request params.
	 *
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	private void loadRequestParams() throws UnsupportedEncodingException {
		String reqPath = URLDecoder.decode(executor.getSubResourcePath().replace("+", "%2B"), "UTF-8");
		Pattern pattern = Pattern.compile("outbound/(.+?)/requests/(.+?)/");
		Matcher matcher = pattern.matcher(reqPath);
		while (matcher.find()) {
			senderAddress = matcher.group(1);
			requestId = matcher.group(2);
		}
	}
}
