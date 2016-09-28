package com.wso2telco.aggregatorblacklist.util;

public enum DatabaseTables
{
	OPERATORAPPS("operatorapps"), 
	ENDPOINTAPPS("endpointapps"),
	OPERATORS("operators"),
	MERCHANTOPCO_BLACKLIST("merchantopco_blacklist");

	private String tableName;

	DatabaseTables(String tableName) {

		this.tableName = tableName;
	}

	public String getTableName() {

		return this.tableName;
	}
}

