/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.workflow.core.service.sub;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.Callback;
import com.wso2telco.core.userprofile.dto.UserProfileDTO;
import org.apache.commons.logging.LogFactory;
import org.workflow.core.activity.TaskApprovalRequest;
import org.workflow.core.model.*;
import org.workflow.core.util.DeploymentTypes;
import org.workflow.core.util.Messages;
import org.workflow.core.util.SubscriptionHistoryVariable;
import org.workflow.core.util.WorkFlowVariables;
import java.text.ParseException;
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
public class IntGtwSubRequestBuilder extends AbstractSubRequestBuilder {

    private static IntGtwSubRequestBuilder instance;

    IntGtwSubRequestBuilder(DeploymentTypes depType) {
        log = LogFactory.getLog(IntGtwSubRequestBuilder.class);
        super.depType = depType;
    }

    public static IntGtwSubRequestBuilder getInstace(DeploymentTypes depType) {
        if (instance == null) {
            instance = new IntGtwSubRequestBuilder(depType);
        }
        return instance;
    }

    @Override
    protected Callback buildApprovalRequest(ApprovalRequest request, UserProfileDTO userProfile) throws BusinessException {
        List<RequestVariable> variables = new ArrayList();
        final String type = "string";

        variables.add(new RequestVariable().setName(WorkFlowVariables.API_PUBLISHER_APPROVAL.getValue()).setValue(request.getStatus()).setType(type));
        variables.add(new RequestVariable().setName(WorkFlowVariables.COMPLETED_BY.getValue()).setValue(userProfile.getUserName()).setType(type));
        variables.add(new RequestVariable().setName(WorkFlowVariables.STATUS.getValue()).setValue(request.getStatus()).setType(type));
        variables.add(new RequestVariable().setName(WorkFlowVariables.COMPLETED_ON.getValue()).setValue(new SimpleDateFormat(WorkFlowVariables.DATE_FORMAT.getValue(), Locale.ENGLISH).format(new Date())).setType(type));
        variables.add(new RequestVariable().setName(WorkFlowVariables.DESCRIPTION.getValue()).setValue(request.getDescription()).setType(type));
        variables.add(new RequestVariable().setName(WorkFlowVariables.SELECTED_TIER.getValue()).setValue(request.getSelectedTier()).setType(type));
//        variables.add(new RequestVariable().setName(WorkFlowVariables.SLECTED_RATE.getValue()).setValue(request.getSelectedRate()).setType(type));

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
    public Callback getSubscriptionHistoryData(TaskSearchDTO searchDTO, UserProfileDTO userProfile) throws BusinessException {

        String filterStr = searchDTO.getFilterBy();
        final Map<String, String> varMap = new HashMap<String, String>();
        Callback returnCall;

        if (filterStr != null && !filterStr.trim().isEmpty()) {
            final String[] filterCriterias = filterStr.split(",");
            for (String criteria : filterCriterias) {
                String[] criteriaArray = criteria.split(":");
                if (criteriaArray.length == 2 && !criteriaArray[0].trim().isEmpty() && !criteriaArray[1].trim().isEmpty()
                        && subHistoryFilterMap().containsKey(criteriaArray[0].trim().toLowerCase())) {
                    varMap.put(subHistoryFilterMap().get(criteriaArray[0].trim().toLowerCase()), criteriaArray[1]);
                }
            }
        }

        int subscriptionId;
        String apiName = ALL;
        String applicationName =ALL;
        String tier = ALL;
        String operator = userProfile.getUserName().toUpperCase();
        String createdBy = ALL;

        if (varMap.containsKey(SubscriptionHistoryVariable.ID.key())) {
            subscriptionId = Integer.parseInt(varMap.get(SubscriptionHistoryVariable.ID.key()));
        }
        else {
            subscriptionId = 0;
        }

        if(varMap.containsKey(SubscriptionHistoryVariable.APINAME.key())){
            apiName = varMap.get(SubscriptionHistoryVariable.APINAME.key());
        }

        if (varMap.containsKey(SubscriptionHistoryVariable.APPNAME.key())) {
            applicationName = varMap.get(SubscriptionHistoryVariable.APPNAME.key());
        }

        if (varMap.containsKey(SubscriptionHistoryVariable.TIER.key())) {
            tier = varMap.get(SubscriptionHistoryVariable.TIER.key());
        }

        if (varMap.containsKey(SubscriptionHistoryVariable.CREATED_BY.key())) {
            createdBy = varMap.get(SubscriptionHistoryVariable.CREATED_BY.key());
        }

        SubscriptionFilter filterObject = new SubscriptionFilter();
        filterObject.setSubscriptionId(subscriptionId);
        filterObject.setApiName(apiName);
        filterObject.setAppName(applicationName);
        filterObject.setTierId(tier);
        filterObject.setCreatedBy(createdBy);

        try {
            SubscriptionHistoryResponse apiRequests = getSubscriptionApprovalHistory(filterObject,operator, searchDTO.getStart(), searchDTO.getBatchSize());
            returnCall = new Callback().setPayload(apiRequests).setSuccess(true).setMessage(Messages.APPLICATION_HISTORY_SUCCESS.getValue());
        } catch (Exception e) {
            returnCall = new Callback().setPayload(e.getMessage()).setSuccess(false).setMessage(Messages.APPLICATION_HISTORY_FAILED.getValue());
        }
        return returnCall;
    }

    @Override
    protected String getCandidateGroup(UserProfileDTO userProfileDTO) {
        if(isSuperAdmin(userProfileDTO)){
            if(userProfileDTO.getDepartment() != null){
                return userProfileDTO.getDepartment();
            }
            return userProfileDTO.getUserName().toLowerCase();
        }else {
            return userProfileDTO.getDepartment();
        }
    }

    @Override
    protected Callback buildAllTaskResponse(TaskSearchDTO searchDTO, TaskList taskList, UserProfileDTO userProfile) throws BusinessException {

        TaskList allTaskList = taskList;
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

        TaskList myTaskList = taskList;
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
