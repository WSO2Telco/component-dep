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
import java.util.Arrays;

public class WhiteListBulk implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6079118123267474720L;
	private String apiId;
    private String appId;
    private String apiName;
    private String userID;
    private String[] msisdnList;
    
    
    public String getAPIName(){
        return this.apiName;
    }
    
    public String setAPIName(String apiName){
        return this.apiName = apiName;
    }
    
    public String getUserID(){
        return this.userID;
    }
    
    public String setUserID(String userID){
        return this.userID = userID;
    }
    
    public String[] getMsisdnList(){
        return this.msisdnList;
    }
    
    public String[] setMsisdnList(String[] msisdnList){
        return this.msisdnList = msisdnList;
    }

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Override
	public String toString() {
		return "WhiteListBulk [apiId=" + apiId + ", appId=" + appId + ", apiName=" + apiName + ", userID=" + userID
				+ ", msisdnList=" + Arrays.toString(msisdnList) + "]";
	}


	
}
