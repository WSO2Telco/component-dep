/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.ratecardservice.util;

public enum DatabaseTables {

	TARIFF("tariff"),
	CURRENCY("currency"),
	RATE_TYPE("rate_type"),
	CATEGORY("category"),
	RATE_DEF("rate_def"),
	RATE_CATEGORY("rate_category"),
	API("api"),
	API_OPERATION("api_operation"),
	OPERATOR("operator"),
	OPERATION_RATE("operation_rate"),
	TAX("tax"),
	RATE_TAX("rate_taxes"),
	TAX_VALIDITY("tax_validity"),
	TAX_LEVEL("rate_tax_level");
	
	DatabaseTables(String tObject) {

		this.tObject = tObject;
	}
	
	public String getTObject(){
		
		return this.tObject;
	}

	String tObject;
}
