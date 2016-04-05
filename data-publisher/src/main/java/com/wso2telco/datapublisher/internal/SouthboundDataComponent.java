package com.wso2telco.datapublisher.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.apimgt.impl.APIManagerConfigurationService;
import org.wso2.carbon.databridge.agent.thrift.lb.LoadBalancingDataPublisher;
import org.wso2.carbon.user.core.service.RealmService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @scr.component name="southbound.data.component" immediate="true"
 * @scr.reference name="api.manager.config.service"
 * interface="org.wso2.carbon.apimgt.impl.APIManagerConfigurationService"
 * cardinality="1..1"
 * policy="dynamic" bind="setAPIManagerConfigurationService"
 * unbind="unsetAPIManagerConfigurationService"
 * @scr.reference name="user.realm.service"
 * interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService"
 * unbind="unsetRealmService"
 */
public class SouthboundDataComponent {

    private static final Log log = LogFactory.getLog(SouthboundDataComponent.class);

    private static APIMgtConfigReader apiMgtConfigReader;
    private static APIManagerConfigurationService amConfigService;
    private static RealmService realmService;

    private static Map<String, LoadBalancingDataPublisher> dataPublisherMap;

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

    protected void deactivate(ComponentContext ctx) {

    }

    protected void setAPIManagerConfigurationService(APIManagerConfigurationService service) {
        log.debug("API manager configuration service bound to the API usage handler");
        amConfigService = service;
    }

    protected void unsetAPIManagerConfigurationService(APIManagerConfigurationService service) {
        log.debug("API manager configuration service unbound from the API usage handler");
        amConfigService = null;
    }

    public static APIMgtConfigReader getApiMgtConfigReaderService() {
        return apiMgtConfigReader;
    }

    /**
     * Fetch the data publisher which has been registered under the tenant domain.
     *
     * @param tenantDomain - The tenant domain under which the data publisher is registered
     * @return - Instance of the LoadBalancingDataPublisher which was registered. Null if not registered.
     */
    public static LoadBalancingDataPublisher getDataPublisher(String tenantDomain) {
        if (dataPublisherMap.containsKey(tenantDomain)) {
            return dataPublisherMap.get(tenantDomain);
        }
        return null;
    }

    /**
     * Adds a LoadBalancingDataPublisher to the data publisher map.
     *
     * @param tenantDomain  - The tenant domain under which the data publisher will be registered.
     * @param dataPublisher - Instance of the LoadBalancingDataPublisher
     * @throws DataPublisherAlreadyExistsException - If a data publisher has already been registered under the
     *                                             tenant domain
     */
    public static void addDataPublisher(String tenantDomain, LoadBalancingDataPublisher dataPublisher)
            throws DataPublisherAlreadyExistsException {
        if (dataPublisherMap.containsKey(tenantDomain)) {
            throw new DataPublisherAlreadyExistsException("A DataPublisher has already been created for the tenant " +
                    tenantDomain);
        }
        dataPublisherMap.put(tenantDomain, dataPublisher);
    }

    protected void setRealmService(RealmService service) {
        if (realmService != null && log.isDebugEnabled()) {
            log.debug("Realm service initialized");
        }
        realmService = service;
    }

    protected void unsetRealmService(RealmService realmService) {
        realmService = null;
    }

}
