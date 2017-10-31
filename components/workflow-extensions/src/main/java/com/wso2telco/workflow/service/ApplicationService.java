package com.wso2telco.workflow.service;

import javax.ws.rs.core.Response;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.hub.workflow.extensions.exceptions.WorkflowExtensionException;
import com.wso2telco.workflow.activityclient.ActivityClientFactory;
import com.wso2telco.workflow.activityclient.ProcessSearchRequest;
import com.wso2telco.workflow.activityclient.RestClient;
import com.wso2telco.workflow.model.TaskList;
import com.wso2telco.workflow.model.TaskSerchDTO;
import com.wso2telco.workflow.service.app.QueryBuilderFactory;



public class ApplicationService {
	public Response getPendingApprovals(final TaskSerchDTO serchD,UserProfileDTO userProfile) throws BusinessException {
		RequestBuilder queryBuilder = QueryBuilderFactory.getInstance().loadQueryBuilder();
		ProcessSearchRequest searchRequest =queryBuilder.buildSearchRequest(serchD, userProfile);
		
		RestClient activityClient= ActivityClientFactory.getInstance().getClient();
		try {
			TaskList taskList = activityClient.getTasks(searchRequest);
		} catch (WorkflowExtensionException e) {
			throw new BusinessException(e);
		}
		return null;
	}

}
