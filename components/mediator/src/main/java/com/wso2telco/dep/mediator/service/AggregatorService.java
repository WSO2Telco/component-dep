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
import com.wso2telco.dep.mediator.dao.AggregatorDAO;
import com.wso2telco.dep.mediator.util.ErrorType;
import com.wso2telco.dep.operatorservice.exception.ApplicationException;
import com.wso2telco.dep.operatorservice.exception.ApplicationException.ApplicationErrorType;
import com.wso2telco.dep.operatorservice.util.OparatorError;

public class AggregatorService {

	/** The Constant log. */
	private final Log log = LogFactory.getLog(AggregatorService.class);

	AggregatorDAO aggregatorDAO;

	{
		aggregatorDAO = new AggregatorDAO();
	}

	public String blacklistedmerchant(int appId, String operatorId, String subscriber, String merchant)
			throws ApplicationException, BusinessException {

		if (appId <= 0) {

			throw new ApplicationException(ApplicationErrorType.INVALID_APPLICATION_ID);
		}

		if (operatorId == null || operatorId.trim().length() <= 0) {

			throw new BusinessException(OparatorError.INVALID_OPARATOR_ID);
		}

		if (subscriber == null || subscriber.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_SUBSCRIBER_NAME);
		}

		if (merchant == null || merchant.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_MERCHANT_NAME);
		}

		String resultcode = null;

		try {

			resultcode = aggregatorDAO.blacklistedmerchant(appId, operatorId, subscriber, merchant);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return resultcode;
	}

	public String blacklistedmerchant(String operatorId, String subscriber, String merchant) throws BusinessException {

		if (operatorId == null || operatorId.trim().length() <= 0) {

			throw new BusinessException(OparatorError.INVALID_OPARATOR_ID);
		}

		if (subscriber == null || subscriber.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_SUBSCRIBER_NAME);
		}

		if (merchant == null || merchant.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_MERCHANT_NAME);
		}

		String resultcode = null;

		try {

			resultcode = aggregatorDAO.blacklistedmerchant(operatorId, subscriber, merchant);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return resultcode;
	}
}
