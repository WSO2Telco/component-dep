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
    private String spName;
    private String appID;
    private String[] msisdnList;
    private String validationRegex;
    private int validationPrefixGroup;
    private int validationDigitsGroup;

    public String getApiID() {
        return apiID;
    }

    public void setApiID(String apiID) {
        this.apiID = apiID;
    }

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

    public String[] getMsisdnList() {
        return msisdnList;
    }

    public void setMsisdnList(String[] msisdnList) {
        this.msisdnList = msisdnList;
    }

    public String getValidationRegex() {
        return validationRegex;
    }

    public void setValidationRegex(String validationRegex) {
        this.validationRegex = validationRegex;
    }

    public int getValidationPrefixGroup() {
        return validationPrefixGroup;
    }

    public void setValidationPrefixGroup(int validationPrefixGroup) {
        this.validationPrefixGroup = validationPrefixGroup;
    }

    public int getValidationDigitsGroup() {
        return validationDigitsGroup;
    }

    public void setValidationDigitsGroup(int validationDigitsGroup) {
        this.validationDigitsGroup = validationDigitsGroup;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }
}
