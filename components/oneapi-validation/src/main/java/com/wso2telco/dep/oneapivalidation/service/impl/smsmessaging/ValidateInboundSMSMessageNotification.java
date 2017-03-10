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

package com.wso2telco.dep.oneapivalidation.service.impl.smsmessaging;

import org.json.JSONObject;

import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.util.UrlValidator;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;

/**
 *
 * @author WSO2telco
 */
public class ValidateInboundSMSMessageNotification implements IServiceValidate {

	private final String[] validationRules = { "ReceivedInfoNotification" };

	public void validate(String json) throws CustomException {

		String callbackData = null;
		String dateTime = null;
		String destinationAddress = null;
		String messageId = null;
		String message = null;
		String resourceURL = null;
		String senderAddress = null;

		try {

			JSONObject objJSONObject = new JSONObject(json);

			JSONObject objInboundSMSMessageNotification = (JSONObject) objJSONObject
					.get("inboundSMSMessageNotification");

			if (!objInboundSMSMessageNotification.isNull("callbackData")) {

				callbackData = nullOrTrimmed(objInboundSMSMessageNotification.getString("callbackData"));
			}

			if (!objInboundSMSMessageNotification.isNull("resourceURL")) {

				resourceURL = nullOrTrimmed(objInboundSMSMessageNotification.getString("resourceURL"));
			}

			JSONObject objInboundSMSMessage = (JSONObject) objInboundSMSMessageNotification.get("inboundSMSMessage");

			if (!objInboundSMSMessage.isNull("dateTime")) {

				dateTime = nullOrTrimmed(objInboundSMSMessage.getString("dateTime"));
			}

			if (!objInboundSMSMessage.isNull("destinationAddress")) {

				destinationAddress = nullOrTrimmed(objInboundSMSMessage.getString("destinationAddress"));
			}

			if (!objInboundSMSMessage.isNull("messageId")) {

				messageId = nullOrTrimmed(objInboundSMSMessage.getString("messageId"));
			}

			if (!objInboundSMSMessage.isNull("message")) {

				message = nullOrTrimmed(objInboundSMSMessage.getString("message"));
			}

			if (!objInboundSMSMessage.isNull("senderAddress")) {

				senderAddress = nullOrTrimmed(objInboundSMSMessage.getString("senderAddress"));
			}
		} catch (Exception e) {

			throw new CustomException("POL0299", "Unexpected Error", new String[] { "" });
		}

		ValidationRule[] rules = null;

		rules = new ValidationRule[] {
				new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "dateTime", dateTime),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "destinationAddress", destinationAddress),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "messageId", messageId),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "message", message),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "resourceURL", resourceURL),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "senderAddress", senderAddress) };

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

		UrlValidator.validateRequest(requestParts, validationRules);
	}

	private static String nullOrTrimmed(String s) {
		String rv = null;
		if (s != null && s.trim().length() > 0) {
			rv = s.trim();
		}
		return rv;
	}

	public void validate(String[] params) throws CustomException {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
