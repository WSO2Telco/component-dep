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
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
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
