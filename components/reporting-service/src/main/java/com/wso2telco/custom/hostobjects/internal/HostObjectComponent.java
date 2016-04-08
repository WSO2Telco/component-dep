package com.wso2telco.custom.hostobjects.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import org.wso2.carbon.apimgt.impl.APIManagerConfigurationService;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * @scr.component name="dialog.HostObject" immediate="true"
 * @scr.reference name="api.manager.config.service"
 *                interface=
 *                "org.wso2.carbon.apimgt.impl.APIManagerConfigurationService"
 *                cardinality="1..1"
 *                policy="dynamic" bind="setAPIManagerConfigurationService"
 *                unbind="unsetAPIManagerConfigurationService"
 * @scr.reference name="user.realm.service"
 *                interface="org.wso2.carbon.user.core.service.RealmService"
 *                cardinality="1..1" policy="dynamic" bind="setRealmService"
 *                unbind="unsetRealmService"
 * @scr.reference name="registry.service"
 *                interface="org.wso2.carbon.registry.core.service.RegistryService"
 *                cardinality="1..1" policy="dynamic" bind="setRegistryService"
 *                unbind="unsetRegistryService"
 * */
public class HostObjectComponent {
	private static final Log log = LogFactory.getLog(HostObjectComponent.class);

	private static APIManagerConfiguration configuration = null;

	private static RealmService realmService = null;

    private static RegistryService registryService = null;

	protected void activate(ComponentContext componentContext) {
		log.debug("HostObjectComponent activated");
		log.info("BillingHostObjectComponent activated");
	}

	protected void deactivate(ComponentContext componentContext) {
		log.debug("HostObjectComponent deactivated");
		log.info(":::HostObjectComponent deactivated:::");
	}

	protected void setAPIManagerConfigurationService(APIManagerConfigurationService amcService) {
		log.debug("API manager configuration service bound to the API host objects");
		configuration = amcService.getAPIManagerConfiguration();
	}

	protected void unsetAPIManagerConfigurationService(APIManagerConfigurationService amcService) {
		log.debug("API manager configuration service unbound from the API host objects");
		configuration = null;
	}

	protected void setRealmService(RealmService realmService) {
		log.debug("Realm service initialized");
		HostObjectComponent.realmService = realmService;
	}

	protected void unsetRealmService(RealmService realmService) {
		HostObjectComponent.realmService = null;
	}

    protected void setRegistryService(RegistryService registryService) {
        if (registryService != null && log.isDebugEnabled()) {
            log.debug("Registry service initialized");
        }
        HostObjectComponent.registryService = registryService;
    }

    protected void unsetRegistryService(RegistryService registryService) {
        HostObjectComponent.registryService = null;
    }

	public static RealmService getRealmService() {
		return realmService;
	}

	public static APIManagerConfiguration getAPIManagerConfiguration() {
		return configuration;
	}

    public static RegistryService getRegistryService() { return registryService; }
}
