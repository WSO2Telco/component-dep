package com.wso2telco.subscription.approval.workflow.serviceTask;




import com.wso2telco.subscription.approval.workflow.model.*;
import com.wso2telco.subscription.approval.workflow.rest.client.NotificationApi;
import com.wso2telco.subscription.approval.workflow.rest.client.SubscriptionWorkflowApi;
import com.wso2telco.subscription.approval.workflow.rest.client.WorkflowApprovalAuditApi;
import com.wso2telco.subscription.approval.workflow.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.subscription.approval.workflow.utils.AuthRequestInterceptor;
import com.wso2telco.subscription.approval.workflow.utils.WorkflowConstants;

import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OperatorAdminDbUpdater implements JavaDelegate {

	private static final Log log = LogFactory.getLog(OperatorAdminDbUpdater.class);

	public void execute(DelegateExecution arg0) throws Exception {

        AuthRequestInterceptor authRequestInterceptor=new AuthRequestInterceptor();
    	String operatorName = arg0.getVariable(WorkflowConstants.OPERATOR).toString();
		int applicationId=Integer.parseInt(arg0.getVariable(WorkflowConstants.APPLICATION_ID).toString());
        String serviceUrl =WorkflowConstants.SERVICE_URL;
        String apiName=arg0.getVariable(WorkflowConstants.API_NAME).toString();
        int apiID=Integer.parseInt(arg0.getVariable(WorkflowConstants.API_ID).toString());
        String deploymentType = arg0.getVariable(WorkflowConstants.DEPLOYMENT_TYPE).toString();
        String apiVersion = arg0.getVariable(WorkflowConstants.API_VERSION).toString();
        String apiProvider= arg0.getVariable(WorkflowConstants.API_PROVIDER).toString();
        String completedByUser= arg0.getVariable(WorkflowConstants.COMPLETE_BY_USER).toString();
        String completedOn= arg0.getVariable(WorkflowConstants.COMPLETED_ON).toString();
        String completedByRole=arg0.getVariable(WorkflowConstants.OPERATOR).toString()+WorkflowConstants.ADMIN_ROLE;
        String applicationName= arg0.getVariable(WorkflowConstants.APPLICATION_NAME).toString();
        String description= arg0.getVariable(WorkflowConstants.DESCRIPTION).toString();
        String selectedTier= arg0.getVariable(WorkflowConstants.SELECTED_TIER).toString();

		String operatorAdminApprovalStatus = arg0.getVariable(WorkflowConstants.OPERATOR_ADMIN_APPROVAL).toString();
		log.info("In OperatorDataUpdater, Operator admin approval status: " + operatorAdminApprovalStatus +
		         " Operator: " + operatorName);

		
        SubscriptionWorkflowApi api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor())
                .target(SubscriptionWorkflowApi.class, serviceUrl);

        WorkflowApprovalAuditApi apiAudit = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor())
                .target(WorkflowApprovalAuditApi.class, serviceUrl);


        NotificationApi apiNotification = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor())
                .target(NotificationApi.class, serviceUrl);



        Subscription subscription=new Subscription();
        subscription.setApiName(apiName);
        subscription.setApplicationID(applicationId);
        subscription.setStatus(operatorAdminApprovalStatus);
        subscription.setOperatorName(operatorName);
        
        SubscriptionValidation subscriptionValidation=new SubscriptionValidation();
        subscriptionValidation.setApiID(apiID);
        subscriptionValidation.setApplicationID(applicationId);


        SubscriptionApprovalAuditRecord subscriptionApprovalAuditRecord=new SubscriptionApprovalAuditRecord();
        subscriptionApprovalAuditRecord.setApiName(apiName);
        subscriptionApprovalAuditRecord.setApiProvider(apiProvider);
        subscriptionApprovalAuditRecord.setApiVersion(apiVersion);
        subscriptionApprovalAuditRecord.setAppId(applicationId);
        subscriptionApprovalAuditRecord.setCompletedByRole(completedByRole);
        subscriptionApprovalAuditRecord.setCompletedByUser(completedByUser);
        subscriptionApprovalAuditRecord.setCompletedOn(completedOn);
        subscriptionApprovalAuditRecord.setSubApprovalType("OPERATOR_ADMIN_APPROVAL");
        subscriptionApprovalAuditRecord.setSubStatus(operatorAdminApprovalStatus);
        try {
            if(!deploymentType.equalsIgnoreCase(WorkflowConstants.HUB)) {
                api.subscriptionApprovalHub(subscription);
            }
            api.subscriptionApprovalOperator(subscription);
            api.subscriptionApprovalValidator(subscriptionValidation);
            apiAudit.subscriptionApprovalAudit(subscriptionApprovalAuditRecord);

            //send email notification
            if(operatorAdminApprovalStatus.equalsIgnoreCase(WorkflowConstants.APPROVE)){
                PLUGINAdminSubApprovalNotificationRequest pLUGINAdminSubApprovalNotificationRequest=new PLUGINAdminSubApprovalNotificationRequest();
                pLUGINAdminSubApprovalNotificationRequest.setApiVersion(apiVersion);
                pLUGINAdminSubApprovalNotificationRequest.setApiContext("");
                pLUGINAdminSubApprovalNotificationRequest.setApiName(apiName);
                pLUGINAdminSubApprovalNotificationRequest.setApiProvider(apiProvider);
                pLUGINAdminSubApprovalNotificationRequest.setApiPublisher(apiProvider);
                pLUGINAdminSubApprovalNotificationRequest.setApplicationName(applicationName);
                pLUGINAdminSubApprovalNotificationRequest.setApplicationDescription(description);
                pLUGINAdminSubApprovalNotificationRequest.setSubscriptionTier(selectedTier);

                apiNotification.subscriptionNotificationApiCreator(pLUGINAdminSubApprovalNotificationRequest);

            }
            else{
                SubApprovalStatusSPNotificationRequest subApprovalStatusSPNotificationRequest=new SubApprovalStatusSPNotificationRequest();
                subApprovalStatusSPNotificationRequest.setApprovalStatus(operatorAdminApprovalStatus);
                subApprovalStatusSPNotificationRequest.setApplicationName(applicationName);
                subApprovalStatusSPNotificationRequest.setApiProvider(apiProvider);
                subApprovalStatusSPNotificationRequest.setApiName(apiName);
                subApprovalStatusSPNotificationRequest.setSubscriptionTier(selectedTier);
                subApprovalStatusSPNotificationRequest.setApplicationDescription(description);
                subApprovalStatusSPNotificationRequest.setApiVersion(apiVersion);
                subApprovalStatusSPNotificationRequest.setApiContext("");

                apiNotification.subscriptionNotificationSp(subApprovalStatusSPNotificationRequest);
            }



        } catch (Exception e) {
            throw new Exception(e);
        }
		
		
		
		
	}

}

