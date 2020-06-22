package com.wso2telco.dep.ipvalidate.handler.validation.utils;

import java.util.Properties;

import com.wso2telco.core.dbutils.fileutils.PropertyFileReader;

public class IPValidationUtil {
	
	public static final String IP_VALIDATION_PROPERTIES_FILE = "ip-validation.properties";
	public static final String IS_IP_VALIDATION_ENABLED = "handler.ip.validation.enable";
	public static final String IP_VALIDATION_CLASSES = "handler.ip.validation.classes";
	public static final String IP_VALIDATION_INBUILD_TOKEN = "handler.ip.validation.token";
	public static final String IP_VALIDATION_CACHE_NAME = "IP_KEY_VALIDATION_CACHE";
	public static final String IP_VALIDATION_CACHE_MOD_EXPIRE = "handler.ip.validation.cache.modify.expire";
	public static final String IP_VALIDATION_CACHE_ACCESS_EXPIRE = "handler.ip.validation.cache.access.expire";
	
	//Error messages
	public static final String IP_VALIDATION_ERROR_CODE_INVALID_CLIENT = "ip.validation.error.code.invalid.client";
	public static final String IP_VALIDATION_ERROR_CODE_INVALID_HOST = "ip.validation.error.code.invalid.host";
	public static final String IP_VALIDATION_ERROR_CODE_VALIDATION_FAILURE = "ip.validation.error.code.validate.failed";
	public static final String IP_VALIDATION_ERROR_MSG_INVALID_CLIENT = "ip.validation.error.msg.invalid.client";
	public static final String IP_VALIDATION_ERROR_MSG_INVALID_HOST = "ip.validation.error.msg.invalid.host";
	public static final String IP_VALIDATION_ERROR_MSG_VALIDATION_FAILURE = "ip.validation.error.msg.validate.failed";
	private static Properties props = null;
	
	public static Properties loadUserMaskingProperties(){
		if (props == null) {
			props = PropertyFileReader.getFileReader().getProperties(IP_VALIDATION_PROPERTIES_FILE);
		}
		return props;
	}

	public static String getUserMaskingConfiguration(String property) {
		loadUserMaskingProperties();
		return props.getProperty(property);
	}
}
