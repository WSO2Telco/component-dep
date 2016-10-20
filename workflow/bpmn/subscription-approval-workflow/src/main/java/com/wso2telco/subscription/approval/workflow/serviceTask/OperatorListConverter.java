package com.wso2telco.subscription.approval.workflow.serviceTask;

import com.wso2telco.subscription.approval.workflow.model.HUBAdminSubApprovalNotificationRequest;
import com.wso2telco.subscription.approval.workflow.model.SubApprovalStatusSPNotificationRequest;
import com.wso2telco.subscription.approval.workflow.rest.client.NotificationApi;
import com.wso2telco.subscription.approval.workflow.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.subscription.approval.workflow.utils.AuthRequestInterceptor;
import com.wso2telco.subscription.approval.workflow.utils.WorkflowConstants;

import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collection;

public class OperatorListConverter implements JavaDelegate {

	private static final Log log = LogFactory.getLog(OperatorListConverter.class);

	public void execute(DelegateExecution arg0) throws Exception {

		String[] operatorList = arg0.getVariable("operators").toString().split(",");
        String adminPassword= arg0.getVariable(WorkflowConstants.ADMIN_PASSWORD).toString();
        String apiName=arg0.getVariable(WorkflowConstants.API_NAME).toString();
        String deploymentType = arg0.getVariable(WorkflowConstants.DEPLOYMENT_TYPE).toString();

		Collection<String> operatorNames = new ArrayList<String>();
		for(String operator : operatorList) {
			operatorNames.add(operator.trim());
			// TODO: make debug
			log.info("Operator '" + operator.trim() + "' added to operatorList");
		}
		arg0.setVariable("operatorList", operatorNames);
        if(deploymentType.equalsIgnoreCase(WorkflowConstants.INTERNAL_GATEWAY)) {

            AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();
            String serviceUrl = arg0.getVariable(WorkflowConstants.SERVICE_URL).toString();
            String apiVersion = arg0.getVariable(WorkflowConstants.API_VERSION).toString();
            String apiProvider = arg0.getVariable(WorkflowConstants.API_PROVIDER).toString();
            String completedByRole = operatorList[0] + WorkflowConstants.ADMIN_ROLE;
            String applicationName = arg0.getVariable(WorkflowConstants.APPLICATION_NAME).toString();
            String apiContext = arg0.getVariable(WorkflowConstants.API_CONTEXT).toString();
            String apiTiers = arg0.getVariable("apiTiers").toString();
            String applicationDescription = arg0.getVariable("applicationDescription").toString();

            NotificationApi apiNotification = Feign.builder()
                    .encoder(new JacksonEncoder())
                    .decoder(new JacksonDecoder())
                    .errorDecoder(new WorkflowCallbackErrorDecoder())
                    .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminPassword))
                    .target(NotificationApi.class, serviceUrl);

            HUBAdminSubApprovalNotificationRequest hUBAdminSubApprovalNotificationRequest = new HUBAdminSubApprovalNotificationRequest();
            hUBAdminSubApprovalNotificationRequest.setApiContext(apiContext);
            hUBAdminSubApprovalNotificationRequest.setApiName(apiName);
            hUBAdminSubApprovalNotificationRequest.setSubscriber(applicationName);
            hUBAdminSubApprovalNotificationRequest.setSubscriptionTier(apiTiers);
            hUBAdminSubApprovalNotificationRequest.setReceiverRole(completedByRole);
            hUBAdminSubApprovalNotificationRequest.setApiVersion(apiVersion);
            hUBAdminSubApprovalNotificationRequest.setApplicationDescription(applicationDescription);
            hUBAdminSubApprovalNotificationRequest.setApiProvider(apiProvider);
            hUBAdminSubApprovalNotificationRequest.setApplicationName(applicationName);

            apiNotification.subscriptionNotificationAdminService(hUBAdminSubApprovalNotificationRequest);
        }


	}
}