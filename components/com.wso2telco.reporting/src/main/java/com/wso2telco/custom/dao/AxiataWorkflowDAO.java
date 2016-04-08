package com.wso2telco.custom.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;
import org.wso2.carbon.apimgt.usage.client.exception.APIMgtUsageQueryServiceClientException;
import com.wso2telco.custom.hostobjects.AMDataAccessObject;
import com.wso2telco.custom.hostobjects.BillingDataAccessObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AxiataWorkflowDAO {
	
	private static final Log log = LogFactory.getLog(AxiataWorkflowDAO.class);

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
    
    public void saveSubscriptionChargeRate(String appId, String apiId, String opName) throws APIManagementException, APIMgtUsageQueryServiceClientException {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String apiName = AMDataAccessObject.getApiNameById(Integer.valueOf(apiId));
            conn = BillingDataAccessObject.getStatsDBConnection();
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

    public void saveSubscriptionChargeRateNB(String appId, String apiId) throws APIManagementException, APIMgtUsageQueryServiceClientException {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String apiName = AMDataAccessObject.getApiNameById(Integer.valueOf(apiId));
            conn = BillingDataAccessObject.getStatsDBConnection();
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
	 * Update the tier for the specified application.
	 * 
	 * @param applicationId
	 * @param tierId
	 * @throws APIManagementException
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
     * @param msg
     * @param t
     * @throws APIManagementException
     */
    private static void handleException(String msg, Throwable t) throws APIManagementException {
        log.error(msg, t);
        throw new APIManagementException(msg, t);
    }
}
