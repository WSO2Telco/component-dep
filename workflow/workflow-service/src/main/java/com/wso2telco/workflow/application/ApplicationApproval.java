package com.wso2telco.workflow.application;

import com.wso2telco.workflow.model.Application;

public interface ApplicationApproval {

	void updateDBAppHubApproval(Application application) throws Exception;
	
	void updateDBAppOpApproval(Application application) throws Exception;
}
