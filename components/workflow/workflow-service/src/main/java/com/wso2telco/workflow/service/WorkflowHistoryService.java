package com.wso2telco.workflow.service;


import java.util.List;

import com.wso2telco.workflow.dao.WorkflowHistoryDAO;
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
        } catch (Exception e) {
            throw new WorkflowServiceException(e);
        }

        return app;
    }
    
    
    public List<APISubscriptionStatusDTO> getAppApprovalHistoryWithOperators(int appID, String opId, String apiid) throws WorkflowServiceException {

        WorkflowHistoryDAO dao = new WorkflowHistoryDAO();
        List<APISubscriptionStatusDTO> apiSubs = null;
        String adjustApiID=null;
        if(apiid!= null && apiid.equals("_ALL"))
        {
        	adjustApiID="%";
        }else{
        	adjustApiID=apiid;
        }
        

        try {
        	
        	if(opId.equalsIgnoreCase("_ALL")){
        		
        		apiSubs=dao.getSubscribedAPIsWithoutOperators(appID, adjustApiID);
        	}else{
        		
        		
        		apiSubs=dao.getSubscribedAPIsWithOperators(appID,Integer.parseInt(opId),adjustApiID);
        	}
        	
            
        } catch (Exception e) {
            throw new WorkflowServiceException(e);
        }

        return apiSubs;
    }
}
