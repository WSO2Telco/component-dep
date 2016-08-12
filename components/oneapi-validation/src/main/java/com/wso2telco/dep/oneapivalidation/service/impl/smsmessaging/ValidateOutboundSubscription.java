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


import org.json.JSONObject;

import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.util.UrlValidator;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;


 
// TODO: Auto-generated Javadoc
/**
 * The Class ValidateOutboundSubscription.
 */
public class ValidateOutboundSubscription implements IServiceValidate {

    /** The validation rules. */
    private final String[] validationRules = {"outbound", "*", "subscriptions"};

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String)
     */
    public void validate(String json) throws CustomException {

        String notifyURL = null;
        String callbackData = null;
        String filterCriteria = null;
        String clientCorrelator = null;

        try {

            JSONObject objJSONObject = new JSONObject(json);
            JSONObject objDeliveryReceiptSubscription = (JSONObject) objJSONObject.get("deliveryReceiptSubscription");

            if (!objDeliveryReceiptSubscription.isNull("filterCriteria")) {
                filterCriteria = nullOrTrimmed(objDeliveryReceiptSubscription.getString("filterCriteria"));
            }

            if (!objDeliveryReceiptSubscription.isNull("clientCorrelator")) {
                clientCorrelator = nullOrTrimmed(objDeliveryReceiptSubscription.getString("clientCorrelator"));
            }

            JSONObject objCallbackReference = (JSONObject) objDeliveryReceiptSubscription.get("callbackReference");

            if (!objCallbackReference.isNull("callbackData")) {
                callbackData = nullOrTrimmed(objCallbackReference.getString("callbackData"));
            }

            if (!objCallbackReference.isNull("notifyURL")) {
                notifyURL = nullOrTrimmed(objCallbackReference.getString("notifyURL"));
            }
        } catch (Exception e) {
            System.out.println("Manipulating recived JSON Object: " + e);
            throw new CustomException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] rules = null;
        rules = new ValidationRule[]{
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "filterCriteria", filterCriteria),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL, "notifyURL", notifyURL)
        };

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
