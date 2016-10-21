/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.services.bw.entity;

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
