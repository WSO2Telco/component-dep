package org.workflow.core.service.app;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.Callback;
import com.wso2telco.core.userprofile.dto.UserProfileDTO;
import org.apache.commons.logging.LogFactory;
import org.workflow.core.activity.TaskApprovalRequest;
import org.workflow.core.model.HistoryResponse;
import org.workflow.core.model.RequestVariable;
import org.workflow.core.model.TaskSearchDTO;
import org.workflow.core.util.DeploymentTypes;
import org.workflow.core.util.HistoryVariable;
import org.workflow.core.util.Messages;
import org.workflow.core.util.WorkFlowVariables;

import java.text.SimpleDateFormat;
import java.util.*;

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

    private HubAppRequestBuilder(DeploymentTypes depType) {
        log = LogFactory.getLog(AbstractAppRequestBuilder.class);
        super.depType = depType;
    }

    public static HubAppRequestBuilder getInstace(DeploymentTypes depType) {
        if (instance == null) {
            instance = new HubAppRequestBuilder(depType);
        }
        return instance;
    }

    @Override
    protected Callback buildApprovalRequest(ApprovalRequest request, UserProfileDTO userProfile) throws BusinessException {
        List<RequestVariable> variables = new ArrayList();
        final String type = "string";

        if (isAdmin(userProfile)) {
            variables
                    .add(new RequestVariable().setName(WorkFlowVariables.HUB_ADMIN_APPROVAL.getValue()).setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.COMPLETED_BY.getValue()).setValue(userProfile.getUserName()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.STATUS.getValue()).setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.COMPLETED_ON.getValue())
                    .setValue(new SimpleDateFormat(WorkFlowVariables.DATE_FORMAT.getValue(), Locale.ENGLISH).format(new Date()))
                    .setType(type));
            variables
                    .add(new RequestVariable().setName(WorkFlowVariables.DESCRIPTION.getValue()).setValue(request.getDescription()).setType(type));
            variables.add(
                    new RequestVariable().setName(WorkFlowVariables.SELECTGED_TIER.getValue()).setValue(request.getSelectedTier()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.CREDIT_PLAN.getValue()).setValue(request.getCreditPlan()).setType(type));
        } else {
            variables.add(
                    new RequestVariable().setName(WorkFlowVariables.OPERATOR_ADMIN_APPROVAL.getValue()).setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.COMPLETED_BY.getValue()).setValue(userProfile.getUserName()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.STATUS.getValue()).setValue(request.getStatus()).setType(type));
            variables.add(new RequestVariable().setName(WorkFlowVariables.COMPLETED_ON.getValue())
                    .setValue(new SimpleDateFormat(WorkFlowVariables.DATE_FORMAT.getValue(), Locale.ENGLISH).format(new Date()))
                    .setType(type));
            variables
                    .add(new RequestVariable().setName(WorkFlowVariables.DESCRIPTION.getValue()).setValue(request.getDescription()).setType(type));
        }

        TaskApprovalRequest approvalRequest = new TaskApprovalRequest();
        approvalRequest.setAction(WorkFlowVariables.ACTION.getValue());
        approvalRequest.setVariables(variables);

        return executeTaskApprovalRequest(approvalRequest, request);
    }
}
