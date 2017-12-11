package org.workflow.core.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.workflow.core.model.rate.RateDefinition;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "apiOperationId",
        "apiOperationName",
        "apiOperationCode",
        "rates",
        "createdBy"
})
public class Operation {

    @JsonProperty("apiOperationId")
    private Integer apiOperationId;
    @JsonProperty("apiOperationName")
    private String apiOperationName;
    @JsonProperty("apiOperationCode")
    private String apiOperationCode;
    @JsonProperty("rates")
    private List<RateDefinition> rates = null;
    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("apiOperationId")
    public Integer getApiOperationId() {
        return apiOperationId;
    }

    @JsonProperty("apiOperationId")
    public void setApiOperationId(Integer apiOperationId) {
        this.apiOperationId = apiOperationId;
    }

    @JsonProperty("apiOperationName")
    public String getApiOperationName() {
        return apiOperationName;
    }

    @JsonProperty("apiOperationName")
    public void setApiOperationName(String apiOperationName) {
        this.apiOperationName = apiOperationName;
    }

    @JsonProperty("apiOperationCode")
    public String getApiOperationCode() {
        return apiOperationCode;
    }

    @JsonProperty("apiOperationCode")
    public void setApiOperationCode(String apiOperationCode) {
        this.apiOperationCode = apiOperationCode;
    }

    @JsonProperty("rates")
    public List<RateDefinition> getRates() {
        return rates;
    }

    @JsonProperty("rates")
    public void setRates(List<RateDefinition> rates) {
        this.rates = rates;
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