package com.wso2telco.subscriptionvalidator.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.rest.RESTConstants;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;
import com.wso2telco.subscriptionvalidator.exceptions.ValidatorException;
import com.wso2telco.subscriptionvalidator.services.MifeValidator;

public class ValidatorUtils {
    private static final Log log = LogFactory.getLog(ValidatorUtils.class);

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

}
