/*
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wso2telco.dep.responsefilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import lk.chathurabuddi.json.schema.filter.JsonSchemaFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class ResponseFilterMediator extends AbstractMediator {

    private static final Log log = LogFactory.getLog(ResponseFilterMediator.class);

    public boolean mediate(MessageContext messageContext) {
        String api = messageContext.getProperty("api.ut.api").toString() + ":" + messageContext.getProperty("api.ut.version").toString();
        String operation = messageContext.getProperty("api.ut.HTTP_METHOD").toString() + " " + messageContext.getProperty("api.ut.resource").toString();
        String userId = messageContext.getProperty("api.ut.userId").toString().split("@")[0];
        String appName = messageContext.getProperty("api.ut.application.name").toString();

        log.info("API : " + api + " | USER : " + userId + " | APP_NAME : " + appName + " | OPERATION : " + operation);

        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
                .getAxis2MessageContext();

        try {
            String jsonString = (String) messageContext.getProperty("jsonPayload");
            if (jsonString != null && !"".equals(jsonString.trim())){
                String filterSchema = findFilterSchema(userId, appName, api, operation);
                if (filterSchema != null) {
                    String filteredJson = new JsonSchemaFilter(filterSchema, jsonString).filter();
                    JsonUtil.getNewJsonPayload(axis2MessageContext, filteredJson, true, true);
                }
            }
        } catch (Exception e) {
            log.error("Error loading ");
            return false;
        }
        return true;
    }

    public String findFilterSchema(String sp, String application, String api, String operation) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String filterSchema = null;

        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            if (connection == null) {
                throw new Exception("database connection error");
            }

            StringBuilder query = new StringBuilder("SELECT fields FROM response_filter");
            query.append(" WHERE sp=? AND application=? AND api=? AND operation=?");
            statement = connection.prepareStatement(query.toString());
            statement.setString(1, sp);
            statement.setString(2, application);
            statement.setString(3, api);
            statement.setString(4, operation);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                filterSchema = resultSet.getString(1);
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            DbUtils.closeAllConnections(statement, connection, resultSet);
        }
        return filterSchema;
    }

}
