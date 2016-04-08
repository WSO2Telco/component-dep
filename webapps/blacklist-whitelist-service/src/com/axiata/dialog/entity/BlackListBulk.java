/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.axiata.dialog.entity;

/**
 *
 * @author gayan
 */
public class BlackListBulk {
    private String apiID;
    private String apiName;
    private String userID;
    private String[] msisdnList;
    
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
    
    public String[] getMsisdnList(){
        return this.msisdnList;
    }
    
    public String[] setMsisdnList(String[] msisdnList){
        return this.msisdnList = msisdnList;
    }
}
