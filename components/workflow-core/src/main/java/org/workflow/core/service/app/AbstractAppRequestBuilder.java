package org.workflow.core.service.app;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.Callback;
import com.wso2telco.core.userprofile.dto.UserProfileDTO;
import org.workflow.core.activity.ActivityRestClient;
import org.workflow.core.activity.RestClientFactory;
import org.workflow.core.activity.TaskApprovalRequest;
import org.workflow.core.dboperation.DatabaseHandler;
import org.workflow.core.execption.WorkflowExtensionException;
import org.workflow.core.model.*;
import org.workflow.core.service.AbsractQueryBuilder;
import org.workflow.core.util.AppVariable;
import org.workflow.core.util.DeploymentTypes;
import org.workflow.core.util.HistoryVariable;
import org.workflow.core.util.Messages;
import org.workflow.core.util.WorkFlowVariables;

import java.util.*;

abstract class AbstractAppRequestBuilder extends AbsractQueryBuilder {

    private static final String GRAPH_LABEL = "APPLICATIONS";

    @Override
    protected String getCandidateGroup(UserProfileDTO userProfileDTO) {
        //return userProfileDTO.getUserName();
    	if(isAdmin(userProfileDTO)){
            return WorkFlowVariables.GATEWAY_ADMIN_ROLE.getValue();
        }else {
            return userProfileDTO.getUserName();
        }
    }

    private AppSearchResponse generateResponse(final TaskList taskList) throws BusinessException {

        TaskMetadata metadata = new TaskMetadata(taskList);
        List<ApplicationTask> applicationTasks = new ArrayList();
        final Map<AppVariable, TaskVariableResponse> varMap = new HashMap<AppVariable, TaskVariableResponse>();

        for (Task task : taskList.getData()) {
            CreateTime createTime = getCreatedTime(task);
            varMap.clear();
            for (final TaskVariableResponse var : task.getVariables()) {
                varMap.put(AppVariable.getByKey(var.getName()), var);
            }

            List<String> tiersStr = Collections.emptyList();

            if (varMap.containsKey(AppVariable.TIER_STRING)) {
                tiersStr = new ArrayList<String>(Arrays.asList(varMap.get(AppVariable.TIER_STRING).getValue().split(",")));
            }

            ApplicationTask applicationTask = new ApplicationTask(varMap);

            applicationTask.setCreateTime(createTime);
            applicationTask.setId(task.getId());
            applicationTask.setAssignee((task.getAssignee() != null)?task.getAssignee():"");
            applicationTask.setTaskDescription(task.getDescription());
            applicationTask.setTiersStr(tiersStr);

            applicationTasks.add(applicationTask);
        }

        AppSearchResponse searchResponse = new AppSearchResponse();
        searchResponse.setMetadata(metadata);
        searchResponse.setApplicationTasks(applicationTasks);

        return searchResponse;
    }

    @Override
    protected Callback buildMyTaskResponse(TaskSearchDTO searchDTO, TaskList taskList, UserProfileDTO userProfile)
            throws BusinessException {
        AppSearchResponse payload;
        Callback returnCall;
        try {
            payload = generateResponse(taskList);
            returnCall = new Callback().setPayload(payload).setSuccess(true).setMessage(Messages.MY_APPLICATION_LOAD_SUCCESS.getValue());
        } catch (BusinessException e) {
            returnCall = new Callback().setPayload(e.getMessage()).setSuccess(false).setMessage(Messages.MY_APPLICATION_LOAD_FAIL.getValue());
        }

        return returnCall;
    }

    @Override
    protected Callback buildAllTaskResponse(TaskSearchDTO searchDTO, TaskList taskList, UserProfileDTO userProfile)
            throws BusinessException {
        AppSearchResponse payload;
        Callback returnCall;
        try {
            payload = generateResponse(taskList);
            returnCall = new Callback().setPayload(payload).setSuccess(true).setMessage(Messages.ALL_APPLICATION_LOAD_SUCCESS.getValue());
        } catch (BusinessException e) {
            returnCall = new Callback().setPayload(e.getMessage()).setSuccess(false).setMessage(Messages.ALL_APPLICATION_LOAD_FAIL.getValue());
        }

        return returnCall;
    }

    @Override
    protected DeploymentTypes getDeployementType() {
        return depType;
    }

