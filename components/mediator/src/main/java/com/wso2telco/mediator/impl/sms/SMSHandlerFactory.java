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
package com.wso2telco.mediator.impl.sms;

import com.wso2telco.mediator.impl.sms.nb.NBOutboundSMSSubscriptionsHandler;
import com.wso2telco.mediator.impl.sms.nb.NBRetrieveSMSHandler;
import com.wso2telco.mediator.impl.sms.sb.SBOutboundSMSSubscriptionsHandler;
import com.wso2telco.mediator.impl.sms.sb.SBRetrieveSMSHandler;
import com.wso2telco.oneapivalidation.exceptions.AxiataException;

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
     * @param ResourceURL the resource url
     * @param executor the executor
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
        } else if (ResourceURL.toLowerCase().contains(retrieveSMSString.toLowerCase())
                && ResourceURL.toLowerCase().contains(regKeyString.toLowerCase())) {
            apiType = RequestType.RETRIEVE_SMS;
            if (httpMethod.equalsIgnoreCase("post")) {
                handler = new NBRetrieveSMSHandler(executor);
                log.debug("Invoke NBRetrieveSMSHandler");
            } else {
                String[] resourceURLParts = ResourceURL.split("/");
                if (resourceURLParts.length == 6) {
                    handler = new SBRetrieveSMSHandler(executor);
                    log.debug("Invoke SBRetrieveSMSHandler");
                } else if (resourceURLParts.length == 5) {
                    handler = new RetrieveSMSHandler(executor);
                    log.debug("Invoke RetrieveSMSHandler");
                }
            }
            //handler = new RetrieveSMSHandler(executor);
        } else if (ResourceURL.toLowerCase().contains(retrieveSMSString.toLowerCase())
                && ResourceURL.toLowerCase().contains(subscriptionKeyString.toLowerCase())) {
            apiType = RequestType.RETRIEVE_SMS_SUBSCRIPTIONS;
            handler = new RetrieveSMSSubscriptionsHandler(executor);
        } else if (ResourceURL.toLowerCase().contains(deliveryNotifyString.toLowerCase())) {
            apiType = RequestType.SMS_INBOUND_NOTIFICATIONS;
            //handler = new SMSInboundNotificationsHandler(executor);

            handler = findSMSInboundNotificationsHandlerType(executor);

        } else if (ResourceURL.toLowerCase().contains(sendSMSKeyString.toLowerCase())
                && lastWord.equals(subscriptionKeyString)) {
            apiType = RequestType.START_OUTBOUND_SUBSCRIPTION;
            if (ResourceURL.toLowerCase().contains("/outbound/subscriptions")) {
                handler = new NBOutboundSMSSubscriptionsHandler(executor);
                log.debug("Invoke NBOutboundSMSSubscriptionsHandler");
            } else {
                handler = findDeliveryNotificationSubscriptionType(executor);
            }
            //handler = new OutboundSMSSubscriptionsHandler(executor);//ADDED
        } else if (ResourceURL.toLowerCase().contains(sendSMSKeyString.toLowerCase()) && ResourceURL.toLowerCase().contains(subscriptionKeyString.toLowerCase()) && (!lastWord.equals(subscriptionKeyString))) {
            apiType = RequestType.STOP_OUTBOUND_SUBSCRIPTION;
            handler = new StopOutboundSMSSubscriptionsHandler(executor);
        } else if (lastWord.equals(deliveryInfoKeyString)) {
            apiType = RequestType.QUERY_SMS;
            handler = new QuerySMSStatusHandler(executor);
        } else {
            throw new AxiataException("SVC0002", "", new String[]{null});
        }
//        return apiType;
        return handler;
    }

    /**
     * Find delivery notification subscription type.
     *
     * @param executor the executor
     * @return the SMS handler
     */
    private static SMSHandler findDeliveryNotificationSubscriptionType(SMSExecutor executor) {

        SMSHandler handler = null;
        try {
            JSONObject objJSONObject = executor.getJsonBody();
            JSONObject objDeliveryReceiptSubscription = objJSONObject.getJSONObject("deliveryReceiptSubscription");
            if (!objDeliveryReceiptSubscription.isNull("operatorCode")) {
                handler = new SBOutboundSMSSubscriptionsHandler(executor);
                log.debug("Invoke SBOutboundSMSSubscriptionsHandler");
            } else {
                handler = new OutboundSMSSubscriptionsHandler(executor);
                log.debug("Invoke OutboundSMSSubscriptionsHandler");
            }
        } catch (Exception e) {
            log.error("Error in findDeliveryNotificationSubscriptionType : " + e.getMessage());
            throw new AxiataException("SVC0002", "", new String[]{null});
        }

        return handler;
    }

    /**
     * Find sms inbound notifications handler type.
     *
     * @param executor the executor
     * @return the SMS handler
     */
    private static SMSHandler findSMSInboundNotificationsHandlerType(SMSExecutor executor) {
        SMSHandler handler = null;
        try {
            JSONObject objJSONObject = executor.getJsonBody();

            if (objJSONObject.isNull("inboundSMSMessageNotification")) {
                handler = new SMSOutboundNotificationsHandler(executor);
            } else if (objJSONObject.isNull("deliveryInfoNotification")) {
                handler = new SMSInboundNotificationsHandler(executor);
            }
             

        } catch (Exception e) {
            throw new AxiataException("SVC0005", "Error when selecting Delivery reciept handler", new String[]{null});
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
