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
package com.wso2telco.datapublisher.internal;


import org.apache.axis2.util.JavaUtils;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;

// TODO: Auto-generated Javadoc
/**
 * The Class APIMgtConfigReader.
 */
public class APIMgtConfigReader {

    /** The bam server thrift port. */
    private String bamServerThriftPort;
    
    /** The bam server url. */
    private String bamServerURL;
    
    /** The bam server user. */
    private String bamServerUser;
    
    /** The bam server password. */
    private String bamServerPassword;
    
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
        bamServerUser = config.getFirstProperty(APIConstants.API_USAGE_BAM_SERVER_USER);
        bamServerPassword = config.getFirstProperty(APIConstants.API_USAGE_BAM_SERVER_PASSWORD);
    }

    /**
     * Gets the bam server thrift port.
     *
     * @return the bam server thrift port
     */
    public String getBamServerThriftPort() {
        return bamServerThriftPort;
    }

    /**
     * Gets the bam server password.
     *
     * @return the bam server password
     */
    public String getBamServerPassword() {
        return bamServerPassword;
    }

    /**
     * Gets the bam server user.
     *
     * @return the bam server user
     */
    public String getBamServerUser() {
        return bamServerUser;
    }

    /**
     * Gets the bam server url.
     *
     * @return the bam server url
     */
    public String getBamServerURL() {
        return bamServerURL;
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
