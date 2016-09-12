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
package com.wso2telco.dep.publisheventsdata.handler;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.dep.publisheventsdata.internal.EventsDataHolder;
import com.wso2telco.dep.publisheventsdata.service.SpendLimitService;

import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class SpendLimitHandler.
 */
public class SpendLimitHandler {

	/** The is events enabled. */
	private final boolean isEventsEnabled;

	private SpendLimitService spendLimitService;

	/**
	 * Instantiates a new spend limit handler.
	 */
	public SpendLimitHandler() {

		Map<String, String> publisherEventConfMap = EventsDataHolder.getPublisherEventConfMap();
		String eventsEnabled = publisherEventConfMap.get("events.spend.limit.handler.enabled");
		isEventsEnabled = Boolean.parseBoolean(eventsEnabled);
		spendLimitService = new SpendLimitService();
	}

	public boolean isSpendLimitExceeded(String msisdn, String consumerKey, String operatorId) throws BusinessException {

		return isEventsEnabled && (isMSISDNSpendLimitExceeded(msisdn) || isApplicationSpendLimitExceeded(consumerKey)
				|| isOperatorSpendLimitExceeded(operatorId));
	}

	public boolean isMSISDNSpendLimitExceeded(String msisdn) throws BusinessException {

		if (isEventsEnabled) {

			return spendLimitService.checkMSISDNSpendLimit(msisdn);
		}

		return false;
	}

	public boolean isApplicationSpendLimitExceeded(String consumerKey) throws BusinessException {

		if (isEventsEnabled) {

			return spendLimitService.checkApplicationSpendLimit(consumerKey);
		}

		return false;
	}

	public boolean isOperatorSpendLimitExceeded(String operatorId) throws BusinessException {

		if (isEventsEnabled) {

			return spendLimitService.checkOperatorSpendLimit(operatorId);
		}

		return false;
	}
}
