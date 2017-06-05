package com.wso2telco.dep.billingservice.util;

public enum DatabaseTables {

	IN_MD_API("inmdapi"),
	IN_MD_OPERATOR("inmdoperator"),
	IN_MD_SERVICES("inmdservices"),
	IN_OPERATION_RATE("inmdoperationrate"),
	IN_MD_RATE("inmdrate"),
	IN_MD_OPERATOR_RATE("inmdoperatorrate"),
	IN_MD_NB_SUBSCRIPTION_RATE("inmdnbsubscriptionrate"),
	IN_MD_SB_SUBSCRIPTIONS("inmdsbsubscriptions");
	
	DatabaseTables(String tObject) {
		
		this.tObject = tObject;
	}
	
	public String getTObject(){
		
		return this.tObject;
	}

	String tObject;
}
