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

public class SMSMessagingDAO {

	public Integer outboundSubscriptionEntry(String notifyurl, String serviceProvider) throws Exception {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Integer newid = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();
			String sql = "SELECT MAX(axiataid) maxid " + "FROM outbound_subscriptions";

			rs = st.executeQuery(sql);
			if (rs.next()) {
				newid = rs.getInt("maxid") + 1;
			}

			sql = "INSERT INTO outbound_subscriptions (axiataid,notifyurl,service_provider) " + "VALUES (" + newid
					+ ",'" + notifyurl + "', '" + serviceProvider + "')";
			st.executeUpdate(sql);

		} catch (Exception e) {

			DbUtils.handleException("Error while inserting in to subscriptions. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, rs);
		}

		return newid;
	}

	/**
	 * Outbound operatorsubs entry.
	 *
	 * @param domainsubs
	 *            the domainsubs
	 * @param axiataid
	 *            the axiataid
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean outboundOperatorsubsEntry(List<Operatorsubs> domainsubs, Integer axiataid) throws Exception {

		Connection con = null;
		Statement st = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();
			String sql = null;

			for (Operatorsubs d : domainsubs) {

				sql = "INSERT INTO outbound_operatorsubs (axiataid,domainurl,operator) " + "VALUES (" + axiataid + ","
						+ "'" + d.getDomain() + "','" + d.getOperator() + "')";
				st.executeUpdate(sql);
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
	public Map<String, String> getSmsRequestIds(String requestID, String senderAddress) throws AxataDBUtilException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> pluginRequestIDs = new HashMap<String, String>();

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

			String sql = "SELECT delivery_address, plugin_requestid from sendsms_reqid where hub_requestid=? AND "
					+ "sender_address=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, requestID);
			ps.setString(2, senderAddress);
			rs = ps.executeQuery();

			while (rs.next()) {

				pluginRequestIDs.put(rs.getString("delivery_address"), rs.getString("plugin_requestid"));
			}
		} catch (Exception e) {

			DbUtils.handleException("Error while inserting in to sendsms_reqid. ", e);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return pluginRequestIDs;
	}

	public Integer subscriptionEntry(String notifyurl, String serviceProvider) throws Exception {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Integer newid = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();
			String sql = "SELECT MAX(axiataid) maxid " + "FROM subscriptions";

			rs = st.executeQuery(sql);
			if (rs.next()) {

				newid = rs.getInt("maxid") + 1;
			}

			sql = "INSERT INTO subscriptions (axiataid,notifyurl,service_provider) " + "VALUES (" + newid + ",'"
					+ notifyurl + "', '" + serviceProvider + "')";

			st.executeUpdate(sql);

		} catch (Exception e) {

			DbUtils.handleException("Error while inserting in to subscriptions. ", e);
		} finally {

			DbUtils.closeAllConnections(st, con, rs);
		}

		return newid;
	}

	/**
	 * Operatorsubs entry.
	 *
	 * @param domainsubs
	 *            the domainsubs
	 * @param axiataid
	 *            the axiataid
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean operatorsubsEntry(List<Operatorsubs> domainsubs, Integer axiataid) throws Exception {

		Connection con = null;
		Statement st = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();
			String sql = null;

			for (Operatorsubs d : domainsubs) {

				sql = "INSERT INTO operatorsubs (axiataid,domainurl,operator) " + "VALUES (" + axiataid + "," + "'"
						+ d.getDomain() + "','" + d.getOperator() + "')";
				st.executeUpdate(sql);
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
	 * @param axiataid
	 *            the axiataid
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<Operatorsubs> subscriptionQuery(Integer axiataid) throws Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		Statement st = null;
		ResultSet rs = null;
		List<Operatorsubs> domainsubs = new ArrayList();
		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();
			String sql = "SELECT domainurl,operator " + "FROM operatorsubs " + "WHERE axiataid = " + axiataid + "";

			rs = st.executeQuery(sql);

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
	 * @param axiataid
	 *            the axiataid
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean subscriptionDelete(Integer axiataid) throws Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		Statement st = null;

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();
			String sql = "DELETE FROM subscriptions " + "WHERE axiataid = " + axiataid + "";

			st.executeUpdate(sql);

			sql = "DELETE FROM operatorsubs " + "WHERE axiataid = " + axiataid + "";

			st.executeUpdate(sql);

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
	public boolean insertSmsRequestIds(String requestID, String senderAddress, Map<String, String> pluginRequestIDs)
			throws AxataDBUtilException {

		Connection con = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

			String sql = "INSERT INTO sendsms_reqid (hub_requestid,sender_address,delivery_address,plugin_requestid) "
					+ "VALUES (?,?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, requestID);
			ps.setString(2, senderAddress);

			for (Map.Entry<String, String> entry : pluginRequestIDs.entrySet()) {

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

	public HashMap<String, String> subscriptionNotifiMap(Integer axiataid) throws Exception {

		HashMap<String, String> subscriptionDetails = new HashMap<String, String>();
		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		Statement st = null;
		ResultSet rs = null;

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();
			String sql = "SELECT notifyurl, service_provider " + "FROM subscriptions " + "WHERE axiataid = " + axiataid
					+ "";

			rs = st.executeQuery(sql);

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

	public HashMap<String, String> subscriptionDNNotifiMap(Integer axiataid) throws Exception {

		HashMap<String, String> dnSubscriptionDetails = new HashMap<String, String>();
		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		Statement st = null;
		ResultSet rs = null;

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();
			String sql = "SELECT notifyurl, service_provider " + "FROM outbound_subscriptions " + "WHERE axiataid = "
					+ axiataid + "";

			rs = st.executeQuery(sql);

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
	 * @param axiataid
	 *            the axiataid
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<Operatorsubs> outboudSubscriptionQuery(Integer axiataid) throws Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
		Statement st = null;
		ResultSet rs = null;
		List<Operatorsubs> domainsubs = new ArrayList();

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			st = con.createStatement();
			String sql = "SELECT domainurl,operator " + "FROM outbound_operatorsubs " + "WHERE axiataid = " + axiataid
					+ "";

			rs = st.executeQuery(sql);

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
     * @param axiataid the axiataid
     * @return true, if successful
     * @throws Exception the exception
     */
    public boolean outboundSubscriptionDelete(Integer axiataid) throws Exception {

        Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
        Statement st = null;

        try {

            if (con == null) {
            	
                throw new Exception("Connection not found");
            }

            st = con.createStatement();
            String sql = "DELETE FROM outbound_subscriptions "
                    + "WHERE axiataid = " + axiataid + "";

            st.executeUpdate(sql);

            sql = "DELETE FROM outbound_operatorsubs "
                    + "WHERE axiataid = " + axiataid + "";

            st.executeUpdate(sql);

        } catch (Exception e) {
        	
            DbUtils.handleException("Error while deleting subscriptions. ", e);
        } finally {
        	
            DbUtils.closeAllConnections(st, con, null);
        }

        return true;
    }
}
