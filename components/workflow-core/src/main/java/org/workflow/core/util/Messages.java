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
public enum Messages {

    /** to define error messages*/
    ERROR_MESSAGES("ERROR MESSAGES"),
    TASK_APPROVAL_SUCCESS("Task Assigned Successfully"),
    TASK_APPROVAL_FAILED("Task Assigning Failed"),
    APPLICATION_APPROVAL_SUCCESS("Application Approved Successfully"),
    MY_APPLICATION_LOAD_SUCCESS("My applications loaded successfully"),
    ALL_APPLICATION_LOAD_SUCCESS("All applications loaded successfully"),
    MY_APPLICATION_LOAD_FAIL("My applications loading failed"),
    ALL_APPLICATION_LOAD_FAIL("All applications loading failed"),
    MY_SUBSCRIPTION_LOAD_SUCCESS("My subscriptions loaded successfully"),
    ALL_SUBSCRIPTION_LOAD_SUCCESS("All subscriptions loaded successfully"),
    MY_SUBSCRIPTION_LOAD_FAIL("My subscriptions loading failed"),
    ALL_SUBSCRIPTION_LOAD_FAIL("All subscriptions loading failed"),
    APPLICATION_APPROVAL_FAILED("Error While Application Approval"),
    SUBSCRIPTION_APPROVAL_SUCCESS("Subscription Approved Successfully"),
    SUBSCRIPTION_APPROVAL_FAILED("Error While Subscription Approval"),
    APPLICATION_HISTORY_SUCCESS("Application Approval History Loaded Successfully"),
    APPLICATION_HISTORY_FAILED("Error Loading Application Approval History"),
    SUBSCRIPTION_HISTORY_SUCCESS("Subscription Approval History Loaded Successfully"),
    SUBSCRIPTION_HISTORY_FALIED("Error Loading Subscription Approval History");

    private String value;

    Messages(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
