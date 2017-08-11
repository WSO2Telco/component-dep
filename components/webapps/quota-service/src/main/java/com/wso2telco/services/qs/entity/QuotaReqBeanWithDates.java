package com.wso2telco.services.qs.entity;


public class QuotaReqBeanWithDates {

	private String byFlag;
	private String info;
	private String toDate;
	private String fromDate;

	public String getByFlag() {
		return byFlag;
	}

	public void setByFlag(String byFlag) {
		this.byFlag = byFlag;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

}
