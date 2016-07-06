package com.wso2telco.services.bw.entity;

import java.io.Serializable;
import java.util.Arrays;

public class WhiteListBulk implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6079118123267474720L;
	private String apiId;
    private String appId;
    private String apiName;
    private String userID;
    private String[] msisdnList;
    
    
    public String getAPIName(){
        return this.apiName;
    }
    
    public String setAPIName(String apiName){
        return this.apiName = apiName;
    }
    
    public String getUserID(){
        return this.userID;
    }
    
    public String setUserID(String userID){
        return this.userID = userID;
    }
    
    public String[] getMsisdnList(){
        return this.msisdnList;
    }
    
    public String[] setMsisdnList(String[] msisdnList){
        return this.msisdnList = msisdnList;
    }

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Override
	public String toString() {
		return "WhiteListBulk [apiId=" + apiId + ", appId=" + appId + ", apiName=" + apiName + ", userID=" + userID
				+ ", msisdnList=" + Arrays.toString(msisdnList) + "]";
	}


	
}
