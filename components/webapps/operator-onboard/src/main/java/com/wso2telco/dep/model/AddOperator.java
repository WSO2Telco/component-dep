package com.wso2telco.dep.model;

public class AddOperator {
	
	private String operatorName;
	
	private String description;
	
	private String tokenAuth;
	
	private Double tokenValidity;
	
	private Double tokenTime;
	
	private String accessToken;
	
	private String refreshToken;
	
	private String tokenURL;

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTokenAuth() {
		return tokenAuth;
	}

	public void setTokenAuth(String tokenAuth) {
		this.tokenAuth = tokenAuth;
	}

	public Double getTokenValidity() {
		return tokenValidity;
	}

	public void setTokenValidity(Double tokenValidity) {
		this.tokenValidity = tokenValidity;
	}

	public Double getTokenTime() {
		return tokenTime;
	}

	public void setTokenTime(Double tokenTime) {
		this.tokenTime = tokenTime;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getTokenURL() {
		return tokenURL;
	}

	public void setTokenURL(String tokenURL) {
		this.tokenURL = tokenURL;
	}
}
