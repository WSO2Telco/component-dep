/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.ratecardservice.dao.model;

public class RateCard {

	private Integer rateDefId;
	private String rateDefName;
	private String rateDefDescription;
	private Integer rateDefDefault;
	private CurrencyDTO currency;
	private RateTypeDTO rateType;
	private Integer rateDefCategoryBase;
	private TariffDTO tariff;
	private RateCategory[] rateCategories;
	private RateTax[] rateTaxes;
	private String createdBy;

	public Integer getRateDefId() {
		return rateDefId;
	}

	public void setRateDefId(Integer rateDefId) {
		this.rateDefId = rateDefId;
	}

	public String getRateDefName() {
		return rateDefName;
	}

	public void setRateDefName(String rateDefName) {
		this.rateDefName = rateDefName;
	}

	public String getRateDefDescription() {
		return rateDefDescription;
	}

	public void setRateDefDescription(String rateDefDescription) {
		this.rateDefDescription = rateDefDescription;
	}

	public Integer getRateDefDefault() {
		return rateDefDefault;
	}

	public void setRateDefDefault(Integer rateDefDefault) {
		this.rateDefDefault = rateDefDefault;
	}

	public CurrencyDTO getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyDTO currency) {
		this.currency = currency;
	}

	public RateTypeDTO getRateType() {
		return rateType;
	}

	public void setRateType(RateTypeDTO rateType) {
		this.rateType = rateType;
	}

	public Integer getRateDefCategoryBase() {
		return rateDefCategoryBase;
	}

	public void setRateDefCategoryBase(Integer rateDefCategoryBase) {
		this.rateDefCategoryBase = rateDefCategoryBase;
	}

	public TariffDTO getTariff() {
		return tariff;
	}

	public void setTariff(TariffDTO tariff) {
		this.tariff = tariff;
	}

	public RateCategory[] getRateCategories() {
		return rateCategories;
	}

	public void setRateCategories(RateCategory[] rateCategories) {
		this.rateCategories = rateCategories;
	}

	public RateTax[] getRateTaxes() {
		return rateTaxes;
	}

	public void setRateTaxes(RateTax[] rateTaxes) {
		this.rateTaxes = rateTaxes;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public static class RateCategory {

		private Integer rateCategoryId;
		private CategoryDTO category;
		private CategoryDTO subCategory;
		private TariffDTO tariff;
		private String createdBy;

		public Integer getRateCategoryId() {
			return rateCategoryId;
		}

		public void setRateCategoryId(Integer rateCategoryId) {
			this.rateCategoryId = rateCategoryId;
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
	}

	public static class RateTax {

		private Integer rateTaxId;
		private TaxDTO tax;
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

		public String getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}
	}
}
