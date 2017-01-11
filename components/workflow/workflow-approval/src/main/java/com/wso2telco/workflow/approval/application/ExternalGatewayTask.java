package com.wso2telco.workflow.approval.application;

import com.wso2telco.workflow.approval.application.rest.client.HubWorkflowApi;
import com.wso2telco.workflow.approval.exception.HubWorkflowCallbackApiErrorDecoder;
import com.wso2telco.workflow.approval.model.Application;
import com.wso2telco.workflow.approval.model.DelegatedArgsDTO;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.activiti.engine.delegate.DelegateExecution;

public class ExternalGatewayTask extends DefaultApplicationTaskExecutor {

    public void execute(final DelegateExecution arg0) throws Exception{
        DelegatedArgsDTO delegatedArgsDTO= constructDelegatedArgsDTO(arg0);
        super.execute(delegatedArgsDTO);
        AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();
        HubWorkflowApi api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new HubWorkflowCallbackApiErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(delegatedArgsDTO.getAdminUserName(),delegatedArgsDTO.getAdminPassword()))
                .target(HubWorkflowApi.class,delegatedArgsDTO.getServiceUrl());

        Application application=new Application();
        application.setApplicationID(delegatedArgsDTO.getApplicationId());
        application.setOperatorName(delegatedArgsDTO.getOperatorName());
        application.setStatus(delegatedArgsDTO.getOperatorAdminApprovalStatus());
        api.applicationApprovalHub(application);
        api.applicationApprovalOperator(application);

    }

}
