package com.wso2telco.workflow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.workflow.model.ApplicationEditDTO;
import com.wso2telco.workflow.utils.APIMgtDatabaseTables;

public class ApplicationDAO {

	private static Log log = LogFactory.getLog(ApplicationDAO.class);

	public void editApplicationTier(ApplicationEditDTO application) throws SQLException, BusinessException {

		Connection con = null;
		PreparedStatement updateApplicationTierStatement = null;

		try {
			con = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);

			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder updatetApplicationTierQueryString = new StringBuilder("UPDATE ");
			updatetApplicationTierQueryString.append(APIMgtDatabaseTables.AM_APPLICATION.getTableName());
			updatetApplicationTierQueryString.append(" SET APPLICATION_TIER = ? ");
			updatetApplicationTierQueryString.append("WHERE APPLICATION_ID = ?");

			updateApplicationTierStatement = con.prepareStatement(updatetApplicationTierQueryString.toString());

			updateApplicationTierStatement.setString(1, application.getApplicationTier());
			updateApplicationTierStatement.setInt(2, application.getApplicationId());

			log.debug("sql query in editApplicationTier : " + updateApplicationTierStatement);

			updateApplicationTierStatement.executeUpdate();
		} catch (SQLException e) {

			log.error("database operation error in editApplicationTier : ", e);
			throw new SQLException();
		} catch (Exception e) {

			log.error("error in editApplicationTier : ", e);
			throw new BusinessException(GenaralError.UNDEFINED);
		} finally {

			DbUtils.closeAllConnections(updateApplicationTierStatement, con, null);
		}
	}

	public String getApplicationTier(int applicationId) throws SQLException, BusinessException {
		StringBuilder appTierQry = new StringBuilder("SELECT APPLICATION_TIER FROM ");
		appTierQry.append(APIMgtDatabaseTables.AM_APPLICATION.getTableName());
		appTierQry.append(" WHERE APPLICATION_ID = ? LIMIT 1");

		Connection connection = null;
		PreparedStatement appTierStmnt = null;
		ResultSet resultSet = null;
		try {
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
			if (connection == null) {
				throw new Exception("Connection not found");
			}
			appTierStmnt = connection.prepareStatement(appTierQry.toString());
			appTierStmnt.setInt(1, applicationId);
			log.debug("sql query in getApplicationTier: " + appTierStmnt);

			resultSet = appTierStmnt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString("APPLICATION_TIER");
			} else {
				log.error("No application tiers found for application: " + applicationId);
				throw new BusinessException(GenaralError.UNDEFINED);
			}
		} catch (SQLException sqlException) {
			log.error("database operation error while fetching current application tier: ", sqlException);
			throw new SQLException();
		} catch (Exception exception) {
			log.error("error while fetching current application tier: ", exception);
			throw new BusinessException(GenaralError.UNDEFINED);
		} finally {
			DbUtils.closeAllConnections(appTierStmnt, connection, resultSet);
		}
	}

	public String getUUID(int applicationId) throws SQLException, BusinessException {
		StringBuilder appTierQry = new StringBuilder("SELECT UUID FROM ");
		appTierQry.append(APIMgtDatabaseTables.AM_APPLICATION.getTableName());
		appTierQry.append(" WHERE APPLICATION_ID = ? LIMIT 1");

		Connection connection = null;
		PreparedStatement appTierStmnt = null;
		ResultSet resultSet = null;
		try {
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
			if (connection == null) {
				throw new Exception("Connection not found");
			}
			appTierStmnt = connection.prepareStatement(appTierQry.toString());
			appTierStmnt.setInt(1, applicationId);
			log.debug("sql query in getApplicationTier: " + appTierStmnt);

			resultSet = appTierStmnt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString("UUID");
			} else {
				log.error("No application tiers found for application: " + applicationId);
				throw new BusinessException(GenaralError.UNDEFINED);
			}
		} catch (SQLException sqlException) {
			log.error("database operation error while fetching current application tier: ", sqlException);
			throw new SQLException();
		} catch (Exception exception) {
			log.error("error while fetching UUID: ", exception);
			throw new BusinessException(GenaralError.UNDEFINED);
		} finally {
			DbUtils.closeAllConnections(appTierStmnt, connection, resultSet);
		}
	}
}