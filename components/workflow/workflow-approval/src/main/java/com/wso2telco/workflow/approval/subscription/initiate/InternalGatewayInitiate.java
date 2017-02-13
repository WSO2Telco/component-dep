package com.wso2telco.workflow.approval.subscription.initiate;

import com.wso2telco.workflow.approval.model.NotificationRequest;
import com.wso2telco.workflow.approval.subscription.rest.client.NotificationApi;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import com.wso2telco.workflow.approval.util.Constants;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.activiti.engine.delegate.DelegateExecution;

public class InternalGatewayInitiate implements SubscriptionInitiate {
    @Override
    public void execute(DelegateExecution arg0) throws Exception {

        String deploymentType = arg0.getVariable(Constants.DEPLOYMENT_TYPE).toString();
        AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();
        String adminUserName = arg0.getVariable(Constants.ADMIN_USER_NAME) != null ? arg0.getVariable(Constants.ADMIN_USER_NAME).toString() : null;
        String adminPassword= arg0.getVariable(Constants.ADMIN_PASSWORD).toString();
        String serviceUrl = arg0.getVariable(Constants.SERVICE_URL)!=null?arg0.getVariable(Constants.SERVICE_URL).toString():null;
        String apiVersion = arg0.getVariable(Constants.API_VERSION)!=null?arg0.getVariable(Constants.API_VERSION).toString():null;
        String apiProvider = arg0.getVariable(Constants.API_PROVIDER)!=null?arg0.getVariable(Constants.API_PROVIDER).toString():null;
        String completedByRole=(deploymentType.equalsIgnoreCase(Constants.INTERNAL_GATEWAY_TYPE2))?(arg0.getVariable(Constants.API_PROVIDER_ROLE)!=null?arg0.getVariable(Constants.API_PROVIDER_ROLE).toString():null):Constants.WORKFLOW_ADMIN_ROLE;
        String applicationName = arg0.getVariable(Constants.APPLICATION_NAME)!=null?arg0.getVariable(Constants.APPLICATION_NAME).toString():null;
        String apiContext = arg0.getVariable(Constants.API_CONTEXT)!=null?arg0.getVariable(Constants.API_CONTEXT).toString():null;
        String apiTiers = arg0.getVariable(Constants.API_TIERS)!=null?arg0.getVariable(Constants.API_TIERS).toString():null;
        String applicationDescription =arg0.getVariable(Constants.APPLICATION_DESCRIPTION)!=null? arg0.getVariable(Constants.APPLICATION_DESCRIPTION).toString():null;
        String subscriber=arg0.getVariable(Constants.SUBSCRIBER)!=null?arg0.getVariable(Constants.SUBSCRIBER).toString():null;
        String apiName=arg0.getVariable(Constants.API_NAME).toString();

        NotificationApi apiNotification = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName,adminPassword))
                .target(NotificationApi.class, serviceUrl);

        NotificationRequest hUBAdminSubApprovalNotificationRequest = new NotificationRequest();
        hUBAdminSubApprovalNotificationRequest.setApiContext(apiContext);
        hUBAdminSubApprovalNotificationRequest.setApiName(apiName);
        hUBAdminSubApprovalNotificationRequest.setSubscriber(subscriber);
        hUBAdminSubApprovalNotificationRequest.setSubscriptionTier(apiTiers);
        hUBAdminSubApprovalNotificationRequest.setReceiverRole(completedByRole);
        hUBAdminSubApprovalNotificationRequest.setApiVersion(apiVersion);
        hUBAdminSubApprovalNotificationRequest.setApplicationDescription(applicationDescription);
        hUBAdminSubApprovalNotificationRequest.setApiProvider(apiProvider);
        hUBAdminSubApprovalNotificationRequest.setApplicationName(applicationName);

        apiNotification.subscriptionNotificationAdminService(hUBAdminSubApprovalNotificationRequest);

    }
}
