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

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public HistoryDetails(ResultSet rs) throws SQLException {
        this.applicationId = rs.getInt("application_id");
        this.applicationName = rs.getString("name");
        this.applicationDescription = rs.getString("description");
        this.status = rs.getString("app_status");
        this.createdBy = rs.getString("created_by");
        this.approvedOn = (rs.getString("oparators") != null) ? rs.getString("oparators") : "None";
    }

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("applicationId")
    public Integer getApplicationId() {
        return applicationId;
    }

    @JsonProperty("applicationName")
    public String getApplicationName() {
        return applicationName;
    }

    @JsonProperty("applicationDescription")
    public String getApplicationDescription() {
        return applicationDescription;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("approvedOn")
    public String getApprovedOn() {
        return approvedOn;
    }
}