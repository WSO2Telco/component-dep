package com.wso2telco.workflow.approval.subscription.initiate;


public class SubscriptionInitiateFactory {

    public SubscriptionInitiate getInstance(String deploymentTypes) {

        SubscriptionInitiate subscriptionInitiate=null;
        if (deploymentTypes.startsWith("internal_gateway")) {
            subscriptionInitiate=new InternalGatewayInitiate();
        } else if (deploymentTypes.equals("external_gateway")) {
            subscriptionInitiate=new ExternalGatewayInitiate();
        } else if (deploymentTypes.equals("hub")) {
            subscriptionInitiate=new HubInitiate();
        }
        return  subscriptionInitiate;
    }
}
