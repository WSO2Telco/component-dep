package com.wso2telco.workflow.service.app;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.hub.workflow.extensions.ApplicationCreationRestWorkflowExecutor;
import com.wso2telco.hub.workflow.extensions.beans.Variable;
import com.wso2telco.hub.workflow.extensions.util.DeploymentTypes;
import com.wso2telco.workflow.activityclient.ProcessSearchRequest;
import com.wso2telco.workflow.model.TaskSerchDTO;
import com.wso2telco.workflow.util.WorkFlowHealper;

class DefaultAppRequestBuilder extends AbsractQueryBuilder {
	{
		super.LOG = LogFactory.getLog(DefaultAppRequestBuilder.class);
	}
	private static final   Map<String,String> FILTERMAP ;
	static{
		FILTERMAP = new HashMap<String, String>();
		FILTERMAP.put("name", ApplicationCreationRestWorkflowExecutor.APPLICATION_NAME);
		FILTERMAP.put("applicationname", ApplicationCreationRestWorkflowExecutor.APPLICATION_NAME);
		FILTERMAP.put("appname",ApplicationCreationRestWorkflowExecutor.APPLICATION_NAME);
		FILTERMAP.put("tier", ApplicationCreationRestWorkflowExecutor.TIER );
		FILTERMAP.put("createdby", ApplicationCreationRestWorkflowExecutor.USER_NAME);
		FILTERMAP.put("owner",  ApplicationCreationRestWorkflowExecutor.USER_NAME);
		
		
	}
	private static DefaultAppRequestBuilder instance;

	private DefaultAppRequestBuilder() {
	}

	public static DefaultAppRequestBuilder getInstace() {
		if (instance == null) {
			instance = new DefaultAppRequestBuilder();
		}
		return instance;
	}

	@Override
	public Map<String, Object> toMap(TaskSerchDTO dto, UserProfileDTO userProfile) throws BusinessException {
		Map<String, Object> queryMap = super.toMap(dto, userProfile);
		queryMap.put("assignee", userProfile.getUserName());
		return queryMap;
	}

	@Override
	public ProcessSearchRequest buildSearchRequest(TaskSerchDTO searchDTO, final UserProfileDTO userProfile)
			throws BusinessException {
		ProcessSearchRequest request = super.buildSearchRequest(searchDTO, userProfile);
		request.setProcessDefinitionKey(
				DeploymentTypes.getByName(WorkFlowHealper.getDeploymentType()).getAppProcessType());
		String filterStr = searchDTO.getFilterBy();
		/**
		 * if the request need to be filtered the string must be formated as
		 * filterby:value,filterby2:value
		 */
		if (filterStr != null && !filterStr.trim().isEmpty()) {
			/**
			 * split the multiple filter criteria by ,
			 */
			final String[] filterCritias = filterStr.split(",");
			for (String critira : filterCritias) {
				/**
				 * split the  criteria by : to separate out the name and value ,
				 */
				String[] critiraarry = critira.split(":");
				/**
				 * validate  name and value. Both should not be null.
				 * and filer name should be defined at the filter map .if not ignore
				 * adding. 
				 */
				if (critiraarry.length == 2 
						&& !critiraarry[0].trim().isEmpty() 
						&& !critiraarry[1].trim().isEmpty()
						&& FILTERMAP.containsKey(critiraarry[0].trim() )) {
					/**
					 * add process variable ,
					 * 
					 */
					
					Variable var = new Variable(FILTERMAP.get(critiraarry[0] ), critiraarry[1]);
					request.addProcessVariable(var);
				}
			}
		}

		return request;
	}
}
