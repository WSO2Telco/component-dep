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
package com.wso2telco.workflow.dao;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.operatorservice.model.Operator;
import com.wso2telco.dep.operatorservice.model.OperatorEndPointDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class WorkflowDbService.
 */

public class WorkflowDbService {

    /**
     * The log.
     */
    private static Log log = LogFactory.getLog(WorkflowDbService.class);


    /**
     * Application entry.
     *
     * @param applicationid the applicationid
     * @param operators     the operators
     * @return the integer
     * @throws Exception the exception
     */

    public void applicationEntry(int applicationid, Integer[] operators) throws SQLException, BusinessException {

        Connection con = null;
        Statement st = null;

        try {
            con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

            if (con == null) {
                throw new Exception("Connection not found");
            }
            con.setAutoCommit(false);
            st = con.createStatement();
            for (Integer d : operators) {
                if(!operatorAppsIsExist(applicationid,d)){
                StringBuilder query = new StringBuilder();
                query.append("INSERT INTO operatorapps (applicationid,operatorid) ");
                query.append("VALUES (" + applicationid + "," + d + ")");
                st.addBatch(query.toString());}
            }
            st.executeBatch();
            con.commit();

        } catch (SQLException e) {
            throw new SQLException();
        } catch (Exception e) {
            throw new BusinessException(GenaralError.UNDEFINED);
        } finally {
            DbUtils.closeAllConnections(st, con, null);
        }

    }


    /**
     * Insert operator app endpoints.
     *
     * @param appID            the app id
     * @param opEndpointIDList the op endpoint id list
     * @throws Exception the exception
     */

    public void insertOperatorAppEndpoints(int appID, int[] opEndpointIDList) throws SQLException, BusinessException {

        Connection con = null;
        Statement st = null;

        try {
            con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            log.debug("opEndpointIDList.length : " + opEndpointIDList.length);
            con.setAutoCommit(false);
            st = con.createStatement();
            for (int i = 0; i < opEndpointIDList.length; i++) {
                if (opEndpointIDList[i] > 0  && !endpointAppsIsExist(opEndpointIDList[i],appID)) {
                    StringBuilder query = new StringBuilder();
                    query.append("INSERT INTO endpointapps (endpointid, applicationid, isactive) VALUES ");
                    query.append("(" + opEndpointIDList[i] + "," + appID + ",0)");
                    st.addBatch(query.toString());
               }
            }
            st.executeBatch();
            con.commit();

        } catch (SQLException e) {
            throw new SQLException();
        } catch (Exception e) {
            throw new BusinessException(GenaralError.UNDEFINED);
        } finally {
            DbUtils.closeAllConnections(st, con, null);
        }
    }


    /**
     * Update operator app endpoint status.
     *
     * @param appID        the app id
     * @param opEndpointID the op endpoint id
     * @param status       the status
     * @throws Exception the exception
     */
    public void updateOperatorAppEndpointStatus(int appID, int opEndpointID, int status) throws SQLException, BusinessException {

        Connection con = null;
        Statement st = null;

        try {
            con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            StringBuilder query = new StringBuilder();
            query.append("UPDATE endpointapps SET isactive=" + status);
            query.append(" WHERE endpointid=" + opEndpointID);
            query.append(" AND applicationid=" + appID);

            st = con.createStatement();
            st.executeUpdate(query.toString());

        } catch (SQLException e) {
            throw new SQLException();
        } catch (Exception e) {
            throw new BusinessException(GenaralError.UNDEFINED);
        } finally {
            DbUtils.closeAllConnections(st, con, null);
        }
    }


    /**
     * Update app approval status op.
     *
     * @param applicationid   the application id
     * @param operatorId the operator id
     * @param status     the status
     * @return true, if successful
     * @throws Exception the exception
     */

    public boolean updateAppApprovalStatusOp(int applicationid, int operatorId, int status) throws SQLException, BusinessException {

        Connection con = null;
        Statement st = null;

        try {
            con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            if (con == null) {
                throw new Exception("Connection not found.");
            }

            st = con.createStatement();
            StringBuilder query = new StringBuilder();
            query.append("UPDATE operatorapps ");
            query.append("SET isactive=" + status + " ");
            query.append("WHERE applicationid =" + applicationid + " ");
            query.append("AND operatorid = " + operatorId + "");

            st.executeUpdate(query.toString());

        } catch (SQLException e) {
            throw new SQLException();
        } catch (Exception e) {
            throw new BusinessException(GenaralError.UNDEFINED);
        } finally {
            DbUtils.closeAllConnections(st, con, null);
        }

        return true;
    }


