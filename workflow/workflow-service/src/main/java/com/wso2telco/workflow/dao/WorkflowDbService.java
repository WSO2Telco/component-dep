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
import com.wso2telco.core.dbutils.Operator;
import com.wso2telco.core.dbutils.Operatorendpoint;
import com.wso2telco.core.dbutils.util.DataSourceNames;
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

	/** The log. */
	private static Log log = LogFactory.getLog(WorkflowDbService.class);


    com.wso2telco.core.dbutils.DbUtils dbUtils=new com.wso2telco.core.dbutils.DbUtils();



    /**
     * Application entry.
     *
     * @param applicationid the applicationid
     * @param operators the operators
     * @return the integer
     * @throws Exception the exception
     */

    public Integer applicationEntry(int applicationid, Integer[] operators) throws Exception {

        Connection con = null;
        Statement st = null;
        Integer newid = 0;

        try {
            con = dbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

            if (con == null) {
                throw new Exception("Connection not found");
            }

            st = con.createStatement();
            String sql = null;
            for (Integer d : operators) {
                sql = "INSERT INTO operatorapps (applicationid,operatorid) "
                        + "VALUES (" + applicationid + "," + d + ")";
                st.executeUpdate(sql);
            }

        } catch (Exception e) {
            dbUtils.handleException("Error while inserting in to operatorapps. ", e);
        } finally {
            dbUtils.closeAllConnections(st, con, null);
        }

        return newid;
    }





    /**
     * Insert operator app endpoints.
     *
     * @param appID the app id
     * @param opEndpointIDList the op endpoint id list
     * @throws Exception the exception
     */

    public void insertOperatorAppEndpoints(int appID, int[] opEndpointIDList) throws Exception {

        Connection con = null;
        Statement st = null;

        try {
           con = dbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

            String inputStr = "";

            log.debug("opEndpointIDList.length : " + opEndpointIDList.length);
            for (int i = 0; i < opEndpointIDList.length; i++) {
            	if(opEndpointIDList[i] > 0) {

            		if (inputStr.length() > 0) {
            			inputStr = inputStr + ",";
            		}
            		inputStr = inputStr + "(" + opEndpointIDList[i] + "," + appID + ",0)";
                    log.debug("inputStr : " + inputStr);
            	}
            }

            log.debug("Final inputStr : " + inputStr);

            String sql = "INSERT INTO endpointapps (endpointid, applicationid, isactive) VALUES " + inputStr;
            log.debug("sql : " + sql);

            st = con.createStatement();
            st.executeUpdate(sql);

        } catch (Exception e) {
            dbUtils.handleException("Error while inserting in to endpointapps. ", e);
        } finally {
            dbUtils.closeAllConnections(st, con, null);
        }
    }


    /**
     * Update operator app endpoint status.
     *
     * @param appID the app id
     * @param opEndpointID the op endpoint id
     * @param status the status
     * @throws Exception the exception
     */
    public void updateOperatorAppEndpointStatus(int appID, int opEndpointID, int status) throws Exception {

        Connection con = null;
        Statement st = null;

        try {
            con = dbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

            String sql = "UPDATE endpointapps SET isactive=" + status
                    + " WHERE endpointid=" + opEndpointID
                    + " AND applicationid=" + appID;

            st = con.createStatement();
            st.executeUpdate(sql);

        } catch (Exception e) {
            dbUtils.handleException("Error while updating endpointapps. ", e);
        } finally {
            dbUtils.closeAllConnections(st, con, null);
        }
    }


    /**
     * Update app approval status op.
     *
     * @param axiataId the axiata id
     * @param operatorId the operator id
     * @param status the status
     * @return true, if successful
     * @throws Exception the exception
     */

    public boolean updateAppApprovalStatusOp(int axiataId, int operatorId, int status) throws Exception {

        Connection con = null;
        Statement st = null;

        try {
            con =dbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            if (con == null) {
                throw new Exception("Connection not found.");
            }

            st = con.createStatement();
            String sql = "UPDATE operatorapps "
                    + "SET isactive=" + status + " "
                    + "WHERE applicationid =" + axiataId + " "
                    + "AND operatorid = " + operatorId + "";

            st.executeUpdate(sql);

        } catch (Exception e) {
            dbUtils.handleException("Error while updating operatorapps. ", e);
        } finally {
            dbUtils.closeAllConnections(st, con, null);
        }

        return true;
    }


    /**
     * Insert validator for subscription.
     *
     * @param appID the app id
     * @param apiID the api id
     * @param validatorID the validator id
     * @return true, if successful
     * @throws Exception the exception
     */
    public boolean insertValidatorForSubscription(int appID, int apiID, int validatorID) throws Exception {
        Connection con = null;
        Statement st = null;
        try {
            con = dbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

            String sql = "INSERT INTO subscription_validator (application_id, api_id, validator_id) VALUES "
                    + "(" + appID + "," + apiID + "," + validatorID + ")";
            st = con.createStatement();
            st.executeUpdate(sql);

        } catch (Exception e) {
            dbUtils.handleException("Error while inserting in to subscription_validator. ", e);
        } finally {
            dbUtils.closeAllConnections(st, con, null);
        }
        return true;
    }


    public  String getSubApprovalOperators(String apiName,
                                           String apiVersion, String apiProvider, int appId)
            throws Exception {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String operators = "";

        try {
            conn =dbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            String query = "SELECT OPERATOR_LIST FROM sub_approval_operators "
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

        } catch (SQLException e) {
            dbUtils.handleException(
                    "Error in retrieving operator list : " + e.getMessage(), e);
        } finally {
            dbUtils.closeAllConnections(ps, conn, rs);

        }

        log.debug("operators : " + operators);
        return operators;
    }

    public Map<String, String> getWorkflowAPIKeyMappings() throws Exception {

        log.debug("[START] getWorkflowAPIKeyMappings()");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String apiName = null;
        String apiKey = null;
        Map<String , String> apiKeyMapping = new HashMap<String, String>();

        try {
            conn = dbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            String query = "SELECT API_NAME, API_KEY FROM workflow_api_key_mappings";
            log.debug("SQL : " + query);

            ps = conn.prepareStatement(query);

            rs = ps.executeQuery();
            while(rs.next()) {
                apiName = rs.getString("API_NAME");
                apiKey = rs.getString("API_KEY");
                apiKeyMapping.put(apiName, apiKey);

                log.debug("apiName : " + apiName + " | apiKey : " + apiKey);
            }

        } catch (SQLException e) {
            dbUtils.handleException(
                    "Error in retrieving workflow related API Key mappings : " + e.getMessage(), e);
        } finally {
            dbUtils.closeAllConnections(ps, conn, rs);
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
    public List<Operator> getOperators() throws Exception {

        Connection con = dbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
        Statement st = null;
        ResultSet rs = null;
        List<Operator> operators = new ArrayList<Operator>();

        try {
            if (con == null) {
                throw new Exception("Connection not found");
            }

            st = con.createStatement();
            String sql = "SELECT ID, operatorname "
                    + "FROM operators";

            rs = st.executeQuery(sql);

            while (rs.next()) {
                Operator operator = new Operator();
                operator.setOperatorid(rs.getInt("ID"));
                operator.setOperatorname(rs.getString("operatorname"));
                operators.add(operator);
            }

        } catch (Exception e) {
            dbUtils.handleException("Error while retrieving operators. ", e);
        } finally {
            dbUtils.closeAllConnections(st, con, rs);
        }
        return operators;
    }


    /**
     * Gets the operator endpoints.
     *
     * @return the operator endpoints
     * @throws Exception the exception
     */
    public List<Operatorendpoint> getOperatorEndpoints() throws Exception {

        List<Operatorendpoint> operatorEndpoints = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = dbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            if (con == null) {
                throw new Exception("Connection not found.");
            }

            st = con.createStatement();
            String sql = "SELECT ID,operatorid,api FROM operatorendpoints";

            rs = st.executeQuery(sql);

            while (rs.next()) {
                Operatorendpoint endpoint = new Operatorendpoint(rs.getInt("operatorid"), null, rs.getString("api"), null);
                endpoint.setId(rs.getInt("ID"));
                operatorEndpoints.add(endpoint);
            }

        } catch (Exception e) {
            dbUtils.handleException("Error while selecting from operatorendpoints. ", e);
        } finally {
            dbUtils.closeAllConnections(st, con, rs);
        }

        return operatorEndpoints;
    }

}
