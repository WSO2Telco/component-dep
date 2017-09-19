package com.wso2telco.workflow.service;

import com.wso2telco.workflow.dao.WorkflowHistoryDAO;
import com.wso2telco.workflow.model.ApplicationStatusDTO;
import com.wso2telco.workflow.utils.WorkflowServiceException;

/**
 * Business logic layer for WorkflowHistoryAPI
 */
public class WorkflowHistoryService {

    public ApplicationStatusDTO getAppApprovalHistory(int appID) throws WorkflowServiceException {

        WorkflowHistoryDAO dao = new WorkflowHistoryDAO();
        ApplicationStatusDTO app = null;

        try {
            app = dao.getApplicationStatus(appID);
            dao.getSubscribedAPIs(appID, app);
        } catch (Exception e) {
            throw new WorkflowServiceException(e);
        }

        return app;
    }
}
