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
import com.wso2telco.dep.operatorservice.model.BlacklistWhitelistCountResponseDTO;
import com.wso2telco.dep.operatorservice.model.BlacklistWhitelistDTO;
import com.wso2telco.dep.operatorservice.model.MSISDNValidationDTO;
import com.wso2telco.dep.operatorservice.util.BlacklistWhitelistConstants;
import com.wso2telco.dep.operatorservice.util.OparatorError;
import com.wso2telco.dep.operatorservice.util.OparatorTable;
import com.wso2telco.dep.operatorservice.util.SQLConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.dto.UserApplicationAPIUsage;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.api.model.SubscribedAPI;
import org.wso2.carbon.apimgt.api.model.Subscriber;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BlackListWhiteListDAO {

	private static final Log log = LogFactory.getLog(BlackListWhiteListDAO.class);

	/**
	 * blacklist list given msisdns
	 *
	 * @throws Exception
	 */
	public int blacklist(BlacklistWhitelistDTO dto)
			throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT IGNORE INTO ");
		sql.append(OparatorTable.API_BLACKLIST_WHITELIST.getTObject());
            sql.append("(api_id,app_id,msisdn,action, SERVICE_PROVIDER, user)");
		sql.append(" VALUES (?, ?, ?, ?,?, ?)");

		Connection conn = null;
		PreparedStatement ps = null;

		int success = 0;

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql.toString());

			conn.setAutoCommit(false);

			int batchLimit = 100;
			for (String msisdn : dto.getMsisdnList()) {

					ps.setString(1, dto.getApiID());
					ps.setString(2, dto.getAppId());
					ps.setString(3, msisdn);
					ps.setString(4, dto.getAction());
					ps.setString(5,dto.getServiceProvider());
					ps.setString(6,dto.getUser());

					ps.addBatch();

					batchLimit--;

					if(batchLimit == 0){
						int[] result = ps.executeBatch();
						for(int a : result){
							if(a==1){
								success++;
							}
						}
                        batchLimit = 100;
                    }
			}
			int[] result =  ps.executeBatch();

			for(int a : result){
				if(a==1){
					success++;
				}
			}

		} catch (Exception e) {
			if(conn != null){
                conn.rollback();
            }
			throw e;
		} finally {
			if(conn != null){
				conn.commit();
			}
			DbUtils.closeAllConnections(ps, conn, null);
		}

		return success;
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
				returnList.add(new MsisdnDTO(rs.getString("prefix"),
						rs.getString("msisdn")));
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
				returnList.add(new MsisdnDTO(rs.getString("PREFIX"),
						rs.getString(BlacklistWhitelistConstants.DAOConstants.MSISDN)));
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);

		}

		return returnList;
	}

	/**
	 * Retrieves the number of blacklist entries for a particular API ID
	 * @return
	 * @throws Exception
	 */
	public int getBlacklistCountByApi(BlacklistWhitelistDTO dto) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT count(*) as count");
		sql.append(" FROM ");
		sql.append(OparatorTable.API_BLACKLIST_WHITELIST.getTObject());
		sql.append(" WHERE 1=1 ");
		sql.append(" AND API_ID LIKE ? ");
		sql.append(" AND APP_ID LIKE ? ");
		sql.append(" AND ACTION = ? ");
		sql.append(" AND SERVICE_PROVIDER LIKE ? ");

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count=0;

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, dto.getApiID());
			ps.setString(2,dto.getAppId());
			ps.setString(3,dto.getAction());
			ps.setString(4,dto.getServiceProvider());
			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("count");
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);
		}

		return count;
	}

	public void streamBlacklistByApi(BlacklistWhitelistDTO dto,int offset, int limit, OutputStream out) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT apis.API_NAME, apps.NAME, apps.CREATED_BY, bw.MSISDN, bw.ACTION, bw.user, bw.CREATED, bw.LAST_MODIFIED");
		sql.append(" FROM ");
		sql.append(OparatorTable.API_BLACKLIST_WHITELIST.getTObject());
		sql.append(" AS bw, ");
		sql.append(DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB));
		sql.append( ".am_application " );
		sql.append(" AS apps, ");
		sql.append(DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB));
		sql.append( ".am_api " );
		sql.append(" AS apis ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND bw.API_ID LIKE ? ");
		sql.append(" AND bw.APP_ID LIKE ? ");
		sql.append(" AND bw.SERVICE_PROVIDER LIKE ? ");
		sql.append(" AND bw.ACTION = ? ");
		sql.append(" AND apps.application_id = app_id ");
		sql.append(" AND apis.API_ID = bw.API_ID ");
		sql.append(" LIMIT ?");
		sql.append(" OFFSET ?");

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql.toString(),ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);
			ps.setFetchSize(Integer.MIN_VALUE);

			ps.setString(1, dto.getApiID());
			ps.setString(2,dto.getAppId());
			ps.setString(3,dto.getServiceProvider());
			ps.setString(4,dto.getAction());
			ps.setInt(5, limit);
			ps.setInt(6, offset);

			rs = ps.executeQuery();

			if(rs.next()){
				StringBuilder sb = new StringBuilder();

				sb.append("API_NAME, NAME, CREATED_BY, MSISDN, ACTION, ADDED_BY, CREATED, LAST_MODIFIED");
				//MOVE WRITING TO STREAM TO SELF CONTAINED METHOD??
				out.write((sb.toString().getBytes()));
				out.write("\n".getBytes());
				out.flush();

				do{
					//IS THIS MORE EFFICIENT THAN STRING CONCAT??
                    sb = new StringBuilder();
					sb.append(rs.getString("API_NAME"));
					sb.append(",");
					sb.append(rs.getString("NAME"));
					sb.append(",");
					sb.append(rs.getString("CREATED_BY"));
					sb.append(",");
					sb.append(rs.getString("MSISDN"));
					sb.append(",");
					sb.append(rs.getString("ACTION"));
					sb.append(",");
                    sb.append(rs.getString("USER"));
                    sb.append(",");
					sb.append(rs.getString("CREATED"));
					sb.append(",");
					sb.append(rs.getString("LAST_MODIFIED"));

					out.write((sb.toString().getBytes()));
					out.write("\n".getBytes());
					out.flush();
				} while(rs.next());
			}


		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);
		}
	}

	public boolean checkIfBlacklistWhitelistExists(BlacklistWhitelistDTO dto) throws Exception {
		boolean isExist = false;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT EXISTS(SELECT 1 FROM ");
		sb.append(OparatorTable.API_BLACKLIST_WHITELIST.getTObject());
		sb.append(" WHERE ");
		sb.append("MSISDN = ? ");
		sb.append(" AND ");
		sb.append(" API_ID LIKE ? ");
		sb.append(" AND ");
		sb.append(" APP_ID LIKE ?");
		sb.append(" AND SERVICE_PROVIDER LIKE ?");
		sb.append(" AND ");
		sb.append(" action = ?) AS result");

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);

			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, dto.getMsisdnList().get(0));
			ps.setString(2, dto.getApiID());
			ps.setString(3, dto.getAppId());
			ps.setString(4,dto.getServiceProvider());
			ps.setString(5, dto.getAction());

			rs = ps.executeQuery();
			while (rs.next()) {
				isExist = rs.getBoolean("result");
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);
		}

		return isExist;
	}

	public BlacklistWhitelistCountResponseDTO getCount(BlacklistWhitelistDTO dto) throws Exception {

		StringBuilder sb = new StringBuilder();
		BlacklistWhitelistCountResponseDTO responseDTO = new BlacklistWhitelistCountResponseDTO();

		sb.append("SELECT count(*) AS count FROM ");
		sb.append(OparatorTable.API_BLACKLIST_WHITELIST.getTObject());
		sb.append(" WHERE ");
		sb.append(" API_ID LIKE ? ");
		sb.append(" AND ");
		sb.append(" APP_ID LIKE ? ");
		sb.append(" AND SERVICE_PROVIDER LIKE ?");
		sb.append(" AND ");
		sb.append(" action = ?");

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);

			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, dto.getApiID());
			ps.setString(2, dto.getAppId());
			ps.setString(3, dto.getServiceProvider());
			ps.setString(4, dto.getAction());

			rs = ps.executeQuery();

			while (rs.next()) {
				responseDTO.setCount(rs.getInt("count"));
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);

        }

        return responseDTO;
    }

	public void remove(BlacklistWhitelistDTO dto) throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(OparatorTable.API_BLACKLIST_WHITELIST.getTObject());
		sql.append(" WHERE API_ID LIKE ?");
		sql.append(" AND APP_ID LIKE ?");
		sql.append(" AND MSISDN = ?");
		sql.append(" AND ACTION = ?");
		sql.append(" AND SERVICE_PROVIDER LIKE ?");

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_STATS_DB);
			ps = conn.prepareStatement(sql.toString());

			ps.setString(1, dto.getApiID());
			ps.setString(2, dto.getAppId());
			ps.setString(3, dto.getMsisdnList().get(0));
			ps.setString(4, dto.getAction());
			ps.setString(5, dto.getServiceProvider());

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
					userApplicationUsages.put(key, usage);
				}

				APIIdentifier apiId = new APIIdentifier(result.getString(BlacklistWhitelistConstants.DAOConstants.API_PROVIDER), result.getString
						(BlacklistWhitelistConstants.DAOConstants.API_NAME) + "|" + result.getInt(BlacklistWhitelistConstants.DAOConstants.API_ID), result.getString(BlacklistWhitelistConstants.DAOConstants.API_VERSION));
				SubscribedAPI apiSubscription = new SubscribedAPI(new Subscriber(userId), apiId);
				apiSubscription.setSubStatus(subStatus);
				apiSubscription.setSubCreatedStatus(subsCreateState);
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
				String spName = result.getString(BlacklistWhitelistConstants.DAOConstants.USER_ID);
				String appUniqueID = appID + ":" + appName + ":" + spName;
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

	private String stripDomain(String userId){
		String id = userId;
		if(userId != null && userId.contains("@")){
			id = userId.split("@")[0];
		}
		return id;
	}

}
