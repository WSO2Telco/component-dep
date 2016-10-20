package com.wso2telco.workflow.approval.exception;

public class ApplicationApprovalWorkflowException extends Exception {

	private static final long serialVersionUID = -8281288328030356863L;

	public ApplicationApprovalWorkflowException(String message) {
		super(message);
	}

	public ApplicationApprovalWorkflowException(String message, Throwable cause) {
		super(message, cause);
	}

}
