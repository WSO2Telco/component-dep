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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.dep.operatorservice.model.OperatorSubscriptionDTO;
import com.wso2telco.core.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.util.FileNames;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.entity.smsmessaging.southbound.SouthboundDeliveryReceiptSubscriptionRequest;
import com.wso2telco.dep.mediator.impl.smsmessaging.*;
import com.wso2telco.dep.mediator.internal.ApiUtils;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.service.SMSMessagingService;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.service.impl.smsmessaging.ValidateCancelSubscription;
import com.wso2telco.dep.oneapivalidation.service.impl.smsmessaging.southbound.ValidateSBOutboundSubscription;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;

import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.utils.CarbonUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class SBOutboundSMSSubscriptionsHandler.
 */
public class OutboundSMSSubscriptionsSouthboundHandler implements SMSHandler {

	/** The log. */
	private static Log log = LogFactory.getLog(OutboundSMSSubscriptionsSouthboundHandler.class);

	/** The Constant API_TYPE. */
	private static final String API_TYPE = "smsmessaging";

	/** The occi. */
	private OriginatingCountryCalculatorIDD occi;

	/** The smsMessagingDAO. */
	private SMSMessagingService smsMessagingService;

	/** The executor. */
	private SMSExecutor executor;

	/** The api utils. */
	private ApiUtils apiUtils;

