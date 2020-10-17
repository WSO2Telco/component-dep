package com.wso2telco.workflow.service;

import java.sql.SQLException;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.workflow.dao.ApplicationDAO;
import com.wso2telco.workflow.dao.SubscriptionDAO;
import com.wso2telco.workflow.model.ApplicationEditDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ApplicationService {

	private static Log log = LogFactory.getLog(ApplicationService.class);

	ApplicationDAO applicationDAO;

	{
		applicationDAO = new ApplicationDAO();
	}
	
	public void editApplicationTier(ApplicationEditDTO application) throws SQLException, BusinessException {

		String previousTier =application.getExistingTier();

		applicationDAO.editApplicationTier(application);

		String logEntry = "Application tier edited :"
				+ " Completed by - " + application.getUser()
				+ ", Application name - " + application.getApplicationName()
				+ ", Previous tier - " + previousTier
				+ ", New tier - " + application.getApplicationTier()
				+ ", Date & Time - " + applicationDAO.getUpdatedTime(application);

		log.info(logEntry);


	}
}
