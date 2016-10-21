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

package com.wso2telco.dep.reportingservice.exception;

import com.wso2telco.core.dbutils.exception.ThrowableError;

public enum ReportingServiceError implements ThrowableError  {
	UNDEFINED("CORE0001", "Undefined Error"),
	INTERNAL_SERVER_ERROR("CORE0299", "Internal Server Error"),
	INPUT_ERROR("CORE0300", "Input mismatch Error");

	private String code;
	private String desc;

	ReportingServiceError(final String code, final String desc) {
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
