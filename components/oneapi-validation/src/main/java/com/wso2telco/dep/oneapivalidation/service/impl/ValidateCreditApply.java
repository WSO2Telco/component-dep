/**
 * Copyright (c) 2015, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.dep.oneapivalidation.service.impl;

import org.json.JSONObject;

import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.util.UrlValidator;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;

public class ValidateCreditApply implements IServiceValidate {
    
    private final String[] validationRules = {"credit", "*","apply"};
    
   public void validate(String json) throws CustomException {

            String type = null;
            String amount = null;
            String clientCorrelator = null;
           String reasonForCredit = null;
            String merchantIdentification = null;
            String notifyURL = null;
            String callbackData = null;

           try {
                JSONObject mainJSONObject = new JSONObject(json);
                JSONObject jsonObj = (JSONObject) mainJSONObject.get("creditApplyRequest");

                if (!jsonObj.isNull("type")) {
                   type = nullOrTrimmed(jsonObj.getString("type"));
                }
                if (!jsonObj.isNull("amount")) {
                    amount = nullOrTrimmed(jsonObj.getString("amount"));
                }
                if (!jsonObj.isNull("clientCorrelator")) {
                   clientCorrelator = nullOrTrimmed(jsonObj.getString("clientCorrelator"));
                }
                if (!jsonObj.isNull("reasonForCredit")) {
                    reasonForCredit = nullOrTrimmed(jsonObj.getString("reasonForCredit"));
               }
                if (!jsonObj.isNull("merchantIdentification")) {
                    merchantIdentification = nullOrTrimmed(jsonObj.getString("merchantIdentification"));
                }

               JSONObject receiptRequest = (JSONObject) jsonObj.get("receiptRequest");

                if (!receiptRequest.isNull("notifyURL")) {
                   notifyURL = nullOrTrimmed(receiptRequest.getString("notifyURL"));
                }
                if (!receiptRequest.isNull("callbackData")) {
                    callbackData = nullOrTrimmed(receiptRequest.getString("callbackData"));
               }

            } catch (Exception e) {
                System.out.println("Manipulating recived JSON Object: " + e);
                throw new CustomException("POL0299", "Unexpected Error", new String[]{""});
            }

            ValidationRule[] rules = null;

            rules = new ValidationRule[]{
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "type", type),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GE_ZERO, "amount", amount),
		        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "clientCorrelator", clientCorrelator),
		        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "reasonForCredit", reasonForCredit),
		        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "merchantIdentification", merchantIdentification),
		        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_URL, "notifyURL", notifyURL),
		        new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),

                };
            
            Validation.checkRequestParams(rules);
            
   }
    
    private static String nullOrTrimmed(String s) {
        String rv = null;
        if (s != null && s.trim().length() > 0) {
            rv = s.trim();
        }
        return rv;
    }

    public void validate(String[] params) throws CustomException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

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
