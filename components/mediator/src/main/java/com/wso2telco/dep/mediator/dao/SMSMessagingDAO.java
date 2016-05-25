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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.wso2telco.dbutils.AxataDBUtilException;
import com.wso2telco.dbutils.DbUtils;
import com.wso2telco.dbutils.Operatorsubs;
import com.wso2telco.dbutils.util.DataSourceNames;
import com.wso2telco.dep.mediator.util.DatabaseTables;

public class SMSMessagingDAO {

	public Integer outboundSubscriptionEntry(String notifyURL, String serviceProvider) throws Exception {

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
			queryString.append("FROM ");
			queryString.append(DatabaseTables.OUTBOUND_SUBSCRIPTIONS.getTableName());

			rs = st.executeQuery(queryString.toString());
			if (rs.next()) {

				newId = rs.getInt("maxid") + 1;
			}

			StringBuilder insertQueryString = new StringBuilder("INSERT INTO ");
			insertQueryString.append(DatabaseTables.OUTBOUND_SUBSCRIPTIONS.getTableName());
			insertQueryString.append(" (axiataid, notifyurl, service_provider) ");
			insertQueryString.append("VALUES (");
			insertQueryString.append(newId);
			insertQueryString.append(", ");
			insertQueryString.append(notifyURL);
			insertQueryString.append(", ");
			insertQueryString.append(serviceProvider);
			insertQueryString.append(")");

			st.executeUpdate(insertQueryString.toString());
		} catch (Exception e) {

			DbUtils.handleException("Error while inserting in to subscriptions. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, rs);
		}

		return newId;
	}

	/**
	 * Outbound operatorsubs entry.
	 *
	 * @param domainsubs
	 *            the domainsubs
	 * @param dnSubscriptionId
	 *            the dnSubscriptionId
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean outboundOperatorsubsEntry(List<Operatorsubs> domainsubs, Integer dnSubscriptionId) throws Exception {

		Connection con = null;
		Statement st = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();

			for (Operatorsubs d : domainsubs) {

				StringBuilder queryString = new StringBuilder("INSERT INTO ");
				queryString.append(DatabaseTables.OUTBOUND_OPERATORSUBS.getTableName());
				queryString.append(" (axiataid, domainurl, operator) ");
				queryString.append("VALUES (");
				queryString.append(dnSubscriptionId);
				queryString.append(", ");
				queryString.append(d.getDomain());
				queryString.append(", ");
				queryString.append(d.getOperator());
				queryString.append(")");

				st.executeUpdate(queryString.toString());
			}
		} catch (Exception e) {

			DbUtils.handleException("Error while inserting in to operatorsubs. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, null);
		}

		return true;
	}

	/**
	 * Gets the sms request ids.
	 *
	 * @param requestID
	 *            the request id
	 * @param senderAddress
	 *            the sender address
	 * @return the sms request ids
	 * @throws AxataDBUtilException
	 *             the axata db util exception
	 */
	public Map<String, String> getSmsRequestIds(String requestId, String senderAddress) throws AxataDBUtilException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> gatewayRequestIds = new HashMap<String, String>();

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

			StringBuilder queryString = new StringBuilder("SELECT delivery_address, plugin_requestid ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.SEND_SMS_REQID.getTableName());
			queryString.append(" WHERE hub_requestid = ? AND sender_address = ?");

			ps = con.prepareStatement(queryString.toString());
			ps.setString(1, requestId);
			ps.setString(2, senderAddress);
			rs = ps.executeQuery();

			while (rs.next()) {

				gatewayRequestIds.put(rs.getString("delivery_address"), rs.getString("plugin_requestid"));
			}
		} catch (Exception e) {

			DbUtils.handleException("Error while inserting in to sendsms_reqid. ", e);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return gatewayRequestIds;
	}

	public Integer subscriptionEntry(String notifyURL, String serviceProvider) throws Exception {

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
			queryString.append("FROM ");
			queryString.append(DatabaseTables.SUBSCRIPTIONS.getTableName());

			rs = st.executeQuery(queryString.toString());
			if (rs.next()) {

				newId = rs.getInt("maxid") + 1;
			}

			StringBuilder insertQueryString = new StringBuilder("INSERT INTO ");
			insertQueryString.append(DatabaseTables.SUBSCRIPTIONS.getTableName());
			insertQueryString.append(" (axiataid, notifyurl, service_provider) ");
			insertQueryString.append("VALUES (");
			insertQueryString.append(newId);
			insertQueryString.append(", ");
			insertQueryString.append(notifyURL);
			insertQueryString.append(", ");
			insertQueryString.append(serviceProvider);
			insertQueryString.append(")");

			st.executeUpdate(insertQueryString.toString());
		} catch (Exception e) {

			DbUtils.handleException("Error while inserting in to subscriptions. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, rs);
		}

		return newId;
	}

