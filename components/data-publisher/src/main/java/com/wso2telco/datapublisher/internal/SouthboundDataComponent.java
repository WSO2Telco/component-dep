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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.apimgt.impl.APIManagerConfigurationService;
import org.wso2.carbon.databridge.agent.thrift.lb.LoadBalancingDataPublisher;
import org.wso2.carbon.user.core.service.RealmService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


// TODO: Auto-generated Javadoc
/**
 * The Class SouthboundDataComponent.
 */
public class SouthboundDataComponent {

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(SouthboundDataComponent.class);

    /** The api mgt config reader. */
    private static APIMgtConfigReader apiMgtConfigReader;
    
    /** The am config service. */
    private static APIManagerConfigurationService amConfigService;
    
    /** The realm service. */
    private static RealmService realmService;

    /** The data publisher map. */
    private static Map<String, LoadBalancingDataPublisher> dataPublisherMap;

    /**
     * Activate.
     *
     * @param ctx the ctx
     */
    protected void activate(ComponentContext ctx) {
        //TODO change debug logs
        try {
            apiMgtConfigReader = new APIMgtConfigReader(amConfigService.getAPIManagerConfiguration());

            dataPublisherMap = new ConcurrentHashMap<String, LoadBalancingDataPublisher>();

            log.debug("Southbound Data Publisher bundle is activated ");
        } catch (Throwable e) {
            log.error("Southbound Data Publisher bundle ", e);
        }
    }

    /**
     * Deactivate.
     *
     * @param ctx the ctx
     */
    protected void deactivate(ComponentContext ctx) {

    }

    /**
     * Sets the API manager configuration service.
     *
     * @param service the new API manager configuration service
     */
    protected void setAPIManagerConfigurationService(APIManagerConfigurationService service) {
        log.debug("API manager configuration service bound to the API usage handler");
        amConfigService = service;
    }

    /**
     * Unset api manager configuration service.
     *
     * @param service the service
     */
    protected void unsetAPIManagerConfigurationService(APIManagerConfigurationService service) {
        log.debug("API manager configuration service unbound from the API usage handler");
        amConfigService = null;
    }

    /**
     * Gets the api mgt config reader service.
     *
     * @return the api mgt config reader service
     */
    public static APIMgtConfigReader getApiMgtConfigReaderService() {
        return apiMgtConfigReader;
    }

  
    /**
     * Gets the data publisher.
     *
     * @param tenantDomain the tenant domain
     * @return the data publisher
     */
    public static LoadBalancingDataPublisher getDataPublisher(String tenantDomain) {
        if (dataPublisherMap.containsKey(tenantDomain)) {
            return dataPublisherMap.get(tenantDomain);
        }
        return null;
    }

   
    /**
     * Adds the data publisher.
     *
     * @param tenantDomain the tenant domain
     * @param dataPublisher the data publisher
     * @throws DataPublisherAlreadyExistsException the data publisher already exists exception
     */
    public static void addDataPublisher(String tenantDomain, LoadBalancingDataPublisher dataPublisher)
            throws DataPublisherAlreadyExistsException {
        if (dataPublisherMap.containsKey(tenantDomain)) {
            throw new DataPublisherAlreadyExistsException("A DataPublisher has already been created for the tenant " +
                    tenantDomain);
        }
        dataPublisherMap.put(tenantDomain, dataPublisher);
    }

    /**
     * Sets the realm service.
     *
     * @param service the new realm service
     */
    protected void setRealmService(RealmService service) {
        if (realmService != null && log.isDebugEnabled()) {
            log.debug("Realm service initialized");
        }
        realmService = service;
    }

    /**
     * Unset realm service.
     *
     * @param realmService the realm service
     */
    protected void unsetRealmService(RealmService realmService) {
        realmService = null;
    }

}
