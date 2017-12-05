package org.workflow.core.service.sub;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.Callback;
import org.workflow.core.activity.ActivityClientFactory;
import org.workflow.core.activity.RestClient;
import org.workflow.core.model.*;
import org.workflow.core.service.AbsractQueryBuilder;
import org.workflow.core.service.ReturnableResponse;
import org.workflow.core.util.AppVariable;
import org.workflow.core.util.DeploymentTypes;
import org.workflow.core.util.Messages;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

abstract class AbstractSubRequestBuilder extends AbsractQueryBuilder {

    private static final String GRAPH_LABEL = "SUBSCRIPTIONS";

    private ReturnableResponse generateResponse(final TaskSerchDTO searchDTO, final TaskList taskList, final UserProfileDTO userProfile) throws ParseException {

        return new ReturnableResponse() {

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH);

            @Override
            public int getTotal() {
                return taskList.getTotal();
            }

            @Override
            public int getStrat() {
                return taskList.getStart();
            }

            @Override
            public int getBatchSize() {
                return taskList.getSize();
            }

            @Override
            public String getFilterBy() {
                return searchDTO.getFilterBy();
            }

            @Override
            public String getOrderBy() {
                return searchDTO.getOrderBy();
            }

            @Override
            public List<ReturnableTaskResponse> getTasks() {
                List<ReturnableTaskResponse> temptaskList = new ArrayList<ReturnableResponse.ReturnableTaskResponse>();

                for (final Task task : taskList.getData()) {
                    final Map<AppVariable, TaskVariableResponse> varMap = new HashMap<AppVariable, TaskVariableResponse>();
                    for (final TaskVariableResponse var : task.getVars()) {
                        varMap.put(AppVariable.getByKey(var.getName()), var);
                    }

                    ReturnableTaskResponse responseTask = new ReturnableTaskResponse() {
                        /**
                         * return task ID
                         */
                        public int getID() {
                            return task.getId();
                        }

                        public String getName() {
                            return varMap.get(AppVariable.NAME).getValue();
                        }

                        public String getDescription() {
                            return varMap.get(AppVariable.DESCRIPTION).getValue();
                        }

                        public String getCreatedDate() {
                            return format.format(task.getCreateTime());
                        }

                        public String getTier() {
                            return varMap.get(AppVariable.TIER).getValue();
                        }

                        public String getAssinee() {
                            return task.getAssignee();
                        }
                    };
                    temptaskList.add(responseTask);
                }
                return temptaskList;
            }
        };
    }

    @Override
    protected Callback buildResponse(TaskSerchDTO searchDTO, TaskList taskList, UserProfileDTO userProfile)
            throws BusinessException {
        ReturnableResponse payload;
        Callback returnCall;
        try {
            payload = generateResponse(searchDTO, taskList, userProfile);
            returnCall = new Callback().setPayload(payload)
                    .setSuccess(true)
                    .setMessage("Subscription Taks listed success ");
        } catch (ParseException e) {
            returnCall = new Callback().setPayload(null)
                    .setSuccess(false)
                    .setMessage("Subscription Taks listed fail ");
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
        RestClient activityClient = ActivityClientFactory.getInstance().getClient(getProcessDefinitionKey());
        TaskDetailsResponse taskList = null;

        for (Range month : months) {
            taskList = activityClient.getHistoricTasks(month.getStart(), month.getEnd(), getProcessDefinitionKey(), user);
            data.add(taskList.getTotal());
        }

        if (!data.isEmpty()) {
            GraphData graphData = new GraphData();
            graphData.setData(data);
            graphData.setLabel("subscriptions".toUpperCase());
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
    protected abstract Callback buildApprovalRequest(ApprovalRequest approvalRequest) throws BusinessException;

    @Override
    protected String getProcessDefinitionKey() {
        return null;
    }
}
