package com.wso2telco.workflow.util;

public class WorkFlowHealper {
	private static final String DEPLOYMENT_TYPE_SYSTEM_PARAM = "DEPLOYMENT_TYPE";

	private WorkFlowHealper() {
	}

	public static String getDeploymentType() {

		return System.getProperty(DEPLOYMENT_TYPE_SYSTEM_PARAM, "hub");
	}
}
