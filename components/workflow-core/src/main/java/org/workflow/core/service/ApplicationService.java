package org.workflow.core.service;

import javax.ws.rs.core.Response;

import org.workflow.core.activity.ActivityClientFactory;
import org.workflow.core.activity.ProcessSearchRequest;
import org.workflow.core.activity.RestClient;
import org.workflow.core.execption.WorkflowExtensionException;
import org.workflow.core.model.TaskList;
import org.workflow.core.model.TaskSerchDTO;
import org.workflow.core.model.TaskVariableResponse;
import org.workflow.core.service.app.QueryBuilderFactory;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;



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
