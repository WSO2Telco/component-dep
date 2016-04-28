/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.custom.hostobjects.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import org.wso2.carbon.apimgt.impl.APIManagerConfigurationService;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;

 
/**
 * @scr.component name="reporting.HostObject" immediate="true"
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
 * */
public class HostObjectComponent {
	
	/** The Constant log. */
	private static final Log log = LogFactory.getLog(HostObjectComponent.class);

	/** The configuration. */
	private static APIManagerConfiguration configuration = null;

	/** The realm service. */
	private static RealmService realmService = null;

    /** The registry service. */
    private static RegistryService registryService = null;

	/**
	 * Activate.
	 *
	 * @param componentContext the component context
	 */
	protected void activate(ComponentContext componentContext) {
		log.debug("HostObjectComponent activated");
		log.info("BillingHostObjectComponent activated");
	}

	/**
	 * Deactivate.
	 *
	 * @param componentContext the component context
	 */
	protected void deactivate(ComponentContext componentContext) {
		log.debug("HostObjectComponent deactivated");
		log.info(":::HostObjectComponent deactivated:::");
	}

	/**
	 * Sets the API manager configuration service.
	 *
	 * @param amcService the new API manager configuration service
	 */
	protected void setAPIManagerConfigurationService(APIManagerConfigurationService amcService) {
		log.debug("API manager configuration service bound to the API host objects");
		configuration = amcService.getAPIManagerConfiguration();
	}

	/**
	 * Unset api manager configuration service.
	 *
	 * @param amcService the amc service
	 */
	protected void unsetAPIManagerConfigurationService(APIManagerConfigurationService amcService) {
		log.debug("API manager configuration service unbound from the API host objects");
		configuration = null;
	}

	/**
	 * Sets the realm service.
	 *
	 * @param realmService the new realm service
	 */
	protected void setRealmService(RealmService realmService) {
		log.debug("Realm service initialized");
		HostObjectComponent.realmService = realmService;
	}

	/**
	 * Unset realm service.
	 *
	 * @param realmService the realm service
	 */
	protected void unsetRealmService(RealmService realmService) {
		HostObjectComponent.realmService = null;
	}

    /**
     * Sets the registry service.
     *
     * @param registryService the new registry service
     */
    protected void setRegistryService(RegistryService registryService) {
        if (registryService != null && log.isDebugEnabled()) {
            log.debug("Registry service initialized");
        }
        HostObjectComponent.registryService = registryService;
    }

    /**
     * Unset registry service.
     *
     * @param registryService the registry service
     */
    protected void unsetRegistryService(RegistryService registryService) {
        HostObjectComponent.registryService = null;
    }

	/**
	 * Gets the realm service.
	 *
	 * @return the realm service
	 */
	public static RealmService getRealmService() {
		return realmService;
	}

	/**
	 * Gets the API manager configuration.
	 *
	 * @return the API manager configuration
	 */
	public static APIManagerConfiguration getAPIManagerConfiguration() {
		return configuration;
	}

    /**
     * Gets the registry service.
     *
     * @return the registry service
     */
    public static RegistryService getRegistryService() { return registryService; }
}
