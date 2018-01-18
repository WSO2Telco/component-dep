package org.workflow.core.activity;

import com.fasterxml.jackson.annotation.*;
import org.workflow.core.model.RequestVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "action",
        "variables"
})
public class TaskApprovalRequest {

    @JsonProperty("action")
    private String action;
    @JsonProperty("variables")
    private List<RequestVariable> variables = null;

    @JsonProperty("action")
    public String getAction() {
        return action;
    }

    @JsonProperty("action")
    public void setAction(String action) {
        this.action = action;
    }

    @JsonProperty("variables")
    public List<RequestVariable> getVariables() {
        return variables;
    }

    @JsonProperty("variables")
    public void setVariables(List<RequestVariable> variables) {
        this.variables = variables;
    }
}
