
package com.wso2telco.workflow.model;

public class SubscriptionEditDTO {

	private Integer applicationId;
	private String applicationName;
	private String subscriptionTier;
	private String apiName;
	private String user;
	private String department;

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

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

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
}