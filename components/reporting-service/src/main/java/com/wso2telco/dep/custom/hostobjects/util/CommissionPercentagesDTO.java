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

// TODO: Auto-generated Javadoc
/**
 * The Class CommissionPercentagesDTO.
 */
public class CommissionPercentagesDTO {

	/** The id. */
	private Integer id;
	
	/** The sp id. */
	private String spId;
	
	/** The merchant code. */
	private String merchantCode;
	
	/** The app id. */
	private Integer appId;
	
	/** The sp commission. */
	private BigDecimal spCommission;
	
	/** The ads commission. */
	private BigDecimal adsCommission;
	
	/** The opco commission. */
	private BigDecimal opcoCommission;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the sp id.
	 *
	 * @return the sp id
	 */
	public String getSpId() {
		return spId;
	}

	/**
	 * Sets the sp id.
	 *
	 * @param spId the new sp id
	 */
	public void setSpId(String spId) {
		this.spId = spId;
	}

	/**
	 * Gets the merchant code.
	 *
	 * @return the merchant code
	 */
	public String getMerchantCode() {
		return merchantCode;
	}

	/**
	 * Sets the merchant code.
	 *
	 * @param merchantCode the new merchant code
	 */
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	/**
	 * Gets the app id.
	 *
	 * @return the app id
	 */
	public Integer getAppId() {
		return appId;
	}

	/**
	 * Sets the app id.
	 *
	 * @param appId the new app id
	 */
	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	/**
	 * Gets the sp commission.
	 *
	 * @return the sp commission
	 */
	public BigDecimal getSpCommission() {
		return spCommission;
	}

	/**
	 * Sets the sp commission.
	 *
	 * @param spCommission the new sp commission
	 */
	public void setSpCommission(BigDecimal spCommission) {
		this.spCommission = spCommission;
	}

	/**
	 * Gets the ads commission.
	 *
	 * @return the ads commission
	 */
	public BigDecimal getAdsCommission() {
		return adsCommission;
	}

	/**
	 * Sets the ads commission.
	 *
	 * @param adsCommission the new ads commission
	 */
	public void setAdsCommission(BigDecimal adsCommission) {
		this.adsCommission = adsCommission;
	}

	/**
	 * Gets the opco commission.
	 *
	 * @return the opco commission
	 */
	public BigDecimal getOpcoCommission() {
		return opcoCommission;
	}

	/**
	 * Sets the opco commission.
	 *
	 * @param opcoCommission the new opco commission
	 */
	public void setOpcoCommission(BigDecimal opcoCommission) {
		this.opcoCommission = opcoCommission;
	}

}
