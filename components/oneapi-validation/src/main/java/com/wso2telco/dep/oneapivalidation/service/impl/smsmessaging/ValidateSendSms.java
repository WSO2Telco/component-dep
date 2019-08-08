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

import com.wso2telco.dep.oneapivalidation.delegator.ValidationDelegator;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.util.UrlValidator;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.dep.user.masking.UserMaskHandler;
import com.wso2telco.dep.user.masking.exceptions.UserMaskingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class ValidateSendSms.
 */
public class ValidateSendSms implements IServiceValidate {

    /** The validation rules. */
    private final String[] validationRules = {"outbound", "*", "requests"};

    private static Log log = LogFactory.getLog(ValidateSendSms.class);

    /** user masking */
    private boolean userAnonymization;

    /** masked msisdn */
    private String userMaskingSecretKey;

    private ValidationDelegator validationDelegator;

    @Deprecated
    public ValidateSendSms() {
        this.validationDelegator = ValidationDelegator.getInstance();
    }

    /**
     *
     * @param validationDelegator
     */
    public ValidateSendSms(ValidationDelegator validationDelegator) {
        this.validationDelegator = validationDelegator;
    }

    /**
     *
     * @param userAnonymization
     * @param userMaskingSecretKey
     */
    @Deprecated
    public ValidateSendSms(boolean userAnonymization, String userMaskingSecretKey) {
        this.userAnonymization = userAnonymization;
        this.userMaskingSecretKey = userMaskingSecretKey;
        this.validationDelegator = ValidationDelegator.getInstance();
    }

    /**
     *
     * @param userAnonymization
     * @param userMaskingSecretKey
     * @param validationDelegator
     */
    public ValidateSendSms(boolean userAnonymization, String userMaskingSecretKey,
                           ValidationDelegator validationDelegator) {
        this.userAnonymization = userAnonymization;
        this.userMaskingSecretKey = userMaskingSecretKey;
        this.validationDelegator = validationDelegator;
    }

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
                    if(this.userAnonymization) {
                        addresses.add(nullOrTrimmed(UserMaskHandler.transcryptUserId(addressArray.getString(a),
                                false, this.userMaskingSecretKey)));
                    } else {
                        addresses.add(nullOrTrimmed(addressArray.getString(a)));
                    }
                }
            }

            if (!objOtboundSMSMessageRequest.isNull("senderAddress")) {

                senderAddress = nullOrTrimmed(objOtboundSMSMessageRequest.getString("senderAddress"));
            }

            if (!objOtboundSMSMessageRequest.isNull("outboundSMSTextMessage")) {
                JSONObject objOutboundSMSTextMessage = (JSONObject) objOtboundSMSMessageRequest.get(
                        "outboundSMSTextMessage");
                if (!objOutboundSMSTextMessage.isNull("message")) {
                    message = nullOrTrimmed(objOutboundSMSTextMessage.getString("message"));
                }
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
        } catch (JSONException e) {
            log.error("Manipulating received JSON Object: " + e);
            throw new CustomException("SVC0001", "", new String[]{"Incorrect JSON Object received"});
        } catch (UserMaskingException ume) {
            log.error("Error occurred while unmasking. Possible reason would be incorrect masking configuration. " , ume);
            throw new CustomException("SVC0003", ume.getMessage(), new String[]{"Invalid user mask configuration"});
        }

        ValidationRule[] rules = {
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "senderAddress", senderAddress),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "address", addresses.toArray(new String[addresses.size()])),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_MESSAGE, "message", message),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "senderName", senderName),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_URL, "notifyURL", notifyURL),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),};

        validationDelegator.checkRequestParams(rules);
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
