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
package com.wso2telco.dep.oneapivalidation.util;

import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import org.apache.log4j.Logger;

import static com.wso2telco.dep.oneapivalidation.util.Validation.isCorrectlyFormattedNumber;


// TODO: Auto-generated Javadoc
/**
 * The Class UrlValidator.
 */
public class UrlValidator {

    /** The logger. */
    static Logger logger = Logger.getLogger(UrlValidator.class);

    /**
     * Validate request.
     *
     * @param requestParts the request parts
     * @param validationRules the validation rules
     * @return true, if successful
     */
    public static boolean validateRequest(String[] requestParts, String[] validationRules) {

        boolean valid = true;

        if (requestParts == null || requestParts.length < validationRules.length) {
            valid = false;
            //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Request is missing required URI components", null);
            throw new CustomException("SVC0002", "Request is missing required URI components", new String[]{null});
        } else {
            String errorMessage = null;
            for (int i = 0; i < validationRules.length; i++) {
                logger.debug("Validation of " + requestParts[i] + " against " + validationRules[i]);
                if (validationRules[i].equals("*")) {
                    if (requestParts[i] == null || requestParts[i].isEmpty()) {
                        errorMessage = "parameter" + " at component [" + i + "] ";
                        valid = false;
                    } else if (!isCorrectlyFormattedNumber(requestParts[i])) {

                        throw new CustomException("SVC0004", "endUserId format invalid. %1",
                                new String[]{requestParts[i]});
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
                throw new CustomException("SVC0002", "Request URI missing required component(s): ", new String[]{errorMessage});
            }
        }
        return valid;
    }
}
