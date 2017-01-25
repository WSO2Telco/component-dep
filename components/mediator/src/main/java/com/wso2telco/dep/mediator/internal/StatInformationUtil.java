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
import org.apache.synapse.MessageContext;
import org.apache.synapse.rest.RESTConstants;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.core.internal.CarbonCoreServiceComponent;
import org.wso2.carbon.registry.core.internal.RegistryCoreServiceComponent;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserRealmService;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.jdbc.JDBCUserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;

import com.wso2telco.dep.mediator.dao.StatDao;
import com.wso2telco.dep.mediator.util.StatisticConstants;

public class StatInformationUtil {

	private static final Log log = LogFactory.getLog(StatInformationUtil.class);

	public static final String API = "api.ut.api";
	public static final String SERVICE_PROVIDER_USERNAME = "api.ut.userId";
	public static final String API_PUBLISHER = "api.ut.apiPublisher";
	public static final String DEFAULT_PROFILE = "default";
	public static final String TENANT = "tenant";
	public static final String APPLICATION_ID = "api.ut.application.id";

	private static UserStoreManager getUserStoreManager(MessageContext context) throws UserStoreException {
		RealmService realmService = RegistryCoreServiceComponent.getRealmService();

		String tenantDomain = (String) context.getProperty(TENANT);

		if (getNullOrTrimmedValue(tenantDomain) == null) {
			String userId = (String) context.getProperty(SERVICE_PROVIDER_USERNAME);
			tenantDomain = getTenantDomain(userId);
		}

		int tenantId = realmService.getTenantManager().getTenantId(tenantDomain);
		UserRealm userRealm = realmService.getTenantUserRealm(tenantId);
		UserStoreManager userStoreManager = userRealm.getUserStoreManager();

		return userStoreManager;

	}

	public static String getAPIId(MessageContext context) throws Exception {
		String apiId = null;

		StatDao statDao = new StatDao();

		String apiName = (String) context.getProperty(API);
		String apiVersion = (String) context.getProperty(RESTConstants.SYNAPSE_REST_API_VERSION);

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

	public static String getServiceProviderId(MessageContext context) throws Exception {
		String publisherId = null;

		try {

			StatDao statDao = new StatDao();

			String serviceProvider = (String) context.getProperty(SERVICE_PROVIDER_USERNAME);

			String serviceProviderUsername = getUsername(serviceProvider);

			publisherId = statDao.getServiceProviderId(serviceProviderUsername);
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving Service Provider Id", ex);
			throw ex;
		}

		return publisherId;
	}
	
	public static String getServiceProviderUserId (MessageContext context) {
		String spUserId = null;
		
		String serviceProvider = (String) context.getProperty(SERVICE_PROVIDER_USERNAME);
		
		spUserId = getUsername(serviceProvider);
		
		return spUserId;
	}

	public static String getUserClaimValue(MessageContext context, String claim) throws UserStoreException {

		UserStoreManager userStoreManager;
		String claimValue = null;
		try {
			String apiPublisher = (String) context.getProperty(API_PUBLISHER);

			userStoreManager = getUserStoreManager(context);
			claimValue = userStoreManager.getUserClaimValue(apiPublisher, claim, DEFAULT_PROFILE);

		} catch (UserStoreException ex) {
			log.error("####STATINJECTION#### Error while retrieving user claim " + claim, ex);
			throw ex;
		} finally {
			PrivilegedCarbonContext.endTenantFlow();
		}

		return claimValue;

	}

	public static String getOperatorName(MessageContext context) throws Exception {
		String operatorName = null;

		try {
			StatDao statDao = new StatDao();
			
			String companyId = (String) context.getProperty(StatisticConstants.COMPANYID);

			operatorName = statDao.getOperatorName(companyId);
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving Operator Name " , ex);
			throw ex;
		}

		return operatorName;
	}
	
	public static String getServiceProviderConsumerKey (MessageContext context) throws Exception {
		String consumerKey = null;
		
		try {
			StatDao statDao = new StatDao();
			
			String applicationId = (String) context.getProperty(APPLICATION_ID);
			
			consumerKey = statDao.getServiceProviderConsumerKey(applicationId);
			
		} catch (Exception ex ) {
			log.error("####STATINJECTION#### Error while retrieving Service Provider Consumer Key " , ex);
			throw ex;
		}
		
		return consumerKey;
	}

	private static String getUsername(String serviceProvider) {
		String splitRegex = "\\@";

		if (serviceProvider != null) {
			String[] serviceProviderComponents = serviceProvider.split(splitRegex);
			return serviceProviderComponents[0];
		}

		return null;
	}

	private static String getTenantDomain(String userId) {
		String splitRegex = "\\@";
		return userId.split(splitRegex)[1];
	}

	private static String getNullOrTrimmedValue(String value) {
		String returnValue = null;

		if (value != null && value.trim().length() > 0) {
			returnValue = value.trim();
		}

		return returnValue;
	}

}
