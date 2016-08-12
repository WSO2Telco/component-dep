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

import org.json.JSONObject;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.util.UrlValidator;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;

public class ValidateProvisionService implements IServiceValidate{

	private final String[] validationRules = { "provision", "*" };

	public void validate(String json) throws CustomException {

		String serviceCode = null;
		String clientCorrelator = null;
		String clientReferenceCode = null;
		String notifyURL = null;
		String callbackData = null;
		String resourceURL = null; // should be check

		try {

			JSONObject objJSONObject = new JSONObject(json);

			JSONObject objServiceProvisionRequest = (JSONObject) objJSONObject.get("serviceProvisionRequest");

			if (!objServiceProvisionRequest.isNull("serviceCode")) {

				serviceCode = nullOrTrimmed(objServiceProvisionRequest.getString("serviceCode"));
			}

			if (!objServiceProvisionRequest.isNull("clientCorrelator")) {

				clientCorrelator = nullOrTrimmed(objServiceProvisionRequest.getString("clientCorrelator"));
			}

			if (!objServiceProvisionRequest.isNull("clientReferenceCode")) {

				clientReferenceCode = nullOrTrimmed(objServiceProvisionRequest.getString("clientReferenceCode"));
			}

			JSONObject objCallbackReference = (JSONObject) objServiceProvisionRequest.get("callbackReference");

			if (!objCallbackReference.isNull("notifyURL")) {

				notifyURL = nullOrTrimmed(objCallbackReference.getString("notifyURL"));
			}

			if (!objCallbackReference.isNull("callbackData")) {

				callbackData = nullOrTrimmed(objCallbackReference.getString("callbackData"));
			}

			if (!objCallbackReference.isNull("resourceURL")) { // should be
																// checked

				resourceURL = nullOrTrimmed(objCallbackReference.getString("resourceURL"));
			}

			ValidationRule[] rules = null;

			rules = new ValidationRule[] {
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "serviceCode", serviceCode),
					new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "clientReferenceCode",
							clientReferenceCode),
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL, "notifyURL", notifyURL),
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "callbackData", callbackData),
					new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL, "resourceURL", resourceURL) }; // should
																													// be
																													// check

			Validation.checkRequestParams(rules);
		} catch (Exception e) {

			throw new CustomException("POL0299", "Unexpected Error", new String[] { "" });
		}
	}

	public void validateUrl(String pathInfo) throws CustomException {

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

		ValidationRule[] rules = {
				new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "msisdn", msisdn) };

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
