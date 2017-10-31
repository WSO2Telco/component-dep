package com.wso2telco.workflow.util;

public class WorkFlowHealper {
	private static final String DEPLOYMENT_TYPE_SYSTEM_PARAM = "DEPLOYMENT_TYPE";

	private static WorkFlowHealper instance;
	private String wrorkflowServiceEndPoint;
	
	public static WorkFlowHealper getInstance() {
		if (instance == null) {
			instance = new WorkFlowHealper();

		}
		return instance;
	}

	private WorkFlowHealper() {
	}

	public static String getDeploymentType() {

		return System.getProperty(DEPLOYMENT_TYPE_SYSTEM_PARAM, "hub");
	}

	public String getWorkflowServiceEndPoint() {
		return wrorkflowServiceEndPoint;
	}

	public void setAppCreationServiceEndPoint(String appCreationServiceEndPoint) {
		this.wrorkflowServiceEndPoint = appCreationServiceEndPoint;
	}

	

}
