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

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.dep.mediator.dao.USSDDAO;
import com.wso2telco.dep.mediator.util.ErrorType;
import com.wso2telco.dep.operatorservice.model.OperatorSubscriptionDTO;
import com.wso2telco.dep.operatorservice.util.OparatorError;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import edu.emory.mathcs.backport.java.util.Collections;

public class USSDService {

	/** The Constant log. */
	private final Log log = LogFactory.getLog(USSDService.class);

	USSDDAO ussdDAO;

	{
		ussdDAO = new USSDDAO();
	}

	public Integer ussdRequestEntry(String notifyURL, String consumerKey, String operatorId, String userId) throws Exception {
		
		if (notifyURL == null || notifyURL.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_NOTIFY_URL);
		}
		if (consumerKey == null || consumerKey.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_CONSUMER_KEY);
		}
	/**	if (operatorId == null || operatorId.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_OPERATOR_ID);
		}**/
		if (userId == null || userId.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_USER_ID);
		}

		Integer newId = 0;

		try {

			newId = ussdDAO.ussdRequestEntry(notifyURL,consumerKey, operatorId, userId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return newId;
	}

	//public String getUSSDNotifyURL(Integer subscriptionId) throws BusinessException {
	public List<String> getUSSDNotify(Integer subscriptionId) throws Exception {
		
		if (subscriptionId == null || subscriptionId <= 0) {

			throw new BusinessException(ErrorType.INVALID_USSD_REQUEST_DID);
		}

		//String notifyurls = "";
		List<String> notifyurls = new ArrayList<String>();

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
	
	public void moUssdSubscriptionEntry(List<OperatorSubscriptionDTO> domainsubs, Integer moSubscriptionId) throws BusinessException { 
		
		if (domainsubs == null || domainsubs.size() <= 0) {

			throw new BusinessException(OparatorError.INVALID_OPERATOR_SUBSCRIPTION_LIST);
		}

		if (moSubscriptionId == null || moSubscriptionId <= 0) {

			throw new BusinessException(ErrorType.INVALID_MO_SUBSCRIPTION_ID);
		}
		
		try {

			ussdDAO.moUssdSubscriptionEntry(domainsubs, moSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

	}
		
	public List<OperatorSubscriptionDTO> moUssdSubscriptionQuery(Integer moSubscriptionId) throws Exception {
		
		if (moSubscriptionId == null || moSubscriptionId <= 0) {

			throw new BusinessException(ErrorType.INVALID_MO_SUBSCRIPTION_ID);
		}

		List<OperatorSubscriptionDTO> domainsubs = null;

		try {

			domainsubs = ussdDAO.moUssdSubscriptionQuery(moSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		if (domainsubs != null) {

			return domainsubs;
		} else {

			return Collections.emptyList();
		}
		
	}
		
	public void moUssdSubscriptionDelete(Integer moSubscriptionId) throws BusinessException {		

			if (moSubscriptionId == null || moSubscriptionId <= 0) {

				throw new BusinessException(ErrorType.INVALID_MO_SUBSCRIPTION_ID);
			}

			try {

				ussdDAO.moUssdSubscriptionDelete(moSubscriptionId);
			} catch (Exception e) {

				throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
			}
		}
	
	public String getOperatorIdByOperator(String operator) throws BusinessException {
		
		
		
		if (operator == null || operator.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_OPERATOR);
		}

		String operatorId="";
		
		try {

			operatorId = ussdDAO.getOperatorIdByOperator(operator);
			
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}
		return operatorId;
		
		
	}    
		
	public void updateOperatorIdBySubscriptionId(Integer subscriptionId, String operatorId) throws BusinessException {
		
		if (subscriptionId == null || subscriptionId <= 0) {

			throw new BusinessException(ErrorType.INVALID_USSD_REQUEST_DID);
		}
		if (operatorId == null || operatorId.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_OPERATOR_ID);
		}
		try {

			ussdDAO.updateOperatorIdBySubscriptionId(subscriptionId, operatorId);

		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

	}
		
		
}
