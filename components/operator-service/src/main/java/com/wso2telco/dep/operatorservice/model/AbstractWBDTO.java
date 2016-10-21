/**
 * Copyright (c) 2015, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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

package com.wso2telco.dep.operatorservice.model;

abstract class AbstractWBDTO {
	private String[] userMSISDN;
	private String apiID;

	public String[] getUserMSISDN() {
		return userMSISDN;
	}

	public void setUserMSISDN(String[] userMSISDN) {
		this.userMSISDN = userMSISDN;
	}

	public void setUserMSISDN(String userMSISDN) {
		this.userMSISDN = new String[] { userMSISDN };
	}
	private boolean hasValidApiID_;
	public String getApiID() {
		return apiID;
	}

	public void setApiID(String apiID) {
		if(apiID!=null && apiID.trim().length()>0){
			
			this.apiID = apiID;
			this.hasValidApiID_ =true;
		}
		
	}
	
	public boolean hasValidApiID(){
		return hasValidApiID_;
	}

}
