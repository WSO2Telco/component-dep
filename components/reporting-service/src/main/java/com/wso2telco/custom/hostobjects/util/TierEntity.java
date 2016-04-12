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

import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class TierEntity.
 */
public class TierEntity {

	/** The tier rate. */
	private String tierRate;
	
	/** The tiers. */
	private HashMap<String, Integer> tiers;

	/**
	 * Gets the tier rate.
	 *
	 * @return the tier rate
	 */
	public String getTierRate() {
		return tierRate;
	}

	/**
	 * Sets the tier rate.
	 *
	 * @param tierRate the new tier rate
	 */
	public void setTierRate(String tierRate) {
		this.tierRate = tierRate;
	}

	/**
	 * Gets the tiers.
	 *
	 * @return the tiers
	 */
	public HashMap<String, Integer> getTiers() {
		return tiers;
	}

	/**
	 * Sets the tiers.
	 *
	 * @param tiers the tiers
	 */
	public void setTiers(HashMap<String, Integer> tiers) {
		this.tiers = tiers;
	}
}
