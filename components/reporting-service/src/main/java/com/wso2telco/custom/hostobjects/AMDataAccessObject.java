
package com.wso2telco.custom.hostobjects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;
import org.wso2.carbon.apimgt.usage.client.exception.APIMgtUsageQueryServiceClientException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AMDataAccessObject {

    private static volatile DataSource amDatasource = null;
    private static final String AM_DATA_SOURCE = "jdbc/WSO2AM_DB";
    private static final Log log = LogFactory.getLog(AMDataAccessObject.class);

    public static void initializeDatasources() throws ValidatorException {
        if (amDatasource != null) {
            return;
        }

        try {
            Context ctx = new InitialContext();
            amDatasource = (DataSource) ctx.lookup(AM_DATA_SOURCE);
        } catch (NamingException e) {
            handleException("Error while looking up the data source: " + AM_DATA_SOURCE, e);
        }
    }

    public static Connection getApiMgtDBConnection() throws SQLException {

        return APIMgtDBUtil.getConnection();
    }

    public static Connection getAMDBConnection() throws SQLException, ValidatorException {
        initializeDatasources();

        if (amDatasource != null) {
            return amDatasource.getConnection();
        }
        throw new SQLException("Axiata Datasource not initialized properly");
    }

    private static void handleException(String msg, Throwable t) throws ValidatorException {
        log.error(msg, t);
        throw new ValidatorException(msg, t);
    }

    public static String getConsumerKeyByApplication(int applicationId) throws APIMgtUsageQueryServiceClientException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT CONSUMER_KEY FROM AM_APPLICATION_KEY_MAPPING WHERE KEY_TYPE = 'PRODUCTION' AND APPLICATION_ID=?";
        String consumerKey = null;

        try {
            conn = getAMDBConnection();
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
            APIMgtDBUtil.closeAllConnections(ps, conn, results);
        }

        return consumerKey;
    }

    public static List<String[]> getAPIListForAPITrafficHistogram(String fromDate, String toDate, String api) throws APIMgtUsageQueryServiceClientException, SQLException {
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
                + " (select api_name from AM_API where api_name like ?) ap \n"
                + " where al.Date between STR_TO_DATE(?,'%Y-%m-%d') and STR_TO_DATE(?,'%Y-%m-%d')";
        List<String[]> api_list = new ArrayList<String[]>();

        try {
            conn = getAMDBConnection();
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
            APIMgtDBUtil.closeAllConnections(ps, conn, results);
        }

        return api_list;
    }

    public static String getApiNameById(int apiid) throws APIMgtUsageQueryServiceClientException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT API_NAME FROM AM_API WHERE API_ID=?";
        String apiName = null;

        try {
            conn = getAMDBConnection();
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
            APIMgtDBUtil.closeAllConnections(ps, conn, results);
        }

        return apiName;
    }
}
