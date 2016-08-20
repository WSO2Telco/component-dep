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

import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.util.FileNames;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.service.USSDService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;
import org.wso2.carbon.utils.CarbonUtils;
import java.io.File;
import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class MOUSSDSubscribeHandler.
 */

public class MOUSSDSubscribeHandler implements USSDHandler {

	/** The log. */
	private Log log = LogFactory.getLog(MOUSSDSubscribeHandler.class);

	/** The Constant API_TYPE. */
	private static final String API_TYPE = "ussd";

	/** The occi. */
	private OriginatingCountryCalculatorIDD occi;

	/** The executor. */
	private USSDExecutor executor;

	/** The ussdDAO. */
	private USSDService ussdService;

	/**
	 * Instantiates a new MOUSSD subscribe handler.
	 *
	 * @param ussdExecutor
	 *            the ussd executor
	 */
	public MOUSSDSubscribeHandler(USSDExecutor ussdExecutor) {

		occi = new OriginatingCountryCalculatorIDD();
		this.executor = ussdExecutor;
		ussdService = new USSDService();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.mediator.impl.ussd.USSDHandler#handle(org.apache.synapse.
	 * MessageContext)
	 */
	@Override
	public boolean handle(MessageContext context) throws Exception {

		FileReader fileReader = new FileReader();
		String file = CarbonUtils.getCarbonConfigDirPath() + File.separator
				+ FileNames.MEDIATOR_CONF_FILE.getFileName();

		JSONObject jsonBody = executor.getJsonBody();
		String notifyUrl = jsonBody.getJSONObject("subscription").getJSONObject("callbackReference")
				.getString("notifyURL");

		Integer subscriptionId = ussdService.ussdRequestEntry(notifyUrl);

		Map<String, String> mediatorConfMap = fileReader.readPropertyFile(file);

		String subsEndpoint = mediatorConfMap.get("ussdGatewayEndpoint") + subscriptionId;

		jsonBody.getJSONObject("subscription").getJSONObject("callbackReference").put("notifyURL", subsEndpoint);

		List<OperatorEndpoint> endpoints = occi.getAPIEndpointsByApp(API_TYPE, executor.getSubResourcePath(),
				executor.getValidoperators());

		executor.removeHeaders(context);

		String responseStr = "";
		for (OperatorEndpoint endpoint : endpoints) {

			responseStr = executor.makeRequest(endpoint, endpoint.getEndpointref().getAddress(), jsonBody.toString(),
					true, context);
			executor.handlePluginException(responseStr);

		}

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

		return false;
	}
}
