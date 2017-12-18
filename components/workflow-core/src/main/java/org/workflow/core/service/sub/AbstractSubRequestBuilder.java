package org.workflow.core.service.sub;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.Callback;
import com.wso2telco.core.userprofile.dto.UserProfileDTO;
import com.wso2telco.dep.operatorservice.service.OparatorService;
import org.workflow.core.activity.ActivityRestClient;
import org.workflow.core.activity.RestClientFactory;
import org.workflow.core.execption.WorkflowExtensionException;
import org.workflow.core.model.*;
import org.workflow.core.model.rate.RateDefinition;
import org.workflow.core.restclient.RateRestClient;
import org.workflow.core.service.AbsractQueryBuilder;
import org.workflow.core.util.AppVariable;
import org.workflow.core.util.DeploymentTypes;
import org.workflow.core.util.Messages;

import java.text.ParseException;
import java.util.*;

abstract class AbstractSubRequestBuilder extends AbsractQueryBuilder {

    private static final String GRAPH_LABEL = "SUBSCRIPTIONS";

    private SearchResponse generateResponse(final TaskList taskList) throws ParseException {

        TaskMetadata metadata = new TaskMetadata();
        metadata.setOrder(taskList.getOrder());
        metadata.setSize(taskList.getSize());
        metadata.setSort(taskList.getSort());
        metadata.setStart(taskList.getStart());
        metadata.setTotal(taskList.getTotal());

        List<ApplicationTask> applicationTasks = new ArrayList();

        for (int k = 0; k < taskList.getData().size(); k++) {

            Task task = taskList.getData().get(k);
            CreateTime createTime = getCreatedTime(task);
            List<RelevantRate> relevantRates = new ArrayList<RelevantRate>();
            List<Operation> operationRates;

            final Map<AppVariable, TaskVariableResponse> varMap = new HashMap<AppVariable, TaskVariableResponse>();
            for (final TaskVariableResponse var : task.getVariables()) {
                varMap.put(AppVariable.getByKey(var.getName()), var);
            }

            if (task.getOperationRates() != null && task.getOperationRates().getApi() != null) {

                operationRates = task.getOperationRates().getApi().getOperations();

                for (Operation operation : operationRates) {

                    RelevantRate relevantRate = new RelevantRate();
                    List<RateDefinition> rateDefinitions = new ArrayList<RateDefinition>();
                    for (RateDefinition rateDefinition : operation.getRates()) {

                        RateDefinition tempDef = new RateDefinition();
                        tempDef.setRateDefId(rateDefinition.getOperationRateId());
                        tempDef.setRateDefName(rateDefinition.getRateDefName());
                        tempDef.setRateDefDescription(rateDefinition.getRateDefDescription());

                        rateDefinitions.add(tempDef);
                    }
                    relevantRate.setApiOperation(operation.getApiOperationName());
                    relevantRate.setRateDefinitions(rateDefinitions);

                    relevantRates.add(relevantRate);
                }
            }

            String description;
            String tier;
            String applicationId;
            String applicationName;
            String operators;
            String assignee;
            List<String> tiersStr;

            if (varMap.containsKey(AppVariable.APPLICATION_DESCRIPTION)) {
                description = varMap.get(AppVariable.APPLICATION_DESCRIPTION).getValue();
            } else {
                description = "";
            }

            if (varMap.containsKey(AppVariable.TIER_NAME)) {
                tier = varMap.get(AppVariable.TIER_NAME).getValue();
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

            if (varMap.containsKey(AppVariable.API_TIERS)) {
                tiersStr = new ArrayList<String>(Arrays.asList(varMap.get(AppVariable.API_TIERS).getValue().split(",")));
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
            applicationTask.setUserName(varMap.get(AppVariable.SUBSCRIBER).getValue());
            applicationTask.setApiName(varMap.get(AppVariable.API_NAME).getValue());
            applicationTask.setApiVersion(varMap.get(AppVariable.API_VERSION).getValue());

            applicationTask.setRelevantRates(relevantRates);
            applicationTask.setSelectedRate("");
            applicationTask.setCreditPlan("");

            applicationTasks.add(applicationTask);
        }

        SearchResponse searchResponse = new SearchResponse();

        searchResponse.setMetadata(metadata);
        searchResponse.setApplicationTasks(applicationTasks);

        return searchResponse;
    }

    @Override
    protected Callback buildMyTaskResponse(TaskSearchDTO searchDTO, TaskList taskList, UserProfileDTO userProfile) throws BusinessException {

        TaskList myTaskList = getOperationRates(taskList, userProfile);
        SearchResponse payload;
        Callback returnCall;
        try {
            payload = generateResponse(myTaskList);
            returnCall = new Callback().setPayload(payload).setSuccess(true).setMessage(Messages.MY_SUBSCRIPTION_LOAD_SUCCESS.getValue());
        } catch (ParseException e) {
            returnCall = new Callback().setPayload(null).setSuccess(false).setMessage(Messages.MY_SUBSCRIPTION_LOAD_FAIL.getValue());
        }

        return returnCall;
    }

    @Override
    protected Callback buildAllTaskResponse(TaskSearchDTO searchDTO, TaskList taskList, UserProfileDTO userProfile) throws BusinessException {

        TaskList allTaskList = taskList;
        if (!isAdmin(userProfile)) {
            allTaskList = filterOperatorApprovedApps(taskList);
        }
        allTaskList = getOperationRates(allTaskList, userProfile);
        SearchResponse payload;
        Callback returnCall;
        try {
            payload = generateResponse(allTaskList);
            returnCall = new Callback().setPayload(payload).setSuccess(true).setMessage(Messages.ALL_SUBSCRIPTION_LOAD_SUCCESS.getValue());
        } catch (ParseException e) {
            returnCall = new Callback().setPayload(null).setSuccess(false).setMessage(Messages.ALL_SUBSCRIPTION_LOAD_FAIL.getValue());
        }

        return returnCall;
    }

    public TaskList filterOperatorApprovedApps(TaskList taskList) {

        String appIds = "";
        List<String> operatorApprovedApps = new ArrayList<String>();
        List<Task> tasks = new ArrayList<Task>();

        for (Task task : taskList.getData()) {
            final Map<AppVariable, TaskVariableResponse> varMap = new HashMap<AppVariable, TaskVariableResponse>();
            for (final TaskVariableResponse var : task.getVariables()) {
                varMap.put(AppVariable.getByKey(var.getName()), var);
            }
            if (varMap.containsKey(AppVariable.ID)) {
                appIds = appIds.concat(varMap.get(AppVariable.ID).getValue() + ",");
            }
        }

        try {
            operatorApprovedApps = new OparatorService().getOparatorApprovedApp(appIds);
        } catch (BusinessException e) {
            log.error("", e);
        }

        if (!operatorApprovedApps.isEmpty()) {
            for (Task task : taskList.getData()) {
                final Map<AppVariable, TaskVariableResponse> varMap = new HashMap<AppVariable, TaskVariableResponse>();
                for (final TaskVariableResponse var : task.getVariables()) {
                    varMap.put(AppVariable.getByKey(var.getName()), var);
                }
                for (String appId : operatorApprovedApps) {
                    if (varMap.get(AppVariable.ID).getValue().trim().equals(appId)) {
                        tasks.add(task);
                    }
                }
            }

            taskList.setData(tasks);
            taskList.setSize(tasks.size());

        } else {
            taskList.setData(tasks);
            taskList.setSize(tasks.size());
        }


        return taskList;
    }

    public TaskList getOperationRates(TaskList taskList, UserProfileDTO userProfile) throws BusinessException {

        RateRestClient rateRestClient = RestClientFactory.getInstance().getRateClient();

        for (int i = 0; i < taskList.getData().size(); i++) {

            Task task = taskList.getData().get(i);

            final Map<AppVariable, TaskVariableResponse> varMap = new HashMap<AppVariable, TaskVariableResponse>();
            for (final TaskVariableResponse var : task.getVariables()) {
                varMap.put(AppVariable.getByKey(var.getName()), var);
            }

            String apiName = varMap.get(AppVariable.API_NAME).getValue();

            try {
                if (isAdmin(userProfile)) {
                    OperationRateResponse rateResponse = rateRestClient.getAdminOperationRates(apiName);
                    taskList.getData().get(i).setOperationRates(rateResponse);
                } else {
                    OperationRateResponse rateResponse = rateRestClient.getOperatorOperationRates(apiName, userProfile.getUserName());
                    taskList.getData().get(i).setOperationRates(rateResponse);
                }
            } catch (WorkflowExtensionException e) {
                log.error("", e);
                throw new BusinessException(e);
            }
        }

        return taskList;
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
            return new Callback().setPayload(graphResponse).setSuccess(true).setMessage(Messages.SUBSCRIPTION_HISTORY_SUCCESS.getValue());
        } else {
            return new Callback().setPayload(Collections.emptyList()).setSuccess(false).setMessage(Messages.SUBSCRIPTION_HISTORY_FALIED.getValue());
        }
    }

    @Override
    protected abstract Callback buildApprovalRequest(ApprovalRequest approvalRequest, UserProfileDTO userProfile) throws BusinessException;

    @Override
    protected String getProcessDefinitionKey() {
        return depType.getSubscriptoinProcessType();
    }
}
