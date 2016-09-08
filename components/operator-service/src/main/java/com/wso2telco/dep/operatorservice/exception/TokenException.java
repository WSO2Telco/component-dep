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
package com.wso2telco.dep.operatorservice.exception;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ThrowableError;

public class TokenException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6940487050911022290L;

	public TokenException(ThrowableError error) {

		super(error);
	}
	
	public enum TokenErrorType implements ThrowableError {

		INVALID_REFRESH_TOKEN("TOKE0001", "Invalid refresh token"),
		INVALID_TOKEN("TOKE0002", "Invalid token"),
		INVALID_TOKEN_TIME("TOKE0003", "Invalid token time"),
		INVALID_TOKEN_VALIDITY("TOKE0003", "Invalid token validity");

		final String code;
		final String msg;

		TokenErrorType(final String code, final String msg) {

			this.code = code;
			this.msg = msg;
		}

		public String getMessage() {
			
			return this.msg;
		}

		public String getCode() {
			
			return this.code;
		}
	}

	@Override
	public String toString() {
		
		return "TokenException [getErrorType()=" + getErrorType() + ", getMessage()=" + getMessage() + "]";
	}
}
