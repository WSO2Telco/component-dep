package com.wso2telco.dep.operatorservice.util;

public class BlacklistWhitelistConstants {

	public static final String EMAIL_DOMAIN_SEPARATOR_REPLACEMENT = "-AT-";

	public static final String EMAIL_DOMAIN_SEPARATOR = "@";

	public static final String ACCESS_TOKEN_STORE_TABLE = "IDN_OAUTH2_ACCESS_TOKEN";

	private BlacklistWhitelistConstants() {
	}

	public static class SubscriptionStatus {
		public static final String BLOCKED = "BLOCKED";
		public static final String PROD_ONLY_BLOCKED = "PROD_ONLY_BLOCKED";
		public static final String UNBLOCKED = "UNBLOCKED";
		public static final String ON_HOLD = "ON_HOLD";
		public static final String REJECTED = "REJECTED";

		private SubscriptionStatus(){
		}
	}

	public static class DAOConstants {
		public static final String API_NAME = "API_NAME";
		public static final String API_ID = "API_ID";
		public static final String API_VERSION = "API_VERSION";
		public static final String MSISDN = "MSISDN";
		public static final String API_PROVIDER = "API_PROVIDER";
		public static final String APPNAME = "APPNAME";
		public static final String APPLICATION_ID = "APPLICATION_ID";

		private DAOConstants(){
		}
	}
}
