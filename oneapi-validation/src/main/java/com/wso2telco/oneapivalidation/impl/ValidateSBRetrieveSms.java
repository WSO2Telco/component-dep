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

/**
 *
 * @author User
 */
public class ValidateSBRetrieveSms implements IServiceValidate{

    private final String[] validationRules = {"inbound", "registrations", "*", "*", "messages"};

    public void validate(String[] params) throws AxiataException {
        //Send parameters within String array according to following order, String[] params = "registrationId", "maxBatchSize";
        String regId = nullOrTrimmed(params[0]);
        String criteria = nullOrTrimmed(params[1]);
        String maxBatchSize = nullOrTrimmed(params[2]);
        
        ValidationRule[] rules = {
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_NUMBER, "registrationId", regId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "criteria", criteria),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "maxBatchSize", maxBatchSize),};
        
        ValidationNew.checkRequestParams(rules);
        
    }

    private static String nullOrTrimmed(String s) {
        String rv = null;
        if (s != null && s.trim().length() > 0) {
            rv = s.trim();
        }
        return rv;
    }

    public void validate(String json) throws AxiataException {
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

        //remove batchSize param
        int reqlength = requestParts.length -1;
        requestParts[reqlength] = requestParts[reqlength].split("\\?")[0];

        UrlValidator.validateRequest(requestParts, validationRules);
    }
    
}
