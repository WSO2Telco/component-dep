/*******************************************************************************
 * Copyright  (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.validator.handler;


import com.wso2telco.dep.validator.handler.exceptions.ValidatorException;
import com.wso2telco.dep.validator.handler.utils.ValidatorDBUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.AbstractHandler;
import org.apache.synapse.rest.RESTConstants;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;


import java.util.Map;

/**
 * This class is a synapse handler that would set API ID information to the header before passing the request to the
 * backend.
 * This handler need to be added at the <handlers> section of your created api configuration.
 */
public class APIInfoHandler extends AbstractHandler implements ManagedLifecycle {

    private static final Log log = LogFactory.getLog(APIInfoHandler.class);

    @Override
    public void init(SynapseEnvironment synapseEnvironment) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean handleRequest(MessageContext messageContext) {

        String api_version = (String) messageContext.getProperty(RESTConstants.SYNAPSE_REST_API);
        api_version = api_version.replace("--", "_").replace(":v", "_");
        APIIdentifier apiIdentifier = null;
        try {
            apiIdentifier = new APIIdentifier(api_version);
            int apiId = ValidatorDBUtils.getApiId(apiIdentifier);
            Object headers = ((Axis2MessageContext) messageContext).getAxis2MessageContext()
                    .getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
            if (headers != null && headers instanceof Map) {
                Map headersMap = (Map) headers;
                headersMap.put("API_ID", Integer.toString(apiId));
            }
        } catch (APIManagementException e) {
            log.error("Error while creating the APIIdentifier and retreiving api id from database", e);
        } catch (ValidatorException e) {
            log.error("Error while getting validator class", e);
        }
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) {
        return true;
    }

    /**
     * Method to call the util class for the validator class name retrieval.
     *
     * @param applicationId
     * @param apiId
     * @return
     * @throws ValidatorException
     */
    private static String getValidatorForSubscription(int applicationId, int apiId) throws ValidatorException {
        String className = ValidatorDBUtils.getValidatorClassForSubscription(applicationId, apiId);
        if (className == null) {
            throw new ValidatorException("No Validator class defined for the subscription with appID: " +
                    applicationId + " apiID: " + apiId);
        }
        return className;
    }
}
