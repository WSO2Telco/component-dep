package org.workflow.core.dboperation;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.reportingservice.dao.Subscription;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.workflow.core.model.HistoryDetails;
import org.workflow.core.model.HistoryResponse;
import org.workflow.core.model.SubscriptionHistoryDetails;
import org.workflow.core.model.SubscriptionHistoryResponse;
import org.workflow.core.util.Tables;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                .append("opco.operatorname LIKE ? AND amapp.application_id LIKE ? AND amapp.name LIKE ? AND amapp.subscriber_id LIKE ? ) ");

            if(status!=null && !status.isEmpty()&& !status.equals(ALL)) {
            	sql	.append("AND amapp.application_status LIKE ? ");
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
                ps.setString(1,"1");
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

             if (status!=null && !status.isEmpty() && !status.equals(ALL))  {
                ps.setString(6, status);
                 
                 ps.setInt(7, offset);
                 ps.setInt(8, count);
            }else{
                ps.setInt(6, offset);
                ps.setInt(7, count);
             }

           

            log.debug("get Operator Wise API Traffic");

            int size = 0;
            rs = ps.executeQuery();
            while (rs.next()) {
                /** Does not consider default application */
                if (!rs.getString("name").equalsIgnoreCase("DefaultApplication")) {
                    applist.add(new HistoryDetails(rs));
                    size++;
                }
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

    public SubscriptionHistoryResponse getSubscriptionApprovalHistory(Subscription filterObject, String operator, int offset, int count) throws BusinessException {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        List<SubscriptionHistoryDetails> subsApprovalList = new ArrayList<SubscriptionHistoryDetails>();
        SubscriptionHistoryResponse subshistoryResponse = new SubscriptionHistoryResponse();
        String apimgtDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB);

        int subscriptionId = filterObject.getSubscriptionId();
        String apiName = filterObject.getApiName();
        String applicationName = filterObject.getAppName();
        String tier = filterObject.getTierId();

        sql.append("SELECT sub.SUBSCRIPTION_ID, sub.TIER_ID, sub.API_ID, api.API_NAME, api.API_VERSION, api.API_PROVIDER, sub.APPLICATION_ID, app.NAME as application_name, sub.SUB_STATUS, sub.CREATED_BY FROM  ")
                .append(apimgtDB + "." + Tables.AM_SUBSCRIPTION.getTObject() + " sub,")
                .append(apimgtDB + "." + Tables.AM_APPLICATION.getTObject() + " app,")
                .append(apimgtDB + "." + Tables.AM_API.getTObject() + " api ")
                .append("WHERE app.APPLICATION_ID = sub.APPLICATION_ID and api.API_ID = sub.API_ID and ")
                .append("sub.SUBSCRIPTION_ID LIKE ? and api.API_NAME LIKE ? and ")
                .append("app.NAME LIKE ? and sub.TIER_ID LIKE ? and ")
                .append("sub.SUBSCRIPTION_ID IN (SELECT WF.subscriptionid FROM hub_depdb.workflow_subscription_approval WF WHERE approvedby = ?) ")
                .append("ORDER BY SUBSCRIPTION_ID ")
                .append(" LIMIT ?,?");


        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql.toString());

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

            ps.setString(5,operator);
            ps.setInt(6, offset);
            ps.setInt(7, count);

            int size = 0;
            rs = ps.executeQuery();
            while (rs.next()) {
                subsApprovalList.add(new SubscriptionHistoryDetails(rs));
                size++;
            }

            subshistoryResponse.setSubscriptions(subsApprovalList);
            subshistoryResponse.setStart(offset);
            subshistoryResponse.setSize(size);
            subshistoryResponse.setTotal(10);

        } catch (Exception e) {
            handleException("getSubscriptionApprovalHistory", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }
        return subshistoryResponse;
    }

    public int getApplicationCount(int applicationId, String applicationName, String subscriber, String operator, String status) throws BusinessException {

        StringBuilder sql = new StringBuilder();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String depDB = DbUtils.getDbNames().get(DataSourceNames.WSO2TELCO_DEP_DB);
        String apimgtDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB);
        int count = 0;

        sql.append("SELECT count(*) as count FROM ")
                .append("(SELECT application_id, name,created_by,IF(description IS NULL, 'Not Specified', description) AS description,")
                .append("ELT(FIELD(application_status, 'CREATED', 'APPROVED', 'REJECTED'), 'PENDING APPROVE', 'APPROVED', 'REJECTED') AS app_status,")
                .append("(SELECT GROUP_CONCAT(opco.operatorname SEPARATOR ',') FROM " + depDB + "." + Tables.DEP_OPERATOR_APPS.getTObject())
                .append(" opcoApp INNER JOIN " + depDB + "." + Tables.DEP_OPERATORS.getTObject() + " opco ON opcoApp.operatorid = opco.id WHERE ")
                .append("opcoApp.isactive = 1 AND opcoApp.applicationid = amapp.application_id GROUP BY opcoApp.applicationid) AS oparators ")
                .append("FROM " + apimgtDB + "." + Tables.AM_APPLICATION.getTObject() + " amapp ")
                .append("WHERE EXISTS( SELECT 1 FROM " + depDB + "." + Tables.DEP_OPERATOR_APPS.getTObject())
                .append(" opcoApp INNER JOIN " + depDB + "." + Tables.DEP_OPERATORS.getTObject() + " opco ON opcoApp.operatorid = opco.id WHERE ")
                .append("opcoApp.isactive LIKE ? AND opcoApp.applicationid = amapp.application_id AND ")
                .append("opco.operatorname LIKE ? AND amapp.application_id LIKE ? AND amapp.name LIKE ? AND amapp.subscriber_id LIKE ? ) ")
                .append("AND amapp.application_status LIKE ? ")
                .append("ORDER BY application_id) t");

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql.toString());
            if (operator.equals(ALL)) {
                ps.setString(1, "%");
                ps.setString(2, "%");
            } else {
                ps.setString(2, operator);
                ps.setString(1,"1");
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

    public Integer insertSubscriptionApprovalHistory(String taskId, String taskType, String approvedBy, String appId, String apiName) throws BusinessException {
        StringBuilder sql = new StringBuilder();
        Connection conn = null;
        PreparedStatement ps = null;
        int subscriptionId = 0;
        int rs = 0;

        String depDB = DbUtils.getDbNames().get(DataSourceNames.WSO2TELCO_DEP_DB);


        sql.append("INSERT INTO ").append(depDB+"."+Tables.DEP_SUBSCRIPTION_APPROVAL.getTObject()+" (taskid, tasktype, subscriptionid, approvedby) VALUES (?,?,?,?)");

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            subscriptionId = getSubscriptionID(apiName, appId);
            ps = conn.prepareStatement(sql.toString());
            ps.setInt(1, Integer.parseInt(taskId));
            ps.setString(2, taskType);
            ps.setInt(3, subscriptionId);
            ps.setString(4, approvedBy);
            rs = ps.executeUpdate();

        } catch (Exception e) {
            handleException("subscription approval persistent", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, null);
        }
        return rs;
    }

    /**
     * This function can be used to obtain SubscriptionID
     * @param apiName
     * @param applicationId
     * @return int
     * @throws SQLException
     * @throws BusinessException
     */
    public int getSubscriptionID(String apiName, String applicationId) throws SQLException, BusinessException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String apimgtDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB);
        try{
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);

            String sql = "SELECT SUBS.SUBSCRIPTION_ID AS " +
                    "SUBSCRIPTION_ID FROM " + apimgtDB + ".AM_SUBSCRIPTION SUBS, " + apimgtDB +".AM_APPLICATION APP, " + apimgtDB +".AM_API API WHERE API.API_ID = (SELECT API.API_ID FROM AM_API API WHERE API.API_NAME = ?) " +
                    "AND APP.APPLICATION_ID = ? AND SUBS.APPLICATION_ID = APP.APPLICATION_ID AND API.API_ID = SUBS.API_ID " +
                    "AND SUBS.SUB_STATUS != 'BlacklistWhitelistConstants.SubscriptionStatus.REJECTED ' ORDER BY APP.NAME";

            ps = conn.prepareStatement(sql);

            ps.setString(1, apiName);
            ps.setInt(2, Integer.parseInt(applicationId));
            rs = ps.executeQuery();

            while (rs.next()) {
                return rs.getInt("SUBSCRIPTION_ID");
            }
        }
        catch (SQLException e) {
            handleException("subscription approval persistent", e);
            throw e;
        } catch (Exception e) {
            handleException("Error while retrieving subscription ID", e);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, conn, rs);
        }


        return -1;
    }
}
