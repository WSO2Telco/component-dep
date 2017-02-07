package com.wso2telco.workflow.approval.subscription;

import com.wso2telco.workflow.approval.model.DelegatedArgsDTO;
import com.wso2telco.workflow.approval.model.Subscription;
import com.wso2telco.workflow.approval.model.SubscriptionValidation;
import com.wso2telco.workflow.approval.subscription.rest.client.SubscriptionWorkflowApi;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;


class ExternalGatewayTask extends AbstractTaskExecutor{

	@Override
	public void performTasks(DelegatedArgsDTO args) throws Exception {

        AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();
        SubscriptionWorkflowApi api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(args.getAdminUserName(), args.getAdminPassword()))
                .target(SubscriptionWorkflowApi.class, args.getServiceUrl());

        Subscription subscription = new Subscription();
        subscription.setApiName(args.getApiName());
        subscription.setApplicationID(args.getApplicationId());
        subscription.setOperatorName(args.getOperatorName());
        subscription.setStatus(args.getOperatorAdminApprovalStatus());
        api.subscriptionApprovalHub(subscription);
        api.subscriptionApprovalOperator(subscription);

        SubscriptionValidation subscriptionValidation = new SubscriptionValidation();
        subscriptionValidation.setApiID(args.getApiID());
        subscriptionValidation.setApplicationID(args.getApplicationId());
        api.subscriptionApprovalValidator(subscriptionValidation);

    }
	
}
