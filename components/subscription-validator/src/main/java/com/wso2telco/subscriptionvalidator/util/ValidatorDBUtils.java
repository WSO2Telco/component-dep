package com.wso2telco.subscriptionvalidator.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;
import com.wso2telco.subscriptionvalidator.exceptions.ValidatorException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ValidatorDBUtils {

    private static volatile DataSource axiataDatasource = null;
    private static final String AXIATA_DATA_SOURCE = "jdbc/AXIATA_MIFE_DB";
    private static final Log log = LogFactory.getLog(ValidatorDBUtils.class);

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

    public static int getApiId(APIIdentifier apiIdent) throws APIManagementException, ValidatorException {
        Connection conn = null;
        int apiId = -1;
        try {
            conn = getApiMgtDBConnection();
            apiId = ApiMgtDAO.getAPIID(apiIdent, conn);
        } catch (SQLException e) {
            handleException("Error occured while getting API ID of API: " + apiIdent + " from the database", e);
        } finally {
            APIMgtDBUtil.closeAllConnections(null, conn, null);
        }
        return apiId;
    }

    public static String getValidatorClassForSubscription(int applicationId, int apiId) throws ValidatorException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT class FROM validator, subscription_validator " +
                "WHERE subscription_validator.application_id=? AND subscription_validator.api_id=? AND " +
                "validator.id=subscription_validator.validator_id";
        String validatorClass = null;
        try {
            conn = getAxiataDBConnection();
            ps = conn.prepareStatement(sql);
            log.debug("getValidatorClassForSubscription for applicationId---> " + applicationId + " apiId--> " + apiId);
            ps.setInt(1, applicationId);
            ps.setInt(2, apiId);
            results = ps.executeQuery();
            while (results.next()) {
                validatorClass = results.getString("class");
            }
        } catch (SQLException e) {
            handleException("Error occured while getting Validator Class for App: " + applicationId + " API: " +
                    apiId + " from the database", e);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, conn, results);
        }
        return validatorClass;
    }

    private static void handleException(String msg, Throwable t) throws ValidatorException {
        log.error(msg, t);
        throw new ValidatorException(msg, t);
    }

}
