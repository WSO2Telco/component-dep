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
package com.wso2telco.dep.oneapivalidation.service.impl.smsmessaging;

import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.util.UrlValidator;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

 
// TODO: Auto-generated Javadoc
/**
 * The Class ValidateSendSms.
 */
public class ValidateSendSms implements IServiceValidate {

    /** The validation rules. */
    private final String[] validationRules = {"outbound", "*", "requests"};

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String)
     */
    public void validate(String json) throws CustomException {

        List<String> addresses = new ArrayList<String>(); // Note there can be multiple addresses specified
        String senderAddress = null;
        String message = null;
        String clientCorrelator = null;
        String notifyURL = null;
        String callbackData = null;
        String senderName = null;

        try {
            JSONObject objJSONObject = new JSONObject(json);

            JSONObject objOtboundSMSMessageRequest = (JSONObject) objJSONObject.get("outboundSMSMessageRequest");

            if (!objOtboundSMSMessageRequest.isNull("address")) {

                JSONArray addressArray = objOtboundSMSMessageRequest.getJSONArray("address");
                for (int a = 0; a < addressArray.length(); a++) {
                    addresses.add(nullOrTrimmed(addressArray.getString(a)));
                }
            }

            if (!objOtboundSMSMessageRequest.isNull("senderAddress")) {

                senderAddress = nullOrTrimmed(objOtboundSMSMessageRequest.getString("senderAddress"));
            }

            JSONObject objOutboundSMSTextMessage = (JSONObject) objOtboundSMSMessageRequest.get("outboundSMSTextMessage");
            if (objOutboundSMSTextMessage.get("message") != null) {

                message = nullOrTrimmed(objOutboundSMSTextMessage.getString("message"));
            }

            if (!objOtboundSMSMessageRequest.isNull("clientCorrelator")) {

                clientCorrelator = nullOrTrimmed(objOtboundSMSMessageRequest.getString("clientCorrelator"));
            }

            if (!objOtboundSMSMessageRequest.isNull("receiptRequest")) {

                JSONObject objReceiptRequest = (JSONObject) objOtboundSMSMessageRequest.get("receiptRequest");
                if (!objReceiptRequest.isNull("callbackData")) {

                    callbackData = nullOrTrimmed(objReceiptRequest.getString("callbackData"));
                }
                if (!objReceiptRequest.isNull("notifyURL")) {

                    notifyURL = nullOrTrimmed(objReceiptRequest.getString("notifyURL"));
                }
            }
            if (!objOtboundSMSMessageRequest.isNull("senderName")) {

                senderName = nullOrTrimmed(objOtboundSMSMessageRequest.getString("senderName"));
            }
        } catch (Exception e) {
            //logger.debug("Response JSON: " + "test");
            System.out.println("Manipulating recived JSON Object: " + e);
            throw new CustomException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] rules = {
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "senderAddress", senderAddress),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "address", addresses.toArray(new String[addresses.size()])),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_MESSAGE, "message", message),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "senderName", senderName),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_URL, "notifyURL", notifyURL),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),};

        Validation.checkRequestParams(rules);
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

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String[])
     */
    public void validate(String[] params) throws CustomException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validateUrl(java.lang.String)
     */
    public void validateUrl(String pathInfo) throws CustomException {
        String[] requestParts = null;
        if (pathInfo != null) {
            if (pathInfo.startsWith("/")) {
                pathInfo = pathInfo.substring(1);
            }
            requestParts = pathInfo.split("/");
        }

        UrlValidator.validateRequest(requestParts, validationRules);
    }
}
