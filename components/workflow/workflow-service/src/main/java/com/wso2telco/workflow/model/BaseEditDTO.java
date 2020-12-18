package com.wso2telco.workflow.model;

public class BaseEditDTO {

    private Integer applicationId;
    private String applicationName;
    private String existingTier;
    private String user;
    private String serviceProvider;

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getExistingTier() {
        return existingTier;
    }

    public void setExistingTier(String existingTier) {
        this.existingTier = existingTier;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
}
