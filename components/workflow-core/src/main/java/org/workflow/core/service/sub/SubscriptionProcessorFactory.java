package org.workflow.core.service.sub;

import org.workflow.core.service.WorkFlow;

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
}
