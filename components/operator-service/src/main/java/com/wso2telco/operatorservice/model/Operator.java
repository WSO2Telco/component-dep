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
package com.wso2telco.operatorservice.model;

// TODO: Auto-generated Javadoc
/**
 * The Class Operator.
 */
public class Operator {
	
	/** The operator id. */
	private int operatorId;
	
	/** The operator name. */
	private String operatorName;
	
	/** The operator description. */
	private String operatorDescription;
	
	/**
	 * Gets the operator id.
	 *
	 * @return the operator id
	 */
	public int getOperatorId() {
		return operatorId;
	}
	
	/**
	 * Sets the operator id.
	 *
	 * @param operatorId the new operator id
	 */
	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}
	
	/**
	 * Gets the operator name.
	 *
	 * @return the operator name
	 */
	public String getOperatorName() {
		return operatorName;
	}
	
	/**
	 * Sets the operator name.
	 *
	 * @param operatorName the new operator name
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	
	/**
	 * Gets the operator description.
	 *
	 * @return the operator description
	 */
	public String getOperatorDescription() {
		return operatorDescription;
	}
	
	/**
	 * Sets the operator description.
	 *
	 * @param operatorDescription the new operator description
	 */
	public void setOperatorDescription(String operatorDescription) {
		this.operatorDescription = operatorDescription;
	}
}

