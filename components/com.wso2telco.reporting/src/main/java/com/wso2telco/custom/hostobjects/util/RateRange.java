package com.wso2telco.custom.hostobjects.util;

import java.math.BigDecimal;

public class RateRange {
	private BigDecimal from;
	BigDecimal to;
	BigDecimal value;
	
	public BigDecimal getFrom() {
		return from;
	}
	public void setFrom(BigDecimal from) {
		this.from = from;
	}
	public BigDecimal getTo() {
		return to;
	}
	public void setTo(BigDecimal to) {
		this.to = to;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
}