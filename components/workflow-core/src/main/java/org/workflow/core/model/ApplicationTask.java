package org.workflow.core.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.workflow.core.util.AppVariable;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "assignee",
        "createTime",
        "taskDescription",
        "applicationId",
        "applicationName",
        "applicationDescription",
        "operators",
        "tier",
        "tiersStr",
        "userName",
        "creditPlan",
        "relevantRates",
        "selectedRate"
})
public class ApplicationTask {

    @JsonProperty("id")
    private String id;
    @JsonProperty("assignee")
    private String assignee;
    @JsonProperty("createTime")
    private CreateTime createTime;
    @JsonProperty("taskDescription")
    private String taskDescription;
    @JsonProperty("applicationId")
    private String applicationId;
    @JsonProperty("applicationName")
    private String applicationName;
    @JsonProperty("applicationDescription")
    private String applicationDescription;
    @JsonProperty("operators")
    private String operators;
    @JsonProperty("tier")
    private String tier;
    @JsonProperty("tiersStr")
    private List<String> tiersStr = null;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("creditPlan")
    private String creditPlan;

    public ApplicationTask(Map<AppVariable, TaskVariableResponse> varMap) {
        this.applicationDescription = (varMap.containsKey(AppVariable.DESCRIPTION)) ? varMap.get(AppVariable.DESCRIPTION).getValue() : "";
        this.tier = (varMap.containsKey(AppVariable.TIER)) ? varMap.get(AppVariable.TIER).getValue() : "";
        this.applicationId = (varMap.containsKey(AppVariable.ID)) ? varMap.get(AppVariable.ID).getValue() : "";
        this.applicationName = (varMap.containsKey(AppVariable.NAME)) ? varMap.get(AppVariable.NAME).getValue() : "";
        this.operators = (varMap.containsKey(AppVariable.OPARATOR)) ? varMap.get(AppVariable.OPARATOR).getValue() : "";
        this.userName = (varMap.containsKey(AppVariable.USERNAME)) ? varMap.get(AppVariable.USERNAME).getValue() : "";
        this.creditPlan = "";
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("assignee")
    public Object getAssignee() {
        return assignee;
    }

    @JsonProperty("assignee")
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @JsonProperty("createTime")
    public CreateTime getCreateTime() {
        return createTime;
    }

    @JsonProperty("createTime")
    public void setCreateTime(CreateTime createTime) {
        this.createTime = createTime;
    }

    @JsonProperty("taskDescription")
    public Object getTaskDescription() {
        return taskDescription;
    }

    @JsonProperty("taskDescription")
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    @JsonProperty("applicationId")
    public String getApplicationId() {
        return applicationId;
    }

    @JsonProperty("applicationId")
    public void setApplicationId(String applicationId) {
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

    @JsonProperty("operators")
    public String getOperators() {
        return operators;
    }

    @JsonProperty("tier")
    public String getTier() {
        return tier;
    }

    @JsonProperty("tiersStr")
    public List<String> getTiersStr() {
        return tiersStr;
    }

    @JsonProperty("tiersStr")
    public void setTiersStr(List<String> tiersStr) {
        this.tiersStr = tiersStr;
    }

    @JsonProperty("userName")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("creditPlan")
    public String getCreditPlan() {
        return creditPlan;
    }
}