package com.wso2telco.dep.operatorservice.model;

import java.io.Serializable;
import java.util.Arrays;

public class BlackListDTO extends AbstractWBDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2084976224501906019L;

	/**
	 * 
	 */
	
	private String apiName;
	private String userID;

	
	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	@Override
	public String toString() {
		return "BlackListDTO [userMSISDN=" + Arrays.toString(getUserMSISDN()) + ", apiID=" + getApiID() + ", apiName=" + apiName
				+ ", userID=" + userID + "]";
	}

}
