package com.wso2telco.workflow.service;

import java.sql.SQLException;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.workflow.dao.SubscriptionDAO;
import com.wso2telco.workflow.model.SubscriptionEditDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.CarbonConstants;

public class SubscriptionService {

	private static Log LOG = LogFactory.getLog(SubscriptionService.class);
	private static final Log AUDIT_LOG = CarbonConstants.AUDIT_LOG;

	SubscriptionDAO subscriptionDAO;

	{
		subscriptionDAO = new SubscriptionDAO();
	}
	
	public void editSubscriptionTier(SubscriptionEditDTO subscription) throws SQLException, BusinessException {
		String currentTier = subscriptionDAO.getSubscriptionTier(subscription.getApplicationId(), subscription.getApiID());
		subscriptionDAO.editSubscriptionTier(subscription);
		this.editSubscriptionAuditLog(currentTier,subscription);
	}

	private void editSubscriptionAuditLog(String currentTier, SubscriptionEditDTO subscription) {
		String logMessage = "Subscription Updated." +
				" | Application: " + subscription.getApplicationId() + ":" + subscription.getApplicationName() +
				" | API: " + subscription.getApiID() + ":" + subscription.getApiName() + ":" + subscription.getApiVersion() +
				" | Previous Tier: " + currentTier +
				" | Updated Tier: " + subscription.getSubscriptionTier() +
				" | User: " + subscription.getUser();
		AUDIT_LOG.info(logMessage);
		LOG.info(logMessage);
	}
}
