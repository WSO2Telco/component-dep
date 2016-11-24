/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.wso2telco.dep.mediator.unmarshaler;

import java.util.ArrayList;
import java.util.List;

public class GroupDTO {

    private String groupName;
    private String operator;
   private String dayAmount;
    private String monthAmount;
    private String userInfoEnabled;
    private List<ServiceProviderDTO> serviceProviderList =new ArrayList<ServiceProviderDTO>();

    GroupDTO(){
    }

    private  GroupDTO( String groupName,String operator, String dayAmount,String monthAmount, String useInfoEnabled){
        this.groupName=groupName;
        this.operator =operator;
        this.dayAmount=dayAmount;
        this.monthAmount=monthAmount;
        this.userInfoEnabled=useInfoEnabled;
    }

    public String getGroupName() {
       return groupName;
    }

   public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOperator() {
        return operator;
   }

    public void setOperator(String operator) {
        this.operator = operator;
    }
    public String getDayAmount() {
        return dayAmount;
    }

    public void setDayAmount(String dayAmount) {
       this.dayAmount = dayAmount;
    }

    public String getMonthAmount() {
        return monthAmount;
   }

    public void setMonthAmount(String monthAmount) {
        this.monthAmount = monthAmount;
    }
    public List<ServiceProviderDTO> getServiceProviderList() {
        return serviceProviderList;
   }
    public void setServiceProviderList(List<ServiceProviderDTO> serviceProviderList) {
        this.serviceProviderList = serviceProviderList;
    }

    public String getUserInfoEnabled() {
        return userInfoEnabled;
    }

    public void setUserInfoEnabled(String userInfoEnabled) {
        this.userInfoEnabled = userInfoEnabled;
    }

    public GroupDTO clone(){
        return new GroupDTO(this.groupName,this.operator, this.dayAmount,this.monthAmount, this.userInfoEnabled);
    }
}
