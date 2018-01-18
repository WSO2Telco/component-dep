package org.workflow.core.model.rate;

/**
 * Created by manoj on 10/12/17.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "currencyId",
        "currencyCode",
        "currencyDescription",
        "createdBy"
})
public class Currency {

    @JsonProperty("currencyId")
    private Integer currencyId;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("currencyDescription")
    private String currencyDescription;
    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("currencyId")
    public Integer getCurrencyId() {
        return currencyId;
    }

    @JsonProperty("currencyId")
    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("currencyDescription")
    public String getCurrencyDescription() {
        return currencyDescription;
    }

    @JsonProperty("currencyDescription")
    public void setCurrencyDescription(String currencyDescription) {
        this.currencyDescription = currencyDescription;
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
