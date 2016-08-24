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
package com.wso2telco.publisheventsdata.internal;


import com.hazelcast.core.HazelcastInstance;

import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class MifeEventsDataHolder.
 */
public class MifeEventsDataHolder {

    /** The mife events props. */
    private static Properties mifeEventsProps;
    
    /** The hazelcast instance. */
    private static HazelcastInstance hazelcastInstance;

    /**
     * Instantiates a new mife events data holder.
     */
    private MifeEventsDataHolder() {
    }

    /**
     * Gets the mife events props.
     *
     * @return the mife events props
     */
    public static Properties getMifeEventsProps() {
        return mifeEventsProps;
    }

    /**
     * Sets the mife events props.
     *
     * @param mifeEventsProps the new mife events props
     */
    public static void setMifeEventsProps(Properties mifeEventsProps) {
        MifeEventsDataHolder.mifeEventsProps = mifeEventsProps;
    }

    /**
     * Register hazelcast instance.
     *
     * @param hazelcastInstance the hazelcast instance
     */
    public static void registerHazelcastInstance(HazelcastInstance hazelcastInstance) {
        MifeEventsDataHolder.hazelcastInstance = hazelcastInstance;
    }

    /**
     * Gets the hazelcast instance.
     *
     * @return the hazelcast instance
     */
    public static HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }
}
