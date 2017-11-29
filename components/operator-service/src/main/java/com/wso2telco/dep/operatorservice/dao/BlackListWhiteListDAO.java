/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.dep.operatorservice.dao;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.MsisdnDTO;
import com.wso2telco.core.msisdnvalidator.MSISDN;
import com.wso2telco.dep.operatorservice.AppObject;
import com.wso2telco.dep.operatorservice.exception.OperatorServiceException;
import com.wso2telco.dep.operatorservice.model.MSISDNSearchDTO;
import com.wso2telco.dep.operatorservice.model.MSISDNValidationDTO;
import com.wso2telco.dep.operatorservice.util.BlacklistWhitelistConstants;
import com.wso2telco.dep.operatorservice.util.OparatorError;
import com.wso2telco.dep.operatorservice.util.OparatorTable;
import com.wso2telco.dep.operatorservice.util.SQLConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.dto.UserApplicationAPIUsage;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.api.model.SubscribedAPI;
import org.wso2.carbon.apimgt.api.model.Subscriber;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class BlackListWhiteListDAO {

	private static final Log log = LogFactory.getLog(BlackListWhiteListDAO.class);

	/**
	 * blacklist list given msisdns
	 *
	 * @param msisdns
	 * @param apiID
	 * @param apiName
	 * @param userID
	 * @throws Exception
	 */
	public void blacklist(MSISDNValidationDTO msisdns, final String apiID, final String apiName, final String userID)
			throws Exception {

		log.debug("BlackListWhiteListDAO.blacklist triggerd MSISDN[" + StringUtils.join(msisdns.getValidProcessed().toArray(), ",") + "] apiID:"
				+ apiID + " apiName:" + apiName + " userID:" + userID);

		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO ");
		sql.append(OparatorTable.BLACKLIST_MSISDN.getTObject());
		sql.append("(PREFIX,MSISDN,API_ID,API_NAME,USER_ID,VALIDATION_REGEX)");
		sql.append(" VALUES (?, ?, ?, ?, ?, ?)");

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql.toString());

			conn.setAutoCommit(false);

			for (MsisdnDTO msisdn : msisdns.getValidProcessed()) {

					ps.setString(1, msisdn.getPrefix());
					ps.setString(2, msisdn.getDigits());
					ps.setString(3, apiID);
					ps.setString(4, apiName);
					ps.setString(5, userID);
					ps.setString(6, msisdns.getValidationRegex());
					ps.addBatch();
			}

			ps.executeBatch();
			conn.commit();

		} catch (Exception e) {
			if(conn != null){
                conn.rollback();
            }
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, null);
		}

	}

	/**
	 * filter and return already balcklisted for the filtering
	 *
	 * @param searchDTO
	 * @return
	 * @throws SQLException
	 */
	public List<MSISDN> loadAlreadyBlacklisted(MSISDNSearchDTO searchDTO) throws SQLException {
		return Collections.EMPTY_LIST;
	}

	public List<MsisdnDTO> loadSubscriptionsForAlreadyWhiteListedMSISDN(String subscriptionID) throws SQLException {
		String sql = SQLConstants.GET_WHITE_LIST_MSISDNS_FOR_SUBSCRIPTION;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MsisdnDTO> returnList = new ArrayList<MsisdnDTO>();

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql);
			ps.setString(1, subscriptionID);
			rs = ps.executeQuery();

			while (rs.next()) {
				returnList.add(new MsisdnDTO(rs.getString("prefix"), rs.getString("msisdn")));
			}

		} catch (SQLException e) {
			log.error(e);
			throw e;
		} catch (Exception e) {
			log.error(e);
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);

		}

		return returnList;
	}

	/**
	 * balcklist single msisdn
	 *
	 * @param msisdn
	 * @param apiID
	 * @param apiName
	 * @param userID
	 * @throws Exception
	 */
	public void blacklist(final MSISDN msisdn, final String apiID, final String apiName, final String userID)
			throws Exception {
		List<MSISDN> msisdns = Collections.emptyList();
		msisdns.add(msisdn);
		blacklist(msisdns, apiID, apiName, userID);
	}

	public List<MsisdnDTO> getBlacklisted(String apiId) throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT PREFIX,MSISDN,API_ID,API_NAME,USER_ID");
		sql.append(" FROM ");
		sql.append(OparatorTable.BLACKLIST_MSISDN.getTObject());
		sql.append(" WHERE 1=1 ");
		if (apiId != null) {
			sql.append(" AND  API_ID =? ");
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MsisdnDTO> returnList = new ArrayList<MsisdnDTO>();

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);

			ps = conn.prepareStatement(sql.toString());
			if (apiId != null) {
				ps.setInt(1, Integer.parseInt(apiId));
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				returnList.add(new MsisdnDTO(rs.getString("PREFIX"),rs.getString(BlacklistWhitelistConstants.DAOConstants.MSISDN)));
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);

		}

		return returnList;
	}

	public String[] getBlacklisted(MSISDNSearchDTO searchDTO) throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT MSISDN,API_ID,API_NAME,USER_ID");
		sql.append(" FROM ");
		sql.append(OparatorTable.BLACKLIST_MSISDN.getTObject());
		sql.append(" WHERE 1=1 ");
		if (searchDTO.getApiID() != null) {
			sql.append(" AND  API_ID =? ");
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> returnList = new ArrayList<String>();

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);

			ps = conn.prepareStatement(sql.toString());
			if (searchDTO.getApiID() != null) {
				ps.setInt(1, Integer.parseInt(searchDTO.getApiID()));
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				returnList.add(rs.getString(BlacklistWhitelistConstants.DAOConstants.MSISDN));
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);

        }

        return returnList.toArray(new String[returnList.size()]);
    }

	public void removeBlacklist(final int apiId, final String userMSISDN) throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(OparatorTable.BLACKLIST_MSISDN.getTObject());
		sql.append(" WHERE API_ID = ?");
		sql.append(" AND MSISDN = ?");

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql.toString());

			ps.setInt(1, apiId);
			ps.setString(2, userMSISDN);

			ps.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, conn, rs);

		}
	}


	/**
	 * when the subscription id is known
	 *
	 * @param userMSISDNs
	 * @param subscriptionId
	 * @param apiID
	 * @param applicationID
	 * @throws SQLException
	 * @throws Exception
	 */
	public void whitelist(MSISDNValidationDTO msisdns, String subscriptionId, String apiID, String applicationID) throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(OparatorTable.SUBSCRIPTION_WHITELIST.getTObject());
		sql.append(" (subscriptionID, prefix, msisdn, api_id, application_id, validation_regex)");
		sql.append(" VALUES (?,?,?,?,?,?);");

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql.toString());

			conn.setAutoCommit(false);
			for (MsisdnDTO msisdn : msisdns.getValidProcessed()) {


				ps.setString(1, subscriptionId);
				ps.setString(2, msisdn.getPrefix());
				ps.setString(3, msisdn.getDigits());
				ps.setString(4, apiID);
				ps.setString(5, applicationID);
				ps.setString(6, msisdns.getValidationRegex());

				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();

        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            log.error("", e);
            throw e;
        } finally {
            DbUtils.closeAllConnections(ps, conn, null);
		}

	}

	/**
	 * this need to replace form the apim admin services
	 *
	// * @param appId
	 * @param apiId
	 * @return
	 * @throws Exception
	 */
