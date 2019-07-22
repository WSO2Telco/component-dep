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
package com.wso2telco.dep.oneapivalidation.service.impl.payment;

import com.wso2telco.dep.oneapivalidation.delegator.ValidationDelegator;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import org.mockito.Mockito;
import org.testng.annotations.Test;

public class ValidateRefundTest {

    @Test
    public void testValidate_whenValidJson() {
        String json = "{\"amountTransaction\":{\"endUserId\":\"tel:+SBcRDj/+M108gFCu1S56zw==\"," +
                "\"transactionOperationStatus\":\"Refund\",\"clientCorrelator\":\"TES35cctrd25\"," +
                "\"referenceCode\":\"REF-TEce2dfdwe\",\"paymentAmount\":{\"chargingInformation\":{\"amount\":\"100\"," +
                "\"description\":\"Alien Invaders Game\",\"currency\":\"USD\"},\"chargingMetaData\":{\"channel\":\"sms\"," +
                "\"onBehalfOf\":\"Merchant\",\"taxAmount\":\"0\"}}}}";

        ValidationRule[] rule = new ValidationRule[]{};
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        Mockito.when(mockValidateValidationDelegator.checkRequestParams(rule)).thenReturn(true);

        ValidateRefund validateRefund = new ValidateRefund(mockValidateValidationDelegator);
        validateRefund.validate(json);
    }

    @Test(expectedExceptions = CustomException.class)
    public void testValidate_whenInvalidJson() {
        String json = "";

        ValidationRule[] rule = new ValidationRule[]{};
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        Mockito.when(mockValidateValidationDelegator.checkRequestParams(rule)).thenReturn(true);

        ValidateRefund validateRefund = new ValidateRefund(mockValidateValidationDelegator);
        validateRefund.validate(json);
    }

    @Test(expectedExceptions = CustomException.class)
    public void testValidate_whenAmountIsNull() {
        String json = "{\"amountTransaction\":{\"endUserId\":\"tel:+SBcRDj/+M108gFCu1S56zw==\"," +
                "\"transactionOperationStatus\":\"Refund\",\"clientCorrelator\":\"TES35cctrd25\"," +
                "\"referenceCode\":\"REF-TEce2dfdwe\",\"paymentAmount\":{\"chargingInformation\":{\"amount\":\"\"," +
                "\"description\":\"Alien Invaders Game\",\"currency\":\"USD\"},\"chargingMetaData\":{\"channel\":\"sms\"," +
                "\"onBehalfOf\":\"Merchant\",\"taxAmount\":\"0\"}}}}";

        ValidationRule[] rule = new ValidationRule[]{};
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        Mockito.when(mockValidateValidationDelegator.checkRequestParams(rule)).thenReturn(true);

        ValidateRefund validateRefund = new ValidateRefund(mockValidateValidationDelegator);
        validateRefund.validate(json);
    }

    @Test(expectedExceptions = CustomException.class)
    public void testValidate_whenAmountIsMinus() {
        String json = "{\"amountTransaction\":{\"endUserId\":\"tel:+SBcRDj/+M108gFCu1S56zw==\"," +
                "\"transactionOperationStatus\":\"Refund\",\"clientCorrelator\":\"TES35cctrd25\"," +
                "\"referenceCode\":\"REF-TEce2dfdwe\",\"paymentAmount\":{\"chargingInformation\":{\"amount\":\"-100\"," +
                "\"description\":\"Alien Invaders Game\",\"currency\":\"USD\"},\"chargingMetaData\":{\"channel\":\"sms\"," +
                "\"onBehalfOf\":\"Merchant\",\"taxAmount\":\"0\"}}}}";

        ValidationRule[] rule = new ValidationRule[]{};
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        Mockito.when(mockValidateValidationDelegator.checkRequestParams(rule)).thenReturn(true);

        ValidateRefund validateRefund = new ValidateRefund(mockValidateValidationDelegator);
        validateRefund.validate(json);
    }

    @Test(expectedExceptions = CustomException.class)
    public void testValidate_whenAmountIsInvalid() {
        String json = "{\"amountTransaction\":{\"endUserId\":\"tel:+SBcRDj/+M108gFCu1S56zw==\"," +
                "\"transactionOperationStatus\":\"Refund\",\"clientCorrelator\":\"TES35cctrd25\"," +
                "\"referenceCode\":\"REF-TEce2dfdwe\",\"paymentAmount\":{\"chargingInformation\":{\"amount\":\"100.000000\"," +
                "\"description\":\"Alien Invaders Game\",\"currency\":\"USD\"},\"chargingMetaData\":{\"channel\":\"sms\"," +
                "\"onBehalfOf\":\"Merchant\",\"taxAmount\":\"0\"}}}}";

        ValidationRule[] rule = new ValidationRule[]{};
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        Mockito.when(mockValidateValidationDelegator.checkRequestParams(rule)).thenReturn(true);

        ValidateRefund validateRefund = new ValidateRefund(mockValidateValidationDelegator);
        validateRefund.validate(json);
    }

    @Test(expectedExceptions = CustomException.class)
    public void testValidate_whenCurrencyIsInvalid() {
        String json = "{\"amountTransaction\":{\"endUserId\":\"tel:+SBcRDj/+M108gFCu1S56zw==\"," +
                "\"transactionOperationStatus\":\"Refund\",\"clientCorrelator\":\"TES35cctrd25\"," +
                "\"referenceCode\":\"REF-TEce2dfdwe\",\"paymentAmount\":{\"chargingInformation\":{\"amount\":\"100\"," +
                "\"description\":\"Alien Invaders Game\",\"currency\":\"USDD\"},\"chargingMetaData\":{\"channel\":\"sms\"," +
                "\"onBehalfOf\":\"Merchant\",\"taxAmount\":\"0\"}}}}";

        ValidationRule[] rule = new ValidationRule[]{};
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        Mockito.when(mockValidateValidationDelegator.checkRequestParams(rule)).thenReturn(true);

        ValidateRefund validateRefund = new ValidateRefund(mockValidateValidationDelegator);
        validateRefund.validate(json);
    }

