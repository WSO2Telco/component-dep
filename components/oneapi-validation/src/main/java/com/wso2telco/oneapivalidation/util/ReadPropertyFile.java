package com.wso2telco.oneapivalidation.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Hiranya
 */
public class ReadPropertyFile {
    
    private final String PROP_FILE_NAME = "config.properties";

    private String getPropertyValue(String propertyName) throws IOException {

        //this is the default batch size that consider when propery file not found. Change if you want to do so
        String result = "";

        Properties prop = new Properties();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROP_FILE_NAME);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            System.out.println("property file '" + PROP_FILE_NAME + "' not found in the classpath");
        }

        // get the property value and print it out
        String propVal = prop.getProperty(propertyName);
        if (propVal != null) {
            result = propVal;
        }

        System.out.println(result + "\nReading property " + propertyName + " on  " + PROP_FILE_NAME + ". Result = " + result);
        inputStream.close();
        return result;
    }

    public int getSMSBatchSize() {
        //this is the default batch size that consider when propery file not found. Change if you want to do so
        int result = 25;
        
        try {
            String value = getPropertyValue("sms.batchSize");
            if(!value.equals("")){
                result = Integer.valueOf(value);
            }
        } catch (Exception e) {
            System.out.println("Exception in getSMSBatchSize() :: " + e);
            result = 25;
        }
        return result;
    }

    public boolean getIsSMSLimitToOne() {
      boolean result = true;
        try {
            String value = getPropertyValue("sms.limitToOne");
            if(value.equals("false")){
                result = false;
            }
        } catch (Exception e) {
            System.out.println("Exception in getIsSMSLimitToOne() :: " + e);

        }
        return result;
    }

    public boolean getApiTypeAvailability(String apiTypeParam){
        boolean isAvailable = false;
        try {
            String value = getPropertyValue(apiTypeParam);
            if(!value.equals("")){
                isAvailable = Boolean.valueOf(value);
            }
        } catch (Exception e) {
            System.out.println("Exception in getApiTypeAvailability("+apiTypeParam+") :: " + e);
            isAvailable = false;
        }
        return isAvailable;
    }
    
}
