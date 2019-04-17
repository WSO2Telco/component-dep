/*******************************************************************************
 * Copyright  (c) 2019, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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
package com.wso2telco.dep.user.masking.utils;

/**
 * API type enum
 */
public enum APIType {
	USSD("ussd", "Unstructured Supplementary Service Data"),
	PAYMENT("payment","Payment API Service"),
	PAYMENT_V2("payment-v2","Payment API Service"),
	LOCATION("location","location API Service"),
	SMS("smsmessaging","SMS Messageing API Service ");
	private String key;
	private String description;

	APIType(final String key, final String description) {
		this.key = key;
		this.description = description;
	}

	public String getCode(){
		return this.key;
	}
}
