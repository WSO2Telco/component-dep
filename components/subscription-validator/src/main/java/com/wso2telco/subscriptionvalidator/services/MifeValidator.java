package com.wso2telco.subscriptionvalidator.services;

import org.apache.synapse.MessageContext;

public interface MifeValidator {

    public boolean validate(MessageContext messageContext);
}
