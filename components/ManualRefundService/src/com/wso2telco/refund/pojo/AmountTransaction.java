/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.refund.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class AmountTransaction {

    /** The client correlator. */
    @SerializedName("clientCorrelator")
    @Expose
    private String clientCorrelator;
    
    /** The end user id. */
    @SerializedName("endUserId")
    @Expose
    private String endUserId;
    
    /** The original server reference code. */
    @SerializedName("originalServerReferenceCode")
    @Expose
    private String originalServerReferenceCode;
    
    /** The payment amount. */
    @SerializedName("paymentAmount")
    @Expose
    private PaymentAmount paymentAmount;
    
    /** The reference code. */
    @SerializedName("referenceCode")
    @Expose
    private String referenceCode;
    
    /** The transaction operation status. */
    @SerializedName("transactionOperationStatus")
    @Expose
    private String transactionOperationStatus;

    /**
     * Gets the client correlator.
     *
     * @return     The clientCorrelator
     */
    public String getClientCorrelator() {
        return clientCorrelator;
    }

    /**
     * Sets the client correlator.
     *
     * @param clientCorrelator     The clientCorrelator
     */
    public void setClientCorrelator(String clientCorrelator) {
        this.clientCorrelator = clientCorrelator;
    }

    /**
     * Gets the end user id.
     *
     * @return     The endUserId
     */
    public String getEndUserId() {
        return endUserId;
    }

    /**
     * Sets the end user id.
     *
     * @param endUserId     The endUserId
     */
    public void setEndUserId(String endUserId) {
        this.endUserId = endUserId;
    }

    /**
     * Gets the original server reference code.
     *
     * @return     The originalServerReferenceCode
     */
    public String getOriginalServerReferenceCode() {
        return originalServerReferenceCode;
    }

    /**
     * Sets the original server reference code.
     *
     * @param originalServerReferenceCode     The originalServerReferenceCode
     */
    public void setOriginalServerReferenceCode(String originalServerReferenceCode) {
        this.originalServerReferenceCode = originalServerReferenceCode;
    }

    /**
     * Gets the payment amount.
     *
     * @return     The paymentAmount
     */
    public PaymentAmount getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Sets the payment amount.
     *
     * @param paymentAmount     The paymentAmount
     */
    public void setPaymentAmount(PaymentAmount paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    /**
     * Gets the reference code.
     *
     * @return     The referenceCode
     */
    public String getReferenceCode() {
        return referenceCode;
    }

    /**
     * Sets the reference code.
     *
     * @param referenceCode     The referenceCode
     */
    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    /**
     * Gets the transaction operation status.
     *
     * @return     The transactionOperationStatus
     */
    public String getTransactionOperationStatus() {
        return transactionOperationStatus;
    }

    /**
     * Sets the transaction operation status.
     *
     * @param transactionOperationStatus     The transactionOperationStatus
     */
    public void setTransactionOperationStatus(String transactionOperationStatus) {
        this.transactionOperationStatus = transactionOperationStatus;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
