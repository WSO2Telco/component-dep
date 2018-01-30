package org.workflow.core.service.sub;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.Callback;
import com.wso2telco.core.userprofile.dto.UserProfileDTO;
import com.wso2telco.dep.operatorservice.service.OparatorService;
import org.workflow.core.activity.ActivityRestClient;
import org.workflow.core.activity.RestClientFactory;
import org.workflow.core.activity.TaskApprovalRequest;
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

    protected SubSearchResponse generateResponse(final TaskList taskList) throws ParseException {

        TaskMetadata metadata = new TaskMetadata(taskList);
        List<SubscriptionTask> subscriptionTasks = new ArrayList();
        final Map<AppVariable, TaskVariableResponse> varMap = new HashMap<AppVariable, TaskVariableResponse>();

        for (Task task : taskList.getData()) {
            CreateTime createTime = getCreatedTime(task);

            varMap.clear();
            for (final TaskVariableResponse var : task.getVariables()) {
                varMap.put(AppVariable.getByKey(var.getName()), var);
            }

            List<String> tiersStr = Collections.emptyList();
            List<RelevantRate> relevantRates = setRelevantRates(task);


            if (varMap.containsKey(AppVariable.API_TIERS)) {
                tiersStr = new ArrayList<String>(Arrays.asList(varMap.get(AppVariable.API_TIERS).getValue().split(",")));
            }

            SubscriptionTask subscriptionTask = new SubscriptionTask(varMap);

            subscriptionTask.setId(task.getId());
            subscriptionTask.setAssignee((task.getAssignee() != null) ? task.getAssignee() : "");
            subscriptionTask.setCreateTime(createTime);
            subscriptionTask.setTaskDescription(task.getDescription());
            subscriptionTask.setTiersStr(tiersStr);

            subscriptionTask.setRelevantRates(relevantRates);

            subscriptionTasks.add(subscriptionTask);
        }

        SubSearchResponse searchResponse = new SubSearchResponse();

        searchResponse.setMetadata(metadata);
        searchResponse.setApplicationTasks(subscriptionTasks);

        return searchResponse;
    }


    private List<RelevantRate> setRelevantRates(Task task) {

        List<RelevantRate> relevantRates = new ArrayList<RelevantRate>();
        List<Operation> operationRates;

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

        return relevantRates;
    }

    protected TaskList filterOperatorApprovedApps(TaskList taskList, String operatorName) {
        String appIds = "";
        List<String> operatorApprovedApps = null;

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
            operatorApprovedApps = new OparatorService().getOparatorApprovedApp(appIds, operatorName);
        } catch (BusinessException e) {
            log.error("", e);
        }

        return filterOperatorApproveApps(taskList, operatorApprovedApps);
    }

    private TaskList filterOperatorApproveApps(TaskList taskList, List<String> operatorApprovedApps) {
        List<Task> tasks = new ArrayList<Task>();
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
            returnCall = new Callback().setPayload(graphResponse).setSuccess(true).setMessage(Messages.SUBSCRIPTION_HISTORY_SUCCESS.getValue());
        } else {
            returnCall = new Callback().setPayload(Collections.emptyList()).setSuccess(false).setMessage(Messages.SUBSCRIPTION_HISTORY_FALIED.getValue());
        }

        return returnCall;
    }

    @Override
    protected abstract Callback buildApprovalRequest(ApprovalRequest approvalRequest, UserProfileDTO userProfile) throws BusinessException;

    @Override
    public HistoryResponse getApprovalHistory(String subscriber, String applicationName, int applicationId, String operator, int offset, int count) throws BusinessException {
        return null;
    }

    protected Callback executeTaskApprovalRequest(TaskApprovalRequest approvalRequest, ApprovalRequest request) throws BusinessException {
        ActivityRestClient activityClient = RestClientFactory.getInstance().getClient(getProcessDefinitionKey());
        Callback returnCall;
        try {
            activityClient.approveTask(request.getTaskId(), approvalRequest);
            returnCall = new Callback().setPayload(null).setSuccess(true).setMessage(Messages.SUBSCRIPTION_APPROVAL_SUCCESS.getValue());
        } catch (WorkflowExtensionException e) {
            log.error("", e);
            returnCall = new Callback().setPayload(null).setSuccess(false).setMessage(Messages.SUBSCRIPTION_APPROVAL_FAILED.getValue());
        }
        return returnCall;
    }

    @Override
    protected String getProcessDefinitionKey() {
        return depType.getSubscriptoinProcessType();
    }
}
