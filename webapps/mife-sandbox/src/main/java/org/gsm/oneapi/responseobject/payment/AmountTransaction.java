/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gsm.oneapi.responseobject.payment;

/**
 *
 * @author User
 */
public class AmountTransaction {
    private String endUserID;
    private String referenceCode;
    private String serverReferenceCode;
    private String resourceURL;
    private String transactionOperationStatus;
    
    private ChargingInformation paymentAmount;

    /**
     * @return the endUserID
     */
    public String getEndUserID() {
        return endUserID;
    }

    /**
     * @param endUserID the endUserID to set
     */
    public void setEndUserID(String endUserID) {
        this.endUserID = endUserID;
    }

    /**
     * @return the referenceCode
     */
    public String getReferenceCode() {
        return referenceCode;
    }

    /**
     * @param referenceCode the referenceCode to set
     */
    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    /**
     * @return the serverReferenceCode
     */
    public String getServerReferenceCode() {
        return serverReferenceCode;
    }

    /**
     * @param serverReferenceCode the serverReferenceCode to set
     */
    public void setServerReferenceCode(String serverReferenceCode) {
        this.serverReferenceCode = serverReferenceCode;
    }

    /**
     * @return the resourceURL
     */
    public String getResourceURL() {
        return resourceURL;
    }

    /**
     * @param resourceURL the resourceURL to set
     */
    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }

    /**
     * @return the transactionOperationStatus
     */
    public String getTransactionOperationStatus() {
        return transactionOperationStatus;
    }

    /**
     * @param transactionOperationStatus the transactionOperationStatus to set
     */
    public void setTransactionOperationStatus(String transactionOperationStatus) {
        this.transactionOperationStatus = transactionOperationStatus;
    }

    /**
     * @return the paymentAmount
     */
    public ChargingInformation getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * @param paymentAmount the paymentAmount to set
     */
    public void setPaymentAmount(ChargingInformation paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
    
    
    
    public static class ChargingInformation{
            private Double amount;
            private String currency;
            private String description;

            /**
             * @return the amount
             */
            public Double getAmount() {
                return amount;
            }

            /**
             * @param amount the amount to set
             */
            public void setAmount(Double amount) {
                this.amount = amount;
            }

            /**
             * @return the currency
             */
            public String getCurrency() {
                return currency;
            }

            /**
             * @param currency the currency to set
             */
            public void setCurrency(String currency) {
                this.currency = currency;
            }

            /**
             * @return the description
             */
            public String getDescription() {
                return description;
            }

            /**
             * @param description the description to set
             */
            public void setDescription(String description) {
                this.description = description;
            }
        }
}
