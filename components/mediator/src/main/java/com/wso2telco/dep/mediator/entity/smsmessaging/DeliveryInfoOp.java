/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.mediator.entity.smsmessaging;

// TODO: Auto-generated Javadoc
/**
 * The Class DeliveryInfoOp.
 */
public class DeliveryInfoOp {
	
	/** The address. */
	private String address;
	
	/** The delivery status. */
	private String deliveryStatus;
	
	/** The operator code. */
	private String operatorCode;
	
	/** The filter criteria. */
	private String filterCriteria;

	/**
	 * Gets the operator code.
	 *
	 * @return the operator code
	 */
	public String getOperatorCode() {
		return operatorCode;
	}

	/**
	 * Sets the operator code.
	 *
	 * @param operatorCode the new operator code
	 */
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	/**
	 * Gets the filter criteria.
	 *
	 * @return the filter criteria
	 */
	public String getFilterCriteria() {
		return filterCriteria;
	}

	/**
	 * Sets the filter criteria.
	 *
	 * @param filterCriteria the new filter criteria
	 */
	public void setFilterCriteria(String filterCriteria) {
		this.filterCriteria = filterCriteria;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the delivery status.
	 *
	 * @return the delivery status
	 */
	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	/**
	 * Sets the delivery status.
	 *
	 * @param deliveryStatus the new delivery status
	 */
	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
}