/*	@Deprecated
	public String findSubscriptionId(String appId, String apiId) throws Exception {

		String sql = "SELECT SUBSCRIPTION_ID from AM_SUBSCRIPTION where APPLICATION_ID = ? and API_ID = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
			ps = conn.prepareStatement(sql);
			ps.setString(1, appId);
			ps.setString(2, apiId);
			rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getString("SUBSCRIPTION_ID");
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);

		}
		throw new Exception(
				"No record found in table AM_SUBSCRIPTION for APPLICATION_ID = " + appId + " and API_ID = " + apiId);
	}*/

	public int findSubscriptionId(String appId, String apiId) throws
			Exception {

		String sql = SQLConstants.GET_SUBSCRIPTION_ID_FOR_API_AND_APP_SQL;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(apiId));
			ps.setInt(2, Integer.parseInt(appId));

			rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt("SUBSCRIPTION_ID");
			}
		} catch (SQLException e) {
			log.error(e);
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);

		}
		throw new CustomException("Blacklist Service - Subscription Checker",
                "No record found in table AM_SUBSCRIPTION for APPLICATION_ID = "+appId+" and API_ID = "+ apiId,
                new String[]{"appId","apiId"});
	}

	public void removeWhitelistNumber(String userMSISDN) throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(OparatorTable.SUBSCRIPTION_WHITELIST.getTObject());
		sql.append(" WHERE `MSISDN`=?;");

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, userMSISDN);
			ps.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, null);
		}
	}


	public List<String> getWhiteListNumbers(String userId, String apiId, String appId) throws Exception {

		String sql = SQLConstants.GET_MSISDN_FOR_WHITELIST;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> whiteList = new ArrayList<String>();

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, apiId);
			ps.setString(3, appId);
			rs = ps.executeQuery();

			while (rs.next()) {
				whiteList.add(rs.getString(BlacklistWhitelistConstants.DAOConstants.MSISDN));
			}
			return whiteList;

		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);
		}
	}

	public Map<String, UserApplicationAPIUsage> getAllAPIUsageByProvider() throws BusinessException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		try {
			String sqlQuery = SQLConstants.GET_APP_API_USAGE_BY_PROVIDER_SQL;
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);

			ps = connection.prepareStatement(sqlQuery);
			result = ps.executeQuery();

			Map<String, UserApplicationAPIUsage> userApplicationUsages = new TreeMap<String,
					UserApplicationAPIUsage>();
			while (result.next()) {
				//int subId = result.getInt("SUBSCRIPTION_ID");
				//Map<String, String> keyData = getAccessTokenData(subId);
				//String accessToken = keyData.get("token");
				//String tokenStatus = keyData.get("status");
				String userId = result.getString("USER_ID");
				String application = result.getString(BlacklistWhitelistConstants.DAOConstants.APPNAME);
				int appId = result.getInt(BlacklistWhitelistConstants.DAOConstants.APPLICATION_ID);
				String subStatus = result.getString("SUB_STATUS");
				String subsCreateState = result.getString("SUBS_CREATE_STATE");
				String key = userId + "::" + application;
				UserApplicationAPIUsage usage = userApplicationUsages.get(key);
				if (usage == null) {
					usage = new UserApplicationAPIUsage();
					usage.setUserId(userId);
					usage.setApplicationName(application);
					usage.setAppId(appId);
					//usage.setAccessToken(accessToken);
					//usage.setAccessTokenStatus(tokenStatus);
					userApplicationUsages.put(key, usage);
				}

				APIIdentifier apiId = new APIIdentifier(result.getString(BlacklistWhitelistConstants.DAOConstants.API_PROVIDER), result.getString
						(BlacklistWhitelistConstants.DAOConstants.API_NAME) + "|" + result.getInt(BlacklistWhitelistConstants.DAOConstants.API_ID), result.getString(BlacklistWhitelistConstants.DAOConstants.API_VERSION));
				SubscribedAPI apiSubscription = new SubscribedAPI(new Subscriber(userId), apiId);
				apiSubscription.setSubStatus(subStatus);
				apiSubscription.setSubCreatedStatus(subsCreateState);
				//apiSubscription.setUUID(result.getString("SUB_UUID"));
				//apiSubscription.setTier(new Tier(result.getString("SUB_TIER_ID")));
				//Application applicationObj = new Application(result.getString("APP_UUID"));
				//apiSubscription.setApplication(applicationObj);
				usage.addApiSubscriptions(apiSubscription);
			}
			return userApplicationUsages;
		} catch (SQLException e) {
			throw new BusinessException(OparatorError.INVALID_OPARATOR_ID);
		} catch (Exception e) {
            throw new BusinessException(OparatorError.INVALID_OPARATOR_ID);
        } finally {
            DbUtils.closeAllConnections(ps, connection, result);
        }

    }


	public List<String> getAllAPIUsageByUser() throws BusinessException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		try {
			java.lang.String sqlQuery = SQLConstants.GET_APP_API_USER_SQL;
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);

			ps = connection.prepareStatement(sqlQuery);
			//ps.setString(1, APIUtil.replaceEmailDomainBack(providerName));
			result = ps.executeQuery();


			List<String> subscriberList = new ArrayList<String>();
			while (result.next()) {
				String userId = result.getString("USER_ID");
				if(!subscriberList.contains(userId)){
					subscriberList.add(userId);
				}
			}

			return subscriberList;
		} catch (SQLException e) {
			throw new BusinessException(OparatorError.INVALID_OPARATOR_ID);
		} catch (Exception e) {
			throw new BusinessException(OparatorError.INVALID_OPARATOR_ID);
		} finally {
            DbUtils.closeAllConnections(ps, connection, result);
        }

	}

	public int getAPIId(String providerName, String apiName, String apiVersion) throws BusinessException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		int result = -1;

		try {


			java.lang.String sqlQuery = SQLConstants.GET_API_ID_SQL;
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);

			ps = connection.prepareStatement(sqlQuery);
			ps.setString(1, APIUtil.replaceEmailDomainBack(providerName));
			//ps.setString(1, providerName);
			ps.setString(2, apiName);
			ps.setString(3, apiVersion);
			resultSet = ps.executeQuery();


			while (resultSet.next()) {
				result = resultSet.getInt("API_ID");
			}
		} catch (SQLException e) {
			throw new BusinessException(OparatorError.INVALID_OPARATOR_ID);
		} catch (Exception e) {
			throw new BusinessException(OparatorError.INVALID_OPARATOR_ID);
		} finally {
            DbUtils.closeAllConnections(ps, connection, resultSet);
        }

		return result;
	}


	public String[] getAPIInfo(int apiID) throws BusinessException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		String[] result = new String[0];

		try {


			java.lang.String sqlQuery = SQLConstants.GET_API_INFO_SQL;
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);

			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, apiID);
			resultSet = ps.executeQuery();

			String[] apiInfoArray = new String[3];

			while (resultSet.next()) {
				apiInfoArray[0] = resultSet.getString("API_PROVIDER");
				apiInfoArray[1] = resultSet.getString("API_NAME");
				apiInfoArray[2] = resultSet.getString("API_VERSION");

				result = apiInfoArray;
			}
		} catch (SQLException e) {
			throw new BusinessException(OparatorError.INVALID_OPARATOR_ID);
		} catch (Exception e) {
			throw new BusinessException(OparatorError.INVALID_OPARATOR_ID);
		} finally {
            DbUtils.closeAllConnections(ps, connection, resultSet);
        }

		return result;
	}


	public List<String> getAllAplicationsByUser(String userID) throws BusinessException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		try {
			java.lang.String sqlQuery = SQLConstants.GET_APP_USER_SUBSCRIPTION_SQL;
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);

			ps = connection.prepareStatement(sqlQuery);
			ps.setString(1, stripDomain(userID));
			result = ps.executeQuery();

			List<String> appUniqueIDList = new ArrayList<String>();
			while (result.next()) {
				String appName = result.getString(BlacklistWhitelistConstants.DAOConstants.APPNAME);
				int appID = result.getInt(BlacklistWhitelistConstants.DAOConstants.APPLICATION_ID);
				String appUniqueID = appID + ":" + appName;
				appUniqueIDList.add(appUniqueID);
			}

			return appUniqueIDList;
		} catch (SQLException e) {
			throw new BusinessException(OparatorError.INVALID_OPARATOR_ID);
		} catch (Exception e) {
			throw new BusinessException(OparatorError.INVALID_OPARATOR_ID);
		} finally {
            DbUtils.closeAllConnections(ps, connection, result);
        }
	}

	public List<String> getAllAplicationsByUserAndOperator(String userID, String operator) throws BusinessException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		try {
			String sqlQuery = SQLConstants.GET_APP_USER_OPERATOR_SUBSCRIPTION_SQL;
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);

			ps = connection.prepareStatement(sqlQuery);
			ps.setString(1, stripDomain(userID));
			ps.setString(2,operator);
			result = ps.executeQuery();

			List<String> appUniqueIDList = new ArrayList<String>();
			while (result.next()) {
				String appName = result.getString(BlacklistWhitelistConstants.DAOConstants.APPNAME);
				int appID = result.getInt(BlacklistWhitelistConstants.DAOConstants.APPLICATION_ID);
				String appUniqueID = appID + ":" + appName;
				appUniqueIDList.add(appUniqueID);
			}

			return appUniqueIDList;
		} catch (SQLException e) {
			throw new BusinessException(OparatorError.INVALID_OPARATOR_ID);
		} catch (Exception e) {
			throw new BusinessException(OparatorError.INVALID_OPARATOR_ID);
		} finally {
			DbUtils.closeAllConnections(ps, connection, result);
		}
	}


	public List<String> getAllApisByUserAndApp(String userID, String appID) throws BusinessException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		try {
			java.lang.String sqlQuery = SQLConstants.GET_API_FOR_USER_AND_APP_SQL;
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);

			ps = connection.prepareStatement(sqlQuery);
			ps.setString(1, APIUtil.replaceEmailDomainBack(userID));
			ps.setString(2, appID);
			result = ps.executeQuery();

			List<String> apiNamesList = new ArrayList<String>();
			while (result.next()) {
				String apiProvider = result.getString("API_PROVIDER");
				String apiName = result.getString("API_NAME");
				String apiVersion = result.getString("API_VERSION");
				int apiID = result.getInt("API_ID");

				String apiFullName = String.valueOf(apiID) + ":"+apiProvider +":"+ apiName + ":" + apiVersion;
				apiNamesList.add(apiFullName);
			}

			return apiNamesList;
		} catch (SQLException e) {
			throw new BusinessException(OparatorError.INVALID_OPARATOR_ID);
		} catch (Exception e) {
			throw new BusinessException(OparatorError.INVALID_OPARATOR_ID);
		} finally {
            DbUtils.closeAllConnections(ps, connection, result);
        }
	}

	/**
	 * Generate sp list.
	 *
	 * @return the array list
	 * @throws Exception the exception
	 */
	public List<String> generateSPNameList() throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet results = null;
		String sql = SQLConstants.GET_SP_NAMES;

		ArrayList<String> spList = new ArrayList<String>();

		try {
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
			ps = connection.prepareStatement(sql);
			results = ps.executeQuery();
			while (results.next()) {
			    spList.add(results.getString("authz_user"));
			}
			return spList;
		} catch (Exception e) {
			throw new BusinessException(OparatorError.INTERNAL_SERVER_ERROR);
		} finally {
			DbUtils.closeAllConnections(ps, connection, results);
		}
	}

    /**
     * Gets list of users with manage-app-admin role assigned to them
     */

    public List<String> getAdminUsers() throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = SQLConstants.GET_ADMIN_USERS;

        ArrayList<String> adminUserList = new ArrayList<String>();

        try {
            connection =DbUtils.getDbConnection(DataSourceNames.WSO2UM_DB);
            ps = connection.prepareStatement(sql.toString());
            results = ps.executeQuery();
            while (results.next()) {
                adminUserList.add(results.getString("um_user_name"));
            }
            return adminUserList;
        } catch (Exception e) {
            throw new BusinessException(OparatorError.INTERNAL_SERVER_ERROR);
        } finally {
            DbUtils.closeAllConnections(ps, connection, results);
        }
    }

	/**
	 * Gets the list of apps belonging to a particular SP
	 *
	 * @param SPname Service Provider's name
	 */

	public List<AppObject> getSPApps(String SPname) throws OperatorServiceException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet results = null;
		String sql = SQLConstants.GET_SP_APPS;
		List<AppObject> appList = new ArrayList<AppObject>();

		try {
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, SPname);
			results = ps.executeQuery();
			while (results.next()) {
				AppObject app = new AppObject();
				app.setApp_id(results.getInt("application_id"));
				app.setApp_name(results.getString("APP_NAME"));
				app.setAccess_token(results.getString("ACCESS_TOKEN"));
				app.setConsumer_key(results.getString("consumer_key"));
				app.setConsmer_secret(results.getString("consumer_secret"));
				appList.add(app);
			}
			return appList;
		} catch (Exception e) {
			throw new OperatorServiceException(e);
		} finally {
			DbUtils.closeAllConnections(ps, connection, results);
		}
	}

	public List<AppObject> getBlacklistedSpApps(String SPname) throws OperatorServiceException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet results = null;
		String sql = SQLConstants.GET_BLACKLISTED_SP_APPS;
		List<AppObject> appList = new ArrayList<AppObject>();

		try {
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, SPname);
			results = ps.executeQuery();
			while (results.next()) {
				AppObject app = new AppObject();
				app.setApp_id(results.getInt("application_id"));
				app.setApp_name(results.getString("APP_NAME"));
				app.setConsumer_key(results.getString("consumer_key"));
				app.setConsmer_secret(results.getString("consumer_secret"));
				appList.add(app);
			}
			return appList;
		} catch (Exception e) {
			throw new OperatorServiceException(e);
		} finally {
			DbUtils.closeAllConnections(ps, connection, results);
		}
	}

	public List<String> getBlacklistedSPList() throws OperatorServiceException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet results = null;
		String sql = SQLConstants.GET_BLACKLISTED_SP_LIST;

		ArrayList<String> blacklistedSpList = new ArrayList<String>();

		try {
			connection =DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
			ps = connection.prepareStatement(sql.toString());
			results = ps.executeQuery();
			while (results.next()) {
				blacklistedSpList.add(results.getString("USERNAME"));
			}
			return blacklistedSpList;
		} catch (Exception e) {
			throw new OperatorServiceException(e);
		} finally {
			DbUtils.closeAllConnections(ps, connection, results);
		}
	}

	/**
	 * Get access token data based on application ID
	 *
	 * @param subscriptionId Subscription Id
	 * @return access token data
	 * @throws Exception
	 */
