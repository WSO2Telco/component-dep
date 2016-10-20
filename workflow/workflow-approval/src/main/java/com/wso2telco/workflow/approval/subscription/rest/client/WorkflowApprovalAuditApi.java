package com.wso2telco.workflow.approval.subscription.rest.client;

import com.wso2telco.workflow.approval.exception.SubscriptionApprovalWorkflowException;
import com.wso2telco.workflow.approval.model.SubscriptionApprovalAuditRecord;

import feign.Headers;
import feign.RequestLine;

public interface WorkflowApprovalAuditApi {

    @RequestLine("POST workflow/audit/subscription")
    @Headers("Content-Type: application/json")
    void subscriptionApprovalAudit(SubscriptionApprovalAuditRecord subscription) throws SubscriptionApprovalWorkflowException;

}
