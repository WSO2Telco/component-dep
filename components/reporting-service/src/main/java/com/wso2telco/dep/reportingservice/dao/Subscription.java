package com.wso2telco.dep.reportingservice.dao;

/**
 * Class Subscription
 */
public class Subscription {

    /** The subscription_id. */
    int subscriptionId;

    /** The tier_id. */
    String tierId;

    /** The API ID. */
    int apiId;

    /** The APPLICATION ID. */
    int appId;

    /** The subscription status. */
    String subStatus;

    /** The subscription create state. */
    String subsCreateState;

    /** The created by. */
    String createdBy;

    public Subscription(int subscriptionId, String tierId, int apiId, int appId, String subStatus, String subsCreateState, String createdBy) {
        this.subscriptionId = subscriptionId;
        this.tierId = tierId;
        this.apiId = apiId;
        this.appId = appId;
        this.subStatus = subStatus;
        this.subsCreateState = subsCreateState;
        this.createdBy = createdBy;
    }

    public Subscription() {
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getTierId() {
        return tierId;
    }

    public void setTierId(String tierId) {
        this.tierId = tierId;
    }

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public String getSubsCreateState() {
        return subsCreateState;
    }

    public void setSubsCreateState(String subsCreateState) {
        this.subsCreateState = subsCreateState;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionId=" + subscriptionId +
                ", tierId='" + tierId + '\'' +
                ", apiId=" + apiId +
                ", appId=" + appId +
                ", subStatus='" + subStatus + '\'' +
                ", subsCreateState='" + subsCreateState + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
