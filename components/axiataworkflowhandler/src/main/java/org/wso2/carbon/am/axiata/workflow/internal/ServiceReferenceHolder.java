package org.wso2.carbon.am.axiata.workflow.internal;

import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import org.wso2.carbon.utils.ConfigurationContextService;

public class ServiceReferenceHolder {
	private static final ServiceReferenceHolder instance = new ServiceReferenceHolder();
	private static ConfigurationContextService contextService = null;
	private static APIManagerConfiguration configuration = null;
	

    public static ServiceReferenceHolder getInstance() {
        return instance;
    }
    
    public static ConfigurationContextService getContextService() {
        return contextService;
    }

    public static void setContextService(ConfigurationContextService contextService) {
        ServiceReferenceHolder.contextService = contextService;
    }
    
    public static APIManagerConfiguration getAPIManagerConfiguration() {
		return configuration;
	}
    
    public static APIManagerConfiguration setAPIManagerConfiguration() {
		return configuration;
	}

}
