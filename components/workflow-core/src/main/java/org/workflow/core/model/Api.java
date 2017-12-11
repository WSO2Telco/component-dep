package org.workflow.core.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "apiId",
        "apiName",
        "apiDescription",
        "operations",
        "createdBy"
})
public class Api {

    @JsonProperty("apiId")
    private Integer apiId;
    @JsonProperty("apiName")
    private String apiName;
    @JsonProperty("apiDescription")
    private String apiDescription;
    @JsonProperty("operations")
    private List<Operation> operations = null;
    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("apiId")
    public Integer getApiId() {
        return apiId;
    }

    @JsonProperty("apiId")
    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    @JsonProperty("apiName")
    public String getApiName() {
        return apiName;
    }

    @JsonProperty("apiName")
    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    @JsonProperty("apiDescription")
    public String getApiDescription() {
        return apiDescription;
    }

    @JsonProperty("apiDescription")
    public void setApiDescription(String apiDescription) {
        this.apiDescription = apiDescription;
    }

    @JsonProperty("operations")
    public List<Operation> getOperations() {
        return operations;
    }

    @JsonProperty("operations")
    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}