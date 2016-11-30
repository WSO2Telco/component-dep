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
package com.wso2telco.dep.subscriptionvalidator.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.subscriptionvalidator.exceptions.ValidatorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


// TODO: Auto-generated Javadoc
/**
 * The Class ValidatorDBUtils.
 */
public class ValidatorDBUtils {
    
    /** The Constant log. */
    private static final Log log = LogFactory.getLog(ValidatorDBUtils.class);

    /**
     * Gets the api mgt db connection.
     *
     * @return the api mgt db connection
     * @throws SQLException the SQL exception
     */
    public static Connection getApiMgtDBConnection() throws SQLException {

        return APIMgtDBUtil.getConnection();
    }

    /**
     * Gets the api id.
     *
     * @param apiIdent the api ident
     * @return the api id
     * @throws APIManagementException the API management exception
     * @throws ValidatorException the validator exception
     */
    public static int getApiId(APIIdentifier apiIdent) throws APIManagementException, ValidatorException {
        Connection conn = null;
        int apiId = -1;
        try {
            conn = getApiMgtDBConnection();
            apiId = ApiMgtDAO.getInstance().getAPIID(apiIdent, conn);
        } catch (SQLException e) {
            handleException("Error occured while getting API ID of API: " + apiIdent + " from the database", e);
        } finally {
            APIMgtDBUtil.closeAllConnections(null, conn, null);
        }
        return apiId;
    }

    /**
     * Gets the validator class for subscription.
     *
     * @param applicationId the application id
     * @param apiId the api id
     * @return the validator class for subscription
     * @throws ValidatorException the validator exception
     */
    public static String getValidatorClassForSubscription(int applicationId, int apiId) throws ValidatorException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT class FROM validator, subscription_validator " +
                "WHERE subscription_validator.application_id=? AND subscription_validator.api_id=? AND " +
                "validator.id=subscription_validator.validator_id";
        String validatorClass = null;
        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            ps = conn.prepareStatement(sql);
            log.debug("getValidatorClassForSubscription for applicationId---> " + applicationId + " apiId--> " + apiId);
            ps.setInt(1, applicationId);
            ps.setInt(2, apiId);
            results = ps.executeQuery();
            while (results.next()) {
                validatorClass = results.getString("class");
            }
        } catch (Exception e) {
            handleException("Error occured while getting Validator Class for App: " + applicationId + " API: " +
                    apiId + " from the database", e);
        } finally {
            closeAllConnections(ps, conn, results);
        }
        return validatorClass;
    }

    /**
     * Handle exception.
     *
     * @param msg the msg
     * @param t the t
     * @throws ValidatorException the validator exception
     */
    private static void handleException(String msg, Throwable t) throws ValidatorException {
        log.error(msg, t);
        throw new ValidatorException(msg, t);
    }

    public static void closeAllConnections(PreparedStatement preparedStatement, Connection connection, ResultSet resultSet) {
        closeConnection(connection);
        closeResultSet(resultSet);
        closeStatement(preparedStatement);
    }

    private static void closeConnection(Connection dbConnection) {
        if(dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException var2) {
                log.warn("Database error. Could not close database connection. Continuing with others. - " + var2.getMessage(), var2);
            }
        }

    }

    private static void closeResultSet(ResultSet resultSet) {
        if(resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException var2) {
                log.warn("Database error. Could not close ResultSet  - " + var2.getMessage(), var2);
            }
        }

    }

    public static void closeStatement(PreparedStatement preparedStatement) {
        if(preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException var2) {
                log.warn("Database error. Could not close PreparedStatement. Continuing with others. - " + var2.getMessage(), var2);
            }
        }

    }

}
