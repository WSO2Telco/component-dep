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
package com.wso2telco.dep.mediator.internal;

import java.util.ArrayList;
import java.util.List;
import org.apache.synapse.MessageContext;
import org.json.JSONArray;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Interface Processor.
 */
public interface Processor {

    /**
     * Gets the app id.
     *
     * @param context the context
     * @return the app id
     */
    public String getAppID(MessageContext context); //throws Exception;

    /**
     * Gets the result list.
     *
     * @param response the response
     * @return the result list
     */
    public JSONArray getResultList(String response); //throws Exception;

    /**
     * Sets the batch size.
     *
     * @param uri the uri
     * @param body the body
     * @param batchSize the batch size
     * @return the API call
     */
    public APICall setBatchSize(String uri, String body, int batchSize); //throws Exception;

    /**
     * Generate response.
     *
     * @param context the context
     * @param results the results
     * @param resourceURL the resource url
     * @param responses the responses
     * @return the JSON object
     */
    public JSONObject generateResponse(MessageContext context, JSONArray results, String resourceURL, ArrayList<String> responses); //throws Exception;

    /**
     * Generate response.
     *
     * @param context the context
     * @param inboundSMSMessageList the inbound sms message list
     * @param resourceURL the resource url
     * @param responses the responses
     * @return the JSON object
     */
    public JSONObject generateResponse(MessageContext context, List<com.wso2telco.dep.mediator.entity.smsmessaging.northbound.InboundSMSMessage> inboundSMSMessageList, String resourceURL, ArrayList<String> responses); //throws Exception;
}
