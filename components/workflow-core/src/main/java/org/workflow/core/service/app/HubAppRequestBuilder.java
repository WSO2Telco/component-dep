package org.workflow.core.service.app;

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

/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class HubAppRequestBuilder extends AbstractAppRequestBuilder {

    private static HubAppRequestBuilder instance;

    {
        log = LogFactory.getLog(AbstractAppRequestBuilder.class);
    }

    private HubAppRequestBuilder(DeploymentTypes depType) throws BusinessException {
        super.depType = depType;
    }

    public static HubAppRequestBuilder getInstace(DeploymentTypes depType) throws BusinessException {
        if (instance == null) {
            instance = new HubAppRequestBuilder(depType);
        }
        return instance;
    }

    @Override
    protected Callback buildApprovalRequest(ApprovalRequest request) throws BusinessException {
        List<RequestVariable> variables = new ArrayList();
        RestClient activityClient = ActivityClientFactory.getInstance().getClient(getProcessDefinitionKey());
        boolean isAdmin = true; // dummy variable
        final String type = "string";
        final String user = "admin";

        if (isAdmin) {
            variables
                    .add(new RequestVariable().setName("hubAdminApproval").setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName("completedByUser").setValue(user).setType(type));
            variables.add(new RequestVariable().setName("status").setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName("completedOn")
                    .setValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH).format(new Date()))
                    .setType(type));
            variables
                    .add(new RequestVariable().setName("description").setValue(request.getDescription()).setType(type));
            variables.add(
                    new RequestVariable().setName("selectedTier").setValue(request.getSelectedTier()).setType(type));
            variables.add(new RequestVariable().setName("creditPlan").setValue(request.getCreditPlan()).setType(type));
        } else {
            variables.add(
                    new RequestVariable().setName("operatorAdminApproval").setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName("completedByUser").setValue(user).setType(type));
            variables.add(new RequestVariable().setName("status").setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName("completedOn")
                    .setValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH).format(new Date()))
                    .setType(type));
            variables
                    .add(new RequestVariable().setName("description").setValue(request.getDescription()).setType(type));
        }

        ApplicationApprovalRequest applicationApprovalRequest = new ApplicationApprovalRequest();
        applicationApprovalRequest.setAction("complete");
        applicationApprovalRequest.setVariables(variables);

        try {
            activityClient.approveTask(request.getTaskId(), applicationApprovalRequest);
            return new Callback().setPayload(null).setSuccess(true).setMessage(Messages.APPLICATION_APPROVAL_SUCCESS.getValue());
        } catch (WorkflowExtensionException e) {
            log.error("", e);
            return new Callback().setPayload(null).setSuccess(false).setMessage(Messages.APPLICATION_APPROVAL_FAILED.getValue());
        }
    }
}