	/**
	 * Operatorsubs entry.
	 *
	 * @param domainsubs
	 *            the domainsubs
	 * @param moSubscriptionId
	 *            the moSubscriptionId
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean operatorsubsEntry(List<Operatorsubs> domainsubs, Integer moSubscriptionId) throws Exception {

		Connection con = null;
		Statement st = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();

			for (Operatorsubs d : domainsubs) {

				StringBuilder queryString = new StringBuilder("INSERT INTO ");
				queryString.append(DatabaseTables.OPERATORSUBS.getTableName());
				queryString.append(" (axiataid, domainurl, operator) ");
				queryString.append("VALUES (");
				queryString.append(moSubscriptionId);
				queryString.append(", ");
				queryString.append(d.getDomain());
				queryString.append(", ");
				queryString.append(d.getOperator());
				queryString.append(")");

				st.executeUpdate(queryString.toString());
			}

		} catch (Exception e) {

			DbUtils.handleException("Error while inserting in to operatorsubs. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, null);
		}

		return true;
	}

	/**
	 * Subscription query.
	 *
	 * @param moSubscriptionId
	 *            the moSubscriptionId
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<Operatorsubs> subscriptionQuery(Integer moSubscriptionId) throws Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		Statement st = null;
		ResultSet rs = null;
		List<Operatorsubs> domainsubs = new ArrayList();

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();

			StringBuilder queryString = new StringBuilder("SELECT domainurl, operator ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.OPERATORSUBS.getTableName());
			queryString.append(" WHERE axiataid = ");
			queryString.append(moSubscriptionId);

			rs = st.executeQuery(queryString.toString());

			while (rs.next()) {

				domainsubs.add(new Operatorsubs(rs.getString("operator"), rs.getString("domainurl")));
			}

		} catch (Exception e) {

			DbUtils.handleException("Error while selecting selecting from operatorsubs. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, rs);
		}

		return domainsubs;
	}

	/**
	 * Subscription delete.
	 *
	 * @param moSubscriptionId
	 *            the moSubscriptionId
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean subscriptionDelete(Integer moSubscriptionId) throws Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		Statement st = null;

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();

			StringBuilder deleteSubscriptionsQueryString = new StringBuilder("DELETE FROM ");
			deleteSubscriptionsQueryString.append(DatabaseTables.SUBSCRIPTIONS.getTableName());
			deleteSubscriptionsQueryString.append(" WHERE axiataid = ");
			deleteSubscriptionsQueryString.append(moSubscriptionId);

			st.executeUpdate(deleteSubscriptionsQueryString.toString());

			StringBuilder deleteOperatorSubscriptionsQueryString = new StringBuilder("DELETE FROM ");
			deleteOperatorSubscriptionsQueryString.append(DatabaseTables.OPERATORSUBS.getTableName());
			deleteOperatorSubscriptionsQueryString.append(" WHERE axiataid = ");
			deleteOperatorSubscriptionsQueryString.append(moSubscriptionId);

			st.executeUpdate(deleteOperatorSubscriptionsQueryString.toString());
		} catch (Exception e) {

			DbUtils.handleException("Error while deleting subscriptions. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, null);
		}

		return true;
	}

	/**
	 * Insert sms request ids.
	 *
	 * @param requestID
	 *            the request id
	 * @param senderAddress
	 *            the sender address
	 * @param pluginRequestIDs
	 *            the plugin request i ds
	 * @return true, if successful
	 * @throws AxataDBUtilException
	 *             the axata db util exception
	 */
	public boolean insertSmsRequestIds(String requestId, String senderAddress, Map<String, String> gatewayRequestIds)
			throws AxataDBUtilException {

		Connection con = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

			StringBuilder queryString = new StringBuilder("INSERT INTO ");
			queryString.append(DatabaseTables.SEND_SMS_REQID.getTableName());
			queryString.append(" (hub_requestid, sender_address, delivery_address, plugin_requestid) ");
			queryString.append("VALUES (?, ?, ?, ?)");

			ps = con.prepareStatement(queryString.toString());
			ps.setString(1, requestId);
			ps.setString(2, senderAddress);

			for (Map.Entry<String, String> entry : gatewayRequestIds.entrySet()) {

				ps.setString(3, entry.getKey());
				ps.setString(4, entry.getValue());
				ps.executeUpdate();
			}
		} catch (Exception e) {

			DbUtils.handleException("Error while inserting in to sendsms_reqid. ", e);
		} finally {

			DbUtils.closeAllConnections(ps, con, null);
		}

