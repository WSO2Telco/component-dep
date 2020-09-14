package com.wso2telco.workflow.utils;

public enum APIMgtDatabaseTables {

	AM_APPLICATION("am_application"),
	AM_SUBSCRIPTION("am_subscription");
	
	private String tableName;

	APIMgtDatabaseTables(String tableName) {

		this.tableName = tableName;
	}

	public String getTableName() {

		return this.tableName;
	}
}