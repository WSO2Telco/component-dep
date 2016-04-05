/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.oneapivalidation.service.impl.ussd;

import com.wso2telco.oneapivalidation.exceptions.AxiataException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.util.UrlValidator;


/**
 *
 * @author User
 */
public class ValidateUssdCancelSubscription implements IServiceValidate {

    private final String[] validationRules = {"ussd", "*", "inbound", "subscriptions", "*"};

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

    public void validate(String[] params) throws AxiataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
