package com.wso2telco.dep.noauth.handler.validation.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.dep.noauth.handler.validation.dao.ClientIdTokenDao;

public class NoAuthDBUtils {
	
	private static final Log log = LogFactory.getLog(NoAuthDBUtils.class);

	private ClientIdTokenDao clientTokenDao;

	public NoAuthDBUtils() {
		clientTokenDao = new ClientIdTokenDao();
	}
	
	public String getToken(Long clientKey) throws Exception
	{
		log.debug("Get token " + clientKey);
		log.info("Get token " + clientKey);
		String token = null;
		try {
			token = clientTokenDao.getClientToken(clientKey);
		} catch (Exception e) {
			log.error("error while getting client token " + e);
			throw e;
		}
		return token;
	}

}
