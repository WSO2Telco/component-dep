package com.wso2telco.workflow.approval.subscription;

import com.wso2telco.workflow.approval.model.DelegatedArgsDTO;
import com.wso2telco.workflow.approval.model.NotificationRequest;
import com.wso2telco.workflow.approval.model.Subscription;
import com.wso2telco.workflow.approval.model.SubscriptionValidation;
import com.wso2telco.workflow.approval.subscription.rest.client.NotificationApi;
import com.wso2telco.workflow.approval.subscription.rest.client.SubscriptionWorkflowApi;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class HubTask extends AbstractTaskExecutor {

    @Override
    public void performTasks(DelegatedArgsDTO delegatedArgsDTO) throws Exception {

        AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();

        SubscriptionWorkflowApi api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(delegatedArgsDTO.getAdminUserName(),delegatedArgsDTO.getAdminPassword()))
                .target(SubscriptionWorkflowApi.class, delegatedArgsDTO.getServiceUrl());


        Subscription subscription = new Subscription();
        subscription.setApiName(delegatedArgsDTO.getApiName());
        subscription.setApplicationID(delegatedArgsDTO.getApplicationId());
        subscription.setStatus(delegatedArgsDTO.getOperatorAdminApprovalStatus());
        subscription.setOperatorName(delegatedArgsDTO.getOperatorName());

        SubscriptionValidation subscriptionValidation = new SubscriptionValidation();
        subscriptionValidation.setApiID(delegatedArgsDTO.getApiID());
        subscriptionValidation.setApplicationID(delegatedArgsDTO.getApplicationId());

        api.subscriptionApprovalOperator(subscription);
        api.subscriptionApprovalValidator(subscriptionValidation);

        NotificationApi apiNotification = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(delegatedArgsDTO.getAdminUserName(), delegatedArgsDTO.getAdminPassword()))
                .target(NotificationApi.class, delegatedArgsDTO.getServiceUrl());


        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setApiContext(delegatedArgsDTO.getApiContext());
        notificationRequest.setApiName(delegatedArgsDTO.getApiName());
        notificationRequest.setSubscriber(delegatedArgsDTO.getSubscriber());
        notificationRequest.setSubscriptionTier(delegatedArgsDTO.getSelectedTier());
        notificationRequest.setReceiverRole(delegatedArgsDTO.getCompletedByRole());
        notificationRequest.setApiVersion(delegatedArgsDTO.getApiVersion());
        notificationRequest.setApplicationDescription(delegatedArgsDTO.getDescription());
        notificationRequest.setApiProvider(delegatedArgsDTO.getApiProvider());
        notificationRequest.setApplicationName(delegatedArgsDTO.getApiContext());

        apiNotification.subscriptionNotificationSp(notificationRequest);

    }
}
