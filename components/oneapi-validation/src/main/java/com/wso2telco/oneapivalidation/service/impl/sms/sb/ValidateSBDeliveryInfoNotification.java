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

package com.wso2telco.oneapivalidation.service.impl.sms.sb;

import org.json.JSONObject;

import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.util.UrlValidator;
import com.wso2telco.oneapivalidation.util.Validation;
import com.wso2telco.oneapivalidation.util.ValidationRule;

/**
 *
 * @author WSO2telco
 */
public class ValidateSBDeliveryInfoNotification implements IServiceValidate {

    private final String[] validationRules = {"DeliveryInfoNotification"};

    public void validate(String json) throws CustomException {

        String callbackData = null;
        String address = null;
        String deliveryStatus = null;

        try {

            JSONObject objJSONObject = new JSONObject(json);

            JSONObject objDeliveryInfoNotification = (JSONObject) objJSONObject.get("deliveryInfoNotification");

            if (!objDeliveryInfoNotification.isNull("callbackData")) {

                callbackData = nullOrTrimmed(objDeliveryInfoNotification.getString("callbackData"));
            }

           JSONObject objDeliveryInfo = (JSONObject) objDeliveryInfoNotification.get("deliveryInfo");

            if (objDeliveryInfo.get("address") != null) {

                address = nullOrTrimmed(objDeliveryInfo.getString("address"));
            }

            if (objDeliveryInfo.get("deliveryStatus") != null) {
                deliveryStatus = nullOrTrimmed(objDeliveryInfo.getString("deliveryStatus"));
            }
        } catch (Exception e) {

            throw new CustomException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] rules = null;

        rules = new ValidationRule[]{
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "address", address),
          new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "deliveryStatus", deliveryStatus)};

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
