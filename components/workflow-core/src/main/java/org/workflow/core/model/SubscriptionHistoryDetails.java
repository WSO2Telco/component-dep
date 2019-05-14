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
        "subscriptionId",
        "tier",
        "apiId",
        "apiName",
        "apiName",
        "applicationId",
        "applicationName",
        "subscriptionStatus",
        "createdBy"
})
public class SubscriptionHistoryDetails {

    @JsonProperty("subscriptionId")
    private Integer subscriptionId;
    @JsonProperty("tier")
    private String tier;
    @JsonProperty("apiId")
    private Integer apiId;
    @JsonProperty("apiName")
    private String apiName;
    @JsonProperty("applicationId")
    private Integer applicationId;
    @JsonProperty("applicationName")
    private String applicationName;
    @JsonProperty("subscriptionStatus")
    private String subscriptionStatus;
    @JsonProperty("createdBy")
    private String createdBy;

    public SubscriptionHistoryDetails(ResultSet rs) throws SQLException {
        this.subscriptionId = rs.getInt("subscription_id");
        this.tier = rs.getString("tier_id");
        this.apiId = rs.getInt("api_id");
        this.apiName = rs.getString("api_name");
        this.applicationId = rs.getInt("application_id");
        this.applicationName = rs.getString("application_name");
        this.subscriptionStatus = rs.getString("sub_status");
        this.createdBy = rs.getString("created_by");
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

    @JsonProperty("subscriptionStatus")
    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    @JsonProperty("tier")
    public String getTier() { return tier; }

    @JsonProperty("subscriptionId")
    public Integer getSubscriptionId() { return subscriptionId; }
}