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
package com.wso2telco.dep.oneapivalidation.service.impl.payment;


import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.util.UrlValidator;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


// TODO: Auto-generated Javadoc
/**
 * The Class ValidateRefund.
 */
public class ValidateRefund implements IServiceValidate {

    static Logger logger=Logger.getLogger(ValidateRefund.class);

    /** The validation rules. */
    private final String[] validationRules = {"*", "transactions", "amount"};
    private String paymentCurrency=null;

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String)
     */
    public void validate(String json) throws CustomException {

        String clientCorrelator = null;
        String endUserId = null;
        Double amount = null;
        String currency = null;
        String description = null;
        String code = null;
        String onBehalfOf = null;
        String purchaseCategoryCode = null;
        String channel = null;
        Double taxAmount = null;
        String mandateId = null;
        String productId = null;
        String serviceId = null;
        String callbackData = null;
        String notifyURL = null;
        String referenceCode = null;
        String notificationFormat = null;
        String originalServerReferenceCode = null;
        String transactionOperationStatus = null;
        String doubleValidationRegex = "(\\d+(\\.\\d{1,2})?)";

        try {
            JSONObject objJSONObject = new JSONObject(json);
            JSONObject objAmountTransaction = (JSONObject) objJSONObject.get("amountTransaction");

            if (!objAmountTransaction.isNull("clientCorrelator") ) {
                clientCorrelator = nullOrTrimmed(objAmountTransaction.get("clientCorrelator").toString());
            }
            if (!objAmountTransaction.isNull("endUserId") ) {
                endUserId = nullOrTrimmed(objAmountTransaction.get("endUserId").toString());
            }
            if (!objAmountTransaction.isNull("callbackData") ) {
                callbackData = nullOrTrimmed(objAmountTransaction.get("callbackData").toString());
            }
            if (!objAmountTransaction.isNull("notifyURL") ) {
                notifyURL = nullOrTrimmed(objAmountTransaction.get("notifyURL").toString());
            }
            if (!objAmountTransaction.isNull("notificationFormat")) {
                notificationFormat = nullOrTrimmed(objAmountTransaction.get("notificationFormat").toString());
            }
            if (!objAmountTransaction.isNull("referenceCode") ) {
                referenceCode = nullOrTrimmed(objAmountTransaction.get("referenceCode").toString());
            }
            if (!objAmountTransaction.isNull("originalServerReferenceCode")) {
                originalServerReferenceCode = nullOrTrimmed(objAmountTransaction.get("originalServerReferenceCode").toString());
            }
            if (!objAmountTransaction.isNull("transactionOperationStatus") ) {
                transactionOperationStatus = nullOrTrimmed(objAmountTransaction.get("transactionOperationStatus").toString());
            }

            JSONObject objPaymentAmount = (JSONObject) objAmountTransaction.get("paymentAmount");
            JSONObject objChargingInformation = (JSONObject) objPaymentAmount.get("chargingInformation");

            if (!objChargingInformation.isNull("amount")) {
                if (!objChargingInformation.get("amount").toString().matches(doubleValidationRegex)) {
                    throw new CustomException("SVC0002", "Invalid input value for message part %1",
                            new String[]{"amount should be a whole or two digit decimal positive number"});
                }
                amount = Double.parseDouble(nullOrTrimmed(String.valueOf(objChargingInformation.get("amount"))));
            }
            if (!objChargingInformation.isNull("currency")) {
                currency = nullOrTrimmed(objChargingInformation.get("currency").toString());
            }
            if (!objChargingInformation.isNull("description") ) {
                description = nullOrTrimmed(objChargingInformation.get("description").toString());
            }
            if (!objChargingInformation.isNull("code")) {
                code = nullOrTrimmed(objChargingInformation.get("code").toString());
            }

            JSONObject objChargingMetaData = (JSONObject) objPaymentAmount.get("chargingMetaData");

            if (!objChargingMetaData.isNull("onBehalfOf") ) {
                onBehalfOf = nullOrTrimmed(objChargingMetaData.get("onBehalfOf").toString());
            }
            if (!objChargingMetaData.isNull("purchaseCategoryCode")) {
                purchaseCategoryCode = nullOrTrimmed(objChargingMetaData.get("purchaseCategoryCode").toString());
            }
            if (!objChargingMetaData.isNull("channel") ) {
                channel = nullOrTrimmed(objChargingMetaData.get("channel").toString());
            }
            if (!objChargingMetaData.isNull("taxAmount")) {
                if(!objChargingMetaData.get("taxAmount").toString().matches(doubleValidationRegex)){
                    throw new CustomException("SVC0002", "Invalid input value for message part %1",
                            new String[]{"taxAmount should be a whole or two digit decimal positive number"});
                }
                taxAmount = Double.parseDouble(nullOrTrimmed(String.valueOf(objChargingMetaData.get("taxAmount"))));

            }
            if (!objChargingMetaData.isNull("mandateId") ) {
                mandateId = nullOrTrimmed(objChargingMetaData.get("mandateId").toString());
            }
            if (!objChargingMetaData.isNull("productId") ) {
                productId = nullOrTrimmed(objChargingMetaData.get("productId").toString());
            }
            if (!objChargingMetaData.isNull("serviceId")) {
                serviceId = nullOrTrimmed(objChargingMetaData.get("serviceId").toString());
            }
            
        } catch (Exception e) {
            System.out.println("Manipulating recived JSON Object: " + e);
            throw new CustomException("POL0299", "Unexpected Error", new String[]{""});
        }

        ValidationRule[] rules = null;

        rules = new ValidationRule[]{
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GT_ZERO, "amount", amount),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_CURRENCY, "currency", currency, paymentCurrency),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "description", description),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "onBehalfOf", onBehalfOf),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "notificationFormat", notificationFormat),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "code", code),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "purchaseCategoryCode", purchaseCategoryCode),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_PAYMENT_CHANNEL, "channel", channel),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO, "taxAmount", taxAmount),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "serviceId", serviceId),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "mandateId", mandateId),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "productId", productId),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_URL, "notifyURL", notifyURL),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "referenceCode", referenceCode),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "originalServerReferenceCode", originalServerReferenceCode),
            new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionOperationStatus", transactionOperationStatus, "Refunded")};

        Validation.checkRequestParams(rules);
    }

    private void retriveCurrencyInPaymentCall(String originalServerReferenceCode) {
        DataSource dataSource;
        String dataSourceName = "jdbc/ADS_BILLING";
        Connection con = null;
        PreparedStatement preparedStatement = null;
        String sql="select * from sb_api_response_summary where serverReferenceCode='"+originalServerReferenceCode+"'";

        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup(dataSourceName);
            con = dataSource.getConnection();
            preparedStatement = con.prepareStatement(sql);
            ResultSet rs=preparedStatement.executeQuery();
            while(rs.next()) {
                JSONObject objJSONObject = new JSONObject(rs.getString("jsonBody"));
                JSONObject objChargingInformation =(JSONObject)((JSONObject)((JSONObject) objJSONObject.get("amountTransaction")).get("paymentAmount")).get("chargingInformation");
                paymentCurrency=objChargingInformation.get("currency").toString();
            }
        } catch (NamingException e) {
            logger.error("Error while looking up the data " +
                    "source: " + dataSourceName, e);
        } catch (SQLException e) {
            logger.error("error while retriving the data:"+e);
        }

    }

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validateUrl(java.lang.String)
     */
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

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String[])
     */
    public void validate(String[] params) throws CustomException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Null or trimmed.
     *
     * @param s the s
     * @return the string
     */
    private static String nullOrTrimmed(String s) {
        String rv = null;
        if (s != null && s.trim().length() > 0) {
            rv = s.trim();
        }
        return rv;
    }
}
