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
package com.wso2telco.dep.oneapivalidation.service.impl.ussd;

import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.util.UrlValidator;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import static com.wso2telco.dep.oneapi.constant.ussd.USSDValueConstants.*;


// TODO: Auto-generated Javadoc
/**
 * The Class ValidateUssdSend.
 */
public class ValidateUssdSend implements IServiceValidate {

    /** The log. */
    private Log log = LogFactory.getLog(ValidateUssdSend.class);

    /** The validation rules. */
    private final String[] validationRules = {"outbound", "*"};

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String)
     */
    public void validate(String json) throws CustomException {
        String address = null;
        String shortCode = null;
        String keyword = null;
        String message = null;
        String clientCorrelator = null;
        String notifyUrl = null;
        String callbackData = null;
        String ussdAction = null;

        try {
            JSONObject objJSONObject = new JSONObject(json);
            JSONObject requestData = objJSONObject.getJSONObject("outboundUSSDMessageRequest");

            if (!requestData.isNull("address")) {
                address = nullOrTrimmed(requestData.getString("address"));
            }
            if (!requestData.isNull("shortCode")) {
                shortCode = nullOrTrimmed(requestData.getString("shortCode"));
            }

            if (requestData.has("keyword") && !requestData.isNull("keyword")) {
                keyword = nullOrTrimmed(requestData.getString("keyword"));
            }
            if (!requestData.isNull("outboundUSSDMessage")) {
                message = nullOrTrimmed(requestData.getString("outboundUSSDMessage"));
            }
            if (!requestData.isNull("clientCorrelator")) {
                clientCorrelator = nullOrTrimmed(requestData.getString("clientCorrelator"));
            }

            if (!requestData.isNull("ussdAction")) {
                ussdAction = nullOrTrimmed(requestData.getString("ussdAction"));
            }

            if (!(MTINIT.equals(ussdAction))) {
                throw new CustomException("SVC0002", "Invalid input value for message part %1",
                        new String[]{"Invalid ussdAction"});
            }

            if (!requestData.isNull("responseRequest")) {
                JSONObject responseRequest = requestData.getJSONObject("responseRequest");
                if (!responseRequest.isNull("notifyURL")) {
                    notifyUrl = nullOrTrimmed(responseRequest.getString("notifyURL"));
                }
                if (!responseRequest.isNull("callbackData")) {
                    callbackData = nullOrTrimmed(responseRequest.getString("callbackData"));
                }
            }

        } catch (JSONException e) {
            log.error("Manipulating recived JSON Object: " + e);
            throw new CustomException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] rules = null;

        rules = new ValidationRule[]{
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "address", address),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "shortCode", shortCode),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "keyword", keyword),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "outboundUSSDMessage", message),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "ussdAction", ussdAction),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "notifyURL", notifyUrl),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),};
        Validation.checkRequestParams(rules);
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

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String[])
     */
    public void validate(String[] params) throws CustomException {
        throw new UnsupportedOperationException("Not supported yet.");
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
