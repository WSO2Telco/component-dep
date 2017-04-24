package com.wso2telco.dep.billingservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.billingservice.util.DatabaseTables;

public class RateCardDAO {

	/** The Constant log. */
	private static final Log log = LogFactory.getLog(RateCardDAO.class);

	public Map<Integer, String> getServiceDetailsByAPICode(String apiCode) throws SQLException, Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<Integer, String> serviceDetails = new HashMap<Integer, String>();

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder queryString = new StringBuilder("SELECT services.servicesDid, services.code ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.IN_MD_API.getTObject());
			queryString.append(" api, ");
			queryString.append(DatabaseTables.IN_MD_SERVICES.getTObject());
			queryString.append(" services ");
			queryString.append("WHERE api.apiDid = services.apiDid ");
			queryString.append("AND api.code = ?");

			ps = con.prepareStatement(queryString.toString());

			ps.setString(1, apiCode);

			log.debug("sql query in getServiceDetailsByAPICode : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				serviceDetails.put(rs.getInt("servicesDid"), rs.getString("code"));
			}
		} catch (SQLException e) {

			log.error("database operation error in getServiceDetailsByAPICode : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in getServiceDetailsByAPICode : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return serviceDetails;
	}

	public Map<Integer, String> getHubRateDetailsByServicesDid(int servicesDid) throws SQLException, Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<Integer, String> rateDetails = new HashMap<Integer, String>();

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder queryString = new StringBuilder("SELECT operationRate.servicesRateDid, rate.code ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.IN_MD_RATE.getTObject());
			queryString.append(" rate, ");
			queryString.append(DatabaseTables.IN_OPERATION_RATE.getTObject());
			queryString.append(" operationRate ");
			queryString.append("WHERE operationRate.rateDid = rate.rateDid ");
			queryString.append("AND operationRate.servicesDid = ?");

			ps = con.prepareStatement(queryString.toString());

			ps.setInt(1, servicesDid);

			log.debug("sql query in getHubRateDetailsByServicesDid : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				rateDetails.put(rs.getInt("servicesRateDid"), rs.getString("code"));
			}
		} catch (SQLException e) {

			log.error("database operation error in getHubRateDetailsByServicesDid : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in getHubRateDetailsByServicesDid : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateDetails;
	}

	public Map<Integer, String> getOperatorRateDetailsByServicesDidAndOperatorCode(int servicesDid, String operatorCode)
			throws SQLException, Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<Integer, String> rateDetails = new HashMap<Integer, String>();

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder queryString = new StringBuilder("SELECT operatorRate.operatorRateDid, rate.code ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.IN_MD_RATE.getTObject());
			queryString.append(" rate, ");
			queryString.append(DatabaseTables.IN_MD_OPERATOR.getTObject());
			queryString.append(" operator, ");
			queryString.append(DatabaseTables.IN_MD_OPERATOR_RATE.getTObject());
			queryString.append(" operatorRate ");
			queryString.append("WHERE operatorRate.rateDid = rate.rateDid ");
			queryString.append("AND operator.operatorDid = operatorRate.operatorDid ");
			queryString.append("AND operatorRate.servicesDid = ? ");
			queryString.append("AND operator.code = ?");

			ps = con.prepareStatement(queryString.toString());

			ps.setInt(1, servicesDid);
			ps.setString(2, operatorCode);

			log.debug("sql query in getOperatorRateDetailsByServicesDidAndOperatorCode : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				rateDetails.put(rs.getInt("operatorRateDid"), rs.getString("code"));
			}
		} catch (SQLException e) {

			log.error("database operation error in getOperatorRateDetailsByServicesDidAndOperatorCode : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in getOperatorRateDetailsByServicesDidAndOperatorCode : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateDetails;
	}

	public void setHubSubscriptionRateData(int servicesRateDid, int applicationDid, String apiCode)
			throws SQLException, Exception {

		Connection conn = null;
		PreparedStatement ps = null;

		try {

			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

			StringBuilder query = new StringBuilder();
			query.append("INSERT INTO ");
			query.append(DatabaseTables.IN_MD_NB_SUBSCRIPTION_RATE.getTObject());
			query.append(" (servicesRateDid, applicationDid, apiDid) ");
			query.append("VALUES (?, ?, (SELECT apiDid ");
			query.append("FROM ");
			query.append(DatabaseTables.IN_MD_API.getTObject());
			query.append(" WHERE code = ?))");

			ps = conn.prepareStatement(query.toString());

			ps.setInt(1, servicesRateDid);
			ps.setInt(2, applicationDid);
			ps.setString(3, apiCode);

			log.debug("sql query in setHubSubscriptionRateData : " + ps);

			ps.execute();
		} catch (SQLException e) {

			log.error("database operation error in setHubSubscriptionRateData : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in setHubSubscriptionRateData : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, conn, null);
		}
	}

	public void setOperatorSubscriptionRateData(int operatorRateDid, int applicationDid)
			throws SQLException, Exception {

		Connection conn = null;
		PreparedStatement ps = null;

		try {

			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

			StringBuilder query = new StringBuilder();
			query.append("INSERT INTO ");
			query.append(DatabaseTables.IN_MD_SB_SUBSCRIPTIONS.getTObject());
			query.append(" (operationRateDid, applicationDid) ");
			query.append("VALUES (?, ?)");

			ps = conn.prepareStatement(query.toString());

			ps.setInt(1, operatorRateDid);
			ps.setInt(2, applicationDid);

			log.debug("sql query in setOperatorSubscriptionRateData : " + ps);

			ps.execute();
		} catch (SQLException e) {

			log.error("database operation error in setOperatorSubscriptionRateData : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in setOperatorSubscriptionRateData : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, conn, null);
		}
	}
}
