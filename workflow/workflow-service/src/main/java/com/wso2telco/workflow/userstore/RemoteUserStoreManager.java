package com.wso2telco.workflow.userstore;

public interface RemoteUserStoreManager {

	String[] getUserListOfRole(String role) ;
	String getUserClaimValue(String userName, String claim);
}
