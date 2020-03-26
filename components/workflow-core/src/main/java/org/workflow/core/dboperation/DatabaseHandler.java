package org.workflow.core.dboperation;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.workflow.core.model.*;
import org.workflow.core.util.Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class DatabaseHandler {

    protected Log log;
    private static final String ALL = "__ALL__";

    public DatabaseHandler() {
        log = LogFactory.getLog(DatabaseHandler.class);
    }


    public int getSubscriberkey(String userid) throws BusinessException {
        StringBuilder sql = new StringBuilder();
        sql.append("select subscriber_id from ")
                .append(Tables.AM_SUBSCRIBER.getTObject())
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
            } else {
                subscriber = 0;
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
     * @param t   the t
     * @throws Exception the exception
     */
    public void handleException(String msg, Throwable t) throws BusinessException {
        log.error(msg, t);
        throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
    }

    public HistoryResponse getApprovalHistory(String subscriber, String applicationName, int applicationId, String operator, String status, int offset, int count) throws BusinessException {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        List<HistoryDetails> applist = new ArrayList<HistoryDetails>();
        HistoryResponse historyResponse = new HistoryResponse();
        String depDB = DbUtils.getDbNames().get(DataSourceNames.WSO2TELCO_DEP_DB);
        String apimgtDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB);

        sql.append("SELECT * FROM ")
                .append("(SELECT application_id, name,created_by,IF(description IS NULL, 'Not Specified', description) AS description,")
                .append("ELT(FIELD(application_status, 'CREATED', 'APPROVED', 'REJECTED'), 'PENDING APPROVE', 'APPROVED', 'REJECTED') AS app_status,")
                .append("(SELECT GROUP_CONCAT(opco.operatorname SEPARATOR ',') FROM " + depDB + "." + Tables.DEP_OPERATOR_APPS.getTObject() + " opcoApp ")
                .append("INNER JOIN " + depDB + "." + Tables.DEP_OPERATORS.getTObject() + " opco ON opcoApp.operatorid = opco.id ")
                .append("WHERE opcoApp.isactive = 1 AND opcoApp.applicationid = amapp.application_id GROUP BY opcoApp.applicationid) AS oparators ")
                .append("FROM " + apimgtDB + "." + Tables.AM_APPLICATION.getTObject() + " amapp ")
                .append("WHERE EXISTS( SELECT 1 FROM " + depDB + "." + Tables.DEP_OPERATOR_APPS.getTObject() + " opcoApp ")
                .append("INNER JOIN " + depDB + "." + Tables.DEP_OPERATORS.getTObject() + " opco ON opcoApp.operatorid = opco.id ")
                .append("WHERE opcoApp.isactive LIKE ? AND opcoApp.applicationid = amapp.application_id AND ")
                .append("opco.operatorname LIKE ? AND amapp.application_id LIKE ? AND amapp.name LIKE ? AND amapp.subscriber_id LIKE ? AND amapp.name != 'DefaultApplication' ) ");

        if (status != null && !status.isEmpty() && !status.equals(ALL)) {
            sql.append("AND amapp.application_status LIKE ? ");
        }

        sql.append("ORDER BY application_id) t")
                .append(" LIMIT ?,?");

        if (!subscriber.equals(ALL)) {
            subscriber = String.valueOf(getSubscriberkey(subscriber));
        }

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql.toString());
            if (operator.equals(ALL)) {
                ps.setString(2, "%");
                ps.setString(1, "%");
            } else {
                ps.setString(2, operator);
                ps.setString(1, "1");
            }
            if (applicationId == 0) {
                ps.setString(3, "%");
            } else {
                ps.setInt(3, applicationId);
            }

            if (applicationName.equals(ALL)) {
                ps.setString(4, "%");
            } else {
                ps.setString(4, applicationName);
            }

            if (subscriber.equals(ALL)) {
                ps.setString(5, "%");
            } else {
                ps.setInt(5, Integer.parseInt(subscriber));
            }

            if (status != null && !status.isEmpty() && !status.equals(ALL)) {
                ps.setString(6, status);

                ps.setInt(7, offset);
                ps.setInt(8, count);
            } else {
                ps.setInt(6, offset);
                ps.setInt(7, count);
            }

           

            log.debug("get Operator Wise API Traffic");

            int size = 0;
            rs = ps.executeQuery();
            while (rs.next()) {
                /** Does not consider default application. Filtered in the query */
                applist.add(new HistoryDetails(rs));
                size++;
            }

            historyResponse.setApplications(applist);
            historyResponse.setStart(offset);
            historyResponse.setSize(size);
            historyResponse.setTotal(getApplicationCount(applicationId, applicationName, subscriber, operator, status));


        } catch (Exception e) {
            handleException("getApprovalHistory", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }
        return historyResponse;
    }

    public HistoryResponse getApprovalHistoryWithPendingJobs(String subscriber, String applicationName, int applicationId, String createdby, String description, String status, int offset, int count) throws BusinessException {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String createdStatus = "CREATED";
        List<HistoryDetails> applist = new ArrayList<HistoryDetails>();
        HistoryResponse historyResponse = new HistoryResponse();
        String apimgtDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT app.APPLICATION_ID, app.NAME, app.APPLICATION_TIER, app.CREATED_BY, IF(app.description IS NULL," +
                " 'Not Specified', app.description) AS description" +
                ",ELT(FIELD(application_status, 'CREATED', 'APPROVED', 'REJECTED'), 'PENDING APPROVE', 'APPROVED', 'REJECTED') " +
                "as app_status, null as oparators FROM " + apimgtDB + ".am_application app " +
                "WHERE app.APPLICATION_ID LIKE ? AND app.NAME LIKE ? AND app.CREATED_BY LIKE ? AND app.description LIKE ? AND app.NAME != 'DefaultApplication' ");

        if (status != null && !status.isEmpty() && !status.equals(ALL)) {
            sql.append("AND app.application_status LIKE ? ");
        }

        sql.append("ORDER BY app.APPLICATION_ID ")
                .append(" LIMIT ?,?");

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql.toString());

            if (applicationId == 0) {
                ps.setString(1, "%");
            } else {
                ps.setInt(1, applicationId);
            }

            if (applicationName.equals(ALL)) {
                ps.setString(2, "%");
            } else {
                ps.setString(2, applicationName);
            }

            if (createdby.equals(ALL)) {
                ps.setString(3, "%");
            } else {
                ps.setString(3, createdby);
            }

            if (description.equals(ALL)) {
                ps.setString(4, "%");
            } else {
                ps.setString(4, "%" + description + "%");
            }

            if (status != null && !status.isEmpty() && !status.equals(ALL)) {
               if(status.toLowerCase().startsWith("pending")){
                    status = createdStatus.toLowerCase();
                    ps.setString(5, status);
                } else {
                    ps.setString(5, status);
                }
                ps.setInt(6, offset);
                ps.setInt(7, count);
            } else {
                ps.setInt(5, offset);
                ps.setInt(6, count);
            }

            int size = 0;
            rs = ps.executeQuery();
            while (rs.next()) {
                /** Does not consider default application. Filtered in the query */
                applist.add(new HistoryDetails(rs));
                size++;
            }

            historyResponse.setApplications(applist);
            historyResponse.setStart(offset);
            historyResponse.setSize(size);
            historyResponse.setTotal(getApplicationCountWithPendingJobs(applicationId, applicationName, createdby, status));


        } catch (Exception e) {
            handleException("getApprovalHistory", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }
        return historyResponse;
    }

    private Integer getApplicationCountWithPendingJobs(int applicationId, String applicationName, String createdby, String status) throws BusinessException {
        StringBuilder sql = new StringBuilder();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        String createdStatus = "CREATED";
        String apimgtDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB);

        sql.append("SELECT count(*) as count FROM " + apimgtDB + ".am_application app " +
                "WHERE app.APPLICATION_ID LIKE ? AND app.NAME LIKE ? AND app.CREATED_BY LIKE ? ");

        if (status != null && !status.isEmpty() && !status.equals(ALL)) {
            sql.append("AND app.application_status LIKE ? ");
        }

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql.toString());
            if (applicationId == 0) {
                ps.setString(1, "%");
            } else {
                ps.setInt(1, applicationId);
            }

            if (applicationName.equals(ALL)) {
                ps.setString(2, "%");
            } else {
                ps.setString(2, applicationName);
            }

            if (createdby.equals(ALL)) {
                ps.setString(3, "%");
            } else {
                ps.setString(3, createdby);
            }

            if (status != null && !status.isEmpty() && !status.equals(ALL)) {
                if(status.toLowerCase().startsWith("pending")){
                    status = createdStatus.toLowerCase();
                    ps.setString(4, status);
                } else {
                    ps.setString(4, status);
                }
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            handleException("getApplicationCountError", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }

        return count;
    }


    public SubscriptionHistoryResponse getSubscriptionApprovalHistory(SubscriptionFilter filterObject, String operator, int offset, int count) throws BusinessException {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        List<SubscriptionHistoryDetails> subsApprovalList = new ArrayList<SubscriptionHistoryDetails>();
        SubscriptionHistoryResponse subshistoryResponse = new SubscriptionHistoryResponse();
        String apimgtDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB);
        String statDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_STATS_DB);

        int subscriptionId = filterObject.getSubscriptionId();
        String apiName = filterObject.getApiName();
        String applicationName = filterObject.getAppName();
        int applicationId = filterObject.getAppId();
        String tier = filterObject.getTierId();
        String createdBy = filterObject.getCreatedBy();
        int apiId = filterObject.getApiId();
        String subStatus = filterObject.getSubStatus();

        sql = "SELECT sub.SUBSCRIPTION_ID, sub.TIER_ID, sub.API_ID, api.API_NAME, api.API_VERSION, api.API_PROVIDER, sub.APPLICATION_ID, app.NAME as application_name, sub.SUB_STATUS, sub.CREATED_BY " +
                "FROM " +
                apimgtDB + "." + Tables.AM_SUBSCRIPTION.getTObject() + " sub," +
                apimgtDB + "." + Tables.AM_APPLICATION.getTObject() + " app," +
                apimgtDB + "." + Tables.AM_API.getTObject() + " api " +
                "WHERE app.APPLICATION_ID = sub.APPLICATION_ID and api.API_ID = sub.API_ID and " +
                "app.APPLICATION_ID = sub.APPLICATION_ID and api.API_ID = sub.API_ID and " +
                "sub.SUBSCRIPTION_ID LIKE ? and api.API_NAME LIKE ? and " +
                "app.NAME LIKE ? and sub.TIER_ID LIKE ? and sub.CREATED_BY LIKE ? and " +
                "sub.SUBSCRIPTION_ID IN " +
                "( SELECT distinct sub.SUBSCRIPTION_ID FROM " +
                apimgtDB + "." + Tables.AM_SUBSCRIPTION.getTObject() + " sub " +
                "INNER JOIN " +
                statDB + "." + Tables.AM_SUB_APPROVAL_AUDIT.getTObject() + " audit " +
                "ON audit.APP_ID = sub.APPLICATION_ID WHERE audit.COMPLETED_BY_USER = ?) " +
                "AND api.API_ID LIKE ? " +
                "AND sub.SUB_STATUS LIKE ? " +
                "AND sub.APPLICATION_ID LIKE ? " +
                "ORDER BY SUBSCRIPTION_ID " +
                " LIMIT ?,?";

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql);

            if (subscriptionId == 0) {
                ps.setString(1, "%");
            } else {
                ps.setInt(1, subscriptionId);
            }

            if (apiName.equals(ALL)) {
                ps.setString(2, "%");
            } else {
                ps.setString(2, apiName);
            }

            if (applicationName.equals(ALL)) {
                ps.setString(3, "%");
            } else {
                ps.setString(3, applicationName);
            }

            if (tier.equals(ALL)) {
                ps.setString(4, "%");
            } else {
                ps.setString(4, tier);
            }

            if (createdBy.equals(ALL)) {
                ps.setString(5, "%");
            } else {
                ps.setString(5, createdBy);
            }

            ps.setString(6, operator);

			if (apiId == 0) {
				ps.setString(7, "%");
			} else {
				ps.setInt(7, apiId);
			}

            if (subStatus.equals(ALL)) {
                ps.setString(8, "%");
            } else {
                ps.setString(8, subStatus);
            }

            if (applicationId == 0) {
                ps.setString(9, "%");
            } else {
                ps.setInt(9, applicationId);
            }

            ps.setInt(10, offset);
            ps.setInt(11, count);

            int size = 0;
            rs = ps.executeQuery();
            while (rs.next()) {
                subsApprovalList.add(new SubscriptionHistoryDetails(rs));
                size++;
            }

            subshistoryResponse.setSubscriptions(subsApprovalList);
            subshistoryResponse.setStart(offset);
            subshistoryResponse.setSize(size);
            subshistoryResponse.setTotal(getSubscriptionTotal(subscriptionId, apiName, applicationName, tier, createdBy, operator));

        } catch (Exception e) {
            handleException("getSubscriptionApprovalHistory", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }
        return subshistoryResponse;
    }

    public int getApplicationCount(int applicationId, String applicationName, String subscriber, String operator, String status) throws BusinessException {

        String sql = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String depDB = DbUtils.getDbNames().get(DataSourceNames.WSO2TELCO_DEP_DB);
        String apimgtDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB);
        int count = 0;

        sql = "SELECT count(*) as count FROM (SELECT application_id, name,created_by,IF(description IS NULL, 'Not Specified', description) AS description," +
                "ELT(FIELD(application_status, 'CREATED', 'APPROVED', 'REJECTED'), 'PENDING APPROVE', 'APPROVED', 'REJECTED') " +
                "AS app_status,(SELECT GROUP_CONCAT(opco.operatorname SEPARATOR ',') FROM " + depDB + "." + Tables.DEP_OPERATOR_APPS.getTObject() +
                " opcoApp INNER JOIN " + depDB + "." + Tables.DEP_OPERATORS.getTObject() + " opco ON opcoApp.operatorid = opco.id WHERE " +
                " opcoApp.isactive = 1 AND opcoApp.applicationid = amapp.application_id GROUP BY opcoApp.applicationid) AS oparators " +
                "FROM " + apimgtDB + "." + Tables.AM_APPLICATION.getTObject() + " amapp " +
                "WHERE EXISTS( SELECT 1 FROM " + depDB + "." + Tables.DEP_OPERATOR_APPS.getTObject() +
                " opcoApp INNER JOIN " + depDB + "." + Tables.DEP_OPERATORS.getTObject() + " opco ON opcoApp.operatorid = opco.id WHERE " +
                "opcoApp.isactive LIKE ? AND opcoApp.applicationid = amapp.application_id AND " +
                "opco.operatorname LIKE ? AND amapp.application_id LIKE ? AND amapp.name LIKE ? AND amapp.subscriber_id LIKE ? ) AND amapp.application_status LIKE ? " +
                "ORDER BY application_id) t";

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql);
            if (operator.equals(ALL)) {
                ps.setString(1, "%");
                ps.setString(2, "%");
            } else {
                ps.setString(2, operator);
                ps.setString(1, "1");
            }
            if (applicationId == 0) {
                ps.setString(3, "%");
            } else {
                ps.setInt(3, applicationId);
            }

            if (applicationName.equals(ALL)) {
                ps.setString(4, "%");
            } else {
                ps.setString(4, applicationName);
            }

            if (subscriber.equals(ALL)) {
                ps.setString(5, "%");
            } else {
                ps.setInt(5, Integer.parseInt(subscriber));
            }

            if (status.equals(ALL)) {
                ps.setString(6, "%");
            } else {
                ps.setString(6, status);
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            handleException("getSubscriberkey", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }

        return count;
    }

    /**
     * Subscription Total
     *
     * @param subscriptionId
     * @param apiName
     * @param applicationName
     * @param tier
     * @param createdBy
     * @param operator
     **/
    public int getSubscriptionTotal(int subscriptionId, String apiName, String applicationName, String tier, String createdBy, String operator) throws BusinessException {

        String sql = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String statDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_STATS_DB);
        String apimgtDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB);
        int count = 0;

        sql = "SELECT count(*) as count  " +
                "FROM " +
                apimgtDB + "." + Tables.AM_SUBSCRIPTION.getTObject() + " sub," +
                apimgtDB + "." + Tables.AM_APPLICATION.getTObject() + " app," +
                apimgtDB + "." + Tables.AM_API.getTObject() + " api " +
                "WHERE app.APPLICATION_ID = sub.APPLICATION_ID and api.API_ID = sub.API_ID and " +
                "app.APPLICATION_ID = sub.APPLICATION_ID and api.API_ID = sub.API_ID and " +
                "sub.SUBSCRIPTION_ID LIKE ? and api.API_NAME LIKE ? and " +
                "app.NAME LIKE ? and sub.TIER_ID LIKE ? and sub.CREATED_BY LIKE ? and " +
                "sub.SUBSCRIPTION_ID IN " +
                "( SELECT distinct sub.SUBSCRIPTION_ID FROM " +
                apimgtDB + "." + Tables.AM_SUBSCRIPTION.getTObject() + " sub " +
                "INNER JOIN " +
                statDB + "." + Tables.AM_SUB_APPROVAL_AUDIT.getTObject() + " audit " +
                "ON audit.APP_ID = sub.APPLICATION_ID WHERE audit.COMPLETED_BY_USER = ?) ";

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql);

            if (subscriptionId == 0) {
                ps.setString(1, "%");
            } else {
                ps.setInt(1, subscriptionId);
            }

            if (apiName.equals(ALL)) {
                ps.setString(2, "%");
            } else {
                ps.setString(2, apiName);
            }

            if (applicationName.equals(ALL)) {
                ps.setString(3, "%");
            } else {
                ps.setString(3, applicationName);
            }

            if (tier.equals(ALL)) {
                ps.setString(4, "%");
            } else {
                ps.setString(4, tier);
            }

            if (createdBy.equals(ALL)) {
                ps.setString(5, "%");
            } else {
                ps.setString(5, createdBy);
            }

            ps.setString(6, operator);

            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            handleException("getSubscriptionTotal ERROR", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }

        return count;
    }

    public String getAllSubscriptionThrottling() throws  BusinessException {

        String sql = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String apimgtDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB);


        sql = "SELECT (NAME)  " +
                "FROM " +
                apimgtDB + "." + Tables.AM_SUBSCRIPTION_POLICY.getTObject();

        List<String> allvales = new ArrayList<String>();

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String val = rs.getString("NAME");
                allvales.add(val);
            }

        } catch (Exception e) {
            handleException("getSubscriptionThrottleDB ERROR", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }

        return allvales.stream().collect(Collectors.joining(","));

    }


    public String getAllApplicationThrottling() throws  BusinessException {

        String sql = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String apimgtDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB);


        sql = "SELECT (NAME)  " +
                "FROM " +
                apimgtDB + "." + Tables.AM_APPLICATION_POLICY.getTObject();

        List<String> allvales = new ArrayList<String>();

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String val = rs.getString("NAME");
                allvales.add(val);
            }

        } catch (Exception e) {
            handleException("getApplicationThrottleDB ERROR", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }

        return allvales.stream().collect(Collectors.joining(","));

    }

}
