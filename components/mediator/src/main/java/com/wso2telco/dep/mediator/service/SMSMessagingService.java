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

import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.dep.mediator.dao.SMSMessagingDAO;
import com.wso2telco.dep.mediator.util.ErrorType;
import com.wso2telco.dep.operatorservice.model.OperatorSubscriptionDTO;
import com.wso2telco.dep.operatorservice.util.OparatorError;
import com.wso2telco.utils.exception.BusinessException;
import com.wso2telco.utils.exception.GenaralError;

import edu.emory.mathcs.backport.java.util.Collections;

public class SMSMessagingService {

	/** The Constant log. */
	private final Log log = LogFactory.getLog(SMSMessagingService.class);

	SMSMessagingDAO smsMessagingDAO;

	{
		smsMessagingDAO = new SMSMessagingDAO();
	}

	public Integer outboundSubscriptionEntry(String notifyURL, String serviceProvider) throws BusinessException {

		if (notifyURL == null || notifyURL.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_NOTIFY_URL);
		}

		if (serviceProvider == null || serviceProvider.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_SUBSCRIBER_NAME);
		}

		Integer newId = 0;

		try {

			newId = smsMessagingDAO.outboundSubscriptionEntry(notifyURL, serviceProvider);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return newId;
	}

	public void outboundOperatorsubsEntry(List<OperatorSubscriptionDTO> domainsubs, Integer dnSubscriptionId)
			throws BusinessException {

		if (dnSubscriptionId == null || dnSubscriptionId <= 0) {

			throw new BusinessException(ErrorType.INVALID_DN_SUBSCRIPTION_ID);
		}

		if (domainsubs == null || domainsubs.size() <= 0) {

			throw new BusinessException(OparatorError.INVALID_OPERATOR_SUBSCRIPTION_LIST);
		}

		try {

			smsMessagingDAO.outboundOperatorsubsEntry(domainsubs, dnSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getSMSRequestIds(String requestId, String senderAddress) throws BusinessException {

		if (requestId == null || requestId.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_REQUEST_ID);
		}

		if (senderAddress == null || senderAddress.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_SENDER_ADDRESS);
		}

		Map<String, String> gatewayRequestIds = null;

		try {

			gatewayRequestIds = smsMessagingDAO.getSMSRequestIds(requestId, senderAddress);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		if (gatewayRequestIds != null) {

			return gatewayRequestIds;
		} else {

			return Collections.emptyMap();
		}
	}

	public Integer subscriptionEntry(String notifyURL, String serviceProvider) throws BusinessException {

		if (notifyURL == null || notifyURL.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_NOTIFY_URL);
		}

		if (serviceProvider == null || serviceProvider.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_SUBSCRIBER_NAME);
		}

		Integer newId = 0;

		try {

			newId = smsMessagingDAO.subscriptionEntry(notifyURL, serviceProvider);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return newId;
	}

	public void operatorSubsEntry(List<OperatorSubscriptionDTO> domainsubs, Integer moSubscriptionId)
			throws BusinessException {

		if (moSubscriptionId == null || moSubscriptionId <= 0) {

			throw new BusinessException(ErrorType.INVALID_MO_SUBSCRIPTION_ID);
		}

		if (domainsubs == null || domainsubs.size() <= 0) {

			throw new BusinessException(OparatorError.INVALID_OPERATOR_SUBSCRIPTION_LIST);
		}

		try {

			smsMessagingDAO.operatorSubsEntry(domainsubs, moSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("unchecked")
	public List<OperatorSubscriptionDTO> subscriptionQuery(Integer moSubscriptionId) throws BusinessException {

		if (moSubscriptionId == null || moSubscriptionId <= 0) {

			throw new BusinessException(ErrorType.INVALID_MO_SUBSCRIPTION_ID);
		}

		List<OperatorSubscriptionDTO> domainsubs = null;

		try {

			domainsubs = smsMessagingDAO.subscriptionQuery(moSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		if (domainsubs != null) {

			return domainsubs;
		} else {

			return Collections.emptyList();
		}
	}

	public void subscriptionDelete(Integer moSubscriptionId) throws BusinessException {

		if (moSubscriptionId == null || moSubscriptionId <= 0) {

			throw new BusinessException(ErrorType.INVALID_MO_SUBSCRIPTION_ID);
		}

		try {

			smsMessagingDAO.subscriptionDelete(moSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	public void insertSMSRequestIds(String requestId, String senderAddress, Map<String, String> gatewayRequestIds)
			throws BusinessException {

		if (requestId == null || requestId.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_REQUEST_ID);
		}

		if (senderAddress == null || senderAddress.trim().length() <= 0) {

			throw new BusinessException(ErrorType.INVALID_SENDER_ADDRESS);
		}

		if (gatewayRequestIds == null || gatewayRequestIds.size() <= 0) {

			throw new BusinessException(ErrorType.INVALID_REQUEST_ID_LIST);
		}

		try {

			smsMessagingDAO.insertSMSRequestIds(requestId, senderAddress, gatewayRequestIds);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> subscriptionNotifiMap(Integer moSubscriptionId) throws BusinessException {

		if (moSubscriptionId == null || moSubscriptionId <= 0) {

			throw new BusinessException(ErrorType.INVALID_MO_SUBSCRIPTION_ID);
		}

		Map<String, String> subscriptionDetails = null;

		try {

			subscriptionDetails = smsMessagingDAO.subscriptionNotifiMap(moSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		if (subscriptionDetails != null) {

			return subscriptionDetails;
		} else {

			return Collections.emptyMap();
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> subscriptionDNNotifiMap(Integer dnSubscriptionId) throws BusinessException {

		if (dnSubscriptionId == null || dnSubscriptionId <= 0) {

			throw new BusinessException(ErrorType.INVALID_DN_SUBSCRIPTION_ID);
		}

		Map<String, String> dnSubscriptionDetails = null;

		try {

			dnSubscriptionDetails = smsMessagingDAO.subscriptionDNNotifiMap(dnSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		if (dnSubscriptionDetails != null) {

			return dnSubscriptionDetails;
		} else {

			return Collections.emptyMap();
		}
	}

	@SuppressWarnings("unchecked")
	public List<OperatorSubscriptionDTO> outboudSubscriptionQuery(Integer dnSubscriptionId) throws BusinessException {

		if (dnSubscriptionId == null || dnSubscriptionId <= 0) {

			throw new BusinessException(ErrorType.INVALID_DN_SUBSCRIPTION_ID);
		}

		List<OperatorSubscriptionDTO> domainsubs = null;

		try {

			domainsubs = smsMessagingDAO.outboudSubscriptionQuery(dnSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		if (domainsubs != null) {

			return domainsubs;
		} else {

			return Collections.emptyList();
		}
	}

	public void outboundSubscriptionDelete(Integer dnSubscriptionId) throws BusinessException {

		if (dnSubscriptionId == null || dnSubscriptionId <= 0) {

			throw new BusinessException(ErrorType.INVALID_DN_SUBSCRIPTION_ID);
		}

		try {

			smsMessagingDAO.outboundSubscriptionDelete(dnSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}
}
