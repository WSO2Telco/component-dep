package com.wso2telco.dep.ipvalidate.handler.validation.dto;

public class ClientIPRange {

	private int summaryId = 0;
	private String startIP = null;
	private String endIP = null;

	public int getSummaryId() {
		return summaryId;
	}

	public void setSummaryId(int summaryId) {
		this.summaryId = summaryId;
	}
	
	public String getStartIP() {
		return startIP;
	}

	public void setStartIP(String startIP) {
		this.startIP = startIP;
	}

	public String getEndIP() {
		return endIP;
	}

	public void setEndIP(String endIP) {
		this.endIP = endIP;
	}

	@Override
	public String toString() {
		return "ClientIPRange [summaryId=" + summaryId + ", startIP=" + startIP + ", endIP=" + endIP + "]";
	}


}
