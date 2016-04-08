/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiata.dialog.entity;

/**
 *
 * @author tharanga_07219
 */
public class WhiteList {
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
