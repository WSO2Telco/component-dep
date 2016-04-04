 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.oneapivalidation.impl;

import com.wso2telco.oneapivalidation.AxiataException;
import com.wso2telco.oneapivalidation.IServiceValidate;
import com.wso2telco.oneapivalidation.UrlValidator;
import com.wso2telco.oneapivalidation.ValidationRule;
import com.wso2telco.oneapivalidation.ValidationNew;


/**
 *
 * @author User
 */
public class ValidateDeliveryStatus implements IServiceValidate {

    private final String[] validationRules = {"outbound", "*", "requests", "*", "deliveryInfos"};

    public void validate(String[] params) throws AxiataException {


        String senderAddress = nullOrTrimmed(params[0]);
        String requestId = nullOrTrimmed(params[1]);

        ValidationRule[] rules = {
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "senderAddress", senderAddress),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "requestId", requestId),};
        
        ValidationNew.checkRequestParams(rules);

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

        UrlValidator.validateRequest(requestParts, validationRules);
    }

    private static String nullOrTrimmed(String s) {
        String rv = null;
        if (s != null && s.trim().length() > 0) {
            rv = s.trim();
        }
        return rv;
    }
}
