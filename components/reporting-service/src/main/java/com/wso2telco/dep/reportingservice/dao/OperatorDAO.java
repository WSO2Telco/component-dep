/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.reportingservice.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;
import org.wso2.carbon.apimgt.usage.client.exception.APIMgtUsageQueryServiceClientException;

import com.wso2telco.dbutils.DbUtils;
import com.wso2telco.dbutils.util.DataSourceNames;
import com.wso2telco.dep.reportingservice.SPObject;
import com.wso2telco.dep.reportingservice.dao.Approval;
import com.wso2telco.dep.reportingservice.util.ReportingTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class DataAccessObject.
 */
public class OperatorDAO {

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(OperatorDAO.class);

    /**
     * Gets the all operators.
     *
     * @return the all operators
     * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
     * @throws SQLException the SQL exception
     */
    public static List<String> getAllOperators() throws APIMgtUsageQueryServiceClientException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT operatorname FROM "+ ReportingTable.OPERATORS +"";
        List<String> op = new ArrayList<String>();
        try {
            conn =  DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            ps = conn.prepareStatement(sql);
            log.debug("getAllOperators for ID");
            results = ps.executeQuery();
            while (results.next()) {
                String temp = results.getString("operatorname");
                op.add(temp);
            }
        } catch (Exception e) {
        	log.error("Error occured while getting All Operators from the database" + e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        return op;
    }
    
    /**
     * Gets the applications by operator.
     *
     * @param operatorName the operator name
     * @return the applications by operator
     * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
     * @throws SQLException the SQL exception
     */
    public static List<Integer> getApplicationsByOperator(String operatorName) throws APIMgtUsageQueryServiceClientException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT opcoApp.applicationid FROM "+ ReportingTable.OPERATORAPPS +" opcoApp INNER JOIN "+ ReportingTable.OPERATORS +" opco ON opcoApp.operatorid = opco.id WHERE opco.operatorname =? AND opcoApp.isactive = 1";
        List<Integer> applicationIds = new ArrayList<Integer>();
        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            ps = conn.prepareStatement(sql);
            ps.setString(1, operatorName);
            log.debug("getApplicationsByOperator");
            results = ps.executeQuery();
            while (results.next()) {
                int temp = results.getInt("applicationid");
                applicationIds.add(temp);
            }
        } catch (Exception e) {
        	log.error("Error occured while getting application ids from the database" + e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        return applicationIds;
    }
    
    /**
     * Gets the operator names by application.
     *
     * @param applicationId the application id
     * @return the operator names by application
     * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
     * @throws SQLException the SQL exception
     */
    public static List<String> getOperatorNamesByApplication(int applicationId) throws APIMgtUsageQueryServiceClientException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT opco.operatorname FROM "+ ReportingTable.OPERATORAPPS +" opcoApp INNER JOIN "+ ReportingTable.OPERATORS +" opco ON opcoApp.operatorid = opco.id  WHERE opcoApp.applicationid =? AND opcoApp.isactive = 1";
        List<String> operatorNames = new ArrayList<String>();
        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, applicationId);
            log.debug("getOperatorNamesByApplication");
            results = ps.executeQuery();
            while (results.next()) {
                String temp = results.getString("operatorname");
                operatorNames.add(temp);
            }
        } catch (Exception e) {
        	log.error("Error occured while getting operator names from the database" + e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        
        return operatorNames;
    }
    
    /**
     * Fill operator trace.
     *
     * @param applicationId the application id
     * @param operatorId the operator id
     * @param lstapproval the lstapproval
     * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
     * @throws SQLException the SQL exception
     */
    public static void fillOperatorTrace(int applicationId,String operatorId, List<Approval> lstapproval) throws APIMgtUsageQueryServiceClientException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT applicationid as application_id ,2 as type, 'APPO' as name, operatorid, IF(isactive = 0, 'NOT APPROVED','APPROVED') as isactive, '' as api_name, created_date,lastupdated_date "+ 
                "FROM "+ ReportingTable.OPERATORAPPS +" where operatorid like ? and applicationid = ? "+
                "UNION ALL SELECT applicationid as application_id, 4 as type,'SUBO' as name, openp.operatorid as operatorid, IF(enp.isactive = 0, 'NOT APPROVED','APPROVED') as isactive, openp.api as api_name, enp.created_date,enp.lastupdated_date "+
                "FROM "+ ReportingTable.ENDPOINTAPPS +" enp,"+ ReportingTable.OPERATORENDPOINTS +" openp WHERE " +
                "enp.endpointid = openp.id and openp.operatorid like ? and applicationid = ? "+
                "ORDER BY type,api_name, operatorid";
        
        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            ps = conn.prepareStatement(sql);
            ps.setInt(2, applicationId);
            ps.setInt(4, applicationId);
                        
            
            if (operatorId.equalsIgnoreCase("%")) {
                ps.setString(1,"%");
                ps.setString(3,"%");
            } else {
                ps.setInt(1, Integer.parseInt(operatorId));
                ps.setInt(3, Integer.parseInt(operatorId));
            }
                        
            rs = ps.executeQuery();
            while (rs.next()) {
                Approval temp = new Approval(rs.getString("application_id"),rs.getString("type"),rs.getString("name"),rs.getInt("operatorid"),
                    rs.getString("isactive"),"",rs.getString("api_name"),"",rs.getString("created_date"),rs.getString("lastupdated_date"));
                lstapproval.add(temp);
            }
        } catch (Exception e) {
        	log.error("Error occured while getting operator names from the database" + e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }
    }

    /**
     * Gets the approved operators by application.
     *
     * @param applicationId the application id
     * @param operator the operator
     * @return the approved operators by application
     */
    public static String getApprovedOperatorsByApplication(int applicationId, String operator) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
                
        String sql = "SELECT opco.operatorname FROM "+ ReportingTable.OPERATORAPPS +" opcoApp INNER JOIN "+ ReportingTable.OPERATORS +" opco ON opcoApp.operatorid = opco.id WHERE opcoApp.isactive = 1 AND opcoApp.applicationid = ? AND opco.operatorname like ?";

        String approvedOperators = "";

        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, applicationId);
            
            if (operator.equals("__ALL__")) {
                ps.setString(2, "%");
            } else {
                ps.setString(2, operator);
            }

            log.debug("getApprovedOperatorsByApplication");
            rs = ps.executeQuery();
            while (rs.next()) {
                String temp = rs.getString("operatorname");  
                approvedOperators = approvedOperators + ", " +temp ;
            }
        } catch (Exception e) {
        	log.error("Error occured while getting approved operators of application from the database" + e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }
        if(approvedOperators == ""){
            approvedOperators = "NONE";
        }else{
            approvedOperators = approvedOperators.replaceFirst(",", "");
        }
        
        return approvedOperators;
    }


    /**
     * Gets the SP list.
     *
     * @param operator the operator
     * @return the SP list
     */
    public static ArrayList<SPObject> getSPList(String operator) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "Select * from sub_approval_operators WHERE OPERATOR_LIST like '%"+operator+"%'";
        ArrayList<SPObject> spList = new ArrayList<SPObject>();
        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                SPObject spObject = new SPObject();
                spObject.setAppId(rs.getInt("APP_ID"));
                spList.add(spObject);

            }

        } catch (Exception e) {
        	log.error("Error occured while getting approved operators of application from the database" + e);
        }
        return spList;
    }
}
