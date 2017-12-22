package org.workflow.core.model;

/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "applicationId",
        "applicationName",
        "applicationDescription",
        "status",
        "approvedOn",
        "createdBy"
})
public class HistoryDetails {

    @JsonProperty("applicationId")
    private Integer applicationId;
    @JsonProperty("applicationName")
    private String applicationName;
    @JsonProperty("applicationDescription")
    private String applicationDescription;
    @JsonProperty("status")
    private String status;
    @JsonProperty("approvedOn")
    private String approvedOn;
    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty("applicationId")
    public Integer getApplicationId() {
        return applicationId;
    }

    @JsonProperty("applicationId")
    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    @JsonProperty("applicationName")
    public String getApplicationName() {
        return applicationName;
    }

    @JsonProperty("applicationName")
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @JsonProperty("applicationDescription")
    public String getApplicationDescription() {
        return applicationDescription;
    }

    @JsonProperty("applicationDescription")
    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("approvedOn")
    public String getApprovedOn() {
        return approvedOn;
    }

    @JsonProperty("approvedOn")
    public void setApprovedOn(String approvedOn) {
        this.approvedOn = approvedOn;
    }

}