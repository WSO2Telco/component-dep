package org.wso2.carbon.am.axiata.workflow.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.wso2.carbon.am.axiata.workflow.interfaces.AxiataAPIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;

public class AxiataAPIConsumerImpl implements AxiataAPIConsumer {
	
	private static final Log log = LogFactory.getLog(AxiataAPIConsumerImpl.class);

	@Override
	public int getAPIID(APIIdentifier apiId) throws APIManagementException {
		int id = -1;
		Connection connection;
		
		try {
			connection = APIMgtDBUtil.getConnection();
			id = ApiMgtDAO.getAPIID(apiId, connection);

		} catch (SQLException e) {
			handleException("Failed to retrieve api id. ", e);
		} catch (APIManagementException e) {
			handleException("Error occured in retrieving api id. ", e);
		}
		return id;
	}

	private static void handleException(String msg, Throwable t) throws APIManagementException {
		log.error(msg, t);
		throw new APIManagementException(msg, t);
	}

}
