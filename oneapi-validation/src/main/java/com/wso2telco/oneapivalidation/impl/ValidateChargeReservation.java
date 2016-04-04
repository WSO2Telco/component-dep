/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.oneapivalidation.impl;

import com.wso2telco.oneapivalidation.AxiataException;
import com.wso2telco.oneapivalidation.IServiceValidate;
import com.wso2telco.oneapivalidation.UrlValidator;
import com.wso2telco.oneapivalidation.ValidationNew;
import com.wso2telco.oneapivalidation.ValidationRule;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class ValidateChargeReservation implements IServiceValidate {

    private final String[] validationRules = {"*", "transactions", "amountReservation", "*"};

    public void validate(String json) throws AxiataException {
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
            JSONObject objAmountReservationTransaction = (JSONObject) objJSONObject.get("amountReservationTransaction");

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
            throw new AxiataException("POL0299", "Unexpected Error", new String[]{""});
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
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionOperationStatus", transactionOperationStatus, "Charged")};

        ValidationNew.checkRequestParams(rules);
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
        String transactionId = nullOrTrimmed(params[0]);

        ValidationRule[] rules = {
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionId", transactionId),};

        ValidationNew.checkRequestParams(rules);
    }

    private static String nullOrTrimmed(String s) {
        String rv = null;
        if (s != null && s.trim().length() > 0) {
            rv = s.trim();
        }
        return rv;
    }
}
