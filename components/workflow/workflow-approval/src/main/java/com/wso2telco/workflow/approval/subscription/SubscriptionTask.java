package com.wso2telco.workflow.approval.subscription;

import org.activiti.engine.delegate.DelegateExecution;

public interface SubscriptionTask {
	
	public void execute(final DelegateExecution arg0) throws Exception;

}
