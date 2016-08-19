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
package com.wso2telco.dep.mediator.util;

import com.wso2telco.utils.exception.ThrowableError;

public enum ErrorType implements ThrowableError {
	
	INVALID_NOTIFY_URL("MED0001","Invalid notify url"),
	INVALID_USSD_REQUEST_DID("MED0002","Invalid USSD request did"),
	INVALID_SUBSCRIBER_NAME("MED0003","Invalid subscriber name"),
	INVALID_DN_SUBSCRIPTION_ID("MED0004","Invalid delivery notification subscription id"),
	INVALID_SENDER_ADDRESS("MED0005","Invalid sender address"),
	INVALID_REQUEST_ID("MED0006","Invalid request id"),
	INVALID_MO_SUBSCRIPTION_ID("MED0007","Invalid mobile originated notification subscription id"),
	INVALID_REQUEST_ID_LIST("MED0008","Invalid request id list"),
	INVALID_MERCHANT_NAME("MED0008","Invalid merchant name"),
	INVALID_CONSUMER_KEY("MED0008","Invalid consumer key"),
	INVALID_OPERATOR_ID("MED0008","Invalid operator id"),
	INVALID_OPERATOR("MED0008","Invalid operator"),
	INVALID_USER_ID("MED0008","Invalid user id");

	private String code;
	private String desc;

	ErrorType(final String code, final String desc) {
		this.desc = desc;
		this.code = code;
	}

	@Override
	public String getMessage() {
		return this.desc;
	}

	@Override
	public String getCode() {
		return this.code;
	}
}
