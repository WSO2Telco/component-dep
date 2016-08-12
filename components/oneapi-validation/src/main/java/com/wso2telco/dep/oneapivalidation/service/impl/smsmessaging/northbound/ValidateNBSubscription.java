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
 * The Class ValidateNBSubscription.
 */
public class ValidateNBSubscription implements IServiceValidate {

    /** The validation rules. */
    private final String[] validationRules = {"inbound", "subscriptions"};

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String)
     */
    public void validate(String json) throws CustomException {

        String notifyURL = null;
        //String criteria = null;
        String notificationFormat = null;
        String clientCorrelator = null;
        String callbackData = null;
        int validationCount = 0;
        List<ValidationRule> rules = new ArrayList<ValidationRule>();

        try {

            JSONObject objJSONObject = new JSONObject(json);

            JSONObject objSubscription = (JSONObject) objJSONObject.get("subscription");

            if (!objSubscription.isNull("destinationAddresses")) {

                JSONArray destinationAddressesArray = objSubscription.getJSONArray("destinationAddresses");
                List<String[]> addressess = new ArrayList<String[]>();
                if (destinationAddressesArray.length() != 0) {
                    for (int a = 0; a < destinationAddressesArray.length(); a++) {
                        //addresses.add(nullOrTrimmed(addressArray.getString(a)));
                        if (!destinationAddressesArray.isNull(a)) {
                            String[] destinationAddressArray = new String[2];
                            String destinationAddress = null;
                            String[] operatorCodeArray = new String[2];
                            String operatorCode = null;
                            String[] criteriaArray = new String[2];
                            String criteria = null;

                            JSONObject destinationAddressesObject = (JSONObject) destinationAddressesArray.getJSONObject(a);
                            if (!destinationAddressesObject.isNull("destinationAddress")) {
                                destinationAddress = nullOrTrimmed(destinationAddressesObject.getString("destinationAddress"));
                                destinationAddressArray[0] = "destinationAddress";
                                destinationAddressArray[1] = destinationAddress;
                                addressess.add(destinationAddressArray);
                            } else {
                                destinationAddressArray[0] = "destinationAddress";
                                destinationAddressArray[1] = null;
                                addressess.add(destinationAddressArray);
                            }

                            if (!destinationAddressesObject.isNull("operatorCode")) {
                                operatorCode = nullOrTrimmed(destinationAddressesObject.getString("operatorCode"));
                                operatorCodeArray[0] = "operatorCode";
                                operatorCodeArray[1] = operatorCode;
                                addressess.add(operatorCodeArray);
                            } else {
                                operatorCodeArray[0] = "operatorCode";
                                operatorCodeArray[1] = null;
                                addressess.add(operatorCodeArray);
                            }

                            if (!destinationAddressesObject.isNull("criteria")) {
                                criteria = nullOrTrimmed(destinationAddressesObject.getString("criteria"));
                                criteriaArray[0] = "criteria";
                                criteriaArray[1] = criteria;
                                addressess.add(criteriaArray);
                            } else {
                                criteriaArray[0] = "criteria";
                                criteriaArray[1] = null;
                                addressess.add(criteriaArray);
                            }
                        }
                    }

                    for (validationCount = 0; validationCount < addressess.size(); validationCount++) {
                        String[] details = addressess.get(validationCount);
                        if (details[0].equalsIgnoreCase("destinationAddress")) {
                            rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "destinationAddress", details[1]));
                        } else if (details[0].equalsIgnoreCase("operatorCode")) {
                            rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "operatorCode", details[1]));
                        } else if (details[0].equalsIgnoreCase("criteria")) {
                            rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "criteria", details[1]));
                        }
                    }
                } else {
                    rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "destinationAddress", ""));
                    rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "operatorCode", ""));
                    rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "criteria", ""));
                }
            } else {
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "destinationAddress", ""));
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "operatorCode", ""));
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "criteria", ""));
            }

            JSONObject objCallbackReference = (JSONObject) objSubscription.get("callbackReference");
            if (!objCallbackReference.isNull("callbackData")) {
                callbackData = nullOrTrimmed(objCallbackReference.getString("callbackData"));
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "callbackData", callbackData));
            } else {
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "callbackData", callbackData));
            }

            if (!objCallbackReference.isNull("notifyURL")) {
                notifyURL = nullOrTrimmed(objCallbackReference.getString("notifyURL"));
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL, "notifyURL", notifyURL));
            } else {
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL, "notifyURL", notifyURL));
            }

            if (!objSubscription.isNull("notificationFormat")) {
                notificationFormat = nullOrTrimmed(objSubscription.getString("notificationFormat"));
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_JSON, "notificationFormat", notificationFormat));
            } else {
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_JSON, "notificationFormat", notificationFormat));
            }

            if (!objSubscription.isNull("clientCorrelator")) {
                clientCorrelator = nullOrTrimmed(objSubscription.getString("clientCorrelator"));
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator));
            } else {
                rules.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator));
            }
        } catch (Exception e) {
            System.out.println("Manipulating recived JSON Object: " + e);
            throw new CustomException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] validationRuleArray = new ValidationRule[rules.size()];
        validationRuleArray = rules.toArray(validationRuleArray);

        Validation.checkRequestParams(validationRuleArray);
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
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String[])
     */
    public void validate(String[] params) throws CustomException {
        throw new UnsupportedOperationException("Not supported yet.");
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
}
