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

	public String getUpdatedTime(ApplicationEditDTO application) throws SQLException, BusinessException{

		Connection con = null;
		PreparedStatement getApplicationTierStatement = null;
		ResultSet rs = null;
		String updatedTime = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);

			if (con == null) {

				throw new Exception("Connection not found");
			}


			StringBuilder getApplicationTierQueryString = new StringBuilder("SELECT UPDATED_TIME FROM ");
			getApplicationTierQueryString.append(APIMgtDatabaseTables.AM_APPLICATION.getTableName());
			getApplicationTierQueryString.append(" WHERE APPLICATION_ID = ?");

			getApplicationTierStatement = con.prepareStatement(getApplicationTierQueryString.toString());
			getApplicationTierStatement.setInt(1, application.getApplicationId());
			rs = getApplicationTierStatement.executeQuery();

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
			DbUtils.closeAllConnections(getApplicationTierStatement, con, rs);
		}

		return updatedTime;
	}


}
