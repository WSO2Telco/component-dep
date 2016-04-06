/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.oneapivalidation.service.impl.sms.sb;

import com.wso2telco.oneapivalidation.exceptions.AxiataException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.util.UrlValidator;
import com.wso2telco.oneapivalidation.util.Validation;
import com.wso2telco.oneapivalidation.util.ValidationRule;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class ValidateSBSubscription implements IServiceValidate{

    private final String[] validationRules = {"inbound", "subscriptions"};

    public void validate(String json) throws AxiataException {

        String destinationAddress = null;
        String notifyURL = null;
        String criteria = null;
        String notificationFormat = null;
        String clientCorrelator = null;
        String callbackData = null;

        try {

            JSONObject objJSONObject = new JSONObject(json);

            JSONObject objSubscription = (JSONObject) objJSONObject.get("subscription");

            JSONObject objCallbackReference = (JSONObject) objSubscription.get("callbackReference");
            if (!objCallbackReference.isNull("callbackData")) {

                callbackData = nullOrTrimmed(objCallbackReference.getString("callbackData"));
            }
            if (!objCallbackReference.isNull("notifyURL")) {

                notifyURL = nullOrTrimmed(objCallbackReference.getString("notifyURL"));
            }

            if (!objSubscription.isNull("criteria")) {

                criteria = nullOrTrimmed(objSubscription.getString("criteria"));
            }
            if (!objSubscription.isNull("destinationAddress")) {

                destinationAddress = nullOrTrimmed(objSubscription.getString("destinationAddress"));
            }
            if (!objSubscription.isNull("notificationFormat")) {

                notificationFormat = nullOrTrimmed(objSubscription.getString("notificationFormat"));
            }
            if (!objSubscription.isNull("clientCorrelator")) {

                clientCorrelator = nullOrTrimmed(objSubscription.getString("clientCorrelator"));
            }
        } catch (Exception e) {
            System.out.println("Manipulating recived JSON Object: " + e);
            throw new AxiataException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] rules = {
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "destinationAddress", destinationAddress),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL, "notifyURL", notifyURL),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "criteria", criteria),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_JSON, "notificationFormat", notificationFormat),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "callbackData", callbackData),};

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
