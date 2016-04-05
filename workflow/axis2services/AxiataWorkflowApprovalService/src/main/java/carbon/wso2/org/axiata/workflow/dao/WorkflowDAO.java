package carbon.wso2.org.axiata.workflow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import carbon.wso2.org.axiata.workflow.model.ApplicationApprovalAuditRecord;
import carbon.wso2.org.axiata.workflow.model.SubscriptionApprovalAuditRecord;
import carbon.wso2.org.axiata.workflow.util.WorkflowDBUtil;

/**
 * The DAO class to handle Axiata workflow related tasks.
 */
public class WorkflowDAO {

	private static final Log log = LogFactory.getLog(WorkflowDAO.class);

	public static void insertAppApprovalAuditRecord(
			ApplicationApprovalAuditRecord record) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = WorkflowDBUtil.getStatsDBConnection();
			String query = "INSERT INTO app_approval_audit (APP_NAME, APP_CREATOR, APP_STATUS, "
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
			WorkflowDBUtil.closeAllConnections(ps, conn, null);
		}
	}

	public static void insertSubApprovalAuditRecord(
			SubscriptionApprovalAuditRecord record) throws Exception {

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = WorkflowDBUtil.getStatsDBConnection();
			String query = "INSERT INTO sub_approval_audit (API_PROVIDER, API_NAME, API_VERSION, APP_ID, "
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
			WorkflowDBUtil.closeAllConnections(ps, conn, null);
		}
	}

	public static String getSubApprovalOperators(String apiName,
			String apiVersion, String apiProvider, int appId)
			throws Exception {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String operators = "";

		try {
			conn = WorkflowDBUtil.getAxiataDBConnection();
			String query = "SELECT OPERATOR_LIST FROM sub_approval_operators "
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
			WorkflowDBUtil.closeAllConnections(ps, conn, rs);
			
		}

		log.debug("operators : " + operators);
		return operators;
	}
	
	public static Map<String, String> getWorkflowAPIKeyMappings() throws Exception {

		log.debug("[START] getWorkflowAPIKeyMappings()");
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String apiName = null;
		String apiKey = null;
		Map<String , String> apiKeyMapping = new HashMap<String, String>();

		try {
			conn = WorkflowDBUtil.getAxiataDBConnection();
			String query = "SELECT API_NAME, API_KEY FROM workflow_api_key_mappings";
			log.debug("SQL : " + query);
			
			ps = conn.prepareStatement(query);

			rs = ps.executeQuery();
			while(rs.next()) {
				apiName = rs.getString("API_NAME");
				apiKey = rs.getString("API_KEY");
				apiKeyMapping.put(apiName, apiKey);
				
				log.debug("apiName : " + apiName + " | apiKey : " + apiKey);
			}

		} catch (SQLException e) {
			handleException(
					"Error in retrieving workflow related API Key mappings : " + e.getMessage(), e);
		} finally {
			WorkflowDBUtil.closeAllConnections(ps, conn, rs);
		}
		
		log.debug("[END] getWorkflowAPIKeyMappings()");
		return apiKeyMapping;
	}

	/**
	 * Handle exception.
	 * 
	 * @param msg
	 * @param t
	 * @throws APIManagementException
	 */
	private static void handleException(String msg, Throwable t)
			throws Exception {
		log.error(msg, t);
		throw new Exception(msg, t);
	}
}
