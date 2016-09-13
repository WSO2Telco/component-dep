package com.wso2telco.workflow.in;

public interface RemoteUserStoreManager {

	String[] getUserListOfRole(String role);
	String getUserClaimValue(String userName, String claim);
}
