package com.wso2telco.custom.hostobjects.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class CategoryEntity{
	
	private String name;
	private String rate;
	private Map<String, BigDecimal> subCategoryMap;//category Name & Rate
	private List<SubCategory> subCategory;
	private List<UsageTiers> usageTiers;
	
	public List<UsageTiers> getUsageTiers() {
		return usageTiers;
	}
	public void setUsageTiers(List<UsageTiers> usageTiers) {
		this.usageTiers = usageTiers;
	}
	
	public Map<String, BigDecimal> getSubCategoryMap() {
		return subCategoryMap;
	}
	public void setSubCategoryMap(Map<String, BigDecimal> subCategoryMap) {
		this.subCategoryMap = subCategoryMap;
	}
	public List<SubCategory> getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(List<SubCategory> subCategory) {
		this.subCategory = subCategory;
	}
	private RateCommission categoryCommission;
	
	public RateCommission getCategoryCommission() {
		return categoryCommission;
	}
	public void setCategoryCommission(RateCommission categoryCommission) {
		this.categoryCommission = categoryCommission;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}


}