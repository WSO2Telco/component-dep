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
import com.wso2telco.dbutils.DbUtils;
import com.wso2telco.dbutils.util.DataSourceNames;
import com.wso2telco.dep.mediator.util.DatabaseTables;

public class AggregatorDAO {

	/**
	 * Blacklistedmerchant.
	 *
	 * @param appId
	 *            the appId
	 * @param operatorId
	 *            the operatorId
	 * @param subscriber
	 *            the subscriber
	 * @param merchant
	 *            the merchant
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String blacklistedmerchant(int appId, String operatorId, String subscriber, String merchant)
			throws Exception {

		String resultcode = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		if (merchant == null || merchant.isEmpty()) {

			return resultcode;
		}

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			// is aggrigator
			StringBuilder queryString = new StringBuilder("SELECT merchantopco_blacklist.id id ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.MERCHANT_OPERATOR_BLACKLIST.getTableName());
			queryString.append(" , ");
			queryString.append(DatabaseTables.OPERATORS.getTableName());
			queryString.append(" WHERE merchantopco_blacklist.operator_id = operators.id ");
			queryString.append("AND application_id = ?");
			queryString.append(" AND operatorname = ?");
			queryString.append(" AND subscriber = ?");
			queryString.append(" AND lower(merchant) = ?");

			ps = con.prepareStatement(queryString.toString());

			ps.setInt(1, appId);
			ps.setString(2, operatorId);
			ps.setString(3, subscriber);
			ps.setString(4, merchant.toLowerCase());

			rs = ps.executeQuery();

			while (rs.next()) {

				resultcode = String.valueOf(rs.getInt("id"));
			}
		} catch (Exception e) {

			DbUtils.handleException("Error while selecting black listed merchant. ", e);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return resultcode;
	}

	/**
	 * Blacklistedmerchant.
	 *
	 * @param operatorId
	 *            the operatorId
	 * @param subscriber
	 *            the subscriber
	 * @param merchant
	 *            the merchant
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String blacklistedmerchant(String operatorId, String subscriber, String merchant) throws Exception {

		String resultcode = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		if (merchant == null || merchant.isEmpty()) {

			return resultcode;
		}

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder merchantQueryString = new StringBuilder("SELECT merchantopco_blacklist.id id ");
			merchantQueryString.append("FROM ");
			merchantQueryString.append(DatabaseTables.MERCHANT_OPERATOR_BLACKLIST.getTableName());
			merchantQueryString.append(" , ");
			merchantQueryString.append(DatabaseTables.OPERATORS.getTableName());
			merchantQueryString.append(" WHERE merchantopco_blacklist.operator_id = operators.id ");
			merchantQueryString.append("AND application_id is null ");
			merchantQueryString.append("AND subscriber = ?");
			merchantQueryString.append(" AND operatorname = ?");
			merchantQueryString.append(" AND lower(merchant) = ?");

			ps = con.prepareStatement(merchantQueryString.toString());

			ps.setString(1, subscriber);
			ps.setString(2, operatorId);
			ps.setString(3, merchant.toLowerCase());

			rs = ps.executeQuery();

			while (rs.next()) {

				resultcode = String.valueOf(rs.getInt("id"));
			}
		} catch (Exception e) {

			DbUtils.handleException("Error while selecting black listed merchant. ", e);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return resultcode;
	}
}
