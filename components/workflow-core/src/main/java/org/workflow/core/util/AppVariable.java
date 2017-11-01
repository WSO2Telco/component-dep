package org.workflow.core.util;

public enum AppVariable {
	NAME("applicationName"),
	WORKFLOWREFID("workflowRefId"),
	CALLBACKURL("callBackUrl"),
	OPARATOR("operators"),
	ID("applicationId"),
    TIER("tier"),
    DESCRIPTION("description"),
    USERNAME("userName"),
    TIERS("tiers"),
    SERVICEURL("serviceURL"),
    ADMINUSER("adminUserName"),
    MANDATESERVICEURL("mandateServiceURL");
	
	private String varName;
	AppVariable(String paramName){
		varName = paramName;
	}
	public String key() {
		return this.varName;	
	}
}
