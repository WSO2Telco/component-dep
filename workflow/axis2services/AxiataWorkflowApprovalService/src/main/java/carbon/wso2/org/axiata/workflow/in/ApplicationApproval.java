package carbon.wso2.org.axiata.workflow.in;

import carbon.wso2.org.axiata.workflow.application.*;

public interface ApplicationApproval {

	void updateDBAppHubApproval(AppHUBApprovalDBUpdateRequestType appHUBApprovalDBUpdateRequest);
	
	void updateDBAppOpApproval(AppOpApprovalDBUpdateRequestType appOpApprovalDBUpdateRequest);
}
