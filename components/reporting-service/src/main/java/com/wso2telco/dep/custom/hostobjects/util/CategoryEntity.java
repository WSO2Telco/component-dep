/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.custom.hostobjects.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class CategoryEntity.
 */
public class CategoryEntity{
	
	/** The name. */
	private String name;
	
	/** The rate. */
	private String rate;
	
	/** The sub category map. */
	private Map<String, BigDecimal> subCategoryMap;//category Name & Rate
	
	/** The sub category. */
	private List<SubCategory> subCategory;
	
	/** The usage tiers. */
	private List<UsageTiers> usageTiers;
	
	/**
	 * Gets the usage tiers.
	 *
	 * @return the usage tiers
	 */
	public List<UsageTiers> getUsageTiers() {
		return usageTiers;
	}
	
	/**
	 * Sets the usage tiers.
	 *
	 * @param usageTiers the new usage tiers
	 */
	public void setUsageTiers(List<UsageTiers> usageTiers) {
		this.usageTiers = usageTiers;
	}
	
	/**
	 * Gets the sub category map.
	 *
	 * @return the sub category map
	 */
	public Map<String, BigDecimal> getSubCategoryMap() {
		return subCategoryMap;
	}
	
	/**
	 * Sets the sub category map.
	 *
	 * @param subCategoryMap the sub category map
	 */
	public void setSubCategoryMap(Map<String, BigDecimal> subCategoryMap) {
		this.subCategoryMap = subCategoryMap;
	}
	
	/**
	 * Gets the sub category.
	 *
	 * @return the sub category
	 */
	public List<SubCategory> getSubCategory() {
		return subCategory;
	}
	
	/**
	 * Sets the sub category.
	 *
	 * @param subCategory the new sub category
	 */
	public void setSubCategory(List<SubCategory> subCategory) {
		this.subCategory = subCategory;
	}
	
	/** The category commission. */
	private RateCommission categoryCommission;
	
	/**
	 * Gets the category commission.
	 *
	 * @return the category commission
	 */
	public RateCommission getCategoryCommission() {
		return categoryCommission;
	}
	
	/**
	 * Sets the category commission.
	 *
	 * @param categoryCommission the new category commission
	 */
	public void setCategoryCommission(RateCommission categoryCommission) {
		this.categoryCommission = categoryCommission;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the rate.
	 *
	 * @return the rate
	 */
	public String getRate() {
		return rate;
	}
	
	/**
	 * Sets the rate.
	 *
	 * @param rate the new rate
	 */
	public void setRate(String rate) {
		this.rate = rate;
	}


}
