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
package com.wso2telco.dep.verificationhandler.verifier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;


 
// TODO: Auto-generated Javadoc
/**
 * The Class DatabaseUtils.
 */
public class DatabaseUtils {

    /** The stat datasource. */
    private static volatile DataSource statDatasource = null;
    
    /** The am datasource. */
    private static volatile DataSource amDatasource = null;

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(DatabaseUtils.class);

    /** The msisdn. */
    //private static HashMap<String, String> msisdnMAP = new HashMap<String, String>();
    private static List<String> msisdn = new ArrayList<String>();
    
    /** The whitelistedmsisdn. */
    private static List<String> whitelistedmsisdn = new ArrayList<String>();
    
    /** The subscription list. */
    private static List<String> subscriptionList = new ArrayList<String>();
    
    /** The subscription i ds. */
    private static List<String> subscriptionIDs = new ArrayList<String>();

    /** The current no. */
    private static int currentNo = 3;

    /**
     * Initialize data source.
     *
     * @throws NamingException the naming exception
     */
    public static void initializeDataSource() throws NamingException {
        if (statDatasource != null) {
            return;
        }

        String statdataSourceName = "jdbc/WSO2AM_STATS_DB";

        if (statdataSourceName != null) {
            try {
                Context ctx = new InitialContext();
                statDatasource = (DataSource) ctx.lookup(statdataSourceName);
            } catch (NamingException e) {
                log.error(e);
                throw e;
            }

        }
    }

    /**
     * Initialize am data source.
     *
     * @throws NamingException the naming exception
     */
    public static void initializeAMDataSource() throws NamingException {
        if (amDatasource != null) {
            return;
        }

        String amDataSourceName = "jdbc/WSO2AM_DB";

        if (amDataSourceName != null) {
            try {
                Context ctx = new InitialContext();
                amDatasource = (DataSource) ctx.lookup(amDataSourceName);
            } catch (NamingException e) {
                log.error(e);
                throw e;
            }

        }
    }


    /**
     * Gets the API id.
     *
     * @param apiName the api name
     * @return the API id
     * @throws NamingException the naming exception
     * @throws SQLException the SQL exception
     */
    public static String getAPIId(String apiName) throws NamingException, SQLException {

        String apiId = null;

        /// String sql = "select * from am_subscription";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            String sql =
                    "select API_ID "
                            + "from AM_API where " + "API_NAME=?;";


            conn = getAMDBConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, apiName);

            rs = ps.executeQuery();

