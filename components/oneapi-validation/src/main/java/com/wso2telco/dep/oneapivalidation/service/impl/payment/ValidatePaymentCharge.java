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


import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.util.UrlValidator;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.dep.user.masking.UserMaskHandler;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


// TODO: Auto-generated Javadoc
/**
 * The Class ValidatePaymentCharge.
 */
public class ValidatePaymentCharge  implements IServiceValidate {

    private Log log = LogFactory.getLog(ValidatePaymentCharge.class);

    /** The validation rules. */
    private final String[] validationRules = {"*", "transactions", "amount"};

    /** user masking */
    private boolean userAnonymization;

    /** user masking encryption key */
    private String maskingSecretKey;

    public ValidatePaymentCharge() {

    }

    public ValidatePaymentCharge(boolean userAnonymization, String maskingSecretKey) {
        this.userAnonymization = userAnonymization;
        this.maskingSecretKey = maskingSecretKey;
    }

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
            String tripleValidationRegex = "^[a-zA-Z]{3}$";
            String quadrupleValidationRegex = "(\\d+(\\.\\d{1,4})?)";

            try {
                JSONObject mainJSONObject = new JSONObject(json);
                JSONObject jsonObj = (JSONObject) mainJSONObject.get("amountTransaction");

                if (!jsonObj.isNull("clientCorrelator")) {
                    clientCorrelator = nullOrTrimmed(jsonObj.getString("clientCorrelator"));
                }
                if (!jsonObj.isNull("endUserId")) {
                    endUserId = nullOrTrimmed(jsonObj.getString("endUserId"));
                    if(this.userAnonymization) {
                        endUserId = UserMaskHandler.maskUserId(endUserId, false, this.maskingSecretKey);
                    }
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
                    if (!chargingInfo.get("amount").toString().matches(quadrupleValidationRegex)) {
                        throw new CustomException("SVC0002", "Invalid input value for message part %1",
                                new String[]{"amount should be a whole or four digit decimal positive number"});
                    }
                    amount = Double.parseDouble(nullOrTrimmed(String.valueOf(chargingInfo.get("amount"))));
                }
                if (!chargingInfo.isNull("currency")) {
                    currency = nullOrTrimmed(chargingInfo.getString("currency"));
                    if (!currency.matches(tripleValidationRegex)) {
                        throw new CustomException("SVC0002", "Invalid input value for message part %1",
                                new String[]{"currency should be three character long string"});
                    }
                }
                if (!chargingInfo.isNull("description")) {
                    description = nullOrTrimmed(chargingInfo.getString("description"));
                }

                if(payAmount.has("chargingMetaData")) {
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
                        if (!chargingMetaData.get("taxAmount").toString().matches(quadrupleValidationRegex)) {
                            throw new CustomException("SVC0002", "Invalid input value for message part %1",
                                    new String[]{"taxAmount should be a whole or four digit decimal positive number"});
                        }
                        taxAmount = Double.parseDouble(nullOrTrimmed(String.valueOf(chargingMetaData.get("taxAmount"))));

                    }
                }

                if (log.isDebugEnabled()){
                    log.debug("Manipulated received JSON Object: " + json);
                }

            } catch (JSONException e) {
                log.error("Manipulating received JSON Object: " + e);
                throw new CustomException("SVC0001", "", new String[]{"Incorrect JSON Object received"});
            } catch (CustomException e){
                log.error("Manipulating received JSON Object: " + e);
                throw new CustomException(e.getErrcode(), e.getErrmsg(), e.getErrvar());
            } catch (BadPaddingException e) {
                log.error("Error occurred while unmasking. Possible reason would be incorrect masking configuration. " , e);
                throw new CustomException("SVC0001", "A service error occurred.", new String[]{"Invalid user mask configuration"});
            } catch (Exception e) {
                log.error("Manipulating received JSON Object: " + e);
                throw new CustomException("POL0299", "Unexpected Error", new String[]{""});
            }

        ValidationRule[] rules = null;

            rules = new ValidationRule[]{
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionOperationStatus", transactionOperationStatus),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "originalServerReferenceCode", originalServerReferenceCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "referenceCode", referenceCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "description", description),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "currency", currency),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GT_ZERO, "amount", amount),
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
