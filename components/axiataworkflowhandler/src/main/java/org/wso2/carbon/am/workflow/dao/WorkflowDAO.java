package org.wso2.carbon.am.workflow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import org.wso2.carbon.apimgt.usage.client.exception.APIMgtUsageQueryServiceClientException;
import org.wso2.carbon.am.axiata.workflow.internal.WorkflowComponent;
import org.wso2.carbon.am.axiata.workflow.model.ApplicationApprovalAuditRecord;
import org.wso2.carbon.am.axiata.workflow.model.SubscriptionApprovalAuditRecord;

/**
 * The DAO class to handle Axiata workflow related tasks.
 */
public class WorkflowDAO {

	private static final Log log = LogFactory.getLog(WorkflowDAO.class);

	private static final String API_USAGE_TRACKING = "APIUsageTracking.";
	private static final String STAT_SOURCE_NAME = API_USAGE_TRACKING
			+ "DataSourceName";
	private static final String axiataDataSourceName = "jdbc/AXIATA_MIFE_DB";
	private static volatile DataSource statDatasource = null;
	private static volatile DataSource axiataDatasource = null;

	public static void initializeDataSource()
			throws APIMgtUsageQueryServiceClientException {
		getStatDataSource();
		getAxiataDataSource();

	}

	public static void getStatDataSource()
			throws APIMgtUsageQueryServiceClientException {
		if (statDatasource != null) {
			return;
		}
		APIManagerConfiguration config = WorkflowComponent
				.getAPIManagerConfiguration();
		String statdataSourceName = config.getFirstProperty(STAT_SOURCE_NAME);

		if (statdataSourceName != null) {
			try {
				Context ctx = new InitialContext();
				statDatasource = (DataSource) ctx.lookup(statdataSourceName);
			} catch (NamingException e) {
				throw new APIMgtUsageQueryServiceClientException(
						"Error while looking up the data " + "source: "
								+ statdataSourceName);
			}

		}
	}

	public static void getAxiataDataSource()
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

	public static Connection getStatsDBConnection() throws SQLException,
			APIMgtUsageQueryServiceClientException {
		initializeDataSource();
		if (statDatasource != null) {
			return statDatasource.getConnection();
		} else {
			throw new SQLException(
					"Statistics Datasource not initialized properly.");
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

	public static void insertAppApprovalAuditRecord(
			ApplicationApprovalAuditRecord record)
			throws APIManagementException,
			APIMgtUsageQueryServiceClientException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getStatsDBConnection();
			String query = "INSERT INTO APP_APPROVAL_AUDIT (APP_NAME, APP_CREATOR, APP_STATUS, "
					+ "APP_APPROVAL_TYPE, COMPLETED_BY_ROLE, COMPLETED_BY_USER) "
					+ "VALUES (?, ?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(query);
			ps.setString(1, record.getAppName());
			ps.setString(2, record.getAppCreator());
			ps.setString(3, record.getAppStatus());
			ps.setString(4, record.getAppApprovalType());
			ps.setString(5, record.getCompletedByRole());
			ps.setString(6, record.getCompletedByUser());
			ps.execute();

		} catch (SQLException e) {
			handleException(
					"Error in inserting application approval audit record : "
							+ e.getMessage(), e);
		} finally {
			APIMgtDBUtil.closeAllConnections(ps, conn, null);
		}
	}

	public static void insertSubApprovalAuditRecord(
			SubscriptionApprovalAuditRecord record)
			throws APIManagementException,
			APIMgtUsageQueryServiceClientException {

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getStatsDBConnection();
			String query = "INSERT INTO SUB_APPROVAL_AUDIT (API_PROVIDER, API_NAME, API_VERSION, APP_ID, "
					+ "SUB_STATUS, SUB_APPROVAL_TYPE, COMPLETED_BY_ROLE, COMPLETED_BY_USER) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(query);
			ps.setString(1, record.getApiProvider());
			ps.setString(2, record.getApiName());
			ps.setString(3, record.getApiVersion());
			ps.setInt(4, record.getAppId());
			ps.setString(5, record.getSubStatus());
			ps.setString(6, record.getSubApprovalType());
			ps.setString(7, record.getCompletedByRole());
			ps.setString(8, record.getCompletedByUser());
			ps.execute();

		} catch (SQLException e) {
			handleException(
					"Error in inserting subscription approval audit record : "
							+ e.getMessage(), e);
		} finally {
			APIMgtDBUtil.closeAllConnections(ps, conn, null);
		}
	}

	public static String getSubApprovalOperators(String apiName,
			String apiVersion, String apiProvider, int appId)
			throws APIManagementException,
			APIMgtUsageQueryServiceClientException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String operators = "";

		try {
			conn = getAxiataDBConnection();
			String query = "SELECT OPERATOR_LIST FROM SUB_APPROVAL_OPERATORS "
					+ "WHERE API_NAME=? AND API_VERSION=? AND API_PROVIDER=? AND APP_ID=? ";
			ps = conn.prepareStatement(query);
			ps.setString(1, apiName);
			ps.setString(2, apiVersion);
			ps.setString(3, apiProvider);
			ps.setInt(4, appId);

			rs = ps.executeQuery();
			if(rs.next()){
				operators = rs.getString("OPERATOR_LIST");
			}

		} catch (SQLException e) {
			handleException(
					"Error in retrieving operator list : " + e.getMessage(), e);
		} finally {
			APIMgtDBUtil.closeAllConnections(ps, conn, rs);
		}

		return operators;
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
