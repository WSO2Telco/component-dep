package com.wso2telco.workflow.approval.subscription.rest.client;


import com.wso2telco.workflow.approval.exception.SubscriptionApprovalWorkflowException;
import com.wso2telco.workflow.approval.model.Subscription;
import com.wso2telco.workflow.approval.model.SubscriptionValidation;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface SubscriptionWorkflowApi {

    @RequestLine("POST workflow/approval/subscription/hub")
    @Headers("Content-Type: application/json")
    void subscriptionApprovalHub (Subscription subscription) throws SubscriptionApprovalWorkflowException;

    @RequestLine("PUT workflow/approval/subscription/operator")
    @Headers("Content-Type: application/json")
    void subscriptionApprovalOperator (Subscription subscription) throws SubscriptionApprovalWorkflowException;
    
    @RequestLine("POST workflow/approval/subscription/validator")
    @Headers("Content-Type: application/json")
    void subscriptionApprovalValidator (SubscriptionValidation subscriptionValidation) throws SubscriptionApprovalWorkflowException;
    
}
