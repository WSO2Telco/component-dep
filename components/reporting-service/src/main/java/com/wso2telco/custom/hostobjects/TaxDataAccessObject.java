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
package com.wso2telco.custom.hostobjects;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;
import org.wso2.carbon.apimgt.usage.client.exception.APIMgtUsageQueryServiceClientException;

import java.sql.*;
import java.util.*;

// TODO: Auto-generated Javadoc
/**
 * The Class TaxDataAccessObject.
 */
public class TaxDataAccessObject {

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(TaxDataAccessObject.class);

    /**
     * Gets the taxes for subscription.
     *
     * @param applicationId the application id
     * @param apiId the api id
     * @return the taxes for subscription
     * @throws APIManagementException the API management exception
     * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
     */
    public static List<Tax> getTaxesForSubscription(int applicationId, int apiId) throws APIManagementException,
            APIMgtUsageQueryServiceClientException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT type,effective_from,effective_to,value FROM tax, subscription_tax " +
                "WHERE subscription_tax.application_id=? AND subscription_tax.api_id=? AND tax.type=subscription_tax.tax_type ";

        List<Tax> taxes = new ArrayList<Tax>();
        try {
            connection = BillingDataAccessObject.getStatsDBConnection();
            ps = connection.prepareStatement(sql);
            log.debug("getTaxesForSubscription for applicationId---> " + applicationId + " apiId--> " + apiId);
            ps.setInt(1, applicationId);
            ps.setInt(2, apiId);
            log.debug("SQL (PS) ---> " + ps.toString());
            results = ps.executeQuery();
            while (results.next()) {
                Tax tax = new Tax();
                tax.setType(results.getString("type"));
                tax.setEffective_from(results.getDate("effective_from"));
                tax.setEffective_to(results.getDate("effective_to"));
                tax.setValue(results.getBigDecimal("value"));
                taxes.add(tax);
            }
        } catch (SQLException e) {
            log.error("SQL Error in getTaxesForSubscription");
            log.error(e.getStackTrace());
            handleException("Error occurred while getting Taxes for Subscription", e);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, connection, results);
        }
        return taxes;
    }

    /**
     * Gets the taxes for tax list.
     *
     * @param taxList the tax list
     * @return the taxes for tax list
     * @throws APIManagementException the API management exception
     * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
     */
    public static List<Tax> getTaxesForTaxList(List<String> taxList) throws APIManagementException,
            APIMgtUsageQueryServiceClientException {
        Connection connection = null;
        Statement st = null;
        ResultSet results = null;

        List<Tax> taxes = new ArrayList<Tax>();

        if (taxList == null || taxList.isEmpty()) {
            return taxes;
        }

        // CSV format surrounded by single quote
        String taxListStr = taxList.toString().replace("[", "'").replace("]", "'").replace(", ", "','");

        String sql = "SELECT type,effective_from,effective_to,value FROM tax WHERE type IN (" + taxListStr + ")";

        try {
            connection = BillingDataAccessObject.getStatsDBConnection();
            st = connection.createStatement();
            log.debug("In getTaxesForTaxList");
            log.debug("SQL (PS) ---> " + st.toString());
            results = st.executeQuery(sql);
            while (results.next()) {
                Tax tax = new Tax();
                tax.setType(results.getString("type"));
                tax.setEffective_from(results.getDate("effective_from"));
                tax.setEffective_to(results.getDate("effective_to"));
                tax.setValue(results.getBigDecimal("value"));
                taxes.add(tax);
            }
            st.close();
        } catch (SQLException e) {
            log.error("SQL Error in getTaxesForTaxList");
            log.error(e.getStackTrace());
            handleException("Error occurred while getting Taxes for Tax List", e);
        } finally {
            APIMgtDBUtil.closeAllConnections(null, connection, results);
        }
        return taxes;
    }

    /**
     * Gets the API request times for subscription.
     *
     * @param year the year
     * @param month the month
     * @param apiName the api name
     * @param apiVersion the api version
     * @param consumerKey the consumer key
     * @param operatorId the operator id
     * @param operation the operation
     * @param category the category
     * @param subcategory the subcategory
     * @return the API request times for subscription
     * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
     * @throws APIManagementException the API management exception
     */
    public static Set<APIRequestDTO> getAPIRequestTimesForSubscription(short year, short month, String apiName,
                                                                       String apiVersion, String consumerKey,
                                                                       String operatorId, int operation, String category, String subcategory) throws
            APIMgtUsageQueryServiceClientException, APIManagementException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT api_version,response_count AS count,STR_TO_DATE(time,'%Y-%m-%d') as date FROM " +
                HostObjectConstants.SB_RESPONSE_SUMMARY_TABLE + " WHERE year=? and month=? and consumerKey=? and " +
                "api=? and api_version=? and operatorId=? and operationType=? and category =? and subcategory=? and responseCode like '2%'";

        Set<APIRequestDTO> apiRequests = new HashSet<APIRequestDTO>();
        try {
            connection = BillingDataAccessObject.getStatsDBConnection();
            ps = connection.prepareStatement(sql);
            ps.setShort(1, year);
            ps.setShort(2, month);
            ps.setString(3, consumerKey);
            ps.setString(4, apiName);
            ps.setString(5, apiVersion);
            ps.setString(6, operatorId);
            ps.setInt(7, operation);
            ps.setString(8, category);
            ps.setString(9, subcategory);

            results = ps.executeQuery();
            while (results.next()) {
                APIRequestDTO req = new APIRequestDTO();
                req.setApiVersion(results.getString("api_version"));
                req.setRequestCount(results.getInt("count"));
                req.setDate(results.getDate("date"));

                apiRequests.add(req);
            }
        } catch (SQLException e) {
            handleException("Error occurred while getting Request Times for Subscription", e);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, connection, results);
        }

        return apiRequests;
    }

    /**
     * Gets the nb api request times for subscription.
     *
     * @param year the year
     * @param month the month
     * @param apiName the api name
     * @param apiVersion the api version
     * @param consumerKey the consumer key
     * @param operation the operation
     * @param category the category
     * @param subcategory the subcategory
     * @return the nb api request times for subscription
     * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
     * @throws APIManagementException the API management exception
     */
    public static Set<APIRequestDTO> getNbAPIRequestTimesForSubscription(short year, short month, String apiName,
                                                                       String apiVersion, String consumerKey,
                                                                       int operation, String category, String subcategory) throws
            APIMgtUsageQueryServiceClientException, APIManagementException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT api_version,response_count AS count,STR_TO_DATE(time,'%Y-%m-%d') as date FROM " +
                HostObjectConstants.NB_RESPONSE_SUMMARY_TABLE + " WHERE year=? and month=? and consumerKey=? and " +
                "api=? and api_version=? and operationType=? and category =? and subcategory=? and responseCode like '2%'";

        Set<APIRequestDTO> apiRequests = new HashSet<APIRequestDTO>();
        try {
            connection = BillingDataAccessObject.getStatsDBConnection();
            ps = connection.prepareStatement(sql);
            ps.setShort(1, year);
            ps.setShort(2, month);
            ps.setString(3, consumerKey);
            ps.setString(4, apiName);
            ps.setString(5, apiVersion);
            ps.setInt(6, operation);
            ps.setString(7, category);
            ps.setString(8, subcategory);

            results = ps.executeQuery();
            while (results.next()) {
                APIRequestDTO req = new APIRequestDTO();
                req.setApiVersion(results.getString("api_version"));
                req.setRequestCount(results.getInt("count"));
                req.setDate(results.getDate("date"));

                apiRequests.add(req);
            }
        } catch (SQLException e) {
            handleException("Error occurred while getting Request Times for Subscription", e);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, connection, results);
        }

        return apiRequests;
    }
    
    /**
     * Gets the API request times for application.
     *
     * @param consumerKey the consumer key
     * @param year the year
     * @param month the month
     * @param userId the user id
     * @return the API request times for application
     * @throws APIManagementException the API management exception
     * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
     */
    public static Map<String, List<APIRequestDTO>> getAPIRequestTimesForApplication(String consumerKey, short year,
                                                                                 short month, String userId)
            throws APIManagementException, APIMgtUsageQueryServiceClientException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT api_version,total_request_count AS count,STR_TO_DATE(time,'%Y-%m-%d') as date FROM API_REQUEST_SUMMARY " +
                "WHERE year=? AND month=? AND consumerKey=?  AND userId=?;";

        Map<String, List<APIRequestDTO>> apiRequests = new HashMap<String, List<APIRequestDTO>>();
        try {
            connection = BillingDataAccessObject.getStatsDBConnection();
            ps = connection.prepareStatement(sql);
            ps.setShort(1, year);
            ps.setShort(2, month);
            ps.setString(3, consumerKey);
            ps.setString(4, userId);

            results = ps.executeQuery();
            while (results.next()) {
                APIRequestDTO req = new APIRequestDTO();
                req.setApiVersion(results.getString("api_version"));
                req.setRequestCount(results.getInt("count"));
                req.setDate(results.getDate("date"));
                if (apiRequests.containsKey(req.getApiVersion())) {
                    apiRequests.get(req.getApiVersion()).add(req);
                } else {
                    List<APIRequestDTO> list = new ArrayList<APIRequestDTO>();
                    list.add(req);
                    apiRequests.put(req.getApiVersion(), list);
                }
            }
        } catch (SQLException e) {
            handleException("Error occurred while getting Request Times for Application", e);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, connection, results);
        }

        return apiRequests;
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

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        try {
            List<Tax> taxList = getTaxesForSubscription(00,25);
            for (int i = 0; i < taxList.size(); i++) {
                Tax tax = taxList.get(i);
                System.out.println(tax.getType() + " ~ " + tax.getEffective_from() + " ~ " + tax.getEffective_to() + " ~ " + tax.getValue());
            }

            Map<String, List<APIRequestDTO>> reqMap = getAPIRequestTimesForApplication("yx1eZTmtbBaYqfIuEYMVgIKonSga", (short)2014, (short)1, "admin");
            System.out.println(reqMap);

        } catch (APIManagementException e) {
            e.printStackTrace();
        } catch (APIMgtUsageQueryServiceClientException e) {
            e.printStackTrace();
        }
    }
}
