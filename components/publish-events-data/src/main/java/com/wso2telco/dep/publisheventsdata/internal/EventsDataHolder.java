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
package com.wso2telco.dep.publisheventsdata.internal;

import com.hazelcast.core.HazelcastInstance;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class EventsDataHolder.
 */
public class EventsDataHolder {

	private static Map<String, String> publisherEventConfMap;

	/** The hazelcast instance. */
	private static HazelcastInstance hazelcastInstance;

	private EventsDataHolder() {
	}

	/**
	 * Register hazelcast instance.
	 *
	 * @param hazelcastInstance
	 *            the hazelcast instance
	 */
	public static void registerHazelcastInstance(HazelcastInstance hazelcastInstance) {
		EventsDataHolder.hazelcastInstance = hazelcastInstance;
	}

	/**
	 * Gets the hazelcast instance.
	 *
	 * @return the hazelcast instance
	 */
	public static HazelcastInstance getHazelcastInstance() {
		return hazelcastInstance;
	}

	public static Map<String, String> getPublisherEventConfMap() {
		return publisherEventConfMap;
	}

	public static void setPublisherEventConfMap(Map<String, String> publisherEventConfMap) {
		EventsDataHolder.publisherEventConfMap = publisherEventConfMap;
	}
}
