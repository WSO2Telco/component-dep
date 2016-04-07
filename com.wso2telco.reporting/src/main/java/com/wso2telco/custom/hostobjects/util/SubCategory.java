package com.wso2telco.custom.hostobjects.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public class SubCategory{
	private String name;
	private Double rate;
	private RateCommission subCategoryCommission;
	private Map<String, BigDecimal> subCategoryMap;//Sub category Name & Rate
	private List<UsageTiers> usageTiers;
	
	public List<UsageTiers> getUsageTiers() {
		return usageTiers;
	}
	public void setUsageTiers(List<UsageTiers> usageTiers) {
		this.usageTiers = usageTiers;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Map<String, BigDecimal> getSubCategoryMap() {
		return subCategoryMap;
	}

	public void setSubCategoryMap(Map<String, BigDecimal> subCategoryMap) {
		this.subCategoryMap = subCategoryMap;
	}

	public RateCommission getSubCategoryCommission() {
		return subCategoryCommission;
	}

	public void setSubCategoryCommission(RateCommission subCategoryCommission) {
		this.subCategoryCommission = subCategoryCommission;
	}
	
}