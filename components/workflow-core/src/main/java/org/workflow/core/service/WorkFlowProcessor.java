package org.workflow.core.service;

import org.workflow.core.activity.ProcessSearchRequest;
import org.workflow.core.model.TaskSearchDTO;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.AssignRequest;
import com.wso2telco.core.dbutils.util.Callback;
import com.wso2telco.core.userprofile.dto.UserProfileDTO;

public interface WorkFlowProcessor {
	ProcessSearchRequest buildSearchRequest(final TaskSearchDTO searchDTO, final UserProfileDTO userProfile) throws BusinessException;
	public Callback searchPending(TaskSearchDTO searchDTO, final UserProfileDTO userProfile) throws BusinessException ;
	public Callback getGraphData(final UserProfileDTO userProfile) throws BusinessException;
	public Callback approveTask(final ApprovalRequest approvalRequest) throws BusinessException;
	public Callback assignTask(final AssignRequest assignRequest) throws BusinessException;
}
