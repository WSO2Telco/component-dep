package com.wso2telco.subscription.approval.workflow.rest.client;


import com.wso2telco.subscription.approval.workflow.exception.SubscriptionApprovalWorkflowException;
import com.wso2telco.subscription.approval.workflow.model.SubscriptionApprovalAuditRecord;
import feign.Headers;
import feign.RequestLine;

public interface WorkflowApprovalAuditApi {

    @RequestLine("POST workflow/audit/subscription")
    @Headers("Content-Type: application/json")
    void subscriptionApprovalAudit(SubscriptionApprovalAuditRecord subscription) throws SubscriptionApprovalWorkflowException;

}
