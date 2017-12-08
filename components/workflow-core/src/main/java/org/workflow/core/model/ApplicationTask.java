package org.workflow.core.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

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
public class ApplicationTask {

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
    private List<String> tiersStr = null;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("apiVersion")
    private String apiVersion;
    @JsonProperty("subscriber")
    private String subscriber;
    @JsonProperty("relevantRates")
    private List<String> relevantRates = null;
    @JsonProperty("selectedRate")
    private String selectedRate;
    @JsonProperty("creditPlan")
    private String creditPlan;

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

    @JsonProperty("apiName")
    public void setApiName(String apiName) {
        this.apiName = apiName;
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

    @JsonProperty("applicationDescription")
    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    @JsonProperty("operators")
    public String getOperators() {
        return operators;
    }

    @JsonProperty("operators")
    public void setOperators(String operators) {
        this.operators = operators;
    }

    @JsonProperty("tier")
    public String getTier() {
        return tier;
    }

    @JsonProperty("tier")
    public void setTier(String tier) {
        this.tier = tier;
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

    @JsonProperty("userName")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("apiVersion")
    public String getApiVersion() {
        return apiVersion;
    }

    @JsonProperty("apiVersion")
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
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
    public List<String> getRelevantRates() {
        return relevantRates;
    }

    @JsonProperty("relevantRates")
    public void setRelevantRates(List<String> relevantRates) {
        this.relevantRates = relevantRates;
    }

    @JsonProperty("selectedRate")
    public String getSelectedRate() {
        return selectedRate;
    }

    @JsonProperty("selectedRate")
    public void setSelectedRate(String selectedRate) {
        this.selectedRate = selectedRate;
    }

    @JsonProperty("creditPlan")
    public String getCreditPlan() {
        return creditPlan;
    }

    @JsonProperty("creditPlan")
    public void setCreditPlan(String creditPlan) {
        this.creditPlan = creditPlan;
    }

}