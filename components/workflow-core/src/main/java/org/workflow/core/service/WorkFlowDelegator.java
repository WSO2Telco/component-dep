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
package org.workflow.core.service;

import org.workflow.core.model.TaskSearchDTO;
import org.workflow.core.util.WorkFlowType;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.AssignRequest;
import com.wso2telco.core.dbutils.util.Callback;
import com.wso2telco.core.userprofile.dto.UserProfileDTO;


public class WorkFlowDelegator {

    public Callback getPendingApplicationApprovals(final TaskSearchDTO serchD, UserProfileDTO userProfile) throws BusinessException {
        WorkFlowProcessor queryBuilder =ActivityProcessFactory.getInstance().getWorkFlowFactory(WorkFlowType.APPLICATION).getWorkFlowProcessor();
        return queryBuilder.searchPending(serchD, userProfile, "APPLICATION");
    }

    public Callback getPendingApplicationApproval(final String taskId, UserProfileDTO userProfile) throws BusinessException {
        WorkFlowProcessor queryBuilder =ActivityProcessFactory.getInstance().getWorkFlowFactory(WorkFlowType.APPLICATION).getWorkFlowProcessor();
        return queryBuilder.searchPendingById(taskId, userProfile, "APPLICATION");
    }

    public Callback getPendingAssignedApplicationApprovals(final TaskSearchDTO serchD, UserProfileDTO userProfile, String assignee) throws BusinessException {
        WorkFlowProcessor queryBuilder =ActivityProcessFactory.getInstance().getWorkFlowFactory(WorkFlowType.APPLICATION).getWorkFlowProcessor();
        return queryBuilder.searchPending(serchD, userProfile,assignee,"APPLICATION");
    }

    public Callback getPendingSubscriptionApprovals(final TaskSearchDTO serchD, UserProfileDTO userProfile) throws BusinessException {
        WorkFlowProcessor queryBuilder =ActivityProcessFactory.getInstance().getWorkFlowFactory(WorkFlowType.SUBSCRIPTION).getWorkFlowProcessor();
        return queryBuilder.searchPending(serchD, userProfile,"SUBSCRIPTION");
    }

    public Callback getPendingAssignedSubscriptionApprovals(final TaskSearchDTO serchD, UserProfileDTO userProfile, String assignee) throws BusinessException {
        WorkFlowProcessor queryBuilder =ActivityProcessFactory.getInstance().getWorkFlowFactory(WorkFlowType.SUBSCRIPTION).getWorkFlowProcessor();
        return queryBuilder.searchPending(serchD, userProfile,assignee,"SUBSCRIPTION");
    }

    public Callback getApplicationGraphData(UserProfileDTO userProfile) throws BusinessException {
        WorkFlowProcessor queryBuilder = ActivityProcessFactory
                .getInstance()
                .getWorkFlowFactory(WorkFlowType.APPLICATION)
                .getWorkFlowProcessor();

        return queryBuilder.getGraphData(userProfile);
    }

    public Callback getSubscriptionGraphData(UserProfileDTO userProfile) throws BusinessException {
        WorkFlowProcessor queryBuilder = ActivityProcessFactory
                .getInstance()
                .getWorkFlowFactory(WorkFlowType.SUBSCRIPTION)
                .getWorkFlowProcessor();

        return queryBuilder.getGraphData(userProfile);
    }

    public Callback getApplicationApprovalHistory(final TaskSearchDTO serchD, UserProfileDTO userProfile) throws BusinessException {
        WorkFlowProcessor queryBuilder = ActivityProcessFactory
                .getInstance()
                .getWorkFlowFactory(WorkFlowType.APPLICATION)
                .getWorkFlowProcessor();

        return queryBuilder.getHistoryData(serchD, userProfile);
    }

    public Callback getSubscriptionApprovalHistory(final TaskSearchDTO serchD, UserProfileDTO userProfile) throws BusinessException {
        WorkFlowProcessor queryBuilder = ActivityProcessFactory
                .getInstance()
                .getWorkFlowFactory(WorkFlowType.SUBSCRIPTION)
                .getWorkFlowProcessor();

        return queryBuilder.getSubscriptionHistoryData(serchD, userProfile);
    }

    public Callback approveApplication(ApprovalRequest approvalRequest, UserProfileDTO userProfile)throws BusinessException {
        WorkFlowProcessor queryBuilder = ActivityProcessFactory
                .getInstance()
                .getWorkFlowFactory(WorkFlowType.APPLICATION)
                .getWorkFlowProcessor();

        return queryBuilder.approveTask(approvalRequest, userProfile);
    }

    public Callback approveSubscription(ApprovalRequest approvalRequest, UserProfileDTO userProfile)throws BusinessException {
        WorkFlowProcessor queryBuilder = ActivityProcessFactory
                .getInstance()
                .getWorkFlowFactory(WorkFlowType.SUBSCRIPTION)
                .getWorkFlowProcessor();

        return queryBuilder.approveTask(approvalRequest, userProfile);
    }

    public Callback assignApplicationTask(AssignRequest assignRequest, UserProfileDTO userProfile)throws BusinessException {
        WorkFlowProcessor queryBuilder = ActivityProcessFactory
                .getInstance()
                .getWorkFlowFactory(WorkFlowType.APPLICATION)
                .getWorkFlowProcessor();

        return queryBuilder.assignTask(assignRequest, userProfile);
    }

    public Callback assignSubscriptionTask(AssignRequest assignRequest, UserProfileDTO userProfile)throws BusinessException {
        WorkFlowProcessor queryBuilder = ActivityProcessFactory
                .getInstance()
                .getWorkFlowFactory(WorkFlowType.SUBSCRIPTION)
                .getWorkFlowProcessor();

        return queryBuilder.assignTask(assignRequest, userProfile);
    }

}
