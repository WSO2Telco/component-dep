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
 * The Class ChargeRate.
 */
public class ChargeRate {

    /** The name. */
    private String name;
    
    /** The currency. */
    private String currency;
    
    /** The value. */
    private BigDecimal value;
    
    /** The type. */
    private RateType type;
    
    /** The categories. */
    private Map<String, Object> categories;
    
    /** The tax list. */
    private List<String> taxList;
    
    /** The is default. */
    private boolean isDefault = false;
    
    /** The rate attributes. */
    private Map<String, String> rateAttributes;
    
    /** The rate ranges. */
    private List<RateRange> rateRanges;
    
    /** The commission. */
    private RateCommission commission;
    
    /** The category based val. */
    private Boolean categoryBasedVal = false;
    
    /** The usage tiers. */
    private List<UsageTiers> usageTiers;
    
    /** The Refund list. */
    private RefundEntity RefundList;
    
    /** The surcharge entity. */
    private SurchargeEntity surchargeEntity;
    
    /** The category entity list. */
    private List<CategoryEntity> categoryEntityList;
    
    
    /**
     * Gets the surcharge entity.
     *
     * @return the surcharge entity
     */
    public SurchargeEntity getSurchargeEntity() {
		return surchargeEntity;
	}

	/**
	 * Sets the surcharge entity.
	 *
	 * @param surchargeEntity the new surcharge entity
	 */
	public void setSurchargeEntity(SurchargeEntity surchargeEntity) {
		this.surchargeEntity = surchargeEntity;
	}

	/**
	 * Gets the category based val.
	 *
	 * @return the category based val
	 */
	public Boolean getCategoryBasedVal() {
		return categoryBasedVal;
	}

	/**
	 * Sets the category based val.
	 *
	 * @param categoryBasedVal the new category based val
	 */
	public void setCategoryBasedVal(Boolean categoryBasedVal) {
		this.categoryBasedVal = categoryBasedVal;
	}

	/**
	 * Gets the usage tiers.
	 *
	 * @return the usage tiers
	 */
	public List<UsageTiers> getUsageTiers() {
		return usageTiers;
	}

	/**
	 * Gets the commission.
	 *
	 * @return the commission
	 */
	public RateCommission getCommission() {
		return commission;
	}

	/**
	 * Sets the commission.
	 *
	 * @param commission the new commission
	 */
	public void setCommission(RateCommission commission) {
		this.commission = commission;
	}



	/**
	 * Gets the refund list.
	 *
	 * @return the refund list
	 */
	public RefundEntity getRefundList() {
		return RefundList;
	}

	/**
	 * Sets the refund list.
	 *
	 * @param refundList the new refund list
	 */
	public void setRefundList(RefundEntity refundList) {
		RefundList = refundList;
	}

	/**
	 * Sets the usage tiers.
	 *
	 * @param tiersEntities the new usage tiers
	 */
	public void setUsageTiers(List<UsageTiers> tiersEntities) {
		this.usageTiers = tiersEntities;
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
     * Instantiates a new charge rate.
     *
     * @param name the name
     */
    public ChargeRate(String name) {
        this.name = name;
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
     * Gets the currency.
     *
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency.
     *
     * @param currency the new currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public RateType getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public void setType(RateType type) {
        this.type = type;
    }

    /**
     * Checks if is default.
     *
     * @return true, if is default
     */
    public boolean isDefault() {
        return isDefault;
    }

    /**
     * Sets the default.
     *
     * @param isDefault the new default
     */
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * Gets the rate attributes.
     *
     * @return the rate attributes
     */
    public Map<String, String> getRateAttributes() {
        return rateAttributes;
    }

    /**
     * Sets the rate attributes.
     *
     * @param rateAttributes the rate attributes
     */
    public void setRateAttributes(Map<String, String> rateAttributes) {
        this.rateAttributes = rateAttributes;
    }

    /**
     * Gets the rate ranges.
     *
     * @return the rate ranges
     */
    public List<RateRange> getRateRanges() {
		return rateRanges;
	}

	/**
	 * Sets the rate ranges.
	 *
	 * @param rateRanges the new rate ranges
	 */
	public void setRateRanges(List<RateRange> rateRanges) {
		this.rateRanges = rateRanges;
	}

	/**
	 * Gets the tax list.
	 *
	 * @return the tax list
	 */
	public List<String> getTaxList() { return taxList; }

    /**
     * Sets the tax list.
     *
     * @param taxList the new tax list
     */
    public void setTaxList(List<String> taxList) { this.taxList = taxList; }

    /**
     * Gets the categories.
     *
     * @return the categories
     */
    public Map<String, Object> getCategories() {
        return categories;
    }

    /**
     * Sets the categories.
     *
     * @param categoryEntityMap the category entity map
     */
    public void setCategories(Map<String, Object> categoryEntityMap) {
        this.categories = categoryEntityMap;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ChargeRate{" +
                "name='" + name + '\'' +
                ", currency='" + currency + '\'' +
                ", value=" + value +
                ", type=" + type +
                ", isDefault=" + isDefault +
                ", rateAttributes=" + rateAttributes +
                ", taxList=" + taxList +
                '}';
    }

	/**
	 * Gets the category entity list.
	 *
	 * @return the category entity list
	 */
	public List<CategoryEntity> getCategoryEntityList() {
		return categoryEntityList;
	}

	/**
	 * Sets the category entity list.
	 *
	 * @param categoryEntityList the new category entity list
	 */
	public void setCategoryEntityList(List<CategoryEntity> categoryEntityList) {
		this.categoryEntityList = categoryEntityList;
	}


}
