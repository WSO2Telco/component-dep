package com.wso2telco.subscription.approval.workflow.rest.client;

import com.wso2telco.subscription.approval.workflow.exception.SubscriptionApprovalWorkflowException;
import com.wso2telco.subscription.approval.workflow.model.HUBAdminSubApprovalNotificationRequest;
import com.wso2telco.subscription.approval.workflow.model.PLUGINAdminSubApprovalNotificationRequest;
import com.wso2telco.subscription.approval.workflow.model.SubApprovalStatusSPNotificationRequest;

import feign.Headers;
import feign.RequestLine;

public interface NotificationApi {

    @RequestLine("POST workflow/notification/subscription/sp")
    @Headers("Content-Type: application/json")
    void subscriptionNotificationSp (SubApprovalStatusSPNotificationRequest subApprovalStatusSPNotificationRequest) throws SubscriptionApprovalWorkflowException;

    @RequestLine("POST workflow/notification/subscription/gateway")
    @Headers("Content-Type: application/json")
    void subscriptionNotificationAdmin(HUBAdminSubApprovalNotificationRequest hUBAdminSubApprovalNotificationRequest) throws SubscriptionApprovalWorkflowException;

    @RequestLine("POST workflow/notification/subscription/gateway")
    @Headers("Content-Type: application/json")
    void subscriptionNotificationApiCreator(PLUGINAdminSubApprovalNotificationRequest pLUGINAdminSubApprovalNotificationRequest) throws SubscriptionApprovalWorkflowException;

}
