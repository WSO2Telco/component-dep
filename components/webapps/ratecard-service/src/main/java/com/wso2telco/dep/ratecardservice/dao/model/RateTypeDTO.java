package com.wso2telco.dep.ratecardservice.dao.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RateTypeDTO {

	private Integer rateTypeId;
	private String rateTypeCode;
	private String rateTypeDesc;

	public Integer getRateTypeId() {
		return rateTypeId;
	}

	public void setRateTypeId(Integer rateTypeId) {
		this.rateTypeId = rateTypeId;
	}

	public String getRateTypeCode() {
		return rateTypeCode;
	}

	public void setRateTypeCode(String rateTypeCode) {
		this.rateTypeCode = rateTypeCode;
	}

	public String getRateTypeDesc() {
		return rateTypeDesc;
	}

	public void setRateTypeDesc(String rateTypeDesc) {
		this.rateTypeDesc = rateTypeDesc;
	}
}
