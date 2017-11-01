package org.workflow.core.service.sub;

import org.workflow.core.service.WorkFlowProcessor;
import org.workflow.core.service.WorkFlow;

import com.wso2telco.core.dbutils.exception.BusinessException;

public class SubscriptionProcessorFactory implements WorkFlow {
	private static SubscriptionProcessorFactory instance;

	private SubscriptionProcessorFactory() {

	}

	public static SubscriptionProcessorFactory getInstance() {
		if (instance == null) {
			instance = new SubscriptionProcessorFactory();
		}
		return instance;
	}

	@Override
	public WorkFlowProcessor getWorkFlowProcessor() throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
}
