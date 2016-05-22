/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.services.bw.entity;

/**
 *
 * @author tharanga_07219
 */
public class BlackList {
    private String apiID;
    private String apiName;
    private String userID;
    
    public String getAPIID(){
        return this.apiID;
    }
    
    public String setAPIID(String apiID){
        return this.apiID = apiID;
    }
    
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
}
