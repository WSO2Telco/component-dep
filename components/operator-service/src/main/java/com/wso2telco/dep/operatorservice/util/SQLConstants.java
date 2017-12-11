package com.wso2telco.dep.operatorservice.util;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;

public class SQLConstants {

	public static final String GET_APP_API_USAGE_BY_PROVIDER_SQL =
			" SELECT " +
					"   SUBS.SUBSCRIPTION_ID AS SUBSCRIPTION_ID, " +
					"   SUBS.APPLICATION_ID AS APPLICATION_ID, " +
					"   SUBS.SUB_STATUS AS SUB_STATUS, " +
					"   SUBS.TIER_ID AS TIER_ID, " +
					"   API.API_PROVIDER AS API_PROVIDER, " +
					"   API.API_ID AS API_ID, " +
					"   API.API_NAME AS API_NAME, " +
					"   API.API_VERSION AS API_VERSION, " +
					"   SUB.USER_ID AS USER_ID, " +
					"   APP.NAME AS APPNAME, " +
					"   SUBS.UUID AS SUB_UUID, " +
					"   SUBS.TIER_ID AS SUB_TIER_ID, " +
					"   APP.UUID AS APP_UUID, " +
					"   SUBS.SUBS_CREATE_STATE AS SUBS_CREATE_STATE " +
					" FROM " +
					"   AM_SUBSCRIPTION SUBS, " +
					"   AM_APPLICATION APP, " +
					"   AM_SUBSCRIBER SUB, " +
					"   AM_API API " +
					" WHERE " +
					"   SUBS.APPLICATION_ID = APP.APPLICATION_ID " +
					"   AND APP.SUBSCRIBER_ID = SUB.SUBSCRIBER_ID " +
					"   AND API.API_ID = SUBS.API_ID " +
					"   AND SUBS.SUB_STATUS != '" + BlacklistWhitelistConstants.SubscriptionStatus.REJECTED + "'" +
					" ORDER BY " +
					"   APP.NAME";


	public static final String GET_APP_API_USER_SQL = new StringBuilder()
			.append(" SELECT distinct(SUB.USER_ID) AS USER_ID FROM AM_SUBSCRIPTION SUBS, AM_APPLICATION APP,  ")
			.append("AM_SUBSCRIBER SUB, AM_API API WHERE SUBS.APPLICATION_ID = APP.APPLICATION_ID ")
			.append("AND APP.SUBSCRIBER_ID = SUB.SUBSCRIBER_ID AND API.API_ID = SUBS.API_ID ")
			.append("AND SUBS.SUB_STATUS != '")
			.append(BlacklistWhitelistConstants.SubscriptionStatus.REJECTED)
			.append("' ")
			.toString();


	public static final String GET_KEY_SQL_OF_SUBSCRIPTION_ID_PREFIX =
			" SELECT " +
					"   IAT.ACCESS_TOKEN AS ACCESS_TOKEN," +
					"   IAT.TOKEN_STATE AS TOKEN_STATE " +
					" FROM" +
					"   AM_APPLICATION_KEY_MAPPING AKM," +
					"   AM_SUBSCRIPTION SM,";

	public static final String GET_KEY_SQL_OF_SUBSCRIPTION_ID_SUFFIX =
			"   IAT," +
					"   IDN_OAUTH_CONSUMER_APPS ICA " +
					" WHERE" +
					"   SM.SUBSCRIPTION_ID = ? " +
					"   AND SM.APPLICATION_ID= AKM.APPLICATION_ID " +
					"   AND ICA.CONSUMER_KEY = AKM.CONSUMER_KEY " +
					"   AND ICA.ID = IAT.CONSUMER_KEY_ID";


	public static final String GET_APP_USER_SUBSCRIPTION_SQL = "SELECT DISTINCT (APPNAME), APPLICATION_ID " +
			"FROM " +
			"  (SELECT APP.NAME AS APPNAME, " +
			"          API.API_NAME, " +
			"          SUB.USER_ID, " +
			"          APP.APPLICATION_ID AS APPLICATION_ID " +
			"   FROM AM_SUBSCRIPTION SUBS, " +
			"        AM_APPLICATION APP, " +
			"        AM_SUBSCRIBER SUB, " +
			"        AM_API API " +
			"   WHERE SUBS.APPLICATION_ID = APP.APPLICATION_ID " +
			"     AND APP .SUBSCRIBER_ID = SUB.SUBSCRIBER_ID " +
			"     AND API.API_ID = SUBS.API_ID " +
			"     AND SUBS.SUB_STATUS != 'REJECTED' " +
			"     AND SUB.USER_ID = ? " +
			"   ORDER BY APP.NAME) AS apps";

