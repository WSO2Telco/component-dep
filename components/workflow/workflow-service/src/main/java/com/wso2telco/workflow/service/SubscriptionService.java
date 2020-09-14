package com.wso2telco.workflow.service;

import java.sql.SQLException;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.workflow.dao.SubscriptionDAO;
import com.wso2telco.workflow.model.SubscriptionEditDTO;

public class SubscriptionService {

	SubscriptionDAO subscriptionDAO;

	{
		subscriptionDAO = new SubscriptionDAO();
	}
	
	public void editSubscriptionTier(SubscriptionEditDTO subscription) throws SQLException, BusinessException {
		
		subscriptionDAO.editSubscriptionTier(subscription);
	}
}