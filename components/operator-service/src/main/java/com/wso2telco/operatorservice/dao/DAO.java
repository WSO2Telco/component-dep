/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.operatorservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.usage.client.exception.APIMgtUsageQueryServiceClientException;

import com.wso2telco.operatorservice.model.Operator;

import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class DAO.
 */
public class DAO {

	/** The Constant log. */
	private static final Log log = LogFactory.getLog(DAO.class);

	/** The axiata datasource. */
	private static volatile DataSource axiataDatasource = null;
	
	/** The Constant axiataDataSourceName. */
	private static final String axiataDataSourceName = "jdbc/AXIATA_MIFE_DB";

	/**
	 * Initialize data source.
	 *
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 */
	public static void initializeDataSource()
			throws APIMgtUsageQueryServiceClientException {
		if (axiataDatasource != null) {
			return;
		}

		if (axiataDataSourceName != null) {
			try {
				Context ctx = new InitialContext();
				axiataDatasource = (DataSource) ctx
						.lookup(axiataDataSourceName);
			} catch (NamingException e) {
				throw new APIMgtUsageQueryServiceClientException(
						"Error while looking up the data " + "source: "
								+ axiataDataSourceName);
			}

		}
	}

	/**
	 * Gets the axiata db connection.
	 *
	 * @return the axiata db connection
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 */
	public static Connection getAxiataDBConnection() throws SQLException,
			APIMgtUsageQueryServiceClientException {

		initializeDataSource();

		if (axiataDatasource != null) {
			return axiataDatasource.getConnection();

		} else {
			throw new SQLException(
					"Axiata Datasource not initialized properly.");
		}
	}

	/**
	 * Retrieve operator list.
	 *
	 * @return the list
	 * @throws APIManagementException the API management exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 */
	public static List<Operator> retrieveOperatorList() throws APIManagementException, APIMgtUsageQueryServiceClientException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Operator> operatorList = new ArrayList<Operator>();
		
		try {
			conn = getAxiataDBConnection();
			String query = "SELECT ID, operatorname, description from operators";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Operator operator = new Operator();
				operator.setOperatorId(rs.getInt("ID"));
				operator.setOperatorName(rs.getString("operatorname"));
				operator.setOperatorDescription(rs.getString("description"));
				
				operatorList.add(operator);
			}

		} catch (SQLException e) {
			handleException(
					"Error in retrieving operator list : "
							+ e.getMessage(), e);
		} finally {
			APIMgtDBUtil.closeAllConnections(ps, conn, rs);
		}
		return operatorList;
	}
	
	/**
	 * Persist operators.
	 *
	 * @param apiName the api name
	 * @param apiVersion the api version
	 * @param apiProvider the api provider
	 * @param appId the app id
	 * @param operatorList the operator list
	 * @throws APIManagementException the API management exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 */
	public static void persistOperators(String apiName, String apiVersion, String apiProvider, int appId, 
			String operatorList) throws APIManagementException, APIMgtUsageQueryServiceClientException {
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getAxiataDBConnection();
			String query = "INSERT INTO sub_approval_operators (API_NAME, API_VERSION, API_PROVIDER, APP_ID, OPERATOR_LIST) "
					+ "VALUES (?, ?, ?, ?, ?)";
			
			ps = conn.prepareStatement(query);
			ps.setString(1, apiName);
			ps.setString(2, apiVersion);
			ps.setString(3, apiProvider);
			ps.setInt(4, appId);
			ps.setString(5, operatorList);
			ps.execute();
			
		} catch (SQLException e) {
			handleException(
					"Error in persisting subscription operator list : "
							+ e.getMessage(), e);
		} finally {
			APIMgtDBUtil.closeAllConnections(ps, conn, null);
		}
	}

	 
	/**
	 * Handle exception.
	 *
	 * @param msg the msg
	 * @param t the t
	 * @throws APIManagementException the API management exception
	 */
	private static void handleException(String msg, Throwable t)
			throws APIManagementException {
		log.error(msg, t);
		throw new APIManagementException(msg, t);
	}
}
