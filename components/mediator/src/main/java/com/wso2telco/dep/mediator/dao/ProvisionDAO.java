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
package com.wso2telco.dep.mediator.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import com.wso2telco.dbutils.DbUtils;
import com.wso2telco.dbutils.util.DataSourceNames;
import com.wso2telco.dep.mediator.util.DatabaseTables;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProvisionDAO {

	/** The Constant log. */
	private final Log log = LogFactory.getLog(ProvisionDAO.class);

	public Integer provisionServiceEntry(String notifyURL, String serviceProvider) throws SQLException, Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer newId = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder insertQueryString = new StringBuilder("INSERT INTO ");
			insertQueryString.append(DatabaseTables.PROVISION_SERVICE_ENTRY.getTableName());
			insertQueryString.append(" (notifyurl, service_provider, is_active) ");
			insertQueryString.append("VALUES (?, ?, ?)");

			ps = con.prepareStatement(insertQueryString.toString(), Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, notifyURL);
			ps.setString(2, serviceProvider);
			ps.setInt(3, 0);

			log.debug("sql query in provisionServiceEntry : " + ps);

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				newId = rs.getInt(1);
			}
		} catch (SQLException e) {

			log.error("database operation error in provisionServiceEntry : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in provisionServiceEntry : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return newId;
	}

	public void provisionServiceOperatorEntry(String operatorEndpoint, Integer provisionServiceId, String operatorName)
			throws SQLException, Exception {

		Connection con = null;
		PreparedStatement insertStatement = null;
		PreparedStatement updateStatement = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

			if (con == null) {

				throw new Exception("Connection not found");
			}

			/**
			 * Set autocommit off to handle the transaction
			 */
			con.setAutoCommit(false);

			StringBuilder queryString = new StringBuilder("INSERT INTO ");
			queryString.append(DatabaseTables.PROVISION_SERVICE_OPERATOR_ENDPOINTS.getTableName());
			queryString.append(" (provision_service_did, domainurl, operator) ");
			queryString.append("VALUES (?, ?, ?)");

			insertStatement = con.prepareStatement(queryString.toString());

			insertStatement.setInt(1, provisionServiceId);
			insertStatement.setString(2, operatorEndpoint);
			insertStatement.setString(3, operatorName);

			log.debug("sql query in provisionServiceOperatorEntry : " + insertStatement);

			insertStatement.executeUpdate();

			StringBuilder updateQueryString = new StringBuilder("UPDATE ");
			updateQueryString.append(DatabaseTables.PROVISION_SERVICE_ENTRY.getTableName());
			updateQueryString.append(" SET is_active = ?");
			updateQueryString.append(" WHERE provision_service_did = ?");

			updateStatement = con.prepareStatement(updateQueryString.toString());

			updateStatement.setInt(1, 1);
			updateStatement.setInt(2, provisionServiceId);

			log.debug("sql query in provisionServiceOperatorEntry : " + updateStatement);

			updateStatement.executeUpdate();

			/**
			 * commit the transaction if all success
			 */
			con.commit();
		} catch (SQLException e) {

			/**
			 * rollback if Exception occurs
			 */
			con.rollback();

			log.error("database operation error in provisionServiceOperatorEntry : ", e);
			throw e;
		} catch (Exception e) {

			/**
			 * rollback if Exception occurs
			 */
			con.rollback();

			log.error("error in provisionServiceOperatorEntry : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(insertStatement, con, null);
			DbUtils.closeAllConnections(updateStatement, null, null);
		}
	}
}
