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

package com.wso2telco.dep.operatorservice.util;

public enum OparatorTable {
	
	OPERATORS("operators"),
	MERCHANT_OPCO_BLACKLIST("merchantopco_blacklist"),
	BLACKLIST_MSISDN("blacklistmsisdn"),
	SUBSCRIPTION_WHITELIST("subscription_WhiteList"),
	SUB_APPROVAL_OPERATORS("sub_approval_operators"),
	OPERATOR_ENDPOINTS("operatorendpoints"),
	OPERATOR_APPS("operatorapps"),
	ENDPOINT_APPS("endpointapps"),
	AM_APPLICATION_KEY_MAPPING("AM_APPLICATION_KEY_MAPPING"),
	AM_APPLICATION("AM_APPLICATION"),
	IDN_OAUTH_CONSUMER_APPS("IDN_OAUTH_CONSUMER_APPS"),
	IDN_OAUTH2_ACCESS_TOKEN("IDN_OAUTH2_ACCESS_TOKEN"),
	UM_USER("um_user"),
	UM_USER_ROLE("um_user_role"),
	UM_ROLE("um_role");



	
	OparatorTable(String tObject) {
		
		this.tObject = tObject;
	}
	
	public String getTObject(){
		
		return this.tObject;
	}

	String tObject;
}
