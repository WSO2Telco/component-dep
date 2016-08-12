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
package com.wso2telco.oneapivalidation.service.impl.payment;

import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.util.UrlValidator;
import com.wso2telco.oneapivalidation.util.Validation;
import com.wso2telco.oneapivalidation.util.ValidationRule;

import org.apache.log4j.Logger;

public class ValidateQueryPaymentStatus implements IServiceValidate {

	static Logger logger = Logger.getLogger(ValidateQueryPaymentStatus.class);

	private final String[] validationRules = { "*", "transactions", "amount",
			"*" };

	public void validate(String[] params) throws CustomException {
		String address = nullOrTrimmed(params[1]);
		String transactionId = nullOrTrimmed(params[4]);

		logger.debug("ValidateQueryPaymentStatus -- validate -> address : "+ address);
		logger.debug("ValidateQueryPaymentStatus -- validate -> transactionId : "+ transactionId);

		ValidationRule[] rules = {
				new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL,"address", address),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,"transactionId", transactionId) };

		Validation.checkRequestParams(rules);
	}

	public void validateUrl(String pathInfo) throws CustomException {
		String[] requestParts = null;
		if (pathInfo != null) {
			if (pathInfo.startsWith("/")) {
				pathInfo = pathInfo.substring(1);
			}
			requestParts = pathInfo.split("/");
		}

		// remove batchSize param
		int reqlength = requestParts.length - 1;
		requestParts[reqlength] = requestParts[reqlength].split("\\?")[0];

		UrlValidator.validateRequest(requestParts, validationRules);
	}

	public void validate(String json) throws CustomException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private static String nullOrTrimmed(String s) {
		String rv = null;
		if (s != null && s.trim().length() > 0) {
			rv = s.trim();
		}
		return rv;
	}

}
