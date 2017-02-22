/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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
import java.util.ArrayList;
import java.util.List;

import com.wso2telco.core.msisdnvalidator.MSISDN;

public class MSISDNSearchDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7476682043789702438L;
	/**
	 * 
	 */
	private String apiID = null;


	private List<MSISDN> msisdntoSearch;{
		msisdntoSearch = new ArrayList<MSISDN>();
	}

	public String getApiID() {
		return apiID;
	}

	public void setApiID(String apiID) {
		this.apiID = apiID;
	}

	public void addMSISDN2Search(MSISDN msisdn) {
		this.msisdntoSearch.add(msisdn);

	}
}
