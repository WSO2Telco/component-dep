/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mife.sandbox.model.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author User
 */

@Entity
@Table(name="charge_amount_request")
public class ChargeAmountRequest implements Serializable {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int Id;
       
    @Column(name="end_user_id")
    private String endUserId;
    
    @Column(name="tran_oper_status")
    private String transactionOperationStatus;
    
    @Column(name="reference_code")
    private String referenceCode;
    
    private String description;
    
    private String currency;
    
    private Double amount;
    
    @Column(name="client_correlator", unique = true)
    private String clientCorrelator;
    
    @Column(name="notify_url")
    private String notifyURL;
    
    @Column(name="on_behalf_of")
    private String onBehalfOf;
    
    @Column(name="purchase_cat_code")
    private String purchaseCategoryCode;
    
    private String channel;
    
    @Column(name="tax_amount")
    private Double taxAmount;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name="effect_date")
    private Date date;
    
    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;
    
    @Column(name="callback_data")
    private String callbackData;
    
    @Column(name="mandate_id")
    private String mandateId;
    
    @Column(name="notification_format")
    private String notificationFormat;    
    
    @Column(name="product_id")
    private String productId;
    
    @Column(name="reference_sequence")
    private Integer referenceSequence;
    
    @Column(name="original_server_reference_code")
    private String originalServerReferenceCode;
    
    @Column(name="service_id")
    private String serviceId;
    
    @Column(name="code")
    private String code;
    
    @Column(name="total_amount_charged")
    private Double totalAmountCharged;
    
    @Column(name="amount_reserved")
    private Double amountReserved;
    
    @Column(name="transaction_id")
    private String transactionId;
    
    @Column(name="payment_transaction_type")
    private int paymentTranscationType;
    
    @Column(name="refund_status")
    private int refundStatus;

    /**
     * @return the chargeId
     */
    public int getChargeId() {
        return Id;
    }

    /**
     * @param chargeId the chargeId to set
     */
    public void setChargeId(int chargeId) {
        this.Id = chargeId;
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
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the Date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param Date the Date to set
     */
    public void setDate(Date date) {
        this.date = date;
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
     * @return the paymentTranscationType
     */
    public int getPaymentTranscationType() {
        return paymentTranscationType;
    }

    /**
     * @param paymentTranscationType the paymentTranscationType to set
     */
    public void setPaymentTranscationType(int paymentTranscationType) {
        this.paymentTranscationType = paymentTranscationType;
    }

    /**
     * @return the transactionId
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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
     * @return the refundStatus
     */
    public int getRefundStatus() {
        return refundStatus;
    }

    /**
     * @param refundStatus the refundStatus to set
     */
    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    /**
     * @return the referenceSequence
     */
    public Integer getReferenceSequence() {
        return referenceSequence;
    }

    /**
     * @param referenceSequence the referenceSequence to set
     */
    public void setReferenceSequence(Integer referenceSequence) {
        this.referenceSequence = referenceSequence;
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
