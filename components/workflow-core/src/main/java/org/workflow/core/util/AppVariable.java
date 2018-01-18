package org.workflow.core.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum AppVariable {
	NAME("applicationName"),
	WORKFLOWREFID("workflowRefId"),
	CALLBACKURL("callBackUrl"),
	OPARATOR("operators"),
	ID("applicationId"),
    TIER("tier"),
	TIER_NAME("tierName"),
	TIER_STRING("tiersStr"),
    DESCRIPTION("description"),
	APPLICATION_DESCRIPTION("applicationDescription"),
    USERNAME("userName"),
    TIERS("tiers"),
    SERVICEURL("serviceURL"),
    ADMINUSER("adminUserName"),
    MANDATESERVICEURL("mandateServiceURL"),
	SUBSCRIBER("subscriber"),
	API_NAME("apiName"),
	API_TIERS("apiTiers"),
	API_VERSION("apiVersion");
	
	private String varName;
	AppVariable(String paramName){
		varName = paramName;
	}
	public String key() {
		return this.varName;	
	}
	
	private static final Map<String,AppVariable> keyMap = new HashMap<String,AppVariable>();
	
	static {
		
		for (AppVariable iterable_element : EnumSet.allOf( AppVariable.class)) {
			keyMap.put(iterable_element.key(), iterable_element);
		} 
	}
	public static AppVariable getByKey(String key) {
		return keyMap.get(key);
	}
}
