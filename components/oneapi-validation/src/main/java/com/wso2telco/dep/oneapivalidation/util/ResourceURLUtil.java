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
package com.wso2telco.dep.oneapivalidation.util;


import org.json.JSONObject;

import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;

// TODO: Auto-generated Javadoc
/**
 * The Class ResourceURLUtil.
 */
public class ResourceURLUtil {

    /** The api type. */
    private String apiType = "";
    
    /** The process class. */
    private String processClass = "";

    /**
     * Gets the process class.
     *
     * @param ResourceURL the resource url
     * @param reqPayload the req payload
     * @return the process class
     */
    public String getProcessClass(String ResourceURL, String reqPayload) {

        String apiType = this.findAPIType(ResourceURL, reqPayload);

        if (apiType.equals("send_sms")) {
            processClass = "Route";
        } else if (apiType.equals("start_outbound_subscription")) {
            //TODO
            //processClass = "Forward";
        } else if (apiType.equals("stop_outbound_subscription")) {
            //TODO
            //processClass = "Forward";
        } else if (apiType.equals("query_sms")) {
            //TODO
            //processClass = "";
        } else if (apiType.equals("retrive_sms")) {
            processClass = "Forward";
        } else if (apiType.equals("retrive_sms_subscriptions")) {
            processClass = "Forward";
        } else if (apiType.equals("payment")) {
            processClass = "Redirect";
        } else if (apiType.equals("location")) {
            processClass = "Redirect";
        }
        else if (apiType.equals("sms_inbound_notifications")) {
            processClass = "Forward";
        } else {
            throw new CustomException("SVC0002", "",new String[]{null});
        }

        return processClass;
    }

    /**
     * Gets the API type.
     *
     * @param ResourceURL the resource url
     * @param jsonPayload the json payload
     * @return the API type
     */
    public String getAPIType(String ResourceURL, String jsonPayload) {
        String apiType = this.findAPIType(ResourceURL, jsonPayload);
        return apiType;
    }


    /**
     * Find api type.
     *
     * @param resourceURL the resource url
     * @param jsonPayload the json payload
     * @return the string
     */
    private String findAPIType(String resourceURL, String jsonPayload) {

        String paymentKeyString = "transactions";
        String sendSMSkeyString = "outbound";
        String sendSMSkeyStringRequest = "requests";
        String retriveSMSString = "inbound";
        String subscriptionKeyString = "subscriptions";
        String regKeyString = "registrations";
        String delivaryInfoKeyString = "deliveryInfos";
        String delivaryNotifyString = "DeliveryInfoNotification";
        String locationString = "location";
        String ussdKeyString = "ussd";


        String lastWord = resourceURL.substring(resourceURL.lastIndexOf("/") + 1);

        if (resourceURL.toLowerCase().contains(sendSMSkeyString.toLowerCase())
                && resourceURL.toLowerCase().contains(sendSMSkeyStringRequest.toLowerCase())
                && (!lastWord.equals(delivaryInfoKeyString))) {
            apiType = "send_sms";
        } else if (resourceURL.toLowerCase().contains(sendSMSkeyString.toLowerCase())
                && lastWord.equals(subscriptionKeyString)) {
            apiType = "start_delivery_subscription";
        } else if (resourceURL.toLowerCase().contains(sendSMSkeyString.toLowerCase())
                && resourceURL.toLowerCase().contains(subscriptionKeyString.toLowerCase())
                && (!lastWord.equals(subscriptionKeyString))) {
            apiType = "stop_delivery_subscription";
        } else if (lastWord.equals(delivaryInfoKeyString)) {
            apiType = "query_sms_delivery";
        } else if (resourceURL.toLowerCase().contains(retriveSMSString.toLowerCase())
                && resourceURL.toLowerCase().contains(regKeyString.toLowerCase())) {
            apiType = "retrive_sms";
        } else if (resourceURL.toLowerCase().contains(retriveSMSString.toLowerCase())
                && resourceURL.toLowerCase().contains(subscriptionKeyString.toLowerCase())) {
            apiType = "retrive_sms_subscriptions";
        } else if (resourceURL.toLowerCase().contains(paymentKeyString.toLowerCase())) {
            String paymentSubApi = findPaymentAPIType(resourceURL,jsonPayload);
            if(!paymentSubApi.equals("0")){
                if(paymentSubApi.equals("reserveAmount")){
                    apiType = "reserve_amount";
                }
                else if(paymentSubApi.equals("reserveAdditionalAmount")){
                    apiType = "reserve_additional_amount";
                }
                else if(paymentSubApi.equals("chargeAgainstReservation")){
                    apiType = "charge_against_reservation";
                }
                else if(paymentSubApi.equals("releaseReservation")){
                    apiType = "release_reservation";
                }
                else if(paymentSubApi.equals("charge")){
                    apiType = "charge";
                }   
                else if(paymentSubApi.equals("refund")){
                    apiType = "refund";
                }
                else if(paymentSubApi.equals("listTransactions")){
                    apiType = "list_transactions";
                }
                else {
                    throw new CustomException("SVC0002", "",new String[]{null});
                }
            } else {
                throw new CustomException("SVC0002", "",new String[]{null});
            }
        } else if (resourceURL.toLowerCase().contains(delivaryNotifyString.toLowerCase())) {
            apiType = "sms_inbound_notifications"; 
        } else if (resourceURL.toLowerCase().contains(locationString.toLowerCase())) {
            apiType = "location";
        } else if (resourceURL.toLowerCase().contains(ussdKeyString.toLowerCase())) {
            //apiType = "location";
            if(resourceURL.toLowerCase().contains("outbound")){
                apiType = "ussd_send";
            } else if (!resourceURL.toLowerCase().contains(subscriptionKeyString.toLowerCase())){
                apiType = "ussd_receive";
            } else {
                if (lastWord.equals(subscriptionKeyString.toLowerCase())){
                    apiType = "ussd_subscription";
                } else {
                    apiType = "stop_ussd_subscription";
                }
            }
            
        } else {
            throw new CustomException("SVC0002", "",new String[]{null});
        }

        return apiType;
    }
    
