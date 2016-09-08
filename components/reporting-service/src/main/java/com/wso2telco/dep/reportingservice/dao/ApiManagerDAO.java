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
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.usage.client.exception.APIMgtUsageQueryServiceClientException;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.reportingservice.util.ReportingTable;

// TODO: Auto-generated Javadoc
/**
 * The Class AMDataAccessObject.
 */
public class ApiManagerDAO {   
    
    /** The Constant log. */
    private static final Log log = LogFactory.getLog(ApiManagerDAO.class);
   
    /**
     * Gets the consumer key by application.
     *
     * @param applicationId the application id
     * @return the consumer key by application
     * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
     * @throws SQLException the SQL exception
     */
    public static String getConsumerKeyByApplication(int applicationId) throws APIMgtUsageQueryServiceClientException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT CONSUMER_KEY FROM "+ ReportingTable.AM_APPLICATION_KEY_MAPPING +" WHERE KEY_TYPE = 'PRODUCTION' AND APPLICATION_ID=?";
        String consumerKey = null;

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, applicationId);
            log.debug("getConsumerKeyByApplication");
            results = ps.executeQuery();
            while (results.next()) {
                consumerKey = results.getString("CONSUMER_KEY");
            }
        } catch (Exception e) {
        	log.error("Error occured while getting consumer key from the database" + e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }

        return consumerKey;
    }

    /**
     * Gets the API list for api traffic histogram.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param api the api
     * @return the API list for api traffic histogram
     * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
     * @throws SQLException the SQL exception
     */
    public List<String[]> getAPIListForAPITrafficHistogram(String fromDate, String toDate, String api) throws APIMgtUsageQueryServiceClientException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "select ap.API_NAME,al.Date,0 as hits from\n"
                + "( select STR_TO_DATE(?,'%Y-%m-%d') -INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as Date\n"
                + "  from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a \n"
                + "  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n"
                + "  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c  \n"
                + "  ) al \n"
                + " cross join \n"
                + " (select api_name from "+ ReportingTable.AM_API +" where api_name like ?) ap \n"
                + " where al.Date between STR_TO_DATE(?,'%Y-%m-%d') and STR_TO_DATE(?,'%Y-%m-%d')";
        List<String[]> api_list = new ArrayList<String[]>();

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql);
            ps.setString(1, toDate);
            ps.setString(2, api);
            ps.setString(3, fromDate);
            ps.setString(4, toDate);
            log.debug("getAPIListForAPITrafficHistogram");
            results = ps.executeQuery();
            while (results.next()) {
                String[] temp = {results.getString(1), results.getDate(2).toString(), results.getString(3)};
                api_list.add(temp);
            }
        } catch (Exception e) {
        	log.error("Error occured while getting API list from the database" + e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }

        return api_list;
    }

    /**
     * Gets the api name by id.
     *
     * @param apiid the apiid
     * @return the api name by id
     * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
     * @throws SQLException the SQL exception
     */
    public String getApiNameById(int apiid) throws APIMgtUsageQueryServiceClientException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT API_NAME FROM "+ ReportingTable.AM_API +" WHERE API_ID=?";
        String apiName = null;

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, apiid);
            log.debug("getApiName");
            results = ps.executeQuery();
            while (results.next()) {
                apiName = results.getString("API_NAME");
            }
        } catch (Exception e) {
        	log.error("Error occured while getting API name from the database" + e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }

        return apiName;
    }
}
