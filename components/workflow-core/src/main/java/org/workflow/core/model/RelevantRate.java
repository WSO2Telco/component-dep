package org.workflow.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.workflow.core.model.rate.RateDefinition;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "apiOperation",
        "rateDefinitions"
})
public class RelevantRate {

    @JsonProperty("apiOperation")
    private String apiOperation;
    @JsonProperty("rateDefinitions")
    private List<RateDefinition> rateDefinitions = null;

    @JsonProperty("apiOperation")
    public String getApiOperation() {
        return apiOperation;
    }

    @JsonProperty("apiOperation")
    public void setApiOperation(String apiOperation) {
        this.apiOperation = apiOperation;
    }

    @JsonProperty("rateDefinitions")
    public List<RateDefinition> getRateDefinitions() {
        return rateDefinitions;
    }

    @JsonProperty("rateDefinitions")
    public void setRateDefinitions(List<RateDefinition> rateDefinitions) {
        this.rateDefinitions = rateDefinitions;
    }
}