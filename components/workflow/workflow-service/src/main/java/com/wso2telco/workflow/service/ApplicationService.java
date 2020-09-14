
package com.wso2telco.workflow.service;

import java.sql.SQLException;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.workflow.dao.ApplicationDAO;
import com.wso2telco.workflow.model.ApplicationEditDTO;

public class ApplicationService {

	ApplicationDAO applicationDAO;

	{
		applicationDAO = new ApplicationDAO();
	}
	
	public void editApplicationTier(ApplicationEditDTO application) throws SQLException, BusinessException {
		
		applicationDAO.editApplicationTier(application);
	}
}