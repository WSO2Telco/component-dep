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
package com.wso2telco.dep.mediator.impl.ussd;

import com.wso2telco.datapublisher.DataPublisherConstants;
import com.wso2telco.dep.mediator.MSISDNConstants;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.dao.USSDDAO;
import com.wso2telco.dep.mediator.internal.Util;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.subscriptionvalidator.util.ValidatorUtils;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class SendUSSDHandler.
 */
public class SendUSSDHandler implements USSDHandler {

	/** The log. */
	private Log log = LogFactory.getLog(SendUSSDHandler.class);

	/** The Constant API_TYPE. */
	private static final String API_TYPE = "ussd";

	/** The occi. */
	private OriginatingCountryCalculatorIDD occi;

	/** The executor. */
	private USSDExecutor executor;

	/** The ussdDAO. */
	private USSDDAO ussdDAO;

	/**
	 * Instantiates a new send ussd handler.
	 *
	 * @param executor
	 *            the executor
	 */
	public SendUSSDHandler(USSDExecutor executor) {
		occi = new OriginatingCountryCalculatorIDD();
		this.executor = executor;
		ussdDAO = new USSDDAO();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.mediator.impl.ussd.USSDHandler#handle(org.apache.synapse.
	 * MessageContext)
	 */
	@Override
	public boolean handle(MessageContext context) throws CustomException, AxisFault, Exception {

		JSONObject jsonBody = executor.getJsonBody();
		String address = jsonBody.getJSONObject("outboundUSSDMessageRequest").getString("address");
		String notifyUrl = jsonBody.getJSONObject("outboundUSSDMessageRequest").getJSONObject("responseRequest")
				.getString("notifyURL");
		String msisdn = address.substring(5);

		String carbonHome = System.getProperty("user.dir");
		log.debug("conf carbonHome - " + carbonHome);
		String fileLocation = carbonHome + "/repository/conf/axiataMediator_conf.properties";

		Util.getPropertyFileByPath(fileLocation);

		Integer subscriptionId = ussdDAO.ussdRequestEntry(notifyUrl);

		String subsEndpoint = Util.propMap.get("ussdGatewayEndpoint") + subscriptionId;
		log.info("Subsendpoint - " + subsEndpoint);

		jsonBody.getJSONObject("outboundUSSDMessageRequest").getJSONObject("responseRequest").put("notifyURL",
				subsEndpoint);

		context.setProperty(MSISDNConstants.USER_MSISDN, msisdn);
		OperatorEndpoint endpoint = null;
		if (ValidatorUtils.getValidatorForSubscription(context).validate(context)) {
			endpoint = occi.getAPIEndpointsByMSISDN(address.replace("tel:", ""), API_TYPE,
					executor.getSubResourcePath(), false, executor.getValidoperators());
		}
		String sending_add = endpoint.getEndpointref().getAddress();
		log.info("sending endpoint found: " + sending_add);

		executor.removeHeaders(context);

		String responseStr = executor.makeRequest(endpoint, sending_add, jsonBody.toString(), true, context);
		executor.handlePluginException(responseStr);

		executor.setResponse(context, responseStr);
		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("messageType", "application/json");

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.mediator.impl.ussd.USSDHandler#validate(java.lang.String,
	 * java.lang.String, org.json.JSONObject, org.apache.synapse.MessageContext)
	 */
	@Override
	public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context)
			throws Exception {
		context.setProperty(DataPublisherConstants.OPERATION_TYPE, 400);
		if (!httpMethod.equalsIgnoreCase("POST")) {
			((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 405);
			throw new Exception("Method not allowed");
		}

		return true;
	}
}
