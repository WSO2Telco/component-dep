package com.wso2telco.dep.verificationhandler.verifier;

import org.wso2.carbon.registry.core.RegistryConstants;

public class VerificationConstants {


	public static final String APIMGT_REGISTRY_LOCATION = "/apimgt";

	public static final String API_APPLICATION_DATA_LOCATION = APIMGT_REGISTRY_LOCATION + "/applicationdata";

	public static final String API_MSISDN_PREFIX = "msisdn-prefix";

	public static final String API_MSISDN_PREFIX_PROPERTY = "msisdn-prefix-property";


	public static final String GET_SUBSCRIPTION_ID_FOR_API_AND_APP_SQL = "SELECT SUBS.SUBSCRIPTION_ID AS " +
			"SUBSCRIPTION_ID FROM AM_SUBSCRIPTION SUBS, AM_APPLICATION APP, AM_API API WHERE API.API_ID = ? " +
			"AND APP.APPLICATION_ID = ? AND SUBS.APPLICATION_ID = APP.APPLICATION_ID AND API.API_ID = SUBS.API_ID " +
			"AND SUBS.SUB_STATUS != 'BlacklistWhitelistConstants.SubscriptionStatus.REJECTED ' ORDER BY APP.NAME";
}
