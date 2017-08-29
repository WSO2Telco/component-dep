package com.wso2telco.dep.ratecardservice.dao.model;

public class RateTaxDTO {

	private Integer rateTaxId;
	private TaxDTO tax;
	private RateDefinitionDTO rateDefinition;
	private String createdBy;
	
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
}
