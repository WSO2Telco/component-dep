package com.wso2telco.dep.operatorservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.msisdnvalidator.MSISDN;
import com.wso2telco.dbutils.DbUtils;
import com.wso2telco.dbutils.util.DataSourceNames;
import com.wso2telco.dep.operatorservice.model.MSISDNSearchDTO;
import com.wso2telco.dep.operatorservice.model.WhiteListMSISDNSearchDTO;
import com.wso2telco.dep.operatorservice.util.OparatorTable;

public class BlackListWhiteListDAO {

	private static final Log log = LogFactory.getLog(BlackListWhiteListDAO.class);

	/**
	 * blacklist list given msisdns
	 * 
	 * @param msisdns
	 * @param apiID
	 * @param apiName
	 * @param userID
	 * @throws Exception
	 */
	public void blacklist(List<MSISDN> msisdns, final String apiID, final String apiName, final String userID)
			throws Exception {

		log.debug("BlackListWhiteListDAO.blacklist triggerd MSISDN[" + StringUtils.join(msisdns, ",") + "] apiID:"
				+ apiID + " apiName:" + apiName + " userID:" + userID);

		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO ");
		sql.append(OparatorTable.BLACKLIST_MSISDN.getTObject());
		sql.append("(MSISDN,API_ID,API_NAME,USER_ID)");
		sql.append(" VALUES (?, ?, ?, ?)");

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql.toString());

			conn.setAutoCommit(false);

			for (MSISDN msisdn : msisdns) {
				ps.setString(1, String.valueOf(msisdn.getCountryCode()) + String.valueOf(msisdn.getNationalNumber()));
				ps.setString(2, apiID);
				ps.setString(3, apiName);
				ps.setString(4, userID);
				ps.addBatch();
			}

