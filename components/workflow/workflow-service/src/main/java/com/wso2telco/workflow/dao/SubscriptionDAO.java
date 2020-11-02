package com.wso2telco.workflow.dao;

import java.sql.*;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.workflow.model.SubscriptionEditDTO;
import com.wso2telco.workflow.utils.APIMgtDatabaseTables;

public class SubscriptionDAO {

	private static Log log = LogFactory.getLog(SubscriptionDAO.class);

	public void editSubscriptionTier(SubscriptionEditDTO subscription) throws SQLException, BusinessException {

		Connection con = null;
		PreparedStatement updateSubscriptionTierStatement = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);

			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder updatetSubscriptionTierQueryString = new StringBuilder("UPDATE ");
			updatetSubscriptionTierQueryString.append(APIMgtDatabaseTables.AM_SUBSCRIPTION.getTableName());
			updatetSubscriptionTierQueryString.append(" SET TIER_ID = ? ");
			updatetSubscriptionTierQueryString.append(", UPDATED_TIME = ?");
			updatetSubscriptionTierQueryString.append("WHERE APPLICATION_ID = ?");
			updatetSubscriptionTierQueryString.append(" AND API_ID = ?");

			updateSubscriptionTierStatement = con.prepareStatement(updatetSubscriptionTierQueryString.toString());

			updateSubscriptionTierStatement.setString(1, subscription.getSubscriptionTier());
			updateSubscriptionTierStatement.setTimestamp(2, new Timestamp(new Date().getTime()));
			updateSubscriptionTierStatement.setInt(3, subscription.getApplicationId());
			updateSubscriptionTierStatement.setInt(4, subscription.getApiID());

			log.debug("sql query in editSubscriptionTier : " + updateSubscriptionTierStatement);

			updateSubscriptionTierStatement.executeUpdate();
		} catch (SQLException e) {

			log.error("database operation error in editSubscriptionTier : ", e);
			throw new SQLException();
		} catch (Exception e) {

			log.error("error in editSubscriptionTier : ", e);
			throw new BusinessException(GenaralError.UNDEFINED);
		} finally {

			DbUtils.closeAllConnections(updateSubscriptionTierStatement, con, null);
		}
	}

	public String getSubscriptionTier(int applicationId, int apiId) throws SQLException, BusinessException {
		StringBuilder subscriptionTierGetQry = new StringBuilder("SELECT TIER_ID FROM ");
		subscriptionTierGetQry.append(APIMgtDatabaseTables.AM_SUBSCRIPTION.getTableName());
		subscriptionTierGetQry.append(" WHERE APPLICATION_ID = ? AND API_ID = ? LIMIT 1");

		Connection connection = null;
		PreparedStatement subscriptionTierGetStmt = null;
		ResultSet resultSet = null;
		try {
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
			if (connection == null) {
				throw new Exception("Connection not found");
			}
			subscriptionTierGetStmt = connection.prepareStatement(subscriptionTierGetQry.toString());
			subscriptionTierGetStmt.setInt(1, applicationId);
			subscriptionTierGetStmt.setInt(2, apiId);
			log.debug("sql query in getSubscriptionTier : " + subscriptionTierGetStmt);

			resultSet = subscriptionTierGetStmt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString("TIER_ID");
			} else {
				log.error("No subscription tiers found for application: " + applicationId + " and api: " + apiId);
				throw new BusinessException(GenaralError.UNDEFINED);
			}
		} catch (SQLException sqlException) {
			log.error("database operation error while fetching current subscription tier: ", sqlException);
			throw new SQLException();
		} catch (Exception exception) {
			log.error("error while fetching current subscription tier: ", exception);
			throw new BusinessException(GenaralError.UNDEFINED);
		} finally {
			DbUtils.closeAllConnections(subscriptionTierGetStmt, connection, resultSet);
		}

	}
}