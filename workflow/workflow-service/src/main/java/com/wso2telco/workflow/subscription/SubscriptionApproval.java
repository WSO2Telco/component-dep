package com.wso2telco.workflow.subscription;


import com.wso2telco.workflow.model.Subscription;
import com.wso2telco.workflow.model.SubscriptionValidation;

public interface SubscriptionApproval {

	void updateDBSubHubApproval(Subscription subHUBApprovalDBUpdateRequest) throws Exception;
	
	void updateDBSubOpApproval(Subscription subOpApprovalDBUpdateRequest) throws Exception;
	
	void insertValidatorForSubscription(SubscriptionValidation hUBApprovalSubValidatorRequest)throws Exception;
}
