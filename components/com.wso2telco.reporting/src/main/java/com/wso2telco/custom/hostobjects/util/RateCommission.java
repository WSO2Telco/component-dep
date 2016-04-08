package com.wso2telco.custom.hostobjects.util;

import java.math.BigDecimal;

public class RateCommission{
	private BigDecimal spCommission;
	private BigDecimal adsCommission;
	private BigDecimal opcoCommission;
	
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