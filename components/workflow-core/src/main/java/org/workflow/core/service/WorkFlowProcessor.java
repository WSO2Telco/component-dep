package org.workflow.core.service;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.core.dbutils.util.AppApprovalRequest;
import com.wso2telco.core.dbutils.util.Callback;
import org.workflow.core.activity.ProcessSearchRequest;
import org.workflow.core.model.TaskSerchDTO;

public interface WorkFlowProcessor {
	ProcessSearchRequest buildSearchRequest(final TaskSerchDTO searchDTO,final UserProfileDTO userProfile) throws BusinessException;
	public Callback searchPending(TaskSerchDTO searchDTO, final UserProfileDTO userProfile) throws BusinessException ;
	public Callback getGraphData(final UserProfileDTO userProfile) throws BusinessException;
	public Callback approveApplication(final AppApprovalRequest appApprovalRequest) throws BusinessException;
}
