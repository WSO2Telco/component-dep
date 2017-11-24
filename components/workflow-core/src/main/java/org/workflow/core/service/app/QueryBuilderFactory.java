package org.workflow.core.service.app;

import org.workflow.core.service.WorkFlowProcessor;
import org.workflow.core.service.WorkFlow;
import org.workflow.core.util.DeploymentTypes;
import org.workflow.core.util.WorkFlowHealper;

import com.wso2telco.core.dbutils.exception.BusinessException;

public final class QueryBuilderFactory  implements WorkFlow{
	private QueryBuilderFactory() {
	}

	private static QueryBuilderFactory instance;

	public static QueryBuilderFactory getInstance() {
		if (instance == null) {
			instance = new QueryBuilderFactory();
		}
		return instance;
	}
	
	public WorkFlowProcessor getWorkFlowProcessor() throws BusinessException {
		DeploymentTypes depType =DeploymentTypes.getByName( WorkFlowHealper.getDeploymentType());
		WorkFlowProcessor queryBuilder=null;
		switch (depType) {
		case EXTERNAL_GATEWAY:
			queryBuilder =  DefaultAppRequestBuilder.getInstace(DeploymentTypes.EXTERNAL_GATEWAY);
			break;
		case HUB:
			queryBuilder =  DefaultAppRequestBuilder.getInstace(DeploymentTypes.HUB);
			break;
		case INTERNAL_GATEWAY:
			queryBuilder =  DefaultAppRequestBuilder.getInstace(DeploymentTypes.INTERNAL_GATEWAY);
			break;
		default:
			queryBuilder =  DefaultAppRequestBuilder.getInstace(DeploymentTypes.EXTERNAL_GATEWAY);
			break;
		
		}
		return queryBuilder;
	}
}
