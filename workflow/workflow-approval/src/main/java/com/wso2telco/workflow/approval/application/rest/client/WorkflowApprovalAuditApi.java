package com.wso2telco.workflow.approval.application.rest.client;

import com.wso2telco.workflow.approval.exception.ApplicationApprovalWorkflowException;
import com.wso2telco.workflow.approval.model.ApplicationApprovalAuditRecord;

import feign.Headers;
import feign.RequestLine;


public interface WorkflowApprovalAuditApi {

    @RequestLine("POST workflow/audit/application")
    @Headers("Content-Type: application/json")
    void applicationApprovalAudit (ApplicationApprovalAuditRecord applicationApprovalAuditRecord) throws ApplicationApprovalWorkflowException;
}