		return true;
	}

	public HashMap<String, String> subscriptionNotifiMap(Integer moSubscriptionId) throws Exception {

		HashMap<String, String> subscriptionDetails = new HashMap<String, String>();
		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		Statement st = null;
		ResultSet rs = null;

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();

			StringBuilder queryString = new StringBuilder("SELECT notifyurl, service_provider ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.SUBSCRIPTIONS.getTableName());
			queryString.append(" WHERE axiataid = ");
			queryString.append(moSubscriptionId);

			rs = st.executeQuery(queryString.toString());

			if (rs.next()) {

				subscriptionDetails.put("notifyurl", rs.getString("notifyurl"));
				subscriptionDetails.put("serviceProvider", rs.getString("service_provider"));
			}
		} catch (Exception e) {

			DbUtils.handleException("Error while selecting from subscriptions. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, rs);
		}

		return subscriptionDetails;
	}

	public HashMap<String, String> subscriptionDNNotifiMap(Integer dnSubscriptionId) throws Exception {

		HashMap<String, String> dnSubscriptionDetails = new HashMap<String, String>();
		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		Statement st = null;
		ResultSet rs = null;

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();

			StringBuilder queryString = new StringBuilder("SELECT notifyurl, service_provider ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.OUTBOUND_SUBSCRIPTIONS.getTableName());
			queryString.append(" WHERE axiataid = ");
			queryString.append(dnSubscriptionId);

			rs = st.executeQuery(queryString.toString());

			if (rs.next()) {

				dnSubscriptionDetails.put("notifyurl", rs.getString("notifyurl"));
				dnSubscriptionDetails.put("serviceProvider", rs.getString("service_provider"));
			}

		} catch (Exception e) {

			DbUtils.handleException("Error while selecting from subscriptions. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, rs);
		}

		return dnSubscriptionDetails;
	}

	/**
	 * Outboud subscription query.
	 *
	 * @param dnSubscriptionId
	 *            the dnSubscriptionId
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<Operatorsubs> outboudSubscriptionQuery(Integer dnSubscriptionId) throws Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		Statement st = null;
		ResultSet rs = null;
		List<Operatorsubs> domainsubs = new ArrayList();

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();

			StringBuilder queryString = new StringBuilder("SELECT domainurl, operator ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.OUTBOUND_OPERATORSUBS.getTableName());
			queryString.append(" WHERE axiataid = ");
			queryString.append(dnSubscriptionId);

			rs = st.executeQuery(queryString.toString());

			while (rs.next()) {

				domainsubs.add(new Operatorsubs(rs.getString("operator"), rs.getString("domainurl")));
			}

		} catch (Exception e) {

			DbUtils.handleException("Error while selecting selecting from operatorsubs. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, rs);
		}

		return domainsubs;
	}

	/**
	 * Outbound subscription delete.
	 *
	 * @param dnSubscriptionId
	 *            the dnSubscriptionId
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean outboundSubscriptionDelete(Integer dnSubscriptionId) throws Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		Statement st = null;

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();

			StringBuilder deleteSubscriptionsQueryString = new StringBuilder("DELETE FROM ");
			deleteSubscriptionsQueryString.append(DatabaseTables.OUTBOUND_SUBSCRIPTIONS.getTableName());
			deleteSubscriptionsQueryString.append(" WHERE axiataid = ");
			deleteSubscriptionsQueryString.append(dnSubscriptionId);

			st.executeUpdate(deleteSubscriptionsQueryString.toString());

			StringBuilder deleteOperatorSubscriptionsQueryString = new StringBuilder("DELETE FROM ");
			deleteOperatorSubscriptionsQueryString.append(DatabaseTables.OUTBOUND_OPERATORSUBS.getTableName());
			deleteOperatorSubscriptionsQueryString.append(" WHERE axiataid = ");
			deleteOperatorSubscriptionsQueryString.append(dnSubscriptionId);

			st.executeUpdate(deleteOperatorSubscriptionsQueryString.toString());
		} catch (Exception e) {

			DbUtils.handleException("Error while deleting subscriptions. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, null);
		}

		return true;
	}
}
