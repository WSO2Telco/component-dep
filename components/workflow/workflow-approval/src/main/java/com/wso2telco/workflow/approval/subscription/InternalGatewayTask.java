package com.wso2telco.workflow.approval.subscription;

import com.wso2telco.workflow.approval.model.DelegatedArgsDTO;
import com.wso2telco.workflow.approval.model.NotificationRequest;
import com.wso2telco.workflow.approval.subscription.rest.client.NotificationApi;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import com.wso2telco.workflow.approval.util.Constants;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class InternalGatewayTask extends AbstractTaskExecutor {

    @Override
    public void performTasks(DelegatedArgsDTO args) throws Exception {

        AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();

        NotificationApi apiNotification = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(args.getAdminUserName(), args.getAdminPassword()))
                .target(NotificationApi.class, args.getServiceUrl());

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setApiVersion(args.getApiVersion());
        notificationRequest.setApiContext(args.getApiContext());
        notificationRequest.setApiName(args.getApiName());
        notificationRequest.setApiProvider(args.getApiProvider());
        notificationRequest.setSubscriber(args.getSubscriber());
        notificationRequest.setApiPublisher(args.getApiPublisher());
        notificationRequest.setApplicationName(args.getApplicationName());
        notificationRequest.setApplicationDescription(args.getDescription());
        notificationRequest.setSubscriptionTier(args.getSelectedTier());

        if (args.getOperatorAdminApprovalStatus().equalsIgnoreCase(Constants.APPROVE)) {
          apiNotification.subscriptionNotificationApiCreator(notificationRequest);
        } else {
          apiNotification.subscriptionNotificationSp(notificationRequest);
        }


    }
}
