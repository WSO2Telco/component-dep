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
 * The Class ValidateReserveAdditional.
 */
public class ValidateReserveAdditional implements IServiceValidate {

    /** The validation rules. */
    private final String[] validationRules = {"*", "transactions", "amountReservation", "*"};

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String)
     */
    public void validate(String json) throws CustomException {
        String endUserId = null;
        String callbackData = null;
        String notifyURL = null;
        String notificationFormat = null;
        Double amount = null;
        String currency = null;
        String description = null;
        String onBehalfOf = null;
        String purchaseCategoryCode = null;
        String channel = null;
        Double taxAmount = null;
        String mandateId = null;
        String productId = null;
        String serviceId = null;
        String referenceCode = null;
        int referenceSequence = 0;
        String transactionOperationStatus = null;

        try {

            JSONObject objJSONObject = new JSONObject(json);
            JSONObject objAmountReservationTransaction = objJSONObject.getJSONObject("amountReservationTransaction");

            if (objAmountReservationTransaction.get("endUserId") != null) {
                endUserId = nullOrTrimmed(objAmountReservationTransaction.get("endUserId").toString());
            }
            if (objAmountReservationTransaction.get("callbackData") != null) {
                callbackData = nullOrTrimmed(objAmountReservationTransaction.get("callbackData").toString());
            }
            if (objAmountReservationTransaction.get("notifyURL") != null) {
                notifyURL = nullOrTrimmed(objAmountReservationTransaction.get("notifyURL").toString());
            }
            if (objAmountReservationTransaction.get("notificationFormat") != null) {
                notificationFormat = nullOrTrimmed(objAmountReservationTransaction.get("notificationFormat").toString());
            }
            if (objAmountReservationTransaction.get("referenceCode") != null) {
                referenceCode = nullOrTrimmed(objAmountReservationTransaction.get("referenceCode").toString());
            }
            if (objAmountReservationTransaction.get("transactionOperationStatus") != null) {
                transactionOperationStatus = nullOrTrimmed(objAmountReservationTransaction.get("transactionOperationStatus").toString());
            }
            if (objAmountReservationTransaction.get("referenceSequence") != null) {
                referenceSequence = Integer.parseInt(nullOrTrimmed(objAmountReservationTransaction.get("referenceSequence").toString()));
            }

            JSONObject objPaymentAmount = (JSONObject) objAmountReservationTransaction.get("paymentAmount");

            JSONObject objChargingInformation = (JSONObject) objPaymentAmount.get("chargingInformation");

            if (objChargingInformation.get("amount") != null) {
                amount = Double.parseDouble(nullOrTrimmed(objChargingInformation.get("amount").toString()));
            }
            if (objChargingInformation.get("currency") != null) {
                currency = nullOrTrimmed(objChargingInformation.get("currency").toString());
            }
            if (objChargingInformation.get("description") != null) {
                description = nullOrTrimmed(objChargingInformation.get("description").toString());
            }

            JSONObject objChargingMetaData = (JSONObject) objPaymentAmount.get("chargingMetaData");

            if (objChargingMetaData.get("onBehalfOf") != null) {
                onBehalfOf = nullOrTrimmed(objChargingMetaData.get("onBehalfOf").toString());
            }
            if (objChargingMetaData.get("purchaseCategoryCode") != null) {
                purchaseCategoryCode = nullOrTrimmed(objChargingMetaData.get("purchaseCategoryCode").toString());
            }
            if (objChargingMetaData.get("channel") != null) {
                channel = nullOrTrimmed(objChargingMetaData.get("channel").toString());
            }
            if (objChargingMetaData.get("taxAmount") != null) {
                taxAmount = Double.parseDouble(nullOrTrimmed(objChargingMetaData.get("taxAmount").toString()));
            }
            if (objChargingMetaData.get("mandateId") != null) {
                mandateId = nullOrTrimmed(objChargingMetaData.get("mandateId").toString());
            }
            if (objChargingMetaData.get("productId") != null) {
                productId = nullOrTrimmed(objChargingMetaData.get("productId").toString());
            }
            if (objChargingMetaData.get("serviceId") != null) {
                serviceId = nullOrTrimmed(objChargingMetaData.get("serviceId").toString());
            }

        } catch (Exception e) {
            System.out.println("Manipulating recived JSON Object: " + e);
                throw new CustomException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] rules = null;

        rules = new ValidationRule[]{
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO, "amount", amount),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_CURRENCY, "currency", currency),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "description", description),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "onBehalfOf", onBehalfOf),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "notificationFormat", notificationFormat),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "purchaseCategoryCode", purchaseCategoryCode),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_PAYMENT_CHANNEL, "channel", channel),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO, "taxAmount", taxAmount),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "serviceId", serviceId),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "mandateId", mandateId),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "productId", productId),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_URL, "notifyURL", notifyURL),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "referenceCode", referenceCode),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "referenceSequence", Integer.toString(referenceSequence)),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionOperationStatus", transactionOperationStatus, "reserved")};

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
        String transactionId = nullOrTrimmed(params[0]);

        ValidationRule[] rules = {
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionId", transactionId),};
        
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
}
