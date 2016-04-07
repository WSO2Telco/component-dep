package com.wso2telco.publisheventsdata.internal;


import com.hazelcast.core.HazelcastInstance;

import java.util.Properties;

public class MifeEventsDataHolder {

    private static Properties mifeEventsProps;
    private static HazelcastInstance hazelcastInstance;

    private MifeEventsDataHolder() {
    }

    public static Properties getMifeEventsProps() {
        return mifeEventsProps;
    }

    public static void setMifeEventsProps(Properties mifeEventsProps) {
        MifeEventsDataHolder.mifeEventsProps = mifeEventsProps;
    }

    public static void registerHazelcastInstance(HazelcastInstance hazelcastInstance) {
        MifeEventsDataHolder.hazelcastInstance = hazelcastInstance;
    }

    public static HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }
}
