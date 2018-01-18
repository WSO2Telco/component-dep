package org.workflow.core.model.rate;

/**
 * Created by manoj on 10/12/17.
 */


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "operationRateId",
        "rateDefId",
        "rateDefName",
        "rateDefDescription",
        "rateDefDefault",
        "currency",
        "rateType",
        "rateDefCategoryBase",
        "tariff",
        "createdBy"
})
public class RateDefinition {

    @JsonProperty("operationRateId")
    private Integer operationRateId;
    @JsonProperty("rateDefId")
    private Integer rateDefId;
    @JsonProperty("rateDefName")
    private String rateDefName;
    @JsonProperty("rateDefDescription")
    private String rateDefDescription;
    @JsonProperty("rateDefDefault")
    private Integer rateDefDefault;
    @JsonProperty("currency")
    private Currency currency;
    @JsonProperty("rateType")
    private RateType rateType;
    @JsonProperty("rateDefCategoryBase")
    private Integer rateDefCategoryBase;
    @JsonProperty("tariff")
    private Tariff tariff;
    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("operationRateId")
    public Integer getOperationRateId() {
        return operationRateId;
    }

    @JsonProperty("operationRateId")
    public void setOperationRateId(Integer operationRateId) {
        this.operationRateId = operationRateId;
    }

    @JsonProperty("rateDefId")
    public Integer getRateDefId() {
        return rateDefId;
    }

    @JsonProperty("rateDefId")
    public void setRateDefId(Integer rateDefId) {
        this.rateDefId = rateDefId;
    }

    @JsonProperty("rateDefName")
    public String getRateDefName() {
        return rateDefName;
    }

    @JsonProperty("rateDefName")
    public void setRateDefName(String rateDefName) {
        this.rateDefName = rateDefName;
    }

    @JsonProperty("rateDefDescription")
    public String getRateDefDescription() {
        return rateDefDescription;
    }

    @JsonProperty("rateDefDescription")
    public void setRateDefDescription(String rateDefDescription) {
        this.rateDefDescription = rateDefDescription;
    }

    @JsonProperty("rateDefDefault")
    public Integer getRateDefDefault() {
        return rateDefDefault;
    }

    @JsonProperty("rateDefDefault")
    public void setRateDefDefault(Integer rateDefDefault) {
        this.rateDefDefault = rateDefDefault;
    }

    @JsonProperty("currency")
    public Currency getCurrencyDAO() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrencyDAO(Currency currency) {
        this.currency = currency;
    }

    @JsonProperty("rateType")
    public RateType getRateTypeDAO() {
        return rateType;
    }

    @JsonProperty("rateType")
    public void setRateTypeDAO(RateType rateType) {
        this.rateType = rateType;
    }

    @JsonProperty("rateDefCategoryBase")
    public Integer getRateDefCategoryBase() {
        return rateDefCategoryBase;
    }

    @JsonProperty("rateDefCategoryBase")
    public void setRateDefCategoryBase(Integer rateDefCategoryBase) {
        this.rateDefCategoryBase = rateDefCategoryBase;
    }

    @JsonProperty("tariff")
    public Tariff getTariffDAO() {
        return tariff;
    }

    @JsonProperty("tariff")
    public void setTariffDAO(Tariff tariff) {
        this.tariff = tariff;
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