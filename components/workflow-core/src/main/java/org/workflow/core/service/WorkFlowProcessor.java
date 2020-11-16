package org.workflow.core.service;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.AssignRequest;
import com.wso2telco.core.dbutils.util.Callback;
import com.wso2telco.core.userprofile.dto.UserProfileDTO;
import org.workflow.core.activity.ProcessAllTaskSearchRequest;
import org.workflow.core.activity.ProcessSearchRequest;
import org.workflow.core.model.TaskSearchDTO;

public interface WorkFlowProcessor {
	ProcessSearchRequest buildSearchRequest(final TaskSearchDTO searchDTO, final UserProfileDTO userProfile) throws BusinessException;
	ProcessAllTaskSearchRequest buildAllTaskSearchRequest(final TaskSearchDTO searchDTO, final UserProfileDTO userProfile) throws BusinessException;
	public Callback searchPending(TaskSearchDTO searchDTO, final UserProfileDTO userProfile, String workflowType) throws BusinessException ;
	public Callback searchPending(TaskSearchDTO searchDTO, final UserProfileDTO userProfile, String assigenee, String workflowType) throws BusinessException;
	public Callback searchPendingById(String taskId, final UserProfileDTO userProfile, String workflowType) throws BusinessException;
	public Callback getGraphData(final UserProfileDTO userProfile) throws BusinessException;
	public Callback getHistoryData(final TaskSearchDTO searchDTO, final UserProfileDTO userProfile) throws BusinessException;
	public Callback getSubscriptionHistoryData(final TaskSearchDTO searchDTO, final UserProfileDTO userProfile) throws BusinessException;
	public Callback approveTask(final ApprovalRequest approvalRequest, final UserProfileDTO userProfile) throws BusinessException;
	public Callback assignTask(final AssignRequest assignRequest, final UserProfileDTO userProfile) throws BusinessException;
}
