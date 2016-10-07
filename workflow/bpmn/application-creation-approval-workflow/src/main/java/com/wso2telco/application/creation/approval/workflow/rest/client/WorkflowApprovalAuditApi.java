package com.wso2telco.application.creation.approval.workflow.rest.client;

import com.wso2telco.application.creation.approval.workflow.exception.HubWorkflowCallbackApiException;
import com.wso2telco.application.creation.approval.workflow.model.ApplicationApprovalAuditRecord;
import feign.Headers;
import feign.RequestLine;


public interface WorkflowApprovalAuditApi {

    @RequestLine("POST workflow/audit/application")
    @Headers("Content-Type: application/json")
    void applicationApprovalAudit (ApplicationApprovalAuditRecord applicationApprovalAuditRecord) throws HubWorkflowCallbackApiException;
}
