
package com.wso2telco.workflow.model;

public class SubscriptionEditDTO {

	private Integer applicationId;
	private String applicationName;
	private String subscriptionTier;
	private String existingTier;
	private String apiName;
	private String apiVersion;
	private String user;
	private String serviceProvider;
	private String department;
	private int apiId;

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

	public String getSubscriptionTier() {
		return subscriptionTier;
	}

	public void setSubscriptionTier(String subscriptionTier) {
		this.subscriptionTier = subscriptionTier;
	}

	public String getApiName() {
		return apiName;
	}

	public String getExistingTier() { return existingTier; }

	public void setExistingTier(String existingTier) { this.existingTier = existingTier; }

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getApiVersion() { return apiVersion; }

	public void setApiVersion(String apiVersion) { this.apiVersion = apiVersion; }

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getServiceProvider() { return serviceProvider; }

	public void setServiceProvider(String serviceProvider) { this.serviceProvider = serviceProvider; }

	public int getApiID() {
		return apiId;
	}

	public void setApiID(int apiId) {
		this.apiId = apiId;
	}
}