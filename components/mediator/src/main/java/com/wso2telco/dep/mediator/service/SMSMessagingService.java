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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.dep.mediator.dao.SMSMessagingDAO;
import com.wso2telco.dep.operatorservice.model.OperatorSubscriptionDTO;
import com.wso2telco.utils.exception.BusinessException;
import com.wso2telco.utils.exception.GenaralError;

public class SMSMessagingService {

	/** The Constant log. */
	private final Log log = LogFactory.getLog(SMSMessagingService.class);

	SMSMessagingDAO smsMessagingDAO;

	{
		smsMessagingDAO = new SMSMessagingDAO();
	}

	public Integer outboundSubscriptionEntry(String notifyURL, String serviceProvider) throws BusinessException {

		Integer newId = 0;

		try {

			newId = smsMessagingDAO.outboundSubscriptionEntry(notifyURL, serviceProvider);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return newId;
	}

	public boolean outboundOperatorsubsEntry(List<OperatorSubscriptionDTO> domainsubs, Integer dnSubscriptionId)
			throws BusinessException {

		try {

			smsMessagingDAO.outboundOperatorsubsEntry(domainsubs, dnSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return true;
	}

	public Map<String, String> getSMSRequestIds(String requestId, String senderAddress) throws BusinessException {

		Map<String, String> gatewayRequestIds = null;

		try {

			gatewayRequestIds = smsMessagingDAO.getSMSRequestIds(requestId, senderAddress);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return gatewayRequestIds;
	}

	public Integer subscriptionEntry(String notifyURL, String serviceProvider) throws BusinessException {

		Integer newId = 0;

		try {

			newId = smsMessagingDAO.subscriptionEntry(notifyURL, serviceProvider);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return newId;
	}

	public boolean operatorSubsEntry(List<OperatorSubscriptionDTO> domainsubs, Integer moSubscriptionId)
			throws BusinessException {

		try {

			smsMessagingDAO.operatorSubsEntry(domainsubs, moSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return true;
	}

	public List<OperatorSubscriptionDTO> subscriptionQuery(Integer moSubscriptionId) throws BusinessException {

		List<OperatorSubscriptionDTO> domainsubs = null;

		try {

			domainsubs = smsMessagingDAO.subscriptionQuery(moSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return domainsubs;
	}

	public boolean subscriptionDelete(Integer moSubscriptionId) throws BusinessException {

		try {

			smsMessagingDAO.subscriptionDelete(moSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return true;
	}

	public boolean insertSMSRequestIds(String requestId, String senderAddress, Map<String, String> gatewayRequestIds)
			throws BusinessException {

		try {

			smsMessagingDAO.insertSMSRequestIds(requestId, senderAddress, gatewayRequestIds);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return true;
	}

	public HashMap<String, String> subscriptionNotifiMap(Integer moSubscriptionId) throws BusinessException {

		HashMap<String, String> subscriptionDetails = null;

		try {

			subscriptionDetails = smsMessagingDAO.subscriptionNotifiMap(moSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return subscriptionDetails;
	}

	public HashMap<String, String> subscriptionDNNotifiMap(Integer dnSubscriptionId) throws BusinessException {

		HashMap<String, String> dnSubscriptionDetails = null;

		try {

			dnSubscriptionDetails = smsMessagingDAO.subscriptionDNNotifiMap(dnSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return dnSubscriptionDetails;
	}

	public List<OperatorSubscriptionDTO> outboudSubscriptionQuery(Integer dnSubscriptionId) throws BusinessException {

		List<OperatorSubscriptionDTO> domainsubs = null;

		try {

			domainsubs = smsMessagingDAO.outboudSubscriptionQuery(dnSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return domainsubs;
	}

	public boolean outboundSubscriptionDelete(Integer dnSubscriptionId) throws BusinessException {

		try {

			smsMessagingDAO.outboundSubscriptionDelete(dnSubscriptionId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return true;
	}
}
