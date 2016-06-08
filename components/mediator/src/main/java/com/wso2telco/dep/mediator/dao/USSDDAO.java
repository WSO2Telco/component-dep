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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.dbutils.DbUtils;
import com.wso2telco.dbutils.util.DataSourceNames;
import com.wso2telco.dep.mediator.util.DatabaseTables;

public class USSDDAO {

	/** The Constant log. */
	private final Log log = LogFactory.getLog(USSDDAO.class);

	/**
	 * Ussd request entry.
	 *
	 * @param notifyURL
	 *            the notifyURL
	 * @return the integer
	 * @throws Exception
	 *             the exception
	 */
	public Integer ussdRequestEntry(String notifyURL) throws SQLException, Exception {

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
			insertQueryString.append(DatabaseTables.USSD_REQUEST_ENTRY.getTableName());
			insertQueryString.append(" (notifyurl) ");
			insertQueryString.append("VALUES (?)");

			ps = con.prepareStatement(insertQueryString.toString(), Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, notifyURL);

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				newId = rs.getInt(1);
			}
		} catch (SQLException e) {

			log.error("DATABASE OPERATION ERROR IN ussdRequestEntry : ", e);
			throw e;
		} catch (Exception e) {

			log.error("ERROR IN ussdRequestEntry : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return newId;
	}

	/**
	 * Gets the USSD notify.
	 *
	 * @param subscriptionId
	 *            the subscriptionId
	 * @return the USSD notify
	 * @throws Exception
	 *             the exception
	 */
	public String getUSSDNotify(Integer subscriptionId) throws SQLException, Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		PreparedStatement ps = null;
		ResultSet rs = null;
		String notifyurls = "";

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder queryString = new StringBuilder("SELECT notifyurl ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.USSD_REQUEST_ENTRY.getTableName());
			queryString.append(" WHERE ussd_request_did = ?");

			ps = con.prepareStatement(queryString.toString());

			ps.setInt(1, subscriptionId);

			rs = ps.executeQuery();

			if (rs.next()) {

				notifyurls = rs.getString("notifyurl");
			}

		} catch (SQLException e) {

			log.error("DATABASE OPERATION ERROR IN getUSSDNotify : ", e);
			throw e;
		} catch (Exception e) {

			log.error("ERROR IN getUSSDNotify : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return notifyurls;
	}

	/**
	 * Ussd entry delete.
	 *
	 * @param subscriptionId
	 *            the subscriptionId
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean ussdEntryDelete(Integer subscriptionId) throws SQLException, Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		PreparedStatement ps = null;

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder queryString = new StringBuilder("DELETE FROM ");
			queryString.append(DatabaseTables.USSD_REQUEST_ENTRY.getTableName());
			queryString.append(" WHERE ussd_request_did = ?");

			ps = con.prepareStatement(queryString.toString());

			ps.setInt(1, subscriptionId);

			ps.executeUpdate();
		} catch (SQLException e) {

			log.error("DATABASE OPERATION ERROR IN ussdEntryDelete : ", e);
			throw e;
		} catch (Exception e) {

			log.error("ERROR IN ussdEntryDelete : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, null);
		}

		return true;
	}
}
