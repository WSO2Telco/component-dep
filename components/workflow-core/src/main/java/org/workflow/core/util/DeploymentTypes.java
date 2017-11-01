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

package org.workflow.core.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum DeploymentTypes {
	HUB("hub", "application_creation_approval_process", "subscription_approval_process"), 
	EXTERNAL_GATEWAY("external_gateway", "application_creation_approval_process","subscription_approval_process"), 
	INTERNAL_GATEWAY("internal_gateway","application_creation_approval_process", "subscription_approval_process");

	private String deploymentType;
	private String appProcess;
	private String subProcess;

	private static Map<String,DeploymentTypes> byNameMap =new HashMap<String, DeploymentTypes>();
	
	static{
		EnumSet<DeploymentTypes> all = EnumSet.allOf( DeploymentTypes.class);
		for (DeploymentTypes deploymentTypes : all) {
			byNameMap.put(deploymentTypes.getDeploymentType(),deploymentTypes);
		}
	}
	
	DeploymentTypes(String deploymentType, String appProcess, String subProcess) {

		this.deploymentType = deploymentType;
		this.appProcess = appProcess;
		this.subProcess = subProcess;
	}

	public String getDeploymentType() {

		return this.deploymentType;
	}
	
	public String getAppProcessType() {

		return this.appProcess;
	}
	public String getSubscriptoinProcessType() {

		return this.subProcess;
	}
	public static DeploymentTypes getByName(String name) {
		return byNameMap.get(name.trim());
	}
}