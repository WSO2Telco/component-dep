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
package com.wso2telco.custom.hostobjects.util;

// TODO: Auto-generated Javadoc
/**
 * The Class UsageTiers.
 */
public class UsageTiers{
	
	/** The rate id. */
	private String rateId;
	
	/** The min. */
	private String min;
	
	/** The max. */
	private String max;
        
        /** The tier name. */
        private String tierName;
	
	/**
	 * Gets the rate id.
	 *
	 * @return the rate id
	 */
	public String getRateId() {
		return rateId;
	}
	
	/**
	 * Sets the rate id.
	 *
	 * @param rateId the new rate id
	 */
	public void setRateId(String rateId) {
		this.rateId = rateId;
	}
	
	/**
	 * Gets the min.
	 *
	 * @return the min
	 */
	public String getMin() {
		return min;
	}
	
	/**
	 * Sets the min.
	 *
	 * @param min the new min
	 */
	public void setMin(String min) {
		this.min = min;
	}
	
	/**
	 * Gets the max.
	 *
	 * @return the max
	 */
	public String getMax() {
		return max;
	}
	
	/**
	 * Sets the max.
	 *
	 * @param max the new max
	 */
	public void setMax(String max) {
		this.max = max;
	}

    /**
     * Gets the tier name.
     *
     * @return the tier name
     */
    public String getTierName() {
        return tierName;
    }

    /**
     * Sets the tier name.
     *
     * @param tierName the new tier name
     */
    public void setTierName(String tierName) {
        this.tierName = tierName;
    }       
}
