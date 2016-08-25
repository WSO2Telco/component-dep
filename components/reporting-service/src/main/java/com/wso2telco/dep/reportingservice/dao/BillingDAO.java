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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.api.model.Application;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowConstants;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.reportingservice.APIResponseDTO;
import com.wso2telco.dep.reportingservice.HostObjectConstants;
import com.wso2telco.dep.reportingservice.PaymentRequestDTO;
import com.wso2telco.dep.reportingservice.SPObject;
import com.wso2telco.dep.reportingservice.southbound.BilledCharge;
import com.wso2telco.dep.reportingservice.southbound.CategoryCharge;
import com.wso2telco.dep.reportingservice.util.CommissionPercentagesDTO;
import com.wso2telco.dep.reportingservice.util.OperatorDetailsEntity;
import com.wso2telco.dep.reportingservice.util.ReportingTable;
import com.wso2telco.core.utils.exception.BusinessException;
import com.wso2telco.core.utils.exception.GenaralError;

/**
 * The Class BillingDataAccessObject.
 */
public class BillingDAO { 
    

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(BillingDAO.class);
    
    /** The api manager dao. */
    ApiManagerDAO apiManagerDAO = new ApiManagerDAO();
    
    /**
     * Prints the api request summary.
     *
     * @throws Exception the exception
     */
    public void printAPIRequestSummary() throws Exception{
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ")
        .append(ReportingTable.API_REQUEST_SUMMARY.getTObject());
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);;
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("=== Results for api :" + rs.getString("api") + " , ck :  "
                        + rs.getString("consumerKey") + " , count : "
                        + rs.getInt("total_request_count"));
            }

        } catch (Exception e) {
            handleException("printAPIRequestSummary", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }

    }

    /**
     * Prints the southbound traffic.
     *
     * @throws Exception the exception
     */
    public void printSouthboundTraffic() throws Exception {
        StringBuilder sql = new StringBuilder(); 
        sql.append("select * from ")
        .append(ReportingTable.TEST_DB.getTObject());
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();

            log.debug("Print Southbound Reporting");
            while (rs.next()) {
                log.debug("=== Results for southbound Traffic :" + rs.getString("ENDPOINT") + " , endpoint :  "
                        + rs.getString("CONSUMER_KEY") + " , key : "
                        + rs.getString("TIME_CREATED"));
            }

        } catch (Exception e) {
            handleException("printSouthboundTraffic", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }

    }

    /**
     * Gets the response time for api.
     *
     * @param apiVersion the api version
     * @return the response time for api
     * @throws Exception the exception
     */
    public String getResponseTimeForAPI(String apiVersion) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("select * from ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" where api_version=? order by time desc limit 1;");
        
        Map<String, Integer> apiCount = new HashMap<String, Integer>();
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = connection.prepareStatement(sql.toString());
            log.debug("getResponseTimeForAPI for apiVersion---> " + apiVersion);
            ps.setString(1, apiVersion);
            log.debug("SQL (PS) ---> " + ps.toString()); 
            results = ps.executeQuery();
            while (results.next()) {
                return results.getString("serviceTime");
            }
        } catch (Exception e) {            
            handleException("getResponseTimeForAPI", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }
        return null;
    }

    /**
     * Gets the all response times for api.
     *
     * @param operator the operator
     * @param appId the app id
     * @param apiVersion the api version
     * @param fromDate the from date
     * @param toDate the to date
     * @return the all response times for api
     * @throws Exception the exception
     */
    public List<APIResponseDTO> getAllResponseTimesForAPI(String operator, String appId, String apiVersion, String fromDate,
            String toDate) throws Exception {

        String appConsumerKey = "%";
        if (operator.contains("__ALL__")) {
            operator = "%";
        }

        if (!appId.contains("__ALL__")) {
            appConsumerKey = getConsumerKeyByAppId(appId);
        }

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("select api_version,response_count AS count, serviceTime,STR_TO_DATE(time,'%Y-%m-%d') as date FROM ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" WHERE api_version=? AND (time BETWEEN ? AND ?) AND operatorId LIKE ? AND consumerKey LIKE ?;");
        
        List<APIResponseDTO> responseTimes = new ArrayList<APIResponseDTO>();
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = connection.prepareStatement(sql.toString());
            log.debug("getAllResponseTimesForAPI for apiVersion---> " + apiVersion);
            ps.setString(1, apiVersion);
            ps.setString(2, fromDate + " 00:00:00");
            ps.setString(3, toDate + " 23:59:59");
            ps.setString(4, operator);
            ps.setString(5, appConsumerKey);
            log.debug("SQL (PS) ---> " + ps.toString());
            results = ps.executeQuery();
            while (results.next()) {
                APIResponseDTO resp = new APIResponseDTO();
                resp.setApiVersion(results.getString("api_version"));
                resp.setResponseCount(results.getInt("count"));
                resp.setServiceTime(results.getInt("serviceTime"));
                resp.setDate(results.getDate("date"));

                responseTimes.add(resp);
            }
        } catch (Exception e) {            
            handleException("getAllResponseTimesForAPI", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }
        return responseTimes;
    }

    /**
     * Gets the API counts for application.
     *
     * @param consumerKey the consumer key
     * @param year the year
     * @param month the month
     * @param userId the user id
     * @return the API counts for application
     * @throws Exception the exception
     */
    public Map<String, Integer> getAPICountsForApplication(String consumerKey, String year,
            String month, String userId)
            throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;

        StringBuilder sql = new StringBuilder();
        
        sql.append("select api_version,sum(response_count) as total from ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" where month= ? AND year = ? and consumerKey=? and userId=? and responseCode like '2%' group by api_version;");

        Map<String, Integer> apiCount = new HashMap<String, Integer>();
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, month);
            ps.setString(2, year);
            ps.setString(3, consumerKey);
            ps.setString(4, userId);

            results = ps.executeQuery();
            while (results.next()) {
                apiCount.put(results.getString("api_version"), results.getInt("total"));
            }
        } catch (Exception e) {
            handleException("getAPICountsForApplication", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }

        return apiCount;
    }

    /**
     * Gets the applicationconsumer.
     *
     * @param applicationid the applicationid
     * @param keytype the keytype
     * @return the applicationconsumer
     * @throws Exception the exception
     */
    public String getApplicationconsumer(Integer applicationid, String keytype)
            throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String consumerKey = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("select consumer_key from ")
        .append(ReportingTable.AM_APPLICATION_KEY_MAPPING.getTObject())
        .append(" where key_type = ? and application_id = ?;");

        Map<String, Integer> apiCount = new HashMap<String, Integer>();
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);;
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, keytype);
            ps.setInt(2, applicationid);

            results = ps.executeQuery();
            while (results.next()) {
                consumerKey = results.getString("consumer_key");
            }
        } catch (Exception e) {
            handleException("getApplicationconsumer", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }

        return consumerKey;
    }

    /**
     * Gets the application name.
     *
     * @param applicationid the applicationid
     * @param keytype the keytype
     * @return the application name
     * @throws Exception the exception
     */
    public String getApplicationName(Integer applicationid, String keytype)
            throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String appName = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("select consumer_key, name from ")
        .append(ReportingTable.AM_APPLICATION_KEY_MAPPING.getTObject())
        .append(" ap, ")
        .append(ReportingTable.AM_APPLICATION.getTObject())
        .append(" am where ap.APPLICATION_ID = am.APPLICATION_ID and key_type = ? and ap.application_id = ?;");

        Map<String, Integer> apiCount = new HashMap<String, Integer>();
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, keytype);
            ps.setInt(2, applicationid);

            results = ps.executeQuery();
            while (results.next()) {
                appName = results.getString("name");
            }
        } catch (Exception e) {
            handleException("getApplicationName", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }

        return appName;
    }

    /**
     * Gets the operator details of subscription.
     *
     * @param applicationId the application id
     * @param apiId the api id
     * @return the operator details of subscription
     * @throws Exception the exception
     */
    public List<OperatorDetailsEntity> getOperatorDetailsOfSubscription(int applicationId, int apiId) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        sql.append("SELECT operator_name,rate_id_sb,operation_id FROM ")
        .append(HostObjectConstants.SUBSCRIPTION_OPCO_RATES_TABLE)
        .append(" WHERE application_id=? AND api_id=? ");

        List<OperatorDetailsEntity> operatorDetails=new ArrayList<OperatorDetailsEntity>();
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);;
            ps = connection.prepareStatement(sql.toString());
            log.debug("getOperatorDetailsOfSubscription for applicationId---> " + applicationId + " apiId--> " + apiId);
            ps.setInt(1, applicationId);
            ps.setInt(2, apiId);
            log.debug("SQL (PS) ---> " + ps.toString());
            results = ps.executeQuery();
            while (results.next()) {
                OperatorDetailsEntity detailsEntity=new OperatorDetailsEntity();

                detailsEntity.setRateName(results.getString("rate_id_sb"));
                detailsEntity.setOperationId(results.getInt("operation_id"));
                detailsEntity.setOperatorName(results.getString("operator_name"));

                operatorDetails.add(detailsEntity);

            }
        } catch (Exception e) {
            handleException("getOperatorDetailsOfSubscription", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }
        return operatorDetails;
    }
    
    /**
     * Gets the details of subscription.
     *
     * @param applicationId the application id
     * @param apiId the api id
     * @return the details of subscription
     * @throws Exception the exception
     */
    public List<OperatorDetailsEntity> getDetailsOfSubscription(int applicationId, int apiId) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        sql.append("SELECT rate_id_nb,operation_id FROM ")
        .append(HostObjectConstants.SUBSCRIPTION_RATES_TABLE)
        .append(" WHERE application_id=? AND api_id=? ");

        List<OperatorDetailsEntity> operatorDetails=new ArrayList<OperatorDetailsEntity>();
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = connection.prepareStatement(sql.toString());
            log.debug("getOperatorDetailsOfSubscription for applicationId---> " + applicationId + " apiId--> " + apiId);
            ps.setInt(1, applicationId);
            ps.setInt(2, apiId);
            log.debug("SQL (PS) ---> " + ps.toString());
            results = ps.executeQuery();
            
            log.debug("REPORTS DEBUG LOGS 00 : ps = "+ps);
            
            while (results.next()) {
                OperatorDetailsEntity detailsEntity=new OperatorDetailsEntity();

                detailsEntity.setRateName(results.getString("rate_id_nb"));
                detailsEntity.setOperationId(Integer.parseInt(results.getString("operation_id")));
                detailsEntity.setOperatorName("__default__");

                operatorDetails.add(detailsEntity);

            }
        } catch (Exception e) {
            handleException("getDetailsOfSubscription", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }
        return operatorDetails;
    }

    /**
     * Gets the API counts for subscription.
     *
     * @param consumerKey the consumer key
     * @param year the year
     * @param month the month
     * @param apiName the api name
     * @param apiVersion the api version
     * @param operatorId the operator id
     * @param operationId the operation id
     * @return the API counts for subscription
     * @throws Exception the exception
     */
    public Map<CategoryCharge,BilledCharge> getAPICountsForSubscription (String consumerKey, short year, short month, String apiName, String apiVersion, String operatorId, int operationId)
            throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        sql.append("SELECT category,subcategory,sum(response_count) as total, sum(chargeAmount) as totalPayAmount ")
        .append("from ")
        .append(HostObjectConstants.SB_RESPONSE_SUMMARY_TABLE)
        .append(" where year=? and month=? and api=? and version=? and consumerKey=? and operatorId=? ")
        .append("and responseCode like '2%' and operationType = ? group by category,subcategory");

        
        Map<CategoryCharge,BilledCharge> apiCount =new HashMap<CategoryCharge,BilledCharge>();
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = connection.prepareStatement(sql.toString());
            ps.setShort(1, year);
            ps.setShort(2, month);
            ps.setString(3, apiName);
            ps.setString(4, apiVersion);
            ps.setString(5, consumerKey);
            ps.setString(6, operatorId);
            ps.setInt(7, operationId);

            results = ps.executeQuery();
            
            CategoryCharge categoryCharge = null;
            while (results.next()) {
                categoryCharge = new CategoryCharge(operationId,results.getString("category"),results.getString("subcategory"));                
                apiCount.put(categoryCharge,new BilledCharge(results.getInt("total")));
            }
        } catch (Exception e) {
            handleException("getAPICountsForSubscription", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }
        log.debug("apiCount :" + apiName + " :" + apiVersion + ": " + consumerKey);
        return apiCount;
    }
    
        /**
         * Gets the northbound api counts for subscription.
         *
         * @param consumerKey the consumer key
         * @param year the year
         * @param month the month
         * @param apiName the api name
         * @param apiVersion the api version
         * @param operationId the operation id
         * @return the northbound api counts for subscription
         * @throws Exception the exception
         */
        public Map<CategoryCharge,BilledCharge> getNorthboundAPICountsForSubscription (String consumerKey, short year, short month, String apiName, String apiVersion,int operationId)
            throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        sql.append("SELECT category,subcategory,sum(response_count) as total, sum(chargeAmount) as totalPayAmount from ")
        .append(HostObjectConstants.NB_RESPONSE_SUMMARY_TABLE)
        .append(" where year=? and month=? and api=? and version=? and consumerKey=? ")
        .append("and responseCode like '2%' and operationType = ? group by category,subcategory");

        
        Map<CategoryCharge,BilledCharge> apiCount =new HashMap<CategoryCharge,BilledCharge>();
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = connection.prepareStatement(sql.toString());
            ps.setShort(1, year);
            ps.setShort(2, month);
            ps.setString(3, apiName);
            ps.setString(4, apiVersion);
            ps.setString(5, consumerKey);
            ps.setInt(6, operationId);

            results = ps.executeQuery();
            log.debug("REPORTS DEBUG LOGS 01 : ps = "+ps);
            CategoryCharge categoryCharge = null;
            while (results.next()) {
                categoryCharge = new CategoryCharge(operationId,results.getString("category"),results.getString("subcategory"));                
                apiCount.put(categoryCharge,new BilledCharge(results.getInt("total")));
            }
        } catch (Exception e) {
            handleException("getNorthboundAPICountsForSubscription", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }

        return apiCount;
    }
    

    /**
     * Gets the API counts for application opco.
     *
     * @param consumerKey the consumer key
     * @param year the year
     * @param month the month
     * @param userId the user id
     * @param api the api
     * @return the API counts for application opco
     * @throws Exception the exception
     */
    public Map<String, Integer> getAPICountsForApplicationOpco(String consumerKey, String year, String month, String userId, String api)
            throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        sql.append("select operatorId,sum(response_count) as total from ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" where year=? and month=? and api=? and userid like ? and consumerKey like ?")
        .append(" and responseCode like '2%' ")
        .append("group by operatorId");

        if (userId.contains("__ALL__")) {
            userId = "%";
        }

        Map<String, Integer> apiCount = new HashMap<String, Integer>();
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, year);
            ps.setString(2, month);
            ps.setString(3, api);
            ps.setString(4, userId);
            ps.setString(5, consumerKey);

            results = ps.executeQuery();
            while (results.next()) {
                apiCount.put(results.getString("operatorId"), results.getInt("total"));
            }
        } catch (Exception e) {
            handleException("getAPICountsForApplicationOpco", e);
        } finally {            
            DbUtils.closeAllConnections(ps, connection, results);
        }

        return apiCount;
    }

    /**
     * Prints the api subscriber table.
     *
     * @throws Exception the exception
     */
    @Deprecated
    public void printAPISubscriberTable() throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select * from ")
        .append(ReportingTable.AM_SUBSCRIBER.getTObject());
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("=== Results for SUBSCRIBER_ID  :" + rs.getInt("SUBSCRIBER_ID")
                        + " , USER_ID :  " + rs.getString("USER_ID")
                        + " , DATE_SUBSCRIBED : " + rs.getDate("DATE_SUBSCRIBED"));
            }

        } catch (Exception e) {
            handleException("printAPISubscriberTable", e);          
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }
    }

   
    /**
     * Gets the all subscriptions.
     *
     * @return the all subscriptions
     * @throws Exception the exception
     */
    public List<String> getAllSubscriptions() throws Exception {
        StringBuilder sql = new StringBuilder(); 
        sql.append("select USER_ID from ")
        .append(ReportingTable.AM_SUBSCRIBER.getTObject());
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> subscriber = new ArrayList<String>();
        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                subscriber.add(rs.getString("USER_ID"));
            }

        } catch (Exception e) {
            handleException("getAllSubscriptions", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }
        return subscriber;
    }

    /**
     * Gets the subscriberkey.
     *
     * @param userid the userid
     * @return the subscriberkey
     * @throws Exception the exception
     */
    public int getSubscriberkey(String userid) throws Exception {
        StringBuilder sql = new StringBuilder(); 
        sql.append("select subscriber_id from ")
        .append(ReportingTable.AM_SUBSCRIBER.getTObject())
        .append(" WHERE USER_ID = ?");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int subscriber = 0;
        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, userid);
            rs = ps.executeQuery();
            if (rs.next()) {
                subscriber = rs.getInt("subscriber_id");
            }

        } catch (Exception e) {
        	handleException("getSubscriberkey", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }
        return subscriber;
    }

    /**
     * Handle exception.
     *
     * @param msg the msg
     * @param t the t
     * @throws Exception the exception
     */
    private static void handleException(String msg, Throwable t) throws BusinessException{
    	
        log.error(msg, t);
        throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gets the no of subscribers.
     *
     * @param subscriber the subscriber
     * @param app the app
     * @param apiName the api name
     * @return the no of subscribers
     * @throws Exception the exception
     */
    //Retriving number of Subscribers for each API
    public int getNoOfSubscribers(String subscriber, String app, String apiName) throws Exception {
        int noOfSubscribers = 0;

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        sql.append("select subscriptionCount from ")
        .append(ReportingTable.SUBSCRIPTIONCOUNT.getTObject())
        .append(" where userId=? AND api=? AND applicationName=?;");
        
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, subscriber);
            ps.setString(2, apiName);
            ps.setString(3, app);

            results = ps.executeQuery();

            while (results.next()) {
                noOfSubscribers = noOfSubscribers + Integer.parseInt(results.getString("subscriptionCount"));
            }
        } catch (Exception e) {
            handleException("getNoOfSubscribers", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }

        return noOfSubscribers;
    }

    /**
     * Gets the payment amounts.
     *
     * @param year the year
     * @param month the month
     * @param consumerKey the consumer key
     * @param api_version the api_version
     * @param operatorId the operator id
     * @param operation the operation
     * @param category the category
     * @param subcategory the subcategory
     * @return the payment amounts
     * @throws Exception the exception
     */
    //Retriving amount charged from end-user through payment API
    public Set<PaymentRequestDTO> getPaymentAmounts(short year, short month, String consumerKey,
                                                           String api_version, String operatorId, int operation, String category, String subcategory) throws
            Exception {

        Connection connection = null; 
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT api,userId,consumerKey,chargeAmount,category,subcategory,merchantId,time FROM ")
        .append(HostObjectConstants.SB_RESPONSE_SUMMARY_TABLE)
        .append(" WHERE api_version =? and consumerKey=? and operatorId =? and responseCode like '2%' and month=? and year=? and operationType=? and category=? and subcategory=? AND operatorRef NOT IN ")
        .append(" (SELECT distinct operatorRef FROM ")
        .append(HostObjectConstants.SB_RESPONSE_SUMMARY_TABLE)
        .append(" WHERE api='refund') ");

        Set<PaymentRequestDTO> requestSet = new HashSet<PaymentRequestDTO>();
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, api_version);
            ps.setString(2, consumerKey);
            ps.setString(3, operatorId);
            ps.setShort(4, month);
            ps.setShort(5, year);
            ps.setInt(6, operation);
            ps.setString(7, category);
            ps.setString(8, subcategory);
            
            ps.setString(9, consumerKey);
            ps.setString(10, operatorId);
            ps.setShort(11, year);
            ps.setShort(12, month);
            
            log.debug("SQL (PS) st ---> " + ps.toString());
            results = ps.executeQuery();
            log.debug("SQL (PS) ed ---> ");
            
            while (results.next()) {
                PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
                paymentRequest.setUserId(results.getString("userId"));
                paymentRequest.setConsumerKey(results.getString("consumerKey"));
                String value = results.getString("chargeAmount");
                paymentRequest.setAmount(new BigDecimal(value.replaceAll(",", "")));
                paymentRequest.setCategory(results.getString("category"));
                paymentRequest.setSubcategory(results.getString("subcategory"));
                paymentRequest.setMerchant(results.getString("merchantId"));
                String rqdate = results.getString("time");
                paymentRequest.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rqdate));
                requestSet.add(paymentRequest);
            }
        } catch (Exception e) {
            handleException("getPaymentAmounts", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }
        log.debug("done getPaymentAmounts :" + consumerKey + " :" + api_version + " :" + operatorId);
        return requestSet;
    }
    
    /**
     * Gets the nb payment amounts.
     *
     * @param year the year
     * @param month the month
     * @param consumerKey the consumer key
     * @param api_version the api_version
     * @param operation the operation
     * @param category the category
     * @param subcategory the subcategory
     * @return the nb payment amounts
     * @throws Exception the exception
     */
    //Retriving amount charged from end-user through payment API
    public Set<PaymentRequestDTO> getNbPaymentAmounts(short year, short month, String consumerKey,
                                                           String api_version, int operation, String category, String subcategory) throws
            Exception {

        
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT api,userId,consumerKey,chargeAmount,category,subcategory,merchantId,time FROM ")
        .append(HostObjectConstants.NB_RESPONSE_SUMMARY_TABLE)
        .append(" WHERE year=? and month=? and api_version =? and consumerKey=? and operationType=? and category=? and subcategory=? and responseCode like '2%' AND operatorRef NOT IN ")
        .append(" (SELECT distinct operatorRef from ")
        .append(HostObjectConstants.NB_RESPONSE_SUMMARY_TABLE)
        .append(" WHERE year=? and month=? and consumerKey=? and operationType=101 and responseCode like '2%')");
       
        Set<PaymentRequestDTO> requestSet = new HashSet<PaymentRequestDTO>();
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = connection.prepareStatement(sql.toString());
            ps.setShort(1, year);
            ps.setShort(2, month);
            ps.setString(3, api_version);
            ps.setString(4, consumerKey);
            ps.setInt(5, operation);
            ps.setString(6, category);
            ps.setString(7, subcategory);
            ps.setShort(8, year);
            ps.setShort(9, month);
            ps.setString(10, consumerKey);                        

            results = ps.executeQuery();

            while (results.next()) {
                PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
                paymentRequest.setUserId(results.getString("userId"));
                paymentRequest.setConsumerKey(results.getString("consumerKey"));
                String value = results.getString("chargeAmount");
                paymentRequest.setAmount(new BigDecimal(value.replaceAll(",", "")));
                paymentRequest.setCategory(results.getString("category"));
                paymentRequest.setSubcategory(results.getString("subcategory"));
                paymentRequest.setMerchant(results.getString("merchantId"));
                String rqdate = results.getString("time");
                paymentRequest.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rqdate));
                requestSet.add(paymentRequest);
            }
        } catch (Exception e) {
            handleException("getNbPaymentAmounts", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }

        return requestSet;
    }

    /**
     * Gets the api id.
     *
     * @param apiIdent the api ident
     * @return the api id
     * @throws Exception the exception
     */
    public int getApiId(APIIdentifier apiIdent) throws Exception {
        Connection conn = null;
        int apiId = -1;
        try {
            conn =DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            apiId = ApiMgtDAO.getAPIID(apiIdent, conn);
        } catch (Exception e) {
            handleException("getApiId", e);
        } finally {
            DbUtils.closeAllConnections(null, conn, null);
        }
        return apiId;
    }

    /**
     * Gets the subscription id for application api.
     *
     * @param appId the app id
     * @param apiIdent the api ident
     * @return the subscription id for application api
     * @throws Exception the exception
     */
    public int getSubscriptionIdForApplicationAPI(int appId, APIIdentifier apiIdent) throws
            Exception {
        StringBuilder sql = new StringBuilder(); 
        sql.append("select SUBSCRIPTION_ID from ")
        .append(ReportingTable.AM_SUBSCRIPTION.getTObject())
        .append(" where APPLICATION_ID=? AND API_ID=?");
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> subscriber = new ArrayList<String>();
        int subscriptionId = -1;
        try {
            conn =DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            int apiId = ApiMgtDAO.getAPIID(apiIdent, conn);
            ps = conn.prepareStatement(sql.toString());
            ps.setInt(1, appId);
            ps.setInt(2, apiId);
            rs = ps.executeQuery();
            if (rs.next()) {
                subscriptionId = rs.getInt("SUBSCRIPTION_ID");
            }
            if (subscriptionId == -1) {
                String msg = "Unable to find the subscription ID for API: " + apiIdent + " in the database";
                log.error(msg);
                throw new APIManagementException(msg);
            }

        } catch (Exception e) {
            handleException("getSubscriptionIdForApplicationAPI", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }
        return subscriptionId;
    }

    /**
     * Gets the total api traffic for pie chart.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param subscriber the subscriber
     * @param operator the operator
     * @param applicationId the application id
     * @return the total api traffic for pie chart
     * @throws Exception the exception
     */
    public List<String[]> getTotalAPITrafficForPieChart(String fromDate, String toDate, String subscriber, String operator, int applicationId) throws Exception {
        String consumerKey = null;
        if (subscriber.equals("__ALL__")) {
            subscriber = "%";
        }
        if (operator.equals("__ALL__")) {
            operator = "%";
        }
        if (applicationId == 0) {
            consumerKey = "%";
        } else {
            consumerKey = apiManagerDAO.getConsumerKeyByApplication(applicationId);
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
       
        sql.append("SELECT api, SUM(response_count) AS api_request_count FROM ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" WHERE userId LIKE ? AND consumerKey LIKE ? AND operatorId LIKE ? AND (STR_TO_DATE(")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(".time, '%Y-%m-%d') BETWEEN  STR_TO_DATE(?, '%Y-%m-%d') AND STR_TO_DATE(?, '%Y-%m-%d')) GROUP BY  api");
        
        List<String[]> api_request = new ArrayList<String[]>();

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, subscriber);
            ps.setString(2, consumerKey);
            ps.setString(3, operator);
            ps.setString(4, fromDate);
            ps.setString(5, toDate);
            log.debug("getTotalTrafficForPieChart");
            results = ps.executeQuery();
            while (results.next()) {
                String[] temp = {results.getString("api"), results.getString("api_request_count")};
                api_request.add(temp);
            }
        } catch (Exception e) {
            handleException("getTotalAPITrafficForPieChart", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        return api_request;
    }

    /**
     * Gets the total api traffic for histogram.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param subscriber the subscriber
     * @param operator the operator
     * @param applicationId the application id
     * @param api the api
     * @return the total api traffic for histogram
     * @throws Exception the exception
     */
    public List<String[]> getTotalAPITrafficForHistogram(String fromDate, String toDate, String subscriber, String operator, int applicationId, String api) throws Exception {
        String consumerKey = null;
        if (subscriber.equals("__ALL__")) {
            subscriber = "%";
        }
        if (operator.equals("__ALL__")) {
            operator = "%";
        }
        if (applicationId == 0) {
            consumerKey = "%";
        } else {
            consumerKey = apiManagerDAO.getConsumerKeyByApplication(applicationId);
        }
        if (api.equals("__ALL__")) {
            api = "%";
        }

        if (consumerKey == null) {
            return null;
        }
        List<String[]> api_list = apiManagerDAO.getAPIListForAPITrafficHistogram(fromDate, toDate, api);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("select api,date(time) as date, sum(response_count) hits from ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" where DATE(time) between STR_TO_DATE(?,'%Y-%m-%d') and STR_TO_DATE(?,'%Y-%m-%d') AND operatorId LIKE ? AND userId LIKE ? AND API LIKE ? AND consumerKey LIKE ? ")
        .append("group by api, date");
        
        List<String[]> api_request = new ArrayList<String[]>();

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, fromDate);
            ps.setString(2, toDate);
            ps.setString(3, operator);
            ps.setString(4, subscriber);
            ps.setString(5, api);
            ps.setString(6, consumerKey);

            log.debug("getTotalTrafficForHistogram");
            results = ps.executeQuery();
            while (results.next()) {
                String[] temp = {results.getString(1), results.getDate(2).toString(), results.getString(3)};
                api_request.add(temp);
            }

            for (int i = 0; i < api_request.size(); i++) {
                String apiRequestNameNDate = api_request.get(i)[0].toString() + "_" + api_request.get(i)[1].toString();
                for (int j = 0; j < api_list.size(); j++) {
                    String apiNameNDate = api_list.get(j)[0].toString() + "_" + api_list.get(j)[1].toString();
                    if (apiRequestNameNDate.equals(apiNameNDate)) {
                        api_list.get(j)[2] = api_request.get(i)[2];
                    }
                }
            }

        } catch (Exception e) {
            handleException("getTotalAPITrafficForHistogram", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        return api_list;
    }

    /**
     * Gets the operator wise api traffic for pie chart.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param subscriber the subscriber
     * @param api the api
     * @param applicationId the application id
     * @return the operator wise api traffic for pie chart
     * @throws Exception the exception
     */
    public List<String[]> getOperatorWiseAPITrafficForPieChart(String fromDate, String toDate, String subscriber, String api, int applicationId) throws Exception {
        String consumerKey = null;
        if (subscriber.equals("__ALL__")) {
            subscriber = "%";
        }
        if (api.equals("__ALL__")) {
            api = "%";
        }
        if (applicationId == 0) {
            consumerKey = "%";
        } else {
            consumerKey = apiManagerDAO.getConsumerKeyByApplication(applicationId);
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT operatorId, SUM(response_count) AS api_request_count ")
        .append("FROM ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" WHERE userId LIKE ? AND consumerKey LIKE ? AND api LIKE ? AND (STR_TO_DATE(")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(".time, '%Y-%m-%d') BETWEEN  STR_TO_DATE(?, '%Y-%m-%d') AND STR_TO_DATE(?, '%Y-%m-%d')) GROUP BY operatorId");
        
        List<String[]> api_request = new ArrayList<String[]>();

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, subscriber);
            ps.setString(2, consumerKey);
            ps.setString(3, api);
            ps.setString(4, fromDate);
            ps.setString(5, toDate);
            log.debug("getOperarorWiseAPITrafficForPieChart");
            results = ps.executeQuery();
            while (results.next()) {
                String[] temp = {results.getString("operatorId"), results.getString("api_request_count")};
                api_request.add(temp);
            }
        } catch (Exception e) {
            handleException("getOperatorWiseAPITrafficForPieChart", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        return api_request;
    }

    /**
     * Gets the approval history.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param subscriber the subscriber
     * @param api the api
     * @param applicationId the application id
     * @param operator the operator
     * @return the approval history
     * @throws Exception the exception
     */
    public List<String[]> getApprovalHistory(String fromDate, String toDate, String subscriber, String api, int applicationId, String operator) throws Exception {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("select application_id,name,if(description is null,'Not Specified',description) as description,")
        .append("ELT(FIELD(application_status,'CREATED','APPROVED','REJECTED'),'PENDING APPROVE','APPROVED','REJECTED') as app_status ")
        .append(ReportingTable.AM_APPLICATION.getTObject())
        .append(" where application_id like ? and subscriber_id like ?");

        List<String[]> applist = new ArrayList<String[]>();

        if (!subscriber.equals("__ALL__")) {
            subscriber = String.valueOf(getSubscriberkey(subscriber));
        }

        if (operator == null) {
            operator = "__ALL__";
        }

        try {
            conn =DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql.toString());
            if (applicationId == 0) {
                ps.setString(1, "%");
            } else {
                ps.setInt(1, applicationId);
            }

            if (subscriber.equals("__ALL__")) {
                ps.setString(2, "%");
            } else {
                ps.setInt(2, Integer.parseInt(subscriber));
            }

            log.debug("getOperarorWiseAPITrafficForPieChart");
            rs = ps.executeQuery();
            while (rs.next()) {
                
                //Does not consider default application
                if (!rs.getString("name").equalsIgnoreCase("DefaultApplication")) {
                    //get operator list
                    String operators = OperatorDAO.getApprovedOperatorsByApplication(rs.getInt("application_id"), operator);
                    if (operator.equals("__ALL__")) {
                        String[] temp = {String.valueOf(rs.getInt("application_id")), rs.getString("name"), rs.getString("description"), rs.getString("app_status"), operators};
                        applist.add(temp);
                    } else {
                        if (operators.contains(operator)) {
                            String[] temp = {String.valueOf(rs.getInt("application_id")), rs.getString("name"), rs.getString("description"), rs.getString("app_status"), operators};
                            applist.add(temp);
                        }
                    }
                }
            }
        } catch (Exception e) {
            handleException("getApprovalHistory", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }
        return applist;
    }

    /**
     * Gets the approval history app.
     *
     * @param applicationId the application id
     * @param operatorid the operatorid
     * @return the approval history app
     * @throws Exception the exception
     */
    public List<Approval> getApprovalHistoryApp(int applicationId, String operatorid) throws Exception {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT application_id, sub_status,tier_id, api_name,api_version FROM ")
        .append(ReportingTable.AM_SUBSCRIPTION.getTObject())
        .append(", ")
        .append(ReportingTable.AM_API.getTObject())
        .append(" WHERE ")
        .append(ReportingTable.AM_SUBSCRIPTION.getTObject())
        .append(".api_id = ")
        .append(ReportingTable.AM_API.getTObject())
        .append(".api_id ")
        .append("AND application_id = ?");

        List<Approval> applist = new ArrayList<Approval>();

        try {

            //populate application
            ApiMgtDAO apiMgtDAO = new ApiMgtDAO();
            Application application = apiMgtDAO.getApplicationById(applicationId);
            
            String appstatus = application.getStatus();
            
            applist.add(new Approval(String.valueOf(applicationId), "1", application.getName(), 0, appstatus, "", "", "", null, null));

            OperatorDAO.fillOperatorTrace(applicationId, operatorid, applist);

            conn =DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql.toString());

            ps.setInt(1, applicationId);

            log.debug("getOperarorWiseAPITrafficForPieChart");
            rs = ps.executeQuery();
            while (rs.next()) {
                Approval temp = new Approval(rs.getString("application_id"), "3", rs.getString("api_name"), 0,
                        rs.getString("sub_status"), rs.getString("tier_id"), rs.getString("api_name"), rs.getString("api_version"), null, null);
                applist.add(temp);
            }

        } catch (Exception e) {
            handleException("getApprovalHistoryApp", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }
        return applist;
    }
    
    /**
     * Gets the all operation types.
     *
     * @return the all operation types
     * @throws Exception the exception
     */
    public List<String[]> getAllOperationTypes() throws Exception{
        
        List<String[]> txTypes = new ArrayList<String[]>();
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT `operation_id`, `operation` FROM ")
        .append(ReportingTable.API_OPERATION_TYPES.getTObject());
        
        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = conn.prepareStatement(sql.toString());
            log.debug("getAllOperationTypes");
            results = ps.executeQuery();
            while(results.next()){
                String[] temp = {results.getString(1), results.getString(2)};
                txTypes.add(temp);
            }
        } catch (Exception e) {
            handleException("getAllOperationTypes", e);
        }finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        return txTypes;
    }

    /**
     * Gets the all ap is.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param subscriber the subscriber
     * @param operator the operator
     * @param applicationId the application id
     * @param api the api
     * @return the all ap is
     * @throws Exception the exception
     */
    public List<String[]> getAllAPIs(String fromDate, String toDate, String subscriber, String operator, int applicationId, String api) throws Exception {
        String consumerKey = null;
        if (subscriber.equals("__ALL__")) {
            subscriber = "%";
        }
        if (operator.equals("__ALL__")) {
            operator = "%";
        }
        if (applicationId == 0) {
            consumerKey = "%";
        } else {
            consumerKey = apiManagerDAO.getConsumerKeyByApplication(applicationId);
        }
        if (api.equals("__ALL__")) {
            api = "%";
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT DISTINCT api FROM ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" WHERE userId LIKE ? AND consumerKey LIKE ? AND operatorId LIKE ? AND api LIKE ? AND (STR_TO_DATE(")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(".time, '%Y-%m-%d') BETWEEN  STR_TO_DATE(?, '%Y-%m-%d') AND STR_TO_DATE(?, '%Y-%m-%d'))");
        
        List<String[]> apis = new ArrayList<String[]>();

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, subscriber);
            ps.setString(2, consumerKey);
            ps.setString(3, operator);
            ps.setString(4, api);
            ps.setString(5, fromDate);
            ps.setString(6, toDate);
            log.debug("getAllAPIs");
            results = ps.executeQuery();
            while (results.next()) {
                String[] temp = {results.getString("api")};
                apis.add(temp);
            }
        } catch (Exception e) {
            handleException("getAllAPIs", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        return apis;
    }

    /**
     * Gets the all error response codes.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param subscriber the subscriber
     * @param operator the operator
     * @param applicationId the application id
     * @param api the api
     * @return the all error response codes
     * @throws Exception the exception
     */
    public List<String[]> getAllErrorResponseCodes(String fromDate, String toDate, String subscriber, String operator, int applicationId, String api) throws Exception {
        String consumerKey = null;
        if (subscriber.equals("__ALL__")) {
            subscriber = "%";
        }
        if (operator.equals("__ALL__")) {
            operator = "%";
        }
        if (applicationId == 0) {
            consumerKey = "%";
        } else {
            consumerKey = apiManagerDAO.getConsumerKeyByApplication(applicationId);
        }
        if (api.equals("__ALL__")) {
            api = "%";
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT DISTINCT IFNULL(exceptionId, 'SVC1000') AS exceptionId FROM ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" WHERE userId LIKE ? AND consumerKey LIKE ? AND operatorId LIKE ? AND api LIKE ? AND (STR_TO_DATE(")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(".time, '%Y-%m-%d') BETWEEN  STR_TO_DATE(?, '%Y-%m-%d') AND STR_TO_DATE(?, '%Y-%m-%d')) AND responseCode NOT IN ('200' , '201', '202', '204')");
        
        List<String[]> resCodes = new ArrayList<String[]>();

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, subscriber);
            ps.setString(2, consumerKey);
            ps.setString(3, operator);
            ps.setString(4, api);
            ps.setString(5, fromDate);
            ps.setString(6, toDate);
            log.debug("getAllErrorResponseCodes");
            results = ps.executeQuery();
            while (results.next()) {
                String[] temp = {results.getString("exceptionId")};
                resCodes.add(temp);
            }
        } catch (Exception e) {
            handleException("getAllErrorResponseCodes", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        log.debug("getAllErrorResponseCodes :");
        return resCodes;
    }

    /**
     * Gets the customer care report data.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param msisdn the msisdn
     * @param subscriber the subscriber
     * @param operator the operator
     * @param app the app
     * @param api the api
     * @param startLimit the start limit
     * @param endLimit the end limit
     * @param timeOffset the time offset
     * @return the customer care report data
     * @throws Exception the exception
     */
    public List<String[]> getCustomerCareReportData(String fromDate, String toDate, String msisdn, String subscriber, String operator, String app, String api, String startLimit, String endLimit, String timeOffset) throws Exception {

        String consumerKey = "";

        if (subscriber.equalsIgnoreCase("__ALL__")) {
            subscriber = "%";
        }
        if (operator.equalsIgnoreCase("__ALL__")) {
            operator = "%";
        }
        if ((app.equalsIgnoreCase("__ALL__")) || (app.equalsIgnoreCase("0"))) {
            consumerKey = "%";
        } else {
            consumerKey = getConsumerKeyByAppId(app);
        }
        if (api.equalsIgnoreCase("__ALL__")) {
            api = "%";
        }

        
		String[] fromDateArray = fromDate.split("-");
		String[] toDateArray = toDate.split("-");

		boolean isSameYear = fromDateArray[0].equalsIgnoreCase(toDateArray[0]) ? true: false;
		boolean isSameMonth = fromDateArray[1].equalsIgnoreCase(toDateArray[1]) ? true: false;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet results = null;
		StringBuilder sql = new StringBuilder();
		
		
		
		if (isSameYear && isSameMonth) {
			sql.append("SELECT time, jsonBody, api  FROM ")
			.append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
			.append(" WHERE operatorId LIKE ? AND replace(userid,'@carbon.super','') LIKE ? AND api LIKE ? AND consumerKey LIKE ? AND (day between ? and ? ) AND (month = ?) AND (year = ?) ");
		} else {
			sql.append("SELECT x.time, x.jsonBody, x.api FROM ")
			.append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
			.append( " x WHERE operatorId LIKE ? AND replace(userid,'@carbon.super','') LIKE ? AND api LIKE ? AND consumerKey LIKE ? AND STR_TO_DATE(x.time,'%Y-%m-%d') between STR_TO_DATE(?,'%Y-%m-%d') and STR_TO_DATE(?,'%Y-%m-%d') ");			 
		}
		if (!msisdn.isEmpty()) {
			sql.append("AND (msisdn LIKE ? or (msisdn LIKE ? or jsonBody like '%senderAddress\":\"")
			.append(msisdn)
			.append("%')) ");
		}
		
		sql.append("LIMIT ")
		.append(startLimit)
		.append(" ,")
		.append(endLimit);
        
        List<String[]> api_request_data = new ArrayList<String[]>();

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, operator);
			ps.setString(2, subscriber);
			ps.setString(3, api);
			ps.setString(4, consumerKey);	

			if (isSameYear && isSameMonth) {
				ps.setInt(5, Integer.parseInt(fromDateArray[2]));
				ps.setInt(6, Integer.parseInt(toDateArray[2]));
				ps.setInt(7, Integer.parseInt(fromDateArray[1]));
				ps.setInt(8, Integer.parseInt(fromDateArray[0]));

				if (!msisdn.isEmpty()) {
					// ps.setInt(9,Integer.parseInt(msisdn));
                    ps.setString(9, "%" + msisdn);
                    ps.setString(10, "%" + msisdn);
                    //ps.setString(10, msisdn);

				}
			} else {
				ps.setString(5, fromDate);
				ps.setString(6, toDate);

				if (!msisdn.isEmpty()) {
					// ps.setInt(7,Integer.parseInt(msisdn));
                    ps.setString(7, "%" + msisdn);
                    ps.setString(8, "%" + msisdn);
                    //ps.setString(8, msisdn);
				}
			}

			log.debug("getCustomerCareReportData");
			log.debug("SQL (PS) ---> " + ps.toString());
			results = ps.executeQuery();
			while (results.next()) {
				String localTime = convertToLocalTime(timeOffset,results.getString(1));
				String[] temp = { localTime, results.getString(2),results.getString(3) };
				api_request_data.add(temp);
			}
		} catch (Exception e) {
            handleException("getCustomerCareReportData", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
		log.debug("end getCustomerCareReportData: " + api_request_data.size());
        return api_request_data;
    }
    
     /**
      * Gets the customer care report data count.
      *
      * @param fromDate the from date
      * @param toDate the to date
      * @param msisdn the msisdn
      * @param subscriber the subscriber
      * @param operator the operator
      * @param app the app
      * @param api the api
      * @return the customer care report data count
      * @throws Exception the exception
      */
     public String getCustomerCareReportDataCount(String fromDate, String toDate, String msisdn, String subscriber, String operator, String app, String api) throws Exception {

        String consumerKey = "";
        String count = "";

        if (subscriber.equalsIgnoreCase("__ALL__")) {
            subscriber = "%";
        }
        if (operator.equalsIgnoreCase("__ALL__")) {
            operator = "%";
        }
        if ((app.equalsIgnoreCase("__ALL__")) || (app.equalsIgnoreCase("0"))) {
            consumerKey = "%";
        } else {
            consumerKey = getConsumerKeyByAppId(app);
        }
        if (api.equalsIgnoreCase("__ALL__")) {
            api = "%";
        }
        String[] fromDateArray = fromDate.split("-");
		String[] toDateArray = toDate.split("-");

		boolean isSameYear = fromDateArray[0].equalsIgnoreCase(toDateArray[0]) ? true: false;
		boolean isSameMonth = fromDateArray[1].equalsIgnoreCase(toDateArray[1]) ? true: false;
		

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        sql.append("SELECT COUNT(*) FROM ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" WHERE operatorId LIKE ? AND replace(userid,'@carbon.super','') LIKE ? AND api LIKE ? AND consumerKey LIKE ? ");
        
        if(isSameYear && isSameMonth ){
        	
        	sql.append("AND (day between ? and ? ) AND (month = ?) AND (year = ?) ");
        }else{
        	sql.append("AND STR_TO_DATE(x.time,'%Y-%m-%d') between STR_TO_DATE(?,'%Y-%m-%d') and STR_TO_DATE(?,'%Y-%m-%d') ");
        }
        
        if(!msisdn.isEmpty()) {
        	sql.append("AND msisdn LIKE ? ");
        }
        
        
        List<String[]> api_request_data = new ArrayList<String[]>();

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, operator);
			ps.setString(2, subscriber);
			ps.setString(3, api);
			ps.setString(4, consumerKey);

			if (isSameYear && isSameMonth) {
				ps.setInt(5, Integer.parseInt(fromDateArray[2]));
				ps.setInt(6, Integer.parseInt(toDateArray[2]));
				ps.setInt(7, Integer.parseInt(fromDateArray[1]));
				ps.setInt(8, Integer.parseInt(fromDateArray[0]));
				if (!msisdn.isEmpty()) {
					// ps.setInt(9,Integer.parseInt(msisdn));
					ps.setString(9, "%" + msisdn);
				}
			} else {
				ps.setString(5, fromDate);
				ps.setString(6, toDate);

				if (!msisdn.isEmpty()) {
					// ps.setInt(7,Integer.parseInt(msisdn));
					ps.setString(7, "%" + msisdn);
				}
			}
		

			log.debug("getCustomerCareReportData count");
			log.debug("SQL (PS) ---> " + ps.toString());
			results = ps.executeQuery();
			results.next();
			count = results.getString(1);
		} catch (Exception e) {        	
            handleException("getCustomerCareReportDataCount", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
		log.debug("getCustomerCareReportDataCount :" + count);
        return count;
    }

    /**
     * Gets the API wise traffic for report.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param subscriber the subscriber
     * @param operator the operator
     * @param api the api
     * @return the API wise traffic for report
     * @throws Exception the exception
     */
    public List<String[]> getAPIWiseTrafficForReport(String fromDate, String toDate, String subscriber, String operator, String api, boolean isError, int applicationId) throws Exception {
    	String consumerKey = null;
    	if (subscriber.equals("__ALL__")) {
            subscriber = "%";
        }
        if (operator.equals("__ALL__")) {
            operator = "%";
        }
        if (api.equals("__ALL__")) {
            api = "%";
        }
        if (applicationId == 0) {
            consumerKey = "%";
        } else {
            consumerKey = ApiManagerDAO.getConsumerKeyByApplication(applicationId);
        }
        
        String responseStr = "responseCode LIKE '20_' ";
		if (isError) {
			responseStr = "responseCode NOT LIKE '20_' ";
		}
		
		String[] fromDateArray = fromDate.split("-");
		String[] toDateArray = toDate.split("-");
		String userId;    
                
    	boolean isSameYear = fromDateArray[0].equalsIgnoreCase(toDateArray[0]) ? true : false;
        boolean isSameMonth = fromDateArray[1].equalsIgnoreCase(toDateArray[1]) ? true : false;
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;

        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT time, userId, operatorId, requestId, msisdn, response_count, responseCode, jsonBody, resourcePath, method, api, ussdAction, ussdSessionId, destinationAddress, senderAddress, message, date_Time, resourceURL, message_Id  FROM ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject()) 
        .append(" WHERE ")
        .append(responseStr)        
        .append("AND (operatorId LIKE ? OR spOperatorId LIKE ?) AND ((replace(userid,'@carbon.super','') LIKE ?) OR (replace(spUserId,'@carbon.super','') LIKE ?)) AND api LIKE ? AND ( consumerKey LIKE ? OR spConsumerKey LIKE ? ) ");
        if(isSameYear && isSameMonth){
			sql.append("AND (day between ? and ? ) AND (month = ?) AND (year = ?) ");

		} else {
			sql.append("AND STR_TO_DATE(time,'%Y-%m-%d') between STR_TO_DATE(?,'%Y-%m-%d') and STR_TO_DATE(?,'%Y-%m-%d') ");
			       
		} 
        
        
        List<String[]> api_request = new ArrayList<String[]>();

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, operator);
            if (api.equalsIgnoreCase("ussd")){
                ps.setString(2, operator);
            } else {
                ps.setString(2, null);
            }//ADDED OperatorId
            ps.setString(3, subscriber);
            if (api.equalsIgnoreCase("ussd")){
                ps.setString(4, subscriber);
            } else {
                ps.setString(4, null);
            }//ADDED UserId
            ps.setString(5, api);
            ps.setString(6, consumerKey);
			if (api.equalsIgnoreCase("ussd")){
                ps.setString(7, consumerKey);
            } else {
                ps.setString(7, null);
            }
			if (isSameYear && isSameMonth) {
                ps.setInt(8,Integer.parseInt(fromDateArray[2]) );
                ps.setInt(9,Integer.parseInt(toDateArray[2]) );
				ps.setInt(10, Integer.parseInt(fromDateArray[1]));
				ps.setInt(11, Integer.parseInt(fromDateArray[0]));
			} else {
				ps.setString(8, fromDate);
				ps.setString(9, toDate);
			}
            
            log.debug("getAPIWiseTrafficForReport");
            log.debug("SQL (PS) ---> " + ps.toString());  
            results = ps.executeQuery();
            System.out.println("getAPIWiseTrafficForReport---------------------------------");
            System.out.print("SQL (PS) ---> " + ps.toString());
                         
            
            while (results.next()) {
                
                String dateTime = results.getString(1);
                if(dateTime == null){
                    dateTime = "";
                }
                
                String jsonBody = results.getString(8);
                String requestUrl = results.getString(9);
                String requestMethod = results.getString(10);
                String requestapi = results.getString(11);
                
                if(results.getString(2)!=null && results.getString(2).contains("@")){
					userId = results.getString(2).split("@")[0];
				} else {
					userId = results.getString(2);
				}
                
                String event_type = "";
                String clientCorelator = "";

                if (!jsonBody.isEmpty()) {
                    
                    try {
                        JSONObject homejson = new JSONObject(jsonBody);
                        if (!homejson.isNull("outboundSMSMessageRequest")) {
                            JSONObject smsTransactionObject = (JSONObject) homejson.get("outboundSMSMessageRequest");
                            if (!smsTransactionObject.isNull("clientCorrelator")) {
                                clientCorelator = smsTransactionObject.getString("clientCorrelator");
                            }
                        }
                    } catch(Exception ex){
                    	log.debug("Unable to read JSON body stored in DB :: "+ex);
                        clientCorelator = "";
                    }
                    
                }

                if (!requestUrl.isEmpty() && !requestapi.isEmpty()) {
                	String apitype = findAPIType(requestUrl, requestapi , requestMethod);
                    if (apitype.equalsIgnoreCase("send_sms")) {
                        event_type = "Outbound";
                    } else if (apitype.equalsIgnoreCase("retrive_sms_subscriptions")) {
                        if (requestMethod.equalsIgnoreCase("DELETE")) {
                            event_type = "CancelReceiptService";
                        } else {
                            event_type = "ReceiptService";
                        }
                    } else if (apitype.equalsIgnoreCase("retrive_sms")) {
                        event_type = "Inbound ";
                    } else if (apitype.equalsIgnoreCase("payment")) {
                        event_type = "Charge";
                    } else if (apitype.equalsIgnoreCase("ussd_send")) {
                        event_type = "USSD Outbound";
                    } else if (apitype.equalsIgnoreCase("ussd_receive")) {
                        if(results.getString(12) != null &&  (results.getString(12).equalsIgnoreCase("mocont") || results.getString(12).equalsIgnoreCase("mofin"))){
                            event_type = "USSD MO Callback";
                        } else if (results.getString(12) != null &&  (results.getString(12).equalsIgnoreCase("mtcont") || results.getString(12).equalsIgnoreCase("mtcont"))) {
                            event_type = "USSD MT Callback";
                        }
                    } else if (apitype.equalsIgnoreCase("ussd_subscription")) {
                        event_type = "USSD Subscription";
                    } else if (apitype.equalsIgnoreCase("stop_ussd_subscription")) {
                        if (requestMethod.equalsIgnoreCase("DELETE")) {
                            event_type = "Stop ussd subscription";
                        }
                    } else if (apitype.equalsIgnoreCase("location")) {
                        event_type = "Location";
                    } else if (apitype.equalsIgnoreCase("wallet/payment")){
                        event_type = "Wallet Payment";
                    }else if (apitype.equalsIgnoreCase("wallet/refund")){
                        event_type = "Wallet Refund";
                    }else if (apitype.equalsIgnoreCase("wallet/list")){
                        event_type = "Wallet List";
                    }else if (apitype.equalsIgnoreCase("wallet/balance")){
                        event_type = "Wallet Balance";
                    }
                }

                String[] temp = {dateTime, userId, results.getString(3), event_type, results.getString(4), clientCorelator, results.getString(5), results.getString(6), results.getString(7) , results.getString(13), results.getString(14), results.getString(15), results.getString(16), results.getString(17), results.getString(18), results.getString(19)};
                api_request.add(temp);
            }
        } catch (Exception e) {
            handleException("Error occured while getting API wise traffic for report from the database", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        log.debug("end getAPIWiseTrafficForReport");
        return api_request;
    }

    /**
     * Gets the tx log data.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param subscriber the subscriber
     * @param operator the operator
     * @param opType the op type
     * @param dataType the data type
     * @return the tx log data
     * @throws Exception the exception
     */
    public ResultSet getTxLogData(String fromDate, String toDate, String subscriber, String operator, int opType, String dataType)
    		throws Exception {
        if (subscriber.equals("__ALL__")) {
            subscriber = "%";
        }
        if (operator.equals("__ALL__")) {
            operator = "%";
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        sql.append("SELECT * FROM ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" WHERE STR_TO_DATE(time,'%Y-%m-%d') between STR_TO_DATE(?,'%Y-%m-%d') and STR_TO_DATE(?,'%Y-%m-%d') AND operatorId LIKE ? AND userid LIKE ? AND operationType LIKE ?");
        
        if(dataType.equals("1")){
            sql.append(" AND (responseCode = 201 OR responseCode = 200)");
        } else if (dataType.equals("2")){
            sql.append(" AND (responseCode != 201 AND responseCode != 200)");
        }

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, fromDate);
            ps.setString(2, toDate);
            ps.setString(3, operator);
            ps.setString(4, subscriber);
            ps.setInt(5, opType);
            log.debug("getAPIWiseTrafficForReport");
            results = ps.executeQuery();
            
        } catch (Exception e) {        	
            handleException("getTxLogData", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        return results;
    }

    /**
     * Gets the tx log data nb.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param subscriber the subscriber
     * @param operator the operator
     * @param opType the op type
     * @param dataType the data type
     * @return the tx log data nb
     * @throws Exception the exception
     */
    public ResultSet getTxLogDataNB(String fromDate, String toDate, String subscriber, String operator, int opType, String dataType)
    		throws Exception{
        if (subscriber.equals("__ALL__")) {
            subscriber = "%";
        }
        if (operator.equals("__ALL__")) {
            operator = "%";
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        sql.append("SELECT * FROM ")
        .append(ReportingTable.NB_API_RESPONSE_SUMMARY.getTObject())
        .append("WHERE STR_TO_DATE(time,'%Y-%m-%d') between STR_TO_DATE(?,'%Y-%m-%d') and STR_TO_DATE(?,'%Y-%m-%d') AND userid LIKE ? AND operationType LIKE ?");
        
        if(dataType.equals("1")){
            sql.append(" AND (responseCode = 201 OR responseCode = 200)");
        } else if (dataType.equals("2")){
            sql.append(" AND (responseCode != 201 AND responseCode != 200)");
        }

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, fromDate);
            ps.setString(2, toDate);
            ps.setString(3, subscriber);
            ps.setInt(4, opType);
            log.debug("getAPIWiseTrafficForReport");
            results = ps.executeQuery();
            
        } catch (Exception e) {        	
            handleException("getTxLogDataNB", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        return results;
    }
    
    /**
     * Gets the API wise traffic for report charging.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param subscriber the subscriber
     * @param operator the operator
     * @param api the api
     * @return the API wise traffic for report charging
     * @throws Exception the exception
     */
    public List<String[]> getAPIWiseTrafficForReportCharging(String fromDate, String toDate, String subscriber, String operator, String api, boolean isError) throws Exception {
        if (subscriber.equals("__ALL__")) {
            subscriber = "%";
        }
        if (operator.equals("__ALL__")) {
            operator = "%";
        }
        if (api.equals("__ALL__")) {
            api = "%";
        }
        String responseStr = "responseCode LIKE '20_' ";
		if (isError) {
			responseStr = "responseCode NOT LIKE '20_' ";
		}
		String[] fromDateArray = fromDate.split("-");
		String[] toDateArray = toDate.split("-");
		String userId;      
                
    	boolean isSameYear = fromDateArray[0].equalsIgnoreCase(toDateArray[0]) ? true : false;
        boolean isSameMonth = fromDateArray[1].equalsIgnoreCase(toDateArray[1]) ? true : false;
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;

        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT res.time, res.userId, res.operatorId, res.requestId, res.msisdn, res.chargeAmount, res.responseCode, res.jsonBody, res.resourcePath, res.method, res.purchaseCategoryCode, res.api, res.taxAmount , res.channel , res.onBehalfOf ,res.description, res.transactionOperationStatus , req.transactionOperationStatus  FROM ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject()).append(" res, ")
        .append(ReportingTable.SB_API_REQUEST_SUMMARY.getTObject()).append(" req")
        .append(" WHERE ")
        .append(responseStr)
        .append("  AND res.operatorId LIKE ? AND replace(res.userid,'@carbon.super','') LIKE ? AND res.api LIKE ? AND res.requestId = req.requestId");

        if (isSameYear && isSameMonth){
			sql.append("AND (res.day between ? and ? ) AND (res.month = ?) AND (res.year = ?) ");

		} else {
			sql.append("AND STR_TO_DATE(res.time,'%Y-%m-%d') between STR_TO_DATE(?,'%Y-%m-%d') and STR_TO_DATE(?,'%Y-%m-%d') ");
		}
        
        List<String[]> api_request = new ArrayList<String[]>();

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, operator);
            ps.setString(2, subscriber);
			ps.setString(3, api);
			log.debug(api);
			
			if (isSameYear && isSameMonth) {
				ps.setInt(4, Integer.parseInt(fromDateArray[2]));
				ps.setInt(5, Integer.parseInt(toDateArray[2]));
				ps.setInt(6, Integer.parseInt(fromDateArray[1]));
				ps.setInt(7, Integer.parseInt(fromDateArray[0]));
			} else {
				ps.setString(4, fromDate);
				ps.setString(5, toDate);
			}

            log.debug("getAPIWiseTrafficForReportCharging");
           
            log.debug("SQL (PS) ---> " + ps.toString());
 
            results = ps.executeQuery();
            while (results.next()) {
                String jsonBody = results.getString(8);
                String requestUrl = results.getString(9);
                String requestMethod = results.getString(10);
                String requestapi = results.getString(12);
                if(results.getString(2)!=null && results.getString(2).contains("@")){
					userId = results.getString(2).split("@")[0];
				} else {
					userId = results.getString(2);
				}
                
                String dateTime = results.getString(1);
                if(dateTime == null){
                    dateTime = "";
                }

                String msisdn = "";
                String clientCorelator = "";
                String currency = "";
                String event_type = "";

                if (!jsonBody.isEmpty()) {
                    try {
                        
                    JSONObject homejson = new JSONObject(jsonBody);
                    if (!homejson.isNull("amountTransaction")) {
                        JSONObject transactionObject = (JSONObject) homejson.get("amountTransaction");
                        if (!transactionObject.isNull("endUserId")) {
                            msisdn = transactionObject.getString("endUserId");
                        }
                        if (!transactionObject.isNull("clientCorrelator")) {
                            clientCorelator = transactionObject.getString("clientCorrelator");
                        }
                        if (!transactionObject.isNull("paymentAmount")) {
                            JSONObject paymentAmountoObj = (JSONObject) transactionObject.get("paymentAmount");
                            if (!paymentAmountoObj.isNull("chargingInformation")) {
                                JSONObject chargingInfoObj = (JSONObject) paymentAmountoObj.get("chargingInformation");
                                if (!chargingInfoObj.isNull("currency")) {
                                    currency = chargingInfoObj.getString("currency");
                                }
                            }
                        }
                    }
                    
                    } catch(Exception ex){
                        System.out.println("Unable to read JSON body stored in DB :: "+ex);
                        clientCorelator = "";
                    }
                    
                }

                if (!requestUrl.isEmpty()) {
                	String apitype = findAPIType(requestUrl, requestapi, requestMethod);
                    if (apitype.equalsIgnoreCase("send_sms")) {
                        event_type = "Outbound";
                    } else if (apitype.equalsIgnoreCase("retrive_sms_subscriptions")) {
                        if (requestMethod.equalsIgnoreCase("DELETE")) {
                            event_type = "CancelReceiptService";
                        } else {
                            event_type = "ReceiptService";
                        }
                    } else if (apitype.equalsIgnoreCase("retrive_sms")) {
                        event_type = "Inbound ";
                    } else if (apitype.equalsIgnoreCase("payment")) {
                    	 if (results.getString(18) != null) {
                             event_type = results.getString(18);
                         } else {
                             event_type = "";
                         }
                    } else if (apitype.equalsIgnoreCase("location")) {
                        event_type = "Location";
                    }else if (apitype.equalsIgnoreCase("sms_dn_inbound_notifications")){
						event_type = "DNCallback ";
					} else if (apitype.equalsIgnoreCase("sms_mo_inbound_notifications")) {
						event_type = "MOCallback";
					} else if (apitype.equalsIgnoreCase("query_sms")) {
						event_type = "DNQuery";
					} else if (apitype.equalsIgnoreCase("start_outbound_subscription")){
						event_type = "DNSubscription";
					} else if (apitype.equalsIgnoreCase("stop_outbound_subscription")) {
						event_type = "DNStopSubscription";
					} else if (apitype.equalsIgnoreCase("refund")){
                        event_type = "Refund";
                    }
                }

                String[] temp = {dateTime, userId, results.getString(3), event_type, results.getString(4), clientCorelator, results.getString(5), results.getString(6),
		                        currency, results.getString(7), results.getString(11), results.getString(13), results.getString(14), results.getString(15),
		                        results.getString(16), results.getString(17)};               
                api_request.add(temp);
            }
        } catch (Exception e) {
            handleException("getAPIWiseTrafficForReportCharging", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        log.debug("end getAPIWiseTrafficForReportCharging");
        return api_request;
    }

    /**
     * Gets the error response codes for pie chart.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param subscriber the subscriber
     * @param operator the operator
     * @param applicationId the application id
     * @param api the api
     * @return the error response codes for pie chart
     * @throws Exception the exception
     */
    public List<String[]> getErrorResponseCodesForPieChart(String fromDate, String toDate, String subscriber, String operator, int applicationId, String api) throws Exception {
        String consumerKey = null;
        if (subscriber.equals("__ALL__")) {
            subscriber = "%";
        }
        if (operator.equals("__ALL__")) {
            operator = "%";
        }
        if (applicationId == 0) {
            consumerKey = "%";
        } else {
            consumerKey = apiManagerDAO.getConsumerKeyByApplication(applicationId);
        }
        if (api.equals("__ALL__")) {
            api = "%";
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT IFNULL(exceptionId,'SVC1000') AS exceptionId, SUM(response_count) AS api_response_count FROM ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" WHERE userId LIKE ? AND consumerKey LIKE ? AND operatorId LIKE ? AND (STR_TO_DATE(")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(".time, '%Y-%m-%d') BETWEEN  STR_TO_DATE(?, '%Y-%m-%d') AND STR_TO_DATE(?, '%Y-%m-%d')) AND api LIKE ? AND responseCode NOT IN ('200' , '201', '202', '204') ")
        .append("GROUP BY exceptionId");
        
        List<String[]> api_response_codes = new ArrayList<String[]>();

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, subscriber);
            ps.setString(2, consumerKey);
            ps.setString(3, operator);
            ps.setString(4, fromDate);
            ps.setString(5, toDate);
            ps.setString(6, api);
            log.debug("getErrorResponseCodesForPieChart");
            results = ps.executeQuery();
            while (results.next()) {
                String[] temp = {results.getString("exceptionId"), results.getString("api_response_count")};
                api_response_codes.add(temp);
            }
        } catch (Exception e) {        	
            handleException("getErrorResponseCodesForPieChart", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        return api_response_codes;
    }

    /**
     * Gets the error response codes for histogram.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param subscriber the subscriber
     * @param operator the operator
     * @param applicationId the application id
     * @param api the api
     * @return the error response codes for histogram
     * @throws Exception the exception
     */
    public List<String[]> getErrorResponseCodesForHistogram(String fromDate, String toDate, String subscriber, String operator, int applicationId, String api) throws Exception {
        String consumerKey = null;
        if (subscriber.equals("__ALL__")) {
            subscriber = "%";
        }
        if (operator.equals("__ALL__")) {
            operator = "%";
        }
        if (applicationId == 0) {
            consumerKey = "%";
        } else {
            consumerKey = apiManagerDAO.getConsumerKeyByApplication(applicationId);
        }
        if (api.equals("__ALL__")) {
            api = "%";
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("select a.exceptionId, a.Date, IFNULL(b.HITS,0) HITS ")
        .append("from (select IFNULL(aa.exceptionId, 'SVC1000') exceptionId, a.Date from ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" aa cross join (select DATE(?) - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as Date ")
        .append("from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a ")
        .append("cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b ")
        .append("cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c ) a ")
        .append("where a.Date between ? and ? AND aa.responseCode NOT IN ('200' , '201', '202', '204') group by a.date, exceptionId) a ")
        .append("left join (SELECT IFNULL(exceptionId,'SVC1000') exceptionId, SUM(response_count) HITS, time FROM ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" WHERE DATE(time) between DATE(?) and DATE(?) AND operatorId LIKE ? AND userId LIKE ? AND API LIKE ? AND consumerKey LIKE ? ")
        .append("AND responseCode NOT IN ('200' , '201', '202', '204') ")
        .append("GROUP BY exceptionId, DATE(time)) b on (a.Date = DATE(b.time) and a.exceptionId = b.exceptionId)");
        
        List<String[]> api_response_codes = new ArrayList<String[]>();

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, toDate);
            ps.setString(2, fromDate);
            ps.setString(3, toDate);
            ps.setString(4, fromDate);
            ps.setString(5, toDate);
            ps.setString(6, operator);
            ps.setString(7, subscriber);
            ps.setString(8, api);
            ps.setString(9, consumerKey);
            log.debug("getErrorResponseCodesForHistogram");
            results = ps.executeQuery();
            while (results.next()) {
                String[] temp = {results.getString(1), results.getDate(2).toString(), results.getString(3)};
                api_response_codes.add(temp);
            }
        } catch (Exception e) {        	
            handleException("getErrorResponseCodesForHistogram", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        return api_response_codes;
    }

    /**
     * Find api type.
     *
     * @param ResourceURL the resource url
     * @param requested_api the requested_api
     * @return the string
     */
    private static String findAPIType(String ResourceURL, String requested_api , String serviceMethod) {

        String apiType = null;

        String paymentKeyString = "transactions";
        String outboundkeyString = "outbound";
        String sendSMSkeyStringRequest = "requests";
        String retriveSMSString = "inbound";
        String subscriptionKeyString = "subscriptions";
        String regKeyString = "registrations";
        String delivaryInfoKeyString = "deliveryInfos";
        String delivaryNotifyString = "DeliveryInfoNotification";
        String receivedInfoNotification = "ReceivedInfoNotification";
        String locationString = "location";
        String ussdKeyString = "ussd";
        String smsKeyString = "smsmessaging";
        String paymentapiKeyString = "payment";
        String refundapiKeyString = "refund";
        String walletKeyString = "wallet";
        String balanceKeyString ="balance";
        String listKeyString ="list";

        String lastWord = ResourceURL.substring(ResourceURL.lastIndexOf("/") + 1);

        if (!requested_api.isEmpty() && requested_api.toLowerCase().contains(smsKeyString.toLowerCase())){
        	
            if (ResourceURL.toLowerCase().contains(outboundkeyString.toLowerCase())
                    && ResourceURL.toLowerCase().contains(sendSMSkeyStringRequest.toLowerCase())
                    && (!lastWord.equals(delivaryInfoKeyString))) {
               apiType = "send_sms";
            } else if (ResourceURL.toLowerCase().contains(outboundkeyString.toLowerCase())
                    && lastWord.equals(subscriptionKeyString) &&
                    !requested_api.toLowerCase().contains(ussdKeyString.toLowerCase())) {
                apiType = "start_outbound_subscription";
            } else if (ResourceURL.toLowerCase().contains(outboundkeyString.toLowerCase())
                    && ResourceURL.toLowerCase().contains(subscriptionKeyString.toLowerCase())
                    && (!lastWord.equals(subscriptionKeyString)) && serviceMethod.equalsIgnoreCase("DELETE")) {
                apiType = "stop_outbound_subscription";
            } else if (lastWord.equals(delivaryInfoKeyString)) {
                apiType = "query_sms";
            } else if (ResourceURL.toLowerCase().contains(retriveSMSString.toLowerCase())
                    && ResourceURL.toLowerCase().contains(regKeyString.toLowerCase())) {
                apiType = "retrive_sms";
            } else if (!requested_api.isEmpty() && !requested_api.toLowerCase().contains(ussdKeyString.toLowerCase())
                    && ResourceURL.toLowerCase().contains(retriveSMSString.toLowerCase())
                    && ResourceURL.toLowerCase().contains(subscriptionKeyString.toLowerCase())) {
                apiType = "retrive_sms_subscriptions";
            } else if (ResourceURL.toLowerCase().contains(delivaryNotifyString.toLowerCase())) {
                apiType = "sms_dn_inbound_notifications";
            } else if (ResourceURL.toLowerCase().contains(receivedInfoNotification.toLowerCase())) {
                apiType = "sms_mo_inbound_notifications";
            }
        	
		} else if (!requested_api.isEmpty() && requested_api.toLowerCase().contains(ussdKeyString.toLowerCase())) {
			if (ResourceURL.toLowerCase().contains("outbound")) {
				apiType = "ussd_send";
			} else if (!ResourceURL.toLowerCase().contains(
					subscriptionKeyString.toLowerCase())) {
				apiType = "ussd_receive";
			} else {
				if (lastWord.equals(subscriptionKeyString.toLowerCase())) {
					apiType = "ussd_subscription";
				} else {
					apiType = "stop_ussd_subscription";
				}
			}
		} else if (!requested_api.isEmpty() && requested_api.toLowerCase().contains(locationString.toLowerCase()) &&
            	ResourceURL.toLowerCase().contains(locationString.toLowerCase())) {
        }  else if ( !requested_api.isEmpty() && requested_api.toLowerCase().contains(paymentapiKeyString.toLowerCase()) &&
                ResourceURL.toLowerCase().contains(paymentKeyString.toLowerCase())) {
	            apiType = "payment";
        } else if (!requested_api.isEmpty() && requested_api.toLowerCase().contains(refundapiKeyString.toLowerCase()) &&
                ResourceURL.toLowerCase().contains(paymentapiKeyString.toLowerCase())){
	        	apiType = "refund";
        }else if (!requested_api.isEmpty() && requested_api.toLowerCase().contains(walletKeyString.toLowerCase())){
        	
                if(ResourceURL.toLowerCase().contains(paymentapiKeyString.toLowerCase())){
                    apiType = "wallet/payment";}
                else if(ResourceURL.toLowerCase().contains(refundapiKeyString.toLowerCase())){
                    apiType = "wallet/refund";

                }else if(ResourceURL.toLowerCase().contains(listKeyString.toLowerCase())){
                   apiType = "wallet/list";

                }else if(ResourceURL.toLowerCase().contains(balanceKeyString.toLowerCase())){
                    apiType = "wallet/balance";
                }
        } else {
            return null;
        }
        return apiType;
    }

    /**
     * Gets the app name by consumer key.
     *
     * @param key the key
     * @return the app name by consumer key
     * @throws Exception the exception
     */
    private String getAppNameByConsumerKey(String key) throws Exception {
        String appName = "";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT `name` FROM ")
        .append(ReportingTable.AM_APPLICATION.getTObject())
        .append(" ap, ")
        .append(ReportingTable.AM_APPLICATION_KEY_MAPPING.getTObject())
        .append(" mp WHERE mp.CONSUMER_KEY =? AND mp.KEY_TYPE ='PRODUCTION' AND ap.APPLICATION_ID = mp.APPLICATION_ID");
        
        try {
            conn =DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, key);

            log.debug("getAPIWiseTrafficForReportCharging");
            results = ps.executeQuery();
            while (results.next()) {
                appName = results.getString(1);
            }
        } catch (Exception e) {        	
            handleException("getAppNameByConsumerKey", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }

        return appName;
    }

    /**
     * Gets the consumer key by app id.
     *
     * @param appId the app id
     * @return the consumer key by app id
     * @throws Exception the exception
     */
    private String getConsumerKeyByAppId(String appId) throws Exception {
        String key = "";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT `CONSUMER_KEY` FROM ")
        .append(ReportingTable.AM_APPLICATION_KEY_MAPPING.getTObject())
        .append(" WHERE `APPLICATION_ID` = ? and `KEY_TYPE` = 'PRODUCTION'");
        
        try {
            conn =DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, appId);

            log.debug("getAPIWiseTrafficForReportCharging");
            results = ps.executeQuery();
            while (results.next()) {
                key = results.getString(1);
            }
        } catch (Exception e) {        	
            handleException("getConsumerKeyByAppId", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }

        return key;
    }

    /**
     * Generate sp list.
     *
     * @return the array list
     * @throws Exception the exception
     */
    public ArrayList<SPObject> generateSPList() throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("select distinct(application_id),name,authz_user from (select am.application_id,am.name,ac.authz_user ")
        .append("from IDN_OAUTH2_ACCESS_TOKEN ac,IDN_OAUTH_CONSUMER_APPS ca,")
        .append(ReportingTable.AM_APPLICATION.getTObject())
        .append(" am,")
        .append(ReportingTable.AM_APPLICATION_KEY_MAPPING.getTObject())
        .append(" km where ac.consumer_key=ca.consumer_key ")
        .append("and km.application_id=am.application_id and km.consumer_key=ca.consumer_key and ac.authz_user=ca.username and user_type='APPLICATION' and token_State='Active') as dummy");
 
        ArrayList<SPObject> spList = new ArrayList<SPObject>();
        
        try {
            connection =DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = connection.prepareStatement(sql.toString());
            results = ps.executeQuery();
            while (results.next()) {
            	SPObject spObject=new SPObject();
            	spObject.setAppId(results.getInt("application_id"));
            	spObject.setSpName(results.getString("name"));
            	spList.add(spObject);                
            }
            return spList;
        } catch (Exception e) {            
            handleException("generateSPList", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }
        return null;
    }
    
    
    
    /**
     * Generate sp object.
     *
     * @param appId the app id
     * @return the SP object
     * @throws Exception the exception
     */
    public SPObject generateSPObject(String appId) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("select am.application_id,am.name,ac.authz_user,ac.ACCESS_TOKEN,ca.consumer_key,ca.consumer_secret ")
        .append("from IDN_OAUTH2_ACCESS_TOKEN ac,IDN_OAUTH_CONSUMER_APPS ca,")
        .append(ReportingTable.AM_APPLICATION.getTObject())
        .append(" am,")
        .append(ReportingTable.AM_APPLICATION_KEY_MAPPING.getTObject())
        .append(" km ")
        .append("where ac.consumer_key=ca.consumer_key and km.application_id=am.application_id ")
        .append("and km.consumer_key=ca.consumer_key and ac.authz_user=ca.username and user_type='APPLICATION' and token_State='Active' and am.application_id='")
        .append(appId)
        .append("' limit 1");
        
        SPObject spObject = new SPObject();
        try {
            connection =DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = connection.prepareStatement(sql.toString());
            results = ps.executeQuery();
            while (results.next()) {
            	spObject.setAppId(results.getInt("application_id"));
            	spObject.setSpName(results.getString("name"));
            	spObject.setUserName(results.getString("authz_user"));
            	spObject.setToken(results.getString("ACCESS_TOKEN"));
            	spObject.setKey(results.getString("consumer_key"));
            	spObject.setSecret(results.getString("consumer_secret"));
            }
            return spObject;
        } catch (Exception e) {           
            handleException("generateSPObject", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }
        return null;
    }  
    
    
    /**
     * Gets the total api traffic for line chart.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param subscriber the subscriber
     * @param operator the operator
     * @param applicationId the application id
     * @param api the api
     * @return the total api traffic for line chart
     * @throws Exception the exception
     */
    public List<String[]> getTotalAPITrafficForLineChart(String fromDate, String toDate, String subscriber, String operator, int applicationId, String api) throws Exception {
        String consumerKey = null;
        if (subscriber.equals("__ALL__")) {
            subscriber = "%";
        }
        if (operator.equals("__ALL__")) {
            operator = "%";
        }
        if (applicationId == 0) {
            consumerKey = "%";
        } else {
            consumerKey = apiManagerDAO.getConsumerKeyByApplication(applicationId);
        }
        if (api.equals("__ALL__")) {
            api = "%";
        }

        if (consumerKey == null) {
            return null;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("select date(time) as date, sum(response_count) hits from ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" where DATE(time) between STR_TO_DATE(?,'%Y-%m-%d') and STR_TO_DATE(?,'%Y-%m-%d') AND operatorId LIKE ? AND userId LIKE ? AND API LIKE ? AND consumerKey LIKE ? ")
        .append("group by date");
        
        List<String[]> api_request = new ArrayList<String[]>();

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, fromDate);
            ps.setString(2, toDate);
            ps.setString(3, operator);
            ps.setString(4, subscriber);
            ps.setString(5, api);
            ps.setString(6, consumerKey);
            if (log.isDebugEnabled()) {
        	log.debug("getTotalTrafficForLineChart : SQL " + ps.toString());
            }
            
            results = ps.executeQuery();
            while (results.next()) {
                String[] temp = {results.getDate(1).toString(), results.getString(2)};
                api_request.add(temp);
            }

        } catch (Exception e) {                  
            handleException("getTotalAPITrafficForLineChart", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        return api_request;
    }
    
    /**
     * Gets the all response times for all ap is.
     *
     * @param operator the operator
     * @param userId the user id
     * @param fromDate the from date
     * @param toDate the to date
     * @param timeRange the time range
     * @return the all response times for all ap is
     * @throws Exception the exception
     */
    public List<APIResponseDTO> getAllResponseTimesForAllAPIs(String operator, String userId, String fromDate,
            String toDate, String timeRange) throws Exception {

		if (log.isDebugEnabled()) {
		    log.debug("getAllResponseTimesForAllAPIs() for  Operator "
			    + operator + " Subscriber " + userId + " betweeen "
			    + fromDate + " to " + toDate);
		}
        
        if (operator.contains("__ALL__")) {
            operator = "%";
        }
        
        if (userId.contains("__ALL__")) {
            userId = "%";
        }
      
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
    
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT SUM(response_count) as sumCount, SUM(serviceTime) as sumServiceTime, STR_TO_DATE(time,'%Y-%m-%d') as date FROM ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY)
        .append(" WHERE (time BETWEEN ? AND ?) AND operatorId LIKE ? AND userId LIKE ? GROUP BY date;");
        
        List<APIResponseDTO> responseTimes = new ArrayList<APIResponseDTO>();
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, fromDate + " 00:00:00");
            ps.setString(2, toDate + " 23:59:59");
            ps.setString(3, operator);
            ps.setString(4, userId);
            results = ps.executeQuery();
            while (results.next()) {
                APIResponseDTO resp = new APIResponseDTO();
                resp.setResponseCount(results.getInt("sumCount"));
                resp.setServiceTime(results.getInt("sumServiceTime"));
                resp.setDate(results.getDate("date"));

                responseTimes.add(resp);
            }
        } catch (Exception e) {            
            handleException("getAllResponseTimesForAllAPIs", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }
        return responseTimes;
    }
    
    /**
     * Gets the all response times for ap iby date.
     *
     * @param operator the operator
     * @param userId the user id
     * @param fromDate the from date
     * @param toDate the to date
     * @param api the api
     * @return the all response times for ap iby date
     * @throws Exception the exception
     */
    public List<APIResponseDTO> getAllResponseTimesForAPIbyDate(String operator, String userId, String fromDate,
            String toDate, String api) throws Exception {

	if (log.isDebugEnabled()) {
	    log.debug("getAllResponseTimesForAllAPIs() for  Operator "
		    + operator + " Subscriber " + userId + " betweeen "
		    + fromDate + " to " + toDate);
	}
        
        if (operator.contains("__ALL__")) {
            operator = "%";
        }
        
        if (userId.contains("__ALL__")) {
            userId = "%";
        }
      
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
    
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT api,SUM(response_count) as sumCount, SUM(serviceTime) as sumServiceTime, STR_TO_DATE(time,'%Y-%m-%d') as date FROM ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" WHERE api=? AND (time BETWEEN ? AND ?) AND operatorId LIKE ? AND userId LIKE ? GROUP BY date;");
        
        List<APIResponseDTO> responseTimes = new ArrayList<APIResponseDTO>();
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, api);
            ps.setString(2, fromDate + " 00:00:00");
            ps.setString(3, toDate + " 23:59:59");
            ps.setString(4, operator);
            ps.setString(5, userId);
            results = ps.executeQuery();
            while (results.next()) {
                APIResponseDTO resp = new APIResponseDTO();
                resp.setApiVersion(results.getString("api"));
                resp.setResponseCount(results.getInt("sumCount"));
                resp.setServiceTime(results.getInt("sumServiceTime"));
                resp.setDate(results.getDate("date"));

                responseTimes.add(resp);
            }
        } catch (Exception e) {            
            handleException("getAllResponseTimesForAPIbyDate", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }
        return responseTimes;
    }


    
    /**
     * Gets the time consumption by api.
     *
     * @param operator the operator
     * @param userId the user id
     * @param fromDate the from date
     * @param toDate the to date
     * @param api the api
     * @return the time consumption by api
     * @throws Exception the exception
     */
    public String[] getTimeConsumptionByAPI(String operator, String userId, String fromDate,
            String toDate, String api) throws Exception {

	if (log.isDebugEnabled()) {
	    log.debug("getAllResponseTimesForAllAPIs() for  Operator "
		    + operator + " Subscriber " + userId + " betweeen "
		    + fromDate + " to " + toDate);
	}
        
        if (operator.contains("__ALL__")) {
            operator = "%";
        }
        
        if (userId.contains("__ALL__")) {
            userId = "%";
        }
      
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;


        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT api, MAX(hightestTime) as highestConsumption, SUM(sumServiceTime)/SUM(sumCount) as avgTotalConsump FROM (")
        .append("SELECT api,SUM(response_count) as sumCount, SUM(serviceTime) as sumServiceTime, (SUM(serviceTime)/SUM(response_count)) as hightestTime, STR_TO_DATE(time,'%Y-%m-%d') as date FROM ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject())
        .append(" WHERE api = ? AND (time BETWEEN ? AND ?) AND operatorId LIKE ? AND userId LIKE ? GROUP BY date) AS T;");

        String[] timeConsumerData = new String[3];
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = connection.prepareStatement(sql.toString());
            ps.setString(1, api);
            ps.setString(2, fromDate + " 00:00:00");
            ps.setString(3, toDate + " 23:59:59");
            ps.setString(4, operator);
            ps.setString(5, userId);
            results = ps.executeQuery();
            while (results.next()) {
              
        	timeConsumerData[0] = results.getString("api");
        	timeConsumerData[1] = Double.toString(results.getDouble("highestConsumption"));
        	timeConsumerData[2] = Double.toString(results.getDouble("avgTotalConsump"));
        	
        	
            }
        } catch (Exception e) {           
            handleException("getTimeConsumptionByAPI", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }
        return timeConsumerData;
    }

     
    /**
     * Gets the subscription created time.
     *
     * @param appId the app id
     * @param apiIdent the api ident
     * @return the subscription created time
     * @throws Exception the exception
     */
    public Date getSubscriptionCreatedTime(int appId, APIIdentifier apiIdent)
            throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        Timestamp wfCreatedTime = null;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT WF.WF_CREATED_TIME FROM ")
        .append(ReportingTable.AM_SUBSCRIPTION.getTObject())
        .append(" SUBS, ")
        .append(ReportingTable.AM_WORKFLOWS.getTObject())
        .append(" WF ")
        .append("WHERE ")
        .append("SUBS.APPLICATION_ID = ? ")
        .append("AND SUBS.API_ID = ? ")
        .append("AND WF.WF_TYPE= ? ")
        .append("AND WF.WF_REFERENCE=SUBS.SUBSCRIPTION_ID ");

        try {
            connection =DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            int apiId = ApiMgtDAO.getAPIID(apiIdent, connection);

            ps = connection.prepareStatement(sql.toString());
            ps.setInt(1, appId);
            ps.setInt(2, apiId);
            ps.setString(3, WorkflowConstants.WF_TYPE_AM_SUBSCRIPTION_CREATION);

            results = ps.executeQuery();
            while (results.next()) {
                wfCreatedTime = results.getTimestamp("WF_CREATED_TIME");
            }
            if (log.isDebugEnabled()) {
                log.debug("Subscription creation Time of workflow for app " + appId + " and API " + apiId
                        + " - " + wfCreatedTime);
            }
        } catch (Exception e) {            
            handleException("getSubscriptionCreatedTime", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }
        return (wfCreatedTime != null) ? new Date(wfCreatedTime.getTime()) : null;
    }
    
	/**
	 * Gets the commission percentages.
	 *
	 * @param spId the sp id
	 * @param appId the app id
	 * @return the commission percentages
	 * @throws Exception the exception
	 */
	public Map<String,CommissionPercentagesDTO> getCommissionPercentages(String spId, Integer appId) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet results = null;
		StringBuilder sql = new StringBuilder(); 
		
		sql.append("select id,subscriber,merchant_code,app_id,sp_commission,ads_commission,opco_commission from ")
		.append("rates_percentages where subscriber=? and app_id=?");
		
		Map<String,CommissionPercentagesDTO> requestSet = new HashMap<String,CommissionPercentagesDTO>();
		try {
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, spId);
			ps.setInt(2, appId);

			results = ps.executeQuery();
                        String merchantCode=null;
			while (results.next()) {
                                merchantCode = results.getString("merchant_code");
                            
				CommissionPercentagesDTO dto=new CommissionPercentagesDTO();
				dto.setId(results.getInt("id"));
				dto.setSpId(results.getString("subscriber"));
				dto.setMerchantCode(merchantCode);
				dto.setAppId(results.getInt("app_id"));
				dto.setSpCommission(new BigDecimal(results.getDouble("sp_commission")));
				dto.setAdsCommission(new BigDecimal(results.getDouble("ads_commission")));
				dto.setOpcoCommission(new BigDecimal(results.getDouble("opco_commission")));
				requestSet.put(merchantCode,dto);
			}
		} catch (Exception e) {
			handleException("getCommissionPercentages",e);
		} finally {
			DbUtils.closeAllConnections(ps, connection, results);
		}

		return requestSet;
	}
    
    /**
     * Gets the operation name by id.
     *
     * @param operationId the operation id
     * @return the operation name by id
     * @throws Exception the exception
     */
    public String getOperationNameById(int operationId) throws Exception {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;

        String operationName = null ;
        StringBuilder sql = new StringBuilder(); 
        
        sql.append("SELECT operation FROM ")
        .append(HostObjectConstants.API_OPERATION_TYPES_TABLE)
        .append(" WHERE operation_id=?");
        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            ps = connection.prepareStatement(sql.toString());
            log.debug("getOperationName for operationID---> " + operationId);
            ps.setInt(1, operationId);
            log.debug("SQL (PS) ---> " + ps.toString());
            results = ps.executeQuery();
            while (results.next()) {
                operationName = results.getString("operation");
            }
        } catch (Exception e) {
            handleException("getOperationNameById", e);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }
        return operationName;
    }

     
    /**
     * Convert to local time.
     *
     * @param timeOffset the time offset
     * @param time the time
     * @return the string
     */
    public String convertToLocalTime(String timeOffset, String time){
        Integer offsetValue = Integer.parseInt(timeOffset);

        log.debug("Offset value = " +offsetValue);
        DateTimeZone systemTimeZone = DateTimeZone.getDefault();
        log.debug("system time zone -== " +systemTimeZone.toString());
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime systemDateTime = formatter.parseDateTime(time);
        log.debug("system date time === " + systemDateTime.toString());
        systemDateTime = systemDateTime.withZoneRetainFields(systemTimeZone);
        log.debug("system date time after adding systemtimezone === " + systemDateTime.toString());

        int hours = -1 * offsetValue / 60;
        int minutes = offsetValue % 60;
        minutes = Math.abs(minutes);

        DateTimeZone localTimeZone = DateTimeZone.forOffsetHoursMinutes(hours,minutes);

        log.debug("Local time zone ==== " + localTimeZone.toString());

        DateTime convertedDateTime = systemDateTime.withZone( localTimeZone );

        String convertedDateTimeString = formatter.print(convertedDateTime);

        log.debug("converted time === "+convertedDateTimeString);

        return convertedDateTimeString;

    }
    
    public static List<String[]> getAPIWiseTrafficForReportWallet(String fromDate, String toDate, String subscriber, String operator, String api, boolean isError) throws Exception {
        
    	if (subscriber.equals("__ALL__")) {
            subscriber = "%";
        }
        if (operator.equals("__ALL__")) {
            operator = "%";
        }
        if (api.equals("__ALL__")) {
            api = "%";
        }

        String responseStr = "responseCode LIKE '20_' ";
        
        if (isError) {
            responseStr = "responseCode NOT LIKE '20_' ";
        }

        String[] fromDateArray = fromDate.split("-");
        String[] toDateArray = toDate.split("-");

        boolean isSameYear = fromDateArray[0].equalsIgnoreCase(toDateArray[0]) ? true : false;
        boolean isSameMonth = fromDateArray[1].equalsIgnoreCase(toDateArray[1]) ? true : false;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        StringBuilder sql = new StringBuilder(); 
        String userId;
        
        sql.append("SELECT res.time, res.userId, res.operatorId, res.requestId, res.msisdn, res.chargeAmount, res.responseCode, res.jsonBody, res.resourcePath, res.method, res.purchaseCategoryCode, res.api, res.taxAmount , res.channel , res.onBehalfOf, res.description, res.transactionOperationStatus , req.transactionOperationStatus ")
        .append(ReportingTable.SB_API_RESPONSE_SUMMARY.getTObject()).append(" res, ")
        .append(ReportingTable.SB_API_REQUEST_SUMMARY.getTObject()).append(" req ")
        .append("WHERE ")
        .append(responseStr)
        .append(" AND res.operatorId LIKE ? AND replace(res.userid,'@carbon.super','') LIKE ? AND res.api LIKE ? AND res.requestId = req.requestId ");
        
        if (isSameYear && isSameMonth) {
        	sql.append("AND (res.day between ? and ? ) AND (res.month = ?) AND (res.year = ?) ");
        } else {
        	sql.append("AND STR_TO_DATE(res.time,'%Y-%m-%d') between STR_TO_DATE(?,'%Y-%m-%d') and STR_TO_DATE(?,'%Y-%m-%d') ");
        }

        List<String[]> api_request = new ArrayList<String[]>();

        try {
        	conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
        	ps = conn.prepareStatement(sql.toString());
            ps.setString(1, operator);
            ps.setString(2, subscriber);
            ps.setString(3, api);

            log.debug(api);
            if (isSameYear && isSameMonth) {
                ps.setInt(4, Integer.parseInt(fromDateArray[2]));
                ps.setInt(5, Integer.parseInt(toDateArray[2]));
                ps.setInt(6, Integer.parseInt(fromDateArray[1]));
                ps.setInt(7, Integer.parseInt(fromDateArray[0]));
            } else {
                ps.setString(4, fromDate);
                ps.setString(5, toDate);
            }

            log.debug("getAPIWiseTrafficForReportWallet"+sql);
            log.debug("SQL (PS) ---> " + ps.toString());

            results = ps.executeQuery();
            while (results.next()) {
                String jsonBody = results.getString(8);
                String requestUrl = results.getString(9);
                String requestMethod = results.getString(10);
                String requestapi = results.getString(12);
                if (results.getString(2) != null && results.getString(2).contains("@")) {
                    userId = results.getString(2).split("@")[0];
                } else {
                    userId = results.getString(2);
                }

                String dateTime = results.getString(1);
                if (dateTime == null) {
                    dateTime = "";
                }

                String msisdn = "";
                String clientCorelator = "";
                String currency = "";
                String event_type = "";
                String amount = "";

                if (!jsonBody.isEmpty()) {
                    try {

                        JSONObject homejson = new JSONObject(jsonBody);
                        if (!homejson.isNull("makePayment")) {
                            JSONObject transactionObject = (JSONObject) homejson.get("makePayment");
                            if (!transactionObject.isNull("endUserId")) {
                                msisdn = transactionObject.getString("endUserId");
                            }
                            if (!transactionObject.isNull("clientCorrelator")) {
                                clientCorelator = transactionObject.getString("clientCorrelator");
                            }
                            if (!transactionObject.isNull("paymentAmount")) {
                                JSONObject paymentAmountoObj = (JSONObject) transactionObject.get("paymentAmount");
                               if (!paymentAmountoObj.isNull("chargingInformation")) {
                                    JSONObject chargingInfoObj = (JSONObject) paymentAmountoObj.get("chargingInformation");
                                    if (!chargingInfoObj.isNull("currency")) {
                                        currency = chargingInfoObj.getString("currency");
                                        amount= chargingInfoObj.getString("amount");
                                    }
                                }
                            }
                        }


                        if (!homejson.isNull("refundTransaction")) {
                            JSONObject transactionObject = (JSONObject) homejson.get("refundTransaction");
                            if (!transactionObject.isNull("endUserId")) {
                               msisdn = transactionObject.getString("endUserId");
                            }
                            if (!transactionObject.isNull("clientCorrelator")) {
                                clientCorelator = transactionObject.getString("clientCorrelator");
                            }
                            if (!transactionObject.isNull("paymentAmount")) {
                                JSONObject paymentAmountoObj = (JSONObject) transactionObject.get("paymentAmount");
                                if (!paymentAmountoObj.isNull("chargingInformation")) {
                                    JSONObject chargingInfoObj = (JSONObject) paymentAmountoObj.get("chargingInformation");
                                    if (!chargingInfoObj.isNull("currency")) {
                                        currency = chargingInfoObj.getString("currency");
                                        amount= chargingInfoObj.getString("amount");
                                    }
                                }
                            }
                       }

                    } catch (Exception ex) {
                        System.out.println("Unable to read JSON body stored in DB :: " + ex);
                        clientCorelator = "";
                    }
    	               }

               if (!requestUrl.isEmpty()) {
                    String apitype = findAPIType(requestUrl, requestapi, requestMethod);
                    if (apitype.equalsIgnoreCase("wallet/payment")){
                        event_type = "Wallet Payment";
                    }else if (apitype.equalsIgnoreCase("wallet/refund")){
                        event_type = "Wallet Refund";
                    }else if (apitype.equalsIgnoreCase("wallet/list")){
                        event_type = "Wallet List";
                    }else if (apitype.equalsIgnoreCase("wallet/balance")){
                        event_type = "Wallet Balance";
                    }
               }

                String[] temp = {dateTime, userId, results.getString(3), event_type, results.getString(4), clientCorelator, results.getString(5), amount,
                        currency, results.getString(7), results.getString(11), results.getString(13), results.getString(14), results.getString(15),
                        results.getString(16), results.getString(17)};
                api_request.add(temp);
            }
        } catch (Exception e) {
            System.out.println("Error occured while getting API wise traffic for report (Charging) from the database" + e);
            handleException("Error occured while getting API wise traffic for report (Charging) from the database", e);
        } finally {
        	DbUtils.closeAllConnections(ps, conn, results);
        }
        log.debug("end getAPIWiseTrafficForReportCharging");
        return api_request;
    }
    

}
