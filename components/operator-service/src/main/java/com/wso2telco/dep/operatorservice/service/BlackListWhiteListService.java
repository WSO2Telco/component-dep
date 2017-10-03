/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.dep.operatorservice.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.core.msisdnvalidator.MSISDN;
import com.wso2telco.core.msisdnvalidator.MSISDNUtil;
import com.wso2telco.dep.operatorservice.dao.BlackListWhiteListDAO;
import com.wso2telco.dep.operatorservice.exception.NumberBlackListException;
import com.wso2telco.dep.operatorservice.exception.SubscriptionWhiteListException;
import com.wso2telco.dep.operatorservice.exception.SubscriptionWhiteListException.SubscriptionWhiteListErrorType;
import com.wso2telco.dep.operatorservice.model.BlackListDTO;
import com.wso2telco.dep.operatorservice.model.MSISDNSearchDTO;
import com.wso2telco.dep.operatorservice.model.WhiteListDTO;
import com.wso2telco.dep.operatorservice.model.WhiteListMSISDNSearchDTO;
import org.wso2.carbon.apimgt.api.dto.UserApplicationAPIUsage;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.api.model.SubscribedAPI;

public class BlackListWhiteListService {
	Log LOG = LogFactory.getLog(BlackListWhiteListService.class);
	private BlackListWhiteListDAO dao;

	private MSISDNUtil phoneNumberValidationUtil_;

	{
		dao = new BlackListWhiteListDAO();
		phoneNumberValidationUtil_ = new MSISDNUtil();
	}

