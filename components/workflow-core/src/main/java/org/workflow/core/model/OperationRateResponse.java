package org.workflow.core.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "api",
        "operator"
})
public class OperationRateResponse {

    @JsonProperty("api")
    private Api api;
    @JsonProperty("operator")
    private Object operator;

    @JsonProperty("api")
    public Api getApi() {
        return api;
    }

    @JsonProperty("api")
    public void setApi(Api api) {
        this.api = api;
    }

    @JsonProperty("operator")
    public Object getOperator() {
        return operator;
    }

    @JsonProperty("operator")
    public void setOperator(Object operator) {
        this.operator = operator;
    }
}
