package com.wso2telco.services.qs.entity;

import java.util.Date;

public class QuotaReqBean {
	private String byFlag;
	private String info;
	private Date fromDate;
	private Date toDate;

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

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}



}
