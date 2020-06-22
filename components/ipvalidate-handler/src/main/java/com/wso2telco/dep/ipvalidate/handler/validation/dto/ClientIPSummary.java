package com.wso2telco.dep.ipvalidate.handler.validation.dto;

public class ClientIPSummary {

	private int summaryId = 0;
	private String clientId = null;
	private String clientToken = null;
	private boolean isValidationEnabled = false;

	public int getSummaryId() {
		return summaryId;
	}

	public void setSummaryId(int summaryId) {
		this.summaryId = summaryId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientToken() {
		return clientToken;
	}

	public void setClientToken(String clientToken) {
		this.clientToken = clientToken;
	}

	public boolean isValidationEnabled() {
		return isValidationEnabled;
	}

	public void setValidationEnabled(boolean isValidationEnabled) {
		this.isValidationEnabled = isValidationEnabled;
	}

	@Override
	public String toString() {
		return "ClientIPSummary [summaryId=" + summaryId + ", clientId=" + clientId + ", clientToken=" + clientToken
				+ ", isValidationEnabled=" + isValidationEnabled + "]";
	}

}
