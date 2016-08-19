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
package com.wso2telco.oneapivalidation.service.impl;

import org.json.JSONObject;

import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.util.UrlValidator;
import com.wso2telco.oneapivalidation.util.Validation;
import com.wso2telco.oneapivalidation.util.ValidationRule;

public class ValidateWalletRefund implements IServiceValidate {
    
    private final String[] validationRules = {"transaction", "*","refund"};
    
    public void validate(String json) throws CustomException {
        
            String endUserId = null;
            String originalReferenceCode = null;
            String description = null;
           String currency = null;
            Double amount = null;
            String clientCorrelator = null;
            String onBehalfOf = null;
            String referenceCode = null;
            String channel = null;
            String originalServerReferenceCode=null;


            String purchaseCategoryCode=null;

            try {
                JSONObject mainJSONObject = new JSONObject(json);
                JSONObject jsonObj = (JSONObject) mainJSONObject.get("refundTransaction");

                if (!jsonObj.isNull("clientCorrelator")) {
                    clientCorrelator = nullOrTrimmed(jsonObj.getString("clientCorrelator"));
                }
                if (!jsonObj.isNull("endUserId")) {
                    endUserId = nullOrTrimmed(jsonObj.getString("endUserId"));
                }
                if (!jsonObj.isNull("referenceCode")) {
                    referenceCode = nullOrTrimmed(jsonObj.getString("referenceCode"));
                }
                if (!jsonObj.isNull("originalServerReferenceCode")) {
                    originalServerReferenceCode = nullOrTrimmed(jsonObj.getString("originalServerReferenceCode"));
                }
                if (!jsonObj.isNull("originalReferenceCode")) {
                   originalReferenceCode = nullOrTrimmed(jsonObj.getString("originalReferenceCode"));
                }
                JSONObject payAmount = (JSONObject) jsonObj.get("paymentAmount");
                JSONObject chargingInfo = (JSONObject) payAmount.get("chargingInformation");
                
                if (!chargingInfo.isNull("amount")) {
                    amount = Double.parseDouble(nullOrTrimmed(chargingInfo.getString("amount")));
                }
                if (!chargingInfo.isNull("currency")) {
                    currency = nullOrTrimmed(chargingInfo.getString("currency"));
               }
                if (!chargingInfo.isNull("description")) {
                    description = nullOrTrimmed(chargingInfo.getString("description"));
                }

                JSONObject chargingMetaData = (JSONObject) payAmount.get("chargingMetaData");
                
                if (!chargingMetaData.isNull("onBehalfOf")) {
                    onBehalfOf = nullOrTrimmed(chargingMetaData.getString("onBehalfOf"));
                }
                if (!chargingMetaData.isNull("purchaseCategoryCode")) {
                    purchaseCategoryCode = nullOrTrimmed(chargingMetaData.getString("purchaseCategoryCode"));
                }
                if (!chargingMetaData.isNull("channel")) {
                    channel = nullOrTrimmed(chargingMetaData.getString("channel"));
                }


                System.out.println("Manipulated recived JSON Object: " + json);

            } catch (Exception e) {
                System.out.println("Manipulating recived JSON Object: " + e);
                throw new CustomException("POL0299", "Unexpected Error", new String[]{""});
            }

           ValidationRule[] rules = null;

            rules = new ValidationRule[]{
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "originalReferenceCode", originalReferenceCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "referenceCode", referenceCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "description", description),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_CURRENCY, "currency", currency),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO, "amount", amount),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "onBehalfOf", onBehalfOf),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "purchaseCategoryCode", purchaseCategoryCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_PAYMENT_CHANNEL, "channel", channel),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "originalServerReferenceCode", originalServerReferenceCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
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

