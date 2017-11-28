package org.workflow.core.service.sub;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.workflow.core.activity.ActivityClientFactory;
import org.workflow.core.activity.ProcessSearchRequest;
import org.workflow.core.activity.RestClient;
import org.workflow.core.execption.WorkflowExtensionException;
import org.workflow.core.model.Task;
import org.workflow.core.model.TaskList;
import org.workflow.core.model.TaskSerchDTO;
import org.workflow.core.model.TaskVariableResponse;
import org.workflow.core.model.Variable;
import org.workflow.core.service.WorkFlowProcessor;
import org.workflow.core.util.DeploymentTypes;

import com.google.gson.Gson;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.core.dbutils.util.Callback;

import feign.QueryMap;

public abstract class AbsractQueryBuilder implements WorkFlowProcessor {
	protected Log log;
	protected RestClient activityClient = null;

	public AbsractQueryBuilder() throws BusinessException {
		activityClient = ActivityClientFactory.getInstance().getClient();
	}

	protected abstract DeploymentTypes getDeployementType() ;

	protected abstract  Callback buildResponse(final TaskSerchDTO searchDTO, final TaskList taskList,
			final UserProfileDTO userProfile) throws BusinessException;

	protected  abstract Map<String,String> getFilterMap() ;

	public Callback searchPending(TaskSerchDTO searchDTO, final UserProfileDTO userProfile) throws BusinessException {
		ProcessSearchRequest processRequest = buildSearchRequest(searchDTO, userProfile);

		String appParam="subscription_approval_process";
		TaskList taskList = null;
		try {
			taskList = activityClient.getTasks(appParam,processRequest);

			for (Task task : taskList.getData()) {
				TaskVariableResponse[] vars = activityClient.getVariables(String.valueOf(task.getId()));
				task.setVariable(vars);
			}

		} catch (WorkflowExtensionException e) {
			log.error("", e);
			throw new BusinessException(e);
		}
		return buildResponse(searchDTO, taskList, userProfile);

	}

	@Override
	public ProcessSearchRequest buildSearchRequest(TaskSerchDTO searchDTO, final UserProfileDTO userProfile)
			throws BusinessException {
		ProcessSearchRequest request = new ProcessSearchRequest();
		request.setSize(searchDTO.getBatchSize());
		request.setStart(searchDTO.getStart());
		request.setSort(searchDTO.getSortBy());

		//request.setProcessDefinitionKey(getDeployementType().getAppProcessType());

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
				 * split the criteria by : to separate out the name and value ,
				 */
				String[] critiraarry = critira.split(":");
				/**
				 * validate name and value. Both should not be null. and filer name should be
				 * defined at the filter map .if not ignore adding.
				 */
				if (critiraarry.length == 2 && !critiraarry[0].trim().isEmpty() && !critiraarry[1].trim().isEmpty()
						&& getFilterMap().containsKey(critiraarry[0].trim())) {
					/**
					 * add process variable ,
					 *
					 */

					Variable var = new Variable(getFilterMap().get(critiraarry[0]), critiraarry[1]);
					request.addProcessVariable(var);
				}
			}
		}

		return request;
	}
}
