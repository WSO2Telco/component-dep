/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gsm.oneapi.responsebean.payment;

/**
 *
 * @author User
 */
public class AmountTransaction {
    
    private static final long serialVersionUID = -8018179657702827920L;

    /**
     * @return the serialVersionUID
     */
    
    /**
     * the clientCorrelator ensures the same payment request is only applied
     * once
     */
    
    private String clientCorrelator;
    private String endUserId;
    private String referenceCode;
    private String transactionOperationStatus;
    
    private PaymentAmount paymentAmount;

    /**
     * @return the clientCorrelator
     */
    public String getClientCorrelator() {
        return clientCorrelator;
    }

    /**
     * @param clientCorrelator the clientCorrelator to set
     */
    public void setClientCorrelator(String clientCorrelator) {
        this.clientCorrelator = clientCorrelator;
    }

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
    public PaymentAmount getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * @param paymentAmount the paymentAmount to set
     */
    public void setPaymentAmount(PaymentAmount paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
    
    public static class PaymentAmount{
        private Double totalAmountCharged;

        private ChargingInformation chargingInformation;
        private ChargingMetaData chargingMetaData;
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
         * @return the chargingMetaData
         */
        public ChargingMetaData getChargingMetaData() {
            return chargingMetaData;
        }

        /**
         * @param chargingMetaData the chargingMetaData to set
         */
        public void setChargingMetaData(ChargingMetaData chargingMetaData) {
            this.chargingMetaData = chargingMetaData;
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
        
        public static class ChargingMetaData{
            private String onBehalfOf;
            private String purchaseCategoryCode;
            private String channel;
            private Double taxAmount;

            /**
             * @return the onBehalfOf
             */
            public String getOnBehalfOf() {
                return onBehalfOf;
            }

            /**
             * @param onBehalfOf the onBehalfOf to set
             */
            public void setOnBehalfOf(String onBehalfOf) {
                this.onBehalfOf = onBehalfOf;
            }

            /**
             * @return the purchaseCategoryCode
             */
            public String getPurchaseCategoryCode() {
                return purchaseCategoryCode;
            }

            /**
             * @param purchaseCategoryCode the purchaseCategoryCode to set
             */
            public void setPurchaseCategoryCode(String purchaseCategoryCode) {
                this.purchaseCategoryCode = purchaseCategoryCode;
            }

            /**
             * @return the channel
             */
            public String getChannel() {
                return channel;
            }

            /**
             * @param channel the channel to set
             */
            public void setChannel(String channel) {
                this.channel = channel;
            }

            /**
             * @return the taxAmount
             */
            public Double getTaxAmount() {
                return taxAmount;
            }

            /**
             * @param taxAmount the taxAmount to set
             */
            public void setTaxAmount(Double taxAmount) {
                this.taxAmount = taxAmount;
            }
        }
    }
}
