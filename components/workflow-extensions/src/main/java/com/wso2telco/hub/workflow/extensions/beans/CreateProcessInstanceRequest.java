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

package com.wso2telco.hub.workflow.extensions.beans;

import java.util.List;

public class CreateProcessInstanceRequest {

    private String processDefinitionKey;

    private String businessKey;

    private List<Variable> variables;

    public CreateProcessInstanceRequest () {}

    public CreateProcessInstanceRequest (String processDefinitionKey, String tenantId) {
        this.processDefinitionKey = processDefinitionKey;
        //this.tenantId = tenantId;
    }


    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    /*public String getTenantId() {
        return tenantId;
    }*/

    /*public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }*/

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }
}
