package com.wso2telco.workflow.service.app;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.workflow.activityclient.ProcessSearchRequest;
import com.wso2telco.workflow.model.TaskSerchDTO;
import com.wso2telco.workflow.service.RequestBuilder;

abstract class AbsractQueryBuilder  implements RequestBuilder {
	protected Log LOG ;
	
	@Override
	public Map<String, Object> toMap(TaskSerchDTO dto, UserProfileDTO userProfile) throws BusinessException {
		LOG.debug("toMap :"+dto+" userProfile:"+userProfile);
		Map<String, Object> queryMap = new HashMap<String, Object>();
		
		queryMap.put("size",dto.getBatchSize());
		queryMap.put( "start", dto.getStart());
		queryMap.put( "order",dto.getOrderBy());
		queryMap.put( "sort",dto.getSortBy());
		
		return queryMap;
	}
	public ProcessSearchRequest buildSearchRequest(final TaskSerchDTO searchDTO,final UserProfileDTO userProfile) throws BusinessException{
		ProcessSearchRequest psRequest = new ProcessSearchRequest();
		psRequest.setSize(searchDTO.getBatchSize());
		psRequest.setStart(searchDTO.getStart());
		psRequest.setSort(searchDTO.getSortBy());
		return psRequest;
	}
}
