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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.dbutils.DbUtils;
import com.wso2telco.dbutils.Operatorsubs;
import com.wso2telco.dep.operatorservice.model.OperatorSubscriptionDTO;
import com.wso2telco.dbutils.util.DataSourceNames;
import com.wso2telco.dep.mediator.util.DatabaseTables;

public class SMSMessagingDAO {

	/** The Constant log. */
	private final Log log = LogFactory.getLog(SMSMessagingDAO.class);

	public Integer outboundSubscriptionEntry(String notifyURL, String serviceProvider) throws SQLException, Exception {

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
			insertQueryString.append(DatabaseTables.OUTBOUND_SUBSCRIPTIONS.getTableName());
			insertQueryString.append(" (notifyurl, service_provider, is_active) ");
			insertQueryString.append("VALUES (?, ?, ?)");

			ps = con.prepareStatement(insertQueryString.toString(), Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, notifyURL);
			ps.setString(2, serviceProvider);
			ps.setInt(3, 0);
			
			log.debug("sql query in outboundSubscriptionEntry : " + ps);

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				newId = rs.getInt(1);
			}
		} catch (SQLException e) {

			log.error("database operation error in outboundSubscriptionEntry : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in outboundSubscriptionEntry : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
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
	public void outboundOperatorsubsEntry(List<OperatorSubscriptionDTO> domainsubs, Integer dnSubscriptionId)
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
			queryString.append(DatabaseTables.OUTBOUND_OPERATORSUBS.getTableName());
			queryString.append(" (dn_subscription_did, domainurl, operator) ");
			queryString.append("VALUES (?, ?, ?)");

			insertStatement = con.prepareStatement(queryString.toString());

			for (OperatorSubscriptionDTO d : domainsubs) {

				insertStatement.setInt(1, dnSubscriptionId);
				insertStatement.setString(2, d.getDomain());
				insertStatement.setString(3, d.getOperator());

				insertStatement.addBatch();
			}
			
			log.debug("sql query in outboundOperatorsubsEntry : " + insertStatement);

			insertStatement.executeBatch();

			StringBuilder updateQueryString = new StringBuilder("UPDATE ");
			updateQueryString.append(DatabaseTables.OUTBOUND_SUBSCRIPTIONS.getTableName());
			updateQueryString.append(" SET is_active = ?");
			updateQueryString.append(" WHERE dn_subscription_did = ?");

			updateStatement = con.prepareStatement(updateQueryString.toString());

			updateStatement.setInt(1, 1);
			updateStatement.setInt(2, dnSubscriptionId);
			
			log.debug("sql query in outboundOperatorsubsEntry : " + updateStatement);

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

			log.error("database operation error in outboundOperatorsubsEntry : ", e);
			throw e;
		} catch (Exception e) {

			/**
			 * rollback if Exception occurs
			 */
			con.rollback();

			log.error("error in outboundOperatorsubsEntry : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(insertStatement, con, null);
			DbUtils.closeAllConnections(updateStatement, null, null);
		}
	}
	
	
	
	public boolean operatorsubsEntry(List<Operatorsubs> domainsubs, Integer dnSubscriptionId) throws SQLException, Exception {

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
			queryString.append(DatabaseTables.OUTBOUND_OPERATORSUBS.getTableName());
			queryString.append(" (dn_subscription_did, domainurl, operator) ");
			queryString.append("VALUES (?, ?, ?)");

			insertStatement = con.prepareStatement(queryString.toString());

			for (Operatorsubs d : domainsubs) {

				insertStatement.setInt(1, dnSubscriptionId);
				insertStatement.setString(2, d.getDomain());
				insertStatement.setString(3, d.getOperator());

				insertStatement.addBatch();
			}

			log.debug("sql query in outboundOperatorsubsEntry : "+ insertStatement);

			insertStatement.executeBatch();

			StringBuilder updateQueryString = new StringBuilder("UPDATE ");
			updateQueryString.append(DatabaseTables.OUTBOUND_SUBSCRIPTIONS.getTableName());
			updateQueryString.append(" SET is_active = ?");
			updateQueryString.append(" WHERE dn_subscription_did = ?");

			updateStatement = con.prepareStatement(updateQueryString.toString());

			updateStatement.setInt(1, 1);
			updateStatement.setInt(2, dnSubscriptionId);

			log.debug("sql query in outboundOperatorsubsEntry : "+ updateStatement);

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

			log.error("database operation error in outboundOperatorsubsEntry : ",e);
			throw e;
		} catch (Exception e) {

			/**
			 * rollback if Exception occurs
			 */
			con.rollback();

			log.error("error in outboundOperatorsubsEntry : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(insertStatement, con, null);
			DbUtils.closeAllConnections(updateStatement, null, null);
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
	 */
	public Map<String, String> getSMSRequestIds(String requestId, String senderAddress) throws SQLException, Exception {

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
			
			log.debug("sql query in getSMSRequestIds : " + ps);
			
			rs = ps.executeQuery();

			while (rs.next()) {

				gatewayRequestIds.put(rs.getString("delivery_address"), rs.getString("plugin_requestid"));
			}
		} catch (SQLException e) {

			log.error("database operation error in getSMSRequestIds : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in getSMSRequestIds : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return gatewayRequestIds;
	}

	public Integer subscriptionEntry(String notifyURL) throws SQLException, Exception {

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
			insertQueryString.append(DatabaseTables.SUBSCRIPTIONS.getTableName());
			insertQueryString.append(" (notifyurl, is_active) ");
			insertQueryString.append("VALUES (?, ?)");

			ps = con.prepareStatement(insertQueryString.toString(), Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, notifyURL);
			ps.setInt(2, 0);
			
			log.debug("sql query in subscriptionEntry : " + ps);

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				newId = rs.getInt(1);
			}
		} catch (SQLException e) {

			log.error("database operation error in subscriptionEntry : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in subscriptionEntry : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
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
	public void operatorSubsEntry(List<OperatorSubscriptionDTO> domainsubs, Integer moSubscriptionId)
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
			queryString.append(DatabaseTables.OPERATORSUBS.getTableName());
			queryString.append(" (mo_subscription_did, domainurl, operator) ");
			queryString.append("VALUES (?, ?, ?)");

			insertStatement = con.prepareStatement(queryString.toString());

			for (OperatorSubscriptionDTO d : domainsubs) {

				insertStatement.setInt(1, moSubscriptionId);
				insertStatement.setString(2, d.getDomain());
				insertStatement.setString(3, d.getOperator());

				insertStatement.addBatch();
			}
			
			log.debug("sql query in operatorSubsEntry : " + insertStatement);

			insertStatement.executeBatch();

			StringBuilder updateQueryString = new StringBuilder("UPDATE ");
			updateQueryString.append(DatabaseTables.SUBSCRIPTIONS.getTableName());
			updateQueryString.append(" SET is_active = ?");
			updateQueryString.append(" WHERE mo_subscription_did = ?");

			updateStatement = con.prepareStatement(updateQueryString.toString());

			updateStatement.setInt(1, 1);
			updateStatement.setInt(2, moSubscriptionId);
			
			log.debug("sql query in operatorSubsEntry : " + updateStatement);

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

			log.error("database operation error in operatorSubsEntry : ", e);
			throw e;
		} catch (Exception e) {

			/**
			 * rollback if Exception occurs
			 */
			con.rollback();

			log.error("error in operatorSubsEntry : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(insertStatement, con, null);
			DbUtils.closeAllConnections(updateStatement, null, null);
		}
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
	public List<Operatorsubs> subscriptionQuery(Integer moSubscriptionId) throws SQLException, Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Operatorsubs> domainsubs = new ArrayList();

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder queryString = new StringBuilder("SELECT domainurl, operator ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.OPERATORSUBS.getTableName());
			queryString.append(" WHERE mo_subscription_did = ?");

			ps = con.prepareStatement(queryString.toString());

			ps.setInt(1, moSubscriptionId);
			
			log.debug("sql query in subscriptionQuery : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				domainsubs.add(new Operatorsubs(rs.getString("operator"), rs.getString("domainurl")));
			}
		} catch (SQLException e) {

			log.error("database operation error in subscriptionQuery : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in subscriptionQuery : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
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
	public void subscriptionDelete(Integer moSubscriptionId) throws SQLException, Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		PreparedStatement deleteSubscriptionsStatement = null;
		PreparedStatement deleteOperatorSubscriptionsStatement = null;

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			/**
			 * Set autocommit off to handle the transaction
			 */
			con.setAutoCommit(false);

			StringBuilder deleteSubscriptionsQueryString = new StringBuilder("DELETE FROM ");
			deleteSubscriptionsQueryString.append(DatabaseTables.SUBSCRIPTIONS.getTableName());
			deleteSubscriptionsQueryString.append(" WHERE mo_subscription_did = ?");

			deleteSubscriptionsStatement = con.prepareStatement(deleteSubscriptionsQueryString.toString());

			deleteSubscriptionsStatement.setInt(1, moSubscriptionId);
			
			log.debug("sql query in subscriptionDelete : " + deleteSubscriptionsStatement);

			deleteSubscriptionsStatement.executeUpdate();

			StringBuilder deleteOperatorSubscriptionsQueryString = new StringBuilder("DELETE FROM ");
			deleteOperatorSubscriptionsQueryString.append(DatabaseTables.OPERATORSUBS.getTableName());
			deleteOperatorSubscriptionsQueryString.append(" WHERE mo_subscription_did = ?");

			deleteOperatorSubscriptionsStatement = con
					.prepareStatement(deleteOperatorSubscriptionsQueryString.toString());

			deleteOperatorSubscriptionsStatement.setInt(1, moSubscriptionId);
			
			log.debug("sql query in subscriptionDelete : " + deleteOperatorSubscriptionsStatement);

			deleteOperatorSubscriptionsStatement.executeUpdate();

			/**
			 * commit the transaction if all success
			 */
			con.commit();
		} catch (SQLException e) {

			/**
			 * rollback if Exception occurs
			 */
			con.rollback();

			log.error("database operation error in subscriptionDelete : ", e);
			throw e;
		} catch (Exception e) {

			/**
			 * rollback if Exception occurs
			 */
			con.rollback();

			log.error("error in subscriptionDelete : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(deleteSubscriptionsStatement, con, null);
			DbUtils.closeAllConnections(deleteOperatorSubscriptionsStatement, null, null);
		}
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
	 * @throws Exception
	 */
	public void insertSMSRequestIds(String requestId, String senderAddress, Map<String, String> gatewayRequestIds)
			throws SQLException, Exception {

		Connection con = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

			/**
			 * Set autocommit off to handle the transaction
			 */
			con.setAutoCommit(false);

			StringBuilder queryString = new StringBuilder("INSERT INTO ");
			queryString.append(DatabaseTables.SEND_SMS_REQID.getTableName());
			queryString.append(" (hub_requestid, sender_address, delivery_address, plugin_requestid) ");
			queryString.append("VALUES (?, ?, ?, ?)");

			ps = con.prepareStatement(queryString.toString());

			for (Map.Entry<String, String> entry : gatewayRequestIds.entrySet()) {

				ps.setString(1, requestId);
				ps.setString(2, senderAddress);
				ps.setString(3, entry.getKey());
				ps.setString(4, entry.getValue());

				ps.addBatch();
			}
			
			log.debug("sql query in insertSMSRequestIds : " + ps);

			ps.executeBatch();

			/**
			 * commit the transaction if all success
			 */
			con.commit();
		} catch (SQLException e) {

			/**
			 * rollback if Exception occurs
			 */
			con.rollback();

			log.error("database operation error in insertSMSRequestIds : ", e);
			throw e;
		} catch (Exception e) {

			/**
			 * rollback if Exception occurs
			 */
			con.rollback();

			log.error("error in insertSMSRequestIds : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, null);
		}
	}

	public Map<String, String> subscriptionNotifiMap(Integer moSubscriptionId) throws SQLException, Exception {

		Map<String, String> subscriptionDetails = new HashMap<String, String>();
		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder queryString = new StringBuilder("SELECT notifyurl, service_provider ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.SUBSCRIPTIONS.getTableName());
			queryString.append(" WHERE mo_subscription_did = ?");

			ps = con.prepareStatement(queryString.toString());

			ps.setInt(1, moSubscriptionId);
			
			log.debug("sql query in subscriptionNotifiMap : " + ps);

			rs = ps.executeQuery();

			if (rs.next()) {

				subscriptionDetails.put("notifyurl", rs.getString("notifyurl"));
				subscriptionDetails.put("serviceProvider", rs.getString("service_provider"));
			}
		} catch (SQLException e) {

			log.error("database operation error in subscriptionNotifiMap : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in subscriptionNotifiMap : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return subscriptionDetails;
	}

	public Map<String, String> subscriptionDNNotifiMap(Integer dnSubscriptionId) throws SQLException, Exception {

		Map<String, String> dnSubscriptionDetails = new HashMap<String, String>();
		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder queryString = new StringBuilder("SELECT notifyurl, service_provider ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.OUTBOUND_SUBSCRIPTIONS.getTableName());
			queryString.append(" WHERE dn_subscription_did = ?");

			ps = con.prepareStatement(queryString.toString());

			ps.setInt(1, dnSubscriptionId);
			
			log.debug("sql query in subscriptionDNNotifiMap : " + ps);

			rs = ps.executeQuery();

			if (rs.next()) {

				dnSubscriptionDetails.put("notifyurl", rs.getString("notifyurl"));
				dnSubscriptionDetails.put("serviceProvider", rs.getString("service_provider"));
			}
		} catch (SQLException e) {

			log.error("database operation error in subscriptionDNNotifiMap : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in subscriptionDNNotifiMap : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
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
	public List<OperatorSubscriptionDTO> outboudSubscriptionQuery(Integer dnSubscriptionId)
			throws SQLException, Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<OperatorSubscriptionDTO> domainsubs = new ArrayList();

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder queryString = new StringBuilder("SELECT domainurl, operator ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.OUTBOUND_OPERATORSUBS.getTableName());
			queryString.append(" WHERE dn_subscription_did = ?");

			ps = con.prepareStatement(queryString.toString());

			ps.setInt(1, dnSubscriptionId);
			
			log.debug("sql query in outboudSubscriptionQuery : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				domainsubs.add(new OperatorSubscriptionDTO(rs.getString("operator"), rs.getString("domainurl")));
			}

		} catch (SQLException e) {

			log.error("database operation error in outboudSubscriptionQuery : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in outboudSubscriptionQuery : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
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
	public void outboundSubscriptionDelete(Integer dnSubscriptionId) throws SQLException, Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		PreparedStatement deleteSubscriptionsStatement = null;
		PreparedStatement deleteOperatorSubscriptionsStatement = null;

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			/**
			 * Set autocommit off to handle the transaction
			 */
			con.setAutoCommit(false);

			StringBuilder deleteSubscriptionsQueryString = new StringBuilder("DELETE FROM ");
			deleteSubscriptionsQueryString.append(DatabaseTables.OUTBOUND_SUBSCRIPTIONS.getTableName());
			deleteSubscriptionsQueryString.append(" WHERE dn_subscription_did = ?");

			deleteSubscriptionsStatement = con.prepareStatement(deleteSubscriptionsQueryString.toString());

			deleteSubscriptionsStatement.setInt(1, dnSubscriptionId);
			
			log.debug("sql query in outboundSubscriptionDelete : " + deleteSubscriptionsStatement);

			deleteSubscriptionsStatement.executeUpdate();

			StringBuilder deleteOperatorSubscriptionsQueryString = new StringBuilder("DELETE FROM ");
			deleteOperatorSubscriptionsQueryString.append(DatabaseTables.OUTBOUND_OPERATORSUBS.getTableName());
			deleteOperatorSubscriptionsQueryString.append(" WHERE dn_subscription_did = ?");

			deleteOperatorSubscriptionsStatement = con
					.prepareStatement(deleteOperatorSubscriptionsQueryString.toString());

			deleteOperatorSubscriptionsStatement.setInt(1, dnSubscriptionId);
			
			log.debug("sql query in outboundSubscriptionDelete : " + deleteOperatorSubscriptionsStatement);

			deleteOperatorSubscriptionsStatement.executeUpdate();

			/**
			 * commit the transaction if all success
			 */
			con.commit();
		} catch (SQLException e) {

			/**
			 * rollback if Exception occurs
			 */
			con.rollback();

			log.error("database operation error in outboundSubscriptionDelete : ", e);
			throw e;
		} catch (Exception e) {

			/**
			 * rollback if Exception occurs
			 */
			con.rollback();

			log.error("error in outboundSubscriptionDelete : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(deleteSubscriptionsStatement, con, null);
			DbUtils.closeAllConnections(deleteOperatorSubscriptionsStatement, null, null);
		}
	}
}
