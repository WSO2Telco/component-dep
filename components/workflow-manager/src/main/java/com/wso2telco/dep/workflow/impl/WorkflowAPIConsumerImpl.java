/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.workflow.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;

import com.wso2telco.dep.workflow.interfaces.WorkflowAPIConsumer;

public class WorkflowAPIConsumerImpl implements WorkflowAPIConsumer {
	
	private static final Log log = LogFactory.getLog(WorkflowAPIConsumerImpl.class);

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
