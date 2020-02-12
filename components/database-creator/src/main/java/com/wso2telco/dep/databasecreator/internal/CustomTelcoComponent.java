/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
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

package com.wso2telco.dep.databasecreator.internal;

import com.wso2telco.dep.databasecreator.util.DatabaseUtil;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.utils.ConfigurationContextService;

@Component(name = "com.wso2telco.dep.databasecreator.internal.CustomTelcoComponent", immediate = true)
public class CustomTelcoComponent {
	
	private static Log log = LogFactory.getLog(CustomTelcoComponent.class);
	private ConfigurationContext configContext;

	protected void activate(ComponentContext ctxt) {
		
		try {
			
			DatabaseUtil.initialize();
			log.info("Telco databases created successfully..");
		} catch (Throwable e) {
			
			log.error("Error while creating telco databases : ", e);
		}
	}

	protected void deactivate(ComponentContext ctxt) {
		
		if (log.isDebugEnabled()) {
			
			log.debug("Telco database creater bundle is deactivated ");
		}
	}

	@Reference(name = "config.context.service", service = org.wso2.carbon.utils.ConfigurationContextService.class, cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC, unbind = "unsetConfigurationContextService")
	protected void setConfigurationContextService(ConfigurationContextService contextService) {

		this.configContext = contextService.getServerConfigContext();
	}

	protected void unsetConfigurationContextService(ConfigurationContextService contextService) {
		
		this.configContext = null;
	}
}