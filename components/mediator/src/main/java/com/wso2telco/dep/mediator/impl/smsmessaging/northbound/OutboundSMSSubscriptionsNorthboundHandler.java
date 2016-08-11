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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.dbutils.Operatorsubs;
import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.entity.smsmessaging.CallbackReference;
import com.wso2telco.dep.mediator.entity.smsmessaging.northbound.NorthboundDeliveryReceiptSubscriptionRequest;
import com.wso2telco.dep.mediator.entity.smsmessaging.northbound.SenderAddresses;
import com.wso2telco.dep.mediator.entity.smsmessaging.southbound.DeliveryReceiptSubscription;
import com.wso2telco.dep.mediator.entity.smsmessaging.southbound.SouthboundDeliveryReceiptSubscriptionRequest;
import com.wso2telco.dep.mediator.entity.smsmessaging.southbound.SouthboundOutboundSubscriptionRequest;
import com.wso2telco.dep.mediator.impl.smsmessaging.SMSExecutor;
import com.wso2telco.dep.mediator.impl.smsmessaging.SMSHandler;
import com.wso2telco.dep.mediator.internal.ApiUtils;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.service.SMSMessagingService;
import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.service.impl.sms.ValidateCancelSubscription;
import com.wso2telco.oneapivalidation.service.impl.sms.nb.ValidateNBOutboundSubscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.wso2telco.dep.operatorservice.model.OperatorSubscriptionDTO;

// TODO: Auto-generated Javadoc
/**
 * The Class NBOutboundSMSSubscriptionsHandler.
 */
public class OutboundSMSSubscriptionsNorthboundHandler implements SMSHandler {

	/** The log. */
	private static Log log = LogFactory.getLog(OutboundSMSSubscriptionsNorthboundHandler.class);

	/** The Constant API_TYPE. */
	private static final String API_TYPE = "sms";

	/** The occi. */
	private OriginatingCountryCalculatorIDD occi;

	/** The smsMessagingDAO. */
	private SMSMessagingService smsMessagingService;

	/** The executor. */
	private SMSExecutor executor;
/*
	*//** The api utils. *//*
	private ApiUtils apiUtils;*/

