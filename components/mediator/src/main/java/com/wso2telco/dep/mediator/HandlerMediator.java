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
package com.wso2telco.dep.mediator;


import com.wso2telco.dep.mediator.internal.Base64Coder;
import com.wso2telco.oneapivalidation.exceptions.CustomException;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


 
// TODO: Auto-generated Javadoc
/**
 * The Class HandlerMediator.
 */
public class HandlerMediator extends AbstractMediator {

    /** The log. */
    @SuppressWarnings("unused")
    
    private static Log log = LogFactory.getLog(HandlerMediator.class);

    /** The executor class. */
    private String executorClass;
    
    /** The nb publisher. */
    private NorthboundPublisher nbPublisher;
    
     
    /* (non-Javadoc)
     * @see org.apache.synapse.Mediator#mediate(org.apache.synapse.MessageContext)
     */
    public boolean mediate(MessageContext context) {

        
        String jsonBody = null;
        try {
            Class clazz = Class.forName(executorClass);
            RequestExecutor reqHandler = (RequestExecutor) clazz.newInstance();

            reqHandler.setApplicationid(storeApplication(context));

            reqHandler.initialize(context);
            reqHandler.validateRequest(reqHandler.getHttpMethod(), reqHandler.getSubResourcePath(),
                    reqHandler.getJsonBody(), context);
            jsonBody = reqHandler.getJsonBody().toString();
            reqHandler.execute(context);

        } catch (CustomException ax) {
            handleCustomException(ax.getErrcode(),ax.getErrvar(), ax, context, jsonBody);
            
        } catch (Exception axisFault) {            
            handleException("Unexpected error ", axisFault, context);
            return false;
        }

        return true;
    }
    
    /**
     * Handle axiata exception.
     *
     * @param errcode the errcode
     * @param errvar the errvar
     * @param ax the ax
     * @param context the context
     * @param jsonBody the json body
     */
    public void handleCustomException(String errcode, String[] errvar, CustomException ax, MessageContext context, String jsonBody) {
        context.setProperty("ERROR_CODE",errcode);
        context.setProperty("errvar",errvar[0]);
        
        //NB Data Publish : Hiranya
        if(nbPublisher == null){
            nbPublisher = new NorthboundPublisher();
        }
        nbPublisher.publishNBErrorResponseData(ax, jsonBody, context);
        
        handleException(ax.getErrmsg(), ax,context);
    }
    
    /**
     * Store application.
     *
     * @param context the context
     * @return the string
     * @throws AxisFault the axis fault
     */
    private String storeApplication(MessageContext context) throws AxisFault {
       String applicationid = null;
        
        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) context).getAxis2MessageContext();
        Object headers = axis2MessageContext.getProperty(
                org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);        
        if (headers != null && headers instanceof Map) {
            try {
                Map headersMap = (Map) headers;
                String jwtparam = (String)headersMap.get("x-jwt-assertion");
                String[] jwttoken = jwtparam.split("\\.");
                String jwtbody = Base64Coder.decodeString(jwttoken[1]);
                JSONObject jwtobj = new JSONObject(jwtbody);
                applicationid = jwtobj.getString("http://wso2.org/claims/applicationid");
                
            } catch (JSONException ex) {
                throw new AxisFault("Error retriving application id");
            }
        }
        
        return applicationid;
    }

    /**
     * Gets the executor class.
     *
     * @return the executor class
     */
    public String getExecutorClass() {
        return executorClass;
    }

    /**
     * Sets the executor class.
     *
     * @param executorClass the new executor class
     */
    public void setExecutorClass(String executorClass) {
        this.executorClass = executorClass;
    }
}
