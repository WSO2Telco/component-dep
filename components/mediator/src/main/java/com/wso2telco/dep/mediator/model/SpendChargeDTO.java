package com.wso2telco.dep.mediator.model;

/**
 * Created by sasikala on 11/24/16.
 */
public class SpendChargeDTO {

    private String groupName;
    private String consumerKey;
    private String operatorId;
    private String msisdn;
    private Double amount;
    private long currentTime;
    private long  orginalTime;
    private int messageType;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public long getOrginalTime() {
        return orginalTime;
    }

    public void setOrginalTime(long orginalTime) {
        this.orginalTime = orginalTime;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
