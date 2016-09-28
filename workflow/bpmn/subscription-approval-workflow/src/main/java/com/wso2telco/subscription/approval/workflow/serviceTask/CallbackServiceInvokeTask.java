package com.wso2telco.subscription.approval.workflow.serviceTask;

import com.wso2telco.subscription.approval.workflow.exception.SubscriptionApprovalWorkflowException;
import com.wso2telco.subscription.approval.workflow.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.subscription.approval.workflow.rest.client.WorkflowHttpClient;
import com.wso2telco.subscription.approval.workflow.utils.WorkflowConstants;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CallbackServiceInvokeTask implements JavaDelegate {

	private static final Log log = LogFactory.getLog(CallbackServiceInvokeTask.class);

	public void execute(DelegateExecution arg0) throws Exception {

		String refId = arg0.getVariable(WorkflowConstants.WORKFLOW_REF_ID).toString();
		String activityName = arg0.getCurrentActivityName();
		String ApprovalStatus;
		if (activityName.equalsIgnoreCase(WorkflowConstants.OPERATOR_CALLBACK_ACTIVITY)) {
			ApprovalStatus = (String) arg0.getVariable(WorkflowConstants.OPERATOR_ADMIN_APPROVAL);
		} else {
			ApprovalStatus = (String) arg0.getVariable(WorkflowConstants.HUB_ADMIN_APPROVAL);
		}

		String callbackUrl = (String) arg0.getVariable(WorkflowConstants.CALL_BACK_URL);

		WorkflowHttpClient client = Feign.builder()
		                                 .encoder(new JacksonEncoder())
		                                 .decoder(new JacksonDecoder())
		                                 .errorDecoder(new WorkflowCallbackErrorDecoder())
		                                 .requestInterceptor(getBasicAuthRequestInterceptor())
		                                 .target(WorkflowHttpClient.class, callbackUrl);

		log.info("Application creation workflow reference Id: " + refId + ", Hub Admin Approval Status: " +
		         ApprovalStatus);

		try {
			client.invokeCallback(refId, ApprovalStatus);
		} catch (SubscriptionApprovalWorkflowException e) {
			throw new Exception(e);
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

