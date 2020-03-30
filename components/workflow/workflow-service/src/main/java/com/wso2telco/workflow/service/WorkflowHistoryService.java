package com.wso2telco.workflow.service;


import java.util.List;

import com.wso2telco.workflow.dao.WorkflowHistoryDAO;
import com.wso2telco.workflow.model.APISubscriptionDTO;
import com.wso2telco.workflow.model.APISubscriptionStatusDTO;
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
			dao.setSubscribedAPIsWithoutOperators(appID, app);
		} catch (Exception e) {
			throw new WorkflowServiceException(e);
		}

		return app;
	}


	public List<APISubscriptionDTO> getAppApprovalHistoryWithOperators(int appID, String opId, String apiid ,int start, int size) throws WorkflowServiceException {

		WorkflowHistoryDAO dao = new WorkflowHistoryDAO();
		List<APISubscriptionDTO> apiSubs = null;
		try {

			if(opId.equalsIgnoreCase("_ALL")){

				apiSubs=dao.getSubscribedAPIsWithoutOperators(appID, apiid,start,size);
			}else{


				apiSubs=dao.getSubscribedAPIsWithOperators(appID,Integer.parseInt(opId),apiid,start,size);
			}


		} catch (Exception e) {
			throw new WorkflowServiceException(e);
		}

		return apiSubs;
	}
}
