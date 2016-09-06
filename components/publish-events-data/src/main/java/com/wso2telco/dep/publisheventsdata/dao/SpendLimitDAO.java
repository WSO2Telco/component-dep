/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.publisheventsdata.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.publisheventsdata.util.DatabaseTables;

public class SpendLimitDAO {

	/** The Constant log. */
	private final Log log = LogFactory.getLog(SpendLimitDAO.class);

	public boolean checkMSISDNSpendLimit(String msisdn) throws SQLException, Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExceeded = false;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder queryString = new StringBuilder("SELECT exists ");
			queryString.append("(SELECT 1 FROM ");
			queryString.append(DatabaseTables.MSISDN_SPEND_LIMIT.getTableName());
			queryString.append(" WHERE msisdn = ?");
			queryString.append(" LIMIT 1)");

			ps = con.prepareStatement(queryString.toString());

			ps.setString(1, msisdn);

			log.debug("sql query in checkMSISDNSpendLimit : " + ps);

			rs = ps.executeQuery();

			if (rs.next()) {

				isExceeded = rs.getBoolean(1);
			}
		} catch (SQLException e) {

			log.error("database operation error in checkMSISDNSpendLimit : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in checkMSISDNSpendLimit : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return isExceeded;
	}

	public boolean checkApplicationSpendLimit(String consumerKey) throws SQLException, Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExceeded = false;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder queryString = new StringBuilder("SELECT exists ");
			queryString.append("(SELECT 1 FROM ");
			queryString.append(DatabaseTables.APPLICATION_SPEND_LIMIT.getTableName());
			queryString.append(" WHERE consumerKey = ?");
			queryString.append(" LIMIT 1)");

			ps = con.prepareStatement(queryString.toString());

			ps.setString(1, consumerKey);

			log.debug("sql query in checkApplicationSpendLimit : " + ps);

			rs = ps.executeQuery();

			if (rs.next()) {

				isExceeded = rs.getBoolean(1);
			}
		} catch (SQLException e) {

			log.error("database operation error in checkApplicationSpendLimit : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in checkApplicationSpendLimit : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return isExceeded;
	}

	public boolean checkOperatorSpendLimit(String operatorId) throws SQLException, Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExceeded = false;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder queryString = new StringBuilder("SELECT exists ");
			queryString.append("(SELECT 1 FROM ");
			queryString.append(DatabaseTables.OPERATOR_SPEND_LIMIT.getTableName());
			queryString.append(" WHERE operatorId = ?");
			queryString.append(" LIMIT 1)");

			ps = con.prepareStatement(queryString.toString());

			ps.setString(1, operatorId);

			log.debug("sql query in checkOperatorSpendLimit : " + ps);

			rs = ps.executeQuery();

			if (rs.next()) {

				isExceeded = rs.getBoolean(1);
			}
		} catch (SQLException e) {

			log.error("database operation error in checkOperatorSpendLimit : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in checkOperatorSpendLimit : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return isExceeded;
	}
}
