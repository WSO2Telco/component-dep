package com.wso2telco.services.qs.entity;

public class QuotaReqBean {
	private String byFlag;
	private String info;
	private String operator;
	private String sp;
	private String app;
		
	public String getSp() {
		return sp;
	}

	public void setSp(String sp) {
		this.sp = sp;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
