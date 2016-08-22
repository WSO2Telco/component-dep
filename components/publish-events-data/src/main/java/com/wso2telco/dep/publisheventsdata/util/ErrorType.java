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
package com.wso2telco.dep.publisheventsdata.util;

import com.wso2telco.utils.exception.ThrowableError;

public enum ErrorType implements ThrowableError {
	
	INVALID_MSISDN("PEV0001","Invalid msisdn"),
	CONSUMER_KEY("PEV0002","Invalid consumer key"),
	OPERATOR_ID("PEV0002","Invalid operator id");

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
