/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dialog.mife.responsebean.payment;

/**
 *
 * @author Dialog
 */
public class ReleaseAmountReservationTransaction {
    private String endUserId;
    private PaymentAmount paymentAmount;
    private String referenceCode;
    private int referenceSequence;
    private String resourceURL;
    private String transactionOperationStatus;

    /**
     * @return the endUserId
     */
    public String getEndUserId() {
        return endUserId;
    }

    /**
     * @param endUserId the endUserId to set
     */
    public void setEndUserId(String endUserId) {
        this.endUserId = endUserId;
    }

    /**
     * @return the paymentAmount
     */
    public PaymentAmount getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * @param paymentAmount the paymentAmount to set
     */
    public void setPaymentAmount(PaymentAmount paymentAmount) {
        this.paymentAmount = paymentAmount;
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
     * @return the referenceSequence
     */
    public int getReferenceSequence() {
        return referenceSequence;
    }

    /**
     * @param referenceSequence the referenceSequence to set
     */
    public void setReferenceSequence(int referenceSequence) {
        this.referenceSequence = referenceSequence;
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
    
    public static class PaymentAmount{
        private Double amountReserved;
        private ChargingInformation chargingInformation;
        private Double totalAmountCharged;

        /**
         * @return the amountReserved
         */
        public Double getAmountReserved() {
            return amountReserved;
        }

        /**
         * @param amountReserved the amountReserved to set
         */
        public void setAmountReserved(Double amountReserved) {
            this.amountReserved = amountReserved;
        }

        /**
         * @return the chargingInformation
         */
        public ChargingInformation getChargingInformation() {
            return chargingInformation;
        }

        /**
         * @param chargingInformation the chargingInformation to set
         */
        public void setChargingInformation(ChargingInformation chargingInformation) {
            this.chargingInformation = chargingInformation;
        }

        /**
         * @return the totalAmountCharged
         */
        public Double getTotalAmountCharged() {
            return totalAmountCharged;
        }

        /**
         * @param totalAmountCharged the totalAmountCharged to set
         */
        public void setTotalAmountCharged(Double totalAmountCharged) {
            this.totalAmountCharged = totalAmountCharged;
        }
        
        public static class ChargingInformation{
            private Double amount;
            private String currency;

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
        }
    }
}
