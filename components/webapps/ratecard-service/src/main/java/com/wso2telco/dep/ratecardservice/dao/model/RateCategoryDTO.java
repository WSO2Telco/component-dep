package com.wso2telco.dep.ratecardservice.dao.model;

public class RateCategoryDTO {

	private Integer rateCategoryId;
	private RateDefinitionDTO rateDefinition;
	private CategoryDTO category;
	private CategoryDTO subCategory;
	private TariffDTO tariff;
	
	public Integer getRateCategoryId() {
		return rateCategoryId;
	}
	public void setRateCategoryId(Integer rateCategoryId) {
		this.rateCategoryId = rateCategoryId;
	}
	public RateDefinitionDTO getRateDefinition() {
		return rateDefinition;
	}
	public void setRateDefinition(RateDefinitionDTO rateDefinition) {
		this.rateDefinition = rateDefinition;
	}
	public CategoryDTO getCategory() {
		return category;
	}
	public void setCategory(CategoryDTO category) {
		this.category = category;
	}
	public CategoryDTO getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(CategoryDTO subCategory) {
		this.subCategory = subCategory;
	}
	public TariffDTO getTariff() {
		return tariff;
	}
	public void setTariff(TariffDTO tariff) {
		this.tariff = tariff;
	}
}
