package com.wso2telco.dep.ipvalidate.handler.validation.dto;

public class ClientIDSummary {

	private int summaryId = 0;
	private String clientId = null;
	private String clientKey = null;
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

	public String getClientKey() {
		return clientKey;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}

	public boolean isValidationEnabled() {
		return isValidationEnabled;
	}

	public void setValidationEnabled(boolean isValidationEnabled) {
		this.isValidationEnabled = isValidationEnabled;
	}

	@Override
	public String toString() {
		return "ClientIDSummary [summaryId=" + summaryId + ", clientId=" + clientId + ", clientKey=" + clientKey
				+ ", isValidationEnabled=" + isValidationEnabled + "]";
	}

}
