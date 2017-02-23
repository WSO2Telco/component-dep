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
package com.wso2telco.dep.operatorservice.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.impl.APIManagerConfigurationService;
import org.wso2.carbon.utils.ConfigurationContextService;

 
/**
 * @scr.component name="operator.store" immediate="true"
 *
 * @scr.reference name="config.context.service"
 * 				  interface="org.wso2.carbon.utils.ConfigurationContextService"
 * 				  cardinality="1..1" 
 * 				  policy="dynamic"  
 * 				  bind="setConfigurationContextService"
 * 				  unbind="unsetConfigurationContextService"
 *
 *@scr.reference name="apimanager.configurations.service"
 * 				  interface="org.wso2.carbon.apimgt.impl.APIManagerConfigurationService"
 * 				  cardinality="1..1"
 * 				  policy="dynamic"
 * 				  bind="setAPIManagerConfigurationService"
 * 				  unbind="unsetAPIManagerConfigurationService"
 *
 *
 */
public class StoreComponent {
	
	/** The Constant log. */
	private static final Log log = LogFactory.getLog(StoreComponent.class);
	
    /**
     * Sets the configuration context service.
     *
     * @param apiManagerConfigurationService the new configuration context service
     */
    protected void setAPIManagerConfigurationService(APIManagerConfigurationService apiManagerConfigurationService) {
        ServiceReferenceHolder.setAPIManagerConfigurationService(apiManagerConfigurationService);
    }

    /**
     * Unset configuration context service.
     *
     * @param apiManagerConfigurationService the context service
     */
    protected void unsetAPIManagerConfigurationService(APIManagerConfigurationService apiManagerConfigurationService) {
        ServiceReferenceHolder.setAPIManagerConfigurationService(null);
    }

	/**
	 * Sets the configuration context service.
	 *
	 * @param contextService the new configuration context service
	 */
	protected void setConfigurationContextService(ConfigurationContextService contextService) {
		ServiceReferenceHolder.setContextService(contextService);
	}

	/**
	 * Unset configuration context service.
	 *
	 * @param contextService the context service
	 */
	protected void unsetConfigurationContextService(ConfigurationContextService contextService) {
		ServiceReferenceHolder.setContextService(null);
	}
}
