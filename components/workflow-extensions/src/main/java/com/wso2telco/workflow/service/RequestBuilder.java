package com.wso2telco.workflow.service;

import java.util.Map;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.workflow.activityclient.ProcessSearchRequest;
import com.wso2telco.workflow.model.TaskList;
import com.wso2telco.workflow.model.TaskSerchDTO;

public interface RequestBuilder {
//	TaskList getApprovePendingTaskList(final TaskSerchDTO serchD,UserProfileDTO userProfile) throws BusinessException;
	Map<String, Object> toMap(TaskSerchDTO dto,UserProfileDTO userProfile) throws BusinessException;
	ProcessSearchRequest buildSearchRequest(final TaskSerchDTO searchDTO,final UserProfileDTO userProfile) throws BusinessException;
}
