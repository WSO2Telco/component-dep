package com.wso2telco.dep.ratecardservice.dao.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RateCardDTO {

	private RateDefinitionDTO rateDefinition;
	private RateCategoryDTO rateCategories[];
	private RateTaxDTO rateTaxes[];
	private String createdBy; 
	
	public RateDefinitionDTO getRateDefinition() {
		return rateDefinition;
	}
	public void setRateDefinition(RateDefinitionDTO rateDefinition) {
		this.rateDefinition = rateDefinition;
	}
	public RateCategoryDTO[] getRateCategories() {
		return rateCategories;
	}
	public void setRateCategories(RateCategoryDTO[] rateCategories) {
		this.rateCategories = rateCategories;
	}
	public RateTaxDTO[] getRateTaxes() {
		return rateTaxes;
	}
	public void setRateTaxes(RateTaxDTO[] rateTaxes) {
		this.rateTaxes = rateTaxes;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
