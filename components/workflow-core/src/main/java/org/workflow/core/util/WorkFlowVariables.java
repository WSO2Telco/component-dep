package org.workflow.core.util;

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
public enum  WorkFlowVariables {

    HUB_ADMIN_APPROVAL("hubAdminApproval"),
    OPERATOR_ADMIN_APPROVAL("operatorAdminApproval"),
    HUB_ADMIN_ROLE("admin"),
    OPERATOR_ADMIN_ROLE("operator1-admin-role"),
    API_PUBLISHER_ADMIN_APPROVAL(""),
    DATE_FORMAT("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
    DATE_FORMAT2("dd-MMM-yyyy"),
    TIME_FORMAT("HH:mm:ss"),
    OFFSET_FORMAT("XXX"),
    DESCRIPTION("description"),
    SELECTGED_TIER("selectedTier"),
    COMPLETED_ON("completedOn"),
    COMPLETED_BY("completedByUser"),
    SLECTED_RATE("selectedRate"),
    CREDIT_PLAN("creditPlan"),
    STATUS("status"),
    ACTION("complete"),
    ADMIN_ROLE("manage-app-admin"),
    ASSIGN_ACTION("claim");


    private String value;

    WorkFlowVariables(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
