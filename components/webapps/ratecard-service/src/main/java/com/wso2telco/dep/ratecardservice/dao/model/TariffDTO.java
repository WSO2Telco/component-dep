package com.wso2telco.dep.ratecardservice.dao.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TariffDTO {

	private Integer tariffId;
	private String tariffName;
	private String tariffDesc;
	private Double tariffDefaultVal;
	private Integer tariffMaxCount;
	private Double tariffExcessRate;
	private Double tariffDefRate;
	private Double tariffSPCommission;
	private Double tariffAdsCommission;
	private Double tariffOpcoCommission;
	private Double tariffSurChargeval;
	private Double tariffSurChargeAds;
	private Double tariffSurChargeOpco;
	private String createdBy;
	private String createdDate;
	private String updatedBy;
	private String updatedDate;
	
	public Integer getTariffId() {
		return tariffId;
	}
	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}
	public String getTariffName() {
		return tariffName;
	}
	public void setTariffName(String tariffName) {
		this.tariffName = tariffName;
	}
	public String getTariffDesc() {
		return tariffDesc;
	}
	public void setTariffDesc(String tariffDesc) {
		this.tariffDesc = tariffDesc;
	}
	public Double getTariffDefaultVal() {
		return tariffDefaultVal;
	}
	public void setTariffDefaultVal(Double tariffDefaultVal) {
		this.tariffDefaultVal = tariffDefaultVal;
	}
	public Integer getTariffMaxCount() {
		return tariffMaxCount;
	}
	public void setTariffMaxCount(Integer tariffMaxCount) {
		this.tariffMaxCount = tariffMaxCount;
	}
	public Double getTariffExcessRate() {
		return tariffExcessRate;
	}
	public void setTariffExcessRate(Double tariffExcessRate) {
		this.tariffExcessRate = tariffExcessRate;
	}
	public Double getTariffDefRate() {
		return tariffDefRate;
	}
	public void setTariffDefRate(Double tariffDefRate) {
		this.tariffDefRate = tariffDefRate;
	}
	public Double getTariffSPCommission() {
		return tariffSPCommission;
	}
	public void setTariffSPCommission(Double tariffSPCommission) {
		this.tariffSPCommission = tariffSPCommission;
	}
	public Double getTariffAdsCommission() {
		return tariffAdsCommission;
	}
	public void setTariffAdsCommission(Double tariffAdsCommission) {
		this.tariffAdsCommission = tariffAdsCommission;
	}
	public Double getTariffOpcoCommission() {
		return tariffOpcoCommission;
	}
	public void setTariffOpcoCommission(Double tariffOpcoCommission) {
		this.tariffOpcoCommission = tariffOpcoCommission;
	}
	public Double getTariffSurChargeval() {
		return tariffSurChargeval;
	}
	public void setTariffSurChargeval(Double tariffSurChargeval) {
		this.tariffSurChargeval = tariffSurChargeval;
	}
	public Double getTariffSurChargeAds() {
		return tariffSurChargeAds;
	}
	public void setTariffSurChargeAds(Double tariffSurChargeAds) {
		this.tariffSurChargeAds = tariffSurChargeAds;
	}
	public Double getTariffSurChargeOpco() {
		return tariffSurChargeOpco;
	}
	public void setTariffSurChargeOpco(Double tariffSurChargeOpco) {
		this.tariffSurChargeOpco = tariffSurChargeOpco;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
}
