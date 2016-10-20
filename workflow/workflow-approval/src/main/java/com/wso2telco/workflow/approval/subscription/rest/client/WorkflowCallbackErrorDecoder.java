package com.wso2telco.workflow.approval.subscription.rest.client;


import com.wso2telco.workflow.approval.exception.SubscriptionApprovalWorkflowException;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class WorkflowCallbackErrorDecoder implements ErrorDecoder {

	public Exception decode(String methodKey, Response response) {
		FeignException exception = FeignException.errorStatus(methodKey, response);
		return new SubscriptionApprovalWorkflowException("Exception in Hub workflow callback API", exception);
	}
}