	public static final String GET_APP_USER_OPERATOR_SUBSCRIPTION_SQL = "SELECT DISTINCT (APPNAME), APPLICATION_ID " +
			"FROM " +
			"(SELECT APP.NAME AS APPNAME, " +
			" API.API_NAME, " +
			" SUB.USER_ID, " +
			"       APP.APPLICATION_ID AS APPLICATION_ID " +
			"FROM AM_SUBSCRIPTION SUBS, " +
			"     AM_APPLICATION APP, " +
			"     AM_SUBSCRIBER SUB, " +
			"     AM_API API, " +
			"     "+ DbUtils.getDbNames().get(DataSourceNames.WSO2TELCO_DEP_DB)+".operators o, " +
			"     "+ DbUtils.getDbNames().get(DataSourceNames.WSO2TELCO_DEP_DB)+".operatorapps oa " +
			"WHERE SUBS.APPLICATION_ID = APP.APPLICATION_ID " +
			"  AND APP .SUBSCRIBER_ID = SUB.SUBSCRIBER_ID " +
			"  AND API.API_ID = SUBS.API_ID " +
			"  AND SUBS.SUB_STATUS != 'REJECTED' " +
			"  AND SUB.USER_ID = ? " +
			"  AND o.ID = oa.operatorid " +
			"  AND o.operatorname = ? " +
			"  AND APP.APPLICATION_ID = oa.applicationid " +
			"ORDER BY APP.NAME) AS apps";


	public static final String GET_API_FOR_USER_AND_APP_SQL = "SELECT API.API_ID AS API_ID, API" +
			".API_PROVIDER AS API_PROVIDER, API.API_NAME " +
			"AS API_NAME, API.API_VERSION AS API_VERSION FROM AM_SUBSCRIPTION  SUBS, AM_APPLICATION APP,  " +
			"AM_SUBSCRIBER SUB, AM_API API WHERE SUBS.APPLICATION_ID = APP.APPLICATION_ID " +
			"AND APP.SUBSCRIBER_ID = SUB.SUBSCRIBER_ID AND API.API_ID = SUBS.API_ID AND SUBS.SUB_STATUS != " +
			"'"+BlacklistWhitelistConstants.SubscriptionStatus.REJECTED +"' AND SUB.USER_ID = ? AND APP.APPLICATION_ID = " +
			"? ORDER BY APP.NAME";


/*	public static final String GET_SUBSCRIPTION_ID_FOR_API_AND_APP_SQL = "SELECT SUBS.SUBSCRIPTION_ID AS SUBSCRIPTION_ID,  " +
			"SUBS.APPLICATION_ID AS APPLICATION_ID, API.API_PROVIDER AS API_PROVIDER, API.API_NAME AS API_NAME, " +
			"API.API_VERSION AS API_VERSION, APP.NAME AS APPNAME  FROM AM_SUBSCRIPTION SUBS, AM_APPLICATION APP,  " +
			"AM_API API WHERE API.API_PROVIDER = ? AND API.API_NAME = ? AND API.API_VERSION = ? " +
			"AND APP.NAME = ? AND SUBS.APPLICATION_ID = APP.APPLICATION_ID AND API.API_ID = SUBS.API_ID " +
			"AND SUBS.SUB_STATUS != 'BlacklistWhitelistConstants.SubscriptionStatus.REJECTED ' ORDER BY APP.NAME";*/


	public static final String GET_SUBSCRIPTION_ID_FOR_API_AND_APP_SQL = "SELECT SUBS.SUBSCRIPTION_ID AS " +
			"SUBSCRIPTION_ID FROM AM_SUBSCRIPTION SUBS, AM_APPLICATION APP, AM_API API WHERE API.API_ID = ? " +
			"AND APP.APPLICATION_ID = ? AND SUBS.APPLICATION_ID = APP.APPLICATION_ID AND API.API_ID = SUBS.API_ID " +
			"AND SUBS.SUB_STATUS != '"+BlacklistWhitelistConstants.SubscriptionStatus.REJECTED+ "' ORDER BY APP.NAME";

	public static final String GET_WHITE_LIST_MSISDNS_FOR_SUBSCRIPTION = "SELECT prefix, msisdn from subscription_WhiteList " +
			"WHERE subscriptionID = ?";

