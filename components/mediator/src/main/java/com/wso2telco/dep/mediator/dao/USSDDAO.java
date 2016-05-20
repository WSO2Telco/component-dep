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
import java.sql.ResultSet;
import java.sql.Statement;
import com.wso2telco.dbutils.DbUtils;
import com.wso2telco.dbutils.util.DataSourceNames;

public class USSDDAO extends CommonDAO {

	/**
	 * Ussd request entry.
	 *
	 * @param notifyURL
	 *            the notifyURL
	 * @return the integer
	 * @throws Exception
	 *             the exception
	 */
	public Integer ussdRequestEntry(String notifyURL) throws Exception {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Integer newId = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();

			StringBuilder queryString = new StringBuilder("SELECT MAX(axiataid) maxid ");
			queryString.append("FROM ussd_request_entry");

			rs = st.executeQuery(queryString.toString());
			if (rs.next()) {

				newId = rs.getInt("maxid") + 1;
			}

			StringBuilder insertQueryString = new StringBuilder(
					"INSERT INTO ussd_request_entry (axiataid, notifyurl) ");
			insertQueryString.append("VALUES (");
			insertQueryString.append(newId);
			insertQueryString.append(", ");
			insertQueryString.append(notifyURL);
			insertQueryString.append(")");

			st.executeUpdate(insertQueryString.toString());
		} catch (Exception e) {

			DbUtils.handleException("Error while inserting in to ussd_request_entry. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, rs);
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
	public String getUSSDNotify(Integer subscriptionId) throws Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		Statement st = null;
		ResultSet rs = null;
		String notifyurls = "";

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();

			StringBuilder queryString = new StringBuilder("SELECT notifyurl ");
			queryString.append("FROM ussd_request_entry ");
			queryString.append("WHERE axiataid = ");
			queryString.append(subscriptionId);

			rs = st.executeQuery(queryString.toString());

			if (rs.next()) {

				notifyurls = rs.getString("notifyurl");
			}

		} catch (Exception e) {

			DbUtils.handleException("Error while selecting from ussd_request_entry. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, rs);
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
	public boolean ussdEntryDelete(Integer subscriptionId) throws Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		Statement st = null;

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();

			StringBuilder queryString = new StringBuilder("DELETE FROM ussd_request_entry ");
			queryString.append("WHERE axiataid = ");
			queryString.append(subscriptionId);

			st.executeUpdate(queryString.toString());
		} catch (Exception e) {

			DbUtils.handleException("Error while deleting ussd_request_entry. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, null);
		}

		return true;
	}
}
