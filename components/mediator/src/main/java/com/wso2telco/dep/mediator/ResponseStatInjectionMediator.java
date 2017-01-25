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
import org.wso2.carbon.user.api.UserStoreException;

import com.wso2telco.dep.mediator.internal.StatInformationUtil;
import com.wso2telco.dep.mediator.util.StatisticConstants;

public class ResponseStatInjectionMediator extends AbstractMediator {

	private final Log log = LogFactory.getLog(ResponseStatInjectionMediator.class);

	@Override
	public boolean mediate(MessageContext messageContext) {
		setApiId(messageContext);
		setServiceProviderId(messageContext);
		setServiceProviderUserId(messageContext);
		setCompanyId(messageContext);
		setDepartment(messageContext);
		setOperatorName(messageContext);
		setServiceProviderConsumerKey(messageContext);
		return true;
	}

	private void setApiId(MessageContext messageContext) {
		try {
			String apiId = StatInformationUtil.getAPIId(messageContext);
			messageContext.setProperty(StatisticConstants.API_ID, apiId);
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving api id", ex);
		}

	}

	private void setServiceProviderId(MessageContext messageContext) {
		try {
			String publisherId = StatInformationUtil.getServiceProviderId(messageContext);
			messageContext.setProperty(StatisticConstants.SERVICE_PROVIDER_ID, publisherId);
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving service provider id", ex);
		}
	}

	private void setServiceProviderUserId(MessageContext messageContext) {
		String spUserId = StatInformationUtil.getServiceProviderUserId(messageContext);
		messageContext.setProperty(StatisticConstants.SERVICE_PROVIDER_USER_ID, spUserId);
	}

	private void setCompanyId(MessageContext messageContext) {
		String companyId;
		try {
			companyId = StatInformationUtil.getUserClaimValue(messageContext, StatisticConstants.COMPANYID_CLAIM);
			messageContext.setProperty(StatisticConstants.COMPANYID, companyId);
		} catch (UserStoreException ex) {
			log.error("####STATINJECTION#### Error while retrieving company Id", ex);
		}

	}

	private void setDepartment(MessageContext messageContext) {
		String department;

		try {
			department = StatInformationUtil.getUserClaimValue(messageContext, StatisticConstants.DEPARTMENT_CLAIM);
			messageContext.setProperty(StatisticConstants.DEPARTMENT, department);
		} catch (UserStoreException ex) {
			log.error("####STATINJECTION#### Error while retrieving department", ex);
		}
	}

	private void setOperatorName(MessageContext messageContext) {

		try {
			String operatorName = StatInformationUtil.getOperatorName(messageContext);
			messageContext.setProperty(StatisticConstants.OPERATOR_NAME, operatorName);
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving operator Name", ex);
		}
	}
	
	private void setServiceProviderConsumerKey (MessageContext messageContext) {
		try {
			String consumerKey = StatInformationUtil.getServiceProviderConsumerKey(messageContext);
			messageContext.setProperty(StatisticConstants.SERVICE_PROVIDER_CONSUMER_KEY, consumerKey);
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving Service Provider Consumer Key", ex);
		}
	}

}
