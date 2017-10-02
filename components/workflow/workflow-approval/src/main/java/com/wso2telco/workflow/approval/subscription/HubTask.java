package com.wso2telco.workflow.approval.subscription;

import com.wso2telco.workflow.approval.exception.HubWorkflowCallbackApiErrorDecoder;
import com.wso2telco.workflow.approval.model.DelegatedArgsDTO;
import com.wso2telco.workflow.approval.model.NotificationRequest;
import com.wso2telco.workflow.approval.model.Subscription;
import com.wso2telco.workflow.approval.model.SubscriptionValidation;
import com.wso2telco.workflow.approval.subscription.rest.client.NotificationApi;
import com.wso2telco.workflow.approval.subscription.rest.client.SubscriptionWorkflowApi;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import com.wso2telco.workflow.approval.util.Constants;

import feign.Feign;
import feign.Feign.Builder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class HubTask extends AbstractTaskExecutor {

    @Override
    public void performTasks(DelegatedArgsDTO delegatedArgsDTO) throws Exception {
    	
    	String adminUserName = delegatedArgsDTO.getAdminUserName() != null ? delegatedArgsDTO.getAdminUserName() : null;
        String adminPassword = delegatedArgsDTO.getAdminPassword() != null ? delegatedArgsDTO.getAdminPassword() : null;
        String serviceUrl = delegatedArgsDTO.getServiceUrl() != null ? delegatedArgsDTO.getServiceUrl() : null;
        int applicationId = Integer.toString(delegatedArgsDTO.getApplicationId()) != null ? delegatedArgsDTO.getApplicationId() : null;
        String apiVersion = delegatedArgsDTO.getApiVersion() !=null? delegatedArgsDTO.getApiVersion() : null;
        String apiProvider = delegatedArgsDTO.getApiProvider() !=null? delegatedArgsDTO.getApiProvider() :null;
        String apiName = delegatedArgsDTO.getApiName() !=null? delegatedArgsDTO.getApiName() :null;
        String selectedRate = delegatedArgsDTO.getSelectedRate() !=null? delegatedArgsDTO.getSelectedRate() :null;
        

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
        subscription.setSelectedRate(selectedRate);

        SubscriptionValidation subscriptionValidation = new SubscriptionValidation();
        subscriptionValidation.setApiID(delegatedArgsDTO.getApiID());
        subscriptionValidation.setApplicationID(delegatedArgsDTO.getApplicationId());

        api.subscriptionApprovalOperator(subscription);
        api.subscriptionApprovalValidator(subscriptionValidation);
     
    }
}
