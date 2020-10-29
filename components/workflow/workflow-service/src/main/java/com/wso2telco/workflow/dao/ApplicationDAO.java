package com.wso2telco.workflow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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

			StringBuilder updatedApplicationTierQueryString = new StringBuilder("UPDATE ");
			updatedApplicationTierQueryString.append(APIMgtDatabaseTables.AM_APPLICATION.getTableName());
			updatedApplicationTierQueryString.append(" SET APPLICATION_TIER = ? ");
			updatedApplicationTierQueryString.append("WHERE APPLICATION_ID = ?");
			updateApplicationTierStatement = con.prepareStatement(updatedApplicationTierQueryString.toString());
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

	public String getSubscriptionTier(int applicationId) throws SQLException, BusinessException {
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
			log.debug("sql query in getSubscriptionTier: " + appTierStmnt);

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

	public String getUpdatedTime(ApplicationEditDTO application) throws SQLException, BusinessException{

		Connection con = null;
		PreparedStatement getApplicationUpdatedTimeStatement = null;
		ResultSet rs = null;
		String updatedTime = null;

		try {
			con = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
			if (con == null) {
				throw new Exception("Connection not found");
			}

			StringBuilder getApplicationUpdatedTimeQueryString = new StringBuilder("SELECT UPDATED_TIME FROM ");
			getApplicationUpdatedTimeQueryString.append(APIMgtDatabaseTables.AM_APPLICATION.getTableName());
			getApplicationUpdatedTimeQueryString.append(" WHERE APPLICATION_ID = ?");
			getApplicationUpdatedTimeStatement = con.prepareStatement(getApplicationUpdatedTimeQueryString.toString());
			getApplicationUpdatedTimeStatement.setInt(1, application.getApplicationId());
			rs = getApplicationUpdatedTimeStatement.executeQuery();

			while (rs.next()) {
				updatedTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(rs.getTimestamp("UPDATED_TIME"));
			}

		}catch (SQLException e) {
			log.error("database operation error in  getUpdatedTime: ", e);
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			log.error("error in application getUpdatedTime : ", e);
			throw new BusinessException(GenaralError.UNDEFINED);
		} finally {
			DbUtils.closeAllConnections(getApplicationUpdatedTimeStatement, con, rs);
		}
		return updatedTime;
	}
}
