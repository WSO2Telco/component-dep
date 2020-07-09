package com.wso2telco.dep.ipvalidate.handler.validation.utils;

import java.util.List;

import javax.cache.Cache;
import javax.cache.Caching;

import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;

import com.wso2telco.dep.ipvalidate.handler.validation.configuration.IPValidationProperties;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.ClientKeyIPData;

public class CacheOperation {

	public static Cache<String, List<ClientKeyIPData>> getCache(final String cacheName) {
		return Caching.getCacheManager(APIConstants.API_MANAGER_CACHE_MANAGER).getCache(cacheName);
	}

	public static Cache createIPKeyValidationCache() {
		return APIUtil.getCache(APIConstants.API_MANAGER_CACHE_MANAGER, IPValidationUtil.IP_VALIDATION_CACHE_NAME,
				IPValidationProperties.getCacheModExpiretime(),IPValidationProperties.getCacheAccessExpiretime());
	}
}
