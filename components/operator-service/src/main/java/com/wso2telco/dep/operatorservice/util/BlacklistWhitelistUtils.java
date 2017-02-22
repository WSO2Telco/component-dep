package com.wso2telco.dep.operatorservice.util;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.msisdnvalidator.InvalidMSISDNException;
import com.wso2telco.core.msisdnvalidator.MSISDN;
import com.wso2telco.core.msisdnvalidator.MSISDNUtil;
import com.wso2telco.dep.operatorservice.internal.ServiceReferenceHolder;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import org.wso2.carbon.core.util.CryptoException;
import org.wso2.carbon.core.util.CryptoUtil;
import org.wso2.carbon.identity.oauth.config.OAuthServerConfiguration;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlacklistWhitelistUtils {

	public static String replaceEmailDomainBack(String input) {
		if (input != null && input.contains(BlacklistWhitelistConstants.EMAIL_DOMAIN_SEPARATOR_REPLACEMENT)) {
			input = input.replace(BlacklistWhitelistConstants.EMAIL_DOMAIN_SEPARATOR_REPLACEMENT,
					BlacklistWhitelistConstants.EMAIL_DOMAIN_SEPARATOR);
		}
		return input;
	}


	public static boolean checkAccessTokenPartitioningEnabled() {
		return OAuthServerConfiguration.getInstance().isAccessTokenPartitioningEnabled();
	}

	public static boolean checkUserNameAssertionEnabled() {
		return OAuthServerConfiguration.getInstance().isUserNameAssertionEnabled();
	}

	public static String[] getAvailableKeyStoreTables() throws Exception {
		String[] keyStoreTables = new String[0];
		Map<String, String> domainMappings = getAvailableUserStoreDomainMappings();
		if (domainMappings != null) {
			keyStoreTables = new String[domainMappings.size()];
			int i = 0;
			for (Map.Entry<String, String> e : domainMappings.entrySet()) {
				String value = e.getValue();
				keyStoreTables[i] = BlacklistWhitelistConstants.ACCESS_TOKEN_STORE_TABLE + "_" + value.trim();
				i++;
			}
		}
		return keyStoreTables;
	}


	public static Map<String, String> getAvailableUserStoreDomainMappings() throws
			Exception {
		Map<String, String> userStoreDomainMap = new HashMap<String, String>();
		String domainsStr = OAuthServerConfiguration.getInstance().getAccessTokenPartitioningDomains();
		if (domainsStr != null) {
			String[] userStoreDomainsArr = domainsStr.split(",");
			for (String anUserStoreDomainsArr : userStoreDomainsArr) {
				String[] mapping = anUserStoreDomainsArr.trim().split(":"); //A:foo.com , B:bar.com
				if (mapping.length < 2) {
					throw new Exception("Domain mapping has not defined");
				}
				userStoreDomainMap.put(mapping[1].trim(), mapping[0].trim()); //key=domain & value=mapping
			}
		}
		return userStoreDomainMap;
	}



	public static String getKeysSqlUsingSubscriptionId(String accessTokenStoreTable) {
		String tokenStoreTable = BlacklistWhitelistConstants.ACCESS_TOKEN_STORE_TABLE;
		if (accessTokenStoreTable != null) {
			tokenStoreTable = accessTokenStoreTable;
		}

		return SQLConstants.GET_KEY_SQL_OF_SUBSCRIPTION_ID_PREFIX +
				tokenStoreTable + SQLConstants.GET_KEY_SQL_OF_SUBSCRIPTION_ID_SUFFIX;
	}

	/**
	 * @param token
	 * @return
	 */
	public static String decryptToken(String token) throws CryptoException {
		APIManagerConfiguration config = ServiceReferenceHolder.getInstance().getApiManagerConfigurationService().
				getAPIManagerConfiguration();

		if (Boolean.parseBoolean(config.getFirstProperty(APIConstants.ENCRYPT_TOKENS_ON_PERSISTENCE))) {
			return new String(CryptoUtil.getDefaultCryptoUtil().base64DecodeAndDecrypt(token), Charset.defaultCharset());
		}
		return token;
	}



	public static List<MSISDN> getMSISDNList(String msisdnString) throws InvalidMSISDNException {

		String[] msisdnArray = msisdnString.split("[,]");

		MSISDNUtil msisdnUtil = new MSISDNUtil();
		List<MSISDN> msisdnArrayList = new ArrayList<MSISDN>();

		for (String msisdn : msisdnArray) {

			//The number format should be telco:phonenumber
			int charIndex = msisdn.indexOf('+');
			String prefix = msisdn.substring(0, charIndex);
			msisdn = msisdn.substring(msisdn.indexOf('+'));
			MSISDN msisdnDTO = msisdnUtil.parse(msisdn);
			msisdnDTO.setPrefix(prefix);

			msisdnArrayList.add(msisdnDTO);
		}

		return msisdnArrayList;
	}
}
