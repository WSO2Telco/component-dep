package com.wso2telco.oneapivalidation.service.impl.sms;

import com.wso2telco.oneapivalidation.exceptions.AxiataException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.util.UrlValidator;
import com.wso2telco.oneapivalidation.util.Validation;
import com.wso2telco.oneapivalidation.util.ValidationRule;

public class ValidateDNCancelSubscription implements IServiceValidate {

    private final String[] validationRules = {"outbound", "subscriptions", "*"};
    
    public void validate(String json) throws AxiataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void validate(String[] params) throws AxiataException {
        
        String subId = nullOrTrimmed(params[0]);
        ValidationRule[] rules = {new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "subscriptionId", subId)};
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
