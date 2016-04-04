/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.oneapivalidation;

import org.apache.log4j.Logger;

/**
 *
 * @author User
 */
public class UrlValidator {

    static Logger logger = Logger.getLogger(UrlValidator.class);

    public static boolean validateRequest(String[] requestParts, String[] validationRules) {

        boolean valid = true;

        if (requestParts == null || requestParts.length < validationRules.length) {
            valid = false;
            //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Request is missing required URI components", null);
            throw new AxiataException("SVC0002", "Request is missing required URI components", new String[]{null});
        } else {
            String errorMessage = null;
            for (int i = 0; i < validationRules.length; i++) {
                logger.debug("Validation of " + requestParts[i] + " against " + validationRules[i]);
                if (validationRules[i].equals("*")) {
                    if (requestParts[i] == null || requestParts[i].isEmpty()) {
                        errorMessage = "parameter" + " at component [" + i + "] ";
                        valid = false;
                    }
                } else if (!requestParts[i].equals(validationRules[i])) {
                    if (valid) {
                        errorMessage = validationRules[i] + " at component [" + i + "] ";
                        valid = false;
                    } else {
                        errorMessage += ", " + validationRules[i] + " at component [" + i + "] ";
                    }
                }
            }
            if (!valid) {
                //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Request URI missing required component(s): ", errorMessage);
                throw new AxiataException("SVC0002", "Request URI missing required component(s): ", new String[]{errorMessage});
            }
        }
        return valid;
    }
}
