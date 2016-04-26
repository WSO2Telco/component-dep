package com.wso2telco.workflow.model;

public class ApplicationApprovalAuditRecord {
	
	private String appName;
	private String appCreator;
	private String appStatus;
	private String appApprovalType;
	private String completedByRole;
	private String completedByUser;
	private String completedOn;
	
	
	public String getAppName() {
		return appName;
	}
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getAppCreator() {
		return appCreator;
	}
	
	public void setAppCreator(String appCreator) {
		this.appCreator = appCreator;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public String getCompletedByUser() {
		return completedByUser;
	}

	public void setCompletedByUser(String completedByUser) {
		this.completedByUser = completedByUser;
	}

	public String getAppApprovalType() {
		return appApprovalType;
	}

	public void setAppApprovalType(String appApprovalType) {
		this.appApprovalType = appApprovalType;
	}

	public String getCompletedByRole() {
		return completedByRole;
	}

	public void setCompletedByRole(String completedByRole) {
		this.completedByRole = completedByRole;
	}

	public String getCompletedOn() {
		return completedOn;
	}

	public void setCompletedOn(String completedOn) {
		this.completedOn = completedOn;
	}
}
