package com.wso2telco.hub.workflow.extensions.dao;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;

public class RestWorkflowDao {

    private static final Log log = LogFactory.getLog(RestWorkflowDao.class);

    /**
     * Gets the operatorName.
     *
     * @return the operatorName
     * @throws Exception the exception
     */
    public String getOperatorName(int applicationId) throws Exception {

        Statement st = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement ps = null;
        String operatorName = null;

        try {
            con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            if (con == null) {
                throw new Exception("Connection not found");
            }

            String query = "SELECT operatorname FROM operators WHERE id = ( SELECT operatorid FROM operatorapps WHERE applicationid = ?);";

            ps = con.prepareStatement(query);
            ps.setInt(1, applicationId);
            rs = ps.executeQuery();

            while (rs.next()) {
                operatorName = rs.getString("operatorname");
            }

        } catch (SQLException e) {
            log.error("Database operation error while retrieving Operator Name ", e);
            DbUtils.handleException("Error in retrieving Operator Name : " + e.getMessage(), e);
        } finally {
            DbUtils.closeAllConnections(ps, con, rs);
        }

        return operatorName;
    }

}
