package com.wso2telco.workflow.subscription;


import com.wso2telco.workflow.model.Subscription;
import com.wso2telco.workflow.model.SubscriptionValidation;

public interface SubscriptionApproval {

	void updateDBSubHubApproval(Subscription subHUBApprovalDBUpdateRequest);
	
	void updateDBSubOpApproval(Subscription subOpApprovalDBUpdateRequest);
	
	void insertValidatorForSubscription(SubscriptionValidation hUBApprovalSubValidatorRequest);
}
