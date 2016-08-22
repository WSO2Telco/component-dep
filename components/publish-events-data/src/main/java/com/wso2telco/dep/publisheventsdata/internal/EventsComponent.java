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
import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.publisheventsdata.util.FileNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.databridge.agent.thrift.lb.LoadBalancingDataPublisher;
import org.wso2.carbon.utils.CarbonUtils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @scr.component name="publish.events" immediate="true"
 * @scr.reference name="hazelcast.instance.service"
 *                interface="com.hazelcast.core.HazelcastInstance"
 *                cardinality="0..1" policy="dynamic"
 *                bind="setHazelcastInstance" unbind="unsetHazelcastInstance"
 */
public class EventsComponent {

	/** The Constant log. */
	private static final Log log = LogFactory.getLog(EventsComponent.class);

	/** The data publisher map. */
	private static Map<String, LoadBalancingDataPublisher> dataPublisherMap;

	/**
	 * Activate.
	 *
	 * @param ctx
	 *            the ctx
	 */
	protected void activate(ComponentContext ctx) {

		try {

			dataPublisherMap = new ConcurrentHashMap<String, LoadBalancingDataPublisher>();

			FileReader fileReader = new FileReader();
			String file = CarbonUtils.getCarbonConfigDirPath() + File.separator
					+ FileNames.PUBLISH_EVENTS_DATA_CONF_FILE.getFileName();

			Map<String, String> publisherEventConfMap = fileReader.readPropertyFile(file);
			EventsDataHolder.setPublisherEventConfMap(publisherEventConfMap);

			log.debug("events bundle is activated ");
		} catch (Throwable e) {

			log.error("events bundle activation failed ", e);
		}
	}

	/**
	 * Deactivate.
	 *
	 * @param ctx
	 *            the ctx
	 */
	protected void deactivate(ComponentContext ctx) {

	}

	/**
	 * Gets the data publisher.
	 *
	 * @param tenantDomain
	 *            the tenant domain
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
	 * @param tenantDomain
	 *            the tenant domain
	 * @param dataPublisher
	 *            the data publisher
	 * @throws DataPublisherAlreadyExistsException
	 *             the data publisher already exists exception
	 */
	public static void addDataPublisher(String tenantDomain, LoadBalancingDataPublisher dataPublisher)
			throws DataPublisherAlreadyExistsException {

		if (dataPublisherMap.containsKey(tenantDomain)) {

			throw new DataPublisherAlreadyExistsException(
					"A DataPublisher has already been created for the tenant " + tenantDomain);
		}

		dataPublisherMap.put(tenantDomain, dataPublisher);
	}

	/**
	 * Sets the hazelcast instance.
	 *
	 * @param hazelcastInstance
	 *            the new hazelcast instance
	 */
	protected void setHazelcastInstance(HazelcastInstance hazelcastInstance) {

		EventsDataHolder.registerHazelcastInstance(hazelcastInstance);
	}

	/**
	 * Unset hazelcast instance.
	 *
	 * @param hazelcastInstance
	 *            the hazelcast instance
	 */
	protected void unsetHazelcastInstance(HazelcastInstance hazelcastInstance) {

		EventsDataHolder.registerHazelcastInstance(null);
	}
}
