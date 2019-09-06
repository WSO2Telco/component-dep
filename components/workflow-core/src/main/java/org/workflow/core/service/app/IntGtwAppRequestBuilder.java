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
public class IntGtwAppRequestBuilder extends AbstractAppRequestBuilder {
    private static IntGtwAppRequestBuilder instance;

    private IntGtwAppRequestBuilder(DeploymentTypes depType) {
        log = LogFactory.getLog(AbstractAppRequestBuilder.class);
        super.depType = depType;
    }

    public static IntGtwAppRequestBuilder getInstace(DeploymentTypes depType) {
        if (instance == null) {
            instance = new IntGtwAppRequestBuilder(depType);
        }
        return instance;
    }

    @Override
    protected Callback buildApprovalRequest(ApprovalRequest request, UserProfileDTO userProfile) throws BusinessException {
        List<RequestVariable> variables = new ArrayList();
        final String type = "string";

        variables
                .add(new RequestVariable().setName(WorkFlowVariables.INT_GTW_ADMIN_APPROVAL.getValue()).setValue(request.getStatus()).setType(type));
        variables.add(new RequestVariable().setName(WorkFlowVariables.COMPLETED_BY.getValue()).setValue(userProfile.getUserName()).setType(type));
        variables.add(new RequestVariable().setName(WorkFlowVariables.STATUS.getValue()).setValue(request.getStatus()).setType(type));
        variables.add(new RequestVariable().setName(WorkFlowVariables.COMPLETED_ON.getValue())
                .setValue(new SimpleDateFormat(WorkFlowVariables.DATE_FORMAT.getValue(), Locale.ENGLISH).format(new Date()))
                .setType(type));
        variables
                .add(new RequestVariable().setName(WorkFlowVariables.DESCRIPTION.getValue()).setValue(request.getDescription()).setType(type));
        variables.add(
                new RequestVariable().setName(WorkFlowVariables.SELECTED_TIER.getValue()).setValue(request.getSelectedTier()).setType(type));
        variables.add(
                new RequestVariable().setName(WorkFlowVariables.SELECTGED_TIER.getValue()).setValue(request.getSelectedTier()).setType(type));
        variables.add(new RequestVariable().setName(WorkFlowVariables.CREDIT_PLAN.getValue()).setValue(request.getCreditPlan()).setType(type));


        TaskApprovalRequest approvalRequest = new TaskApprovalRequest();
        approvalRequest.setAction(WorkFlowVariables.ACTION.getValue());
        approvalRequest.setVariables(variables);

        return super.executeTaskApprovalRequest(approvalRequest, request);
    }

    @Override
    public Callback getSubscriptionHistoryData(TaskSearchDTO searchDTO, UserProfileDTO userProfile) throws BusinessException {
        return null;
    }

    @Override
    public Callback getHistoryData(TaskSearchDTO searchDTO, UserProfileDTO userProfile) throws BusinessException {

        final Map<String, String> varMap = new HashMap<String, String>();
        Callback returnCall;
        String filterStr = searchDTO.getFilterBy();

        if (filterStr != null && !filterStr.trim().isEmpty()) {
            final String[] filterCriterias = filterStr.split(",");
            for (String criteria : filterCriterias) {
                String[] criteriaArray = criteria.split(":");
                if (criteriaArray.length == 2 && !criteriaArray[0].trim().isEmpty() && !criteriaArray[1].trim().isEmpty()
                        && historyFilterMap().containsKey(criteriaArray[0].trim().toLowerCase())) {
                    varMap.put(historyFilterMap().get(criteriaArray[0].trim().toLowerCase()), criteriaArray[1]);
                }
            }
        }

        String subscriber = ALL;
        String operator = ALL;
        String status = ALL;
        String createdBy = ALL;
        int applicationId;
        String applicationName =ALL;


        if(varMap.containsKey(HistoryVariable.NAME.key())){
            applicationName = varMap.get(HistoryVariable.NAME.key());
        }

        if (varMap.containsKey(HistoryVariable.SP.key())) {
            subscriber = varMap.get(HistoryVariable.SP.key());
        }

        if(varMap.containsKey(HistoryVariable.ID.key())) {
            applicationId = Integer.parseInt(varMap.get(HistoryVariable.ID.key()));
        }else {
            applicationId = 0;
        }

        if (varMap.containsKey(HistoryVariable.STATUS.key())) {
            status = varMap.get(HistoryVariable.STATUS.key());
        }

        if(!isAdmin(userProfile)){
            operator = userProfile.getUserName().toUpperCase();
        }else if(varMap.containsKey(HistoryVariable.OPARATOR.key())){
            operator = varMap.get(HistoryVariable.OPARATOR.key());
        }

        if (varMap.containsKey(HistoryVariable.CREATED_BY.key())) {
            createdBy = varMap.get(HistoryVariable.CREATED_BY.key());
        }

        try {
            HistoryResponse apiRequests = getApprovalHistoryWithPendingJobs( subscriber, applicationName, applicationId, createdBy, status, searchDTO.getStart(), searchDTO.getBatchSize());
            returnCall = new Callback().setPayload(apiRequests).setSuccess(true).setMessage(Messages.APPLICATION_HISTORY_SUCCESS.getValue());
        } catch (Exception e) {
            returnCall = new Callback().setPayload(e.getMessage()).setSuccess(false).setMessage(Messages.APPLICATION_HISTORY_FAILED.getValue());
        }
        return returnCall;
    }
}
