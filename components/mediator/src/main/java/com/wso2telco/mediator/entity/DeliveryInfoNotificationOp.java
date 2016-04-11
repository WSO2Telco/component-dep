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
package com.wso2telco.mediator.entity;

// TODO: Auto-generated Javadoc
/**
 * The Class DeliveryInfoNotificationOp.
 */
public class DeliveryInfoNotificationOp {

	/** The callback data. */
	private String callbackData;
	
	/** The delivery info. */
	private DeliveryInfoOp deliveryInfo;

	/**
	 * Gets the callback data.
	 *
	 * @return the callback data
	 */
	public String getCallbackData() {
		return callbackData;
	}

	/**
	 * Sets the callback data.
	 *
	 * @param callbackData the new callback data
	 */
	public void setCallbackData(String callbackData) {
		this.callbackData = callbackData;
	}

	/**
	 * Gets the delivery info.
	 *
	 * @return the delivery info
	 */
	public DeliveryInfoOp getDeliveryInfo() {
		return deliveryInfo;
	}

	/**
	 * Sets the delivery info.
	 *
	 * @param deliveryInfo the new delivery info
	 */
	public void setDeliveryInfo(DeliveryInfoOp deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}

}
