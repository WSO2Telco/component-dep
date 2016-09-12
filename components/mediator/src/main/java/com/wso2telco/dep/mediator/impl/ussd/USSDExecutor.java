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
package com.wso2telco.dep.mediator.impl.ussd;

import com.wso2telco.dep.mediator.RequestExecutor;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.json.JSONException;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class USSDExecutor.
 */
public class USSDExecutor extends RequestExecutor {

    /** The log. */
    private Log log = LogFactory.getLog(USSDExecutor.class);

    /** The handler. */
    private USSDHandler handler;

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.RequestExecutor#execute(org.apache.synapse.MessageContext)
     */
    @Override
    public boolean execute(MessageContext context) throws CustomException, AxisFault, Exception {
        try {
            USSDHandler handler = getUSSDHandler(getSubResourcePath());
            return handler.handle(context);

        } catch (JSONException e) {
            log.error(e.getMessage());
            throw new CustomException("SVC0001", "", new String[]{"Request is missing required URI components"});
        }
    }

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.RequestExecutor#validateRequest(java.lang.String, java.lang.String, org.json.JSONObject, org.apache.synapse.MessageContext)
     */
    @Override
    public boolean validateRequest(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext
            context) throws Exception {

        USSDHandler handler = getUSSDHandler(requestPath);
        return handler.validate(httpMethod, requestPath, jsonBody, context);
    }

    /**
     * Gets the USSD handler.
     *
     * @param requestPath the request path
     * @return the USSD handler
     */
    private USSDHandler getUSSDHandler(String requestPath) {
        if (handler == null) {
            handler = USSDHandlerFactory.createHandler(requestPath, this);
        }
        return handler;
    }
}
