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
package com.wso2telco.dep.verificationhandler.model.Payment;

 
// TODO: Auto-generated Javadoc
/**
 * The Class AmountTransaction.
 */
public class AmountTransaction {
    
    /** The client correlator. */
    String clientCorrelator;
    
    /** The end user id. */
    String endUserId;
    
    /** The payment amount. */
    PaymentAmount paymentAmount;
    
    /** The reference code. */
    String referenceCode;

    /**
     * Gets the client correlator.
     *
     * @return the client correlator
     */
    public String getClientCorrelator() {

        return clientCorrelator;
    }

    /**
     * Sets the client correlator.
     *
     * @param clientCorrelator the new client correlator
     */
    public void setClientCorrelator(String clientCorrelator) {
        this.clientCorrelator = clientCorrelator;
    }

    /**
     * Gets the end user id.
     *
     * @return the end user id
     */
    public String getEndUserId() {
        return endUserId;
    }

    /**
     * Sets the end user id.
     *
     * @param endUserId the new end user id
     */
    public void setEndUserId(String endUserId) {
        this.endUserId = endUserId;
    }

    /**
     * Gets the payment amount.
     *
     * @return the payment amount
     */
    public PaymentAmount getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Sets the payment amount.
     *
     * @param paymentAmount the new payment amount
     */
    public void setPaymentAmount(PaymentAmount paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    /**
     * Gets the reference code.
     *
     * @return the reference code
     */
    public String getReferenceCode() {
        return referenceCode;
    }

    /**
     * Sets the reference code.
     *
     * @param referenceCode the new reference code
     */
    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }
}
