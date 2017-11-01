package org.workflow.core.service.app;

import org.workflow.core.service.RequestBuilder;
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
	
	public RequestBuilder loadQueryBuilder() throws BusinessException {
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
			queryBuilder =  DefaultAppRequestBuilder.getInstace();
			break;
		default:
			queryBuilder =  DefaultAppRequestBuilder.getInstace();
			break;
		
		}
		return queryBuilder;
	}
}
