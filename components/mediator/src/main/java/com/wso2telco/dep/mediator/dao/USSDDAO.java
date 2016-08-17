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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.dbutils.DbUtils;
import com.wso2telco.dbutils.util.DataSourceNames;
import com.wso2telco.dep.mediator.util.DatabaseTables;
import com.wso2telco.dep.operatorservice.model.OperatorSubscriptionDTO;

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
			
			log.debug("sql query in ussdRequestEntry : " + ps);

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				newId = rs.getInt(1);
			}
		} catch (SQLException e) {

			log.error("database operation error in ussdRequestEntry : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in ussdRequestEntry : ", e);
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
	public String getUSSDNotifyURL(Integer subscriptionId) throws SQLException, Exception {

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
			
			log.debug("sql query in getUSSDNotifyURL : " + ps);

			rs = ps.executeQuery();

			if (rs.next()) {

				notifyurls = rs.getString("notifyurl");
			}

		} catch (SQLException e) {

			log.error("database operation error in getUSSDNotify : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in getUSSDNotify : ", e);
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
			
			log.debug("sql query in ussdEntryDelete : " + ps);

			ps.executeUpdate();
		} catch (SQLException e) {

			log.error("database operation error in ussdEntryDelete : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in ussdEntryDelete : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, null);
		}

		return true;
	}
	
	
	public void moUssdSubscriptionEntry(List<OperatorSubscriptionDTO> domainsubs,Integer moSubscriptionId) throws SQLException, Exception{

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		PreparedStatement insertStatement = null;
		
		try {
			
			if (con == null) {
				throw new Exception("Connection not found");
			}
			con.setAutoCommit(false);
			
			StringBuilder queryString = new StringBuilder("INSERT INTO ");
			queryString.append(DatabaseTables.MO_USSD_SUBSCRIPTIONS.getTableName());
			queryString.append(" (ussd_request_did, domainurl, operator) ");
			queryString.append("VALUES (?, ?, ?)");
			
			insertStatement = con.prepareStatement(queryString.toString());

			for (OperatorSubscriptionDTO d : domainsubs) {
				
				insertStatement.setInt(1, moSubscriptionId);
				insertStatement.setString(2, d.getDomain());
				insertStatement.setString(3, d.getOperator());
				
				insertStatement.addBatch();
			}
			
			insertStatement.executeBatch();
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
		}
	}
	
	/**
	 * 
	 * @param moSubscriptionId
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	
	public List<OperatorSubscriptionDTO> moUssdSubscriptionQuery(Integer moSubscriptionId) throws SQLException, Exception {
	
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
			queryString.append(DatabaseTables.MO_USSD_SUBSCRIPTIONS.getTableName());
			queryString.append(" WHERE ussd_request_did = ?");

			ps = con.prepareStatement(queryString.toString());

			ps.setInt(1, moSubscriptionId);
			
			log.debug("sql query in subscriptionQuery : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				domainsubs.add(new OperatorSubscriptionDTO(rs.getString("operator"), rs.getString("domainurl")));
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
	
	
	public void moUssdSubscriptionDelete(Integer moSubscriptionId) throws SQLException, Exception {

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
			deleteSubscriptionsQueryString.append(DatabaseTables.USSD_REQUEST_ENTRY.getTableName());
			deleteSubscriptionsQueryString.append(" WHERE axiataid = ?");

			deleteSubscriptionsStatement = con.prepareStatement(deleteSubscriptionsQueryString.toString());

			deleteSubscriptionsStatement.setInt(1, moSubscriptionId);
			
			log.debug("sql query in outboundSubscriptionDelete : " + deleteSubscriptionsStatement);

			deleteSubscriptionsStatement.executeUpdate();

			StringBuilder deleteOperatorSubscriptionsQueryString = new StringBuilder("DELETE FROM ");
			deleteOperatorSubscriptionsQueryString.append(DatabaseTables.MO_USSD_SUBSCRIPTIONS.getTableName());
			deleteOperatorSubscriptionsQueryString.append(" WHERE axiataid = ?");

			deleteOperatorSubscriptionsStatement = con.prepareStatement(deleteOperatorSubscriptionsQueryString.toString());

			deleteOperatorSubscriptionsStatement.setInt(1, moSubscriptionId);
			
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

			log.error("database operation error in moUssdSubscriptionDelete : ", e);
			throw e;
		} catch (Exception e) {

			/**
			 * rollback if Exception occurs
			 */
			con.rollback();

			log.error("Error while deleting subscriptions. ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(deleteSubscriptionsStatement, con, null);
			DbUtils.closeAllConnections(deleteOperatorSubscriptionsStatement, null, null);
		}
	}
}
