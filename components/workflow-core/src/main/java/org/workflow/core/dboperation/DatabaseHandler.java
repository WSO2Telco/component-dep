package org.workflow.core.dboperation;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.reportingservice.dao.OperatorDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.workflow.core.model.HistoryDetails;
import org.workflow.core.model.HistoryResponse;
import org.workflow.core.util.Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                subscriber = 1;
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

    public HistoryResponse getApprovalHistory(String subscriber, String applicationName, int applicationId, String operator, int offset, int count) throws BusinessException {

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
                .append("WHERE opcoApp.applicationid = amapp.application_id AND ")
                .append("opco.operatorname LIKE ? AND amapp.application_id LIKE '%' AND amapp.name LIKE '%' AND amapp.subscriber_id LIKE '%') ) ")
                .append("ORDER BY application_id) t LIMIT ?,?");


//        sql.append("select application_id,name,created_by,if(description is null,'Not Specified',description) as description,")
//                .append("ELT(FIELD(application_status,'CREATED','APPROVED','REJECTED'),'PENDING APPROVE','APPROVED','REJECTED') as app_status from ")
//                .append(Tables.AM_APPLICATION.getTObject())
//                .append(" where application_id like ? and name like ? and subscriber_id like ?")
//                .append(" order by application_id LIMIT ?,?");

        if (!subscriber.equals(ALL)) {
            subscriber = String.valueOf(getSubscriberkey(subscriber));
        }

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql.toString());
            if (operator.equals(ALL)) {
                ps.setString(1, "%");
            } else {
                ps.setString(1, operator);
            }
            if (applicationId == 0) {
                ps.setString(2, "%");
            } else {
                ps.setInt(2, applicationId);
            }

            if (applicationName.equals(ALL)) {
                ps.setString(3, "%");
            } else {
                ps.setString(3, applicationName);
            }

            if (subscriber.equals(ALL)) {
                ps.setString(4, "%");
            } else {
                ps.setInt(4, Integer.parseInt(subscriber));
            }

            ps.setInt(5, offset);
            ps.setInt(6, count);

            log.debug("get Operator Wise API Traffic");
            rs = ps.executeQuery();
            while (rs.next()) {
                /** Does not consider default application */
                if (!rs.getString("name").equalsIgnoreCase("DefaultApplication")) {
                    //get operator list
                        HistoryDetails details = new HistoryDetails();
                        details.setApplicationId(rs.getInt("application_id"));
                        details.setApplicationName(rs.getString("name"));
                        details.setApplicationDescription(rs.getString("description"));
                        details.setStatus(rs.getString("app_status"));
                        details.setCreatedBy(rs.getString("created_by"));
                        details.setApprovedOn(rs.getString("oparators"));
                        applist.add(details);

                }
            }

            historyResponse.setApplications(applist);
            historyResponse.setStart(offset);
            historyResponse.setSize(count);
            historyResponse.setTotal(getApplicationCount(applicationId, applicationName, subscriber));


        } catch (Exception e) {
            handleException("getApprovalHistory", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }
        return historyResponse;
    }

    public int getApplicationCount(int applicationId, String applicationName, String subscriber) throws BusinessException {

        String sqlQuery = "select count(*) as count from AM_APPLICATION where application_id like ? and name like ? and subscriber_id like ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sqlQuery);
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

            if (subscriber.equals(ALL)) {
                ps.setString(3, "%");
            } else {
                ps.setInt(3, Integer.parseInt(subscriber));
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
}
