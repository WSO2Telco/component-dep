package com.wso2telco.workflow.approval.application;

import com.wso2telco.workflow.approval.exception.HubWorkflowCallbackApiErrorDecoder;
import com.wso2telco.workflow.approval.model.ApplicationApprovalAuditRecord;
import com.wso2telco.workflow.approval.model.DelegatedArgsDTO;
import com.wso2telco.workflow.approval.model.NotificationRequest;
import com.wso2telco.workflow.approval.subscription.rest.client.NotificationApi;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import com.wso2telco.workflow.approval.util.Constants;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class DefaultApplicationTaskExecutor implements ApplicationTask {

    private static final Log log = LogFactory.getLog(DefaultApplicationTaskExecutor.class);

    protected DelegatedArgsDTO constructDelegatedArgsDTO(final DelegateExecution arg0) {

        String operatorName = arg0.getVariable(Constants.OPERATOR) != null ? arg0.getVariable(Constants.OPERATOR).toString() : null;
        int applicationId = arg0.getVariable(Constants.APPLICATION_ID) != null ? Integer.parseInt(arg0.getVariable(Constants.APPLICATION_ID).toString()) : null;
        String serviceUrl = arg0.getVariable(Constants.SERVICE_URL) != null ? arg0.getVariable(Constants.SERVICE_URL).toString() : null;
        String deploymentType = arg0.getVariable(Constants.DEPLOYMENT_TYPE) != null ? arg0.getVariable(Constants.DEPLOYMENT_TYPE).toString() : null;
        String completedByUser = arg0.getVariable(Constants.COMPLETE_BY_USER) != null ? arg0.getVariable(Constants.COMPLETE_BY_USER).toString() : null;
        String completedOn = arg0.getVariable(Constants.COMPLETED_ON) != null ? arg0.getVariable(Constants.COMPLETED_ON).toString() : null;
        String completedByRole = arg0.getVariable(Constants.OPERATOR).toString() + Constants.ADMIN_ROLE;
        String applicationName = arg0.getVariable(Constants.APPLICATION_NAME) != null ? arg0.getVariable(Constants.APPLICATION_NAME).toString() : null;
        String userName = arg0.getVariable(Constants.USER_NAME) != null ? arg0.getVariable(Constants.USER_NAME).toString() : null;
        String adminUserName = arg0.getVariable(Constants.ADMIN_USER_NAME) != null ? arg0.getVariable(Constants.ADMIN_USER_NAME).toString() : null;
        String adminPassword = arg0.getVariable(Constants.ADMIN_PASSWORD) != null ? arg0.getVariable(Constants.ADMIN_PASSWORD).toString() : null;
        String operatorAdminApprovalStatus = arg0.getVariable(Constants.OPERATOR_ADMIN_APPROVAL) != null ? arg0.getVariable(Constants.OPERATOR_ADMIN_APPROVAL).toString() : null;
        String applicationDescription = arg0.getVariable(Constants.DESCRIPTION) != null ? arg0.getVariable(Constants.DESCRIPTION).toString() : null;
        String selectedTier = arg0.getVariable(Constants.TIER) != null ? arg0.getVariable(Constants.TIER).toString() : null;
        String adminSelectedTier = arg0.getVariable(Constants.ADMIN_SELECTED_TIER) != null ? arg0.getVariable(Constants.ADMIN_SELECTED_TIER).toString() : null;

        DelegatedArgsDTO delegatedArgsDTO = new DelegatedArgsDTO();
        delegatedArgsDTO.setAdminUserName(adminUserName);
        delegatedArgsDTO.setAdminPassword(adminPassword);
        delegatedArgsDTO.setServiceUrl(serviceUrl);
        delegatedArgsDTO.setOperatorName(operatorName);
        delegatedArgsDTO.setApplicationId(applicationId);
        delegatedArgsDTO.setDeploymentType(deploymentType);
        delegatedArgsDTO.setCompletedOn(completedOn);
        delegatedArgsDTO.setCompletedByRole(completedByRole);
        delegatedArgsDTO.setApplicationName(applicationName);
        delegatedArgsDTO.setOperatorAdminApprovalStatus(operatorAdminApprovalStatus);
        delegatedArgsDTO.setDescription(applicationDescription);
        delegatedArgsDTO.setSelectedTier(selectedTier);
        delegatedArgsDTO.setAppCreatorName(userName);
        delegatedArgsDTO.setAdminSelectedTier(adminSelectedTier);
        return delegatedArgsDTO;
    }

    public void execute(final DelegatedArgsDTO delegatedArgsDTO) throws Exception {
        AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();

        com.wso2telco.workflow.approval.application.rest.client.WorkflowApprovalAuditApi apiAudit = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new HubWorkflowCallbackApiErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(delegatedArgsDTO.getAdminUserName(), delegatedArgsDTO.getAdminPassword()))
                .target(com.wso2telco.workflow.approval.application.rest.client.WorkflowApprovalAuditApi.class, delegatedArgsDTO.getServiceUrl());

        ApplicationApprovalAuditRecord applicationApprovalAuditRecord = new ApplicationApprovalAuditRecord();
        applicationApprovalAuditRecord.setAppApprovalType("ADMIN");
        applicationApprovalAuditRecord.setAppCreator(delegatedArgsDTO.getCompletedByUser());
        applicationApprovalAuditRecord.setAppName(delegatedArgsDTO.getApplicationName());
        applicationApprovalAuditRecord.setAppStatus(delegatedArgsDTO.getOperatorAdminApprovalStatus());
        applicationApprovalAuditRecord.setCompletedByUser(delegatedArgsDTO.getCompletedByUser());
        applicationApprovalAuditRecord.setCompletedByRole(delegatedArgsDTO.getCompletedByRole());
        applicationApprovalAuditRecord.setCompletedOn(delegatedArgsDTO.getCompletedOn());

        apiAudit.applicationApprovalAudit(applicationApprovalAuditRecord);

    }

    public void execute(final DelegateExecution arg0) throws Exception {
        DelegatedArgsDTO delegatedArgsDTO= constructDelegatedArgsDTO(arg0);
        this.execute(delegatedArgsDTO);
    }

}
 
