/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.oneapivalidation.service.impl.payment;


import org.json.JSONObject;

import com.wso2telco.oneapivalidation.exceptions.AxiataException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.util.UrlValidator;
import com.wso2telco.oneapivalidation.util.Validation;
import com.wso2telco.oneapivalidation.util.ValidationRule;

/**
 *
 * @author User
 */
public class ValidateRefund implements IServiceValidate {

    private final String[] validationRules = {"*", "transactions", "amount"};

    public void validate(String json) throws AxiataException {

        String clientCorrelator = null;
        String endUserId = null;
        Double amount = null;
        String currency = null;
        String description = null;
        String code = null;
        String onBehalfOf = null;
        String purchaseCategoryCode = null;
        String channel = null;
        Double taxAmount = null;
        String mandateId = null;
        String productId = null;
        String serviceId = null;
        String callbackData = null;
        String notifyURL = null;
        String referenceCode = null;
        String notificationFormat = null;
        String originalServerReferenceCode = null;
        String transactionOperationStatus = null;

        try {
            JSONObject objJSONObject = new JSONObject(json);
            JSONObject objAmountTransaction = (JSONObject) objJSONObject.get("amountTransaction");

            if (objAmountTransaction.get("clientCorrelator") != null) {
                clientCorrelator = nullOrTrimmed(objAmountTransaction.get("clientCorrelator").toString());
            }
            if (objAmountTransaction.get("endUserId") != null) {
                endUserId = nullOrTrimmed(objAmountTransaction.get("endUserId").toString());
            }
            if (objAmountTransaction.get("callbackData") != null) {
                callbackData = nullOrTrimmed(objAmountTransaction.get("callbackData").toString());
            }
            if (objAmountTransaction.get("notifyURL") != null) {
                notifyURL = nullOrTrimmed(objAmountTransaction.get("notifyURL").toString());
            }
            if (objAmountTransaction.get("notificationFormat") != null) {
                notificationFormat = nullOrTrimmed(objAmountTransaction.get("notificationFormat").toString());
            }
            if (objAmountTransaction.get("referenceCode") != null) {
                referenceCode = nullOrTrimmed(objAmountTransaction.get("referenceCode").toString());
            }
            if (objAmountTransaction.get("originalServerReferenceCode") != null) {
                originalServerReferenceCode = nullOrTrimmed(objAmountTransaction.get("originalServerReferenceCode").toString());
            }
            if (objAmountTransaction.get("transactionOperationStatus") != null) {
                transactionOperationStatus = nullOrTrimmed(objAmountTransaction.get("transactionOperationStatus").toString());
            }

            JSONObject objPaymentAmount = (JSONObject) objAmountTransaction.get("paymentAmount");
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
            if (objChargingInformation.get("code") != null) {
                code = nullOrTrimmed(objChargingInformation.get("code").toString());
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
            throw new AxiataException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] rules = null;

        rules = new ValidationRule[]{
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GT_ZERO, "amount", amount),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "currency", currency),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "description", description),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "onBehalfOf", onBehalfOf),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "notificationFormat", notificationFormat),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "code", code),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "purchaseCategoryCode", purchaseCategoryCode),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_PAYMENT_CHANNEL, "channel", channel),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO, "taxAmount", taxAmount),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "serviceId", serviceId),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "mandateId", mandateId),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "productId", productId),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_URL, "notifyURL", notifyURL),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "referenceCode", referenceCode),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "originalServerReferenceCode", originalServerReferenceCode),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionOperationStatus", transactionOperationStatus, "Refunded")};

        Validation.checkRequestParams(rules);
    }

    public void validateUrl(String pathInfo) throws AxiataException {
        String[] requestParts = null;
        if (pathInfo != null) {
            if (pathInfo.startsWith("/")) {
                pathInfo = pathInfo.substring(1);
            }
            requestParts = pathInfo.split("/");
        }

        UrlValidator.validateRequest(requestParts, validationRules);
    }

    public void validate(String[] params) throws AxiataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private static String nullOrTrimmed(String s) {
        String rv = null;
        if (s != null && s.trim().length() > 0) {
            rv = s.trim();
        }
        return rv;
    }
}
