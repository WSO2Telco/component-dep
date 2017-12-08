package org.workflow.core.service;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.AssignRequest;
import com.wso2telco.core.dbutils.util.Callback;
import org.workflow.core.activity.ProcessSearchRequest;
import org.workflow.core.model.TaskSearchDTO;

public interface WorkFlowProcessor {
	ProcessSearchRequest buildSearchRequest(final TaskSearchDTO searchDTO, final UserProfileDTO userProfile) throws BusinessException;
	public Callback searchPending(TaskSearchDTO searchDTO, final UserProfileDTO userProfile) throws BusinessException ;
	public Callback searchPending(TaskSearchDTO searchDTO, final UserProfileDTO userProfile, String assigenee) throws BusinessException ;
	public Callback getGraphData(final UserProfileDTO userProfile) throws BusinessException;
	public Callback approveTask(final ApprovalRequest approvalRequest) throws BusinessException;
	public Callback assignTask(final AssignRequest assignRequest) throws BusinessException;
}
