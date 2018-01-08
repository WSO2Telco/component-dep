/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.workflow.dao;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.*;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.workflow.model.ApplicationApprovalAuditRecord;
import com.wso2telco.workflow.model.SubscriptionApprovalAuditRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The DAO class to handle workflow related tasks.
 */
public class WorkflowStatsDbService {

    public void insertAppApprovalAuditRecord(
            ApplicationApprovalAuditRecord record) throws SQLException, BusinessException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
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
            DbUtils.closeAllConnections(ps, conn, null);
        }
    }

    public void insertSubApprovalAuditRecord(
            SubscriptionApprovalAuditRecord record) throws SQLException, BusinessException {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
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
            DbUtils.closeAllConnections(ps, conn, null);
        }
    }

}
