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

import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.dbutils.Operatorsubs;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.dao.SMSMessagingDAO;
import com.wso2telco.dep.mediator.entity.smsmessaging.southbound.SouthboundOutboundSubscriptionRequest;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.internal.Util;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.service.SMSMessagingService;
import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.service.impl.sms.ValidateCancelSubscription;
import com.wso2telco.oneapivalidation.service.impl.sms.ValidateOutboundSubscription;

// TODO: Auto-generated Javadoc
/**
 * The Class OutboundSMSSubscriptionsHandler.
 */
public class OutboundSMSSubscriptionsHandler implements SMSHandler {

	/** The log. */
	private static Log log = LogFactory.getLog(OutboundSMSSubscriptionsHandler.class);

	/** The Constant API_TYPE. */
	private static final String API_TYPE = "sms";

	/** The occi. */
	private OriginatingCountryCalculatorIDD occi;

	/** The smsMessagingDAO. */
	private SMSMessagingDAO smsMessagingDAO;

	/** The executor. */
	private SMSExecutor executor;

	/**
	 * Instantiates a new outbound sms subscriptions handler.
	 *
	 * @param executor
	 *            the executor
	 */
	public OutboundSMSSubscriptionsHandler(SMSExecutor executor) {

		this.executor = executor;
		occi = new OriginatingCountryCalculatorIDD();
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
		if (executor.getHttpMethod().equalsIgnoreCase("POST")) {
			return createSubscriptions(context);
		}else if (executor.getHttpMethod().equalsIgnoreCase("DELETE")) {
			           return deleteSubscriptions(context);
			        }
		return false;
	}

