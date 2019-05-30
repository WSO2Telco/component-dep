package com.wso2telco.dep.oneapivalidation.service.impl.smsmessaging;

/**
 * Copyright (c) 2019, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.wso2telco.dep.oneapivalidation.delegator.ValidationDelegator;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import org.mockito.Mockito;
import org.testng.annotations.Test;

public class ValidateSendSmsTest {

    @Test
    public void testValidate_whenValidJson() {
        String json = "{\"outboundSMSMessageRequest\":{\"address\":[\"tel:+94777323222\"]," +
                "\"senderAddress\":\"tel:+7555\",\"outboundSMSTextMessage\":{\"message\":\"Test1\"}," +
                "\"clientCorrelator\":\"scs1211\",\"receiptRequest\":{\"notifyURL\":\"http://application.example.com/notifications/DeliveryInfoNotification\"," +
                "\"callbackData\":\"some-data-useful-to-the-requester\"},\"senderName\":\"\"}}";

        ValidationRule[] rule = new ValidationRule[]{};
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        Mockito.when(mockValidateValidationDelegator.checkRequestParams(rule)).thenReturn(true);

        ValidateSendSms validateSendSms = new ValidateSendSms(mockValidateValidationDelegator);
        validateSendSms.validate(json);
    }

    @Test(expectedExceptions = CustomException.class)
    public void testValidate_whenUnmaskedMSISDN() {
        String json = "{\"outboundSMSMessageRequest\":{\"address\":[\"tel:+94777323222\"]," +
                "\"senderAddress\":\"tel:+7555\",\"outboundSMSTextMessage\":{\"message\":\"Test1\"}," +
                "\"clientCorrelator\":\"scs1211\",\"receiptRequest\":{\"notifyURL\":\"http://application.example.com/notifications/DeliveryInfoNotification\"," +
                "\"callbackData\":\"some-data-useful-to-the-requester\"},\"senderName\":\"\"}}";

        ValidationRule[] rule = new ValidationRule[]{};
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        Mockito.when(mockValidateValidationDelegator.checkRequestParams(rule)).thenReturn(true);
        boolean userAnonymization = true;
        String userMaskingSecretKey = "changethis";

        ValidateSendSms validateSendSms = new ValidateSendSms(userAnonymization, userMaskingSecretKey,
                mockValidateValidationDelegator);
        validateSendSms.validate(json);
    }

    @Test
    public void testValidate_whenMaskedMSISDN() {
        String json = "{\"outboundSMSMessageRequest\":{\"address\":[\"tel:+SBcRDj/+M108gFCu1S56zw==\"]," +
                "\"senderAddress\":\"tel:+7555\",\"outboundSMSTextMessage\":{\"message\":\"Test1\"}," +
                "\"clientCorrelator\":\"scs1211\",\"receiptRequest\":{\"notifyURL\":\"http://application.example.com/notifications/DeliveryInfoNotification\"," +
                "\"callbackData\":\"some-data-useful-to-the-requester\"},\"senderName\":\"\"}}";

        ValidationRule[] rule = new ValidationRule[]{};
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        Mockito.when(mockValidateValidationDelegator.checkRequestParams(rule)).thenReturn(true);
        boolean userAnonymization = true;
        String userMaskingSecretKey = "changethis";

        ValidateSendSms validateSendSms = new ValidateSendSms(userAnonymization, userMaskingSecretKey,
                mockValidateValidationDelegator);
        validateSendSms.validate(json);
    }

    @Test(expectedExceptions = CustomException.class)
    public void testValidate_whenInvalidJson() {
        String json = "";

        ValidationRule[] rule = new ValidationRule[]{};
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        Mockito.when(mockValidateValidationDelegator.checkRequestParams(rule)).thenReturn(true);

        ValidateSendSms validateSendSms = new ValidateSendSms(mockValidateValidationDelegator);
        validateSendSms.validate(json);
    }

    @Test
    public void validateUrlTest_whenValidURL() {
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        ValidateSendSms validateSendSms = new ValidateSendSms(mockValidateValidationDelegator);
        String url = "/outbound/tel%3A%2B7555/requests";
        validateSendSms.validateUrl(url);
    }

    @Test(expectedExceptions = CustomException.class)
    public void validateUrlTest_whenInvalidURL() {
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        ValidateSendSms validateSendSms = new ValidateSendSms(mockValidateValidationDelegator);
        String url = "/smsmessaging/v1/outbound/tel%3A%2B7555/requests";
        validateSendSms.validateUrl(url);
    }

}
