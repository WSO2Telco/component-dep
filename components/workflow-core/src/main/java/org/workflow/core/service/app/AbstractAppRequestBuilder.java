package org.workflow.core.service.app;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.Callback;
import org.workflow.core.activity.RestClientFactory;
import org.workflow.core.activity.ActivityRestClient;
import org.workflow.core.activity.TaskApprovalRequest;
import org.workflow.core.execption.WorkflowExtensionException;
import org.workflow.core.model.*;
import org.workflow.core.service.AbsractQueryBuilder;
import org.workflow.core.util.AppVariable;
import org.workflow.core.util.DeploymentTypes;
import org.workflow.core.util.Messages;
import org.workflow.core.util.WorkFlowVariables;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

abstract class AbstractAppRequestBuilder extends AbsractQueryBuilder {

    private static final String GRAPH_LABEL = "APPLICATIONS";

    private SearchResponse generateResponse(final TaskSearchDTO searchDTO, final TaskList taskList, final UserProfileDTO userProfile) throws ParseException {

        DateFormat format = new SimpleDateFormat(WorkFlowVariables.DATE_FORMAT.getValue(), Locale.ENGLISH);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat offsetFormatter = new SimpleDateFormat("XXX");

        TaskMetadata metadata = new TaskMetadata();
        metadata.setOrder(taskList.getOrder());
        metadata.setSize(taskList.getSize());
        metadata.setSort(taskList.getSort());
        metadata.setStart(taskList.getStart());
        metadata.setTotal(taskList.getTotal());

        List<ApplicationTask> applicationTasks = new ArrayList();

        for (int k = 0; k < taskList.getData().size(); k++) {

            Task task = taskList.getData().get(k);
            CreateTime createTime = new CreateTime();

            if (task.getCreateTime() != null) {
                Date date = format.parse(task.getCreateTime());
                createTime.setDate(dateFormatter.format(date));
                createTime.setTime(timeFormatter.format(date));
                createTime.setOffset(offsetFormatter.format(date));
                createTime.setUnformatted(task.getCreateTime());
            } else {
                createTime.setDate("");
                createTime.setTime("");
                createTime.setOffset("");
                createTime.setUnformatted("");
            }

            final Map<AppVariable, TaskVariableResponse> varMap = new HashMap<AppVariable, TaskVariableResponse>();
            for (final TaskVariableResponse var : task.getVariables()) {
                varMap.put(AppVariable.getByKey(var.getName()), var);
            }

            String description;
            String tier;
            String applicationId;
            String applicationName;
            String operators;
            String assignee;
            List<String> tiersStr;

            if (varMap.containsKey(AppVariable.DESCRIPTION)) {
                description = varMap.get(AppVariable.DESCRIPTION).getValue();
            } else {
                description = "";
            }

            if (varMap.containsKey(AppVariable.TIER)) {
                tier = varMap.get(AppVariable.TIER).getValue();
            } else {
                tier = "";
            }

            if (varMap.containsKey(AppVariable.ID)) {
                applicationId = varMap.get(AppVariable.ID).getValue();
            } else {
                applicationId = "";
            }

            if (varMap.containsKey(AppVariable.NAME)) {
                applicationName = varMap.get(AppVariable.NAME).getValue();
            } else {
                applicationName = "";
            }

            if (varMap.containsKey(AppVariable.OPARATOR)) {
                operators = varMap.get(AppVariable.OPARATOR).getValue();
            } else {
                operators = "";
            }

            if (varMap.containsKey(AppVariable.TIER_STRING)) {
                tiersStr = new ArrayList<String>(Arrays.asList(varMap.get(AppVariable.TIER_STRING).getValue().split(",")));
            } else {
                tiersStr = Collections.emptyList();
            }

            if (task.getAssignee() == null) {
                assignee = "";
            } else {
                assignee = task.getAssignee();
            }

            ApplicationTask applicationTask = new ApplicationTask();

            applicationTask.setId(task.getId());
            applicationTask.setAssignee(assignee);
            applicationTask.setCreateTime(createTime);
            applicationTask.setTaskDescription(task.getDescription());
            applicationTask.setApplicationId(applicationId);
            applicationTask.setApplicationName(applicationName);
            applicationTask.setApplicationDescription(description);
            applicationTask.setOperators(operators);
            applicationTask.setTier(tier);
            applicationTask.setTiersStr(tiersStr);
            applicationTask.setUserName(varMap.get(AppVariable.USERNAME).getValue());
            applicationTask.setCreditPlan("");

            applicationTask.setRelevantRates(Collections.<String>emptyList());
            applicationTask.setSelectedRate("");
            applicationTask.setApiName("");

            applicationTasks.add(applicationTask);
        }

        SearchResponse searchResponse = new SearchResponse();

        searchResponse.setMetadata(metadata);
        searchResponse.setApplicationTasks(applicationTasks);

        return searchResponse;
    }

