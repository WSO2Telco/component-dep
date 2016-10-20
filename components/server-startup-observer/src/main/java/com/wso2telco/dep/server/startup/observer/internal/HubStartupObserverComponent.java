/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.wso2telco.dep.server.startup.observer.internal;

import com.wso2telco.dep.server.startup.observer.HubStartupObserver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.core.ServerStartupObserver;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.osgi.service.component.ComponentContext;

/**
 * @scr.component name="hub.startup.observer.component" immediate="true"
 * @scr.reference name="registry.service"
 * interface="org.wso2.carbon.registry.core.service.RegistryService"
 * cardinality="1..1"
 * policy="dynamic" bind="setRegistryService"
 * unbind="unsetRegistryService"
 */
public class HubStartupObserverComponent {

    private static final Log log = LogFactory.getLog(HubStartupObserverComponent.class);

    protected void activate(ComponentContext ctx) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("HubStartupObserverComponent activating");
        }
        ctx.getBundleContext().registerService(ServerStartupObserver.class.getName(), new
                HubStartupObserver(), null);
    }

    protected void deactivate(ComponentContext ctx) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("HubStartupObserverComponent de-activating");
        }
    }

    protected void setRegistryService(RegistryService registryService) {
        if (registryService != null && log.isDebugEnabled()) {
            log.debug("Registry service initialized");
        }
        ServiceReferenceHolder.getInstance().setRegistryService(registryService);
    }

    protected void unsetRegistryService(RegistryService registryService) {
        ServiceReferenceHolder.getInstance().setRegistryService(null);
    }

}
