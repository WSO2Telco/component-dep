package org.workflow.core.service;

import org.workflow.core.model.TaskSerchDTO;
import org.workflow.core.util.WorkFlowType;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.core.dbutils.util.Callback;



public class WorkFlowDelegator {
	public Callback getPendingApplicationApprovals(final TaskSerchDTO serchD,UserProfileDTO userProfile) throws BusinessException {
		WorkFlowProcessor queryBuilder =ActivityProcessFactory
										.getInstance()
										.getWorkFlowFactory(WorkFlowType.APPLICATION)
										.getWorkFlowProcessor();
		
		return queryBuilder.searchPending(serchD, userProfile);
	}
	
	public Callback getPendingSubscriptionApprovals(final TaskSerchDTO serchD,UserProfileDTO userProfile) throws BusinessException {
		WorkFlowProcessor queryBuilder =ActivityProcessFactory
										.getInstance()
										.getWorkFlowFactory(WorkFlowType.SUBSCRIPTION)
										.getWorkFlowProcessor();
		
		return queryBuilder.searchPending(serchD, userProfile);
	}

}
