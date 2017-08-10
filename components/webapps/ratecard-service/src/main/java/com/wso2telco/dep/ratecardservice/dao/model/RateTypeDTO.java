package com.wso2telco.dep.ratecardservice.dao.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RateTypeDTO {

	private Integer rateTypeId;
	private String rateTypeCode;
	private String rateTypeDesc;
	private String createdBy;
	private String createdDate;
	private String updatedBy;
	private String updatedDate;

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
