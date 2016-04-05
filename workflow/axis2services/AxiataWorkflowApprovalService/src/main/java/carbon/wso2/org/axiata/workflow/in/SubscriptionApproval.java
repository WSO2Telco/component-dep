package carbon.wso2.org.axiata.workflow.in;

import carbon.wso2.org.axiata.workflow.subscription.*;

public interface SubscriptionApproval {

	void updateDBSubHubApproval(SubHUBApprovalDBUpdateRequestType subHUBApprovalDBUpdateRequest);
	
	void updateDBSubOpApproval(SubOpApprovalDBUpdateRequestType subOpApprovalDBUpdateRequest);
	
	SubOperatorRetrievalResponse retrieveSubOperatorList(SubOperatorRetrievalRequestType subOperatorRetrievalRequest);
	
	void insertValidatorForSubscription(HUBApprovalSubValidatorRequestType hUBApprovalSubValidatorRequest);
}
