package com.wso2telco.workflow.model;

public class SubTierUpdtReq extends BaseTierUpdtReq{

    private String apiId;
    private String subscriptionId;
    private String status;
    private String requestedThrottlingPolicy;

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestedThrottlingPolicy() {
        return requestedThrottlingPolicy;
    }

    public void setRequestedThrottlingPolicy(String requestedThrottlingPolicy) {
        this.requestedThrottlingPolicy = requestedThrottlingPolicy;
    }
}
