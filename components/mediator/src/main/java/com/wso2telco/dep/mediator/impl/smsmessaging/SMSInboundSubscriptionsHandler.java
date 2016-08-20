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
import com.wso2telco.datapublisher.DataPublisherConstants;
import com.wso2telco.dep.operatorservice.model.OperatorSubscriptionDTO;
import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.util.FileNames;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.entity.smsmessaging.southbound.SouthboundSubscribeRequest;
import com.wso2telco.dep.mediator.internal.ApiUtils;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.service.SMSMessagingService;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.service.impl.smsmessaging.southbound.ValidateSBSubscription;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;
import org.wso2.carbon.utils.CarbonUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class RetrieveSMSSubscriptionsHandler.
 */
public class SMSInboundSubscriptionsHandler implements SMSHandler {

	/** The log. */
	private static Log log = LogFactory.getLog(SMSInboundSubscriptionsHandler.class);

	/** The Constant API_TYPE. */
	private static final String API_TYPE = "sms";

	/** The occi. */
	private OriginatingCountryCalculatorIDD occi;

	/** The smsMessagingDAO. */
	private SMSMessagingService smsMessagingService;

	/** The executor. */
	private SMSExecutor executor;

	/** The api utils. */
	private ApiUtils apiUtils;

	/**
	 * Instantiates a new retrieve sms subscriptions handler.
	 *
	 * @param executor
	 *            the executor
	 */
	public SMSInboundSubscriptionsHandler(SMSExecutor executor) {

		this.executor = executor;
		occi = new OriginatingCountryCalculatorIDD();
		smsMessagingService = new SMSMessagingService();
		apiUtils = new ApiUtils();
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

		String requestid = UID.getUniqueID(Type.RETRIVSUB.getCode(), context, executor.getApplicationid());
		Gson gson = new GsonBuilder().serializeNulls().create();

		FileReader fileReader = new FileReader();
		String file = CarbonUtils.getCarbonConfigDirPath() + File.separator
				+ FileNames.MEDIATOR_CONF_FILE.getFileName();

		Map<String, String> mediatorConfMap = fileReader.readPropertyFile(file);

		HashMap<String, String> jwtDetails = apiUtils.getJwtTokenDetails(context);
		JSONObject jsonBody = executor.getJsonBody();
		JSONObject jsondstaddr = jsonBody.getJSONObject("subscription");

		String orgclientcl = "";
		if (!jsondstaddr.isNull("clientCorrelator")) {
			orgclientcl = jsondstaddr.getString("clientCorrelator");
		}

		String serviceProvider = jwtDetails.get("subscriber");
		log.debug("subscriber Name : " + serviceProvider);

		SouthboundSubscribeRequest subsrequst = gson.fromJson(jsonBody.toString(), SouthboundSubscribeRequest.class);
		String origNotiUrl = subsrequst.getSubscription().getCallbackReference().getNotifyURL();

		List<OperatorEndpoint> endpoints = occi.getAPIEndpointsByApp(API_TYPE, executor.getSubResourcePath(),
				executor.getValidoperators());

		Integer moSubscriptionId = smsMessagingService
				.subscriptionEntry(subsrequst.getSubscription().getCallbackReference().getNotifyURL(), serviceProvider);

		String subsEndpoint = mediatorConfMap.get("hubSubsGatewayEndpoint") + "/" + moSubscriptionId;
		jsondstaddr.getJSONObject("callbackReference").put("notifyURL", subsEndpoint);

		jsondstaddr.put("clientCorrelator", orgclientcl + ":" + requestid);

		List<OperatorSubscriptionDTO> domainsubs = new ArrayList<OperatorSubscriptionDTO>();
		SouthboundSubscribeRequest subsresponse = null;

		for (OperatorEndpoint endpoint : endpoints) {

			String notifyres = executor.makeRequest(endpoint, endpoint.getEndpointref().getAddress(),
					jsonBody.toString(), true, context);

			if (notifyres == null) {

				throw new CustomException("POL0299", "", new String[] { "Error registering subscription" });
			} else {

				// plugin exception handling
				subsresponse = gson.fromJson(notifyres, SouthboundSubscribeRequest.class);

				if (subsresponse.getSubscription() == null) {

					executor.handlePluginException(notifyres);
				}

				domainsubs.add(new OperatorSubscriptionDTO(endpoint.getOperator(),
						subsresponse.getSubscription().getResourceURL()));
			}
		}

		smsMessagingService.operatorSubsEntry(domainsubs, moSubscriptionId);

		String ResourceUrlPrefix = mediatorConfMap.get("hubGateway");
		subsresponse.getSubscription()
				.setResourceURL(ResourceUrlPrefix + executor.getResourceUrl() + "/" + moSubscriptionId);

		JSONObject replyobj = new JSONObject(subsresponse);
		JSONObject replysubs = replyobj.getJSONObject("subscription");

		replysubs.put("clientCorrelator", orgclientcl);

		replysubs.getJSONObject("callbackReference").put("notifyURL", origNotiUrl);

		jsondstaddr.put("resourceURL", ResourceUrlPrefix + executor.getResourceUrl() + "/" + moSubscriptionId);

		executor.removeHeaders(context);
		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 201);
		executor.setResponse(context, replyobj.toString());

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

		context.setProperty(DataPublisherConstants.OPERATION_TYPE, 205);
		IServiceValidate validator;

		validator = new ValidateSBSubscription();
		validator.validateUrl(requestPath);
		validator.validate(jsonBody.toString());

		return true;
	}
}
