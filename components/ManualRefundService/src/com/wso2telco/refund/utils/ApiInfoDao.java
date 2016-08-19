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
package com.wso2telco.refund.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;

public class ApiInfoDao {

	/** The prop. */
	private Properties prop = new Properties();
	
	/** The Constant logger. */
	final static Logger logger = Logger.getLogger(ApiInfoDao.class);
	
	/**
	 * Instantiates a new api info dao.
	 */
	public ApiInfoDao(){
				
		
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");		 
		
		try {
			prop.load(inputStream);
		} catch (IOException e) {				
			e.printStackTrace();
			logger.error(e.toString());
		}
		
	}
	
	/**
	 * Gets the method.
	 *
	 * @return the method
	 */
	protected String getMethod() {		
		return prop.getProperty("method");
	}

	/**
	 * Gets the resource path.
	 *
	 * @return the resource path
	 */
	protected String getResourcePath() {
		return prop.getProperty("resource_path");
	}

	/**
	 * Gets the host name.
	 *
	 * @return the host name
	 */
	protected String getHostName() {
		
		String host=null;
		
		try {
			host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			host = null;
		}
		
		return host;
	}

	/**
	 * Gets the context.
	 *
	 * @return the context
	 */
	protected String getContext() {
		return prop.getProperty("context");
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	protected String getUsername() {
		return prop.getProperty("userid");
	}

	/**
	 * Gets the api publisher.
	 *
	 * @return the api publisher
	 */
	protected String getApiPublisher() {
		return prop.getProperty("api_publisher");
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	protected String getVersion() {
		return prop.getProperty("version");
	}

	/**
	 * Gets the api_version.
	 *
	 * @return the api_version
	 */
	protected String getApi_version() {
		return prop.getProperty("api_version");
	}

	/**
	 * Gets the api.
	 *
	 * @return the api
	 */
	protected String getApi(){		
		return prop.getProperty("api");
	}
	
	
	/**
	 * Gets the message row id.
	 *
	 * @return the message row id
	 */
	protected String getMessageRowId() {
		UUID uuid = UUID.randomUUID();
		String messageRowId = "ManualRefundAPI_" + uuid;		
		return messageRowId;
	}

	/**
	 * Gets the current time.
	 *
	 * @return the current time
	 */
	protected String getCurrentTime() {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		return timeStamp;
	}	

}
