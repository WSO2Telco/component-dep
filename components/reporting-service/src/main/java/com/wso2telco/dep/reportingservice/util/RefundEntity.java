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

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class RefundEntity.
 */
public class RefundEntity{
	
	/** The name. */
	private String name;
	
	/** The refund list. */
	private List<String> refundList;
	
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
	 * Gets the refund list.
	 *
	 * @return the refund list
	 */
	public List<String> getRefundList() {
		return refundList;
	}
	
	/**
	 * Sets the refund list.
	 *
	 * @param refundList the new refund list
	 */
	public void setRefundList(List<String> refundList) {
		this.refundList = refundList;
	}		
}
