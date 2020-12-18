
package com.wso2telco.workflow.model;

public class SubscriptionEditDTO extends BaseEditDTO{

	private String subscriptionTier;
	private String apiName;
	private String apiVersion;
	private String department;
	private int apiId;

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

	public String getApiVersion() { return apiVersion; }

	public void setApiVersion(String apiVersion) { this.apiVersion = apiVersion; }

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getApiID() {
		return apiId;
	}

	public void setApiID(int apiId) {
		this.apiId = apiId;
	}
}