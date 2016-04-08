
package com.wso2telco.custom.hostobjects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;
import org.wso2.carbon.apimgt.usage.client.exception.APIMgtUsageQueryServiceClientException;

import com.wso2telco.custom.dao.Approval;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AxiataDataAccessObject {

    private static volatile DataSource axiataDatasource = null;
    private static final String AXIATA_DATA_SOURCE = "jdbc/AXIATA_MIFE_DB";
    private static final Log log = LogFactory.getLog(AxiataDataAccessObject.class);

    public static void initializeDatasources() throws ValidatorException {
        if (axiataDatasource != null) {
            return;
        }

        try {
            Context ctx = new InitialContext();
            axiataDatasource = (DataSource) ctx.lookup(AXIATA_DATA_SOURCE);
        } catch (NamingException e) {
            handleException("Error while looking up the data source: " + AXIATA_DATA_SOURCE, e);
        }
    }

    public static Connection getApiMgtDBConnection() throws SQLException {

        return APIMgtDBUtil.getConnection();
    }

    public static Connection getAxiataDBConnection() throws SQLException, ValidatorException {
        initializeDatasources();

        if (axiataDatasource != null) {
            return axiataDatasource.getConnection();
        }
        throw new SQLException("Axiata Datasource not initialized properly");
    }

    private static void handleException(String msg, Throwable t) throws ValidatorException {
        log.error(msg, t);
        throw new ValidatorException(msg, t);
    }

    public static List<String> getAllOperators() throws APIMgtUsageQueryServiceClientException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT operatorname FROM operators";
        List<String> op = new ArrayList<String>();
        try {
            conn = getAxiataDBConnection();
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
            APIMgtDBUtil.closeAllConnections(ps, conn, results);
        }
        return op;
    }
    
    public static List<Integer> getApplicationsByOperator(String operatorName) throws APIMgtUsageQueryServiceClientException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT opcoApp.applicationid FROM operatorapps opcoApp INNER JOIN operators opco ON opcoApp.operatorid = opco.id WHERE opco.operatorname =? AND opcoApp.isactive = 1";
        List<Integer> applicationIds = new ArrayList<Integer>();
        try {
            conn = getAxiataDBConnection();
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
            APIMgtDBUtil.closeAllConnections(ps, conn, results);
        }
        return applicationIds;
    }
    
    public static List<String> getOperatorNamesByApplication(int applicationId) throws APIMgtUsageQueryServiceClientException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT opco.operatorname FROM operatorapps opcoApp INNER JOIN operators opco ON opcoApp.operatorid = opco.id  WHERE opcoApp.applicationid =? AND opcoApp.isactive = 1";
        List<String> operatorNames = new ArrayList<String>();
        try {
            conn = getAxiataDBConnection();
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
            APIMgtDBUtil.closeAllConnections(ps, conn, results);
        }
        
        return operatorNames;
    }
    
    public static void fillOperatorTrace(int applicationId,String operatorId, List<Approval> lstapproval) throws APIMgtUsageQueryServiceClientException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT applicationid as application_id ,2 as type, 'APPO' as name, operatorid, IF(isactive = 0, 'NOT APPROVED','APPROVED') as isactive, '' as api_name, created_date,lastupdated_date "+ 
                "FROM operatorapps where operatorid like ? and applicationid = ? "+
                "UNION ALL SELECT applicationid as application_id, 4 as type,'SUBO' as name, openp.operatorid as operatorid, IF(enp.isactive = 0, 'NOT APPROVED','APPROVED') as isactive, openp.api as api_name, enp.created_date,enp.lastupdated_date "+
                "FROM endpointapps enp,operatorendpoints openp WHERE " +
                "enp.endpointid = openp.id and openp.operatorid like ? and applicationid = ? "+
                "ORDER BY type,api_name, operatorid";
        
        List<String> operatorNames = new ArrayList<String>();
        try {
            conn = getAxiataDBConnection();
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
            APIMgtDBUtil.closeAllConnections(ps, conn, rs);
        }
    }

    public static String getApprovedOperatorsByApplication(int applicationId, String operator) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
                
        String sql = "SELECT opco.operatorname FROM operatorapps opcoApp INNER JOIN operators opco ON opcoApp.operatorid = opco.id WHERE opcoApp.isactive = 1 AND opcoApp.applicationid = ? AND opco.operatorname like ?";

        String approvedOperators = "";

        try {
            conn = getAxiataDBConnection();
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
            APIMgtDBUtil.closeAllConnections(ps, conn, rs);
        }
        if(approvedOperators == ""){
            approvedOperators = "NONE";
        }else{
            approvedOperators = approvedOperators.replaceFirst(",", "");
        }
        
        return approvedOperators;
    }


    public static ArrayList<SPObject> getSPList(String operator) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "Select * from sub_approval_operators WHERE OPERATOR_LIST like '%"+operator+"%'";
        ArrayList<SPObject> spList = new ArrayList<SPObject>();
        try {
            conn = getAxiataDBConnection();
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
