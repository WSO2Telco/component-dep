package com.wso2telco.dep.verificationhandler.verifier;

import org.wso2.carbon.registry.core.RegistryConstants;

public class VerificationUtils {

	public static String getMSISDNPropertyPrefixFile() {
		return VerificationConstants.API_APPLICATION_DATA_LOCATION + RegistryConstants.PATH_SEPARATOR +
				VerificationConstants.API_MSISDN_PREFIX + RegistryConstants.PATH_SEPARATOR;
	}
}
