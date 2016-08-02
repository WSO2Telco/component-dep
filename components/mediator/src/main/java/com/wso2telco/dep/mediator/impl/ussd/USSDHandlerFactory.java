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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.oneapivalidation.exceptions.CustomException;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating USSDHandler objects.
 */
public class USSDHandlerFactory {

    /** The log. */
    private static Log log = LogFactory.getLog(USSDHandlerFactory.class);
    
    /**
     * Creates a new USSDHandler object.
     *
     * @param ResourceURL the resource url
     * @param executor the executor
     * @return the USSD handler
     */
    public static USSDHandler createHandler(String ResourceURL, USSDExecutor executor) {
        String sendUSSDKeyString = "outbound";
        String retrieveUSSDString = "inbound";
        String moUssdSubsciption = "inbound/subscriptions";

        String lastWord = ResourceURL.substring(ResourceURL.lastIndexOf("/") + 1);
        RequestType apiType;
        USSDHandler handler = null;

        if (ResourceURL.toLowerCase().contains(moUssdSubsciption.toLowerCase())) {
             handler = new MOUSSDSubscribeHandler(executor);
        } else if (ResourceURL.toLowerCase().contains(sendUSSDKeyString.toLowerCase())) {
            apiType = RequestType.SEND_USSD;
            handler = new SendUSSDHandler(executor);
        } else if (ResourceURL.toLowerCase().contains(retrieveUSSDString.toLowerCase())) {
            apiType = RequestType.RETRIEVE_USSD;
            handler = new USSDInboundHandler(executor);
        } else {
            throw new CustomException("SVC0002", "", new String[]{null});
        }
//        return apiType;
        return handler;
    }

    /**
     * The Enum RequestType.
     */
    private enum RequestType {
        
        /** The send ussd. */
        SEND_USSD,
        
        /** The retrieve ussd. */
        RETRIEVE_USSD
    }
}