            while (rs.next()) {
                apiId = rs.getString("API_ID");
            }


        } catch (SQLException e) {
            log.error("Error occured while writing southbound record.", e);
            throw e;
        } catch (NamingException e) {
            log.error("Error while finding the Datasource.", e);
            throw e;
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, conn, rs);
        }

        return apiId;
    }


    /**
     * Gets the subscription id.
     *
     * @param apiID the api id
     * @param applicationID the application id
     * @return the subscription id
     * @throws NamingException the naming exception
     * @throws SQLException the SQL exception
     */
    public static String getSubscriptionId(String apiID, String applicationID) throws NamingException, SQLException {

        String subscriptionId = null;

        // String sql = "select * from am_subscription";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            String sql =
                    "select SUBSCRIPTION_ID "
                            + "from AM_SUBSCRIPTION where " + "API_ID=? AND "
                            + "APPLICATION_ID=?;";


            conn = getAMDBConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, apiID);
            ps.setString(2, applicationID);

            rs = ps.executeQuery();

            subscriptionIDs.clear();

            while (rs.next()) {
                subscriptionIDs.add(rs.getString("SUBSCRIPTION_ID"));
            }


        } catch (SQLException e) {
            log.error("Error occured while writing southbound record.", e);
            throw e;
        } catch (NamingException e) {
            log.error("Error while finding the Datasource.", e);
            throw e;
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, conn, rs);
        }

        return subscriptionIDs.get(0);
    }

    /**
     * Read blacklist numbers.
     *
     * @param apiName the api name
     * @return the list
     * @throws SQLException the SQL exception
     * @throws NamingException the naming exception
     */
    public static List<String> ReadBlacklistNumbers(String apiName) throws SQLException, NamingException {

        String sql = "select * from blacklistmsisdn where " + "API_NAME=?;";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getStatsDBConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, apiName);

            rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    String msisdnTable = rs.getString("MSISDN").replace("tel3A+", "");
                    log.debug("msisdn in the table = " + msisdnTable);
                    msisdn.add(msisdnTable);

                }
            }

        } catch (SQLException e) {
            log.error("Error occured while writing southbound record.", e);
            throw e;
        } catch (NamingException e) {
            log.error("Error while finding the Datasource..", e);
            throw e;
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, conn, rs);
        }

        return msisdn;


    }

    /**
     * Read whitelist numbers.
     *
     * @param subscriptionID the subscription id
     * @return the list
     * @throws SQLException the SQL exception
     * @throws NamingException the naming exception
     */
    public static List<String> ReadWhitelistNumbers(String subscriptionID) throws SQLException, NamingException {


        String sql =
                "select msisdn "
                        + "from subscription_WhiteList where " + "subscriptionID=?;";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getStatsDBConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, subscriptionID);

            rs = ps.executeQuery();
            whitelistedmsisdn.clear();
            if (rs != null) {


                while (rs.next()) {
                    String msisdnTable = rs.getString("msisdn").replace("tel3A+", "");
                    log.info("msisdn in the table = " + msisdnTable);
                    whitelistedmsisdn.add(msisdnTable);

                }
            }

        } catch (SQLException e) {
            log.error("Error occured while writing southbound record.", e);
            throw e;
        } catch (NamingException e) {
            log.error("Error while finding the Datasource.", e);
            throw e;
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, conn, rs);
        }
        return whitelistedmsisdn;
    }

    /**
     * Check white listed.
     *
     * @param MSISDN the msisdn
     * @param applicationId the application id
     * @param subscriptionId the subscription id
     * @param apiId the api id
     * @return the white list result
     * @throws SQLException the SQL exception
     * @throws NamingException the naming exception
     */
    public static WhiteListResult checkWhiteListed(String MSISDN, String applicationId, String subscriptionId, String apiId) throws SQLException, NamingException {

        WhiteListResult whiteListResult = null;


        String sql = "SELECT * FROM `subscription_WhiteList` WHERE \n" +
                //check with all value mean MSISDN to subscription
                "(`subscriptionID` = ? AND `msisdn` = ? AND `api_id` = ? AND `application_id` =  ?) OR \n" +
                //check with out subscription. but match API,MSISDN and ApplicationID
                "(`subscriptionID` IS NULL  AND `msisdn` = ? AND `api_id` = ? AND `application_id` =  ?) OR \n" +
                //check with only subscription ID and MSISDN
                "(`subscriptionID` = ? AND `msisdn` = ? AND `api_id` IS NULL  AND `application_id` IS NULL ) OR \n" +
                //match specific MSISDN to whole application
                "(`subscriptionID` IS NULL  AND `msisdn` = ? AND `api_id` IS NULL  AND `application_id` =  ?) OR \n" +
                //Match applicaiton only. it mean application can use any API, MSISDN without whitelist individual
                "(`subscriptionID` IS NULL  AND `msisdn` IS NULL  AND `api_id` IS NULL  AND `application_id` =  ?) OR \n" +
                //Match application's API only. it mean application can use specific API with any MSISDN without whitelist individual
                "(`subscriptionID` IS NULL  AND `msisdn` IS NULL  AND `api_id` = ? AND `application_id` =  ?)  LIMIT 0,1 ";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            conn = getStatsDBConnection();
            ps = conn.prepareStatement(sql);

            //"(`subscriptionID` = ? AND `msisdn` = ? AND `api_id` = ? AND `application_id` =  >) OR \n" +
            ps.setString(1, subscriptionId);
            ps.setString(2, MSISDN);
            ps.setString(3, apiId);
            ps.setString(4, applicationId);

            //"(`subscriptionID` = null AND `msisdn` = ? AND `api_id` = ? AND `application_id` =  ?) OR\n"
            ps.setString(5, MSISDN);
            ps.setString(6, apiId);
            ps.setString(7, applicationId);

            // "(`subscriptionID` = ? AND `msisdn` = ? AND `api_id` = null AND `application_id` =  null) OR \n" +
            ps.setString(8, subscriptionId);
            ps.setString(9, MSISDN);

            // "(`subscriptionID` = null AND `msisdn` = ? AND `api_id` = null AND `application_id` =  ?) OR \n" +
            ps.setString(10, MSISDN);
            ps.setString(11, applicationId);

            //"(`subscriptionID` = null AND `msisdn` = null AND `api_id` = null AND `application_id` =  ?) OR \n" +
            ps.setString(12, applicationId);

            // "(`subscriptionID` = null AND `msisdn` = null AND `api_id` = ? AND `application_id` =  ?)  ";
            ps.setString(13, apiId);
            ps.setString(14, applicationId);

            rs = ps.executeQuery();
            if (rs != null) {


                while (rs.next()) {

                    whiteListResult = new WhiteListResult();
                    String msisdnTable = rs.getString("msisdn");
                    if (msisdnTable != null) {
                        msisdnTable = msisdnTable.replace("tel3A+", "");
                        msisdnTable = msisdnTable.replace("tel:+", "");
                        msisdnTable = msisdnTable.replace("tel:", "");
                    }

                    whiteListResult.setApi_id(rs.getString("api_id"));
                    whiteListResult.setApplication_id(rs.getString("application_id"));
                    whiteListResult.setSubscriptionID(rs.getString("subscriptionID"));
                    whiteListResult.setMsisdn(msisdnTable);

                }
            }
//            log.info(ps);


        } catch (SQLException e) {
            log.error("Error occured while writing southbound record.", e);
            throw e;
        } catch (NamingException e) {
            log.error("Error while finding the Datasource.", e);
            throw e;
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, conn, rs);
        }

        return whiteListResult;
    }

    /**
     * Read subscription numbers.
     *
     * @param subscriber the subscriber
     * @param app the app
     * @param api the api
     * @return the list
     * @throws APIManagementException the API management exception
     */
    public static List<String> ReadSubscriptionNumbers(String subscriber, String app, String api) throws APIManagementException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql =
                "select MSISDN "
                        + "from subscriptionmsisdn where "
                        + "userID=? AND "
                        + "application=? AND "
                        + "api=?;";
        try {
            try {
                connection = getStatsDBConnection();
            } catch (NamingException ex) {
                Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            ps = connection.prepareStatement(sql);
            ps.setString(1, subscriber);
            ps.setString(2, app);
            ps.setString(3, api);

            results = ps.executeQuery();


            while (results.next()) {
                subscriptionList.add(results.getString("MSISDN"));
            }
        } catch (SQLException e) {
            handleException("Error occurred while getting Invocation count for Application", e);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, connection, results);
        }
        return subscriptionList;
    }


    /**
     * Update subscription numbers.
     *
     * @param subscriber the subscriber
     * @param app the app
     * @param api the api
     * @param updatedSubscriberCount the updated subscriber count
     * @throws APIManagementException the API management exception
     */
    public static void UpdateSubscriptionNumbers(String subscriber, String app, String api, String updatedSubscriberCount) throws APIManagementException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql =
                "update subscriptionCount set "
                        + "subscriptionCount=? where "
                        + "userId=? AND "
                        + "api=? AND "
                        + "applicationName=?;";
        try {
            try {
                connection = getStatsDBConnection();
            } catch (NamingException ex) {
                Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            ps = connection.prepareStatement(sql);
            ps.setString(1, updatedSubscriberCount);
            ps.setString(2, subscriber);
            ps.setString(3, api);
            ps.setString(4, app);

            ps.execute();

        } catch (SQLException e) {
            handleException("Error occurred while getting Invocation count for Application", e);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, connection, results);
        }

    }

    /**
     * Subscribe user.
     *
     * @param subscriber the subscriber
     * @param app the app
     * @param api the api
     * @param msisdn the msisdn
     * @throws APIManagementException the API management exception
     */
    public static void SubscribeUser(String subscriber, String app, String api, String msisdn) throws APIManagementException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;


        String sql = "INSERT INTO `dialg_stats`.`subscriptionmsisdn` (`userID`, `api`, `application`, `MSISDN`) VALUES (?, ?, ?, ?);";
        try {
            try {
                connection = getStatsDBConnection();
            } catch (NamingException ex) {
                Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            ps = connection.prepareStatement(sql);

            ps.setString(1, subscriber);
            ps.setString(2, api);
            ps.setString(3, app);
            ps.setString(4, msisdn);

            ps.execute();

        } catch (SQLException e) {
            handleException("Error occurred while getting Invocation count for Application", e);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, connection, results);
        }

    }

    /**
     * Write amount.
     *
     * @param userID the user id
     * @param application the application
     * @param amount the amount
     * @param msisdn the msisdn
     * @throws SQLException the SQL exception
     * @throws NamingException the naming exception
     */
    public static void writeAmount(String userID, String application, String amount, String msisdn) throws SQLException, NamingException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;

        String sql = "INSERT INTO `dialg_stats`.`payment` (`application`, `amount`, `userID`, `MSISDN`, `Date`) VALUES (?, ?, ?, ?, CURDATE());";
        try {
            try {
                connection = getStatsDBConnection();
            } catch (NamingException ex) {
                Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            ps = connection.prepareStatement(sql);

            ps.setString(1, application);
            ps.setString(2, amount);
            ps.setString(3, userID);
            ps.setString(4, msisdn);

            ps.execute();

        } catch (SQLException e) {
            //handleException("Error occurred while getting Invocation count for Application", e);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, connection, results);
        }

    }

    /**
     * Write subscription.
     *
     * @param userID the user id
     * @param application the application
     * @param api the api
     * @throws SQLException the SQL exception
     * @throws NamingException the naming exception
     */
    public static void writeSubscription(String userID, String application, String api) throws SQLException, NamingException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;

        String sql = "INSERT INTO `dialg_stats`.`subscriptioncount` (`api`, `subscriptionCount`, `userId`, `applicationName`) VALUES (?, ?, ?, ?);";
        try {
            try {
                connection = getStatsDBConnection();
            } catch (NamingException ex) {
                Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            ps = connection.prepareStatement(sql);

            ps.setString(1, api);
            ps.setString(2, "1");
            ps.setString(3, userID);
            ps.setString(4, application);

            ps.execute();

        } catch (SQLException e) {
            //handleException("Error occurred while getting Invocation count for Application", e);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, connection, results);
        }

    }

    /**
     * Gets the subscription count.
     *
     * @param subscriber the subscriber
     * @param app the app
     * @param api the api
     * @return the subscription count
     * @throws APIManagementException the API management exception
     * @throws SQLException the SQL exception
     */
    public static String getSubscriptionCount(String subscriber, String app, String api) throws APIManagementException, SQLException {
        Connection connection = null;
        String subscriptionCount = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql =
                "select subscriptionCount "
                        + "from subscriptioncount where "
                        + "userId=? AND "
                        + "applicationName=? AND "
                        + "api=?;";
        try {
            try {
                connection = getStatsDBConnection();
            } catch (NamingException ex) {
                Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            ps = connection.prepareStatement(sql);
            ps.setString(1, subscriber);
            ps.setString(2, app);
            ps.setString(3, api);

            results = ps.executeQuery();

            while (results.next()) {
                subscriptionCount = results.getString("subscriptionCount");
            }


        } catch (SQLException e) {
            handleException("Error occurred while getting Invocation count for Application", e);
            return null;
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, connection, results);
        }
        return subscriptionCount;
    }

    /**
     * Next.
     *
     * @return the int
     */
    public static int next() {
        if (currentNo == Integer.MAX_VALUE) {
            currentNo = 0;
        }
        return currentNo++;
    }

    /**
     * Gets the stats db connection.
     *
     * @return the stats db connection
     * @throws SQLException the SQL exception
     * @throws NamingException the naming exception
     */
    public static Connection getStatsDBConnection() throws SQLException, NamingException {
        initializeDataSource();
        if (statDatasource != null) {
            return statDatasource.getConnection();
        } else {
            throw new SQLException("Statistics Datasource not initialized properly");
        }
    }

    /**
     * Gets the AMDB connection.
     *
     * @return the AMDB connection
     * @throws SQLException the SQL exception
     * @throws NamingException the naming exception
     */
    public static Connection getAMDBConnection() throws SQLException, NamingException {
        initializeAMDataSource();
        if (amDatasource != null) {
            return amDatasource.getConnection();
        } else {
            throw new SQLException("AM Datasource not initialized properly");
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
