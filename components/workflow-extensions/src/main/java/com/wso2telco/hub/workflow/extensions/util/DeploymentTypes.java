package com.wso2telco.hub.workflow.extensions.util;

public enum DeploymentTypes
{
	HUB("hub"),
	EXTERNAL_GATEWAY("external_gateway"),
	INTERNAL_GATEWAY("internal_gateway");

	private String deploymentType;

	DeploymentTypes(String deploymentType) {

		this.deploymentType = deploymentType;
	}

	public String getDeploymentType() {

		return this.deploymentType;
	}
}