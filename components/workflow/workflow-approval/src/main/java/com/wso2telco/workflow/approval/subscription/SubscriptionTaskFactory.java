package com.wso2telco.workflow.approval.subscription;

import com.wso2telco.workflow.approval.util.Constants;

public class SubscriptionTaskFactory {

    public SubscriptionTask getInstance(String deploymentTypes) {
        SubscriptionTask subscriptionTask = null;
        if (deploymentTypes.startsWith(Constants.INTERNAL_GATEWAY)) {
            subscriptionTask = new InternalGatewayTask();
        } else if (deploymentTypes.equals(Constants.EXTERNAL_GATEWAY)) {
            subscriptionTask = new ExternalGatewayTask();
        } else if (deploymentTypes.equals(Constants.HUB)) {
            subscriptionTask = new HubTask();
        }
        return subscriptionTask;
    }

}
