/**
 * Copyright (c) 2015, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.dep.operatorservice.model;

import java.io.Serializable;
import java.util.Arrays;

public class WhiteListDTO extends AbstractWBDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -629976919811789083L;

	private String subscriptionID = null;
	private String applicationID = null;

	public String getSubscriptionID() {
		return subscriptionID;
	}


	public void setSubscriptionID(final String subscriptionID) {
		if (subscriptionID != null && subscriptionID.trim().length() > 0) {
			this.subscriptionID = subscriptionID.trim();
		}
	}

	/*public boolean hasValidSubscriptionID() {
		return isValidSubscriptionID_;
	}

	private boolean hasValidAppid_;*/

	public String getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(String applicationID) {
		if (applicationID != null && applicationID.trim().length() > 0) {
			this.applicationID = applicationID;
		}
	}


	@Override
	public String toString() {
		return "WhiteListDTO [subscriptionID=" + subscriptionID + ", applicationID=" + applicationID
				+ ", getUserMSISDN()=" + Arrays.toString(getUserMSISDN()) + ", getApiID()=" + getApiID() + "]";
	}

}
