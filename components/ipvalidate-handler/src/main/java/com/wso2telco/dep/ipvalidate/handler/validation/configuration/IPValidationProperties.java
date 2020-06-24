package com.wso2telco.dep.ipvalidate.handler.validation.configuration;

import java.util.Properties;

import com.wso2telco.core.dbutils.fileutils.PropertyFileReader;
import com.wso2telco.dep.ipvalidate.handler.validation.utils.IPValidationUtil;

public class IPValidationProperties {
	
	static {
		loadValidationProperties();		
	}
	
	private static boolean isCustomValidationEnabled;
	private static String validatorClasses;
	private static Long cacheModExpiretime;
	private static Long cacheAccessExpiretime;
	public static int invalidClientErrCode;
	public static int invalidHostErrCode;
	public static int validationFalidErrCode;
	public static String invalidClientErrMsg;
	public static String invalidHostErrMsg;
	public static String validationFalidErrMsg;
	
	
	private static void loadValidationProperties()
	{
		Properties props = PropertyFileReader.getFileReader().getProperties(IPValidationUtil.IP_VALIDATION_PROPERTIES_FILE);
		
		isCustomValidationEnabled = Boolean.valueOf(props.getProperty(IPValidationUtil.IS_IP_VALIDATION_ENABLED));
		validatorClasses = props.getProperty(IPValidationUtil.IP_VALIDATION_CLASSES);
		cacheModExpiretime = Long.parseLong(props.getProperty(IPValidationUtil.IP_VALIDATION_CACHE_MOD_EXPIRE));
		cacheAccessExpiretime = Long.parseLong(props.getProperty(IPValidationUtil.IP_VALIDATION_CACHE_ACCESS_EXPIRE));
		
		invalidClientErrCode = Integer.parseInt(props.getProperty(IPValidationUtil.IP_VALIDATION_ERROR_CODE_INVALID_CLIENT));
		invalidHostErrCode = Integer.parseInt(props.getProperty(IPValidationUtil.IP_VALIDATION_ERROR_CODE_INVALID_HOST));
		validationFalidErrCode = Integer.parseInt(props.getProperty(IPValidationUtil.IP_VALIDATION_ERROR_CODE_VALIDATION_FAILURE));
		
		invalidClientErrMsg = props.getProperty(IPValidationUtil.IP_VALIDATION_ERROR_MSG_INVALID_CLIENT);
		invalidHostErrMsg = props.getProperty(IPValidationUtil.IP_VALIDATION_ERROR_MSG_INVALID_HOST);
		validationFalidErrMsg = props.getProperty(IPValidationUtil.IP_VALIDATION_ERROR_MSG_VALIDATION_FAILURE);
	}


	public static boolean isCustomValidationEnabled() {
		return isCustomValidationEnabled;
	}


	public static String getValidatorClasses() {
		return validatorClasses;
	}


	public static Long getCacheModExpiretime() {
		return cacheModExpiretime;
	}


	public static Long getCacheAccessExpiretime() {
		return cacheAccessExpiretime;
	}


	public static int getInvalidClientErrCode() {
		return invalidClientErrCode;
	}


	public static int getInvalidHostErrCode() {
		return invalidHostErrCode;
	}


	public static int getValidationFalidErrCode() {
		return validationFalidErrCode;
	}


	public static String getInvalidClientErrMsg() {
		return invalidClientErrMsg;
	}


	public static String getInvalidHostErrMsg() {
		return invalidHostErrMsg;
	}


	public static String getValidationFalidErrMsg() {
		return validationFalidErrMsg;
	}

	

	
}
