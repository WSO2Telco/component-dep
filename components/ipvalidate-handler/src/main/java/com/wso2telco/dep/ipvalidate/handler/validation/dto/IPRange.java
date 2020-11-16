package com.wso2telco.dep.ipvalidate.handler.validation.dto;

public class IPRange {

	private int rangeId = 0;
	private String startIP = null;
	private String endIP = null;

	public int getRangeId() {
		return rangeId;
	}

	public void setRangeId(int rangeId) {
		this.rangeId = rangeId;
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
		return "IPRange [rangeId=" + rangeId + ", startIP=" + startIP + ", endIP=" + endIP + "]";
	}

	
}
