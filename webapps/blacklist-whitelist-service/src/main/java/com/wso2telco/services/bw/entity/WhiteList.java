/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.services.bw.entity;

import java.io.Serializable;

public class WhiteList implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -9154995737075601217L;
	private String subscriptionID;
    private String apiID;
    private String applicationID;
    
    
    public WhiteList() {
    }
    
    public String getSubscriptionID(){
        return this.subscriptionID;
    }
    
    public String getAPIID(){
        return this.apiID;
    }
    
    public String getApplicationID(){
        return this.applicationID;
    }
    
    public void setSubscriptionID(String subscriptionID){
        this.subscriptionID = subscriptionID;
    }
    
    public void setAPIID(String apiID){
        this.apiID = apiID;
    }
    
    public void setApplicationID(String applicationID){
        this.applicationID = applicationID;
    }
   
}
