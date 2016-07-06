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
package com.wso2telco.dep.mediator.internal;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import com.wso2telco.oneapivalidation.exceptions.CustomException;

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
     * @return the process class
     */
    public String getProcessClass(String ResourceURL) {

        String apiType = this.findAPIType(ResourceURL);

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
        } else if (apiType.equals("sms_inbound_notifications")) {
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
     * @return the API type
     */
    public String getAPIType(String ResourceURL) {

        String apiType = this.findAPIType(ResourceURL);


        return apiType;
    }

    /**
     * Str_piece.
     *
     * @param str the str
     * @param separator the separator
     * @param index the index
     * @return the string
     */
    private String str_piece(String str, char separator, int index) {
        String str_result = "";
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == separator) {
                count++;
                if (count == index) {
                    break;
                }
            } else {
                if (count == index - 1) {
                    str_result += str.charAt(i);
                }
            }
        }
        return str_result;
    }

    /**
     * Find api type.
     *
     * @param ResourceURL the resource url
     * @return the string
     */
    private String findAPIType(String ResourceURL) {

        String paymentKeyString = "transactions";
        String sendSMSkeyString = "outbound";
        String sendSMSkeyStringRequest = "requests";
        String retriveSMSString = "inbound";
        String subscriptionKeyString = "subscriptions";
        String regKeyString = "registrations";
        String delivaryInfoKeyString = "deliveryInfos";
        String delivaryNotifyString = "DeliveryInfoNotification";
        String locationString = "location";


        String lastWord = ResourceURL.substring(ResourceURL.lastIndexOf("/") + 1);

        if (ResourceURL.toLowerCase().contains(sendSMSkeyString.toLowerCase())
                && ResourceURL.toLowerCase().contains(sendSMSkeyStringRequest.toLowerCase())
                && (!lastWord.equals(delivaryInfoKeyString))) {
            apiType = "send_sms";
        } else if (ResourceURL.toLowerCase().contains(sendSMSkeyString.toLowerCase())
                && lastWord.equals(subscriptionKeyString)) {
            apiType = "start_outbound_subscription";
        } else if (ResourceURL.toLowerCase().contains(sendSMSkeyString.toLowerCase())
                && ResourceURL.toLowerCase().contains(subscriptionKeyString.toLowerCase())
                && (!lastWord.equals(subscriptionKeyString))) {
            apiType = "stop_outbound_subscription";
        } else if (lastWord.equals(delivaryInfoKeyString)) {
            apiType = "query_sms";
        } else if (ResourceURL.toLowerCase().contains(retriveSMSString.toLowerCase())
                && ResourceURL.toLowerCase().contains(regKeyString.toLowerCase())) {
            apiType = "retrive_sms";
        } else if (ResourceURL.toLowerCase().contains(retriveSMSString.toLowerCase())
                && ResourceURL.toLowerCase().contains(subscriptionKeyString.toLowerCase())) {
            apiType = "retrive_sms_subscriptions";
        } else if (ResourceURL.toLowerCase().contains(paymentKeyString.toLowerCase())) {
            apiType = "payment";
        } else if (ResourceURL.toLowerCase().contains(delivaryNotifyString.toLowerCase())) {
            apiType = "sms_inbound_notifications";        
        } else if (ResourceURL.toLowerCase().contains(locationString.toLowerCase())) {
            apiType = "location";
        } else {
            throw new CustomException("SVC0002", "",new String[]{null});
        }

        return apiType;
    }
    
    /**
     * Gets the param values.
     *
     * @param url the url
     * @return the param values
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public String[] getParamValues(String url) throws UnsupportedEncodingException {
        
       List<String> paramlist  = new ArrayList();
        
        String[] urlParts = url.split("\\?");
        if (urlParts.length < 2) {
            paramlist.add(URLDecoder.decode(urlParts[1].split("=")[1],"UTF-8"));            
        } else {
            String query = urlParts[1];
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                String value = "";
                if (pair.length > 1) {
                    paramlist.add(URLDecoder.decode(pair[1],"UTF-8"));
                }
            }
        }
        
        return paramlist.toArray(new String[paramlist.size()]);
    }
   
}
