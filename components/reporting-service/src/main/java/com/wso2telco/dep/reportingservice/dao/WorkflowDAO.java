/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.reportingservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;

import com.wso2telco.dbutils.DbUtils;
import com.wso2telco.dbutils.util.DataSourceNames;


// TODO: Auto-generated Javadoc
/**
 * The Class WorkflowDAO.
 */
public class WorkflowDAO {
	
	/** The Constant log. */
	private static final Log log = LogFactory.getLog(WorkflowDAO.class);

    /**
     * Update subscription tier.
     *
     * @param subscriptionId the subscription id
     * @param tierId the tier id
     * @throws APIManagementException the API management exception
     */
    public void updateSubscriptionTier(String subscriptionId, String tierId) throws APIManagementException {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conn = APIMgtDBUtil.getConnection();
            String query = "UPDATE AM_SUBSCRIPTION SET TIER_ID=?" +
                           " WHERE SUBSCRIPTION_ID=?";
            ps = conn.prepareStatement(query);
            ps.setString(1, tierId);
            ps.setInt(2, Integer.parseInt(subscriptionId));
            ps.executeUpdate();
            
        } catch (SQLException e) {
            handleException("Error in updating subscription tier : " + e.getMessage(), e);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, conn, rs);
        }
    }
    
    /**
     * Save subscription charge rate.
     *
     * @param appId the app id
     * @param apiId the api id
     * @param opName the op name
     * @throws Exception 
     */
    public void saveSubscriptionChargeRate(String appId, String apiId, String opName) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String apiName = ApiManagerDAO.getApiNameById(Integer.valueOf(apiId));
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            String sql = "SELECT operation_id,default_rate FROM api_operation_types WHERE api=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, apiName);
            log.debug("SQL (PS) ---> " + ps.toString());
            rs = ps.executeQuery();

            while (rs.next()) {
                Integer opId = Integer.parseInt(rs.getString("operation_id"));
                String defaultRate = rs.getString("default_rate");

                String query = "INSERT INTO subscription_rates (`application_id`, `api_id`, `operator_name`, `rate_id_sb`, `operation_id`) VALUES (?, ?, ?, ?, ?)";
                ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(appId));
                ps.setInt(2, Integer.parseInt(apiId));
                ps.setString(3, opName);
                ps.setString(4, defaultRate);
                ps.setInt(5, opId);
                ps.executeUpdate();
            }
            
        } catch (SQLException e) {
            handleException("Error in Creating subscription charge rate : " + e.getMessage(), e);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, conn, rs);
        }
    }

    /**
     * Save subscription charge rate nb.
     *
     * @param appId the app id
     * @param apiId the api id
     * @throws Exception 
     */
    public void saveSubscriptionChargeRateNB(String appId, String apiId) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String apiName = ApiManagerDAO.getApiNameById(Integer.valueOf(apiId));
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            String sql = "SELECT operation_id,default_rate FROM api_operation_types WHERE api=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, apiName);
            log.debug("SQL (PS) ---> " + ps.toString());
            rs = ps.executeQuery();

            while (rs.next()) {
                Integer opId = Integer.parseInt(rs.getString("operation_id"));
                String defaultRate = rs.getString("default_rate");

                String query = "INSERT INTO subscription_rates_nb (`application_id`, `api_id`, `rate_id_nb`, `operation_id`) VALUES (?, ?, ?, ?)";
                ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(appId));
                ps.setInt(2, Integer.parseInt(apiId));
                ps.setString(3, defaultRate);
                ps.setInt(4, opId);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            handleException("Error in Creating subscription charge rate : " + e.getMessage(), e);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, conn, rs);
        }
    }
    
     
    
    /**
     * Update application tier.
     *
     * @param applicationId the application id
     * @param tierId the tier id
     * @throws APIManagementException the API management exception
     */
    public void updateApplicationTier(String applicationId, String tierId) throws APIManagementException {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conn = APIMgtDBUtil.getConnection();
            String query = "UPDATE AM_APPLICATION SET APPLICATION_TIER=?" +
                           " WHERE APPLICATION_ID=?";
            ps = conn.prepareStatement(query);
            ps.setString(1, tierId);
            ps.setInt(2, Integer.parseInt(applicationId));
            ps.executeUpdate();
            
        } catch (SQLException e) {
            handleException("Error in updating application tier : " + e.getMessage(), e);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, conn, rs);
        }
    }
    
     
    /**
     * Handle exception.
     *
     * @param msg the msg
     * @param t the t
     * @throws APIManagementException the API management exception
     */
    private static void handleException(String msg, Throwable t) throws APIManagementException {
        log.error(msg, t);
        throw new APIManagementException(msg, t);
    }
}
