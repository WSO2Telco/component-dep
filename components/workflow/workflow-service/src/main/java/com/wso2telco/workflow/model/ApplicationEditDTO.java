
package com.wso2telco.workflow.model;

public class ApplicationEditDTO extends BaseEditDTO{

	private String applicationTier;
	private String tokenType;

	public String getApplicationTier() {
		return applicationTier;
	}

	public void setApplicationTier(String applicationTier) {
		this.applicationTier = applicationTier;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
}