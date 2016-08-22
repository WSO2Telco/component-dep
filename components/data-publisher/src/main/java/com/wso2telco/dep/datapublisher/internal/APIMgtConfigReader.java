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
package com.wso2telco.dep.datapublisher.internal;


import org.apache.axis2.util.JavaUtils;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;

// TODO: Auto-generated Javadoc
/**
 * The Class APIMgtConfigReader.
 */
public class APIMgtConfigReader {

    /** The das server url. */
    private String dasServerURL;
    
    /** The das server user. */
    private String dasServerUser;
    
    /** The das server password. */
    private String dasServerPassword;
    
    /** The enabled. */
    private boolean enabled;

    /**
     * Instantiates a new API mgt config reader.
     *
     * @param config the config
     */
    public APIMgtConfigReader(APIManagerConfiguration config) {
        String enabledStr = config.getFirstProperty(APIConstants.API_USAGE_ENABLED);
        enabled = enabledStr != null && JavaUtils.isTrueExplicitly(enabledStr);
        dasServerURL = config.getFirstProperty(APIConstants.API_USAGE_BAM_SERVER_URL_GROUPS);
        dasServerUser = config.getFirstProperty(APIConstants.API_USAGE_BAM_SERVER_USER);
        dasServerPassword = config.getFirstProperty(APIConstants.API_USAGE_BAM_SERVER_PASSWORD);
    }

    /**
     * Gets the das server password.
     *
     * @return the das server password
     */
    public String getDasServerPassword() {
        return dasServerPassword;
    }

    /**
     * Gets the das server user.
     *
     * @return the das server user
     */
    public String getDasServerUser() {
        return dasServerUser;
    }

    /**
     * Gets the das server url.
     *
     * @return the das server url
     */
    public String getDasServerURL() {
        return dasServerURL;
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }
}
