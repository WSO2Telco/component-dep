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

    public Callback getPendingSubscriptionApproval(final String taskId, UserProfileDTO userProfile) throws BusinessException {
        WorkFlowProcessor queryBuilder =ActivityProcessFactory.getInstance().getWorkFlowFactory(WorkFlowType.SUBSCRIPTION).getWorkFlowProcessor();
        return queryBuilder.searchPendingById(taskId, userProfile, "SUBSCRIPTION");
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
