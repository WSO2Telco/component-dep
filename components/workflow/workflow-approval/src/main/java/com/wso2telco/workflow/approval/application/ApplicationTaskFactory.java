package com.wso2telco.workflow.approval.application;

import com.wso2telco.workflow.approval.util.Constants;

public class ApplicationTaskFactory {

    public ApplicationTask getInstance(String deploymentTypes) {
        ApplicationTask subscriptionTask = null;
        if (deploymentTypes.equals(Constants.HUB)) {
            subscriptionTask = new HubTask();
        }else if (deploymentTypes.equals(Constants.EXTERNAL_GATEWAY)) {
            subscriptionTask = new ExternalGatewayTask();
        } else {
            subscriptionTask =new DefaultApplicationTaskExecutor();
        }
        return subscriptionTask;
    }

}
