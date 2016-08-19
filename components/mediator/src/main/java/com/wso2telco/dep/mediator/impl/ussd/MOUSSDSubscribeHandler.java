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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.entity.SubscriptionRequest;
import com.wso2telco.dep.mediator.internal.ApiUtils;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.service.USSDService;
import com.wso2telco.dep.operatorservice.model.OperatorSubscriptionDTO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;

import java.util.ArrayList;
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
	
	private ApiUtils apiUtils;

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
		apiUtils = new ApiUtils();
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
		Map<String, String> mediatorConfMap = fileReader.readMediatorConfFile();
		
		String requestid = UID.getUniqueID(Type.MO_USSD.getCode(), context, executor.getApplicationid());
		
		JSONObject jsonBody = executor.getJsonBody();
		String notifyUrl = jsonBody.getJSONObject("subscription").getJSONObject("callbackReference").getString("notifyURL");
		Gson gson = new GsonBuilder().serializeNulls().create();
		
		AuthenticationContext authContext = APISecurityUtils.getAuthenticationContext(context);
        String consumerKey = "";
        String userId="";
        if (authContext != null) {
            consumerKey = authContext.getConsumerKey();
            userId=authContext.getUsername();

        }
		
		//Integer subscriptionId = ussdService.ussdRequestEntry(notifyUrl,consumerKey);
		String operatorId="";
        Integer subscriptionId = ussdService.ussdRequestEntry(notifyUrl,consumerKey,operatorId,userId);
		log.info("created subscriptionId  -  " + subscriptionId);	

		String subsEndpoint = mediatorConfMap.get("ussdGatewayEndpoint") + subscriptionId;
		log.info("Subsendpoint - " +subsEndpoint);

		jsonBody.getJSONObject("subscription").getJSONObject("callbackReference").put("notifyURL", subsEndpoint);

		List<OperatorEndpoint> endpoints = occi.getAPIEndpointsByApp(API_TYPE, executor.getSubResourcePath(),executor.getValidoperators());

		executor.removeHeaders(context);

		String responseStr = "";
		SubscriptionRequest subscription_request;
		List<OperatorSubscriptionDTO> operatorsubses = new ArrayList<OperatorSubscriptionDTO>();
		
		for (OperatorEndpoint endpoint : endpoints) {
			operatorId=ussdService.getOperatorIdByOperator(endpoint.getOperator());
			ussdService.updateOperatorIdBySubscriptionId(subscriptionId,operatorId);
			responseStr = executor.makeRequest(endpoint, endpoint.getEndpointref().getAddress(), jsonBody.toString(),true, context,false);
			subscription_request = gson.fromJson(responseStr, SubscriptionRequest.class);
			operatorsubses.add(new OperatorSubscriptionDTO(endpoint.getOperator(), subscription_request.getSubscription().getResourceURL()));		
			executor.handlePluginException(responseStr);

		}
		JSONObject jObject  = new JSONObject(responseStr);
		jObject.getJSONObject("subscription").getJSONObject("callbackReference").put("notifyURL",notifyUrl);
		jObject.getJSONObject("subscription").put("resourceURL",mediatorConfMap.get("hubGateway")+executor.getResourceUrl()+"/"+subscriptionId);
		ussdService.moUssdSubscriptionEntry(operatorsubses, subscriptionId);
		String responseobj =jObject.toString();
		executor.setResponse(context, responseobj);
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
	public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context) throws Exception {

		return false;
	}
}
