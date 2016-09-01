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

package com.wso2telco.dep.mediator.util;

public enum ErrorHolder {
	INVALID_SERVICE_INVORK("001", "mandetory input fields are null "),
	OPARATOR_MISSING_IN_HEADER("002","Oparator missing in the header"),
	NO_VALID_OPARATOR("003","No valid operator found for given MSISDN"),
	OPARATOR_NOT_PROVISIONED("004","Requested service is not provisioned"),
	OPARATOR_ENDPOINT_NOT_DEFIEND("005","Oparetor endpoint not defined ")
	;
	private String description;
	private String code;

	ErrorHolder(final String code, final String description) {
		this.description = description;
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public String getDescription() {
	return this.description;
	}
	public String toString(){
		return this.code +" : " +this.description;
	}
}