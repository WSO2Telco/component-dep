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

package com.wso2telco.dep.reportingservice.util;

// TODO: Auto-generated Javadoc
/**
 * The Enum ReportingTable.
 */
public enum ReportingTable {

	/** The sb api response summary. */
	SB_API_RESPONSE_SUMMARY("SB_API_RESPONSE_SUMMARY"),
	
	/** The nb api response summary. */
	NB_API_RESPONSE_SUMMARY("NB_API_RESPONSE_SUMMARY"),
	
	/** The sb api request summary. */
	SB_API_REQUEST_SUMMARY("SB_API_REQUEST_SUMMARY"),
	
	/** The nb api request summary. */
	NB_API_REQUEST_SUMMARY("NB_API_REQUEST_SUMMARY"),	
	
	/** The am subscription. */
	AM_SUBSCRIPTION("AM_SUBSCRIPTION"),
	
	/** The am workflows. */
	AM_WORKFLOWS("AM_WORKFLOWS"),
	
	/** The am application key mapping. */
	AM_APPLICATION_KEY_MAPPING("AM_APPLICATION_KEY_MAPPING"),
	
	/** The am application. */
	AM_APPLICATION("AM_APPLICATION"),
	
	/** The api operation types. */
	API_OPERATION_TYPES("api_operation_types"),
	
	/** The am api. */
	AM_API("AM_API"),
	
	/** The operators. */
	OPERATORS("operators"),
	
	/** The operatorapps. */
	OPERATORAPPS("operatorapps"),
	
	/** The operatorendpoints. */
	OPERATORENDPOINTS("operatorendpoints"),
	
	/** The endpointapps. */
	ENDPOINTAPPS("endpointapps"),
	
	/** The sub approval operators. */
	SUB_APPROVAL_OPERATORS("sub_approval_operators"),
	
	/** The tax. */ 
	TAX("tax"),
	
	/** The subscription tax. */
	SUBSCRIPTION_TAX("subscription_tax"), 
	
	/** The api request summary. */
	API_REQUEST_SUMMARY("API_REQUEST_SUMMARY"), 
	
	/** The subscription rates. */
	SUBSCRIPTION_RATES("subscription_rates"),
	
	/** The subscription rates nb. */
	SUBSCRIPTION_RATES_NB("subscription_rates_nb"),
	
	/** The test db. */
	TEST_DB("TEST_DB"),
	
	/** The am subscriber. */
	AM_SUBSCRIBER("AM_SUBSCRIBER"),
	
	/** The subscriptioncount. */
	SUBSCRIPTIONCOUNT("SUBSCRIPTIONCOUNT");
	
	/**
	 * Instantiates a new reporting table.
	 *
	 * @param tObject the t object
	 */
	ReportingTable(String tObject) {
		this.tObject = tObject;
	}
	
	/**
	 * Gets the t object.
	 *
	 * @return the t object
	 */
	public String getTObject(){
		return this.tObject;
	}

	/** The t object. */
	String tObject;
}
