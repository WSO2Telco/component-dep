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
package com.wso2telco.dep.oneapivalidation.service.impl.smsmessaging.northbound;


import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.util.UrlValidator;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;

 
// TODO: Auto-generated Javadoc
/**
 * The Class ValidateNBRetrieveSms.
 */
public class ValidateNBRetrieveSms implements IServiceValidate {

    /** The validation rules. */
    private final String[] validationRules = {"inbound", "registrations", "messages"};

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String[])
     */
    public void validate(String[] params) throws CustomException {
        //Send parameters within String array according to following order, String[] params = "registrationId", "maxBatchSize";
        String regId = nullOrTrimmed(params[0]);
        String maxBatchSize = nullOrTrimmed(params[1]);

        ValidationRule[] rules = {
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_NUMBER, "registrationId", regId),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "maxBatchSize", maxBatchSize),};

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
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String)
     */
    public void validate(String json) throws CustomException {
         
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
            throw new CustomException("POL0299", "Unexpected Error", new String[]{""});
        }
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

        //remove batchSize param
        int reqlength = requestParts.length - 1;
        requestParts[reqlength] = requestParts[reqlength].split("\\?")[0];

        UrlValidator.validateRequest(requestParts, validationRules);
    }

}
