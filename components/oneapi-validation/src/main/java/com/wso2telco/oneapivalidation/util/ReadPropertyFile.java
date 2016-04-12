/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.oneapivalidation.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

 
// TODO: Auto-generated Javadoc
/**
 * The Class ReadPropertyFile.
 */
public class ReadPropertyFile {
    
    /** The prop file name. */
    private final String PROP_FILE_NAME = "config.properties";

    /**
     * Gets the property value.
     *
     * @param propertyName the property name
     * @return the property value
     * @throws IOException Signals that an I/O exception has occurred.
     */
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

    /**
     * Gets the SMS batch size.
     *
     * @return the SMS batch size
     */
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

    /**
     * Gets the checks if is sms limit to one.
     *
     * @return the checks if is sms limit to one
     */
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

    /**
     * Gets the api type availability.
     *
     * @param apiTypeParam the api type param
     * @return the api type availability
     */
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
