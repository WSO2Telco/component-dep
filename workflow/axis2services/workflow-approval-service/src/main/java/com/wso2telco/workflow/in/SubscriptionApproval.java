package com.wso2telco.workflow.in;

import com.wso2telco.workflow.subscription.*;

public interface SubscriptionApproval {

	void updateDBSubHubApproval(SubHUBApprovalDBUpdateRequestType subHUBApprovalDBUpdateRequest);
	
	void updateDBSubOpApproval(SubOpApprovalDBUpdateRequestType subOpApprovalDBUpdateRequest);
	
	SubOperatorRetrievalResponse retrieveSubOperatorList(SubOperatorRetrievalRequestType subOperatorRetrievalRequest);
	
	void insertValidatorForSubscription(HUBApprovalSubValidatorRequestType hUBApprovalSubValidatorRequest);
}
