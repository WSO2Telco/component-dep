/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.oneapivalidation.service.impl.sms.nb;


import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
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
public class ValidateNBRetrieveSms implements IServiceValidate {

    private final String[] validationRules = {"inbound", "registrations", "messages"};

    public void validate(String[] params) throws AxiataException {
        //Send parameters within String array according to following order, String[] params = "registrationId", "maxBatchSize";
        String regId = nullOrTrimmed(params[0]);
        String maxBatchSize = nullOrTrimmed(params[1]);

        ValidationRule[] rules = {
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_NUMBER, "registrationId", regId),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "maxBatchSize", maxBatchSize),};

        Validation.checkRequestParams(rules);

    }

    private static String nullOrTrimmed(String s) {
        String rv = null;
        if (s != null && s.trim().length() > 0) {
            rv = s.trim();
        }
        return rv;
    }

    public void validate(String json) throws AxiataException {
        /*throw new UnsupportedOperationException("Not supported yet.");*/
        String maxBatchSize = null;
        String clientCorrelator = null;
        int validationCount = 0;
        List<ValidationRule> rules = new ArrayList<ValidationRule>();

        try {
            JSONObject objJSONObject = new JSONObject(json);

            JSONObject objInboundSMSMessages = (JSONObject) objJSONObject.get("inboundSMSMessages");

            if (!objInboundSMSMessages.isNull("registrations")) {
                JSONArray registrationsArray = objInboundSMSMessages.getJSONArray("registrations");
                List<String[]> registrations = new ArrayList<String[]>();

                if (registrationsArray.length() != 0) {
                    for (int a = 0; a < registrationsArray.length(); a++) {
                        if (!registrationsArray.isNull(a)) {
                            String[] registrationIDArray = new String[2];
                            String registrationID = null;
                            String[] operatorCodeArray = new String[2];
                            String operatorCode = null;
                            String[] criteriaArray = new String[2];
                            String criteria = null;

                            JSONObject registrationsObject = (JSONObject) registrationsArray.getJSONObject(a);
                            if (!registrationsObject.isNull("registrationID")) {
                                registrationID = nullOrTrimmed(registrationsObject.getString("registrationID"));
                                registrationIDArray[0] = "registrationID";
                                registrationIDArray[1] = registrationID;
                                registrations.add(registrationIDArray);
                            } else {
                                registrationIDArray[0] = "registrationID";
                                registrationIDArray[1] = null;
                                registrations.add(registrationIDArray);
                            }

                            if (!registrationsObject.isNull("operatorCode")) {
                                operatorCode = nullOrTrimmed(registrationsObject.getString("operatorCode"));
                                operatorCodeArray[0] = "operatorCode";
                                operatorCodeArray[1] = operatorCode;
                                registrations.add(operatorCodeArray);
                            } else {
                                operatorCodeArray[0] = "operatorCode";
                                operatorCodeArray[1] = null;
                                registrations.add(operatorCodeArray);
                            }

                            if (!registrationsObject.isNull("criteria")) {
                                criteria = nullOrTrimmed(registrationsObject.getString("criteria"));
                                criteriaArray[0] = "criteria";
                                criteriaArray[1] = criteria;
                                registrations.add(criteriaArray);
                            } else {
                                criteriaArray[0] = "criteria";
                                criteriaArray[1] = null;
                                registrations.add(criteriaArray);
                            }
                        }
                    }

                    for (validationCount = 0; validationCount < registrations.size(); validationCount++) {
                        String[] details = registrations.get(validationCount);
                        if (details[0].equalsIgnoreCase("registrationID")) {
                            rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "registrationID", details[1]));
                        } else if (details[0].equalsIgnoreCase("operatorCode")) {
                            rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "operatorCode", details[1]));
                        } else if (details[0].equalsIgnoreCase("criteria")) {
                            rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "criteria", details[1]));
                        }
                    }
                } else {
                    rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "registrationID", ""));
                    rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "operatorCode", ""));
                    rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "criteria", ""));
                }
            } else {
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "registrationID", ""));
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "operatorCode", ""));
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "criteria", ""));
            }

            if (!objInboundSMSMessages.isNull("maxBatchSize")) {
                maxBatchSize = nullOrTrimmed(objInboundSMSMessages.getString("maxBatchSize"));
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "maxBatchSize", maxBatchSize));
            } else {
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "maxBatchSize", maxBatchSize));
            }

            if (!objInboundSMSMessages.isNull("clientCorrelator")) {
                clientCorrelator = nullOrTrimmed(objInboundSMSMessages.getString("clientCorrelator"));
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator));
            } else {
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator));
            }

            ValidationRule[] validationRuleArray = new ValidationRule[rules.size()];
            validationRuleArray = rules.toArray(validationRuleArray);

            Validation.checkRequestParams(validationRuleArray);
        } catch (Exception e) {
            System.out.println("Manipulating recived JSON Object: " + e);
            throw new AxiataException("POL0299", "Unexpected Error", new String[]{""});
        }
    }

    public void validateUrl(String pathInfo) throws AxiataException {
        String[] requestParts = null;
        if (pathInfo != null) {
            if (pathInfo.startsWith("/")) {
                pathInfo = pathInfo.substring(1);
            }
            requestParts = pathInfo.split("/");
        }

        //remove batchSize param
        int reqlength = requestParts.length - 1;
        requestParts[reqlength] = requestParts[reqlength].split("\\?")[0];

        UrlValidator.validateRequest(requestParts, validationRules);
    }

}