	public static final String GET_API_ID_SQL =
			" SELECT AM_API.API_ID AS API_ID FROM  AM_API WHERE AM_API.API_PROVIDER = ? AND AM_API.API_NAME = ? AND " +
					"AM_API.API_VERSION = ?";


	public static final String GET_API_INFO_SQL =
			" SELECT API_PROVIDER, API_NAME, API_VERSION FROM  AM_API WHERE API_ID = ?";


	public static final String GET_MSISDN_FOR_WHITELIST =
			"SELECT wh.msisdn FROM subscription_whitelist wh,"+DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB)+".AM_SUBSCRIPTION sub " +
			"where sub.created_by=? and wh.api_id=? and wh.application_id=? and wh.subscriptionId=sub.subscription_Id ";

	public static final String GET_SP_NAMES = new StringBuilder().append("SELECT distinct(authz_user) from")
			.append("  (SELECT ac.authz_user")
			.append("   FROM ")
			.append(OparatorTable.IDN_OAUTH2_ACCESS_TOKEN)
			.append(" ac, ")
			.append(OparatorTable.IDN_OAUTH_CONSUMER_APPS)
			.append(" ca, ")
			.append(OparatorTable.AM_APPLICATION.getTObject())
			.append(" am, ")
			.append(OparatorTable.AM_APPLICATION_KEY_MAPPING.getTObject())
			.append(" km")
			.append("   WHERE ac .CONSUMER_KEY_ID = ca.ID")
			.append("     AND km .application_id = am.application_id")
			.append("     AND km .consumer_key = ca.consumer_key")
			.append("     AND ac .authz_user = ca.username")
			.append("     AND user_type = 'APPLICATION'")
			.append("     AND token_State = 'Active') AS dummy")
			.toString();

	public static final String GET_ADMIN_USERS = new StringBuilder()
			.append("SELECT usr.um_user_name ")
			.append("		FROM ").append(OparatorTable.UM_USER.getTObject()).append(" usr, ")
			.append(OparatorTable.UM_USER_ROLE.getTObject())
			.append(" usr_role ")
			.append("WHERE usr.UM_ID = usr_role.UM_USER_ID ")
			.append("  AND usr_role.UM_ROLE_ID = ")
			.append("    (SELECT role.UM_ID ")
			.append("     FROM ")
			.append(OparatorTable.UM_ROLE.getTObject())
			.append(" role ")
			.append("     WHERE role.UM_ROLE_NAME =?)")
			.toString();

	public static final String GET_SP_APPS = new StringBuilder().append("select am.application_id,ca.APP_NAME,ac.authz_user,ac.ACCESS_TOKEN,")
			.append("ca.consumer_key,ca.consumer_secret from ")
			.append(OparatorTable.IDN_OAUTH2_ACCESS_TOKEN.getTObject())
			.append(" ac, ")
			.append(OparatorTable.IDN_OAUTH_CONSUMER_APPS.getTObject())
			.append(" ca, ")
			.append(OparatorTable.AM_APPLICATION.getTObject())
			.append(" am, ")
			.append(OparatorTable.AM_APPLICATION_KEY_MAPPING)
			.append(" km ")
			.append("where ac.consumer_key_id=ca.id and km.application_id=am.application_id  ")
			.append("and km.consumer_key=ca.consumer_key and ac.authz_user=ca.username and user_type='APPLICATION' ")
			.append("and token_State='Active' and authz_user = ?")
			.toString();

	public static final String GET_BLACKLISTED_SP_LIST = new StringBuilder().append("SELECT DISTINCT ( username ) ")
			.append("FROM   ((SELECT ca.username ")
			.append("         FROM   idn_identity_user_data iud, ")
			.append("                idn_oauth_consumer_apps ca ")
			.append("         WHERE  data_key = 'http://wso2.org/claims/identity/accountlocked' ")
            .append("                AND iud.DATA_VALUE = 'true'                                ")
			.append("                AND ca.username = iud.user_name ")
			.append("                AND ca.grant_types = '')) AS r ")
			.toString();

	public static final String GET_BLACKLISTED_SP_APPS = new StringBuilder().append("SELECT am.application_id, ")
			.append("       ca.app_name, ")
			.append("       ca.consumer_key, ")
			.append("       ca.consumer_secret ")
			.append("FROM   idn_oauth_consumer_apps ca, ")
			.append("       am_application am, ")
			.append("       am_application_key_mapping km ")
			.append("WHERE  km.application_id = am.application_id ")
			.append("       AND km.consumer_key = ca.consumer_key ")
			.append("       AND ca.grant_types = '' ")
			.append("       AND ca.username = ? ")
            .toString();

}