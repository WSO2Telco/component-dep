package com.wso2telco.oneapivalidation.service.impl;

import org.json.JSONObject;

import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.util.UrlValidator;
import com.wso2telco.oneapivalidation.util.Validation;
import com.wso2telco.oneapivalidation.util.ValidationRule;

public class ValidateCreditRefund implements IServiceValidate {
    
   private final String[] validationRules = {"credit", "*","refund"};
    
	public void validate(String json) throws CustomException {

		String msisdn = null;
		String originalServerReferenceCode = null;
		Double refundAmount = null;
		String clientCorrelator = null;
		String reasonForRefund = null;
		Double amount = null;
		String currency = null;
		String description = null;
		String onBehalfOf = null;
		String purchaseCategoryCode = null;
		String channel = null;
		Double taxAmount = null;
		String referenceCode = null;

		try {
			JSONObject mainJSONObject = new JSONObject(json);
			JSONObject jsonObj = (JSONObject) mainJSONObject
					.get("refundRequest");

			if (!jsonObj.isNull("originalServerReferenceCode")) {
				originalServerReferenceCode = nullOrTrimmed(jsonObj
						.getString("originalServerReferenceCode"));
			}
			if (!jsonObj.isNull("refundAmount")) {
				refundAmount = Double.parseDouble(nullOrTrimmed(jsonObj
						.getString("refundAmount")));
			}
			if (!jsonObj.isNull("clientCorrelator")) {
				clientCorrelator = nullOrTrimmed(jsonObj
						.getString("clientCorrelator"));
			}
			if (!jsonObj.isNull("reasonForRefund")) {
				reasonForRefund = nullOrTrimmed(jsonObj
						.getString("reasonForRefund"));
			}

			if (!jsonObj.isNull("msisdn")) {
				msisdn = nullOrTrimmed(jsonObj.getString("msisdn"));
			}
			if (!jsonObj.isNull("referenceCode")) {
				referenceCode = nullOrTrimmed(jsonObj
						.getString("referenceCode"));
			}

			JSONObject payAmount = (JSONObject) jsonObj.get("paymentAmount");
			JSONObject chargingInformation = (JSONObject) payAmount
					.get("chargingInformation");

			if (!chargingInformation.isNull("amount")) {
				amount = Double.parseDouble(nullOrTrimmed(chargingInformation
						.getString("amount")));
			}
			if (!chargingInformation.isNull("currency")) {
				currency = nullOrTrimmed(chargingInformation
						.getString("currency"));
			}
			if (!chargingInformation.isNull("description")) {
				description = nullOrTrimmed(chargingInformation
						.getString("description"));
			}

			JSONObject chargingMetaData = (JSONObject) payAmount
					.get("chargingMetaData");

			if (!chargingMetaData.isNull("onBehalfOf")) {
				onBehalfOf = nullOrTrimmed(chargingMetaData
						.getString("onBehalfOf"));
			}
			if (!chargingMetaData.isNull("purchaseCategoryCode")) {
				purchaseCategoryCode = nullOrTrimmed(chargingMetaData
						.getString("purchaseCategoryCode"));
			}
			if (!chargingMetaData.isNull("channel")) {
				channel = nullOrTrimmed(chargingMetaData.getString("channel"));
			}
			if (!chargingMetaData.isNull("taxAmount")) {
				taxAmount = Double.parseDouble(nullOrTrimmed(chargingMetaData
						.getString("taxAmount")));
			}

			System.out.println("Manipulated recived JSON Object: " + json);

		} catch (Exception e) {
			System.out.println("Manipulating recived JSON Object: " + e);
			throw new CustomException("POL0299", "Unexpected Error",
					new String[] { "" });
		}

		ValidationRule[] rules = null;

		rules = new ValidationRule[] {
				new ValidationRule(
						ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID,
						"msisdn", msisdn),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
						"originalServerReferenceCode",
						originalServerReferenceCode),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
						"referenceCode", referenceCode),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
						"description", description),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
						"reasonForRefund", reasonForRefund),
				new ValidationRule(
						ValidationRule.VALIDATION_TYPE_MANDATORY_CURRENCY,
						"currency", currency),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,
						"amount", amount),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,
						"taxAmount", taxAmount),
				new ValidationRule(
						ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO,
						"refundAmount", refundAmount),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,
						"clientCorrelator", clientCorrelator),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,
						"onBehalfOf", onBehalfOf),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,
						"purchaseCategoryCode", purchaseCategoryCode),
				new ValidationRule(
						ValidationRule.VALIDATION_TYPE_OPTIONAL_PAYMENT_CHANNEL,
						"channel", channel),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,
						"originalServerReferenceCode",
						originalServerReferenceCode),
				new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL,
						"clientCorrelator", clientCorrelator), };

		Validation.checkRequestParams(rules);

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
}
