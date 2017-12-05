package org.workflow.core.service.sub;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.Callback;
import org.apache.commons.logging.LogFactory;
import org.workflow.core.activity.ActivityClientFactory;
import org.workflow.core.activity.TaskApprovalRequest;
import org.workflow.core.activity.RestClient;
import org.workflow.core.execption.WorkflowExtensionException;
import org.workflow.core.model.RequestVariable;
import org.workflow.core.util.DeploymentTypes;
import org.workflow.core.util.Messages;
import org.workflow.core.util.WorkFlowVariables;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class HubSubRequestBuilder extends AbstractSubRequestBuilder {

    private static HubSubRequestBuilder instance;

    {
        log = LogFactory.getLog(HubSubRequestBuilder.class);
    }

    private HubSubRequestBuilder(DeploymentTypes depType) {
        super.depType = depType;
    }

    public static HubSubRequestBuilder getInstace(DeploymentTypes depType) throws BusinessException {
        if (instance == null) {
            instance = new HubSubRequestBuilder(depType);
        }
        return instance;
    }

    @Override
    protected Callback buildApprovalRequest(ApprovalRequest request) throws BusinessException {
        List<RequestVariable> variables = new ArrayList();
        RestClient activityClient = ActivityClientFactory.getInstance().getClient(getProcessDefinitionKey());
        boolean isAdmin = true; //dummy variable
        final String type = "string";
        final String user = "admin";

        if (isAdmin) {
            variables.add(new RequestVariable().setName(WorkFlowVariables.HUB_ADMIN_APPROVAL.getValue()).setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.COMPLETED_BY.getValue()).setValue(user).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.STATUS.getValue()).setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.COMPLETED_ON.getValue()).setValue(new SimpleDateFormat(WorkFlowVariables.DATE_FORMAT.getValue(), Locale.ENGLISH).format(new Date())).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.DESCRIPTION.getValue()).setValue(request.getDescription()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.SELECTGED_TIER.getValue()).setValue(request.getSelectedTier()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.SLECTED_RATE.getValue()).setValue(request.getSelectedRate()).setType(type));
        } else {
            variables.add(new RequestVariable().setName(WorkFlowVariables.OPERATOR_ADMIN_APPROVAL.getValue()).setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.COMPLETED_BY.getValue()).setValue(user).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.STATUS.getValue()).setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.COMPLETED_ON.getValue()).setValue(new SimpleDateFormat(WorkFlowVariables.DATE_FORMAT.getValue(), Locale.ENGLISH).format(new Date())).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.DESCRIPTION.getValue()).setValue(request.getDescription()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.SLECTED_RATE.getValue()).setValue(request.getSelectedRate()).setType(type));
        }

        TaskApprovalRequest applicationApprovalRequest = new TaskApprovalRequest();
        applicationApprovalRequest.setAction(WorkFlowVariables.ACTION.getValue());
        applicationApprovalRequest.setVariables(variables);

        try {
            activityClient.approveTask(request.getTaskId(), applicationApprovalRequest);
            return new Callback().setPayload(null).setSuccess(true).setMessage(Messages.SUBSCRIPTION_APPROVAL_SUCCESS.getValue());
        } catch (WorkflowExtensionException e) {
            log.error("", e);
            return new Callback().setPayload(null).setSuccess(false).setMessage(Messages.SUBSCRIPTION_APPROVAL_FAILED.getValue());
        }
    }

}
