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

package com.wso2telco.dep.mediator.executor;

/**
 * Class holds executor data;
 */
public class ExecutorObj {

    private String executorName;
    private String executorDescription;
    private String executorId;
    private String fullQulifiedName;

    public ExecutorObj (String executorName, String executorDescription) {
        this.executorName = executorName;
        this.executorDescription = executorDescription;
    }

    public ExecutorObj (String executorId, String executorName, String executorDescription, String fullQulifiedName) {
        this.executorId = executorId;
        this.executorName = executorName;
        this.executorDescription = executorDescription;
        this.fullQulifiedName = fullQulifiedName;
    }

    public ExecutorObj (String executorName, String fullQulifiedName, String executorDescription) {
        this.executorName = executorName;
        this.fullQulifiedName = fullQulifiedName;
        this.executorDescription = executorDescription;
    }

    public String getExecutorDescription() {
        return executorDescription;
    }

    public void setExecutorDescription(String executorDescription) {
        this.executorDescription = executorDescription;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public String getExecutorId() { return executorId; }

    public void setExecutorId(String executorId) { this.executorId = executorId; }

    public String getFullQulifiedName() { return fullQulifiedName; }

    public void setFullQulifiedName(String fullQulifiedName) { this.fullQulifiedName = fullQulifiedName; }

    //TODO: serialize or not
}
