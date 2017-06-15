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
package com.wso2telco.dep.logging;

import org.apache.synapse.MessageContext;


public class UniqueIDGenerator {
    
	private static long id;
    private static final String REQUEST_ID = "mife.prop.requestId";

    public static synchronized String generateAndSetUniqueID(String axtype, MessageContext context,String appid) {       
        String requestId = System.currentTimeMillis()+axtype+appid+"0"+ id++;
        context.setProperty(REQUEST_ID, requestId);
        return requestId;
    }
    
    public static String resourceURL(String resUrl,String reqid) {        
       return resUrl.substring(0,resUrl.lastIndexOf("/")+1)+reqid;
    }
    
    public static String resourceURLWithappend(String resUrl,String reqid, String msg) {        
       return resUrl.substring(0,resUrl.lastIndexOf("/")+1)+reqid+"/"+msg;
    }
    
    public static String retriveMsgResourceURL(String resUrl, String msg){
        return resUrl.substring(0,resUrl.lastIndexOf("/")+1)+msg;
    }
    
    public static String getRequestID(MessageContext messageContext) {
    	String requestId = "";
    	if (messageContext != null) {
        	requestId = (String) messageContext.getProperty(REQUEST_ID);
    	}
    	return requestId;
    }
}