	@Override
	public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context)
			throws Exception {
		context.setProperty("mife.prop.operationType", 205);
		IServiceValidate validator;
		if (httpMethod.equalsIgnoreCase("POST")) {
			validator = new ValidateOutboundSubscription();
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
	private boolean createSubscriptions(MessageContext context)
			throws Exception {

		String requestid = UID.getUniqueID(Type.RETRIVSUB.getCode(), context,
				executor.getApplicationid());
		Gson gson = new GsonBuilder().serializeNulls().create();

		JSONObject jsonBody = executor.getJsonBody();
		JSONObject jsondstaddr = jsonBody
				.getJSONObject("deliveryReceiptSubscription");

		if (!jsondstaddr.isNull("filterCriteria")) {
			SouthboundOutboundSubscriptionRequest subsrequst = gson.fromJson(jsonBody.toString(),SouthboundOutboundSubscriptionRequest.class);
			String origNotiUrl = subsrequst.getDeliveryReceiptSubscription().getCallbackReference().getNotifyURL();

			List<OperatorEndpoint> endpoints = occi.getAPIEndpointsByApp(API_TYPE, executor.getSubResourcePath(),executor.getValidoperators());

			Integer axiataid = smsMessagingDAO.subscriptionEntry(subsrequst.getDeliveryReceiptSubscription().getCallbackReference().getNotifyURL());
			Util.getPropertyFile();
			String subsEndpoint = Util.getApplicationProperty("hubSubsGatewayEndpoint")+ "/"+ axiataid;
			jsondstaddr.getJSONObject("callbackReference").put("notifyURL",subsEndpoint);

			List<Operatorsubs> domainsubs = new ArrayList<Operatorsubs>();
			SouthboundOutboundSubscriptionRequest subsresponse = null;
			for (OperatorEndpoint endpoint : endpoints) {
				String notifyres = executor.makeRequest(endpoint, endpoint.getEndpointref().getAddress(), jsonBody.toString(),true, context);
				if (notifyres == null) {
					throw new CustomException("POL0299", "",new String[] { "Error registering subscription" });
				} else {
					subsresponse = gson.fromJson(notifyres,SouthboundOutboundSubscriptionRequest.class);
					if (subsrequst.getDeliveryReceiptSubscription() == null) {
						executor.handlePluginException(notifyres);
					}
					domainsubs.add(new Operatorsubs(endpoint.getOperator(),subsrequst.getDeliveryReceiptSubscription().getResourceURL()));

				}
			}

			boolean issubs = smsMessagingDAO.operatorsubsEntry(domainsubs,
					axiataid);
			String ResourceUrlPrefix = Util.getApplicationProperty("hubGateway");
			subsrequst.getDeliveryReceiptSubscription().setResourceURL(ResourceUrlPrefix + executor.getResourceUrl() + "/"+ axiataid);
			JSONObject replyobj = new JSONObject(subsresponse);
			JSONObject replysubs = replyobj.getJSONObject("deliveryReceiptSubscription");
			// String replydest = replysubs.getString("destinationAddress");

			/*
			 * JSONArray arradd = new JSONArray(); arradd.put(replydest);
			 * replysubs.put("destinationAddress", arradd);
			 */

			replysubs.getJSONObject("callbackReference").put("notifyURL",origNotiUrl);
			jsondstaddr.put("resourceURL",ResourceUrlPrefix + executor.getResourceUrl() + "/"+ axiataid);

			executor.removeHeaders(context);
			((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 201);
			executor.setResponse(context, replyobj.toString());
		} else {/*
				 * NBSubscribeRequest nbSubsrequst =
				 * gson.fromJson(jsonBody.toString(), NBSubscribeRequest.class);
				 * String origNotiUrl =
				 * nbSubsrequst.getSubscription().getCallbackReference
				 * ().getNotifyURL();
				 * 
				 * List<OperatorEndpoint> endpoints =
				 * occi.getAPIEndpointsByApp(API_TYPE,
				 * executor.getSubResourcePath(),executor.getValidoperators());
				 * 
				 * Integer axiataid =
				 * dbservice.subscriptionEntry(nbSubsrequst.getSubscription
				 * ().getCallbackReference().getNotifyURL());
				 * Util.getPropertyFile(); String subsEndpoint =
				 * Util.getApplicationProperty("hubSubsGatewayEndpoint") + "/" +
				 * axiataid;
				 * jsondstaddr.getJSONObject("callbackReference").put("notifyURL"
				 * , subsEndpoint);
				 * 
				 * 
				 * List<Operatorsubs> domainsubs = new
				 * ArrayList<Operatorsubs>(); SBOutboundSubscriptionRequest
				 * sbSubsresponse = null; DestinationAddresses[]
				 * destinationAddresses =
				 * nbSubsrequst.getSubscription().getDestinationAddresses();
				 * 
				 * for (OperatorEndpoint endpoint : endpoints) {
				 * 
				 * for (int i = 0; i < destinationAddresses.length; i++) { if
				 * (destinationAddresses
				 * [i].getOperatorCode().equalsIgnoreCase(endpoint
				 * .getOperator())) { SBOutboundSubscriptionRequest sbSubsrequst
				 * = new SBOutboundSubscriptionRequest(); Subscription sbrequest
				 * = new Subscription(); CallbackReference callbackReference =
				 * new CallbackReference();
				 * 
				 * callbackReference.setCallbackData(nbSubsrequst.getSubscription
				 * ().getCallbackReference().getCallbackData());
				 * callbackReference.setNotifyURL(subsEndpoint);
				 * sbrequest.setCallbackReference(callbackReference);
				 * 
				 * sbrequest.setNotificationFormat(nbSubsrequst.getSubscription()
				 * .getNotificationFormat());
				 * sbrequest.setCriteria(destinationAddresses[i].getCriteria());
				 * sbrequest.setDestinationAddress(destinationAddresses[i].
				 * getDestinationAddress());
				 * sbSubsrequst.setSubscription(sbrequest); String sbRequestBody
				 * = removeResourceURL(gson.toJson(sbSubsrequst)); String
				 * notifyres = executor.makeRequest(endpoint,
				 * endpoint.getEndpointref().getAddress(), sbRequestBody, true,
				 * context); if (notifyres == null) {
				 * destinationAddresses[i].setStatus("Failed"); } else {
				 * sbSubsresponse = gson.fromJson(notifyres,
				 * SBOutboundSubscriptionRequest.class); if
				 * (sbSubsresponse.getSubscription() == null) {
				 * destinationAddresses[i].setStatus("NotCreated"); } else {
				 * domainsubs.add(new Operatorsubs(endpoint.getOperator(),
				 * sbSubsresponse.getSubscription().getResourceURL()));
				 * destinationAddresses[i].setStatus("Created"); } } break; } }
				 * }
				 * 
				 * boolean issubs = dbservice.operatorsubsEntry(domainsubs,
				 * axiataid); String ResourceUrlPrefix =
				 * Util.getApplicationProperty("hubGateway");
				 * DestinationAddresses[] responseDestinationAddresses = new
				 * DestinationAddresses[destinationAddresses.length]; int
				 * destinationAddressesCount = 0; int successResultCount = 0;
				 * for (DestinationAddresses destinationAddressesResult :
				 * destinationAddresses) { String subscriptionStatus =
				 * destinationAddressesResult.getStatus(); if
				 * (subscriptionStatus == null) {
				 * destinationAddressesResult.setStatus("Failed"); } else if
				 * (subscriptionStatus.equals("Created")) {
				 * successResultCount++; }
				 * responseDestinationAddresses[destinationAddressesCount] =
				 * destinationAddressesResult; destinationAddressesCount++; }
				 * 
				 * if (successResultCount == 0) { throw new
				 * AxiataException("POL0299", "", new
				 * String[]{"Error registering subscription"}); }
				 * 
				 * nbSubsrequst.getSubscription().setDestinationAddresses(
				 * responseDestinationAddresses);
				 * nbSubsrequst.getSubscription().
				 * setResourceURL(ResourceUrlPrefix + executor.getResourceUrl()
				 * + "/" + axiataid);
				 * 
				 * nbSubsrequst.getSubscription().getCallbackReference().
				 * setNotifyURL(origNotiUrl);
				 * 
				 * String nbResponseBody = gson.toJson(nbSubsrequst);
				 * executor.removeHeaders(context); ((Axis2MessageContext)
				 * context).getAxis2MessageContext().setProperty("HTTP_SC",
				 * 201); executor.setResponse(context,
				 * nbResponseBody.toString());
				 */
		}
		return true;

	}
		private String removeResourceURL(String sbSubsrequst) {
			        String sbrequestString = "";
			        try {
			            JSONObject objJSONObject = new JSONObject(sbSubsrequst);
			           JSONObject objSubscriptionRequest = (JSONObject) objJSONObject.get("subscription");
			            objSubscriptionRequest.remove("resourceURL");
						           sbrequestString = objSubscriptionRequest.toString();
			        } catch (JSONException ex) {
			            throw new CustomException("POL0299", "", new String[]{"Error registering subscription"});
			        }
			        return "{\"subscription\":" + sbrequestString + "}";
			    }
	
	
	private boolean deleteSubscriptions(MessageContext context) throws Exception {
		        String requestPath = executor.getSubResourcePath();
		        String dnSubscriptionId = requestPath.substring(requestPath.lastIndexOf("/") + 1);
		
		        String requestid = UID.getUniqueID(Type.RETRIVSUB.getCode(), context, executor.getApplicationid());
		
		        List<Operatorsubs> domainsubs = smsMessagingDAO.subscriptionQuery(Integer.valueOf(dnSubscriptionId));
		        if (domainsubs.isEmpty()) {
		           throw new CustomException("POL0001", "", new String[]{"SMS Receipt Subscription Not Found: " + dnSubscriptionId});
		        }
		
		        String resStr = "";
		        for (Operatorsubs subs : domainsubs) {
		            resStr = executor.makeDeleteRequest(new OperatorEndpoint(new EndpointReference(subs.getDomain()), subs.getOperator()), subs.getDomain(), null, true, context);
		        }
		        new SMSMessagingService().subscriptionDelete(Integer.valueOf(dnSubscriptionId));
				        //JSONObject reply = new JSONObject();
		        //reply.put("response", "No content");
		
		        //replyGracefully(reply, context);
		       //routeToEndPoint(new OperatorEndpoint(new EndpointReference(sendResponse),null),context, sendResponse, "",
		        // "", "");
		        executor.removeHeaders(context);
		        ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 204);
		
		        return true;
		    }
}
