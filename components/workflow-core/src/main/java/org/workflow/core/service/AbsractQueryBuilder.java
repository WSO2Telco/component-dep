package org.workflow.core.service;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.core.dbutils.util.AppApprovalRequest;
import com.wso2telco.core.dbutils.util.Callback;
import org.apache.commons.logging.Log;
import org.workflow.core.activity.ActivityClientFactory;
import org.workflow.core.activity.ApplicationApprovalRequest;
import org.workflow.core.activity.ProcessSearchRequest;
import org.workflow.core.activity.RestClient;
import org.workflow.core.execption.WorkflowExtensionException;
import org.workflow.core.model.*;
import org.workflow.core.util.DeploymentTypes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    protected abstract List<Integer> getHistoricalData(String user, List<Range> months) throws BusinessException;

	public Callback searchPending(TaskSerchDTO searchDTO, final UserProfileDTO userProfile) throws BusinessException {
		ProcessSearchRequest processRequest = buildSearchRequest(searchDTO, userProfile);
		TaskList taskList = null;
		try {
			taskList = activityClient.getTasks(processRequest);

			for (TaskList.Task task : taskList.getData()) {
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
		
		request.setProcessDefinitionKey(getDeployementType().getAppProcessType());
		
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

    @Override
    public Callback getGraphData(UserProfileDTO userProfile) throws BusinessException {

        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH);
        DateFormat monthFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);

        List<Range> months = new ArrayList();
        List<String> xAxisLabels = new ArrayList();

        for (int i = -5; i < 1; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, i);
            calendar.set(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR, -12);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Date start = calendar.getTime();

            calendar.add(Calendar.MONTH, 2);
            calendar.set(Calendar.DATE, -1);
            calendar.set(Calendar.HOUR, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);

            Date stop = calendar.getTime();

            months.add(new Range().setStart(simpleDateFormat.format(start)).setEnd(simpleDateFormat.format(stop)));
            xAxisLabels.add(monthFormat.format(stop));
        }

        List<Integer> data = getHistoricalData(userProfile.getUserName(), months);
        if (!data.isEmpty()) {
            GraphData graphData = new GraphData();
            graphData.setData(data);
            graphData.setLabel("applications");
            List<GraphData> graphDataList = new ArrayList();
            graphDataList.add(graphData);
            GraphResponse graphResponse = new GraphResponse();
            graphResponse.setXAxisLabels(xAxisLabels);
            graphResponse.setGraphData(graphDataList);
            return new Callback().setPayload(graphResponse).setSuccess(true).setMessage("History Loaded Successfully");
        } else {
            return new Callback().setPayload(Collections.emptyList()).setSuccess(false).setMessage("Error Loading Approval History");
        }

    }

	@Override
	public Callback approveApplication(AppApprovalRequest appApprovalRequest) throws BusinessException {
		ApplicationApprovalRequest request = buildApprovalRequest(appApprovalRequest);
//		try {
//			activityClient.approveApplication(appApprovalRequest.getTaskId(), request);
//
//		} catch (WorkflowExtensionException e) {
//			log.error("", e);
//			throw new BusinessException(e);
//		}
		return null;
	}

	protected abstract  ApplicationApprovalRequest buildApprovalRequest(final AppApprovalRequest appApprovalRequest) throws BusinessException;

}
