package com.wso2telco.workflow.service.app;

import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.workflow.model.TaskSerchDTO;
import com.wso2telco.workflow.service.RequestBuilder;

final class INTGWQueryBuilder extends AbsractQueryBuilder {
	{
		super.LOG = LogFactory.getLog(INTGWQueryBuilder.class);
	}
	protected static INTGWQueryBuilder instance;

	private INTGWQueryBuilder() {

	}

	@Override
	public Map<String, Object> toMap(TaskSerchDTO dto, UserProfileDTO userProfile) throws BusinessException {
		Map<String, Object> queryMap = super.toMap(dto, userProfile);
		//TODO:need to fill the assignGroup
		return queryMap;
	}

	public static RequestBuilder getInstace() {
		if (instance == null) {
			instance = new INTGWQueryBuilder();
		}
		return instance;
	}

}
