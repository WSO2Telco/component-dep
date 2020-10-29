
package com.wso2telco.workflow.service;

import java.sql.SQLException;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.workflow.dao.ApplicationDAO;
import com.wso2telco.workflow.model.ApplicationEditDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.CarbonConstants;

public class ApplicationService {

	private static Log LOG = LogFactory.getLog(ApplicationService.class);
	private static final Log AUDIT_LOG = CarbonConstants.AUDIT_LOG;

	ApplicationDAO applicationDAO;

	{
		applicationDAO = new ApplicationDAO();
	}
	
	public void editApplicationTier(ApplicationEditDTO application) throws SQLException, BusinessException {
		String currentTier = applicationDAO.getApplicationTier(application.getApplicationId());
		applicationDAO.editApplicationTier(application);

		editApplicationLog(currentTier,application);
		editApplicationAuditLog(currentTier, application);
	}

	private void editApplicationLog(String currentTier, ApplicationEditDTO application) {
		String logMessage = "Application tier edited :"
				+ " Completed by - " + application.getUser()
				+ ", Application name - " + application.getApplicationName()
				+ ", Previous tier - " + currentTier
				+ ", New tier - " + application.getApplicationTier();
		LOG.info(logMessage);
	}

	private void editApplicationAuditLog(String currentTier, ApplicationEditDTO application) {
		String logMessage = "Application Updated." +
				" | Application: " + application.getApplicationId() + ":" + currentTier +
				" | SP: " + application.getServiceProvider() +
				" | Previous Tier: " + currentTier +
				" | Updated Tier: " + application.getApplicationTier() +
				" | User: " + application.getUser();
		AUDIT_LOG.info(logMessage);
		LOG.info(logMessage);
	}
}