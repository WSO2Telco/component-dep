package org.workflow.core.service;

import java.util.Map;

import org.workflow.core.activity.ProcessSearchRequest;
import org.workflow.core.model.TaskSerchDTO;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;

public interface RequestBuilder {
//	TaskList getApprovePendingTaskList(final TaskSerchDTO serchD,UserProfileDTO userProfile) throws BusinessException;
	Map<String, Object> toMap(TaskSerchDTO dto,UserProfileDTO userProfile) throws BusinessException;
	ProcessSearchRequest buildSearchRequest(final TaskSerchDTO searchDTO,final UserProfileDTO userProfile) throws BusinessException;
	
}
