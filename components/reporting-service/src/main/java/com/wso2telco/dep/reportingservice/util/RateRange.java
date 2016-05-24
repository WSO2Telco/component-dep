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
package com.wso2telco.dep.reportingservice.util;

import java.math.BigDecimal;

// TODO: Auto-generated Javadoc
/**
 * The Class RateRange.
 */
public class RateRange {
	
	/** The from. */
	private BigDecimal from;
	
	/** The to. */
	BigDecimal to;
	
	/** The value. */
	BigDecimal value;
	
	/**
	 * Gets the from.
	 *
	 * @return the from
	 */
	public BigDecimal getFrom() {
		return from;
	}
	
	/**
	 * Sets the from.
	 *
	 * @param from the new from
	 */
	public void setFrom(BigDecimal from) {
		this.from = from;
	}
	
	/**
	 * Gets the to.
	 *
	 * @return the to
	 */
	public BigDecimal getTo() {
		return to;
	}
	
	/**
	 * Sets the to.
	 *
	 * @param to the new to
	 */
	public void setTo(BigDecimal to) {
		this.to = to;
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
}
