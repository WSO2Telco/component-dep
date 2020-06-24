package com.wso2telco.dep.noauth.handler.validation.util;


import javax.cache.Cache;
import javax.cache.Caching;

import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;

import com.wso2telco.dep.noauth.handler.validation.configuration.NoAuthValidationProperties;

public class NoAuthCacheOperation {
	
	public static Cache<Long, String> getCache(final String cacheName) {
		return Caching.getCacheManager(APIConstants.API_MANAGER_CACHE_MANAGER).getCache(cacheName);
	}

	public static Cache createNoAuthCache() {
		return APIUtil.getCache(APIConstants.API_MANAGER_CACHE_MANAGER, NoAuthValidationUtil.NOAUTH_CACHE_NAME,
				NoAuthValidationProperties.getCacheModExpiretime(),NoAuthValidationProperties.getCacheAccessExpiretime());
	}

}
