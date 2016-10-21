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
public class SubscriptionApprovalAuditRecord {

	private String apiProvider;
	private String apiName;
	private String apiVersion;
	private int appId;
	private String subStatus;
	private String subApprovalType;
	private String completedByRole;
	private String completedByUser;
	private String completedOn;
	
	public String getApiProvider() {
		return apiProvider;
	}
	
	public void setApiProvider(String apiProvider) {
		this.apiProvider = apiProvider;
	}
	
	public String getApiName() {
		return apiName;
	}
	
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	
	public String getApiVersion() {
		return apiVersion;
	}
	
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	
	public int getAppId() {
		return appId;
	}
	
	public void setAppId(int appId) {
		this.appId = appId;
	}
	
	public String getSubStatus() {
		return subStatus;
	}
	
	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}
	
	public String getCompletedByUser() {
		return completedByUser;
	}
	
	public void setCompletedByUser(String completedByUser) {
		this.completedByUser = completedByUser;
	}

	public String getSubApprovalType() {
		return subApprovalType;
	}

	public void setSubApprovalType(String subApprovalType) {
		this.subApprovalType = subApprovalType;
	}

	public String getCompletedByRole() {
		return completedByRole;
	}

	public void setCompletedByRole(String completedByRole) {
		this.completedByRole = completedByRole;
	}

	public String getCompletedOn() {
		return completedOn;
	}

	public void setCompletedOn(String completedOn) {
		this.completedOn = completedOn;
	}
}
