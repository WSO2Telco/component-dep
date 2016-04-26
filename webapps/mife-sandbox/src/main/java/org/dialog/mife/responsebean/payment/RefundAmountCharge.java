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
public class RefundAmountCharge {
    private String clientCorrelator;
    private String endUserId;
    private PaymentAmount paymentAmount;
    private String callbackData;
    private String notifyURL;
    private String notificationFormat;
    private String referenceCode;
    private String originalServerReferenceCode;
    private String resourceURL;
    private String transactionOperationStatus;

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
     * @return the callbackData
     */
    public String getCallbackData() {
        return callbackData;
    }

    /**
     * @param callbackData the callbackData to set
     */
    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }

    /**
     * @return the notifyURL
     */
    public String getNotifyURL() {
        return notifyURL;
    }

    /**
     * @param notifyURL the notifyURL to set
     */
    public void setNotifyURL(String notifyURL) {
        this.notifyURL = notifyURL;
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
     * @return the originalServerReferenceCode
     */
    public String getOriginalServerReferenceCode() {
        return originalServerReferenceCode;
    }

    /**
     * @param originalServerReferenceCode the originalServerReferenceCode to set
     */
    public void setOriginalServerReferenceCode(String originalServerReferenceCode) {
        this.originalServerReferenceCode = originalServerReferenceCode;
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
     * @return the notificationFormat
     */
    public String getNotificationFormat() {
        return notificationFormat;
    }

    /**
     * @param notificationFormat the notificationFormat to set
     */
    public void setNotificationFormat(String notificationFormat) {
        this.notificationFormat = notificationFormat;
    }
    
    public static class PaymentAmount{
        private ChargingInformation chargingInformation;
        private ChargingMetaData chargingMetaData;
        private Double totalAmountRefunded;

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

        /**
         * @return the totalAmountRefunded
         */
        public Double getTotalAmountRefunded() {
            return totalAmountRefunded;
        }

        /**
         * @param totalAmountRefunded the totalAmountRefunded to set
         */
        public void setTotalAmountRefunded(Double totalAmountRefunded) {
            this.totalAmountRefunded = totalAmountRefunded;
        }
        
        public static class ChargingInformation{
            private Double amount;
            private String currency;
            private String description;
            private String code;

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

            /**
             * @return the code
             */
            public String getCode() {
                return code;
            }

            /**
             * @param code the code to set
             */
            public void setCode(String code) {
                this.code = code;
            }
        }
        
        public static class ChargingMetaData{
            private String onBehalfOf;
            private String purchaseCategoryCode;
            private String channel;
            private Double taxAmount;
            private String mandateId;
            private String productId;
            private String serviceId;

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

            /**
             * @return the mandateId
             */
            public String getMandateId() {
                return mandateId;
            }

            /**
             * @param mandateId the mandateId to set
             */
            public void setMandateId(String mandateId) {
                this.mandateId = mandateId;
            }

            /**
             * @return the productId
             */
            public String getProductId() {
                return productId;
            }

            /**
             * @param productId the productId to set
             */
            public void setProductId(String productId) {
                this.productId = productId;
            }

            /**
             * @return the serviceId
             */
            public String getServiceId() {
                return serviceId;
            }

            /**
             * @param serviceId the serviceId to set
             */
            public void setServiceId(String serviceId) {
                this.serviceId = serviceId;
            }
        }
    }
}
