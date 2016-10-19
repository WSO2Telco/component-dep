package com.wso2telco.workflow.approval.application.rest.client;


import com.wso2telco.workflow.approval.exception.ApplicationApprovalWorkflowException;
import com.wso2telco.workflow.approval.model.Application;

import feign.Headers;
import feign.RequestLine;

public interface HubWorkflowApi {

    @RequestLine("POST workflow/approval/application/hub")
    @Headers("Content-Type: application/json")
    void applicationApprovalHub (Application application) throws ApplicationApprovalWorkflowException;

    @RequestLine("PUT workflow/approval/application/operator")
    @Headers("Content-Type: application/json")
    void applicationApprovalOperator (Application application) throws ApplicationApprovalWorkflowException;
}
