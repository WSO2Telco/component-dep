package com.wso2telco.workflow.approval.subscription;

import com.wso2telco.workflow.approval.model.DelegatedArgsDTO;

import org.activiti.engine.delegate.DelegateExecution;

import com.wso2telco.workflow.approval.model.SubscriptionApprovalAuditRecord;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowApprovalAuditApi;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import com.wso2telco.workflow.approval.util.Constants;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

abstract class AbstractTaskExecutor implements SubscriptionTask {

    private static final Log log = LogFactory.getLog(AbstractTaskExecutor.class);

    public abstract void performTasks(final DelegatedArgsDTO args) throws Exception;

    public void execute(final DelegateExecution arg0) throws Exception {

        final String adminUserName = arg0.getVariable(Constants.ADMIN_USER_NAME) != null ? arg0.getVariable(Constants.ADMIN_USER_NAME).toString() : null;
        final String adminPassword = arg0.getVariable(Constants.ADMIN_PASSWORD) != null ? arg0.getVariable(Constants.ADMIN_PASSWORD).toString() : null;
        final String serviceUrl = arg0.getVariable(Constants.SERVICE_URL) != null ? arg0.getVariable(Constants.SERVICE_URL).toString() : null;
        final String apiName = arg0.getVariable(Constants.API_NAME) != null ? arg0.getVariable(Constants.API_NAME).toString() : null;
        String operatorName = arg0.getVariable(Constants.OPERATOR) != null ? arg0.getVariable(Constants.OPERATOR).toString() : null;
        int applicationId = arg0.getVariable(Constants.APPLICATION_ID) != null ? Integer.parseInt(arg0.getVariable(Constants.APPLICATION_ID).toString()) : 0;
        int apiID = arg0.getVariable(Constants.API_ID) != null ? Integer.parseInt(arg0.getVariable(Constants.API_ID).toString()) : 0;
        String deploymentType = arg0.getVariable(Constants.DEPLOYMENT_TYPE) != null ? arg0.getVariable(Constants.DEPLOYMENT_TYPE).toString() : null;
        String apiVersion = arg0.getVariable(Constants.API_VERSION) != null ? arg0.getVariable(Constants.API_VERSION).toString() : null;
        String apiProvider = arg0.getVariable(Constants.API_PROVIDER) != null ? arg0.getVariable(Constants.API_PROVIDER).toString() : null;
        String completedByUser = arg0.getVariable(Constants.COMPLETE_BY_USER) != null ? arg0.getVariable(Constants.COMPLETE_BY_USER).toString() : null;
        String completedOn = arg0.getVariable(Constants.COMPLETED_ON) != null ? arg0.getVariable(Constants.COMPLETED_ON).toString() : null;
        String completedByRole = arg0.getVariable(Constants.OPERATOR) != null ? arg0.getVariable(Constants.OPERATOR).toString() + Constants.ADMIN_ROLE : null;
        String applicationName = arg0.getVariable(Constants.APPLICATION_NAME) != null ? arg0.getVariable(Constants.APPLICATION_NAME).toString() : null;
        String description = arg0.getVariable(Constants.APPLICATION_DESCRIPTION) != null ? arg0.getVariable(Constants.APPLICATION_DESCRIPTION).toString() : null;
        String selectedTier = arg0.getVariable(Constants.SELECTED_TIER) != null ? arg0.getVariable(Constants.SELECTED_TIER).toString() : null;
        String apiContext = arg0.getVariable(Constants.API_CONTEXT) != null ? arg0.getVariable(Constants.API_CONTEXT).toString() : null;
        String subscriber = arg0.getVariable(Constants.SUBSCRIBER) != null ? arg0.getVariable(Constants.SUBSCRIBER).toString() : null;
        String operatorAdminApprovalStatus = arg0.getVariable(Constants.OPERATOR_ADMIN_APPROVAL) != null ? arg0.getVariable(Constants.OPERATOR_ADMIN_APPROVAL).toString() : null;
        String apiProviderRole = arg0.getVariable(Constants.API_PROVIDER_ROLE) != null ? arg0.getVariable(Constants.API_PROVIDER_ROLE).toString() : null;
        String adminSelectedTier = arg0.getVariable(Constants.ADMIN_SELECTED_TIER) != null ? arg0.getVariable(Constants.ADMIN_SELECTED_TIER).toString() : null;
        String selectedRate = arg0.getVariable(Constants.SELECTED_RATE) != null ? arg0.getVariable(Constants.SELECTED_RATE).toString() : null;

        log.info("In OperatorDataUpdater, Operator admin approval status: " + operatorAdminApprovalStatus +
                " Operator: " + operatorName);
        AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();

        SubscriptionApprovalAuditRecord subscriptionApprovalAuditRecord = new SubscriptionApprovalAuditRecord();
        subscriptionApprovalAuditRecord.setApiName(apiName);
        subscriptionApprovalAuditRecord.setApiProvider(apiProvider);
        subscriptionApprovalAuditRecord.setApiVersion(apiVersion);
        subscriptionApprovalAuditRecord.setAppId(applicationId);
        subscriptionApprovalAuditRecord.setCompletedByRole(completedByRole);
        subscriptionApprovalAuditRecord.setCompletedByUser(completedByUser);
        subscriptionApprovalAuditRecord.setCompletedOn(completedOn);
        subscriptionApprovalAuditRecord.setSubApprovalType("OPERATOR_ADMIN_APPROVAL");
        subscriptionApprovalAuditRecord.setSubStatus(operatorAdminApprovalStatus);

        DelegatedArgsDTO delegatedArgsDTO = new DelegatedArgsDTO();
        delegatedArgsDTO.setAdminUserName(adminUserName);
        delegatedArgsDTO.setAdminPassword(adminPassword);
        delegatedArgsDTO.setServiceUrl(serviceUrl);
        delegatedArgsDTO.setOperatorName(operatorName);
        delegatedArgsDTO.setApplicationId(applicationId);
        delegatedArgsDTO.setApiID(apiID);
        delegatedArgsDTO.setDeploymentType(deploymentType);
        delegatedArgsDTO.setApiVersion(apiVersion);
        delegatedArgsDTO.setApiProvider(apiProvider);
        delegatedArgsDTO.setCompletedOn(completedOn);
        delegatedArgsDTO.setCompletedByRole(completedByRole);
        delegatedArgsDTO.setApplicationName(applicationName);
        delegatedArgsDTO.setDescription(description);
        delegatedArgsDTO.setSelectedTier(selectedTier);
        delegatedArgsDTO.setApiContext(apiContext);
        delegatedArgsDTO.setSubscriber(subscriber);
        delegatedArgsDTO.setApiName(apiName);
        delegatedArgsDTO.setOperatorAdminApprovalStatus(operatorAdminApprovalStatus);
        delegatedArgsDTO.setApiProviderRole(apiProviderRole);
        delegatedArgsDTO.setAdminSelectedTier(adminSelectedTier);
        delegatedArgsDTO.setSelectedRate(selectedRate);

        WorkflowApprovalAuditApi apiAudit = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName, adminPassword))
                .target(WorkflowApprovalAuditApi.class, serviceUrl);

        apiAudit.subscriptionApprovalAudit(subscriptionApprovalAuditRecord);
        performTasks(delegatedArgsDTO);
    }


}
 
