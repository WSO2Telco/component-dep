package com.wso2telco.workflow.model;

public class BaseTierUpdtReq {

    private String throttlingPolicy;
    private String applicationId;

    public String getThrottlingPolicy() {
        return throttlingPolicy;
    }

    public void setThrottlingPolicy(String throttlingPolicy) {
        this.throttlingPolicy = throttlingPolicy;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}
