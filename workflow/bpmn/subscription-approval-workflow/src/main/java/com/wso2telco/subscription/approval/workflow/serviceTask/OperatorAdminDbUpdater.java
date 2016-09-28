package com.wso2telco.subscription.approval.workflow.serviceTask;


import com.wso2telco.application.creation.approval.workflow.model.Subscription;
import com.wso2telco.application.creation.approval.workflow.model.SubscriptionValidation;
import com.wso2telco.subscription.approval.workflow.rest.client.SubscriptionWorkflowApi;
import com.wso2telco.subscription.approval.workflow.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.subscription.approval.workflow.utils.WorkflowConstants;

import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OperatorAdminDbUpdater implements JavaDelegate {

	private static final Log log = LogFactory.getLog(OperatorAdminDbUpdater.class);

	public void execute(DelegateExecution arg0) throws Exception {

		String operatorName = arg0.getVariable(WorkflowConstants.OPERATOR).toString();
		int applicationId=Integer.parseInt(arg0.getVariable("applicationId").toString());
        String serviceUrl = "https://localhost:9443/workflow-service/";
        String apiName=arg0.getVariable("apiName").toString();
        int apiID=Integer.parseInt(arg0.getVariable("apiId").toString());;
        
		String operatorAdminApprovalStatus = arg0.getVariable(WorkflowConstants.OPERATOR_ADMIN_APPROVAL).toString();
		log.info("In OperatorDataUpdater, Operator admin approval status: " + operatorAdminApprovalStatus +
		         " Operator: " + operatorName);
		
        SubscriptionWorkflowApi api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(getBasicAuthRequestInterceptor())
                .target(SubscriptionWorkflowApi.class, serviceUrl);
	
        Subscription subscription=new Subscription();
        subscription.setApiName(apiName);
        subscription.setApplicationID(applicationId);
        subscription.setOpID("1");
        subscription.setStatus(operatorAdminApprovalStatus);
        
        SubscriptionValidation subscriptionValidation=new SubscriptionValidation();
        subscriptionValidation.setApiID(apiID);
        subscriptionValidation.setApplicationID(applicationId);
    
        try {
            api.subscriptionApprovalHub(subscription);
            api.subscriptionApprovalOperator(subscription);
            api.subscriptionApprovalValidator(subscriptionValidation);
        } catch (Exception e) {
           e.printStackTrace();
        }
		
		
		
		
	}
	
	private BasicAuthRequestInterceptor getBasicAuthRequestInterceptor () {
		String username;
		String password;

		// check java system properties first
		username = System.getProperty(WorkflowConstants.WORKFLOW_CALLBACK_USERNAME_PROPERTY);
		// if not found, check environment variables
		if (username == null) {
			username = System.getenv(WorkflowConstants.WORKFLOW_CALLBACK_USERNAME_PROPERTY);
		}
		// if still not found, use 'admin'
		if (username == null) {
			username = "admin";
		}

		// check java system properties first
		password = System.getProperty(WorkflowConstants.WORKFLOW_CALLBACK_PASSWORD_PROPERTY);
		// if not found, check environment variables
		if (password == null) {
			password = System.getenv(WorkflowConstants.WORKFLOW_CALLBACK_PASSWORD_PROPERTY);
		}
		// if still not found, use 'admin'
		if (password == null) {
			password = "admin";
		}

		return new BasicAuthRequestInterceptor(username, password);
	}

}