			ps.executeBatch();
			conn.commit();

		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, null);
		}

	}

	/**
	 * filter and return already balcklisted for the filtering
	 * 
	 * @param searchDTO
	 * @return
	 * @throws SQLException
	 */
	public List<MSISDN> loadAlreadyBlacklisted(MSISDNSearchDTO searchDTO) throws SQLException {
		return Collections.EMPTY_LIST;
	}

	public List<MSISDN> loadSubScriptionWhiteListed(WhiteListMSISDNSearchDTO searchDTO) throws SQLException {
		return Collections.EMPTY_LIST;
	}
	/**
	 * balcklist single msisdn
	 * 
	 * @param msisdn
	 * @param apiID
	 * @param apiName
	 * @param userID
	 * @throws Exception
	 */
	public void blacklist(final MSISDN msisdn, final String apiID, final String apiName, final String userID)
			throws Exception {
		List<MSISDN> msisdns = Collections.emptyList();
		msisdns.add(msisdn);
		blacklist(msisdns, apiID, apiName, userID);
	}

	public String[] getBlacklisted(MSISDNSearchDTO searchDTO) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT MSISDN,API_ID,API_NAME,USER_ID");
		sql.append(" FROM ");
		sql.append(OparatorTable.BLACKLIST_MSISDN.getTObject());
		sql.append(" WHERE 1=1 ");

		if (searchDTO.isValidApiName()) {
			sql.append(" AND  API_NAME =? ");
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> returnList_ = new ArrayList<String>();

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);

			ps = conn.prepareStatement(sql.toString());
			if (searchDTO.isValidApiName()) {
				ps.setString(1, searchDTO.getApiName());
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				returnList_.add(rs.getString("MSISDN"));
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);

		}
		if (returnList_ != null && !returnList_.isEmpty()) {
			return returnList_.toArray(new String[returnList_.size()]);
		} else {
			return null;
		}

	}

	public void removeBlacklist(final String apiName, final String userMSISDN) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(OparatorTable.BLACKLIST_MSISDN.getTObject());
		sql.append(" WHERE API_NAME = ?");
		sql.append(" AND MSISDN = ?");
		//sql.append(" (API_NAME, MSISDN) ");
		//sql.append("VALUES (?, ?)");

		
		
		//String sql = "DELETE FROM `blacklistmsisdn` WHERE `API_NAME`=? && `MSISDN`=?;";

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql.toString());

			ps.setString(1, apiName);
			ps.setString(2, userMSISDN);

			ps.executeUpdate();
							
		} catch (SQLException e) {
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, conn, rs);

		}
	}
	
	
	/**
	 * when the subscription id is known
	 * @param userMSISDNs
	 * @param SubscriptionID
	 * @param apiID
	 * @param applicationID
	 * @throws SQLException
	 * @throws Exception
	 */
	public void whitelist(List<MSISDN> userMSISDNs, String SubscriptionID, String apiID, String applicationID) 	throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(OparatorTable.SUBSCRIPTION_WHITELIST.getTObject());
		sql.append(" (MSISDN,subscriptionID,api_id,application_id)");
		sql.append(" VALUES (?,?,?,?);");

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql.toString());

			conn.setAutoCommit(false);
			for (MSISDN msisdn : userMSISDNs) {

				ps.setString(1, String.valueOf(msisdn.getCountryCode()).intern() + String.valueOf(msisdn.getNationalNumber()));
				ps.setString(2, SubscriptionID);
				ps.setString(3, apiID);
				ps.setString(4, applicationID);

				ps.addBatch();
			}
			ps.executeUpdate();
			conn.commit();

		} catch (Exception e) {
			conn.rollback();
			log.error("", e);
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, null);
		}

	}
	
	/**
	 * this need to replace form the apim admin services
	 * @param appId
	 * @param apiId
	 * @return
	 * @throws Exception
	 */
	 @Deprecated
	public  String findSubscriptionId(String appId, String apiId) throws Exception {

		String sql = "SELECT SUBSCRIPTION_ID from AM_SUBSCRIPTION where APPLICATION_ID = ? and API_ID = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn =DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql);
			ps.setString(1, appId);
			ps.setString(2, apiId);
			rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getString("SUBSCRIPTION_ID");
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);

		}
		throw new Exception(
				"No record found in table AM_SUBSCRIPTION for APPLICATION_ID = " + appId + " and API_ID = " + apiId);
	}
	
	public void removeWhitelistNumber(String userMSISDN) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ");
		sql.append( OparatorTable.SUBSCRIPTION_WHITELIST.getTObject());
		sql.append(" WHERE `MSISDN`=?;");

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, userMSISDN);
			ps.execute();
		} catch (Exception e) {
			throw e;
		} finally { 
			DbUtils.closeAllConnections(ps, conn, null);
		}
	}
	
	/*public static String getSubscribers() throws SQLException, NamingException, ValidatorException {
		String sql = "SELECT SUBSCRIBER_ID, USER_ID from AM_SUBSCRIBER;";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getAMDBConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			String json = "{";
			while (rs.next()) {
				String subscriberId = rs.getString("SUBSCRIBER_ID");
				String username = rs.getString("USER_ID");
				json += "\"" + subscriberId + "\":\"" + username + "\",";
			}
			if (json.endsWith(",")) {
				json = json.substring(0, json.length() - 1);
			}
			json += "}";
			return json;
		} catch (SQLException e) {
			System.out.println(e.toString());
			throw e;
		} finally {

			closeAllConnections(ps, conn, rs);

		}
	}*/

	/*
	 * public static int next() { if (currentNo == Integer.MAX_VALUE) {
	 * currentNo = 0; } return currentNo++; }
	 */

	/*
	 * 
	 * 
	 * // @SuppressWarnings("finally") public static List<String> void
	 * writeWhitelistNumbersBulk(String[] userMSISDN, String apiId, String
	 * userID, String appId) throws Exception {
	 * System.out.println(Arrays.toString(userMSISDN));
	 * 
	 * String subscriptionId = findSubscriptionId(appId, apiId); String sql =
	 * "INSERT INTO `subscription_WhiteList` (`msisdn`,`api_id`,`application_id`,`subscriptionID`) VALUES "
	 * ;// (`msisdn`,`api_id`,`application_id`,`subscriptionID`) for (int i = 0;
	 * i < userMSISDN.length; i++) { if (i == 0) { sql += "(?,?,?,?)"; } else {
	 * sql += " ,(?,?,?,?)"; } } sql += ";";
	 * 
	 * Connection conn = null; PreparedStatement ps = null;
	 * 
	 * try { conn = getStatDBConnection(); ps = conn.prepareStatement(sql);
	 * 
	 * for (int i = 0; i < userMSISDN.length; i++) { int queryStartPos = i * 4 +
	 * 1; ps.setString(queryStartPos, userMSISDN[i]); ps.setString(queryStartPos
	 * + 1, apiId); // ps.setString(queryStartPos + 2, apiName);
	 * ps.setString(queryStartPos + 2, appId); ps.setString(queryStartPos + 3,
	 * subscriptionId); }
	 * 
	 * ps.execute();
	 * 
	 * } catch (SQLException e) { System.out.println(e.toString()); throw e; }
	 * finally {
	 * 
	 * closeAllConnections(ps, conn, null);
	 * 
	 * }
	 * 
	 * }
	 * 
	 *
	 * 
	
	 * 
	 * public static String getApps(String subscriberId) throws SQLException,
	 * NamingException, ValidatorException { String sql =
	 * "SELECT APPLICATION_ID, NAME from AM_APPLICATION where SUBSCRIBER_ID = ?"
	 * ; Connection conn = null; PreparedStatement ps = null; ResultSet rs =
	 * null; try { conn = getAMDBConnection(); ps = conn.prepareStatement(sql);
	 * ps.setString(1, subscriberId); rs = ps.executeQuery(); String json = "{";
	 * while (rs.next()) { String appId = rs.getString("APPLICATION_ID"); String
	 * appName = rs.getString("NAME"); json += "\"" + appId + "\":\"" + appName
	 * + "\","; } if (json.endsWith(",")) { json = json.substring(0,
	 * json.length() - 1); } json += "}"; return json; } catch (SQLException e)
	 * { System.out.println(e.toString()); throw e; } finally {
	 * 
	 * closeAllConnections(ps, conn, rs);
	 * 
	 * } }
	 * 
	 * public static String getApis(String appId) throws SQLException,
	 * NamingException, ValidatorException {
	 * 
	 * String appSql = "SELECT API_ID,API_NAME from AM_API where API_ID IN";
	 * String subSql =
	 * "SELECT DISTINCT API_ID from AM_SUBSCRIPTION where APPLICATION_ID = ?";
	 * String sql = appSql + "(" + subSql + ");"; Connection conn = null;
	 * PreparedStatement ps = null; ResultSet rs = null; try { conn =
	 * getAMDBConnection(); ps = conn.prepareStatement(sql); ps.setString(1,
	 * appId); rs = ps.executeQuery(); String json = "{"; while (rs.next()) {
	 * String apiId = rs.getString("API_ID"); String apiName =
	 * rs.getString("API_NAME"); json += "\"" + apiId + "\":\"" + apiName +
	 * "\","; } if (json.endsWith(",")) { json = json.substring(0, json.length()
	 * - 1); } json += "}"; return json; } catch (SQLException e) {
	 * System.out.println(e.toString()); throw e; } finally {
	 * 
	 * closeAllConnections(ps, conn, rs);
	 * 
	 * } }
	 * 
	 * public static void closeAllConnections(PreparedStatement
	 * preparedStatement, Connection connection, ResultSet resultSet) {
	 * 
	 * closeConnection(connection); closeStatement(preparedStatement);
	 * closeResultSet(resultSet); }
	 * 
	 *//**
		 * Close Connection
		 * 
		 * @param dbConnection
		 *            Connection
		 */
	/*
	 * private static void closeConnection(Connection dbConnection) { if
	 * (dbConnection != null) { try { dbConnection.close(); } catch
	 * (SQLException e) { log.warn(
	 * "Database error. Could not close database connection. Continuing with " +
	 * "others. - " + e.getMessage(), e); } } }
	 * 
	 *//**
		 * Close ResultSet
		 * 
		 * @param resultSet
		 *            ResultSet
		 */
	/*
	 * private static void closeResultSet(ResultSet resultSet) { if (resultSet
	 * != null) { try { resultSet.close(); } catch (SQLException e) { log.warn(
	 * "Database error. Could not close ResultSet  - " + e.getMessage(), e); } }
	 * 
	 * }
	 * 
	 *//**
		 * Close PreparedStatement
		 * 
		 * @param preparedStatement
		 *            PreparedStatement
		 */
	/*
	 * private static void closeStatement(PreparedStatement preparedStatement) {
	 * if (preparedStatement != null) { try { preparedStatement.close(); } catch
	 * (SQLException e) { log.warn(
	 * "Database error. Could not close PreparedStatement. Continuing with" +
	 * " others. - " + e.getMessage(), e); } }
	 * 
	 * }
	 * 
	 */
	
	 
	public List<String> getWhiteListNumbers() throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT MSISDN FROM ");
		sql.append(OparatorTable.SUBSCRIPTION_WHITELIST.getTObject());
		

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> whiteList = new ArrayList<String>();

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();

			while (rs.next()) {
				whiteList.add(rs.getString("MSISDN"));
			}
			return whiteList;
			
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);

		}
	}
	  
	 
}