	/**
	 * Instantiates a new NB outbound sms subscriptions handler.
	 *
	 * @param executor
	 *            the executor
	 */
	public OutboundSMSSubscriptionsNorthboundHandler(SMSExecutor executor) {
		this.executor = executor;
		occi = new OriginatingCountryCalculatorIDD();
		smsMessagingService = new SMSMessagingService();
	//	apiUtils = new ApiUtils();
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
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		
		
		/*
		IServiceValidate validator;
		if (httpMethod.equalsIgnoreCase("POST")) {
			validator = new ValidateNBOutboundSubscription();
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
	*/}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.mediator.impl.sms.SMSHandler#handle(org.apache.synapse.
	 * MessageContext)
	 */
	@Override
	public boolean handle(MessageContext context) throws Exception {
		
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		/*if (executor.getHttpMethod().equalsIgnoreCase("POST")) {
			return createSubscriptions(context);
		}

		return false;*/
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
		Map<String, String> mediatorConfMap = fileReader.readMediatorConfFile();

		//HashMap<String, String> jwtDetails = apiUtils.getJwtTokenDetails(context);
		JSONObject jsonBody = executor.getJsonBody();
		JSONObject jsondstaddr = jsonBody.getJSONObject("deliveryReceiptSubscription");

		//String serviceProvider = jwtDetails.get("subscriber");
		//log.debug("Subscriber Name : " + serviceProvider);
		
		SouthboundOutboundSubscriptionRequest subsrequst = gson.fromJson(jsonBody.toString(), SouthboundOutboundSubscriptionRequest.class);

/*		NorthboundDeliveryReceiptSubscriptionRequest nbDeliveryReceiptSubscriptionRequest = gson.fromJson(jsonBody.toString(),
				NorthboundDeliveryReceiptSubscriptionRequest.class);*/
		/*String orgclientcl = nbDeliveryReceiptSubscriptionRequest.getDeliveryReceiptSubscription()
				.getClientCorrelator();*/

		String origNotiUrl = subsrequst.getDeliveryReceiptSubscription().getCallbackReference().getNotifyURL();

		List<OperatorEndpoint> endpoints = occi.getAPIEndpointsByApp(API_TYPE, executor.getSubResourcePath(),executor.getValidoperators());

		Integer dnSubscriptionId = smsMessagingService.subscriptionEntry(subsrequst
				.getDeliveryReceiptSubscription().getCallbackReference().getNotifyURL()/*, serviceProvider*/);
		
		String subsEndpoint = mediatorConfMap.get("hubSubsGatewayEndpoint") + "/" + dnSubscriptionId;
		jsondstaddr.getJSONObject("callbackReference").put("notifyURL", subsEndpoint);
		
		
		/*nbDeliveryReceiptSubscriptionRequest.getDeliveryReceiptSubscription().getCallbackReference()
				.setNotifyURL(subsEndpoint);
		log.debug("Delivery notification subscription northbound request body : "
				+ gson.toJson(nbDeliveryReceiptSubscriptionRequest));*/
/*
		SenderAddresses[] senderAddresses = nbDeliveryReceiptSubscriptionRequest.getDeliveryReceiptSubscription()
				.getSenderAddresses();*/

		/*List<OperatorSubscriptionDTO> domainsubs = new ArrayList<OperatorSubscriptionDTO>();
		SouthboundDeliveryReceiptSubscriptionRequest sbDeliveryReceiptSubscriptionResponse = null;*/
		
		List<Operatorsubs> domainsubs = new ArrayList<Operatorsubs>();
		SouthboundOutboundSubscriptionRequest subsresponse = null;

		for (OperatorEndpoint endpoint : endpoints) {
			
			String notifyres = executor.makeRequest(endpoint, endpoint.getEndpointref().getAddress(), jsonBody.toString(), true, context);
			                if (notifyres == null) {
			                    throw new CustomException("POL0299", "", new String[]{"Error registering subscription"});
			                } else {
			                    subsresponse = gson.fromJson(notifyres, SouthboundOutboundSubscriptionRequest.class);
			                    if (subsrequst.getDeliveryReceiptSubscription() == null) {
			                        executor.handlePluginException(notifyres);
			                    }
			                    domainsubs.add(new Operatorsubs(endpoint.getOperator(), subsrequst.getDeliveryReceiptSubscription().getResourceURL()));
			                }
			

		/*	for (int i = 0; i < senderAddresses.length; i++) {
				if (senderAddresses[i].getOperatorCode().equalsIgnoreCase(endpoint.getOperator())) {
					log.debug("Operator name: " + endpoint.getOperator());
					SouthboundDeliveryReceiptSubscriptionRequest sbDeliveryReceiptSubscriptionRequest = new SouthboundDeliveryReceiptSubscriptionRequest();
					DeliveryReceiptSubscription deliveryReceiptSubscriptionRequest = new DeliveryReceiptSubscription();
					CallbackReference callbackReference = new CallbackReference();

					callbackReference.setCallbackData(nbDeliveryReceiptSubscriptionRequest
							.getDeliveryReceiptSubscription().getCallbackReference().getCallbackData());
					callbackReference.setNotifyURL(subsEndpoint);
					deliveryReceiptSubscriptionRequest.setCallbackReference(callbackReference);
					//deliveryReceiptSubscriptionRequest.setClientCorrelator(orgclientcl + ":" + requestid);
					//deliveryReceiptSubscriptionRequest.setOperatorCode(senderAddresses[i].getOperatorCode());
					deliveryReceiptSubscriptionRequest.setFilterCriteria(senderAddresses[i].getFilterCriteria());
					sbDeliveryReceiptSubscriptionRequest
							.setDeliveryReceiptSubscription(deliveryReceiptSubscriptionRequest);

					String sbRequestBody = removeResourceURL(gson.toJson(sbDeliveryReceiptSubscriptionRequest));
					log.debug("Delivery notification southbound request body of " + endpoint.getOperator()
							+ " operator: " + sbRequestBody);

					String url = endpoint.getEndpointref().getAddress();
					String southboundURLPart = "/" + senderAddresses[i].getSenderAddress() + "/subscriptions";
					url = url.replace("/subscriptions", southboundURLPart);
					log.debug("Delivery notification southbound request url of " + endpoint.getOperator()
							+ " operator: " + url);

					String notifyres = executor.makeRequest(endpoint, url, sbRequestBody, true, context);

					log.debug("Delivery notification southbound response body of " + endpoint.getOperator()
							+ " operator: " + notifyres);

					if (notifyres == null) {
						senderAddresses[i].setStatus("Failed");

					} else {
						// plugin exception handling
						sbDeliveryReceiptSubscriptionResponse = gson.fromJson(notifyres,
								SouthboundDeliveryReceiptSubscriptionRequest.class);
						if (sbDeliveryReceiptSubscriptionResponse.getDeliveryReceiptSubscription() == null) {
							senderAddresses[i].setStatus("NotCreated");
						} else {
							domainsubs.add(new OperatorSubscriptionDTO(endpoint.getOperator(),
									sbDeliveryReceiptSubscriptionResponse.getDeliveryReceiptSubscription()
											.getResourceURL()));
							senderAddresses[i].setStatus("Created");
						}
					}
					break;
				}
			}*/
		}
		
		
		//boolean issubs = smsMessagingService.outboundOperatorsubsEntry(domainsubs, dnSubscriptionId);
		String ResourceUrlPrefix = mediatorConfMap.get("hubGateway");
		subsrequst.getDeliveryReceiptSubscription().setResourceURL(ResourceUrlPrefix + executor.getResourceUrl() + "/"+ dnSubscriptionId);
		JSONObject replyobj = new JSONObject(subsresponse);
		JSONObject replysubs = replyobj.getJSONObject("deliveryReceiptSubscription");
		// String replydest = replysubs.getString("destinationAddress");

		/*
		 * JSONArray arradd = new JSONArray(); arradd.put(replydest);
		 * replysubs.put("destinationAddress", arradd);
		 */

		replysubs.getJSONObject("callbackReference").put("notifyURL",origNotiUrl);
		jsondstaddr.put("resourceURL",ResourceUrlPrefix + executor.getResourceUrl() + "/"+ dnSubscriptionId);

		executor.removeHeaders(context);
		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 201);
		executor.setResponse(context, replyobj.toString());

		return true;
		/*smsMessagingService.outboundOperatorsubsEntry(domainsubs, dnSubscriptionId);
		String ResourceUrlPrefix = mediatorConfMap.get("hubGateway");

		SenderAddresses[] responseSenderAddresses = new SenderAddresses[senderAddresses.length];
		int senderAddressesCount = 0;
		int successResultCount = 0;
		for (SenderAddresses sendernAddressesResult : senderAddresses) {
			String deliverySubscriptionStatus = sendernAddressesResult.getStatus();
			if (deliverySubscriptionStatus == null) {
				sendernAddressesResult.setStatus("Failed");
			} else if (deliverySubscriptionStatus.equals("Created")) {
				successResultCount++;
			}
			responseSenderAddresses[senderAddressesCount] = sendernAddressesResult;
			senderAddressesCount++;
		}

		if (successResultCount == 0) {
			throw new CustomException("POL0299", "", new String[] { "Error registering subscription" });
		}

		nbDeliveryReceiptSubscriptionRequest.getDeliveryReceiptSubscription()
				.setSenderAddresses(responseSenderAddresses);
		nbDeliveryReceiptSubscriptionRequest.getDeliveryReceiptSubscription()
				.setResourceURL(ResourceUrlPrefix + executor.getResourceUrl() + "/" + dnSubscriptionId);
		nbDeliveryReceiptSubscriptionRequest.getDeliveryReceiptSubscription().getCallbackReference()
				.setNotifyURL(origNotiUrl);

		String nbDeliveryReceiptSubscriptionResponseBody = gson.toJson(nbDeliveryReceiptSubscriptionRequest);
		log.debug("Delivery notification subscription northbound response body : "
				+ nbDeliveryReceiptSubscriptionResponseBody);

		executor.removeHeaders(context);
		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 201);
		executor.setResponse(context, nbDeliveryReceiptSubscriptionResponseBody);

		return true;*/
	}

	/**
	 * Removes the resource url.
	 *
	 * @param sbSubsrequst
	 *            the sb subsrequst
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
			throw new CustomException("POL0299", "", new String[] { "Error registering subscription" });
		}

		return "{\"deliveryReceiptSubscription\":" + sbDeliveryNotificationrequestString + "}";
	}
}
