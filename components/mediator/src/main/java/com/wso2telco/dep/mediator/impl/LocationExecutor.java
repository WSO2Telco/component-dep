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
package com.wso2telco.dep.mediator.impl;


import com.wso2telco.dep.datapublisher.DataPublisherConstants;
import com.wso2telco.dep.mediator.MSISDNConstants;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.RequestExecutor;
import com.wso2telco.dep.mediator.internal.ResourceURLUtil;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.impl.location.ValidateLocation;
import com.wso2telco.dep.subscriptionvalidator.util.ValidatorUtils;

import org.apache.axis2.AxisFault;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class LocationExecutor.
 */
public class LocationExecutor extends RequestExecutor {

    /** The occi. */
    private OriginatingCountryCalculatorIDD occi;

    /**
     * Instantiates a new location executor.
     */
    public LocationExecutor() {
        occi = new OriginatingCountryCalculatorIDD();
    }

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.RequestExecutor#execute(org.apache.synapse.MessageContext)
     */
    @Override
    public boolean execute(MessageContext context) throws CustomException, AxisFault, Exception {

        String[] params = new ResourceURLUtil().getParamValues(getSubResourcePath());
        context.setProperty(MSISDNConstants.USER_MSISDN, params[0].substring(5));
        OperatorEndpoint endpoint = null;
        if (ValidatorUtils.getValidatorForSubscription(context).validate(context)) {
            endpoint = occi.getAPIEndpointsByMSISDN(params[0].replace("tel:", ""), "location", getSubResourcePath(), true,
                    getValidoperators());
        }
        String sending_add = endpoint.getEndpointref().getAddress();

        String responseStr = makeGetRequest(endpoint, sending_add, getSubResourcePath(), true, context);

        removeHeaders(context);
        handlePluginException(responseStr);
        //set response re-applied
        setResponse(context, responseStr);
        ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("messageType", "application/json");
        ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("ContentType", "application/json");
        return true;
    }

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.RequestExecutor#validateRequest(java.lang.String, java.lang.String, org.json.JSONObject, org.apache.synapse.MessageContext)
     */
    @Override
    public boolean validateRequest(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context) throws Exception {
        
        context.setProperty(DataPublisherConstants.OPERATION_TYPE, 300);
        
        if (!httpMethod.equalsIgnoreCase("GET")) {
            ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 405);
            throw new Exception("Method not allowed");
        }
        String[] params = new ResourceURLUtil().getParamValues(requestPath);
        ValidateLocation validator = new ValidateLocation();
        validator.validateUrl(requestPath);
        validator.validate(params);

        return true;
    }
}
