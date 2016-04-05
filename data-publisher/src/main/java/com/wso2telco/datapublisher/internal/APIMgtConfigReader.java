package com.wso2telco.datapublisher.internal;


import org.apache.axis2.util.JavaUtils;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;

public class APIMgtConfigReader {

    private String bamServerThriftPort;
    private String bamServerURL;
    private String bamServerUser;
    private String bamServerPassword;
    private boolean enabled;

    public APIMgtConfigReader(APIManagerConfiguration config) {
        String enabledStr = config.getFirstProperty(APIConstants.API_USAGE_ENABLED);
        enabled = enabledStr != null && JavaUtils.isTrueExplicitly(enabledStr);
        bamServerUser = config.getFirstProperty(APIConstants.API_USAGE_BAM_SERVER_USER);
        bamServerPassword = config.getFirstProperty(APIConstants.API_USAGE_BAM_SERVER_PASSWORD);
    }

    public String getBamServerThriftPort() {
        return bamServerThriftPort;
    }

    public String getBamServerPassword() {
        return bamServerPassword;
    }

    public String getBamServerUser() {
        return bamServerUser;
    }

    public String getBamServerURL() {
        return bamServerURL;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
