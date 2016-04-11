package com.wso2telco.operatorservice.model;

public class Operator {
	
	private int operatorId;
	private String operatorName;
	private String operatorDescription;
	
	public int getOperatorId() {
		return operatorId;
	}
	
	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}
	
	public String getOperatorName() {
		return operatorName;
	}
	
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	
	public String getOperatorDescription() {
		return operatorDescription;
	}
	
	public void setOperatorDescription(String operatorDescription) {
		this.operatorDescription = operatorDescription;
	}
}

