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
public class ApplicationApprovalAuditRecord {

	private String appName;
	private String appCreator;
	private String appStatus;
	private String appApprovalType;
	private String completedByRole;
	private String completedByUser;
	private String completedOn;
	
	
	public String getAppName() {
		return appName;
	}
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getAppCreator() {
		return appCreator;
	}
	
	public void setAppCreator(String appCreator) {
		this.appCreator = appCreator;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public String getCompletedByUser() {
		return completedByUser;
	}

	public void setCompletedByUser(String completedByUser) {
		this.completedByUser = completedByUser;
	}

	public String getAppApprovalType() {
		return appApprovalType;
	}

	public void setAppApprovalType(String appApprovalType) {
		this.appApprovalType = appApprovalType;
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
