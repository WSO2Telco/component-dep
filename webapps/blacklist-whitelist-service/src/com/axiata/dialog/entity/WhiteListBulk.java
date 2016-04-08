package com.axiata.dialog.entity;
//=====================================================PRIYANKA_06608
public class WhiteListBulk {
    private String apiId;
    private String appId;
    private String apiName;
    private String userID;
    private String[] msisdnList;
/*    
    public String getAPIID(){
        return this.apiID;
    }
    
    public String setAPIID(String apiID){
        return this.apiID = apiID;
    }*/
    
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


}
