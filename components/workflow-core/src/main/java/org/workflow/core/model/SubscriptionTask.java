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
        "apiName",
        "createTime",
        "taskDescription",
        "applicationId",
        "applicationName",
        "applicationDescription",
        "operators",
        "tier",
        "tiersStr",
        "userName",
        "apiVersion",
        "subscriber",
        "relevantRates",
        "selectedRate",
        "creditPlan"
})
public class SubscriptionTask {

    @JsonProperty("id")
    private String id;
    @JsonProperty("assignee")
    private String assignee;
    @JsonProperty("apiName")
    private String apiName;
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
    private List<String> tiersStr;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("apiVersion")
    private String apiVersion;
    @JsonProperty("subscriber")
    private String subscriber;
    @JsonProperty("relevantRates")
    private List<RelevantRate> relevantRates;
    @JsonProperty("selectedRate")
    private String selectedRate;

    public SubscriptionTask(Map<AppVariable, TaskVariableResponse> varMap) {
        this.applicationDescription = (varMap.containsKey(AppVariable.APPLICATION_DESCRIPTION))?varMap.get(AppVariable.APPLICATION_DESCRIPTION).getValue():"";
        this.tier = (varMap.containsKey(AppVariable.TIER_NAME))?varMap.get(AppVariable.TIER_NAME).getValue():"";
        this.applicationId = (varMap.containsKey(AppVariable.ID))?varMap.get(AppVariable.ID).getValue():"";
        this.applicationName = (varMap.containsKey(AppVariable.NAME))?varMap.get(AppVariable.NAME).getValue():"";
        this.operators = (varMap.containsKey(AppVariable.OPARATOR))?varMap.get(AppVariable.OPARATOR).getValue():"";
        this.userName = (varMap.containsKey(AppVariable.SUBSCRIBER))?varMap.get(AppVariable.SUBSCRIBER).getValue():"";
        this.apiName = (varMap.containsKey(AppVariable.API_NAME))?varMap.get(AppVariable.API_NAME).getValue():"";
        this.apiVersion = (varMap.containsKey(AppVariable.API_VERSION))?varMap.get(AppVariable.API_VERSION).getValue():"";
        this.selectedRate = "";
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

    @JsonProperty("apiName")
    public String getApiName() {
        return apiName;
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

    @JsonProperty("applicationName")
    public String getApplicationName() {
        return applicationName;
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

    @JsonProperty("apiVersion")
    public String getApiVersion() {
        return apiVersion;
    }

    @JsonProperty("subscriber")
    public String getSubscriber() {
        return subscriber;
    }

    @JsonProperty("subscriber")
    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    @JsonProperty("relevantRates")
    public List<RelevantRate> getRelevantRates() {
        return relevantRates;
    }

    @JsonProperty("relevantRates")
    public void setRelevantRates(List<RelevantRate> relevantRates) {
        this.relevantRates = relevantRates;
    }

    @JsonProperty("selectedRate")
    public String getSelectedRate() {
        return selectedRate;
    }

}