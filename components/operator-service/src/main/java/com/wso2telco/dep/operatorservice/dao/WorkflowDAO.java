/**
 * Copyright (c) 2015, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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
package com.wso2telco.dep.operatorservice.dao;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.operatorservice.model.WorkflowReferenceDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class WorkflowDAO {

    /**
     * The Constant log.
     */
    private final Log log = LogFactory.getLog(OperatorDAO.class);

    public void insertWorkflowRef(String workflowID,String apiName, String apiVersion,int appId,String serviceEndpoint) throws Exception {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            this.updateWorkflowRef(apiName,apiVersion,appId);
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            StringBuilder query = new StringBuilder();
            query.append("INSERT INTO ");
            query.append("workflow_reference");
            query.append(" (workflow_ref_id, application_id, api_name, api_version,service_endpoint,status) ");
            query.append("VALUES (?, ?, ?, ?,?,?)");

            ps = conn.prepareStatement(query.toString());
            ps.setString(1, workflowID);
            ps.setInt(2, appId);
            ps.setString(3, apiName);
            ps.setString(4, apiVersion);
            ps.setString(5, serviceEndpoint);
            ps.setInt(6, 1);

            log.debug("sql query in persistOperators : " + ps);
            ps.execute();

        } catch (SQLException e) {
            log.error("database operation error in retrieveOperatorList : ", e);
            throw e;
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }

    }


    public WorkflowReferenceDTO findWorkflow(String apiName,String appId,String apiVersion) throws Exception {

        WorkflowReferenceDTO workflowReferenceDTO=new WorkflowReferenceDTO();
        String sql = "SELECT * from workflow_reference where api_name = ? and application_id = ? and status=? and api_version=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn =DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            ps = conn.prepareStatement(sql);
            ps.setString(1, apiName);
            ps.setString(2, appId);
            ps.setInt(3, 1);
            ps.setString(4, apiVersion);
            rs = ps.executeQuery();
            while (rs.next()) {
                 workflowReferenceDTO.setWorkflowRef(rs.getString("workflow_ref_id"));
                 workflowReferenceDTO.setWorkflowServiceURL(rs.getString("service_endpoint"));
            }
        } catch (SQLException e) {
          log.error("SQLException "+e);
            throw e;
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }
      return workflowReferenceDTO;
    }

    public void updateWorkflowRef(String apiName, String apiVersion, int appId)
            throws SQLException, Exception {

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            if (con == null) {
                throw new SQLException("Connection not found");
            }
            StringBuilder queryString = new StringBuilder("UPDATE ");
            queryString.append("workflow_reference");
            queryString.append(" SET status = ?");
            queryString.append(" WHERE api_version = ?");
            queryString.append(" and application_id = ?");
            queryString.append(" and api_name = ?");

            ps = con.prepareStatement(queryString.toString());

            ps.setInt(1, 0);
            ps.setString(2, apiVersion);
            ps.setInt(3, appId);
            ps.setString(4, apiName);
            log.debug("sql query in updateOperatorToken : " + ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("database operation error in updateWorkflowRef : ", e);
            throw e;
        } catch (Exception e) {
            log.error("error in updateWorkflowRef : ", e);
            throw e;
        } finally {
            DbUtils.closeAllConnections(ps, con, null);
        }
    }

    public Boolean operatorAppsIsActive(int applicationId,String operatorName)
            throws Exception, Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean isExist =false;
        int isActive=0;
        int operatorId=getOperatorIdByName(operatorName);

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            StringBuilder query = new StringBuilder();
            query.append("SELECT applicationid,isactive FROM operatorapps ");
            query.append("WHERE applicationid=? and operatorid=?");
            ps = conn.prepareStatement(query.toString());
            ps.setInt(1, applicationId);
            ps.setInt(2, operatorId);

            rs = ps.executeQuery();
            if (rs.next()) {
                isActive = rs.getInt("isactive");
                if(isActive==1){
                    isExist = true;}
            }

        } catch (SQLException e) {
            log.error(e);
            throw new SQLException(e);
        } catch (Exception e) {
            log.error(e);
            throw e;
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);

        }

        return isExist;

    }

    public List<String> getOparatorApprovedApp(String []  applicationDIds)          throws  Exception {
       
    	return getOparatorApprovedApp(applicationDIds,null);
    }
    
    public List<String> getOparatorApprovedApp(String []  applicationDIds,final String oparorName)          throws  Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> returnApp = new ArrayList<String>() ;

        try {
            String cvsAppIds =StringUtils.join(applicationDIds, ',');
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			
            StringBuilder query = new StringBuilder();
            query.append("SELECT applicationid ");
            query.append(" FROM operatorapps opapp ");
            query.append("WHERE 1=1 ");
          
            if(oparorName!=null && oparorName.trim().length()>0) {
            	query.append(" AND EXISTS (SELECT 1 FROM");
            	query.append(" operators op ");
            	query.append(" WHERE op.ID=opapp.operatorid ");
            	query.append( " AND op.operatorname =? )");
            }
            query.append(" AND opapp.applicationid in(");
            query.append(  cvsAppIds).append(" )");
            query.append(" AND isactive=1");
            ps = conn.prepareStatement(query.toString());
            
            if(oparorName!=null && oparorName.trim().length()>0) {
            	ps.setString(1, oparorName.trim().toUpperCase());
            }
            
            rs = ps.executeQuery();
           
            while (rs.next()) {
            	returnApp.add(rs.getString("applicationid"));
				
			}

        } catch (Exception e) {
        	  log.error("getAppStatus",e);
        	  throw e;
		}
        finally {
            DbUtils.closeAllConnections(ps, conn, rs);

        }

        return returnApp;

    }
    
    
    /**
     * Gets the operator if by name.
     *
     * @return the operatorId
     * @throws Exception the exception
     */
    public int getOperatorIdByName(String operatorName) throws SQLException, Exception {

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int operatorId = 0;
        try {
            con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

            if (con == null) {
                throw new SQLException("Connection not found");
            }

            st = con.createStatement();
            StringBuilder query = new StringBuilder();
            query.append("SELECT ID, operatorname ");
            query.append("FROM operators WHERE operatorname = '" + operatorName + " ' ");
            rs = st.executeQuery(query.toString());
            while (rs.next()) {
                operatorId = rs.getInt("ID");
            }

        } catch (SQLException e) {
            throw new SQLException();
        } catch (Exception e) {
            throw  e;
        } finally {
            DbUtils.closeAllConnections(st, con, rs);
        }
        return operatorId;
    }

    public List<WorkflowReferenceDTO> findWorkflowByAppId(String appId) throws Exception {

        WorkflowReferenceDTO workflowReferenceDTO=new WorkflowReferenceDTO();
        String sql = "SELECT * from workflow_reference where application_id = ? ";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<WorkflowReferenceDTO> workflowReferenceDTOs=new ArrayList<WorkflowReferenceDTO>();

        try {
            conn =DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            ps = conn.prepareStatement(sql);
            ps.setString(1, appId);
            rs = ps.executeQuery();

            while (rs.next()) {
                workflowReferenceDTO.setWorkflowRef(rs.getString("workflow_ref_id"));
                workflowReferenceDTO.setWorkflowServiceURL(rs.getString("service_endpoint"));
                workflowReferenceDTOs.add(workflowReferenceDTO);
            }

        } catch (SQLException e) {
            log.error("SQLException "+e);
            throw e;
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }
        return workflowReferenceDTOs;
    }

}
