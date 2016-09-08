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
package com.wso2telco.dep.verificationhandler.model.payment;

 
// TODO: Auto-generated Javadoc
/**
 * The Class PaymentAmount.
 */
public class PaymentAmount {

    /** The amount. */
    Double amount;
    
    /** The charging information. */
    ChargingInformation chargingInformation;
    
    /** The charging meta data. */
    ChargingMetaData chargingMetaData;
    
    /** The transaction operation status. */
    String transactionOperationStatus;

    /**
     * Gets the amount.
     *
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets the amount.
     *
     * @param amount the new amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Sets the amount.
     *
     * @param amount the new amount
     */
    public void setAmount(String amount) {

        try {
            this.amount = Double.parseDouble(amount);
        } catch (Exception e) {
        }
    }

    /**
     * Gets the charging information.
     *
     * @return the charging information
     */
    public ChargingInformation getChargingInformation() {
        return chargingInformation;
    }

    /**
     * Sets the charging information.
     *
     * @param chargingInformation the new charging information
     */
    public void setChargingInformation(ChargingInformation chargingInformation) {
        this.chargingInformation = chargingInformation;
    }

    /**
     * Gets the charging meta data.
     *
     * @return the charging meta data
     */
    public ChargingMetaData getChargingMetaData() {
        return chargingMetaData;
    }

    /**
     * Sets the charging meta data.
     *
     * @param chargingMetaData the new charging meta data
     */
    public void setChargingMetaData(ChargingMetaData chargingMetaData) {
        this.chargingMetaData = chargingMetaData;
    }

    /**
     * Gets the transaction operation status.
     *
     * @return the transaction operation status
     */
    public String getTransactionOperationStatus() {
        return transactionOperationStatus;
    }

    /**
     * Sets the transaction operation status.
     *
     * @param transactionOperationStatus the new transaction operation status
     */
    public void setTransactionOperationStatus(String transactionOperationStatus) {
        this.transactionOperationStatus = transactionOperationStatus;
    }
}
