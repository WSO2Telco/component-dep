package com.wso2telco.custom.hostobjects.util;

import java.util.List;

public class RefundEntity{
	private String name;
	private List<String> refundList;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getRefundList() {
		return refundList;
	}
	public void setRefundList(List<String> refundList) {
		this.refundList = refundList;
	}		
}