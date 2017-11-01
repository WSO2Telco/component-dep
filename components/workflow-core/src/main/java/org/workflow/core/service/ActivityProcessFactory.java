package org.workflow.core.service;

import org.workflow.core.service.app.QueryBuilderFactory;
import org.workflow.core.service.sub.SubscriptionProcessorFactory;
import org.workflow.core.util.WorkFlowType;

public class ActivityProcessFactory {
	private ActivityProcessFactory() {
	}

	private static  ActivityProcessFactory instance;

	public static synchronized ActivityProcessFactory getInstance() {
		if (instance == null) {
			instance = new ActivityProcessFactory();
		}
		return instance;

	}
	
	public WorkFlow getWorkFlowFactory(WorkFlowType type) {
		WorkFlow wf = null;
		switch (type) {
		case APPLICATION:
			wf= QueryBuilderFactory.getInstance();
			break;
		case SUBSCRIPTION:
			wf= SubscriptionProcessorFactory.getInstance();
			break;
		default:
			break;
		}
		return wf;
	} 
}
