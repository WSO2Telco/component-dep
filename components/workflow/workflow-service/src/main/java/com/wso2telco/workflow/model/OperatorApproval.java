package com.wso2telco.workflow.model;

public class OperatorApproval {
	
	private String operatorName;
	private String approvalStatus;

    public OperatorApproval(String operatorName, String approvalStatus) {
        this.operatorName = operatorName;
        this.approvalStatus = approvalStatus;
    }

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

    
    
    
    
}
