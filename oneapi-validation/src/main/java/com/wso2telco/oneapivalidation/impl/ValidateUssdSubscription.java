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
public class ValidateUssdSubscription implements IServiceValidate {

    private final String[] validationRules = {"ussd", "*", "inbound", "subscriptions"};

    public void validate(String json) throws AxiataException {


        String callbackData = null;
        String notifyUrl = null;
        String destinationAddress = null;
        String clientCorrelator = null;
        String resourceUrl = null;

        try {
            JSONObject objJSONObject = new JSONObject(json);
            JSONObject requestData = objJSONObject.getJSONObject("subscription");

            if (requestData.has("destinationAddress")) {
                destinationAddress = nullOrTrimmed(requestData.getString("destinationAddress"));
            }
            if (requestData.has("clientCorrelator")) {
                clientCorrelator = nullOrTrimmed(requestData.getString("clientCorrelator"));
            }
            if (requestData.has("resourceURL")) {
                resourceUrl = nullOrTrimmed(requestData.getString("resourceURL"));
            }

            if (requestData.has("callbackReference")) {
                JSONObject callbackReference = requestData.getJSONObject("callbackReference");

                if (callbackReference.has("callbackData")) {
                    callbackData = nullOrTrimmed(callbackReference.getString("callbackData"));
                }
                if (callbackReference.has("notifyURL")) {
                    notifyUrl = nullOrTrimmed(callbackReference.getString("notifyURL"));
                }
            }

        } catch (Exception e) {

            System.out.println("Manipulating recived JSON Object: " + e);
            throw new AxiataException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] rules = null;

        rules = new ValidationRule[]{
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "destinationAddress", destinationAddress),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "resourceURL", resourceUrl),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "clientCorrelator", clientCorrelator),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "notifyURL", notifyUrl),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),};
        
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
