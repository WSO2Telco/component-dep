/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.oneapivalidation.service.impl.sms.nb;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.wso2telco.oneapivalidation.exceptions.AxiataException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.util.UrlValidator;
import com.wso2telco.oneapivalidation.util.Validation;
import com.wso2telco.oneapivalidation.util.ValidationRule;
import org.json.JSONArray;

/**
 *
 * @author User
 */
public class ValidateNBOutboundSubscription implements IServiceValidate {

    private final String[] validationRules = {"outbound", "subscriptions"};

    public void validate(String json) throws AxiataException {

        String notifyURL = null;
        String callbackData = null;
        String clientCorrelator = null;
        int validationCount = 0;
        List<ValidationRule> rules = new ArrayList<ValidationRule>();

        try {

            JSONObject objJSONObject = new JSONObject(json);
            JSONObject objDeliveryReceiptSubscription = (JSONObject) objJSONObject.get("deliveryReceiptSubscription");

            if (!objDeliveryReceiptSubscription.isNull("clientCorrelator")) {
                clientCorrelator = nullOrTrimmed(objDeliveryReceiptSubscription.getString("clientCorrelator"));
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator));
            } else {
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator));
            }

            if (!objDeliveryReceiptSubscription.isNull("senderAddresses")) {
                JSONArray senderAddressesArray = objDeliveryReceiptSubscription.getJSONArray("senderAddresses");
                List<String[]> addressess = new ArrayList<String[]>();
                if (senderAddressesArray.length() != 0) {
                    for (int a = 0; a < senderAddressesArray.length(); a++) {
                        if (!senderAddressesArray.isNull(a)) {
                            String[] senderAddressArray = new String[2];
                            String senderAddress = null;
                            String[] operatorCodeArray = new String[2];
                            String operatorCode = null;
                            String[] filterCriteriaArray = new String[2];
                            String filterCriteria = null;

                            JSONObject senderAddressesObject = (JSONObject) senderAddressesArray.getJSONObject(a);
                            if (!senderAddressesObject.isNull("senderAddress")) {
                                senderAddress = nullOrTrimmed(senderAddressesObject.getString("senderAddress"));
                                senderAddressArray[0] = "senderAddress";
                                senderAddressArray[1] = senderAddress;
                                addressess.add(senderAddressArray);
                            } else {
                                senderAddressArray[0] = "senderAddress";
                                senderAddressArray[1] = null;
                                addressess.add(senderAddressArray);
                            }

                            if (!senderAddressesObject.isNull("operatorCode")) {
                                operatorCode = nullOrTrimmed(senderAddressesObject.getString("operatorCode"));
                                operatorCodeArray[0] = "operatorCode";
                                operatorCodeArray[1] = operatorCode;
                                addressess.add(operatorCodeArray);
                            } else {
                                operatorCodeArray[0] = "operatorCode";
                                operatorCodeArray[1] = null;
                                addressess.add(operatorCodeArray);
                            }

                            if (!senderAddressesObject.isNull("filterCriteria")) {
                                filterCriteria = nullOrTrimmed(senderAddressesObject.getString("filterCriteria"));
                                filterCriteriaArray[0] = "filterCriteria";
                                filterCriteriaArray[1] = filterCriteria;
                                addressess.add(filterCriteriaArray);
                            } else {
                                filterCriteriaArray[0] = "filterCriteria";
                                filterCriteriaArray[1] = null;
                                addressess.add(filterCriteriaArray);
                            }
                        }
                    }

                    for (validationCount = 0; validationCount < addressess.size(); validationCount++) {
                        String[] details = addressess.get(validationCount);
                        if (details[0].equalsIgnoreCase("senderAddresses")) {
                            rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "senderAddresses", details[1]));
                        } else if (details[0].equalsIgnoreCase("operatorCode")) {
                            rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "operatorCode", details[1]));
                        } else if (details[0].equalsIgnoreCase("filterCriteria")) {
                            rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "filterCriteria", details[1]));
                        }
                    }
                } else {
                    rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "senderAddress", ""));
                    rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "operatorCode", ""));
                    rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "filterCriteria", ""));
                }
            } else {
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "senderAddress", ""));
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "operatorCode", ""));
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "filterCriteria", ""));
            }

            JSONObject objCallbackReference = (JSONObject) objDeliveryReceiptSubscription.get("callbackReference");
            if (!objCallbackReference.isNull("callbackData")) {
                callbackData = nullOrTrimmed(objCallbackReference.getString("callbackData"));
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData));
            } else {
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData));
            }

            if (!objCallbackReference.isNull("notifyURL")) {
                notifyURL = nullOrTrimmed(objCallbackReference.getString("notifyURL"));
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL, "notifyURL", notifyURL));
            } else {
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL, "notifyURL", notifyURL));
            }
        } catch (Exception e) {
            System.out.println("Manipulating recived JSON Object: " + e);
            throw new AxiataException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] validationRuleArray = new ValidationRule[rules.size()];
        validationRuleArray = rules.toArray(validationRuleArray);

        Validation.checkRequestParams(validationRuleArray);
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
