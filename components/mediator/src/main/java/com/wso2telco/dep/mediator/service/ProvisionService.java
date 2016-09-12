/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.mediator.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.dep.mediator.dao.ProvisionDAO;
import com.wso2telco.dep.mediator.util.ErrorType;
import com.wso2telco.dep.operatorservice.util.OparatorError;

public class ProvisionService {

	/** The Constant log. */
	private final Log log = LogFactory.getLog(ProvisionService.class);

	ProvisionDAO provisionDAO;

	{
		provisionDAO = new ProvisionDAO();
	}

	public Integer provisionServiceEntry(String notifyURL, String serviceProvider) throws BusinessException {

		if (notifyURL == null || notifyURL.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_NOTIFY_URL);
		}

		if (serviceProvider == null || serviceProvider.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_SUBSCRIBER_NAME);
		}

		Integer newId = 0;

		try {

			newId = provisionDAO.provisionServiceEntry(notifyURL, serviceProvider);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return newId;
	}

	public void provisionServiceOperatorEntry(String operatorEndpoint, Integer provisionServiceId, String operatorName)
			throws BusinessException {

		if (operatorEndpoint == null || operatorEndpoint.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_OPERATOR_ENDPOINT);
		}

		if (provisionServiceId == null || provisionServiceId <= 0) {

			throw new BusinessException(ErrorType.INVALID_PROVISION_SERVICE_ID);
		}

		if (operatorName == null || operatorName.trim().length() <= 0) {

			throw new BusinessException(OparatorError.INVALID_OPARATOR_NAME);
		}

		try {

			provisionDAO.provisionServiceOperatorEntry(operatorEndpoint, provisionServiceId, operatorName);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}
}
