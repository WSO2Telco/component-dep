package com.wso2telco.dep.ratecardservice.dao.model;

public class RateCategoryDTO {

	private Integer rateCategoryId;
	private RateDefinitionDTO rateDefinition;
	private CategoryDTO category;
	private CategoryDTO subCategory;
	private TariffDTO tariff;
	private String createdBy;
	private String createdDate;
	private String updatedBy;
	private String updatedDate;
	
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
