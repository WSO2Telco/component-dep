package com.wso2telco.workflow.dao;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.workflow.model.ApplicationApprovalAuditRecord;
import com.wso2telco.workflow.model.SubscriptionApprovalAuditRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The DAO class to handle workflow related tasks.
 */
public class WorkflowStatsDbService {

    private static final Log log = LogFactory.getLog(WorkflowStatsDbService.class);
    DbUtils dbUtils = new DbUtils();

    public void insertAppApprovalAuditRecord(
            ApplicationApprovalAuditRecord record) throws SQLException, BusinessException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            StringBuilder query = new StringBuilder();
            query.append("INSERT INTO app_approval_audit (APP_NAME, APP_CREATOR, APP_STATUS, ");
            query.append("APP_APPROVAL_TYPE, COMPLETED_BY_ROLE, COMPLETED_BY_USER) ");
            query.append("VALUES (?, ?, ?, ?, ?, ?)");
            ps = conn.prepareStatement(query.toString());
            ps.setString(1, record.getAppName());
            ps.setString(2, record.getAppCreator());
            ps.setString(3, record.getAppStatus());
            ps.setString(4, record.getAppApprovalType());
            ps.setString(5, record.getCompletedByRole());
            ps.setString(6, record.getCompletedByUser());
            ps.execute();

        } catch (SQLException e) {
            throw new SQLException();
        } catch (Exception e) {
            throw new BusinessException(GenaralError.UNDEFINED);
        } finally {
            dbUtils.closeAllConnections(ps, conn, null);
        }
    }

    public void insertSubApprovalAuditRecord(
            SubscriptionApprovalAuditRecord record) throws SQLException, BusinessException {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
            StringBuilder query = new StringBuilder();
            query.append("INSERT INTO sub_approval_audit (API_PROVIDER, API_NAME, API_VERSION, APP_ID, ");
            query.append("SUB_STATUS, SUB_APPROVAL_TYPE, COMPLETED_BY_ROLE, COMPLETED_BY_USER) ");
            query.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps = conn.prepareStatement(query.toString());
            ps.setString(1, record.getApiProvider());
            ps.setString(2, record.getApiName());
            ps.setString(3, record.getApiVersion());
            ps.setInt(4, record.getAppId());
            ps.setString(5, record.getSubStatus());
            ps.setString(6, record.getSubApprovalType());
            ps.setString(7, record.getCompletedByRole());
            ps.setString(8, record.getCompletedByUser());
            ps.execute();

        } catch (SQLException e) {
            throw new SQLException();
        } catch (Exception e) {
            throw new BusinessException(GenaralError.UNDEFINED);
        } finally {
            dbUtils.closeAllConnections(ps, conn, null);
        }
    }

}
