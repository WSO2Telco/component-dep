package com.wso2telco.custom.hostobjects.util;

public enum ChargingPlanType {

	UNLIMITED("Unlimited"), GOLD("Gold"), SILVER("Silver"), BRONZE("Bronze"),PREMIUM("Premium"),SUBSCRIPTION("Subscription"),REQUEST_BASED("Requestbased");

	private String value;

	private ChargingPlanType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
