package org.workflow.core.service.app;

import org.workflow.core.service.RequestBuilder;
import org.workflow.core.util.DeploymentTypes;
import org.workflow.core.util.WorkFlowHealper;

public final class QueryBuilderFactory {
	private QueryBuilderFactory() {
	}

	private static QueryBuilderFactory instance;

	public static QueryBuilderFactory getInstance() {
		if (instance == null) {
			instance = new QueryBuilderFactory();
		}
		return instance;
	}
	
	public RequestBuilder loadQueryBuilder() {
		DeploymentTypes depType =DeploymentTypes.getByName( WorkFlowHealper.getDeploymentType());
		RequestBuilder queryBuilder=null;
		switch (depType) {
		case EXTERNAL_GATEWAY:
			queryBuilder =  DefaultAppRequestBuilder.getInstace();
			break;
		case HUB:
			queryBuilder =  DefaultAppRequestBuilder.getInstace();
			break;
		case INTERNAL_GATEWAY:
			queryBuilder =  INTGWQueryBuilder.getInstace();
			break;
		default:
			queryBuilder =  DefaultAppRequestBuilder.getInstace();
			break;
		
		}
		return queryBuilder;
	}
}
