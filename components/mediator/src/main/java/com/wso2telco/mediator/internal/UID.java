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
package com.wso2telco.mediator.internal;

 

 


import org.apache.synapse.MessageContext;

import com.wso2telco.datapublisher.DataPublisherConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class UID.
 */
public class UID {
    
    /** The id. */
    //private static long startTime = System.currentTimeMillis();
    private static long id;

    /**
     * Gets the unique id.
     *
     * @param axtype the axtype
     * @param context the context
     * @param appid the appid
     * @return the unique id
     */
    public static synchronized String getUniqueID(String axtype, MessageContext context,String appid) {       
        String requestId = System.currentTimeMillis()+axtype+appid+"0"+ id++;
        context.setProperty(DataPublisherConstants.REQUEST_ID, requestId);
        return requestId;
    }
    
    /**
     * Resource url.
     *
     * @param resUrl the res url
     * @param reqid the reqid
     * @return the string
     */
    public static String resourceURL(String resUrl,String reqid) {        
       return resUrl.substring(0,resUrl.lastIndexOf("/")+1)+reqid;
    }
    
    /**
     * Resource url withappend.
     *
     * @param resUrl the res url
     * @param reqid the reqid
     * @param msg the msg
     * @return the string
     */
    public static String resourceURLWithappend(String resUrl,String reqid, String msg) {        
       return resUrl.substring(0,resUrl.lastIndexOf("/")+1)+reqid+"/"+msg;
    }
    
}


