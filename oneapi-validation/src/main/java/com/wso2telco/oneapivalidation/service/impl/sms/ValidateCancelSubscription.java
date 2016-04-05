/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.oneapivalidation.service.impl.sms;

import com.wso2telco.oneapivalidation.exceptions.AxiataException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.util.UrlValidator;
import com.wso2telco.oneapivalidation.util.Validation;
import com.wso2telco.oneapivalidation.util.ValidationRule;

/**
 *
 * @author User
 */
public class ValidateCancelSubscription implements IServiceValidate {

    private final String[] validationRules = {"inbound", "subscriptions", "*"};
    
    public void validate(String json) throws AxiataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void validate(String[] params) throws AxiataException {
        //Send parameters within String array according to following order, String[] params = "subscriptionId";
        
        String subId = nullOrTrimmed(params[0]);
        
        ValidationRule[] rules = {
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "subscriptionId", subId),};
        
        Validation.checkRequestParams(rules);
    }

    private static String nullOrTrimmed(String s) {
        String rv = null;
        if (s != null && s.trim().length() > 0) {
            rv = s.trim();
        }
        return rv;
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
