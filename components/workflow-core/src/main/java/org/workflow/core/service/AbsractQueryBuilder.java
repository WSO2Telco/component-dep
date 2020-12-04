package org.workflow.core.service;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.AssignRequest;
import com.wso2telco.core.dbutils.util.Callback;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.core.userprofile.dto.UserProfileDTO;
import org.apache.commons.logging.Log;
import org.workflow.core.activity.ActivityRestClient;
import org.workflow.core.activity.ProcessSearchRequest;
import org.workflow.core.activity.RestClientFactory;
import org.workflow.core.activity.TaskAssignRequest;
import org.workflow.core.dboperation.DatabaseHandler;
import org.workflow.core.execption.WorkflowExtensionException;
import org.workflow.core.model.*;
import org.workflow.core.util.*;
//import org.wso2.carbon.apimgt.impl.utils.APIUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class AbsractQueryBuilder implements WorkFlowProcessor {

    protected Log log;
    protected DeploymentTypes depType;
    static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
    static final String MONTH_FORMAT = "MMM";
    protected static final String ALL = "__ALL__";
    private DateFormat format = new SimpleDateFormat(WorkFlowVariables.DATE_FORMAT.getValue(), Locale.ENGLISH);
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(WorkFlowVariables.DATE_FORMAT2.getValue());
    private SimpleDateFormat timeFormatter = new SimpleDateFormat(WorkFlowVariables.TIME_FORMAT.getValue());
    private SimpleDateFormat offsetFormatter = new SimpleDateFormat(WorkFlowVariables.OFFSET_FORMAT.getValue());

    protected abstract String getProcessDefinitionKey();

    protected abstract DeploymentTypes getDeployementType();

    protected abstract Callback buildMyTaskResponse(final TaskSearchDTO searchDTO, final TaskList taskList,
                                                    final UserProfileDTO userProfile) throws BusinessException;

    protected abstract Callback buildAllTaskResponse(final TaskSearchDTO searchDTO, final TaskList taskList,
                                                     final UserProfileDTO userProfile) throws BusinessException;

    protected abstract Callback getHistoricalGraphData(String user, List<Range> months, List<String> xAxisLabels) throws BusinessException;

    public abstract HistoryResponse getApprovalHistory(String subscriber, String applicationName, int applicationId, String operator, String status, int offset, int count) throws BusinessException;

    protected abstract Callback buildApprovalRequest(final ApprovalRequest approvalRequest, final UserProfileDTO userProfile) throws BusinessException;

    protected abstract String getCandidateGroup(UserProfileDTO userProfileDTO);

    public Callback searchPending(TaskSearchDTO searchDTO, final UserProfileDTO userProfile, String workflowType) throws BusinessException {
        ProcessSearchRequest processRequest = buildSearchRequest(searchDTO, userProfile);
        processRequest.setCandidateGroup(getCandidateGroup(userProfile));
        TaskList taskList = executeRequest(processRequest, workflowType);
        return buildAllTaskResponse(searchDTO, taskList, userProfile);
    }

    @Override
    public Callback searchPending(TaskSearchDTO searchDTO, UserProfileDTO userProfile, String assigenee, String workflowType) throws BusinessException {
        ProcessSearchRequest processRequest = buildSearchRequest(searchDTO, userProfile);
        processRequest.setAssignee(assigenee);
        TaskList taskList = executeRequest(processRequest,workflowType);
        return buildMyTaskResponse(searchDTO, taskList, userProfile);
    }

    public TaskList executeRequest(ProcessSearchRequest processRequest, String workflowType) throws BusinessException {
        TaskList taskList = null;
        ActivityRestClient activityClient = RestClientFactory.getInstance().getClient(getProcessDefinitionKey());

        try {
            taskList = activityClient.getTasks(processRequest);

            /**
             * temp
             * if(APIUtil.isAdvanceThrottlingEnabled() && workflowType.equals("APPLICATION")){
             * */
	        if(workflowType.equals("APPLICATION")){
                String APiTiers = getTiersFromDB(workflowType);

                for (Task task : taskList.getData()) {
                    TaskVariableResponse[] vars = activityClient.getVariables(String.valueOf(task.getId()));
                    task.setVariables(replaceiActivitiTiers(vars,APiTiers,workflowType));
                }
            }else{
                for (Task task : taskList.getData()) {
                    TaskVariableResponse[] vars = activityClient.getVariables(String.valueOf(task.getId()));
                    task.setVariables(vars);
                }
            }

        } catch (WorkflowExtensionException e) {
            log.error("", e);
            throw new BusinessException(e);
        }

        return taskList;
    }

    @Override
    public ProcessSearchRequest buildSearchRequest(TaskSearchDTO searchDTO, final UserProfileDTO userProfile) throws BusinessException {
        ProcessSearchRequest request = new ProcessSearchRequest();
        request.setSize(searchDTO.getBatchSize());
        request.setStart(searchDTO.getStart());
        request.setSort(searchDTO.getSortBy());
        request.setProcessDefinitionKey(getProcessDefinitionKey());

        String filterStr = searchDTO.getFilterBy();
        /**
         * if the request need to be filtered the string must be formated as
         * filterby:value,filterby2:value
         */
        if (filterStr != null && !filterStr.trim().isEmpty()) {
            /**
             * split the multiple filter criteria by ,
             */
            final String[] filterCriterias = filterStr.split(",");
            for (String criteria : filterCriterias) {
                /**
                 * split the criteria by : to separate out the name and value ,
                 */
                String[] criteriaArray = criteria.split(":");
                /**
                 * validate name and value. Both should not be null. and filer name should be
                 * defined at the filter map .if not ignore adding.
                 */
                if (criteriaArray.length == 2 && !criteriaArray[0].trim().isEmpty() && !criteriaArray[1].trim().isEmpty()
                        && getFilterMap().containsKey(criteriaArray[0].trim().toLowerCase())) {
                    /**
                     * add process variable
                     */
                    Variable var = new Variable(getFilterMap().get(criteriaArray[0].toLowerCase()), criteriaArray[1]);
                    request.addProcessVariable(var);
                }
            }
        }

        return request;
    }

    protected Map<String, String> getFilterMap() {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put("username", AppVariable.USERNAME.key());
        filter.put("name", AppVariable.USERNAME.key());
        filter.put("subscriber", AppVariable.SUBSCRIBER.key());
        filter.put("applicationname", AppVariable.NAME.key());
        filter.put("appname", AppVariable.NAME.key());
        filter.put("applicationid", AppVariable.ID.key());
        filter.put("appid", AppVariable.ID.key());
        filter.put("tier", AppVariable.TIER.key());
        filter.put("apiname", AppVariable.API_NAME.key());
        filter.put("api", AppVariable.API_NAME.key());
        filter.put("status", AppVariable.STATUS.key());
        return filter;
    }

    @Override
    public Callback getGraphData(UserProfileDTO userProfile) throws BusinessException {

        DateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        DateFormat monthFormat = new SimpleDateFormat(MONTH_FORMAT, Locale.ENGLISH);

        List<Range> months = new ArrayList();
        List<String> xAxisLabels = new ArrayList();

        for (int i = -5; i < 1; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, i);
            calendar.set(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Date start = calendar.getTime();

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DATE, 0);
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Date stop = calendar.getTime();

            months.add(new Range().setStart(simpleDateFormat.format(start)).setEnd(simpleDateFormat.format(stop)));
            xAxisLabels.add(monthFormat.format(stop));
        }

        return getHistoricalGraphData(userProfile.getUserName(), months, xAxisLabels);

    }

    @Override
    public Callback approveTask(ApprovalRequest approvalRequest, UserProfileDTO userProfile) throws BusinessException {
        return buildApprovalRequest(approvalRequest, userProfile);
    }

    @Override
    public Callback assignTask(AssignRequest assignRequest, UserProfileDTO userProfile) throws BusinessException {
        TaskAssignRequest request = new TaskAssignRequest();
        request.setAction(WorkFlowVariables.ASSIGN_ACTION.getValue());
        request.setAssignee(userProfile.getUserName().toLowerCase());
        ActivityRestClient activityClient = RestClientFactory.getInstance().getClient(getProcessDefinitionKey());
        boolean success;
        String message;
        try {
            activityClient.assignTask(assignRequest.getTaskId(), request);
            success = true;
            message = Messages.TASK_APPROVAL_SUCCESS.getValue();
        } catch (WorkflowExtensionException e) {
            log.error("", e);
            success = false;
            message = Messages.TASK_APPROVAL_FAILED.getValue();
        }

        return new Callback().setPayload(null).setSuccess(success).setMessage(message);
    }

    public CreateTime getCreatedTime(Task task) {
        CreateTime createTime = new CreateTime();
        try {
            if (task.getCreateTime() != null) {
                Date date = null;
                date = format.parse(task.getCreateTime());
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
        } catch (ParseException e) {
            createTime = null;
        }
        return createTime;
    }

    protected Map<String, String> historyFilterMap() {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put("username", HistoryVariable.SP.key());
        filter.put("user", HistoryVariable.SP.key());
        filter.put("name", HistoryVariable.SP.key());
        filter.put("subscriber", HistoryVariable.SP.key());
        filter.put("sp", HistoryVariable.SP.key());
        filter.put("provider", HistoryVariable.SP.key());
        filter.put("serviceprovider", HistoryVariable.SP.key());
        filter.put("applicationname", HistoryVariable.NAME.key());
        filter.put("application", HistoryVariable.NAME.key());
        filter.put("appname", HistoryVariable.NAME.key());
        filter.put("app", HistoryVariable.NAME.key());
        filter.put("applicationid", HistoryVariable.ID.key());
        filter.put("appid", HistoryVariable.ID.key());
        filter.put("id", HistoryVariable.ID.key());
        filter.put("operator",HistoryVariable.OPARATOR.key());
        filter.put("status", HistoryVariable.STATUS.key());
        filter.put("createdby", HistoryVariable.SP.key());
        return filter;
    }

    protected boolean isAdmin(UserProfileDTO userProfile) {
        String[] userRoles = userProfile.getUserRoles();
        boolean isAdmin = false;
        for (String role : userRoles) {
            if (role.trim().equals(WorkFlowVariables.ADMIN_ROLE.getValue())) {
                isAdmin = true;
            }
        }
        return isAdmin;
    }
    public TaskVariableResponse[] replaceiActivitiTiers(TaskVariableResponse[] vars , String TiersStr , String workflowType) throws  BusinessException {

        if(workflowType.equals(WorkFlowType.APPLICATION.toString())){
            for(int j=0;j< vars.length;j++){
                if(vars[j].getName().equals("tiersStr")){
                    vars[j].setValue(TiersStr);
                }
            }
        }else if(workflowType.equals(WorkFlowType.SUBSCRIPTION.toString())){
            for(int j=0;j< vars.length;j++){
                if(vars[j].getName().equals("apiTiers")){
                    vars[j].setValue(TiersStr);
                }
            }
        }

        return vars;
    }

    public String getTiersFromDB(String workFlowType) throws BusinessException {
        String ApiTiers = null;
        String apimgtDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB);

        DatabaseHandler handler = new DatabaseHandler();
        if(workFlowType.equals(WorkFlowType.APPLICATION.toString()) && apimgtDB != null){
            ApiTiers = handler.getAllApplicationThrottling();
        }else if(workFlowType.equals(WorkFlowType.SUBSCRIPTION.toString()) && apimgtDB != null){
            ApiTiers = handler.getAllSubscriptionThrottling();
        }
        return ApiTiers;
    }
}
