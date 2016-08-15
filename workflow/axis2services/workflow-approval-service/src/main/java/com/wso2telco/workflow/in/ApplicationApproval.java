package com.wso2telco.workflow.in;

import com.wso2telco.workflow.application.*;

public interface ApplicationApproval {

	void updateDBAppHubApproval(AppHUBApprovalDBUpdateRequestType appHUBApprovalDBUpdateRequest);
	
	void updateDBAppOpApproval(AppOpApprovalDBUpdateRequestType appOpApprovalDBUpdateRequest);
}
