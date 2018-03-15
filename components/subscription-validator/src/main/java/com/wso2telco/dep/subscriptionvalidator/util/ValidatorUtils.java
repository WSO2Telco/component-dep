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

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.wso2.carbon.apimgt.api.APIManagementException;

import com.wso2telco.dep.subscriptionvalidator.exceptions.ValidatorException;
import com.wso2telco.dep.subscriptionvalidator.services.MifeValidator;

// TODO: Auto-generated Javadoc
/**
 * The Class ValidatorUtils.
 */
public class ValidatorUtils {

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(ValidatorUtils.class);

    private static List<ValidatorClassDTO> validatorClassDTOs = null;

    /**
     * Gets the validator for subscription.
     *
     * @param applicationId the application id
     * @param apiId the api id
     * @return the validator for subscription
     * @throws ValidatorException the validator exception
     */
    /*public static MifeValidator getValidatorForSubscription(int applicationId, int apiId) throws ValidatorException {

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
    }*/
    private static String getValidatorClassName(int applicationId, int apiId){
    	String className = null;
    	for(ValidatorClassDTO classDTO : validatorClassDTOs){
    		if(classDTO.getApp() == applicationId && classDTO.getApi() == apiId){
    			className = classDTO.getClassName();
    			log.debug("Validator Class Name: " + className);
    			break;
    		}
    	}
    	
    	return className;
    }
    
    public static MifeValidator getValidatorForSubscription(int applicationId, int apiId) throws ValidatorException {
    	String className = null;
    	if(validatorClassDTOs == null || validatorClassDTOs.isEmpty()){
    		validatorClassDTOs = ValidatorDBUtils.getValidatorClassForSMSSubscription();
    	}
    	className = getValidatorClassName(applicationId, apiId);
    	if(className == null){
    		validatorClassDTOs = ValidatorDBUtils.getValidatorClassForSMSSubscription();
    		className = getValidatorClassName(applicationId, apiId);
    	}

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

     //   AuthenticationContext authContext = APISecurityUtils.getAuthenticationContext(mc);
      //  int applicationId = Integer.parseInt(authContext.getApplicationId());
        int applicationId = Integer.parseInt((String)mc.getProperty("APPLICATION_ID"));
        //String api_version = (String) mc.getProperty(RESTConstants.SYNAPSE_REST_API);
        //api_version = api_version.replace("--", "_").replace(":v", "_");
        //APIIdentifier apiIdentifier = new APIIdentifier(api_version);
        //int apiId = ValidatorDBUtils.getApiId(apiIdentifier);
        int apiId = Integer.parseInt((String) mc.getProperty("API_ID"));

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

        String apiIdString = null;
        int applicationId = Integer.parseInt((String)mc.getProperty("APPLICATION_ID"));
        Object headers = ((Axis2MessageContext) mc).getAxis2MessageContext()
                .getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
        if (headers != null && headers instanceof Map) {
            Map headersMap = (Map) headers;
            apiIdString = (String) headersMap.get("API_ID");

        }
        int apiId = Integer.parseInt(apiIdString);

        return getValidatorForSubscription(applicationId, apiId);
    }

}
