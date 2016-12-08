/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.subscriptionvalidator.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.rest.RESTConstants;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;

import com.wso2telco.dep.subscriptionvalidator.exceptions.ValidatorException;
import com.wso2telco.dep.subscriptionvalidator.services.MifeValidator;

// TODO: Auto-generated Javadoc
/**
 * The Class ValidatorUtils.
 */
public class ValidatorUtils {
    
    /** The Constant log. */
    private static final Log log = LogFactory.getLog(ValidatorUtils.class);

    /**
     * Gets the validator for subscription.
     *
     * @param applicationId the application id
     * @param apiId the api id
     * @return the validator for subscription
     * @throws ValidatorException the validator exception
     */
    public static MifeValidator getValidatorForSubscription(int applicationId, int apiId) throws ValidatorException {

        String className = ValidatorDBUtils.getValidatorClassForSubscription(applicationId, apiId);
        log.debug("Validator Class Name: " + className);

        if (className == null) {
            throw new ValidatorException("No Validator class defined for the subscription with appID: " +
                    applicationId + " apiID: " + apiId);
        }
        try {
            MifeValidator validator = (MifeValidator) Class.forName(className).newInstance();
            return validator;
        } catch (Exception e) {
            log.error("Error occured while loading Validator class: " + className, e);
            throw new ValidatorException("Error occured while loading Validator class: " + className, e);
        }
    }

    /**
     * Gets the validator for subscription.
     *
     * @param mc the mc
     * @return the validator for subscription
     * @throws APIManagementException the API management exception
     * @throws ValidatorException the validator exception
     */
    public static MifeValidator getValidatorForSubscription(MessageContext mc) throws APIManagementException,
            ValidatorException {

        AuthenticationContext authContext = APISecurityUtils.getAuthenticationContext(mc);
        int applicationId = Integer.parseInt(authContext.getApplicationId());

        String api_version = (String) mc.getProperty(RESTConstants.SYNAPSE_REST_API);
        api_version = api_version.replace("--", "_").replace(":v", "_");
        APIIdentifier apiIdentifier = new APIIdentifier(api_version);
        int apiId = ValidatorDBUtils.getApiId(apiIdentifier);

        return getValidatorForSubscription(applicationId, apiId);
    }


    /**
     * Gets the validator for subscription using properties in the message context.
     *
     * @param mc the mc
     * @return the validator for subscription
     * @throws ValidatorException the validator exception
     */
    public static MifeValidator getValidatorForSubscriptionFromMessageContext(MessageContext mc) throws
            ValidatorException {

        int applicationId = Integer.parseInt((String)mc.getProperty("APPLICATION_ID"));
        int apiId = Integer.parseInt((String)mc.getProperty("API_ID"));

        return getValidatorForSubscription(applicationId, apiId);
    }

}
