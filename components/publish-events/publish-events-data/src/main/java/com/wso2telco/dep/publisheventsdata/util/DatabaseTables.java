package com.wso2telco.dep.publisheventsdata.util;

public enum DatabaseTables {
	
	DAY_SPEND_LIMIT_DATA("day_spendlimitdata"),
	MONTH_SPEND_LIMIT_DATA("month_spendlimitdata");
	
	private String tableName;

	DatabaseTables(String tableName) {

		this.tableName = tableName;
	}

	public String getTableName() {

		return this.tableName;
	}
}
