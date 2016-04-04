/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.oneapivalidation.impl.sms.sb;


import org.json.JSONObject;

import com.wso2telco.oneapivalidation.AxiataException;
import com.wso2telco.oneapivalidation.IServiceValidate;
import com.wso2telco.oneapivalidation.UrlValidator;
import com.wso2telco.oneapivalidation.ValidationNew;
import com.wso2telco.oneapivalidation.ValidationRule;

/**
 * This implementation will supports for plugin Sample request format - {
 * "deliveryReceiptSubscription": { "callbackReference": { "callbackData":
 * "12345", "notifyURL":
 * "http://application.example.com/notifications/DeliveryInfoNotification" },
 * "operatorCode": "DIALOG", "filterCriteria": "GIGPICS", "clientCorrelator" :
 * "12345" } } Sample response format - { "deliveryReceiptSubscription": {
 * "callbackReference": { "callbackData": "12345", "notifyURL":
 * "http://application.example.com/notifications/DeliveryInfoNotification" },
 * "operatorCode": "DIALOG", "filterCriteria": "GIGPICS", "clientCorrelator" :
 * "12345", "resourceURL":
 * "http://example.com/smsmessaging/v1/outbound/subscriptions/sub789 " } }
 */
public class ValidateSBOutboundSubscription implements IServiceValidate {

    private final String[] validationRules = {"outbound", "*", "subscriptions"};

    public void validate(String json) throws AxiataException {

        String notifyURL = null;
        String callbackData = null;
        String filterCriteria = null;
        String operatorCode = null;
        String clientCorrelator = null;

        try {

            JSONObject objJSONObject = new JSONObject(json);
            JSONObject objDeliveryReceiptSubscription = (JSONObject) objJSONObject.get("deliveryReceiptSubscription");

            if (!objDeliveryReceiptSubscription.isNull("filterCriteria")) {
                filterCriteria = nullOrTrimmed(objDeliveryReceiptSubscription.getString("filterCriteria"));
            }

            if (!objDeliveryReceiptSubscription.isNull("operatorCode")) {
                operatorCode = nullOrTrimmed(objDeliveryReceiptSubscription.getString("operatorCode"));
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
            throw new AxiataException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] rules = null;
        rules = new ValidationRule[]{
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "filterCriteria", filterCriteria),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "operatorCode", operatorCode),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL, "notifyURL", notifyURL)
        };

        ValidationNew.checkRequestParams(rules);
    }

    private static String nullOrTrimmed(String s) {
        String rv = null;
        if (s != null && s.trim().length() > 0) {
            rv = s.trim();
        }
        return rv;
    }

    public void validate(String[] params) throws AxiataException {
        throw new UnsupportedOperationException("Not supported yet.");
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
}
