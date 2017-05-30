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

			StringBuilder query = new StringBuilder("select apiop.api_operationid, apiop.api_operation, apiop.api_operationcode ");
			query.append("from api_operation apiop, api ");
			query.append("where apiop.apiid = api.apiid ");
			query.append("and api.apiname=?");

			ps = con.prepareStatement(query.toString());

			ps.setString(1, apiCode);

			log.debug("sql query in getServiceDetailsByAPICode : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {
				serviceDetails.put(rs.getInt("api_operationid"), rs.getString("api_operation"));
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

			StringBuilder query = new StringBuilder("select opr.operation_rateid,rd.rate_defname ");
			query.append("from operation_rate opr, rate_def rd ");
			query.append("where opr.rate_defid=rd.rate_defid ");
			query.append("and api_operationid=? ");

			ps = con.prepareStatement(query.toString());

			ps.setInt(1, servicesDid);

			log.debug("sql query in getHubRateDetailsByServicesDid : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				rateDetails.put(rs.getInt("operation_rateid"), rs.getString("rate_defname"));
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

	//TODO:changing
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

			StringBuilder query = new StringBuilder("SELECT operationRate.operation_rateid, rate.rate_defname ");
			query.append("FROM ");
			query.append("rate_def rate,operator op,operation_rate operationRate ");
			query.append("WHERE operationRate.rate_defid = rate.rate_defid ");
			query.append("AND op.operatorid = operationRate.operator_id ");
			query.append("AND operationRate.api_operationid = ? ");
			query.append("AND op.operatorname = ?");

			ps = con.prepareStatement(query.toString());

			ps.setInt(1, servicesDid);
			ps.setString(2, operatorCode);

			log.debug("sql query in getOperatorRateDetailsByServicesDidAndOperatorCode : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				rateDetails.put(rs.getInt("operation_rateid"), rs.getString("rate_defname"));
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


	//TODO:change here rate. write rollback as well
	public void setHubSubscriptionRateData(int servicesRateDid, int applicationDid)
			throws SQLException, Exception {

		Connection conn = null;
		PreparedStatement ps = null;

		try {

			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);

			StringBuilder query = new StringBuilder("INSERT INTO rate_db.sub_rate_nb ");
			query.append("(api_operationid,applicationid,rate_defid) ");
			query.append("VALUES ((select api_operationid from operation_rate where operation_rateid=?),?,");
			query.append("(select rate_defid from operation_rate where operation_rateid=?))");
			//query.append("(SELECT rate_defid FROM rate_def WHERE rate_defname=?))");

			ps = conn.prepareStatement(query.toString());
			ps.setInt(1,servicesRateDid);
			ps.setInt(2,applicationDid);
			ps.setInt(3,servicesRateDid);

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

	public void setOperatorSubscriptionRateData(int operatorRateDid, int applicationDid, String operatorId, String operationId)
			throws SQLException, Exception {

		Connection conn = null;
		PreparedStatement ps = null;

		try {

			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);

			StringBuilder query = new StringBuilder("INSERT INTO sub_rate_sb ");
			query.append("(operatorid, api_operationid, applicationid, rate_defid)");
			query.append("VALUES (?, ?,?,?)");

			ps = conn.prepareStatement(query.toString());

			ps.setInt(1, 1);
			ps.setInt(2, 1);
			ps.setInt(3,1);
			ps.setInt(4,2);

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
