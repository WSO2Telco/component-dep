package org.workflow.core.service;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.AssignRequest;
import com.wso2telco.core.dbutils.util.Callback;
import org.workflow.core.model.TaskSearchDTO;
import org.workflow.core.util.WorkFlowType;


public class WorkFlowDelegator {

    public Callback getPendingApplicationApprovals(final TaskSearchDTO serchD, UserProfileDTO userProfile) throws BusinessException {
        WorkFlowProcessor queryBuilder =ActivityProcessFactory.getInstance().getWorkFlowFactory(WorkFlowType.APPLICATION).getWorkFlowProcessor();
        return queryBuilder.searchPending(serchD, userProfile);
    }

    public Callback getPendingSubscriptionApprovals(final TaskSearchDTO serchD, UserProfileDTO userProfile) throws BusinessException {
        WorkFlowProcessor queryBuilder =ActivityProcessFactory.getInstance().getWorkFlowFactory(WorkFlowType.SUBSCRIPTION).getWorkFlowProcessor();
        return queryBuilder.searchPending(serchD, userProfile);
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

    public Callback approveApplication(ApprovalRequest approvalRequest)throws BusinessException {
        WorkFlowProcessor queryBuilder = ActivityProcessFactory
                .getInstance()
                .getWorkFlowFactory(WorkFlowType.APPLICATION)
                .getWorkFlowProcessor();

        return queryBuilder.approveTask(approvalRequest);
    }

    public Callback approveSubscription(ApprovalRequest approvalRequest)throws BusinessException {
        WorkFlowProcessor queryBuilder = ActivityProcessFactory
                .getInstance()
                .getWorkFlowFactory(WorkFlowType.SUBSCRIPTION)
                .getWorkFlowProcessor();

        return queryBuilder.approveTask(approvalRequest);
    }

    public Callback assignApplicationTask(AssignRequest assignRequest)throws BusinessException {
        WorkFlowProcessor queryBuilder = ActivityProcessFactory
                .getInstance()
                .getWorkFlowFactory(WorkFlowType.APPLICATION)
                .getWorkFlowProcessor();

        return queryBuilder.assignTask(assignRequest);
    }

    public Callback assignSubscriptionTask(AssignRequest assignRequest)throws BusinessException {
        WorkFlowProcessor queryBuilder = ActivityProcessFactory
                .getInstance()
                .getWorkFlowFactory(WorkFlowType.SUBSCRIPTION)
                .getWorkFlowProcessor();

        return queryBuilder.assignTask(assignRequest);
    }

}
