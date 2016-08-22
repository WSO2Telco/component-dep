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
package com.wso2telco.dep.publisheventsdata.service;

import com.wso2telco.dep.publisheventsdata.util.ErrorType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.dep.publisheventsdata.dao.SpendLimitDAO;
import com.wso2telco.utils.exception.BusinessException;
import com.wso2telco.utils.exception.GenaralError;

public class SpendLimitService {

	/** The Constant log. */
	private final Log log = LogFactory.getLog(SpendLimitService.class);

	SpendLimitDAO spendLimitDAO;

	{
		spendLimitDAO = new SpendLimitDAO();
	}

	public boolean checkMSISDNSpendLimit(String msisdn) throws BusinessException {

		if (msisdn == null || msisdn.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_MSISDN);
		}

		boolean isExceeded = false;

		try {

			isExceeded = spendLimitDAO.checkMSISDNSpendLimit(msisdn);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return isExceeded;
	}

	public boolean checkApplicationSpendLimit(String consumerKey) throws BusinessException {

		if (consumerKey == null || consumerKey.trim().length() <= 0) {

			throw new BusinessException(ErrorType.CONSUMER_KEY);
		}

		boolean isExceeded = false;

		try {

			isExceeded = spendLimitDAO.checkApplicationSpendLimit(consumerKey);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return isExceeded;
	}

	public boolean checkOperatorSpendLimit(String operatorId) throws BusinessException {

		if (operatorId == null || operatorId.trim().length() <= 0) {

			throw new BusinessException(ErrorType.OPERATOR_ID);
		}

		boolean isExceeded = false;

		try {

			isExceeded = spendLimitDAO.checkOperatorSpendLimit(operatorId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return isExceeded;
	}
}
