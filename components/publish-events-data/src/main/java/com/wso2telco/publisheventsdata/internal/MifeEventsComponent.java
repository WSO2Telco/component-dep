package com.wso2telco.publisheventsdata.internal;


import com.hazelcast.core.HazelcastInstance;
import com.wso2telco.publisheventsdata.MifeEventsConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.databridge.agent.thrift.lb.LoadBalancingDataPublisher;
import org.wso2.carbon.utils.CarbonUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @scr.component name="mife.events.component" immediate="true"
 * @scr.reference name="hazelcast.instance.service"
 * interface="com.hazelcast.core.HazelcastInstance" cardinality="0..1"
 * policy="dynamic" bind="setHazelcastInstance" unbind="unsetHazelcastInstance"
 */
public class MifeEventsComponent {

    private static final Log log = LogFactory.getLog(MifeEventsComponent.class);

    private static Map<String, LoadBalancingDataPublisher> dataPublisherMap;

    protected void activate(ComponentContext ctx) {
        try {
            dataPublisherMap = new ConcurrentHashMap<String, LoadBalancingDataPublisher>();
            Properties eventProperties = loadEventProperties();
            MifeEventsDataHolder.setMifeEventsProps(eventProperties);

            log.debug("MIFE Events bundle is activated ");
        } catch (Throwable e) {
            log.error("MIFE Events bundle activation failed ", e);
        }
    }

    protected void deactivate(ComponentContext ctx) {

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

    protected void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        MifeEventsDataHolder.registerHazelcastInstance(hazelcastInstance);
    }

    protected void unsetHazelcastInstance(HazelcastInstance hazelcastInstance) {
        MifeEventsDataHolder.registerHazelcastInstance(null);
    }

    private Properties loadEventProperties(){
        String configPath = CarbonUtils.getCarbonConfigDirPath() + File.separator + MifeEventsConstants.MIFE_EVENTS_PROPERTIES_FILE;
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(configPath));
        } catch (IOException e) {
            log.error("Error while loading mife-events.properties file", e);
        }
        return props;
    }

}
