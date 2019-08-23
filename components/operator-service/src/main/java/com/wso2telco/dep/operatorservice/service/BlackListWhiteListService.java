/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.dep.operatorservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.dep.oneapivalidation.util.MsisdnDTO;
import com.wso2telco.dep.operatorservice.dao.BlackListWhiteListDAO;
import com.wso2telco.dep.operatorservice.exception.NumberBlackListException;
import com.wso2telco.dep.operatorservice.exception.SubscriptionWhiteListException;
import com.wso2telco.dep.operatorservice.exception.SubscriptionWhiteListException.SubscriptionWhiteListErrorType;
import com.wso2telco.dep.operatorservice.model.*;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.dto.UserApplicationAPIUsage;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.api.model.SubscribedAPI;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BlackListWhiteListService {
	Log LOG = LogFactory.getLog(BlackListWhiteListService.class);
	private BlackListWhiteListDAO dao;
	private Pattern bwRegex = Pattern.compile("\\d+");

	public BlackListWhiteListService()
	{
		dao = new BlackListWhiteListDAO();
	}

    /**
	 * validate the MSISDN s and black list . The MSISDN need to provided with +
	 * sign if not all the process fails. The entire process run as a single
	 * transaction .
	 */
	public APIBlacklistWhitelistResponseDTO blacklist(BlacklistWhitelistDTO dto) throws BusinessException {

		List<String> valid = new ArrayList<>();
		List<String> invalid = new ArrayList<>();
		APIBlacklistWhitelistResponseDTO responseDTO = new APIBlacklistWhitelistResponseDTO();
		int success = 0;

		Matcher matcher;
		try {

			for (String m : dto.getMsisdnList()) {
				matcher = bwRegex.matcher(m);
				if (matcher.matches()) {
					valid.add(m);
				} else {
					invalid.add(m);
				}
			}

			if (!valid.isEmpty()) {
				dto.setMsisdnList(valid);
				success = dao.blacklist(dto);
			}

			responseDTO.setProcessed(success);
			responseDTO.setFailed(invalid.size() + (valid.size() - success));

		} catch (Exception e) {
			LOG.error("Unable to process blacklist", e);
			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		if(invalid.isEmpty()){
			LOG.error("Could not process due to invalid format: " + invalid);
		}

		return responseDTO;
    }

	/**
	 * Returns the count of a entries for blacklist whitelist combination
	 * @param dto BlacklistWhiltelistDTO
	 * @return BlacklistWhitelistCountResponseDTO
	 * @throws BusinessException
	 */
	public BlacklistWhitelistCountResponseDTO count(BlacklistWhitelistDTO dto) throws BusinessException {
		try {
			return dao.getCount(dto);
		} catch (Exception e) {
			LOG.error("loadBlacklisted", e);
			throw new NumberBlackListException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Removes a blacklist whitelist entry
	 * @param dto BlacklistWhitelistDTO
	 * @throws BusinessException
	 */
	public void remove(BlacklistWhitelistDTO dto) throws BusinessException {
		try {
			Matcher matcher = bwRegex.matcher(dto.getMsisdnList().get(0));
			if (matcher.matches()) {
				dao.remove(dto);
			}
		} catch (Exception e) {
			LOG.error("remove", e);
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

		MSISDNValidationDTO msisdnValidationDTO = new MSISDNValidationDTO();
		msisdnValidationDTO.setValid(Arrays.asList(msisdns));
		msisdnValidationDTO.setValidationRegex(whiteListDTO.getValidationRegex());
		msisdnValidationDTO.setValidationPrefixGroup(whiteListDTO.getValidationPrefixGroup());
		msisdnValidationDTO.setValidationDigitsGroup(whiteListDTO.getValidationDigitsGroup());
		msisdnValidationDTO.process();

		String subscriptionID = whiteListDTO.getSubscriptionID();
		// if no subscription provided
		// subscription id derived using applicationid and using api id
		if (subscriptionID == null) {
			subscriptionID = String.valueOf(dao.findSubscriptionId(whiteListDTO.getApplicationID(), whiteListDTO.getApiID()));

			if (subscriptionID.trim().length() <= 0) {
				throw new SubscriptionWhiteListException(SubscriptionWhiteListErrorType.NULL_SUBSCRIPTION);
			}
		}

		final String apiID = whiteListDTO.getApiID();
		final String applicationID = whiteListDTO.getApplicationID();


		// check the input stream for already white listed numbers
		List<MsisdnDTO> alreadyWhilteListed;
		try {
			alreadyWhilteListed = dao.loadSubscriptionsForAlreadyWhiteListedMSISDN(subscriptionID);
			for (MsisdnDTO msisdn : alreadyWhilteListed) {
				msisdnValidationDTO.getValidProcessed().remove(msisdn);
			}
		} catch (SQLException e) {
			LOG.error("whiteListSubscription, calling dao.loadSubscriptionsForAlreadyWhiteListedMSISDN(", e);
			throw new SubscriptionWhiteListException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		// All numbers already white listed then throw exception
		if (msisdnValidationDTO.getValidProcessed().isEmpty()) {
			LOG.debug("All the numbers already whitelisted");
			throw new SubscriptionWhiteListException(SubscriptionWhiteListErrorType.SUBSCRIPTION_ALREADY_WHITELISTED);
		}
		// persistence goes hare
		try {
			dao.whitelist(msisdnValidationDTO, subscriptionID, apiID, applicationID);

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

	public String[] getWhiteListNumbers(String userId, String apiId, String appId) throws BusinessException {
		try {
			List<String> result = dao.getWhiteListNumbers(userId,apiId,appId);
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

	/**
	 * Returns the offset and limit required for pagination of blacklisted APIs
	 *
	 * @param apiId apiId API ID of blacklisted numbers to get count of
	 * @param limit maximum number of records per page
	 * @return
	 */
	private List<PaginationDTO> getBlacklistPages(BlacklistWhitelistDTO dto, int limit) throws Exception {
		int count = dao.getBlacklistCountByApi(dto);
		int factor = count / limit ;
		int remainder = count % limit;
		int index = 0;

		List<PaginationDTO> blacklistPages = new ArrayList<>();

		for ( ; index < factor; index++ ) {
			blacklistPages.add(new PaginationDTO(limit, (index * limit)));
		}

		if (remainder > 0) {
			blacklistPages.add(new PaginationDTO(limit, (index * limit)));
		}

		return blacklistPages;
	}

	/**
	 * Writes the blacklist whitelist entries to the given output stream as a zip file
	 * @param dto BlacklistWhitelistDTO
	 * @param out Outputstream
	 * @throws Exception
	 */
	public void getBlacklistAsZip(BlacklistWhitelistDTO dto, OutputStream out) throws Exception {
		List<PaginationDTO> pages = getBlacklistPages(dto,1000000);
		int index = 1;

		ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(out));
		//for each page create new entry and write to stream

		for(PaginationDTO p : pages){
			zipOut.putNextEntry(new ZipEntry("blacklist-whitelist-numbers-" + index + ".csv"));
			dao.streamBlacklistByApi(dto,p.getOffset(),p.getLimit(),zipOut);
			zipOut.closeEntry();
			index++;
		}

		zipOut.close();
		out.flush();
		out.close();
	}

	/**
	 * Searches for a valid blacklist whitelist entry
	 * @param dto BlacklistWhitelistDTO
	 * @return
	 * @throws Exception
	 */
	public BlacklistWhitelistSearchResponseDTO search(BlacklistWhitelistDTO dto) throws Exception {
		BlacklistWhitelistSearchResponseDTO responseDTO = new BlacklistWhitelistSearchResponseDTO();
		//validate
		Matcher matcher = bwRegex.matcher(dto.getMsisdnList().get(0));
		if (matcher.matches()) {
			boolean exists = dao.checkIfBlacklistWhitelistExists(dto);
			responseDTO.setExists(exists);

			if (!exists) {
				responseDTO.setError("MSISDN not found");
			}

			return responseDTO;
		} else{
			responseDTO.setExists(false);
			responseDTO.setError("Validation Error: Invalid format!");
			return responseDTO;
		}
	}
}