	/**
	 * Instantiates a new SB outbound sms subscriptions handler.
	 *
	 * @param executor
	 *            the executor
	 */
	public OutboundSMSSubscriptionsSouthboundHandler(SMSExecutor executor) {
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
		if (executor.getHttpMethod().equalsIgnoreCase("POST")) {
			return createSubscriptions(context);
		} /*else if (executor.getHttpMethod().equalsIgnoreCase("DELETE")) {
			           return deleteSubscriptions(context);
			        }*/

		return false;
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
		// context.setProperty(DataPublisherConstants.OPERATION_TYPE, 205);
		IServiceValidate validator;
		if (httpMethod.equalsIgnoreCase("POST")) {
			validator = new ValidateSBOutboundSubscription();
			validator.validateUrl(requestPath);
			validator.validate(jsonBody.toString());
			return true;
		} else if (httpMethod.equalsIgnoreCase("DELETE")) {
			String dnSubscriptionId = requestPath.substring(requestPath.lastIndexOf("/") + 1);
			String[] params = { dnSubscriptionId };
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
	 * @param context
	 *            the context
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	private boolean createSubscriptions(MessageContext context) throws Exception {

		String requestid = UID.getUniqueID(Type.RETRIVSUB.getCode(), context, executor.getApplicationid());
		Gson gson = new GsonBuilder().serializeNulls().create();

		FileReader fileReader = new FileReader();
		String file = CarbonUtils.getCarbonConfigDirPath() + File.separator + FileNames.MEDIATOR_CONF_FILE.getFileName();
		
		Map<String, String> mediatorConfMap = fileReader.readPropertyFile(file);

		HashMap<String, String> jwtDetails = apiUtils.getJwtTokenDetails(context);
		JSONObject jsonBody = executor.getJsonBody();
		JSONObject jsondstaddr = jsonBody.getJSONObject("deliveryReceiptSubscription");
		//String orgclientcl = jsondstaddr.getString("clientCorrelator");
		String orgclientcl = "";
		if (!jsondstaddr.isNull("clientCorrelator")) {
			orgclientcl = jsondstaddr.getString("clientCorrelator");
		}

		String serviceProvider = jwtDetails.get("subscriber");
		log.debug("Subscriber Name : " + serviceProvider);

		String hubDNSubsGatewayEndpoint = mediatorConfMap.get("hubDNSubsGatewayEndpoint");
		log.debug("Hub / Gateway DN Notify URL : " + hubDNSubsGatewayEndpoint);
		
		SouthboundDeliveryReceiptSubscriptionRequest subsrequst = gson.fromJson(jsonBody.toString(),
				SouthboundDeliveryReceiptSubscriptionRequest.class);
		String origNotiUrl = subsrequst.getDeliveryReceiptSubscription().getCallbackReference().getNotifyURL();
		subsrequst.getDeliveryReceiptSubscription().setClientCorrelator(orgclientcl + ":" + requestid);

		List<OperatorEndpoint> endpoints = occi.getAPIEndpointsByApp(API_TYPE, executor.getSubResourcePath(),
				executor.getValidoperators());

		Integer dnSubscriptionId = smsMessagingService.outboundSubscriptionEntry(subsrequst.getDeliveryReceiptSubscription().getCallbackReference().getNotifyURL(), serviceProvider);
		String subsEndpoint = hubDNSubsGatewayEndpoint + "/" + dnSubscriptionId;
		//String subsEndpoint = mediatorConfMap.get("hubSubsGatewayEndpoint") + "/" + dnSubscriptionId;
		jsondstaddr.getJSONObject("callbackReference").put("notifyURL", subsEndpoint);
		
		subsrequst.getDeliveryReceiptSubscription().getCallbackReference().setNotifyURL(subsEndpoint);

		//String sbRequestBody = removeResourceURL(gson.toJson(subsrequst));
		
		List<OperatorSubscriptionDTO> domainsubs = new ArrayList<OperatorSubscriptionDTO>();
		SouthboundDeliveryReceiptSubscriptionRequest subsresponse = null;
		
		for (OperatorEndpoint endpoint : endpoints) {

			String url = endpoint.getEndpointref().getAddress();
			url = url.replace("/subscriptions", "/subscriptionsMultipleOperators");
			log.debug("Delivery notification adaptor request url of " + endpoint.getOperator() + " operator: " + url);

			String notifyres = executor.makeRequest(endpoint, url, jsonBody.toString(), true, context,false);

			if (notifyres == null) {

				throw new CustomException("POL0299", "", new String[] { "Error registering subscription" });
			} else {

				subsresponse = gson.fromJson(notifyres, SouthboundDeliveryReceiptSubscriptionRequest.class);
				if (subsrequst.getDeliveryReceiptSubscription() == null) {
					executor.handlePluginException(notifyres);
				}
				domainsubs.add(new OperatorSubscriptionDTO(endpoint.getOperator(),subsresponse.getDeliveryReceiptSubscription().getResourceURL()));
			}
		}

		smsMessagingService.outboundOperatorsubsEntry(domainsubs, dnSubscriptionId);
		String ResourceUrlPrefix = mediatorConfMap.get("hubGateway");
		subsresponse.getDeliveryReceiptSubscription().setResourceURL(ResourceUrlPrefix + executor.getResourceUrl() + "/" + dnSubscriptionId);
		JSONObject replyobj = new JSONObject(subsresponse);
		JSONObject replysubs = replyobj.getJSONObject("deliveryReceiptSubscription");
		replysubs.put("clientCorrelator", orgclientcl);
		replysubs.getJSONObject("callbackReference").put("notifyURL", origNotiUrl);
		jsondstaddr.put("resourceURL", ResourceUrlPrefix + executor.getResourceUrl() + "/" + dnSubscriptionId);

		executor.removeHeaders(context);
		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 201);
		executor.setResponse(context, replyobj.toString());

		return true;
	}

	/**
	 * Removes the resource url.
	 *
	 * @param sbSubsrequst
	 *            the sb subsrequst
	 * @return the string
	 */
	/*private String removeResourceURL(String sbSubsrequst) {
		String sbDeliveryNotificationrequestString = "";
		try {
			JSONObject objJSONObject = new JSONObject(sbSubsrequst);
			JSONObject objDeliveryNotificationRequest = (JSONObject) objJSONObject.get("deliveryReceiptSubscription");
			objDeliveryNotificationRequest.remove("resourceURL");

			sbDeliveryNotificationrequestString = objDeliveryNotificationRequest.toString();
		} catch (JSONException ex) {
			log.error("Error in removeResourceURL" + ex.getMessage());
			throw new CustomException("POL0299", "", new String[] { "Error registering subscription" });
		}
		return "{\"deliveryReceiptSubscription\":" + sbDeliveryNotificationrequestString + "}";
	}*/
	
	/*private boolean deleteSubscriptions(MessageContext context) throws Exception {
        String requestPath = executor.getSubResourcePath();
        String dnSubscriptionId = requestPath.substring(requestPath.lastIndexOf("/") + 1);

        String requestid = UID.getUniqueID(Type.DELRETSUB.getCode(), context, executor.getApplicationid());

        List<OperatorSubscriptionDTO> domainsubs = (smsMessagingService.outboudSubscriptionQuery(Integer.valueOf(dnSubscriptionId)));
        if (domainsubs.isEmpty()) {
            throw new CustomException("POL0001", "", new String[]{"SMS Receipt Subscription Not Found: " + dnSubscriptionId});
        }

        String resStr = "";
        for (OperatorSubscriptionDTO subs : domainsubs) {
			resStr = executor.makeDeleteRequest(
					new OperatorEndpoint(new EndpointReference(subs.getDomain()), subs.getOperator()), subs.getDomain(),
					null, true, context,false);
		}
        new SMSMessagingService().outboundSubscriptionDelete(Integer.valueOf(dnSubscriptionId));

       executor.removeHeaders(context);
        ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 204);

        return true;
    }*/
	
	
}
