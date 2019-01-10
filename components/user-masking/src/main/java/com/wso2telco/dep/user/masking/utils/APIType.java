package com.wso2telco.dep.user.masking.utils;

/**
 * API type enum
 */
public enum APIType {
	USSD("ussd", "Unstructured Supplementary Service Data"),
	PAYMENT("payment","Payment API Service"),
	PAYMENT_V2("payment-v2","Payment API Service"),
	LOCATION("location","location API Service"),
	SMS("smsmessaging","SMS Messageing API Service ");
	private String key;
	private String description;

	APIType(final String key, final String description) {
		this.key = key;
		this.description = description;
	}

	public String getCode(){
		return this.key;
	}
}
