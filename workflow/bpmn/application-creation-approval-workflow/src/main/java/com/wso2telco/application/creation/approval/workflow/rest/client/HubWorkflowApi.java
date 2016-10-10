package com.wso2telco.application.creation.approval.workflow.rest.client;


import com.wso2telco.application.creation.approval.workflow.exception.HubWorkflowCallbackApiException;
import com.wso2telco.application.creation.approval.workflow.model.Application;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface HubWorkflowApi {

    @RequestLine("POST workflow/approval/application/hub")
    @Headers("Content-Type: application/json")
    void applicationApprovalHub (Application application) throws HubWorkflowCallbackApiException;

    @RequestLine("PUT workflow/approval/application/operator")
    @Headers("Content-Type: application/json")
    void applicationApprovalOperator (Application application) throws HubWorkflowCallbackApiException;
}
