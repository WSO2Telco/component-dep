/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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
package com.wso2telco.verificationhandler.verifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;

import java.util.logging.Level;

import java.util.logging.Logger;

 
// TODO: Auto-generated Javadoc
/**
 * The Class APIUtil.
 */
public class APIUtil {

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(APIUtil.class);
    
    
    /**
     * Gets the api.
     *
     * @param messageContext the message context
     * @return the api
     */
    public static String getAPI(MessageContext messageContext){
        
        String resourceUrl=(String) messageContext.getProperty("REST_FULL_REQUEST_PATH");

        String apiContext = (String) messageContext.getProperty("api.ut.context");

        String apiName = apiContext.substring(apiContext.indexOf("/") + 1);

        return apiName;
    }
    
}
