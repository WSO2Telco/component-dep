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

package com.wso2telco.workflow.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Subscription {

    private int endpointID;
    private int applicationID;
    private String status;
    private String apiName;
    private String opID;
    private String operatorName;
    private String workflowRefId;
    private String selectedRate;
    private String selectedTier;
    private String apiVersion;
    private String apiProvider;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getApiProvider() {
        return apiProvider;
    }

    public void setApiProvider(String apiProvider) {
        this.apiProvider = apiProvider;
    }


    public String getWorkflowRefId() {
        return workflowRefId;
    }

    public void setWorkflowRefId(String workflowRefId) {
        this.workflowRefId = workflowRefId;
    }


    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOpID() {
        return opID;
    }

    public void setOpID(String opID) {
        this.opID = opID;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    public int getEndpointID() {
        return endpointID;
    }

    public void setEndpointID(int endpointID) {
        this.endpointID = endpointID;
    }

    public String getSelectedRate() {
    	return selectedRate;
    }

    public void setSelectedRate(String selectedRate) {
        this.selectedRate = selectedRate;
    }

    public String getSelectedTier() {
        return selectedTier;
    }

    public void setSelectedTier(String selectedTier) {
        this.selectedTier = selectedTier;
    }



}