    @Test(expectedExceptions = CustomException.class)
    public void testValidate_whenTaxAmountHasMinus() {
        String json = "{\"amountTransaction\":{\"endUserId\":\"tel:+SBcRDj/+M108gFCu1S56zw==\"," +
                "\"transactionOperationStatus\":\"Refund\",\"clientCorrelator\":\"TES35cctrd25\"," +
                "\"referenceCode\":\"REF-TEce2dfdwe\",\"paymentAmount\":{\"chargingInformation\":{\"amount\":\"100\"," +
                "\"description\":\"Alien Invaders Game\",\"currency\":\"USD\"},\"chargingMetaData\":{\"channel\":\"sms\"," +
                "\"onBehalfOf\":\"Merchant\",\"taxAmount\":\"-10\"}}}}";

        ValidationRule[] rule = new ValidationRule[]{};
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        Mockito.when(mockValidateValidationDelegator.checkRequestParams(rule)).thenReturn(true);

        ValidateRefund validateRefund = new ValidateRefund(mockValidateValidationDelegator);
        validateRefund.validate(json);
    }

    @Test(expectedExceptions = CustomException.class)
    public void testValidate_whenTaxAmountIsInvalid() {
        String json = "{\"amountTransaction\":{\"endUserId\":\"tel:+SBcRDj/+M108gFCu1S56zw==\"," +
                "\"transactionOperationStatus\":\"Refund\",\"clientCorrelator\":\"TES35cctrd25\"," +
                "\"referenceCode\":\"REF-TEce2dfdwe\",\"paymentAmount\":{\"chargingInformation\":{\"amount\":\"100\"," +
                "\"description\":\"Alien Invaders Game\",\"currency\":\"USD\"},\"chargingMetaData\":{\"channel\":\"sms\"," +
                "\"onBehalfOf\":\"Merchant\",\"taxAmount\":\"10.00000\"}}}}";

        ValidationRule[] rule = new ValidationRule[]{};
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        Mockito.when(mockValidateValidationDelegator.checkRequestParams(rule)).thenReturn(true);

        ValidateRefund validateRefund = new ValidateRefund(mockValidateValidationDelegator);
        validateRefund.validate(json);
    }

    @Test
    public void testValidate_whenMaskedMSISDN() {
        String json = "{\"amountTransaction\":{\"endUserId\":\"tel:+SBcRDj/+M108gFCu1S56zw==\"," +
                "\"transactionOperationStatus\":\"Refund\",\"clientCorrelator\":\"TES35cctrd25\"," +
                "\"referenceCode\":\"REF-TEce2dfdwe\",\"paymentAmount\":{\"chargingInformation\":{\"amount\":\"100\"," +
                "\"description\":\"Alien Invaders Game\",\"currency\":\"USD\"},\"chargingMetaData\":{\"channel\":\"sms\"," +
                "\"onBehalfOf\":\"Merchant\",\"taxAmount\":\"0\"}}}}";

        ValidationRule[] rule = new ValidationRule[]{};
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        Mockito.when(mockValidateValidationDelegator.checkRequestParams(rule)).thenReturn(true);

        boolean userAnonymization = true;
        String userMaskingSecretKey = "changethis";

        ValidateRefund validateRefund = new ValidateRefund(userAnonymization,userMaskingSecretKey,mockValidateValidationDelegator);
        validateRefund.validate(json);
    }

    @Test
    public void testValidate_whenUnaskedMSISDN() {
        String json = "{\"amountTransaction\":{\"endUserId\":\"tel:+SBcRDj/+M108gFCu1S56zw==\"," +
                "\"transactionOperationStatus\":\"Refund\",\"clientCorrelator\":\"TES35cctrd25\"," +
                "\"referenceCode\":\"REF-TEce2dfdwe\",\"paymentAmount\":{\"chargingInformation\":{\"amount\":\"100\"," +
                "\"description\":\"Alien Invaders Game\",\"currency\":\"USD\"},\"chargingMetaData\":{\"channel\":\"sms\"," +
                "\"onBehalfOf\":\"Merchant\",\"taxAmount\":\"0\"}}}}";

        ValidationRule[] rule = new ValidationRule[]{};
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        Mockito.when(mockValidateValidationDelegator.checkRequestParams(rule)).thenReturn(true);

        boolean userAnonymization = false;
        String userMaskingSecretKey = "changethis";

        ValidateRefund validateRefund = new ValidateRefund(userAnonymization,userMaskingSecretKey,mockValidateValidationDelegator);
        validateRefund.validate(json);
    }


    @Test
    public void validateUrlTest_whenValidURL() {
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        ValidateRefund validateRefund = new ValidateRefund(mockValidateValidationDelegator);
        String url = "/94777323228/transactions/amount";
        validateRefund.validateUrl(url);
    }

    @Test(expectedExceptions = CustomException.class)
    public void validateUrlTest_whenInvalidURL() {
        ValidationDelegator mockValidateValidationDelegator = Mockito.mock(ValidationDelegator.class);
        ValidateRefund validateRefund = new ValidateRefund(mockValidateValidationDelegator);
        String url = "payment/v1/94777323228/transactions/amount";
        validateRefund.validateUrl(url);
    }

}