/*	public Map<String, String>  getAccessTokenData(int subscriptionId) throws  Exception {
		Map<String, String> apiKeys = new HashMap<String, String>();

		if (checkAccessTokenPartitioningEnabled() && checkUserNameAssertionEnabled()) {
			String[] keyStoreTables = getAvailableKeyStoreTables();
			if (keyStoreTables != null) {
				for (String keyStoreTable : keyStoreTables) {
					apiKeys = getAccessTokenData(subscriptionId,
							getKeysSqlUsingSubscriptionId(keyStoreTable));
					if (apiKeys.size() > 0) {
						break;
					}
				}
			}
		} else {
			apiKeys = getAccessTokenData(subscriptionId, getKeysSqlUsingSubscriptionId(null));
		}
		return apiKeys;
	}*/

	/*private Map<String, String> getAccessTokenData(int subscriptionId, String getKeysSql)
			throws Exception {
		Connection connection  = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		Map<String, String> apiKeys = new HashMap<String, String>();
		try {
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
			ps = connection.prepareStatement(getKeysSql);
			ps.setInt(1, subscriptionId);
			result = ps.executeQuery();
			while (result.next()) {
				apiKeys.put("token", BlacklistWhitelistUtils.decryptToken(result.getString("ACCESS_TOKEN")));
				apiKeys.put("status", result.getString("TOKEN_STATE"));
			}
		} catch (SQLException e) {
			//handleException("Failed to get keys for application: " + subscriptionId, e);
			e.printStackTrace();
		}  finally {
			//APIMgtDBUtil.closeAllConnections(ps, connection, result);
			DbUtils.closeAllConnections(ps, connection, result);
		}
		return apiKeys;
	}
*/

	private String stripDomain(String userId){
		String id = userId;
		if(userId != null && userId.contains("@")){
			id = userId.split("@")[0];
		}
		return id;
	}

}
