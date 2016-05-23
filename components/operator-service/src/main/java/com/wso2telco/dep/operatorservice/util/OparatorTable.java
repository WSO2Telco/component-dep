package com.wso2telco.dep.operatorservice.util;

public enum OparatorTable {
	OPARATOR("operators"),
	MERCHANT_OPCO_BLACKLIST("merchantopco_blacklist"),
	BLACKLIST_MSISDN("blacklistmsisdn"),
	SUBSCRIPTION_WHITELIST("subscription_WhiteList"),
	SUB_APPROVAL_OPERATORS("sub_approval_operators");
	OparatorTable(String tObject) {
		this.tObject = tObject;
	}
	
	public String getTObject(){
		return this.tObject;
	}

	String tObject;
}
