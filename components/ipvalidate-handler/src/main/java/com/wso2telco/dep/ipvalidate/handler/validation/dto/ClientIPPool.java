package com.wso2telco.dep.ipvalidate.handler.validation.dto;

import java.util.ArrayList;

public class ClientIPPool {

	private int summaryId = 0;
	private ArrayList<String> ip = null;

	public int getSummaryId() {
		return summaryId;
	}

	public void setSummaryId(int summaryId) {
		this.summaryId = summaryId;
	}

	public ArrayList<String> getIp() {
		return ip;
	}

	public void setIp(ArrayList<String> ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		return "ClientIPPool [summaryId=" + summaryId + ", ip=" + ip + "]";
	}
	
}
