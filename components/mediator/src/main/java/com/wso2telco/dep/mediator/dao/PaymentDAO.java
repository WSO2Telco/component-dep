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
package com.wso2telco.dep.mediator.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.wso2telco.dbutils.DbUtils;
import com.wso2telco.dbutils.util.DataSourceNames;

public class PaymentDAO extends CommonDAO{

	/**
     * Gets the valid pay categories.
     *
     * @return the valid pay categories
     * @throws Exception the exception
     */
    public List<String> getValidPayCategories() throws Exception {

        Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
        Statement st = null;
        ResultSet rs = null;
        List<String> categories = new ArrayList<String>();

        try {
        	
            if (con == null) {
            	
                throw new Exception("Connection not found");
            }

            st = con.createStatement();
            
            StringBuilder queryString = new StringBuilder("SELECT id, category ");
            queryString.append("FROM valid_payment_categories");

            rs = st.executeQuery(queryString.toString());

            while (rs.next()) {    
            	
                categories.add(rs.getString("category"));
            }

        } catch (Exception e) {
        	
            DbUtils.handleException("Error while retrieving valid payment categories. ", e);
        } finally {
        	
            DbUtils.closeAllConnections(st, con, rs);
        }
        
        return categories;
    }
}
