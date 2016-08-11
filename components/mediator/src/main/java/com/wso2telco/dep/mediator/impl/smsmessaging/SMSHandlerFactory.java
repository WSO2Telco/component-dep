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

import com.wso2telco.dep.mediator.impl.smsmessaging.northbound.OutboundSMSSubscriptionsNorthboundHandler;
import com.wso2telco.dep.mediator.impl.smsmessaging.northbound.RetrieveSMSNorthboundHandler;
import com.wso2telco.dep.mediator.impl.smsmessaging.northbound.SMSInboundSubscriptionsNorthboundHandler;
import com.wso2telco.dep.mediator.impl.smsmessaging.southbound.OutboundSMSSubscriptionsSouthboundHandler;
import com.wso2telco.dep.mediator.impl.smsmessaging.southbound.RetrieveSMSSouthboundHandler;
import com.wso2telco.oneapivalidation.exceptions.CustomException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating SMSHandler objects.
 */
public class SMSHandlerFactory {

	/** The log. */
	private static Log log = LogFactory.getLog(SMSHandlerFactory.class);

	/**
	 * Creates a new SMSHandler object.
	 *
	 * @param ResourceURL
	 *            the resource url
	 * @param executor
	 *            the executor
	 * @return the SMS handler
	 */
	public static SMSHandler createHandler(String ResourceURL, SMSExecutor executor) {

		String sendSMSKeyString = "outbound";
		String sendSMSKeyStringRequest = "requests";
		String retrieveSMSString = "inbound";
		String subscriptionKeyString = "subscriptions";
		String regKeyString = "registrations";
		String deliveryInfoKeyString = "deliveryInfos";
		String deliveryNotifyString = "DeliveryInfoNotification";

		String lastWord = ResourceURL.substring(ResourceURL.lastIndexOf("/") + 1);
		RequestType apiType;
		SMSHandler handler = null;
		String httpMethod = executor.getHttpMethod();

		if (ResourceURL.toLowerCase().contains(sendSMSKeyString.toLowerCase())
				&& ResourceURL.toLowerCase().contains(sendSMSKeyStringRequest.toLowerCase())
				&& (!lastWord.equals(deliveryInfoKeyString))) {

			apiType = RequestType.SEND_SMS;
			handler = new SendSMSHandler(executor);
			log.debug("invoking send sms handler");
		} else if (ResourceURL.toLowerCase().contains(retrieveSMSString.toLowerCase())
				&& ResourceURL.toLowerCase().contains(regKeyString.toLowerCase())) {

			apiType = RequestType.RETRIEVE_SMS;
			if (httpMethod.equalsIgnoreCase("post")) {

				handler = new RetrieveSMSNorthboundHandler(executor);
				log.debug("invoking retrieve sms northbound handler");
			} else {

				String[] resourceURLParts = ResourceURL.split("/");
				if (resourceURLParts.length == 6) {

					handler = new RetrieveSMSSouthboundHandler(executor);
					log.debug("invoking retrieve sms southbound handler");
				} else if (resourceURLParts.length == 5) {

					handler = new RetrieveSMSHandler(executor);
					log.debug("invoking retrieve sms handler");
				}
			}
		} else if (ResourceURL.toLowerCase().contains(retrieveSMSString.toLowerCase())
				&& ResourceURL.toLowerCase().contains(subscriptionKeyString.toLowerCase())) {

			apiType = RequestType.RETRIEVE_SMS_SUBSCRIPTIONS;
			if (httpMethod.equalsIgnoreCase("delete")) {

				handler = new StopInboundSMSSubscriptionsHandler(executor);
				log.debug("invoking stop inbound sms subscriptions handler");
			} else {

				handler = findInboundNotificationSubscriptionsHandlerType(executor);
			}
		} else if (ResourceURL.toLowerCase().contains(deliveryNotifyString.toLowerCase())) {

			apiType = RequestType.SMS_INBOUND_NOTIFICATIONS;
			//handler = findSMSInboundNotificationsHandlerType(executor);
			handler = findSMSInboundNotificationsHandlerType(executor);
		} else if (ResourceURL.toLowerCase().contains(sendSMSKeyString.toLowerCase())
				&& lastWord.equals(subscriptionKeyString)) {

			apiType = RequestType.START_OUTBOUND_SUBSCRIPTION;
			//handler = new OutboundSMSSubscriptionsHandler(executor);//ADDED
			if (ResourceURL.toLowerCase().contains("/outbound/subscriptions")) {

				handler = new OutboundSMSSubscriptionsNorthboundHandler(executor);
				log.debug("invoking outbound sms subscriptions NORTHBOUND HANDLER");
			} else {

				handler = findDeliveryNotificationSubscriptionsHandlerType(executor);
			}
		} else if (ResourceURL.toLowerCase().contains(sendSMSKeyString.toLowerCase())
				&& ResourceURL.toLowerCase().contains(subscriptionKeyString.toLowerCase())
				&& (!lastWord.equals(subscriptionKeyString))) {

			apiType = RequestType.STOP_OUTBOUND_SUBSCRIPTION;
			handler = new StopOutboundSMSSubscriptionsHandler(executor);
			log.debug("invoking stop outbound sms subscriptions handler");
		} else if (lastWord.equals(deliveryInfoKeyString)) {

			apiType = RequestType.QUERY_SMS;
			handler = new QuerySMSStatusHandler(executor);
			log.debug("invoking query sms status handler");
		} else {

			throw new CustomException("SVC0002", "", new String[] { null });
		}

		return handler;
	}

