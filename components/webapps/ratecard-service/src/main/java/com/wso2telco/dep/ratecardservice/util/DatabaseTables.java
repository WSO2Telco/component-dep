package com.wso2telco.dep.ratecardservice.util;

public enum DatabaseTables {

	TARIFF("tariff"),
	CURRENCY("currency"),
	RATE_TYPE("rate_type"),
	CATEGORY("category"),
	RATE_DEF("rate_def"),
	RATE_CATEGORY("rate_category"),
	API("api"),
	API_OPERATION("api_operation"),
	OPERATOR("operator"),
	OPERATION_RATE("operation_rate");
	
	DatabaseTables(String tObject) {
		
		this.tObject = tObject;
	}
	
	public String getTObject(){
		
		return this.tObject;
	}

	String tObject;
}
