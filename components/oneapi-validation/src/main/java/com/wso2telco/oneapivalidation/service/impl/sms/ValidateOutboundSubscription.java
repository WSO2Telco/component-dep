/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.oneapivalidation.service.impl.sms;


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
public class ValidateOutboundSubscription implements IServiceValidate {

    private final String[] validationRules = {"outbound", "*", "subscriptions"};

    public void validate(String json) throws AxiataException {

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
            throw new AxiataException("POL0299", "Unexpected Error", new String[]{""});
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
