/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.oneapivalidation.service.impl.sms;

import com.wso2telco.oneapivalidation.exceptions.AxiataException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.util.UrlValidator;
import com.wso2telco.oneapivalidation.util.Validation;
import com.wso2telco.oneapivalidation.util.ValidationRule;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class ValidateSendSms implements IServiceValidate {

    private final String[] validationRules = {"outbound", "*", "requests"};

    public void validate(String json) throws AxiataException {

        List<String> addresses = new ArrayList<String>(); // Note there can be multiple addresses specified
        String senderAddress = null;
        String message = null;
        String clientCorrelator = null;
        String notifyURL = null;
        String callbackData = null;
        String senderName = null;

        try {
            JSONObject objJSONObject = new JSONObject(json);

            JSONObject objOtboundSMSMessageRequest = (JSONObject) objJSONObject.get("outboundSMSMessageRequest");

            if (!objOtboundSMSMessageRequest.isNull("address")) {

                JSONArray addressArray = objOtboundSMSMessageRequest.getJSONArray("address");
                for (int a = 0; a < addressArray.length(); a++) {
                    addresses.add(nullOrTrimmed(addressArray.getString(a)));
                }
            }

            if (!objOtboundSMSMessageRequest.isNull("senderAddress")) {

                senderAddress = nullOrTrimmed(objOtboundSMSMessageRequest.getString("senderAddress"));
            }

            JSONObject objOutboundSMSTextMessage = (JSONObject) objOtboundSMSMessageRequest.get("outboundSMSTextMessage");
            if (objOutboundSMSTextMessage.get("message") != null) {

                message = nullOrTrimmed(objOutboundSMSTextMessage.getString("message"));
            }

            if (!objOtboundSMSMessageRequest.isNull("clientCorrelator")) {

                clientCorrelator = nullOrTrimmed(objOtboundSMSMessageRequest.getString("clientCorrelator"));
            }

            if (!objOtboundSMSMessageRequest.isNull("receiptRequest")) {

                JSONObject objReceiptRequest = (JSONObject) objOtboundSMSMessageRequest.get("receiptRequest");
                if (!objReceiptRequest.isNull("callbackData")) {

                    callbackData = nullOrTrimmed(objReceiptRequest.getString("callbackData"));
                }
                if (!objReceiptRequest.isNull("notifyURL")) {

                    notifyURL = nullOrTrimmed(objReceiptRequest.getString("notifyURL"));
                }
            }
            if (!objOtboundSMSMessageRequest.isNull("senderName")) {

                senderName = nullOrTrimmed(objOtboundSMSMessageRequest.getString("senderName"));
            }
        } catch (Exception e) {
            //logger.debug("Response JSON: " + "test");
            System.out.println("Manipulating recived JSON Object: " + e);
            throw new AxiataException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] rules = {
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "senderAddress", senderAddress),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "address", addresses.toArray(new String[addresses.size()])),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_MESSAGE, "message", message),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "senderName", senderName),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_URL, "notifyURL", notifyURL),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),};

        Validation.checkRequestParams(rules);
    }

    private static String nullOrTrimmed(String s) {
        String rv = null;
        if (s != null && s.trim().length() > 0) {
            rv = s.trim();
        }
        return rv;
    }

    public void validate(String[] params) throws AxiataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void validateUrl(String pathInfo) throws AxiataException {
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
