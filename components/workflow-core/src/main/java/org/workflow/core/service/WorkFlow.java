package org.workflow.core.service;

import com.wso2telco.core.dbutils.exception.BusinessException;

public interface WorkFlow {
	public WorkFlowProcessor getWorkFlowProcessor() throws BusinessException ;
}
