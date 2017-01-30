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

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.dep.mediator.dao.StatDao;

public class StatInformationUtil {

	private final Log log = LogFactory.getLog(StatInformationUtil.class);

	public static final String API = "api.ut.api";
	public static final String SERVICE_PROVIDER_USERNAME = "api.ut.userId";
	public static final String API_PUBLISHER = "api.ut.apiPublisher";
	public static final String DEFAULT_PROFILE = "default";
	public static final String TENANT = "tenant";
	public static final String APPLICATION_ID = "api.ut.application.id";
	
	StatDao statDao = new StatDao();

	public String getAPIId(String apiName, String apiVersion) throws Exception {
		String apiId = null;

		try {
			apiId = statDao.getAPIId(apiName, apiVersion);
		} catch (SQLException ex) {
			log.error("####STATINJECTION#### Error while retrieving api id", ex);
			throw ex;
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving api id", ex);
			throw ex;
		}

		return apiId;
	}

	public String getServiceProviderId(String serviceProvider) throws Exception {
		String publisherId = null;

		try {

			String serviceProviderUsername = getUsername(serviceProvider);

			publisherId = statDao.getServiceProviderId(serviceProviderUsername);
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving Service Provider Id", ex);
			throw ex;
		}

		return publisherId;
	}

	public String getServiceProviderUserId(String serviceProvider) {
		String spUserId = null;

		spUserId = getUsername(serviceProvider);

		return spUserId;
	}
	
	public String getAPIPublisherId (String apiPublisherUserName) throws Exception {
		String apiPublisherId = null;
		
		try {
			apiPublisherId = statDao.getAPIPublisherId(apiPublisherUserName);
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving API Publisher Id", ex);
			throw ex;
		}
		
		return apiPublisherId;
	}

	public String getOperatorName(String companyId) throws Exception {
		String operatorName = null;

		try {
			operatorName = statDao.getOperatorName(companyId);
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving Operator Name ", ex);
			throw ex;
		}

		return operatorName;
	}

	public String getServiceProviderConsumerKey(String applicationId) throws Exception {
		String consumerKey = null;

		try {
			consumerKey = statDao.getServiceProviderConsumerKey(applicationId);

		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving Service Provider Consumer Key ", ex);
			throw ex;
		}

		return consumerKey;
	}

	private String getUsername(String serviceProvider) {
		String splitRegex = "\\@";

		if (serviceProvider != null) {
			String[] serviceProviderComponents = serviceProvider.split(splitRegex);
			return serviceProviderComponents[0];
		}

		return null;
	}

}
