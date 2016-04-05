/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.oneapivalidation.service.impl.payment;


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
public class ValidateReleaseReservation implements IServiceValidate {

    private final String[] validationRules = {"*", "transactions", "amountReservation", "*"};

    public void validate(String json) throws AxiataException {

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
            throw new AxiataException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] rules = {
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionOperationStatus", transactionOperationStatus, "released"),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "referenceSequence", Integer.valueOf(referenceSequence)),};

        Validation.checkRequestParams(rules);
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

        Validation.checkRequestParams(rules);
    }
    
    private static String nullOrTrimmed(String s) {
        String rv = null;
        if (s != null && s.trim().length() > 0) {
            rv = s.trim();
        }
        return rv;
    }
}
