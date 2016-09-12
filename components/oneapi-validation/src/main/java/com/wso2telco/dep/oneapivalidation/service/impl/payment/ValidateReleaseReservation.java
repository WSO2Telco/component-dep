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
package com.wso2telco.dep.oneapivalidation.service.impl.payment;


import org.json.JSONObject;

import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.util.UrlValidator;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;

 
// TODO: Auto-generated Javadoc
/**
 * The Class ValidateReleaseReservation.
 */
public class ValidateReleaseReservation implements IServiceValidate {

    /** The validation rules. */
    private final String[] validationRules = {"*", "transactions", "amountReservation", "*"};

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String)
     */
    public void validate(String json) throws CustomException {

        String endUserId = null;
        int referenceSequence = 0;
        String transactionOperationStatus = null;

        try {
            JSONObject objJSONObject = new JSONObject(json);
            JSONObject objAmountReservationTransaction = (JSONObject) objJSONObject.get("amountReservationTransaction");

            if (objAmountReservationTransaction.get("endUserId") != null) {
                endUserId = nullOrTrimmed(objAmountReservationTransaction.get("endUserId").toString());
            }
            if (objAmountReservationTransaction.get("referenceSequence") != null) {
                referenceSequence = Integer.parseInt(nullOrTrimmed(objAmountReservationTransaction.get("referenceSequence").toString()));
            }
            if (objAmountReservationTransaction.get("transactionOperationStatus") != null) {
                transactionOperationStatus = nullOrTrimmed(objAmountReservationTransaction.get("transactionOperationStatus").toString());
            }
        } catch (Exception e) {
            System.out.println("Manipulating recived JSON Object: " + e);
            throw new CustomException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] rules = {
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionOperationStatus", transactionOperationStatus, "released"),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "referenceSequence", Integer.valueOf(referenceSequence)),};

        Validation.checkRequestParams(rules);
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

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String[])
     */
    public void validate(String[] params) throws CustomException {
        String transactionId = nullOrTrimmed(params[0]);

        ValidationRule[] rules = {
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionId", transactionId),};

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
}
