package org.workflow.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "url",
        "owner",
        "assignee",
        "delegationState",
        "name",
        "description",
        "createTime",
        "dueDate",
        "priority",
        "suspended",
        "taskDefinitionKey",
        "tenantId",
        "category",
        "formKey",
        "parentTaskId",
        "parentTaskUrl",
        "executionId",
        "executionUrl",
        "processInstanceId",
        "processInstanceUrl",
        "processDefinitionId",
        "processDefinitionUrl",
        "variables",
        "operationRates"
})
public class Task {

    @JsonProperty("id")
    private String id;
    @JsonProperty("url")
    private String url;
    @JsonProperty("owner")
    private Object owner;
    @JsonProperty("assignee")
    private String assignee;
    @JsonProperty("delegationState")
    private Object delegationState;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("createTime")
    private String createTime;
    @JsonProperty("dueDate")
    private Object dueDate;
    @JsonProperty("priority")
    private Integer priority;
    @JsonProperty("suspended")
    private Boolean suspended;
    @JsonProperty("taskDefinitionKey")
    private String taskDefinitionKey;
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("category")
    private Object category;
    @JsonProperty("formKey")
    private Object formKey;
    @JsonProperty("parentTaskId")
    private Object parentTaskId;
    @JsonProperty("parentTaskUrl")
    private Object parentTaskUrl;
    @JsonProperty("executionId")
    private String executionId;
    @JsonProperty("executionUrl")
    private String executionUrl;
    @JsonProperty("processInstanceId")
    private String processInstanceId;
    @JsonProperty("processInstanceUrl")
    private String processInstanceUrl;
    @JsonProperty("processDefinitionId")
    private String processDefinitionId;
    @JsonProperty("processDefinitionUrl")
    private String processDefinitionUrl;
    @JsonProperty("variables")
    private TaskVariableResponse[] variables = null;
    @JsonProperty("operationRates")
    private OperationRateResponse operationRates = null;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("owner")
    public Object getOwner() {
        return owner;
    }

    @JsonProperty("owner")
    public void setOwner(Object owner) {
        this.owner = owner;
    }

    @JsonProperty("assignee")
    public String getAssignee() {
        return assignee;
    }

    @JsonProperty("assignee")
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @JsonProperty("delegationState")
    public Object getDelegationState() {
        return delegationState;
    }

    @JsonProperty("delegationState")
    public void setDelegationState(Object delegationState) {
        this.delegationState = delegationState;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("createTime")
    public String getCreateTime() {
        return createTime;
    }

    @JsonProperty("createTime")
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @JsonProperty("dueDate")
    public Object getDueDate() {
        return dueDate;
    }

    @JsonProperty("dueDate")
    public void setDueDate(Object dueDate) {
        this.dueDate = dueDate;
    }

    @JsonProperty("priority")
    public Integer getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @JsonProperty("suspended")
    public Boolean getSuspended() {
        return suspended;
    }

    @JsonProperty("suspended")
    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    @JsonProperty("taskDefinitionKey")
    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    @JsonProperty("taskDefinitionKey")
    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    @JsonProperty("tenantId")
    public String getTenantId() {
        return tenantId;
    }

    @JsonProperty("tenantId")
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @JsonProperty("category")
    public Object getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(Object category) {
        this.category = category;
    }

    @JsonProperty("formKey")
    public Object getFormKey() {
        return formKey;
    }

    @JsonProperty("formKey")
    public void setFormKey(Object formKey) {
        this.formKey = formKey;
    }

    @JsonProperty("parentTaskId")
    public Object getParentTaskId() {
        return parentTaskId;
    }

    @JsonProperty("parentTaskId")
    public void setParentTaskId(Object parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    @JsonProperty("parentTaskUrl")
    public Object getParentTaskUrl() {
        return parentTaskUrl;
    }

    @JsonProperty("parentTaskUrl")
    public void setParentTaskUrl(Object parentTaskUrl) {
        this.parentTaskUrl = parentTaskUrl;
    }

    @JsonProperty("executionId")
    public String getExecutionId() {
        return executionId;
    }

    @JsonProperty("executionId")
    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    @JsonProperty("executionUrl")
    public String getExecutionUrl() {
        return executionUrl;
    }

    @JsonProperty("executionUrl")
    public void setExecutionUrl(String executionUrl) {
        this.executionUrl = executionUrl;
    }

    @JsonProperty("processInstanceId")
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    @JsonProperty("processInstanceId")
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @JsonProperty("processInstanceUrl")
    public String getProcessInstanceUrl() {
        return processInstanceUrl;
    }

    @JsonProperty("processInstanceUrl")
    public void setProcessInstanceUrl(String processInstanceUrl) {
        this.processInstanceUrl = processInstanceUrl;
    }

    @JsonProperty("processDefinitionId")
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    @JsonProperty("processDefinitionId")
    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    @JsonProperty("processDefinitionUrl")
    public String getProcessDefinitionUrl() {
        return processDefinitionUrl;
    }

    @JsonProperty("processDefinitionUrl")
    public void setProcessDefinitionUrl(String processDefinitionUrl) {
        this.processDefinitionUrl = processDefinitionUrl;
    }

    @JsonProperty("variables")
    public TaskVariableResponse[] getVariables() {
        return variables;
    }

    @JsonProperty("variables")
    public void setVariables(TaskVariableResponse[] variables) {
        this.variables = variables;
    }

    @JsonProperty("operationRates")
    public OperationRateResponse getOperationRates() {
        return operationRates;
    }

    @JsonProperty("operationRates")
    public void setOperationRates(OperationRateResponse operationRates) {
        this.operationRates = operationRates;
    }

}
