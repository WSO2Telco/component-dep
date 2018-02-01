package org.workflow.core.service.sub;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.Callback;
import com.wso2telco.core.userprofile.dto.UserProfileDTO;
import org.apache.commons.logging.LogFactory;
import org.workflow.core.activity.ActivityRestClient;
import org.workflow.core.activity.RestClientFactory;
import org.workflow.core.activity.TaskApprovalRequest;
import org.workflow.core.model.RequestVariable;
import org.workflow.core.model.SubSearchResponse;
import org.workflow.core.model.TaskList;
import org.workflow.core.model.TaskSearchDTO;
import org.workflow.core.util.DeploymentTypes;
import org.workflow.core.util.Messages;
import org.workflow.core.util.WorkFlowVariables;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class HubSubRequestBuilder extends AbstractSubRequestBuilder {

    private static HubSubRequestBuilder instance;

    private HubSubRequestBuilder(DeploymentTypes depType) {
        log = LogFactory.getLog(HubSubRequestBuilder.class);
        super.depType = depType;
    }

    public static HubSubRequestBuilder getInstace(DeploymentTypes depType) {
        if (instance == null) {
            instance = new HubSubRequestBuilder(depType);
        }
        return instance;
    }

    @Override
    protected Callback buildApprovalRequest(ApprovalRequest request, UserProfileDTO userProfile) throws BusinessException {
        List<RequestVariable> variables = new ArrayList();
        ActivityRestClient activityClient = RestClientFactory.getInstance().getClient(getProcessDefinitionKey());
        final String type = "string";
        boolean success;
        String message;

        if (isAdmin(userProfile)) {
            variables.add(new RequestVariable().setName(WorkFlowVariables.HUB_ADMIN_APPROVAL.getValue()).setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.COMPLETED_BY.getValue()).setValue(userProfile.getUserName()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.STATUS.getValue()).setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.COMPLETED_ON.getValue()).setValue(new SimpleDateFormat(WorkFlowVariables.DATE_FORMAT.getValue(), Locale.ENGLISH).format(new Date())).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.DESCRIPTION.getValue()).setValue(request.getDescription()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.SELECTGED_TIER.getValue()).setValue(request.getSelectedTier()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.SLECTED_RATE.getValue()).setValue(request.getSelectedRate()).setType(type));
        } else {
            variables.add(new RequestVariable().setName(WorkFlowVariables.OPERATOR_ADMIN_APPROVAL.getValue()).setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.COMPLETED_BY.getValue()).setValue(userProfile.getUserName()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.STATUS.getValue()).setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.COMPLETED_ON.getValue()).setValue(new SimpleDateFormat(WorkFlowVariables.DATE_FORMAT.getValue(), Locale.ENGLISH).format(new Date())).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.DESCRIPTION.getValue()).setValue(request.getDescription()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.SLECTED_RATE.getValue()).setValue(request.getSelectedRate()).setType(type));
        }

        TaskApprovalRequest approvalRequest = new TaskApprovalRequest();
        approvalRequest.setAction(WorkFlowVariables.ACTION.getValue());
        approvalRequest.setVariables(variables);

        return executeTaskApprovalRequest(approvalRequest, request);
    }

    @Override
    public Callback getHistoryData(TaskSearchDTO searchDTO, UserProfileDTO userProfile) throws BusinessException {
        return null;
    }

    @Override
    protected String getCandidateGroup(UserProfileDTO userProfileDTO) {
        return userProfileDTO.getUserName();
    }

    @Override
    protected Callback buildAllTaskResponse(TaskSearchDTO searchDTO, TaskList taskList, UserProfileDTO userProfile) throws BusinessException {

        TaskList allTaskList = taskList;
        if (!isAdmin(userProfile) && !taskList.getData().isEmpty()) {
            allTaskList = filterOperatorApprovedApps(taskList, userProfile.getUserName());
        }
        allTaskList = getOperationRates(allTaskList, userProfile);
        SubSearchResponse payload;
        Callback returnCall;
        try {
            payload = generateResponse(allTaskList);
            returnCall = new Callback().setPayload(payload).setSuccess(true).setMessage(Messages.ALL_SUBSCRIPTION_LOAD_SUCCESS.getValue());
        } catch (ParseException e) {
            returnCall = new Callback().setPayload(null).setSuccess(false).setMessage(Messages.ALL_SUBSCRIPTION_LOAD_FAIL.getValue());
        }

        return returnCall;
    }

    @Override
    protected Callback buildMyTaskResponse(TaskSearchDTO searchDTO, TaskList taskList, UserProfileDTO userProfile) throws BusinessException {

        TaskList myTaskList = getOperationRates(taskList, userProfile);
        SubSearchResponse payload;
        Callback returnCall;
        try {
            payload = generateResponse(myTaskList);
            returnCall = new Callback().setPayload(payload).setSuccess(true).setMessage(Messages.MY_SUBSCRIPTION_LOAD_SUCCESS.getValue());
        } catch (ParseException e) {
            returnCall = new Callback().setPayload(null).setSuccess(false).setMessage(Messages.MY_SUBSCRIPTION_LOAD_FAIL.getValue());
        }

        return returnCall;
    }
}