	/**
	 * Find delivery notification subscription type.
	 *
	 * @param executor
	 *            the executor
	 * @return the SMS handler
	 */
	private static SMSHandler findDeliveryNotificationSubscriptionsHandlerType(SMSExecutor executor) {

		SMSHandler handler = null;

		try {

			JSONObject objJSONObject = executor.getJsonBody();
			JSONObject objDeliveryReceiptSubscription = objJSONObject.getJSONObject("deliveryReceiptSubscription");

			if (!objDeliveryReceiptSubscription.isNull("operatorCode")) {

				handler = new OutboundSMSSubscriptionsSouthboundHandler(executor);
				log.debug("invoking outbound sms subscriptions southbound handler");
			} else {

				handler = new OutboundSMSSubscriptionsHandler(executor);
				log.debug("invoking outbound sms subscriptions handler");
			}
		} catch (Exception e) {

			log.error("error in findDeliveryNotificationSubscriptionsHandlerType : " + e.getMessage());
			throw new CustomException("SVC0002", "", new String[] { null });
		}

		return handler;
	}

	private static SMSHandler findInboundNotificationSubscriptionsHandlerType(SMSExecutor executor) {

		SMSHandler handler = null;

		try {

			JSONObject objJSONObject = executor.getJsonBody();
			JSONObject objSubscription = objJSONObject.getJSONObject("subscription");

			if (!objSubscription.isNull("criteria")) {

				handler = new SMSInboundSubscriptionsHandler(executor);
				log.debug("invoking sms inbound subscriptions handler");
			} else {

				handler = new SMSInboundSubscriptionsNorthboundHandler(executor);
				log.debug("invoking sms inbound sms subscriptions northbound handler");
			}
		} catch (Exception e) {

			log.error("error in findInboundNotificationSubscriptionsHandlerType : " + e.getMessage());
			throw new CustomException("SVC0002", "", new String[] { null });
		}

		return handler;
	}

	/**
	 * Find sms inbound notifications handler type.
	 *
	 * @param executor
	 *            the executor
	 * @return the SMS handler
	 */
	private static SMSHandler findSMSInboundNotificationsHandlerType(SMSExecutor executor) {

		SMSHandler handler = null;

		try {

			JSONObject objJSONObject = executor.getJsonBody();
			JSONObject objInboundNotificationsHandler = objJSONObject.getJSONObject("deliveryInfoNotification");
			if (objInboundNotificationsHandler!=null) {
				            	handler = new SMSOutboundNotificationsHandler(executor);
				            } else {
				            	handler = new SMSInboundNotificationsHandler(executor); 
				            }
			/*if (objJSONObject.isNull("inboundSMSMessageNotification")) {

				handler = new SMSOutboundNotificationsHandler(executor);
				log.debug("invoking sms outbound notifications handler");
			} else if (objJSONObject.isNull("deliveryInfoNotification")) {

				handler = new SMSInboundNotificationsHandler(executor);
				log.debug("invoking sms inbound notifications handler");
			}*/
		} catch (Exception e) {

			//log.error("error in findSMSInboundNotificationsHandlerType : " + e.getMessage());
			throw new CustomException("SVC0005", "Error when selecting Delivery reciept handler",new String[] { null });
		}

		return handler;
	}

	/**
	 * The Enum RequestType.
	 */
	private enum RequestType {

		/** The send sms. */
		SEND_SMS,

		/** The retrieve sms. */
		RETRIEVE_SMS,

		/** The retrieve sms subscriptions. */
		RETRIEVE_SMS_SUBSCRIPTIONS,

		/** The sms inbound notifications. */
		SMS_INBOUND_NOTIFICATIONS,

		/** The start outbound subscription. */
		START_OUTBOUND_SUBSCRIPTION,

		/** The stop outbound subscription. */
		STOP_OUTBOUND_SUBSCRIPTION,

		/** The query sms. */
		QUERY_SMS
	}
}
