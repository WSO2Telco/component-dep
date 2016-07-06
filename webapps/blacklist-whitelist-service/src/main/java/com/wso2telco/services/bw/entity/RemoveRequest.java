/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wso2telco.services.bw.entity;

import java.io.Serializable;

public class RemoveRequest implements Serializable{

	private static final long serialVersionUID = 4671872069760564128L;
	private String apiName;
    
    public String getAPIName(){
        return this.apiName;
    }
    
    public String setAPIName(String apiName){
        return this.apiName = apiName;
    }    
}
