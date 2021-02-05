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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import lk.chathurabuddi.json.schema.constants.FreeFormAction;
import lk.chathurabuddi.json.schema.filter.JsonSchemaFilter;
import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.glassfish.jersey.uri.UriTemplate;

public class ResponseFilterMediator extends AbstractMediator {

    public boolean mediate(MessageContext messageContext) {
        String api = messageContext.getProperty("api.ut.api").toString() + ":" + messageContext.getProperty("api.ut.version").toString();
        String httpVerb = messageContext.getProperty("api.ut.HTTP_METHOD").toString();
        String resource = messageContext.getProperty("api.ut.resource").toString();
        String userId = messageContext.getProperty("api.ut.userId").toString().split("@")[0];
        String appName = messageContext.getProperty("api.ut.application.name").toString();

        if(log.isDebugEnabled()){
            log.debug("API : " + api + " | USER : " + userId + " | APP_NAME : " + appName + " | HTTP_VERB : " + httpVerb + " | RESOURCE : " + resource);
        }

        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
                .getAxis2MessageContext();

        try {
            String jsonString = (String) messageContext.getProperty("jsonPayload");
            if (jsonString != null && !"".equals(jsonString.trim())){
                String filterSchema = findFilterSchema(userId, appName, api, httpVerb, resource);
                if (filterSchema != null && isMatchingPayload(jsonString, filterSchema)) {
                    String filteredJson = new JsonSchemaFilter(filterSchema, jsonString, FreeFormAction.DETACH).filter();
                    if (isNonEmptyJson(filteredJson)) {
                        JsonUtil.getNewJsonPayload(axis2MessageContext, filteredJson, true, true);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error while filtering the response.", e);
            return false;
        }
        return true;
    }

    // compare the root elements of the payload according to the filter schema
    private boolean isMatchingPayload(String jsonString, String filterSchema) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(jsonString);
        JsonNode schema = mapper.readTree(filterSchema);
        String rootElementType = schema.findPath("type").asText();
        return !((json.isArray() != "array".equals(rootElementType)) ||
                ("object".equals(rootElementType) && !rootElementsMatched(json, schema)));
    }

    private boolean rootElementsMatched(JsonNode json, JsonNode schema) {
        Iterator<String> properties = schema.findPath("properties").fieldNames();
        while (properties.hasNext()) {
            String property = properties.next();
            if (json.has(property)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNonEmptyJson(String filteredJson) {
        return filteredJson != null && !filteredJson.isEmpty() && !"{}".equals(filteredJson);
    }

    public String findFilterSchema(String sp, String application, String api, String httpVerb, String resource) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String filterSchema = null;

        try {
            connection = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            if (connection == null) {
                throw new SQLException("database connection error");
            }

            StringBuilder query = new StringBuilder("SELECT operation, fields FROM response_filter");
            query.append(" WHERE sp=? AND application=? AND api=?");
            statement = connection.prepareStatement(query.toString());
            statement.setString(1, sp);
            statement.setString(2, application);
            statement.setString(3, api);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String[] uriTemplateComponents = resultSet.getString(1).split(" ");
                if (httpVerb != null &&
                        httpVerb.equals(uriTemplateComponents[0]) &&
                        new UriTemplate(uriTemplateComponents[1]).match(resource, new ArrayList<String>())) {
                    filterSchema = resultSet.getString(2);
                }
            }
        } finally {
            DbUtils.closeAllConnections(statement, connection, resultSet);
        }
        return filterSchema;
    }

}