    @Override
    protected Callback getHistoricalGraphData(String user, List<Range> months, List<String> xAxisLabels) throws BusinessException {
        List<Integer> data = new ArrayList();
        ActivityRestClient activityClient = RestClientFactory.getInstance().getClient(getProcessDefinitionKey());
        TaskDetailsResponse taskList = null;
        Callback returnCall;

        for (Range month : months) {
            taskList = activityClient.getHistoricTasks(month.getStart(), month.getEnd(), getProcessDefinitionKey(), user);
            data.add(taskList.getTotal());

        }

        if (!data.isEmpty()) {
            GraphData graphData = new GraphData();
            graphData.setData(data);
            graphData.setLabel(GRAPH_LABEL.toUpperCase());
            List<GraphData> graphDataList = new ArrayList();
            graphDataList.add(graphData);
            GraphResponse graphResponse = new GraphResponse();
            graphResponse.setXAxisLabels(xAxisLabels);
            graphResponse.setGraphData(graphDataList);
            returnCall = new Callback().setPayload(graphResponse).setSuccess(true).setMessage(Messages.APPLICATION_HISTORY_SUCCESS.getValue());
        } else {
            returnCall = new Callback().setPayload(Collections.emptyList()).setSuccess(false)
                    .setMessage(Messages.APPLICATION_HISTORY_FAILED.getValue());
        }

        return returnCall;
    }

    protected Callback executeTaskApprovalRequest(TaskApprovalRequest approvalRequest, ApprovalRequest request) throws BusinessException {
        ActivityRestClient activityClient = RestClientFactory.getInstance().getClient(getProcessDefinitionKey());
        Callback returnCall;
        try {
            activityClient.approveTask(request.getTaskId(), approvalRequest);
            returnCall = new Callback().setPayload(null).setSuccess(true).setMessage(Messages.APPLICATION_APPROVAL_SUCCESS.getValue());
        } catch (WorkflowExtensionException e) {
            log.error("", e);
            returnCall = new Callback().setPayload(null).setSuccess(false).setMessage(Messages.APPLICATION_APPROVAL_FAILED.getValue());
        }
        return returnCall;
    }

    public HistoryResponse getApprovalHistory(String subscriber, String applicationName, int applicationId, String operator, String status, int offset, int count) throws BusinessException {
        DatabaseHandler handler = new DatabaseHandler();
        return handler.getApprovalHistory(subscriber, applicationName, applicationId, operator, status, offset, count);
    }

    @Override
    protected abstract Callback buildApprovalRequest(ApprovalRequest approvalRequest, UserProfileDTO userProfile) throws BusinessException;

    protected String getProcessDefinitionKey() {
        return depType.getAppProcessType();
    }

    @Override
    public Callback getHistoryData(TaskSearchDTO searchDTO, UserProfileDTO userProfile) throws BusinessException {

        String filterStr = searchDTO.getFilterBy();
        final Map<String, String> varMap = new HashMap<String, String>();
        Callback returnCall;

        if (filterStr != null && !filterStr.trim().isEmpty()) {
            final String[] filterCriterias = filterStr.split(",");
            for (String criteria : filterCriterias) {
                String[] criteriaArray = criteria.split(":");
                if (criteriaArray.length == 2 && !criteriaArray[0].trim().isEmpty() && !criteriaArray[1].trim().isEmpty()
                        && historyFilterMap().containsKey(criteriaArray[0].trim().toLowerCase())) {
                    varMap.put(historyFilterMap().get(criteriaArray[0].trim().toLowerCase()), criteriaArray[1]);
                }
            }
        }

        String subscriber = ALL;
        int applicationId;
        String applicationName =ALL;
        String operator = ALL;
        String status = ALL;

        if (varMap.containsKey(HistoryVariable.SP.key())) {
            subscriber = varMap.get(HistoryVariable.SP.key());
        }

        if(varMap.containsKey(HistoryVariable.NAME.key())){
            applicationName = varMap.get(HistoryVariable.NAME.key());
        }

        if(varMap.containsKey(HistoryVariable.ID.key())) {
            applicationId = Integer.parseInt(varMap.get(HistoryVariable.ID.key()));
        }else {
            applicationId = 0;
        }

        if (varMap.containsKey(HistoryVariable.STATUS.key())) {
            status = varMap.get(HistoryVariable.STATUS.key());
        }

        if(!isAdmin(userProfile)){
            operator = userProfile.getUserName().toUpperCase();
        }else if(varMap.containsKey(HistoryVariable.OPARATOR.key())){
            operator = varMap.get(HistoryVariable.OPARATOR.key());
        }

        try {
            HistoryResponse apiRequests = getApprovalHistory( subscriber, applicationName, applicationId, operator, status, searchDTO.getStart(), searchDTO.getBatchSize());
            returnCall = new Callback().setPayload(apiRequests).setSuccess(true).setMessage(Messages.APPLICATION_HISTORY_SUCCESS.getValue());
        } catch (Exception e) {
            returnCall = new Callback().setPayload(e.getMessage()).setSuccess(false).setMessage(Messages.APPLICATION_HISTORY_FAILED.getValue());
        }
        return returnCall;
    }
}
