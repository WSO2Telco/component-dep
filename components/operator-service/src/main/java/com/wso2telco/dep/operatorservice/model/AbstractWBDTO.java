package com.wso2telco.dep.operatorservice.model;

abstract class AbstractWBDTO {
	private String[] userMSISDN;
	private String apiID;

	public String[] getUserMSISDN() {
		return userMSISDN;
	}

	public void setUserMSISDN(String[] userMSISDN) {
		this.userMSISDN = userMSISDN;
	}

	public void setUserMSISDN(String userMSISDN) {
		this.userMSISDN = new String[] { userMSISDN };
	}
	private boolean hasValidApiID_;
	public String getApiID() {
		return apiID;
	}

	public void setApiID(String apiID) {
		if(apiID!=null && apiID.trim().length()>0){
			
			this.apiID = apiID;
			this.hasValidApiID_ =true;
		}
		
	}
	
	public boolean hasValidApiID(){
		return hasValidApiID_;
	}

}
