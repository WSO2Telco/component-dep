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
			java.util.Date date = new java.util.Date();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
			updateSubscriptionTierStatement.setString(1, subscription.getSubscriptionTier());
			updateSubscriptionTierStatement.setTimestamp(2, timestamp);
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
}
