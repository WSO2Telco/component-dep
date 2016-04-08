/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.services.bw.entity;

import java.io.Serializable;

public class WhiteList implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -9154995737075601217L;
	private String subscriptionID;
    private String apiID;
    private String applicationID;
    
    
    public WhiteList() {
    }
    
    public String getSubscriptionID(){
        return this.subscriptionID;
    }
    
    public String getAPIID(){
        return this.apiID;
    }
    
    public String getApplicationID(){
        return this.applicationID;
    }
    
    public void setSubscriptionID(String subscriptionID){
        this.subscriptionID = subscriptionID;
    }
    
    public void setAPIID(String apiID){
        this.apiID = apiID;
    }
    
    public void setApplicationID(String applicationID){
        this.applicationID = applicationID;
    }
   
}
