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
package com.wso2telco.dep.mediator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.rest.RESTConstants;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.registry.core.internal.RegistryCoreServiceComponent;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;

import com.wso2telco.dep.mediator.internal.StatInformationUtil;
import com.wso2telco.dep.mediator.util.StatisticConstants;

public class ResponseStatInjectionMediator extends AbstractMediator {

	private final Log log = LogFactory.getLog(ResponseStatInjectionMediator.class);

	StatInformationUtil statInformationUtil = new StatInformationUtil();

	@Override
	public boolean mediate(MessageContext messageContext) {
		setApiId(messageContext);
		setServiceProviderId(messageContext);
		setServiceProviderUserId(messageContext);
		setCompanyId(messageContext);
		setDepartment(messageContext);
		setOperatorName(messageContext);
		setServiceProviderConsumerKey(messageContext);
		setApiPublisherId(messageContext);
		return true;
	}

	private void setApiId(MessageContext messageContext) {
		try {

			String apiName = (String) messageContext.getProperty(StatInformationUtil.API);
			String apiVersion = (String) messageContext.getProperty(RESTConstants.SYNAPSE_REST_API_VERSION);

			String apiId = statInformationUtil.getAPIId(apiName, apiVersion);
			messageContext.setProperty(StatisticConstants.API_ID, apiId);
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving api id", ex);
		}

	}

	private void setServiceProviderId(MessageContext messageContext) {
		try {
			String publisherId = statInformationUtil.getServiceProviderId(serviceProvider);
			messageContext.setProperty(StatisticConstants.SERVICE_PROVIDER_ID, publisherId);

		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving service provider id", ex);
		}
	}

	private void setServiceProviderUserId(MessageContext messageContext) {

		String serviceProvider = (String) messageContext.getProperty(StatInformationUtil.SERVICE_PROVIDER_USERNAME);

		String spUserId = statInformationUtil.getServiceProviderUserId(serviceProvider);
		messageContext.setProperty(StatisticConstants.SERVICE_PROVIDER_USER_ID, spUserId);
	}

	private void setCompanyId(MessageContext messageContext) {
		String companyId;
		try {
			companyId = this.getUserClaimValue(messageContext, StatisticConstants.OPERATOR_CLAIM);
			messageContext.setProperty(StatisticConstants.OPERATORID, companyId);
		} catch (UserStoreException ex) {
			log.error("####STATINJECTION#### Error while retrieving company Id", ex);
		}

	}
	
	private void setApiPublisherId (MessageContext messageContext) {
		String apiPublisherId;
		
		try {
			
			String apiPublisher = (String) messageContext.getProperty(StatInformationUtil.API_PUBLISHER);
			
			apiPublisherId = statInformationUtil.getAPIPublisherId(apiPublisher);
			messageContext.setProperty(StatisticConstants.API_PUBLISHER_ID, apiPublisherId);
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving api publisher id", ex);
		}
	}

	private void setDepartment(MessageContext messageContext) {
		String department;

		try {
			department = this.getUserClaimValue(messageContext, StatisticConstants.DEPARTMENT_CLAIM);
			messageContext.setProperty(StatisticConstants.DEPARTMENT, department);
		} catch (UserStoreException ex) {
			log.error("####STATINJECTION#### Error while retrieving department", ex);
		}
	}

	private void setOperatorName(MessageContext messageContext) {

		try {

			String companyId = (String) messageContext.getProperty(StatisticConstants.OPERATORID);

			String operatorName = statInformationUtil.getOperatorName(companyId);
			messageContext.setProperty(StatisticConstants.OPERATOR_NAME, operatorName);
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving operator Name", ex);
		}
	}

	private void setServiceProviderConsumerKey(MessageContext messageContext) {
		try {

			String applicationId = (String) messageContext.getProperty(StatInformationUtil.APPLICATION_ID);

			String consumerKey = statInformationUtil.getServiceProviderConsumerKey(applicationId);
			messageContext.setProperty(StatisticConstants.SERVICE_PROVIDER_CONSUMER_KEY, consumerKey);
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving Service Provider Consumer Key", ex);
		}
	}

	private UserStoreManager getUserStoreManager(MessageContext context) throws UserStoreException {
		RealmService realmService = RegistryCoreServiceComponent.getRealmService();

		String tenantDomain = (String) context.getProperty(StatInformationUtil.TENANT);

		if (getNullOrTrimmedValue(tenantDomain) == null) {
			String userId = (String) context.getProperty(StatInformationUtil.SERVICE_PROVIDER_USERNAME);
			tenantDomain = getTenantDomain(userId);
		}

		int tenantId = realmService.getTenantManager().getTenantId(tenantDomain);
		UserRealm userRealm = realmService.getTenantUserRealm(tenantId);
		UserStoreManager userStoreManager = userRealm.getUserStoreManager();

		return userStoreManager;

	}

	public String getUserClaimValue(MessageContext context, String claim) throws UserStoreException {

		UserStoreManager userStoreManager;
		String claimValue = null;
		try {
			String apiPublisher = (String) context.getProperty(StatInformationUtil.API_PUBLISHER);

			userStoreManager = getUserStoreManager(context);
			claimValue = userStoreManager.getUserClaimValue(apiPublisher, claim, StatInformationUtil.DEFAULT_PROFILE);

		} catch (UserStoreException ex) {
			log.error("####STATINJECTION#### Error while retrieving user claim " + claim, ex);
			throw ex;
		} finally {
			PrivilegedCarbonContext.endTenantFlow();
		}

		return claimValue;

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
