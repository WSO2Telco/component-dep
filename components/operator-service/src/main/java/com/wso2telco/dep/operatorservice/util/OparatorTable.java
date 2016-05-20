package com.wso2telco.dep.operatorservice.util;

public enum OparatorTable {
	OPARATOR("operators");
	OparatorTable(String tObject) {
		this.tObject = tObject;
	}
	
	public String getTObject(){
		return this.tObject;
	}

	String tObject;
}
