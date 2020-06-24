package com.wso2telco.dep.noauth.handler.validation.configuration;

import java.util.Properties;

import com.wso2telco.core.dbutils.fileutils.PropertyFileReader;
import com.wso2telco.dep.noauth.handler.validation.util.NoAuthValidationUtil;

public class NoAuthValidationProperties {

	static {
		loadValidationProperties();
	}

	private static Long cacheModExpiretime;
	private static Long cacheAccessExpiretime;
	private static Long noauthClientId;
	public static int validationFalidErrCode;
	public static String validationFalidErrMsg;

	private static void loadValidationProperties() {
		Properties props = PropertyFileReader.getFileReader()
				.getProperties(NoAuthValidationUtil.NOAUTH_PROPERTIES_FILE);

		cacheModExpiretime = Long.parseLong(props.getProperty(NoAuthValidationUtil.NOAUTH_CACHE_MOD_EXPIRE));
		cacheAccessExpiretime = Long
				.parseLong(props.getProperty(NoAuthValidationUtil.NOAUTH_CACHE_MOD_EXPIRE));
		noauthClientId = Long.parseLong(props.getProperty(NoAuthValidationUtil.NOAUTH_CLIENT_KEY));
		validationFalidErrCode = Integer.parseInt(props.getProperty(NoAuthValidationUtil.NOAUTH_ERROR_CODE_VALIDATION_FAILURE));
		validationFalidErrMsg = props.getProperty(NoAuthValidationUtil.NOAUTH_ERROR_MSG_VALIDATION_FAILURE);

	}

	public static Long getCacheModExpiretime() {
		return cacheModExpiretime;
	}

	public static Long getCacheAccessExpiretime() {
		return cacheAccessExpiretime;
	}

	public static Long getNoauthClientId() {
		return noauthClientId;
	}

	public static int getValidationFalidErrCode() {
		return validationFalidErrCode;
	}

	public static String getValidationFalidErrMsg() {
		return validationFalidErrMsg;
	}

}
