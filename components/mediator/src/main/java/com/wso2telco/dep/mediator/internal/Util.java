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
package com.wso2telco.dep.mediator.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.CarbonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class Util.
 */
public class Util {

	private static RealmService realmService;
	private static Properties props = new Properties();
	private static Log log = LogFactory.getLog(Util.class);
	public static Map<String, String> propMap = new HashMap<String, String>();

	/**
	 * Gets the realm service.
	 *
	 * @return the realm service
	 */
	public static RealmService getRealmService() {
		return realmService;
	}

	/**
	 * Sets the realm service.
	 *
	 * @param realmSer
	 *            the new realm service
	 */
	public static synchronized void setRealmService(RealmService realmSer) {

		realmService = realmSer;

	}

	public static String getApplicationProperty(String key) {

		return props.getProperty(key);
	}

	public static void getPropertyFile() {
		try {
			String configPath =
					CarbonUtils.getCarbonConfigDirPath() + File.separator + "axiataMediator_conf.properties";
			FileInputStream in = new FileInputStream(configPath);
			props.load(in);
			// String carbonHome = System.getProperty("user.dir");
			// log.debug("Carbon home : " + carbonHome);

			//props.load(Util.class.getResourceAsStream( carbonHome + "/repository/conf/axiataMediator_conf
			// .properties"));
			//log.debug( Util.getApplicationProperty("sendSMSResourceURL"));
			// log.debug (Util.getApplicationProperty("hubGateway"));
			//log.debug(Util.getApplicationProperty("ussdGatewayEndpoint"));

		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			log.info("FileNotFound");
			System.err.println(
					"Check your Property file, it should be in application home dir, Error:"
					+ e.getCause() + "Cant load APPLICATION.properties");

			//System.exit(-1);
		} catch (IOException e) {
			log.info("IO Error");
			System.err.println(
					"Check your Property file, it should be in application home dir, Error:"
					+ e.getCause() + "Cant load APPLICATION.properties");
			//System.exit(-1);
		}
	}

	public static void getPropertyFileByPath(String path) throws IOException {

		Properties props = new Properties();
		FileInputStream in = new FileInputStream(path);

		try {

			props.load(in);
			propMap.put("ussdGatewayEndpoint", props.getProperty("ussdGatewayEndpoint"));
			propMap.put("hub_gateway_id", props.getProperty("hub_gateway_id"));
			propMap.put("hubMOSubsGatewayEndpoint", props.getProperty("hubMOSubsGatewayEndpoint"));
			propMap.put("hubDNSubsGatewayEndpoint", props.getProperty("hubDNSubsGatewayEndpoint"));

			propMap.put("retry_on_fail", props.getProperty("retry_on_fail"));
			propMap.put("retry_count", props.getProperty("retry_count"));
			propMap.put("esbEndpoint", props.getProperty("esbEndpoint"));
		} catch (FileNotFoundException e) {

			log.debug("file not found !!! ");
		} catch (IOException e) {

			log.debug("file not found !!! ");
		} finally {

			in.close();
		}
	}

	/**
	 * Checks if is all null.
	 *
	 * @param list
	 *            the list
	 * @return true, if is all null
	 */
	public static boolean isAllNull(Iterable<?> list) {
		for (Object obj : list) {
			if (obj != null)
				return false;
		}
		return true;
	}

}
