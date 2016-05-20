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
	 * Operator endPoints.
	 *
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<Operatorendpoint> getOperatorEndpoints() throws Exception {

		final int opEndpointsID = 0;

		Cache<Integer, List<Operatorendpoint>> cache = Caching.getCacheManager(AXIATA_MEDIATOR_CACHE_MANAGER)
				.getCache("axiataDbOperatorEndpoints");
		List<Operatorendpoint> endPoints = cache.get(opEndpointsID);

		if (endPoints == null) {

			Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			Statement st = null;
			ResultSet rs = null;
			endPoints = new ArrayList<Operatorendpoint>();

			try {

				if (con == null) {

					throw new Exception("Connection not found");
				}

				st = con.createStatement();

				StringBuilder queryString = new StringBuilder("SELECT operatorid, operatorname, api, endpoint ");
				queryString.append("FROM operatorendpoints, operators ");
				queryString.append("WHERE operatorendpoints.operatorid = operators.id");

				rs = st.executeQuery(queryString.toString());

				while (rs.next()) {

					endPoints.add(new Operatorendpoint(rs.getInt("operatorid"), rs.getString("operatorname"),
							rs.getString("api"), rs.getString("endpoint")));
				}
			} catch (Exception e) {

				DbUtils.handleException("Error while retrieving operator endpoint. ", e);
			} finally {

				DbUtils.closeAllConnections(st, con, rs);
			}
			if (!endPoints.isEmpty()) {

				cache.put(opEndpointsID, endPoints);
			}
		}

		return endPoints;
	}

	/**
	 * Application operators.
	 *
	 * @param applicationId
	 *            the applicationId
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<Operator> getApplicationOperators(Integer applicationId) throws Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		Statement st = null;
		ResultSet rs = null;
		List<Operator> operators = new ArrayList<Operator>();

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();

			StringBuilder queryString = new StringBuilder(
					"SELECT oa.id id, oa.applicationid, oa.operatorid, o.operatorname, o.refreshtoken, o.tokenvalidity, o.tokentime, o.token, o.tokenurl, o.tokenauth ");
			queryString.append("FROM operatorapps oa, operators o ");
			queryString.append("WHERE oa.operatorid = o.id AND oa.isactive = 1  AND oa.applicationid = ");
			queryString.append(applicationId);

			rs = st.executeQuery(queryString.toString());

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
	 *            the appId
	 * @param apiType
	 *            the apiType
	 * @return the list
	 * @throws SQLException
	 *             the SQL exception
	 * @throws AxataDBUtilException
	 *             the AxataDBUtilException
	 */
	public List<Integer> getActiveApplicationOperators(Integer appId, String apiType)
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

			StringBuilder queryString = new StringBuilder("SELECT o.operatorid ");
			queryString.append("FROM endpointapps e, operatorendpoints o ");
			queryString.append("WHERE o.id = e.endpointid AND e.applicationid = ");
			queryString.append(appId);
			queryString.append(" AND e.isactive = 1 AND o.api = ");
			queryString.append(apiType);

			rs = st.executeQuery(queryString.toString());

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
	 * @param refreshToken
	 *            the refreshToken
	 * @param tokenValidity
	 *            the tokenValidity
	 * @param tokenTime
	 *            the tokenTime
	 * @param token
	 *            the token
	 * @return the integer
	 * @throws Exception
	 *             the exception
	 */
	public Integer updateOperatorToken(int id, String refreshToken, long tokenValidity, long tokenTime, String token)
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

			StringBuilder queryString = new StringBuilder("UPDATE operators ");
			queryString.append("SET refreshtoken = ");
			queryString.append(refreshToken);
			queryString.append(" ,tokenvalidity = ");
			queryString.append(tokenValidity);
			queryString.append(" ,tokentime = ");
			queryString.append(tokenTime);
			queryString.append(" ,token = ");
			queryString.append(token);
			queryString.append(" WHERE id = ");
			queryString.append(id);

			st.executeUpdate(queryString.toString());
		} catch (Exception e) {

			DbUtils.handleException("Error while updating operators. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, null);
		}

		return newid;
	}
}
