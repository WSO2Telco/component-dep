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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.cache.Cache;
import javax.cache.Caching;

import com.wso2telco.dbutils.AxataDBUtilException;
import com.wso2telco.dbutils.DbUtils;
import com.wso2telco.dbutils.Operator;
import com.wso2telco.dbutils.Operatorendpoint;
import com.wso2telco.dbutils.util.DataSourceNames;

public class OperatorDAO extends CommonDAO {

	/**
	 * Operator endpoints.
	 *
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<Operatorendpoint> getOperatorEndpoints() throws Exception {

		final int opEndpointsID = 0;

		Cache<Integer, List<Operatorendpoint>> cache = Caching.getCacheManager(AXIATA_MEDIATOR_CACHE_MANAGER)
				.getCache("axiataDbOperatorEndpoints");
		List<Operatorendpoint> endpoints = cache.get(opEndpointsID);
		System.out.println((endpoints == null ? "== cache miss ==" : "== cache hit ==") + " --- operatorEndpoints");

		if (endpoints == null) {

			Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			Statement st = null;
			ResultSet rs = null;
			endpoints = new ArrayList<Operatorendpoint>();

			try {

				if (con == null) {

					throw new Exception("Connection not found");
				}

				st = con.createStatement();
				String sql = "SELECT operatorid,operatorname,api,endpoint " + "FROM operatorendpoints, operators "
						+ "WHERE operatorendpoints.operatorid = operators.id";

				rs = st.executeQuery(sql);

				while (rs.next()) {

					endpoints.add(new Operatorendpoint(rs.getInt("operatorid"), rs.getString("operatorname"),
							rs.getString("api"), rs.getString("endpoint")));
				}
			} catch (Exception e) {

				DbUtils.handleException("Error while retrieving operator endpoint. ", e);
			} finally {

				DbUtils.closeAllConnections(st, con, rs);
			}
			if (!endpoints.isEmpty()) {

				cache.put(opEndpointsID, endpoints);
			}
		}

		return endpoints;
	}

	/**
	 * Application operators.
	 *
	 * @param axiataid
	 *            the axiataid
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<Operator> getApplicationOperators(Integer axiataid) throws Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		Statement st = null;
		ResultSet rs = null;
		String domainurls = "";
		List<Operator> operators = new ArrayList<Operator>();

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();
			String sql = "SELECT oa.id id,oa.applicationid,oa.operatorid,o.operatorname,o.refreshtoken,o.tokenvalidity,o.tokentime,o.token, o.tokenurl, o.tokenauth "
					+ "FROM operatorapps oa, operators o "
					+ "WHERE oa.operatorid = o.id AND oa.isactive = 1  AND oa.applicationid = '" + axiataid + "'";

			System.out.println(sql);
			rs = st.executeQuery(sql);

			while (rs.next()) {

				Operator oper = new Operator();
				oper.setId(rs.getInt("id"));
				oper.setApplicationid(rs.getInt("applicationid"));
				oper.setOperatorid(rs.getInt("operatorid"));
				oper.setOperatorname(rs.getString("operatorname"));
				oper.setRefreshtoken(rs.getString("refreshtoken"));
				oper.setTokenvalidity(rs.getLong("tokenvalidity"));
				oper.setTokentime(rs.getLong("tokentime"));
				oper.setToken(rs.getString("token"));
				oper.setTokenurl(rs.getString("tokenurl"));
				oper.setTokenauth(rs.getString("tokenauth"));
				operators.add(oper);
			}
		} catch (Exception e) {

			DbUtils.handleException("Error while selecting from operatorapps, operators. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, rs);
		}

		return operators;
	}

	/**
	 * Active application operators.
	 *
	 * @param appId
	 *            the app id
	 * @param apitype
	 *            the apitype
	 * @return the list
	 * @throws SQLException
	 *             the SQL exception
	 * @throws AxataDBUtilException
	 *             the axata db util exception
	 */
	public List<Integer> getActiveApplicationOperators(Integer appId, String apitype)
			throws SQLException, AxataDBUtilException {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<Integer> operators = new ArrayList<Integer>();

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			if (con == null) {

				throw new Exception("Connection not found.");
			}

			st = con.createStatement();
			String sql = "SELECT o.operatorid FROM endpointapps e,operatorendpoints o "
					+ " where o.id = e.endpointid AND e.applicationid = " + appId + " AND e.isactive = 1 AND o.api='"
					+ apitype + "'";

			rs = st.executeQuery(sql);

			while (rs.next()) {

				Integer operatorid = (rs.getInt("operatorid"));
				operators.add(operatorid);
			}

		} catch (Exception e) {

			DbUtils.handleException("Error while selecting from endpointapps, operatorendpoints ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, rs);
		}

		return operators;
	}

	/**
	 * Token update.
	 *
	 * @param id
	 *            the id
	 * @param refreshtoken
	 *            the refreshtoken
	 * @param tokenvalidity
	 *            the tokenvalidity
	 * @param tokentime
	 *            the tokentime
	 * @param token
	 *            the token
	 * @return the integer
	 * @throws Exception
	 *             the exception
	 */
	public Integer updateOperatorToken(int id, String refreshtoken, long tokenvalidity, long tokentime, String token)
			throws Exception {

		Connection con = null;
		Statement st = null;
		Integer newid = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();
			String sql = "UPDATE operators " + "SET refreshtoken='" + refreshtoken + "',tokenvalidity=" + tokenvalidity
					+ ",tokentime=" + tokentime + ",token='" + token + "' " + "WHERE id =" + id;

			st.executeUpdate(sql);

		} catch (Exception e) {

			DbUtils.handleException("Error while updating operators. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, null);
		}

		return newid;
	}
}
