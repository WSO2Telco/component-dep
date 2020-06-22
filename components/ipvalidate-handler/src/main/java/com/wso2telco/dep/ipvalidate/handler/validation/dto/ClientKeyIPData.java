package com.wso2telco.dep.ipvalidate.handler.validation.dto;

import java.util.List;

public class ClientKeyIPData {

	private int summaryId = 0;
	private String clientId = null;
	private String clientToken = null;
	private ClientIPPool poolIpList = null;
	private List<ClientIPRange> rangeIpList = null;
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

	public ClientIPPool getPoolIpList() {
		return poolIpList;
	}

	public void setPoolIpList(ClientIPPool poolIpList) {
		this.poolIpList = poolIpList;
	}

	public List<ClientIPRange> getRangeIpList() {
		return rangeIpList;
	}

	public void setRangeIpList(List<ClientIPRange> rangeIpList) {
		this.rangeIpList = rangeIpList;
	}

	public boolean isValidationEnabled() {
		return isValidationEnabled;
	}

	public void setValidationEnabled(boolean isValidationEnabled) {
		this.isValidationEnabled = isValidationEnabled;
	}

	@Override
	public String toString() {
		return "ClientKeyIPData [summaryId=" + summaryId + ", clientId=" + clientId + ", clientToken=" + clientToken
				+ ", poolIpList=" + poolIpList + ", rangeIpList=" + rangeIpList + ", isValidationEnabled="
				+ isValidationEnabled + "]";
	}

}
