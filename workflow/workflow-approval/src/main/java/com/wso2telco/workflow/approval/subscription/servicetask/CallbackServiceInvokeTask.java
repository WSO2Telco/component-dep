package com.wso2telco.workflow.approval.subscription.servicetask;


import com.wso2telco.workflow.approval.exception.SubscriptionApprovalWorkflowException;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowHttpClient;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import com.wso2telco.workflow.approval.util.Constants;

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

        String adminPassword= arg0.getVariable(Constants.ADMIN_PASSWORD).toString();
        AuthRequestInterceptor authRequestInterceptor=new AuthRequestInterceptor();

		String refId = arg0.getVariable(Constants.WORKFLOW_REF_ID).toString();
		String activityName = arg0.getCurrentActivityName();
		String approvalStatus;
		if (activityName.equalsIgnoreCase(Constants.OPERATOR_CALLBACK_ACTIVITY)) {
			approvalStatus = (String) arg0.getVariable(Constants.OPERATOR_ADMIN_APPROVAL);
		} else if (activityName.equalsIgnoreCase(Constants.PUBLISHER_CALLBACK_ACTIVITY)) {
			approvalStatus = (String) arg0.getVariable(Constants.API_PUBLISHER_APPROVAL);
		} else {
			approvalStatus = (String) arg0.getVariable(Constants.HUB_ADMIN_APPROVAL);
		}

		String callbackUrl = (String) arg0.getVariable(Constants.CALL_BACK_URL);

		WorkflowHttpClient client = Feign.builder()
		                                 .encoder(new JacksonEncoder())
		                                 .decoder(new JacksonDecoder())
		                                 .errorDecoder(new WorkflowCallbackErrorDecoder())
		                                 .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminPassword))
		                                 .target(WorkflowHttpClient.class, callbackUrl);

		log.info("Application creation workflow reference Id: " + refId + ", Hub Admin Approval Status: " +
				approvalStatus);

		try {
			client.invokeCallback(refId, approvalStatus);
		} catch (SubscriptionApprovalWorkflowException e) {
			throw new Exception(e);
		}
	}

}