    /**
     * Find payment api type.
     *
     * @param requestURL the request url
     * @param jsonPayload the json payload
     * @return the string
     */
    private String findPaymentAPIType(String requestURL, String jsonPayload){
        String transactionOperationStatus = null;
        //String requestString = null;
        String apiType = null;

        if (requestURL.contains("amountReservation")) {
            String parts[] = requestURL.split("/transactions/");
            String urlParts[] = parts[1].split("/");

            if (urlParts.length == 1) {
                apiType = "reserveAmount";
            } else if (urlParts.length == 2) {
                JSONObject objJSONObject;
                try {
                    objJSONObject = new JSONObject(jsonPayload);
                    JSONObject objAmountReservationTransaction = (JSONObject) objJSONObject.get("amountReservationTransaction");

                    if (objAmountReservationTransaction.get("transactionOperationStatus") != null) {
                        transactionOperationStatus = nullOrTrimmed(objAmountReservationTransaction.get("transactionOperationStatus").toString());
                        if (transactionOperationStatus.equalsIgnoreCase("Reserved")) {
                            apiType = "reserveAdditionalAmount";
                        } else if (transactionOperationStatus.equalsIgnoreCase("Charged")) {
                            apiType = "chargeAgainstReservation";
                        } else if (transactionOperationStatus.equalsIgnoreCase("Released")) {
                            apiType = "releaseReservation";
                        } else {
                            //throw new Exception("API Type Not found");
                            apiType = "0";
                        }
                    } else {
                        //throw new Exception("API Type Not found");
                        apiType = "0";
                    }
                } catch (Exception ex) {
                    //logger.error("Sandbox controller - Manipulating recived JSON Object: " + ex);
                    //throw new Exception("API Type Not found");
                    throw new CustomException("SVC0002", "",new String[]{null});
                }
            } else {
                //throw new Exception("API Type Not found");
                apiType = "0";
            }
        } else if (requestURL.contains("amount")) {
            
            try {
                JSONObject objJSONObject = new JSONObject(jsonPayload);
                JSONObject objAmountTransaction = (JSONObject) objJSONObject.get("amountTransaction");

                if (objAmountTransaction.get("transactionOperationStatus") != null) {
                    transactionOperationStatus = nullOrTrimmed(objAmountTransaction.get("transactionOperationStatus").toString());
                    if (transactionOperationStatus.equalsIgnoreCase("Charged")) {
                        apiType = "charge";
                    } else if (transactionOperationStatus.equalsIgnoreCase("Refunded")) {
                        apiType = "refund";
                    } else {
                        apiType = "0";
                    }
                } else {
                    //throw new Exception("API Type Not found");
                    apiType = "0";
                }
            } catch (Exception e) {
                //logger.error("Sandbox controller - Manipulating recived JSON Object: " + e);
                //throw new Exception("API Type Not found");
                throw new CustomException("SVC0002", "",new String[]{null});
            }
        } else if (requestURL.contains("transactions")) {
            apiType = "listTransactions";
        } else {
            //throw new Exception("API Type Not found");
            apiType = "0";
        }
        return apiType;
    }
    
    /**
     * Null or trimmed.
     *
     * @param s the s
     * @return the string
     */
    private static String nullOrTrimmed(String s) {
        String rv = null;
        if (s != null && s.trim().length() > 0) {
            rv = s.trim();
        }
        return rv;
    }
}
