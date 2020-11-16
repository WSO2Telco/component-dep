package com.wso2telco.dep.mediator;

import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class ResponseTimeMediator extends AbstractMediator {

    private static final String REQUEST_TIMESTAMP = "REQUEST_TIMESTAMP";
    private static final String RESPONSE_TIMESTAMP = "RESPONSE_TIMESTAMP";
    private static final String RESPONSE_TIME = "RESPONSE_TIME";
    
    @Override
    public boolean mediate(MessageContext messageContext) {
    	if ((messageContext.getProperty(REQUEST_TIMESTAMP) != null)
    			&& (messageContext.getProperty(RESPONSE_TIMESTAMP) != null)) {
    		Long requestTimeStamp = Long.parseLong((String) messageContext.getProperty(REQUEST_TIMESTAMP));
    		Long responseTimeStamp = Long.parseLong((String) messageContext.getProperty(RESPONSE_TIMESTAMP));
            Long responseTime = responseTimeStamp - requestTimeStamp;
            messageContext.setProperty(RESPONSE_TIME, responseTime);
    	} else {
            messageContext.setProperty(RESPONSE_TIME, -1);
    	}
        return true;
    }

}