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
package com.wso2telco.dep.oneapivalidation.service.impl.provision;

import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.util.UrlValidator;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;

public class ValidateListCustomerService implements IServiceValidate{

	private final String[] validationRules = { "provision", "*", "list", "active" };

	public void validate(String json) throws CustomException {

		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void validateUrl(String pathInfo) throws CustomException { // should
																		// be
																		// check

		String[] requestParts = null;

		if (pathInfo != null) {
			if (pathInfo.startsWith("/")) {
				pathInfo = pathInfo.substring(1);
			}
			requestParts = pathInfo.split("/");
		}

		UrlValidator.validateRequest(requestParts, validationRules);
	}

	public void validate(String[] params) throws CustomException {

		String msisdn = nullOrTrimmed(params[0]);
		String offset = nullOrTrimmed(params[1]);
		String limit = nullOrTrimmed(params[2]);

		ValidationRule[] rules = {
				new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "msisdn", msisdn),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "offset", offset), // should
																											// be
																											// verify
				new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "limit", limit) }; // should
																											// be
																											// verify

		Validation.checkRequestParams(rules);
	}

	private static String nullOrTrimmed(String s) {

		String rv = null;

		if (s != null && s.trim().length() > 0) {
			rv = s.trim();
		}

		return rv;
	}
}
