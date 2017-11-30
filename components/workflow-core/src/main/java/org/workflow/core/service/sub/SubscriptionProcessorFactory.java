package org.workflow.core.service.sub;

import org.workflow.core.service.WorkFlowProcessor;
import org.workflow.core.service.WorkFlow;
import org.workflow.core.util.DeploymentTypes;
import org.workflow.core.util.WorkFlowHealper;

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
	//TODO: it need to implement subscription bulder for EXT-GW,INT-gw as well
	public WorkFlowProcessor getWorkFlowProcessor() throws BusinessException {
		DeploymentTypes deploymentType = DeploymentTypes.getByName(WorkFlowHealper.getDeploymentType());
		WorkFlowProcessor workflowProcessor = null;
		switch(deploymentType) {
		/*case EXTERNAL_GATEWAY:
			workflowProcessor = HubSubRequestBuilder.getInstace(deploymentType);
			break;*/
		case HUB:
			workflowProcessor = HubSubRequestBuilder.getInstace();
			break;
		/*case INTERNAL_GATEWAY:
			workflowProcessor = HubSubRequestBuilder.getInstace(deploymentType);
			break;*/
		/*default:
			workflowProcessor = HubSubRequestBuilder.getInstace(deploymentType);
			break;*/
		}
		return workflowProcessor;
	}
}
