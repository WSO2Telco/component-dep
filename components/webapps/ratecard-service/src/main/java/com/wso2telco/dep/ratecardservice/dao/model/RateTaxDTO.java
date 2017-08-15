package com.wso2telco.dep.ratecardservice.dao.model;

public class RateTaxDTO {

	private Integer rateTaxId;
	private TaxDTO tax;
	private RateDefinitionDTO rateDefinition;
	private String createdBy;
	private String createdDate;
	private String updatedBy;
	private String updatedDate;
	
	public Integer getRateTaxId() {
		return rateTaxId;
	}
	public void setRateTaxId(Integer rateTaxId) {
		this.rateTaxId = rateTaxId;
	}
	public TaxDTO getTax() {
		return tax;
	}
	public void setTax(TaxDTO tax) {
		this.tax = tax;
	}
	public RateDefinitionDTO getRateDefinition() {
		return rateDefinition;
	}
	public void setRateDefinition(RateDefinitionDTO rateDefinition) {
		this.rateDefinition = rateDefinition;
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
