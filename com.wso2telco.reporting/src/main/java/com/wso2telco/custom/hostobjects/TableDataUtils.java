/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.custom.hostobjects;

import java.io.InputStream;
import java.util.Properties;

public class TableDataUtils {
    
    private String statsDB;
    private String amDB;
    
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
     * @return the statsDB
     */
    public String getStatsDB() {
        return statsDB;
    }

    /**
     * @return the amDB
     */
    public String getAmDB() {
        return amDB;
    }
}
