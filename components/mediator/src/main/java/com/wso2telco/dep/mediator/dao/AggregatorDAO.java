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

public class AggregatorDAO extends CommonDAO{

	/**
	 * Blacklistedmerchant.
	 *
	 * @param appid
	 *            the appid
	 * @param operatorid
	 *            the operatorid
	 * @param subscriber
	 *            the subscriber
	 * @param merchant
	 *            the merchant
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String blacklistedmerchant(int appid, String operatorid, String subscriber, String merchant)
			throws Exception {

		String resultcode = null;
		Connection con = null;
		Statement st = null;
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
			st = con.createStatement();
			String sql = "SELECT merchantopco_blacklist.id id " + "FROM merchantopco_blacklist, operators "
					+ "WHERE merchantopco_blacklist.operator_id = operators.id " + "AND application_id = " + appid + " "
					+ "AND operatorname = '" + operatorid + "' " + "AND subscriber = '" + subscriber + "' "
					+ "AND lower(merchant) = '" + merchant.toLowerCase() + "'";

			rs = st.executeQuery(sql);
			if (rs.next()) {

				resultcode = String.valueOf(rs.getInt("id"));
			} else {

				sql = "SELECT merchantopco_blacklist.id id " + "FROM merchantopco_blacklist, operators "
						+ "WHERE merchantopco_blacklist.operator_id = operators.id " + "AND application_id is null "
						+ "AND subscriber = '" + subscriber + "' " + "AND operatorname = '" + operatorid + "' "
						+ "AND lower(merchant) = '" + merchant.toLowerCase() + "'";

				rs = st.executeQuery(sql);
				if (rs.next()) {

					resultcode = String.valueOf(rs.getInt("id"));
				}
			}

		} catch (Exception e) {

			DbUtils.handleException("Error while selecting black listed merchant. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, rs);
		}

		return resultcode;
	}
}