    @Override
    protected Callback buildMyTaskResponse(TaskSearchDTO searchDTO, TaskList taskList, UserProfileDTO userProfile)
            throws BusinessException {
        SearchResponse payload;
        Callback returnCall;
        try {
            payload = generateResponse(searchDTO, taskList, userProfile);
            returnCall = new Callback().setPayload(payload).setSuccess(true).setMessage(Messages.MY_APPLICATION_LOAD_SUCCESS.getValue());
        } catch (ParseException e) {
            returnCall = new Callback().setPayload(null).setSuccess(false).setMessage(Messages.MY_APPLICATION_LOAD_FAIL.getValue());
        }

        return returnCall;
    }

    @Override
    protected Callback buildAllTaskResponse(TaskSearchDTO searchDTO, TaskList taskList, UserProfileDTO userProfile)
            throws BusinessException {
        SearchResponse payload;
        Callback returnCall;
        try {
            payload = generateResponse(searchDTO, taskList, userProfile);
            returnCall = new Callback().setPayload(payload).setSuccess(true).setMessage(Messages.ALL_APPLICATION_LOAD_SUCCESS.getValue());
        } catch (ParseException e) {
            returnCall = new Callback().setPayload(null).setSuccess(false).setMessage(Messages.ALL_APPLICATION_LOAD_FAIL.getValue());
        }

        return returnCall;
    }

    @Override
    protected DeploymentTypes getDeployementType() {
        return depType;
    }

    @Override
    protected Callback getHistoricalData(String user, List<Range> months, List<String> xAxisLabels) throws BusinessException {
        List<Integer> data = new ArrayList();
        ActivityRestClient activityClient = RestClientFactory.getInstance().getClient(getProcessDefinitionKey());
        TaskDetailsResponse taskList = null;

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
            return new Callback().setPayload(graphResponse).setSuccess(true).setMessage(Messages.APPLICATION_HISTORY_SUCCESS.getValue());
        } else {
            return new Callback().setPayload(Collections.emptyList()).setSuccess(false)
                    .setMessage(Messages.APPLICATION_HISTORY_FAILED.getValue());
        }
    }

    protected Callback executeTaskApprovalRequest(TaskApprovalRequest approvalRequest, ApprovalRequest request) throws BusinessException {
        ActivityRestClient activityClient = RestClientFactory.getInstance().getClient(getProcessDefinitionKey());
        try {
            activityClient.approveTask(request.getTaskId(), approvalRequest);
            return new Callback().setPayload(null).setSuccess(true).setMessage(Messages.APPLICATION_APPROVAL_SUCCESS.getValue());
        } catch (WorkflowExtensionException e) {
            log.error("", e);
            return new Callback().setPayload(null).setSuccess(false).setMessage(Messages.APPLICATION_APPROVAL_FAILED.getValue());
        }
    }

    @Override
    protected abstract Callback buildApprovalRequest(ApprovalRequest approvalRequest) throws BusinessException;

    protected String getProcessDefinitionKey() {
        return depType.getAppProcessType();
    }
}
