package com.wso2telco.custom.hostobjects.util;

import java.math.BigDecimal;

public class CommissionPercentagesDTO {

	private Integer id;
	private String spId;
	private String merchantCode;
	private Integer appId;
	private BigDecimal spCommission;
	private BigDecimal adsCommission;
	private BigDecimal opcoCommission;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSpId() {
		return spId;
	}

	public void setSpId(String spId) {
		this.spId = spId;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public BigDecimal getSpCommission() {
		return spCommission;
	}

	public void setSpCommission(BigDecimal spCommission) {
		this.spCommission = spCommission;
	}

	public BigDecimal getAdsCommission() {
		return adsCommission;
	}

	public void setAdsCommission(BigDecimal adsCommission) {
		this.adsCommission = adsCommission;
	}

	public BigDecimal getOpcoCommission() {
		return opcoCommission;
	}

	public void setOpcoCommission(BigDecimal opcoCommission) {
		this.opcoCommission = opcoCommission;
	}

}
