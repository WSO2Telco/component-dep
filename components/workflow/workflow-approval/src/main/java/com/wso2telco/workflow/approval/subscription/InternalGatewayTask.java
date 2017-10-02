package com.wso2telco.workflow.approval.subscription;

import com.wso2telco.workflow.approval.model.DelegatedArgsDTO;
import com.wso2telco.workflow.approval.model.NotificationRequest;
import com.wso2telco.workflow.approval.model.Subscription;
import com.wso2telco.workflow.approval.subscription.rest.client.NotificationApi;
import com.wso2telco.workflow.approval.subscription.rest.client.SubscriptionWorkflowApi;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import com.wso2telco.workflow.approval.util.Constants;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class InternalGatewayTask extends AbstractTaskExecutor {

    @Override
    public void performTasks(DelegatedArgsDTO args) throws Exception {
    	//No logic since notification logic is now handled by NotifyApprovalTask.executeOperatorAdminSubscriptionApproval()
    }
}
