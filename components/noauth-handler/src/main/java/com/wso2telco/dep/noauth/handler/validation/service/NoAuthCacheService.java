package com.wso2telco.dep.noauth.handler.validation.service;

import javax.cache.Cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.dep.noauth.handler.validation.configuration.NoAuthValidationProperties;
import com.wso2telco.dep.noauth.handler.validation.util.NoAuthCacheOperation;
import com.wso2telco.dep.noauth.handler.validation.util.NoAuthDBUtils;
import com.wso2telco.dep.noauth.handler.validation.util.NoAuthValidationUtil;

public class NoAuthCacheService {
	private static final Log log = LogFactory.getLog(NoAuthCacheService.class);

	static {
		log.debug("initialize no auth cache");
		log.info("initialize no auth cache");
		try {
			NoAuthCacheOperation.createNoAuthCache();
			loadCache();
		} catch (Exception e) {
			log.error("Error while initializing cache : " + e);
			e.printStackTrace();
		}
	}

	public static Cache<Long, String> getCache() {
		log.info("get cache ");
		Cache<Long, String> noAuthCache = NoAuthCacheOperation
				.getCache(NoAuthValidationUtil.NOAUTH_CACHE_NAME);
		
		try {
			if (!noAuthCache.iterator().hasNext()) {
				loadCache();
				noAuthCache = NoAuthCacheOperation.getCache(NoAuthValidationUtil.NOAUTH_CACHE_NAME);
			}
		} catch (Exception ex) {
			log.error("Error while initializing cache : " + ex);
			ex.printStackTrace();
		}

		return noAuthCache;
	}

	public static void loadCache() throws Exception {
		log.debug("load no auth Cache");
		log.info("load no auth Cache");
		NoAuthDBUtils noauthDBUtils = new NoAuthDBUtils();
		String token = noauthDBUtils.getToken(NoAuthValidationProperties.getNoauthClientId());
		log.info("token" + token);
		Cache<Long, String> noauthCache = NoAuthCacheOperation
				.getCache(NoAuthValidationUtil.NOAUTH_CACHE_NAME);

		noauthCache.put(NoAuthValidationProperties.getNoauthClientId(), token);
	}
	
}