	/**
	 * validate the MSISDN s and black list . The MSISDN need to provided with +
	 * sign if not all the process fails. The entire process run as a single
	 * transaction .
	 */
	public void blacklist(BlackListDTO dto) throws BusinessException {

		String[] msisdns = dto.getUserMSISDN();
		List<MSISDN> numberA = new ArrayList<MSISDN>();
		MSISDNSearchDTO mSISDNSearchDTO = new MSISDNSearchDTO();
		try {
			for (String msisdn : msisdns) {

				//The number format should be telco:phonenumber
				int charIndex = msisdn.indexOf('+');
				String prefix = msisdn.substring(0, charIndex);
				msisdn = msisdn.substring(msisdn.indexOf('+'));
				MSISDN msisdnDTO = phoneNumberValidationUtil_.parse(msisdn);
				msisdnDTO.setPrefix(prefix);
				numberA.add(msisdnDTO);
				mSISDNSearchDTO.addMSISDN2Search(msisdnDTO);
			}

			final String apiID_ = dto.getApiID();
			final String apiName_ = dto.getApiName();
			final String userId_ = dto.getUserID();

			// load already black listed numbers
			mSISDNSearchDTO.setApiID(apiID_);
			List<MSISDN> alreadyBlacklisted;

			alreadyBlacklisted = dao.loadAlreadyBlacklisted(mSISDNSearchDTO);

			// Remove already black listed from the list
			for (MSISDN msisdn : alreadyBlacklisted) {

				numberA.remove(msisdn);
			}

			if (numberA.isEmpty()) {
				LOG.debug(" All the numbers already black listed");
				return;
			}
			dao.blacklist(numberA, apiID_, apiName_, userId_);
		} catch (Exception e) {
			LOG.error("blacklist ", e);
			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	public String[] loadBlacklisted(MSISDNSearchDTO searchDTO) throws BusinessException {
		try {
			return dao.getBlacklisted(searchDTO);
		} catch (Exception e) {
			LOG.error("loadBlacklisted", e);
			throw new NumberBlackListException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	public void removeBlacklist(int apiId, final String userMSISDN) throws BusinessException {
		try {
			dao.removeBlacklist(apiId, userMSISDN);
		} catch (Exception e) {
			LOG.error("removeBlacklist", e);
			throw new NumberBlackListException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * remove already white listed msisdns from the list and white list the
	 * subscription
	 *
	 * @param whiteListDTO
	 * @throws BusinessException
	 */
	public void whiteListSubscription(WhiteListDTO whiteListDTO) throws Exception {
		String[] msisdns = whiteListDTO.getUserMSISDN();
		List<MSISDN> msisdnArrayList = new ArrayList<MSISDN>();
		WhiteListMSISDNSearchDTO mSISDNSearchDTO = new WhiteListMSISDNSearchDTO();

		for (String msisdn : msisdns) {

			//The number format should be telco:phonenumber
			int charIndex = msisdn.indexOf('+');
			String prefix = msisdn.substring(0, charIndex);
			msisdn = msisdn.substring(msisdn.indexOf('+'));
			MSISDN msisdnDTO = phoneNumberValidationUtil_.parse(msisdn);
			//msisdnDTO = phoneNumberValidationUtil_.parse(msisdn);
			msisdnDTO.setPrefix(prefix);
			msisdnArrayList.add(msisdnDTO);
			mSISDNSearchDTO.addMSISDN2Search(msisdnDTO);
		}

		String subscriptionID = whiteListDTO.getSubscriptionID();
		// if no subscription provided
		// subscription id derived using applicationid and using api id
		if(subscriptionID == null){
			//String[] apiArray = whiteListDTO.getApiID().split("[:]");
			String apiId = whiteListDTO.getApiID();
			subscriptionID = String.valueOf(dao.findSubscriptionId(whiteListDTO.getApplicationID(), whiteListDTO.getApiID()));

			if (subscriptionID.trim().length() <= 0) {
				throw new SubscriptionWhiteListException(SubscriptionWhiteListErrorType.NULL_SUBSCRIPTION);
			}
		}

		final String apiID = whiteListDTO.getApiID();
		final String applicationID =  whiteListDTO.getApplicationID();


		// check the input stream for already white listed numbers
		mSISDNSearchDTO.setApiID(apiID);
		List<MSISDN> alreadyWhilteListed;
		try {
			alreadyWhilteListed = dao.loadSubscriptionsForAlreadyWhiteListedMSISDN(subscriptionID);
			for (MSISDN msisdn : alreadyWhilteListed) {
				msisdnArrayList.remove(msisdn);
			}
		} catch (SQLException e) {
			LOG.error("whiteListSubscription, calling dao.loadSubscriptionsForAlreadyWhiteListedMSISDN(", e);
			throw new SubscriptionWhiteListException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		// All numbers already white listed then throw exception
		if (msisdnArrayList.isEmpty()) {
			LOG.debug(" All the numbers already black listed");
			throw new SubscriptionWhiteListException(SubscriptionWhiteListErrorType.SUBSCRIPTION_ALREADY_WHITELISTED);
		}
		// persistence goes hare
		try {
			dao.whitelist(msisdnArrayList, subscriptionID, apiID, applicationID);

		} catch (Exception e) {
			LOG.error("whiteListSubscription. caling dao.whitelist", e);
			throw new SubscriptionWhiteListException(GenaralError.INTERNAL_SERVER_ERROR);
		}

	}

	public void removeWhitelistNumber(String userMSISDN) throws BusinessException {

		try {
			dao.removeWhitelistNumber(userMSISDN);
		} catch (Exception e) {
			LOG.error("removeWhitelistNumber", e);
			throw new SubscriptionWhiteListException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	public String[] getWhiteListNumbers() throws BusinessException {
		try {
			List<String> result = dao.getWhiteListNumbers();
			if (result == null || result.isEmpty()) {
				return null;
			}
			return result.toArray(new String[result.size()]);
		} catch (Exception e) {
			LOG.error("getWhiteListNumbers", e);
			throw new SubscriptionWhiteListException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	public  String getAllSubscribers() throws BusinessException {

		Map<String, UserApplicationAPIUsage> userApplicationAPIUsageMap = dao.getAllAPIUsageByProvider();
		List<UserApplicationAPIUsage> userApplicationAPIUsageArrayList = new ArrayList<UserApplicationAPIUsage>(userApplicationAPIUsageMap.values());

		List<String> subsscriberList = new ArrayList<String>();
		for (UserApplicationAPIUsage userApplicationAPIUsage : userApplicationAPIUsageArrayList){
			if(!subsscriberList.contains(userApplicationAPIUsage.getUserId())){
				subsscriberList.add(userApplicationAPIUsage.getUserId());
			}
		}
		Gson gson = new GsonBuilder().create();
		//return gson.toJson(userApplicationAPIUsageArrayList);
		return gson.toJson(subsscriberList);
	}

	public  String getAllSubscribedUsers() throws BusinessException {

		List<String> subscriberList = dao.getAllAPIUsageByUser();

		Gson gson = new GsonBuilder().create();
		return gson.toJson(subscriberList);
	}


	public  String getAllApplicationsByUser(String userID) throws BusinessException {

		List<String> appUniqueIDList = dao.getAllAplicationsByUser(userID);

		Gson gson = new GsonBuilder().create();
		return gson.toJson(appUniqueIDList);
	}

	public  String getAllApplicationsByUserAndOperator(String userID, String operator) throws BusinessException {

		List<String> appUniqueIDList = dao.getAllAplicationsByUserAndOperator(userID, operator);

		Gson gson = new GsonBuilder().create();
		return gson.toJson(appUniqueIDList);
	}

	public  String getAllApisByUserAndApp(String userID, String appID) throws BusinessException {

		List<String> appList = dao.getAllApisByUserAndApp(userID, appID);

		Gson gson = new GsonBuilder().create();
		return gson.toJson(appList);
	}


	public String getAllAPIs() throws BusinessException {

		Map<String, UserApplicationAPIUsage> userApplicationAPIUsageMap = dao.getAllAPIUsageByProvider();
		List<UserApplicationAPIUsage> userApplicationAPIUsageArrayList = new ArrayList<UserApplicationAPIUsage>(userApplicationAPIUsageMap.values());

		List<String> apiNameList = new ArrayList<String>();
		for (UserApplicationAPIUsage userApplicationAPIUsage : userApplicationAPIUsageArrayList){
			SubscribedAPI[] subscribedAPIS =  userApplicationAPIUsage.getApiSubscriptions();

			for (SubscribedAPI subscribedAPI:subscribedAPIS){

				APIIdentifier apiIdentifier = subscribedAPI.getApiId();
				String[] apiName = apiIdentifier.getApiName().split("[|]");
				String apiFullName = apiIdentifier.getProviderName()+":"+apiName[0]+":"+apiIdentifier
						.getVersion()+ ":" + apiName[1];

				if(!apiNameList.contains(apiFullName)){
					apiNameList.add(apiFullName);
				}
			}
		}

		Gson gson = new GsonBuilder().create();
		//return gson.toJson(userApplicationAPIUsageArrayList);
		return gson.toJson(apiNameList);
	}


	public int getAPIId(String providerName, String apiName, String apiVersion) throws BusinessException {

		return  dao.getAPIId(providerName,apiName,apiVersion);
	}

	public String[] getAPIInfo(int apiId) throws BusinessException {
		return dao.getAPIInfo(apiId);
	}
}
