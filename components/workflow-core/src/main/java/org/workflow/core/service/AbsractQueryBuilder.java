package org.workflow.core.service;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.AssignRequest;
import com.wso2telco.core.dbutils.util.Callback;
import org.apache.commons.logging.Log;
import org.workflow.core.activity.*;
import org.workflow.core.execption.WorkflowExtensionException;
import org.workflow.core.model.*;
import org.workflow.core.util.AppVariable;
import org.workflow.core.util.DeploymentTypes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class AbsractQueryBuilder implements WorkFlowProcessor {

    protected Log log;
    protected DeploymentTypes depType;
    final static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";


    protected abstract String getProcessDefinitionKey();

    protected abstract DeploymentTypes getDeployementType();

    protected abstract Callback buildResponse(final TaskSerchDTO searchDTO, final TaskList taskList,
                                              final UserProfileDTO userProfile) throws BusinessException;

    protected abstract Callback getHistoricalData(String user, List<Range> months, List<String> xAxisLabels) throws BusinessException;

    protected abstract Callback buildApprovalRequest(final ApprovalRequest approvalRequest) throws BusinessException;

    public Callback searchPending(TaskSerchDTO searchDTO, final UserProfileDTO userProfile) throws BusinessException {
        ProcessSearchRequest processRequest = buildSearchRequest(searchDTO, userProfile);
        TaskList taskList = null;
        
        RestClient activityClient = ActivityClientFactory.getInstance().getClient(getProcessDefinitionKey());
        try {
            taskList = activityClient.getTasks(processRequest);

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

//		request.setProcessDefinitionKey(getDeployementType().getAppProcessType());

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

    protected Map<String, String> getFilterMap() {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put("name", AppVariable.NAME.key());
        filter.put("applicationname", AppVariable.NAME.key());
        filter.put("appname", AppVariable.NAME.key());
        filter.put("tier", AppVariable.TIER.key());
        filter.put("createdby", AppVariable.USERNAME.key());
        filter.put("owner", AppVariable.USERNAME.key());
        return filter;
    }

    @Override
    public Callback getGraphData(UserProfileDTO userProfile) throws BusinessException {

        DateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
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

        return getHistoricalData(userProfile.getUserName(), months, xAxisLabels);

    }

    @Override
    public Callback approveTask(ApprovalRequest approvalRequest) throws BusinessException {
        return buildApprovalRequest(approvalRequest);
    }

    @Override
    public Callback assignTask(AssignRequest assignRequest) throws BusinessException {
        String assignee = "admin";
        ApplicationAssignRequest request = new ApplicationAssignRequest();
        request.setAction("claim");
        request.setAssignee(assignee.toLowerCase());
        RestClient activityClient = ActivityClientFactory.getInstance().getClient(getProcessDefinitionKey());
        try {
            activityClient.assignTask(assignRequest.getTaskId(), request);
            return new Callback().setPayload(null).setSuccess(true).setMessage("Task Assigned Successfully");
        } catch (WorkflowExtensionException e) {
            log.error("", e);
            return new Callback().setPayload(null).setSuccess(false).setMessage("Task Assigned Not Successfully");
        }
    }
}
