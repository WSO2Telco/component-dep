package org.wso2.carbon.am.axiata.workflow.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import org.wso2.carbon.apimgt.impl.APIManagerConfigurationService;
import org.wso2.carbon.utils.ConfigurationContextService;
import org.wso2.carbon.am.axiata.workflow.internal.ServiceReferenceHolder;

/**
 * @scr.component name="axiata.workflow" immediate="true"
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
public class WorkflowComponent {
	private static final Log log = LogFactory.getLog(WorkflowComponent.class);
	private static APIManagerConfiguration configuration = null;
	
    protected void setConfigurationContextService(ConfigurationContextService contextService) {
        ServiceReferenceHolder.setContextService(contextService);
    }

    protected void unsetConfigurationContextService(ConfigurationContextService contextService) {
        ServiceReferenceHolder.setContextService(null);
    }
    
	protected void setAPIManagerConfigurationService(APIManagerConfigurationService amcService) {
		log.debug("API manager configuration service bound to AxiataWorlflowHandler.");
		configuration = amcService.getAPIManagerConfiguration();
	}

	protected void unsetAPIManagerConfigurationService(APIManagerConfigurationService amcService) {
		log.debug("API manager configuration service unbound from AxiataWorlflowHandler.");
		configuration = null;
	}
	
	public static APIManagerConfiguration getAPIManagerConfiguration() {
		return configuration;
	}
}
