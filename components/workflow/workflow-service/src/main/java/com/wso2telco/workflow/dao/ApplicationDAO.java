package com.wso2telco.workflow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
