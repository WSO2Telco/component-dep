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

public class AxiataDAO {

	private static final Log log = LogFactory.getLog(AxiataDAO.class);

	private static volatile DataSource axiataDatasource = null;
	private static final String axiataDataSourceName = "jdbc/AXIATA_MIFE_DB";

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
	 * @param msg
	 * @param t
	 * @throws APIManagementException
	 */
	private static void handleException(String msg, Throwable t)
			throws APIManagementException {
		log.error(msg, t);
		throw new APIManagementException(msg, t);
	}
}
