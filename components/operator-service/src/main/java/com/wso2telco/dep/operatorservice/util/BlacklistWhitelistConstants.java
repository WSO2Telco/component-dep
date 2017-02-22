package com.wso2telco.dep.operatorservice.util;

public class BlacklistWhitelistConstants {

	public static final String EMAIL_DOMAIN_SEPARATOR_REPLACEMENT = "-AT-";

	public static final String EMAIL_DOMAIN_SEPARATOR = "@";

	public static final String ACCESS_TOKEN_STORE_TABLE = "IDN_OAUTH2_ACCESS_TOKEN";


	public static class SubscriptionStatus {
		public static final String BLOCKED = "BLOCKED";
		public static final String PROD_ONLY_BLOCKED = "PROD_ONLY_BLOCKED";
		public static final String UNBLOCKED = "UNBLOCKED";
		public static final String ON_HOLD = "ON_HOLD";
		public static final String REJECTED = "REJECTED";

		private SubscriptionStatus(){

		}
	}
}
