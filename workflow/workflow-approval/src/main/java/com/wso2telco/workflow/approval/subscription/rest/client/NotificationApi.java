package com.wso2telco.workflow.approval.subscription.rest.client;



import com.wso2telco.workflow.approval.exception.SubscriptionApprovalWorkflowException;
import com.wso2telco.workflow.approval.model.HUBAdminSubApprovalNotificationRequest;
import com.wso2telco.workflow.approval.model.PLUGINAdminSubApprovalNotificationRequest;
import com.wso2telco.workflow.approval.model.SubApprovalStatusSPNotificationRequest;

import feign.Headers;
import feign.RequestLine;

public interface NotificationApi {

    @RequestLine("POST workflow/notification/subscription/sp")
    @Headers("Content-Type: application/json")
    void subscriptionNotificationSp (SubApprovalStatusSPNotificationRequest subApprovalStatusSPNotificationRequest) throws SubscriptionApprovalWorkflowException;

    @RequestLine("POST workflow/notification/subscription/gateway")
    @Headers("Content-Type: application/json")
    void subscriptionNotificationAdmin (HUBAdminSubApprovalNotificationRequest hUBAdminSubApprovalNotificationRequest) throws SubscriptionApprovalWorkflowException;

    @RequestLine("POST workflow/notification/subscription/publisher")
    @Headers("Content-Type: application/json")
    void subscriptionNotificationApiCreator (PLUGINAdminSubApprovalNotificationRequest pLUGINAdminSubApprovalNotificationRequest) throws SubscriptionApprovalWorkflowException;

    @RequestLine("POST workflow/notification/subscription/gateway")
    @Headers("Content-Type: application/json")
    void subscriptionNotificationAdminService(HUBAdminSubApprovalNotificationRequest hUBAdminSubApprovalNotificationRequest) throws SubscriptionApprovalWorkflowException;

}
