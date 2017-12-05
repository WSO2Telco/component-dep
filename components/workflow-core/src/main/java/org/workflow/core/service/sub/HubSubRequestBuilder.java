package org.workflow.core.service.sub;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.Callback;
import org.apache.commons.logging.LogFactory;
import org.workflow.core.activity.ActivityClientFactory;
import org.workflow.core.activity.ApplicationApprovalRequest;
import org.workflow.core.activity.RestClient;
import org.workflow.core.execption.WorkflowExtensionException;
import org.workflow.core.model.RequestVariable;
import org.workflow.core.util.DeploymentTypes;
import org.workflow.core.util.Messages;

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

    HubSubRequestBuilder() {
        super.depType = DeploymentTypes.HUB;
    }

    public static HubSubRequestBuilder getInstace() throws BusinessException {
        if (instance == null) {
            instance = new HubSubRequestBuilder();
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
            variables.add(new RequestVariable().setName("hubAdminApproval").setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName("completedByUser").setValue(user).setType(type));
            variables.add(new RequestVariable().setName("status").setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName("completedOn").setValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH).format(new Date())).setType(type));
            variables.add(new RequestVariable().setName("description").setValue(request.getDescription()).setType(type));
            variables.add(new RequestVariable().setName("selectedTier").setValue(request.getSelectedTier()).setType(type));
            variables.add(new RequestVariable().setName("selectedRate").setValue(request.getSelectedRate()).setType(type));
        } else {
            variables.add(new RequestVariable().setName("operatorAdminApproval").setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName("completedByUser").setValue(user).setType(type));
            variables.add(new RequestVariable().setName("status").setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName("completedOn").setValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH).format(new Date())).setType(type));
            variables.add(new RequestVariable().setName("description").setValue(request.getDescription()).setType(type));
            variables.add(new RequestVariable().setName("selectedRate").setValue(request.getSelectedRate()).setType(type));
        }

        ApplicationApprovalRequest applicationApprovalRequest = new ApplicationApprovalRequest();
        applicationApprovalRequest.setAction("complete");
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
