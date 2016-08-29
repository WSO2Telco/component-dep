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

import org.json.JSONException;
import org.json.JSONObject;

import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.UrlValidator;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;

 
// TODO: Auto-generated Javadoc
/**
 * The Class ValidateDeliverySubscription.
 */
public class ValidateDeliverySubscription implements com.wso2telco.dep.oneapivalidation.service.IServiceValidate {

    /** The validation rules. */
    private final String[] validationRules = {"outbound", "*", "subscriptions"};

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String)
     */
    public void validate(String json) throws CustomException {

        String clientCorrelator = null;
        String notifyURL = null;
        String callbackData = null;
        String filterCriteria = null;
        try {
            JSONObject mainJson = new JSONObject(json);
            JSONObject deliveryReceiptSubscription = mainJson.getJSONObject("deliveryReceiptSubscription");

            if (deliveryReceiptSubscription.get("filterCriteria") != null) {
                filterCriteria = deliveryReceiptSubscription.getString("filterCriteria");
            }

            JSONObject callbackReference = deliveryReceiptSubscription.getJSONObject("callbackReference");
            if (callbackReference.get("callbackData") != null) {
                callbackData = callbackReference.getString("callbackData");
            }
            if (callbackReference.get("notifyURL") != null) {
                notifyURL = callbackReference.getString("notifyURL");
            }

        } catch (JSONException ex) {
            System.out.println("ValidateDeliverySubscription: "+ex);
            throw new CustomException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] rules = {
                //new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "senderAddress", senderAddress),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL, "notifyURL", notifyURL),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "filterCriteria", filterCriteria),};
        
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
}
