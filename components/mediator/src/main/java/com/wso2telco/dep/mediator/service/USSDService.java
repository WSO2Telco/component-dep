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
import com.wso2telco.dep.mediator.dao.USSDDAO;
import com.wso2telco.dep.mediator.util.ErrorType;
import com.wso2telco.utils.exception.BusinessException;
import com.wso2telco.utils.exception.GenaralError;

public class USSDService {

	/** The Constant log. */
	private final Log log = LogFactory.getLog(USSDService.class);

	USSDDAO ussdDAO;

	{
		ussdDAO = new USSDDAO();
	}

	public Integer ussdRequestEntry(String notifyURL) throws BusinessException {

		if (notifyURL == null || notifyURL.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_NOTIFY_URL);
		}

		Integer newId = 0;

		try {

			newId = ussdDAO.ussdRequestEntry(notifyURL);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return newId;
	}

	public String getUSSDNotifyURL(Integer subscriptionId) throws BusinessException {

		if (subscriptionId == null || subscriptionId <= 0) {

			throw new BusinessException(ErrorType.INVALID_USSD_REQUEST_DID);
		}

		String notifyurls = "";

		try {

			notifyurls = ussdDAO.getUSSDNotifyURL(subscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return notifyurls;
	}

	public boolean ussdEntryDelete(Integer subscriptionId) throws BusinessException {

		if (subscriptionId == null || subscriptionId <= 0) {

			throw new BusinessException(ErrorType.INVALID_USSD_REQUEST_DID);
		}

		try {

			ussdDAO.ussdEntryDelete(subscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return true;
	}
}
