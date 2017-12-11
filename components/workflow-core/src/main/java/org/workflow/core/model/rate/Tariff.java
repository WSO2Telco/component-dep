package org.workflow.core.model.rate;

/**
 * Created by manoj on 10/12/17.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "tariffId",
        "tariffName",
        "tariffDescription",
        "tariffDefaultVal",
        "tariffMaxCount",
        "tariffExcessRate",
        "tariffDefRate",
        "tariffSPCommission",
        "tariffAdsCommission",
        "tariffOpcoCommission",
        "tariffSurChargeval",
        "tariffSurChargeAds",
        "tariffSurChargeOpco",
        "createdBy"
})
public class Tariff {

    @JsonProperty("tariffId")
    private Integer tariffId;
    @JsonProperty("tariffName")
    private String tariffName;
    @JsonProperty("tariffDescription")
    private String tariffDescription;
    @JsonProperty("tariffDefaultVal")
    private Integer tariffDefaultVal;
    @JsonProperty("tariffMaxCount")
    private Integer tariffMaxCount;
    @JsonProperty("tariffExcessRate")
    private Integer tariffExcessRate;
    @JsonProperty("tariffDefRate")
    private Integer tariffDefRate;
    @JsonProperty("tariffSPCommission")
    private Integer tariffSPCommission;
    @JsonProperty("tariffAdsCommission")
    private Integer tariffAdsCommission;
    @JsonProperty("tariffOpcoCommission")
    private Integer tariffOpcoCommission;
    @JsonProperty("tariffSurChargeval")
    private Integer tariffSurChargeval;
    @JsonProperty("tariffSurChargeAds")
    private Integer tariffSurChargeAds;
    @JsonProperty("tariffSurChargeOpco")
    private Integer tariffSurChargeOpco;
    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("tariffId")
    public Integer getTariffId() {
        return tariffId;
    }

    @JsonProperty("tariffId")
    public void setTariffId(Integer tariffId) {
        this.tariffId = tariffId;
    }

    @JsonProperty("tariffName")
    public String getTariffName() {
        return tariffName;
    }

    @JsonProperty("tariffName")
    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }

    @JsonProperty("tariffDescription")
    public String getTariffDescription() {
        return tariffDescription;
    }

    @JsonProperty("tariffDescription")
    public void setTariffDescription(String tariffDescription) {
        this.tariffDescription = tariffDescription;
    }

    @JsonProperty("tariffDefaultVal")
    public Integer getTariffDefaultVal() {
        return tariffDefaultVal;
    }

    @JsonProperty("tariffDefaultVal")
    public void setTariffDefaultVal(Integer tariffDefaultVal) {
        this.tariffDefaultVal = tariffDefaultVal;
    }

    @JsonProperty("tariffMaxCount")
    public Integer getTariffMaxCount() {
        return tariffMaxCount;
    }

    @JsonProperty("tariffMaxCount")
    public void setTariffMaxCount(Integer tariffMaxCount) {
        this.tariffMaxCount = tariffMaxCount;
    }

    @JsonProperty("tariffExcessRate")
    public Integer getTariffExcessRate() {
        return tariffExcessRate;
    }

    @JsonProperty("tariffExcessRate")
    public void setTariffExcessRate(Integer tariffExcessRate) {
        this.tariffExcessRate = tariffExcessRate;
    }

    @JsonProperty("tariffDefRate")
    public Integer getTariffDefRate() {
        return tariffDefRate;
    }

    @JsonProperty("tariffDefRate")
    public void setTariffDefRate(Integer tariffDefRate) {
        this.tariffDefRate = tariffDefRate;
    }

    @JsonProperty("tariffSPCommission")
    public Integer getTariffSPCommission() {
        return tariffSPCommission;
    }

    @JsonProperty("tariffSPCommission")
    public void setTariffSPCommission(Integer tariffSPCommission) {
        this.tariffSPCommission = tariffSPCommission;
    }

    @JsonProperty("tariffAdsCommission")
    public Integer getTariffAdsCommission() {
        return tariffAdsCommission;
    }

    @JsonProperty("tariffAdsCommission")
    public void setTariffAdsCommission(Integer tariffAdsCommission) {
        this.tariffAdsCommission = tariffAdsCommission;
    }

    @JsonProperty("tariffOpcoCommission")
    public Integer getTariffOpcoCommission() {
        return tariffOpcoCommission;
    }

    @JsonProperty("tariffOpcoCommission")
    public void setTariffOpcoCommission(Integer tariffOpcoCommission) {
        this.tariffOpcoCommission = tariffOpcoCommission;
    }

    @JsonProperty("tariffSurChargeval")
    public Integer getTariffSurChargeval() {
        return tariffSurChargeval;
    }

    @JsonProperty("tariffSurChargeval")
    public void setTariffSurChargeval(Integer tariffSurChargeval) {
        this.tariffSurChargeval = tariffSurChargeval;
    }

    @JsonProperty("tariffSurChargeAds")
    public Integer getTariffSurChargeAds() {
        return tariffSurChargeAds;
    }

    @JsonProperty("tariffSurChargeAds")
    public void setTariffSurChargeAds(Integer tariffSurChargeAds) {
        this.tariffSurChargeAds = tariffSurChargeAds;
    }

    @JsonProperty("tariffSurChargeOpco")
    public Integer getTariffSurChargeOpco() {
        return tariffSurChargeOpco;
    }

    @JsonProperty("tariffSurChargeOpco")
    public void setTariffSurChargeOpco(Integer tariffSurChargeOpco) {
        this.tariffSurChargeOpco = tariffSurChargeOpco;
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