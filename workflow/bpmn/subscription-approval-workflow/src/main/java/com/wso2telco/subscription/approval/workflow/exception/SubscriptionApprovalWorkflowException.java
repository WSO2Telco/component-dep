package com.wso2telco.subscription.approval.workflow.exception;

public class SubscriptionApprovalWorkflowException extends Exception {

	private static final long serialVersionUID = -8281288328030356863L;

	public SubscriptionApprovalWorkflowException(String message) {
		super(message);
	}

	public SubscriptionApprovalWorkflowException(String message, Throwable cause) {
		super(message, cause);
	}

}
