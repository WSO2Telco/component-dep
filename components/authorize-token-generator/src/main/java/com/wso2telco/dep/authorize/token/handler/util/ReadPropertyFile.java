package com.wso2telco.dep.authorize.token.handler.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by sheshan on 8/8/17.
 */
public class ReadPropertyFile {

    private static final Log log = LogFactory.getLog(ReadPropertyFile.class);
    private static final String FILE_NAME = "mig_aouth_token.properties";
    private static final String FILE_PATH = java.lang.System.getProperty("carbon.config.dir.path")+"/"+FILE_NAME;

    public static Map<String, String> getPropertyFile(){
        Properties prop = new Properties();
        InputStream input = null;
        Map<String, String> propertyMap = new HashMap<String, String>();
        try {
            input = new FileInputStream(FILE_PATH);
            if (input == null) {
                log.error("Sorry, unable to find " + FILE_PATH);
            }
            prop.load(input);
            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                propertyMap.put(key , value);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return propertyMap;

    }
}

