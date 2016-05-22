package com.wso2telco.dep.operatorservice.model;

import com.wso2telco.core.msisdnvalidator.MSISDN;

public class BlackLIstedMSISDN extends MSISDN {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5001966164641369040L;

	public BlackLIstedMSISDN(int countryCode, long nationalNumber, String apiID, String userID) {
		super(countryCode, nationalNumber);
		this.apiID = apiID;
		this.userID = userID;
	}

	private String apiID;
	private String userID;

	public String getApiID() {
		return apiID;
	}
	public String getUserID() {
		return userID;
	}
	
	

}