    /**
     * Insert validator for subscription.
     *
     * @param appID       the app id
     * @param apiID       the api id
     * @param validatorID the validator id
     * @return true, if successful
     * @throws Exception the exception
     */
    public boolean insertValidatorForSubscription(int appID, int apiID, int validatorID) throws SQLException, BusinessException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            if (!subscriptionIsExist(appID, apiID)) {
                con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
                StringBuilder query = new StringBuilder();
                query.append("INSERT INTO subscription_validator (application_id, api_id, validator_id) VALUES ");
                query.append("(?,?,?)");
                ps = con.prepareStatement(query.toString());
                ps.setInt(1, appID);
                ps.setInt(2, apiID);
                ps.setInt(3, validatorID);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException();
        } catch (Exception e) {
            throw new BusinessException(GenaralError.UNDEFINED);
        } finally {
            DbUtils.closeAllConnections(ps, con, null);
        }
        return true;
    }


    public String getSubApprovalOperators(String apiName,
                                          String apiVersion, String apiProvider, int appId)
            throws Exception, BusinessException {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String operators = "";

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            StringBuilder query = new StringBuilder();
            query.append("SELECT OPERATOR_LIST FROM sub_approval_operators ");
            query.append("WHERE API_NAME=? AND API_VERSION=? AND API_PROVIDER=? AND APP_ID=? ");
            ps = conn.prepareStatement(query.toString());
            ps.setString(1, apiName);
            ps.setString(2, apiVersion);
            ps.setString(3, apiProvider);
            ps.setInt(4, appId);

            rs = ps.executeQuery();
            if (rs.next()) {
                operators = rs.getString("OPERATOR_LIST");
            }

        } catch (SQLException e) {
            throw new SQLException();
        } catch (Exception e) {
            throw new BusinessException(GenaralError.UNDEFINED);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);

        }

        log.debug("operators : " + operators);
        
        return operators;
    }

    public Map<String, String> getWorkflowAPIKeyMappings() throws SQLException, BusinessException {

        log.debug("[START] getWorkflowAPIKeyMappings()");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String apiName;
        String apiKey;
        Map<String, String> apiKeyMapping = new HashMap<String, String>();

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            StringBuilder query = new StringBuilder();
            query.append("SELECT API_NAME, API_KEY FROM workflow_api_key_mappings");
            log.debug("SQL : " + query);

            ps = conn.prepareStatement(query.toString());

            rs = ps.executeQuery();
            while (rs.next()) {
                apiName = rs.getString("API_NAME");
                apiKey = rs.getString("API_KEY");
                apiKeyMapping.put(apiName, apiKey);

                log.debug("apiName : " + apiName + " | apiKey : " + apiKey);
            }
        } catch (SQLException e) {
            throw new SQLException();
        } catch (Exception e) {
            throw new BusinessException(GenaralError.UNDEFINED);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }

        log.debug("[END] getWorkflowAPIKeyMappings()");
        return apiKeyMapping;
    }

    /**
     * Gets the operators.
     *
     * @return the operators
     * @throws Exception the exception
     */
    public List<Operator> getOperators() throws SQLException, BusinessException {

        Statement st = null;
        ResultSet rs = null;
        List<Operator> operators = new ArrayList<Operator>();
        Connection con = null;

        try {
            con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            if (con == null) {
                throw new Exception("Connection not found");
            }

            st = con.createStatement();
            StringBuilder query = new StringBuilder();
            query.append("SELECT ID, operatorname ");
            query.append("FROM operators");

            rs = st.executeQuery(query.toString());

            while (rs.next()) {
                Operator operator = new Operator();
                operator.setOperatorId(rs.getInt("ID"));
                operator.setOperatorName(rs.getString("operatorname"));
                operators.add(operator);
            }

        } catch (SQLException e) {
            throw new SQLException();
        } catch (Exception e) {
            throw new BusinessException(GenaralError.UNDEFINED);
        } finally {
            DbUtils.closeAllConnections(st, con, rs);
        }
        return operators;
    }


    /**
     * Gets the operator endpoints.
     *
     * @return the operator endpoints
     * @throws Exception the exception
     */
    public List<OperatorEndPointDTO> getOperatorEndpoints() throws SQLException, BusinessException {

        List<OperatorEndPointDTO> operatorEndpoints = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            if (con == null) {
                throw new Exception("Connection not found.");
            }

            st = con.createStatement();
            StringBuilder query = new StringBuilder();
            query.append("SELECT ID,operatorid,api FROM operatorendpoints");

            rs = st.executeQuery(query.toString());

            while (rs.next()) {
                OperatorEndPointDTO endpoint = new OperatorEndPointDTO(rs.getInt("operatorid"), null, rs.getString("api"), null);
                endpoint.setId(rs.getInt("ID"));
                operatorEndpoints.add(endpoint);
            }

        } catch (SQLException e) {
            throw new SQLException();
        } catch (Exception e) {
            throw new BusinessException(GenaralError.UNDEFINED);
        } finally {
            DbUtils.closeAllConnections(st, con, rs);
        }

        return operatorEndpoints;
    }


    /**
     * Gets the operator if by name.
     *
     * @return the operatorId
     * @throws Exception the exception
     */
    public int getOperatorIdByName(String operatorName) throws SQLException, BusinessException {

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int operatorId = 0;
        try {
            con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

            if (con == null) {
                throw new Exception("Connection not found");
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
            throw new BusinessException(GenaralError.UNDEFINED);
        } finally {
            DbUtils.closeAllConnections(st, con, rs);
        }
        return operatorId;
    }

    public Boolean endpointAppsIsExist(int endpointId,int applicationId)
            throws Exception, BusinessException {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean isExist =false;

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            StringBuilder query = new StringBuilder();
            query.append("SELECT id FROM endpointapps ");
            query.append("WHERE endpointid=? and applicationid=?");
            ps = conn.prepareStatement(query.toString());
            ps.setInt(1, endpointId);
            ps.setInt(2, applicationId);

            rs = ps.executeQuery();
            if (rs.next()) {
                isExist = true;
            }

        } catch (SQLException e) {
            log.error(e);
            throw new SQLException(e);
        } catch (Exception e) {
            log.error(e);
            throw new BusinessException(GenaralError.UNDEFINED);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);

        }

        log.debug("isExist : " + isExist);
        return isExist;
    }


    public Boolean subscriptionIsExist(int applicationId,int apiId)
            throws Exception, BusinessException {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean isExist =false;

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            StringBuilder query = new StringBuilder();
            query.append("SELECT application_id FROM subscription_validator ");
            query.append("WHERE application_id=? and api_id=?");
            ps = conn.prepareStatement(query.toString());
            ps.setInt(1, applicationId);
            ps.setInt(2, apiId);

            rs = ps.executeQuery();
            if (rs.next()) {
                isExist = true;
            }

        } catch (SQLException e) {
            log.error(e);
            throw new SQLException(e);
        } catch (Exception e) {
            log.error(e);
            throw new BusinessException(GenaralError.UNDEFINED);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);

        }

        return isExist;
    }


    public Boolean operatorAppsIsExist(int applicationId,int operatorId)
            throws Exception, BusinessException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean isExist =false;

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            StringBuilder query = new StringBuilder();
            query.append("SELECT applicationid FROM operatorapps ");
            query.append("WHERE applicationid=? and operatorid=?");
            ps = conn.prepareStatement(query.toString());
            ps.setInt(1, applicationId);
            ps.setInt(2, operatorId);

            rs = ps.executeQuery();
            if (rs.next()) {
                isExist = true;
            }

        } catch (SQLException e) {
            log.error(e);
            throw new SQLException(e);
        } catch (Exception e) {
            log.error(e);
            throw new BusinessException(GenaralError.UNDEFINED);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);

        }

        return isExist;

    }

    public List<String> getApplicationApprovedOrPendingOperators(int applicationID)
            throws Exception, BusinessException {
        List<String> operators = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            if (con == null) {
                throw new Exception("Connection not found.");
            }

            String query = "SELECT operatorname FROM operatorapps OA LEFT JOIN operators O ON OA.operatorid=O.ID WHERE OA.isactive IN (0,1) AND OA.applicationid=?";

            ps = con.prepareStatement(query);
            ps.setInt(1, applicationID);
            rs = ps.executeQuery();

            while (rs.next()) {
                operators.add(rs.getString("operatorname"));
            }

        } catch (SQLException e) {
            throw new SQLException();
        } catch (Exception e) {
            throw new BusinessException(GenaralError.UNDEFINED);
        } finally {
            DbUtils.closeAllConnections(ps, con, rs);
        }

        return operators;
    }
}
