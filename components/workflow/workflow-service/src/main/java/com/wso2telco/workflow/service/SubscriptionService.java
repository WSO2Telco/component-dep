package com.wso2telco.workflow.service;

import java.sql.SQLException;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.workflow.dao.SubscriptionDAO;
import com.wso2telco.workflow.model.SubscriptionEditDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SubscriptionService {


	private static Log log = LogFactory.getLog(SubscriptionService.class);

	SubscriptionDAO subscriptionDAO;

	{
		subscriptionDAO = new SubscriptionDAO();
	}
	
	public void editSubscriptionTier(SubscriptionEditDTO subscription) throws SQLException, BusinessException {

		String previousTier = subscription.getExistingTier();

		subscriptionDAO.editSubscriptionTier(subscription);

		String logEntry = "Subscription tier edited :"
				+ " Completed by - " + subscription.getUser()
				+ ", Application name - " + subscription.getApplicationName()
				+ ", API name - " + subscription.getApiName()
				+ ", Previous tier - "+ previousTier
				+ ", New tier - " + subscription.getSubscriptionTier()
				+ ", Date & Time - " + subscriptionDAO.getUpdatedTime(subscription);

		log.info(logEntry);
	}
}
