package com.wso2telco.subscription.approval.workflow.serviceTask;

import com.wso2telco.subscription.approval.workflow.model.HUBAdminSubApprovalNotificationRequest;
import com.wso2telco.subscription.approval.workflow.rest.client.NotificationApi;
import com.wso2telco.subscription.approval.workflow.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.subscription.approval.workflow.utils.AuthRequestInterceptor;
import com.wso2telco.subscription.approval.workflow.utils.WorkflowConstants;
import feign.Feign;
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
		Collection<String> operatorNames = new ArrayList<String>();
		for(String operator : operatorList) {
			operatorNames.add(operator.trim());
			// TODO: make debug
			log.info("Operator '" + operator.trim() + "' added to operatorList");
		}
		arg0.setVariable("operatorList", operatorNames);

        AuthRequestInterceptor authRequestInterceptor=new AuthRequestInterceptor();
        String serviceUrl = WorkflowConstants.SERVICE_URL;
        String operatorName = arg0.getVariable(WorkflowConstants.OPERATOR).toString();
        int applicationId=Integer.parseInt(arg0.getVariable(WorkflowConstants.APPLICATION_ID).toString());
        String apiName=arg0.getVariable(WorkflowConstants.API_NAME).toString();
        int apiID=Integer.parseInt(arg0.getVariable(WorkflowConstants.API_ID).toString());
        String deploymentType = arg0.getVariable(WorkflowConstants.DEPLOYMENT_TYPE).toString();
        String apiVersion = arg0.getVariable(WorkflowConstants.API_VERSION).toString();
        String apiProvider= arg0.getVariable(WorkflowConstants.API_PROVIDER).toString();
        String completedByUser= arg0.getVariable(WorkflowConstants.COMPLETE_BY_USER).toString();
        String completedOn= arg0.getVariable(WorkflowConstants.COMPLETED_ON).toString();
        String completedByRole=arg0.getVariable(WorkflowConstants.OPERATOR).toString()+WorkflowConstants.ADMIN_ROLE;
        String applicationName= arg0.getVariable(WorkflowConstants.APPLICATION_NAME).toString();

        NotificationApi apiNotification = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor())
                .target(NotificationApi.class, serviceUrl);

        HUBAdminSubApprovalNotificationRequest hUBAdminSubApprovalNotificationRequest=new HUBAdminSubApprovalNotificationRequest();
        hUBAdminSubApprovalNotificationRequest.setApiContext("");
        hUBAdminSubApprovalNotificationRequest.setReceiverRole(completedByRole);
        hUBAdminSubApprovalNotificationRequest.setApiVersion(apiVersion);
        hUBAdminSubApprovalNotificationRequest.setApplicationDescription("");
        hUBAdminSubApprovalNotificationRequest.setApiProvider(apiProvider);
        hUBAdminSubApprovalNotificationRequest.setApplicationName(applicationName);

        apiNotification.subscriptionNotificationAdmin(hUBAdminSubApprovalNotificationRequest);

	}
}