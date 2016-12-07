package com.wso2telco.workflow.approval.application;

import com.wso2telco.workflow.approval.util.Constants;

public class ApplicationTaskFactory {

    public ApplicationTask getInstance(String deploymentTypes) {
        ApplicationTask subscriptionTask = null;
        if (deploymentTypes.equals(Constants.HUB)) {
            subscriptionTask = new HubTask();
        } else {
            new DefaultApplicationTaskExecutor();
        }
        return subscriptionTask;
    }

}
