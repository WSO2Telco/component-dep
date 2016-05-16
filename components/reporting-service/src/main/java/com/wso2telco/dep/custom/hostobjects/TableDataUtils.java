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
package com.wso2telco.dep.custom.hostobjects;

import java.io.InputStream;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class TableDataUtils.
 */
public class TableDataUtils {
    
    /** The stats db. */
    private String statsDB;
    
    /** The am db. */
    private String amDB;
    
    /**
     * Instantiates a new table data utils.
     */
    public TableDataUtils(){
        Properties props = new Properties();
        InputStream in = null;
        
        try {
            in = this.getClass().getClassLoader().getResourceAsStream("tabledata.properties");
            props.load(in);
            in.close();
            
            this.statsDB = props.getProperty("wso2.statsdb");
            this.amDB = props.getProperty("wso2.amdb");
            
            
        } catch (Exception e) {
            System.out.println("Error in Invoking PropertyReader(): "+e);
        }
    }

     
    /**
     * Gets the stats db.
     *
     * @return the stats db
     */
    public String getStatsDB() {
        return statsDB;
    }

     
    /**
     * Gets the am db.
     *
     * @return the am db
     */
    public String getAmDB() {
        return amDB;
    }
}
