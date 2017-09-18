/*******************************************************************************
 * Copyright (c) 2015-2017, WSO2.Telco Inc. (http://www.wso2telco.com)
 *
 * All Rights Reserved. WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.apihandler.util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ReadPropertyFile {

    private static final Log log = LogFactory.getLog(ReadPropertyFile.class);
    private static final String FILE_NAME = "mig_aouth_token.properties";
    private static final String FILE_PATH = java.lang.System.getProperty("carbon.config.dir.path") + "/" + FILE_NAME;

    private ReadPropertyFile() {

    }

    public static Map<String, String> getPropertyFile() {
        Properties prop = new Properties();
        InputStream input = null;
        Map<String, String> propertyMap = new HashMap<String, String>();
        try {
            input = new FileInputStream(FILE_PATH);
            prop.load(input);
            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                propertyMap.put(key, value);
            }

        } catch (IOException ex) {
            log.error(ex.getStackTrace());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    log.error(e.getStackTrace());
                }
            }
        }

        return propertyMap;

    }
}
