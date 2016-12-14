package com.wso2telco.workflow.approval.application;

import org.activiti.engine.delegate.DelegateExecution;

public interface ApplicationTask {
	
	public void execute(final DelegateExecution arg0) throws Exception;

}
