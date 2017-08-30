/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.ratecardservice.dao.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class APIOperationDTO {

	private Integer apiOperationId;
	private APIDTO api;
	private String apiOperation;
	private String apiOperationCode;
	private String createdBy;
	
	public Integer getApiOperationId() {
		return apiOperationId;
	}
	public void setApiOperationId(Integer apiOperationId) {
		this.apiOperationId = apiOperationId;
	}
	public APIDTO getApi() {
		return api;
	}
	public void setApi(APIDTO api) {
		this.api = api;
	}
	public String getApiOperation() {
		return apiOperation;
	}
	public void setApiOperation(String apiOperation) {
		this.apiOperation = apiOperation;
	}
	public String getApiOperationCode() {
		return apiOperationCode;
	}
	public void setApiOperationCode(String apiOperationCode) {
		this.apiOperationCode = apiOperationCode;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
