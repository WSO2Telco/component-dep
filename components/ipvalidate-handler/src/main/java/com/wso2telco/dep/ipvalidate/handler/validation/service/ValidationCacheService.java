package com.wso2telco.dep.ipvalidate.handler.validation.service;

import java.util.ArrayList;
import java.util.List;

import javax.cache.Cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.dep.ipvalidate.handler.validation.dto.ClientIPPool;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.ClientIPRange;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.ClientKeyIPData;
import com.wso2telco.dep.ipvalidate.handler.validation.utils.CacheOperation;
import com.wso2telco.dep.ipvalidate.handler.validation.utils.IPValidationDBUtils;
import com.wso2telco.dep.ipvalidate.handler.validation.utils.IPValidationUtil;

public class ValidationCacheService {
	private static final Log log = LogFactory.getLog(ValidationCacheService.class);

	static {
		log.debug("initialize IP validation cache");
		try {
			CacheOperation.createIPKeyValidationCache();
			loadCache();
		} catch (Exception e) {
			log.error("Error while initializing cache : " + e);
			e.printStackTrace();
		}
	}

	public static Cache<String, List<ClientKeyIPData>> getCache() {
		Cache<String, List<ClientKeyIPData>> ipValidationCache = CacheOperation
				.getCache(IPValidationUtil.IP_VALIDATION_CACHE_NAME);
		try {
			if (!ipValidationCache.iterator().hasNext()) {
				loadCache();
				ipValidationCache = CacheOperation.getCache(IPValidationUtil.IP_VALIDATION_CACHE_NAME);
			}
		} catch (Exception ex) {
			log.error("Error while initializing cache : " + ex);
			ex.printStackTrace();
		}

		return ipValidationCache;
	}

	public static void loadCache() throws Exception {
		log.debug("load Cache");
		IPValidationDBUtils ipvalidationDBUtils = new IPValidationDBUtils();
		ArrayList<String> clientKeyList = ipvalidationDBUtils.getClientKeyList();
		Cache<String, List<ClientKeyIPData>> clienIPCache = CacheOperation
				.getCache(IPValidationUtil.IP_VALIDATION_CACHE_NAME);

		for (String clientKey : clientKeyList) {
			List<ClientKeyIPData> clientIpSummaryList = ipvalidationDBUtils.getValidIPListForClient(clientKey);
			clienIPCache.put(clientKey, clientIpSummaryList);
		}
	}
	
	public String getTokenfromCache(String clientKey)
	{
		String clientToken = null;
		List<ClientKeyIPData> clientIpSummaryList = ValidationCacheService.getCache()
				.get(clientKey);		
		if(!clientIpSummaryList.isEmpty())
		{
			clientToken = clientIpSummaryList.get(0).getClientToken();
		}
		log.info("return Token from Cache " + clientKey + " ; " + clientToken);
		return clientToken;
	}
}
