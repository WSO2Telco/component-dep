package com.wso2telco.operatorservice.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import org.wso2.carbon.apimgt.impl.APIManagerConfigurationService;
import org.wso2.carbon.utils.ConfigurationContextService;

/**
 * @scr.component name="axiata.store" immediate="true"
 * @scr.reference name="config.context.service"
 * 				  interface="org.wso2.carbon.utils.ConfigurationContextService"
 * 				  cardinality="1..1" 
 * 				  policy="dynamic"  
 * 				  bind="setConfigurationContextService"
 * 				  unbind="unsetConfigurationContextService"
 * @scr.reference name="api.manager.config.service"
 *                interface=
 *                "org.wso2.carbon.apimgt.impl.APIManagerConfigurationService"
 *                cardinality="1..1"
 *                policy="dynamic" bind="setAPIManagerConfigurationService"
 *                unbind="unsetAPIManagerConfigurationService"
 *
 */
public class AxiataStoreComponent {
	private static final Log log = LogFactory.getLog(AxiataStoreComponent.class);
	private static APIManagerConfiguration configuration = null;
	
    protected void setConfigurationContextService(ConfigurationContextService contextService) {
        ServiceReferenceHolder.setContextService(contextService);
    }

    protected void unsetConfigurationContextService(ConfigurationContextService contextService) {
        ServiceReferenceHolder.setContextService(null);
    }
    
	protected void setAPIManagerConfigurationService(APIManagerConfigurationService amcService) {
		log.debug("API manager configuration service bound to AxiataStore host objects.");
		configuration = amcService.getAPIManagerConfiguration();
	}

	protected void unsetAPIManagerConfigurationService(APIManagerConfigurationService amcService) {
		log.debug("API manager configuration service unbound from AxiataStore host objects.");
		configuration = null;
	}
	
	public static APIManagerConfiguration getAPIManagerConfiguration() {
		return configuration;
	}
}
