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
package com.wso2telco.dep.workflow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.workflow.model.ApplicationApprovalAuditRecord;
import com.wso2telco.dep.workflow.model.SubscriptionApprovalAuditRecord;


public class WorkflowDAO {

	private static final Log log = LogFactory.getLog(WorkflowDAO.class);
	
	public static void insertAppApprovalAuditRecord(
			ApplicationApprovalAuditRecord record)
			throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			String query = "INSERT INTO APP_APPROVAL_AUDIT (APP_NAME, APP_CREATOR, APP_STATUS, "
					+ "APP_APPROVAL_TYPE, COMPLETED_BY_ROLE, COMPLETED_BY_USER) "
					+ "VALUES (?, ?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(query);
			ps.setString(1, record.getAppName());
			ps.setString(2, record.getAppCreator());
			ps.setString(3, record.getAppStatus());
			ps.setString(4, record.getAppApprovalType());
			ps.setString(5, record.getCompletedByRole());
			ps.setString(6, record.getCompletedByUser());
			ps.execute();

		} catch (Exception e) {
			log.error("",e);
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, null);
		}
	}

	public static void insertSubApprovalAuditRecord(
			SubscriptionApprovalAuditRecord record)
			throws Exception {

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			String query = "INSERT INTO SUB_APPROVAL_AUDIT (API_PROVIDER, API_NAME, API_VERSION, APP_ID, "
					+ "SUB_STATUS, SUB_APPROVAL_TYPE, COMPLETED_BY_ROLE, COMPLETED_BY_USER) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(query);
			ps.setString(1, record.getApiProvider());
			ps.setString(2, record.getApiName());
			ps.setString(3, record.getApiVersion());
			ps.setInt(4, record.getAppId());
			ps.setString(5, record.getSubStatus());
			ps.setString(6, record.getSubApprovalType());
			ps.setString(7, record.getCompletedByRole());
			ps.setString(8, record.getCompletedByUser());
			ps.execute();

		} catch (Exception e) {
			log.error("",e);
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, null);
		}
	}

	public static String getSubApprovalOperators(String apiName,
			String apiVersion, String apiProvider, int appId)
			throws Exception {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String operators = "";

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			String query = "SELECT OPERATOR_LIST FROM SUB_APPROVAL_OPERATORS "
					+ "WHERE API_NAME=? AND API_VERSION=? AND API_PROVIDER=? AND APP_ID=? ";
			ps = conn.prepareStatement(query);
			ps.setString(1, apiName);
			ps.setString(2, apiVersion);
			ps.setString(3, apiProvider);
			ps.setInt(4, appId);

			rs = ps.executeQuery();
			if(rs.next()){
				operators = rs.getString("OPERATOR_LIST");
			}

		} catch (Exception e) {
			log.error("",e);
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);
		}

		return operators;	
	}

}
