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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.dep.oneapivalidation.util.MsisdnDTO;
import com.wso2telco.dep.operatorservice.AppObject;
import com.wso2telco.dep.operatorservice.dao.BlackListWhiteListDAO;
import com.wso2telco.dep.operatorservice.exception.BlacklistException;
import com.wso2telco.dep.operatorservice.exception.NumberBlackListException;
import com.wso2telco.dep.operatorservice.exception.OperatorServiceException;
import com.wso2telco.dep.operatorservice.exception.SubscriptionWhiteListException;
import com.wso2telco.dep.operatorservice.exception.SubscriptionWhiteListException.SubscriptionWhiteListErrorType;
import com.wso2telco.dep.operatorservice.model.BlackListDTO;
import com.wso2telco.dep.operatorservice.model.MSISDNSearchDTO;
import com.wso2telco.dep.operatorservice.model.MSISDNValidationDTO;
import com.wso2telco.dep.operatorservice.model.WhiteListDTO;
import com.wso2telco.dep.operatorservice.model.WhiteListMSISDNSearchDTO;
import com.wso2telco.dep.operatorservice.service.adminService.AdminServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.dto.UserApplicationAPIUsage;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.api.model.SubscribedAPI;
import org.wso2.carbon.apimgt.hostobjects.internal.HostObjectComponent;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import org.wso2.carbon.identity.mgt.stub.UserIdentityManagementAdminServiceStub;
import org.wso2.carbon.identity.oauth.stub.OAuthAdminServiceStub;
import org.wso2.carbon.identity.oauth.stub.dto.OAuthConsumerAppDTO;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlackListWhiteListService {
	Log LOG = LogFactory.getLog(BlackListWhiteListService.class);
	private BlackListWhiteListDAO dao;

	public BlackListWhiteListService()
	{
		dao = new BlackListWhiteListDAO();
	}

    /**
	 * validate the MSISDN s and black list . The MSISDN need to provided with +
	 * sign if not all the process fails. The entire process run as a single
	 * transaction .
	 */
	public void blacklist(BlackListDTO dto) throws BusinessException {

		String[] msisdns = dto.getUserMSISDN();
		MSISDNValidationDTO msisdnValidationDTO = new MSISDNValidationDTO();

		final String apiID_ = dto.getApiID();
		final String apiName_ = dto.getApiName();
		final String userId_ = dto.getUserID();


			try{

				msisdnValidationDTO.setValid(Arrays.asList(msisdns));
				msisdnValidationDTO.setValidationRegex(dto.getValidationRegex());
				msisdnValidationDTO.setValidationPrefixGroup(dto.getValidationPrefixGroup());
				msisdnValidationDTO.setValidationDigitsGroup(dto.getValidationDigitsGroup());
				msisdnValidationDTO.process();


				// load already black listed numbers
				List<MsisdnDTO> alreadyBlacklisted;

				alreadyBlacklisted = dao.getBlacklisted(apiID_);

				// Remove already black listed from the list
				for (MsisdnDTO msisdn : alreadyBlacklisted) {
					msisdnValidationDTO.getValidProcessed().remove(msisdn);
				}

			} catch (Exception e){
				LOG.error("Error while getting already blacklisted users",e);
				throw new BlacklistException(BlacklistException.BlacklistErrorType.INTERNAL_SERVER_ERROR);
			}


			if (msisdnValidationDTO.getValidProcessed().isEmpty()) {
				LOG.debug(" All the numbers already black listed");
				throw new BlacklistException(BlacklistException.BlacklistErrorType.USER_ALREADY_BLACKLISTED);
			}

		try {
			dao.blacklist(msisdnValidationDTO, apiID_, apiName_, userId_);
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

	//assumes case will match in all cases
    public String getSPNameList() throws OperatorServiceException {
        try {
            Gson gson = new GsonBuilder().create();
            List<String> adminUsers = dao.getAdminUsers();
            List<String> SPs = dao.generateSPNameList();

            for (String name : adminUsers) {
                if (SPs.contains(name)) {
                    SPs.remove(name);
                }
            }

            return gson.toJson(SPs);
        } catch (Exception e) {
            throw new OperatorServiceException(e);
        }
    }

	public String getBlacklistedSPList() throws OperatorServiceException {
		try {
			Gson gson = new GsonBuilder().create();

			return gson.toJson(dao.getBlacklistedSPList());
		} catch (Exception e) {
			throw new OperatorServiceException(e);
		}
	}

	public void blacklistSP(String SPname) throws OperatorServiceException {
		try {
			List<AppObject> appList = dao.getSPApps(SPname);
			APIManagerConfiguration config = HostObjectComponent.getAPIManagerConfiguration();
			String serviceEndpoint = config.getFirstProperty(APIConstants.AUTH_MANAGER_URL) + "UserIdentityManagementAdminService";

			if (appList.isEmpty()) {
				throw new OperatorServiceException("Invalid SP! No applications found.");
			}

			removeGrantTypes(appList);

			tokenRevoke(appList);

			AdminServiceClient adminService = new AdminServiceClient();
			UserIdentityManagementAdminServiceStub stub = new UserIdentityManagementAdminServiceStub(serviceEndpoint);

			ServiceClient sc = stub._getServiceClient();
			Options options;
			options = sc.getOptions();
			options.setManageSession(true);
			options.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, adminService.getSessionCookie());

			stub.lockUserAccount(SPname);
			adminService.logOut();
		} catch (Exception e) {
			throw new OperatorServiceException(e);
		}

	}

	/**
	 *
	 * See <a href="https://docs.wso2.com/display/IS530/User+Account+Locking+and+Account+Disabling#UserAccountLockingandAccountDisabling-Unlockingauseraccountusingtheadminservice">User unlock admin service</a>
	 * for detail on email notification
	 */
	public void removeblacklistedSP(String SPname) throws OperatorServiceException {

		try {
			List<String> blacklistedSpList = dao.getBlacklistedSPList();

			if(!blacklistedSpList.contains(SPname)){
				throw new OperatorServiceException("SP not blacklisted!");
			}

			APIManagerConfiguration config = HostObjectComponent.getAPIManagerConfiguration();
			String serviceEndpoint = config.getFirstProperty(APIConstants.AUTH_MANAGER_URL) + "UserIdentityManagementAdminService";

			AdminServiceClient adminService = new AdminServiceClient();
			UserIdentityManagementAdminServiceStub stub = new UserIdentityManagementAdminServiceStub(serviceEndpoint);

			List<AppObject> blacklistedSpApps = dao.getBlacklistedSpApps(SPname);
            restoreGrantTypes(blacklistedSpApps);

			ServiceClient sc = stub._getServiceClient();
			Options options;
			options = sc.getOptions();
			options.setManageSession(true);
			options.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, adminService.getSessionCookie());

			stub.unlockUserAccount(SPname, null);

			adminService.logOut();
		} catch (Exception e) {
			throw new OperatorServiceException(e);
		}
	}

	private void removeGrantTypes(List<AppObject> appList) throws OperatorServiceException {
		try {
			APIManagerConfiguration config = HostObjectComponent.getAPIManagerConfiguration();
			String serviceEndpoint = config.getFirstProperty(APIConstants.AUTH_MANAGER_URL) + "OAuthAdminService";

			AdminServiceClient adminService = new AdminServiceClient();
			OAuthAdminServiceStub stub = new OAuthAdminServiceStub(serviceEndpoint);
			OAuthConsumerAppDTO appDTO = new OAuthConsumerAppDTO();

			ServiceClient sc = stub._getServiceClient();
			Options options;
			options = sc.getOptions();
			options.setManageSession(true);
			options.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, adminService.getSessionCookie());

			for (AppObject app : appList) {
				try {
					appDTO.setOAuthVersion("OAuth-2.0");
					appDTO.setApplicationName(app.getApp_name());
					appDTO.setUsername(app.getSp_name());
					appDTO.setOauthConsumerKey(app.getConsumer_key());
					appDTO.setOauthConsumerSecret(app.getConsmer_secret());
					appDTO.setGrantTypes(null);
					stub.updateConsumerApplication(appDTO);
				} catch (Exception e) {
					LOG.error("Error removing grant types for application: " + app.getApp_name() + "of SP: " + app.getSp_name(), e);
				}
			}
			adminService.logOut();
		} catch (Exception e) {
			throw new OperatorServiceException(e);
		}

	}

	private void restoreGrantTypes(List<AppObject> appList) throws OperatorServiceException {
		try {
			APIManagerConfiguration config = HostObjectComponent.getAPIManagerConfiguration();
			String serviceEndpoint = config.getFirstProperty(APIConstants.AUTH_MANAGER_URL) + "OAuthAdminService";

			AdminServiceClient adminService = new AdminServiceClient();
			OAuthAdminServiceStub stub = new OAuthAdminServiceStub(serviceEndpoint);
			OAuthConsumerAppDTO appDTO = new OAuthConsumerAppDTO();

			ServiceClient sc = stub._getServiceClient();
			Options options;
			options = sc.getOptions();
			options.setManageSession(true);
			options.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, adminService.getSessionCookie());

			for (AppObject app : appList) {
				try {
				    appDTO.setOAuthVersion("OAuth-2.0");
					appDTO.setApplicationName(app.getApp_name());
					appDTO.setUsername(app.getSp_name());
					appDTO.setOauthConsumerKey(app.getConsumer_key());
					appDTO.setOauthConsumerSecret(app.getConsmer_secret());
					appDTO.setGrantTypes("refresh_token urn:ietf:params:oauth:grant-type:saml2-bearer password iwa:ntlm client_credentials");
					stub.updateConsumerApplication(appDTO);
				} catch (Exception e) {
					LOG.error("Error restoring grant types for application: " + app.getApp_name() + "of SP: " + app.getSp_name(), e);
				}
			}
			adminService.logOut();
		} catch (Exception e) {
			throw new OperatorServiceException(e);
		}

	}

	private void tokenRevoke(List<AppObject> appList) throws OperatorServiceException {
        APIManagerConfiguration config = HostObjectComponent.getAPIManagerConfiguration();
		String revokeURL = config.getFirstProperty(APIConstants.REVOKE_API_URL);

		String auth_token;
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		CloseableHttpClient client = null;

		try {
			client = HttpClientBuilder.create().build();

			HttpPost post = new HttpPost(revokeURL);
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");

			HttpResponse response;
			for (AppObject app : appList) {
				try {
					auth_token = new String(Base64.encodeBase64((app.getConsumer_key() + ":" + app.getConsmer_secret()).getBytes()));
					post.setHeader("Authorization", "Basic " + auth_token);
					urlParameters.add(new BasicNameValuePair("token", app.getAccess_token()));
					post.setEntity(new UrlEncodedFormEntity(urlParameters));

					response = client.execute(post);
					LOG.debug("Token revoke response code for app: " + app.getApp_name() + " :"
							+ response.getStatusLine().getStatusCode());
					post.reset();
					urlParameters.clear();
				} catch (Exception e) {
					LOG.error("Error revoking token for application: " + app.getApp_name() + "of SP: " + app.getSp_name(), e);
				}
			}
			client.close();
		} catch (Exception e) {
			throw new OperatorServiceException(e);
		}
	}



}
