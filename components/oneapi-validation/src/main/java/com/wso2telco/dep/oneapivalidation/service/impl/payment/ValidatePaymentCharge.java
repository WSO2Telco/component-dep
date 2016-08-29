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
package com.wso2telco.dep.oneapivalidation.service.impl.payment;


import org.json.JSONObject;

import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.util.UrlValidator;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;

 
// TODO: Auto-generated Javadoc
/**
 * The Class ValidatePaymentCharge.
 */
public class ValidatePaymentCharge  implements IServiceValidate {
    
    /** The validation rules. */
    private final String[] validationRules = {"*", "transactions", "amount"};
    
    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String)
     */
    public void validate(String json) throws CustomException {
        //throw new UnsupportedOperationException("Not supported yet.");
        
            String endUserId = null;
            String transactionOperationStatus = null;
            String originalServerReferenceCode = null;
            String referenceCode = null;
            String description = null;
            String currency = null;
            Double amount = null;

            String clientCorrelator = null;
            String onBehalfOf = null;
            String purchaseCategoryCode = null;
            String channel = null;
            Double taxAmount = null;

            try {
                JSONObject mainJSONObject = new JSONObject(json);
                JSONObject jsonObj = (JSONObject) mainJSONObject.get("amountTransaction");

                if (!jsonObj.isNull("clientCorrelator")) {
                    clientCorrelator = nullOrTrimmed(jsonObj.getString("clientCorrelator"));
                }
                if (!jsonObj.isNull("endUserId")) {
                    endUserId = nullOrTrimmed(jsonObj.getString("endUserId"));
                }
                if (!jsonObj.isNull("referenceCode")) {
                    referenceCode = nullOrTrimmed(jsonObj.getString("referenceCode"));
                }
                if (!jsonObj.isNull("transactionOperationStatus")) {
                    transactionOperationStatus = nullOrTrimmed(jsonObj.getString("transactionOperationStatus"));
                }
                if (!jsonObj.isNull("originalServerReferenceCode")) {
                    originalServerReferenceCode = nullOrTrimmed(jsonObj.getString("originalServerReferenceCode"));
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
                if (!chargingMetaData.isNull("taxAmount")) {
                    taxAmount = Double.parseDouble(nullOrTrimmed(chargingMetaData.getString("taxAmount")));
                }

                System.out.println("Manipulated recived JSON Object: " + json);

            } catch (Exception e) {
                System.out.println("Manipulating recived JSON Object: " + e);
                throw new CustomException("POL0299", "Unexpected Error", new String[]{""});
            }

            ValidationRule[] rules = null;

            rules = new ValidationRule[]{
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionOperationStatus", transactionOperationStatus),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "originalServerReferenceCode", originalServerReferenceCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "referenceCode", referenceCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "description", description),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_CURRENCY, "currency", currency),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO, "amount", amount),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "onBehalfOf", onBehalfOf),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "purchaseCategoryCode", purchaseCategoryCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_PAYMENT_CHANNEL, "channel", channel),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO, "taxAmount", taxAmount),};
            
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
