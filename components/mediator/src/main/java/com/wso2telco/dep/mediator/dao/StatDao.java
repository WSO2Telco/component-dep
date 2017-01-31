package com.wso2telco.dep.mediator.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;

public class StatDao {

	private final Log log = LogFactory.getLog(StatDao.class);

	public String getAPIId(String apiName, String apiVersion) throws SQLException, Exception {

		Connection connection = null;
		PreparedStatement ps = null;
		String apiId = null;
		ResultSet resultSet = null;

		try {
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);

			StringBuffer sqlBuilder = new StringBuffer();

			sqlBuilder.append("select API_ID ");
			sqlBuilder.append(" FROM am_api ");
			sqlBuilder.append(" WHERE  API_NAME= ?");
			sqlBuilder.append(" AND API_VERSION = ?");

			ps = connection.prepareStatement(sqlBuilder.toString());

			ps.setString(1, apiName);
			ps.setString(2, apiVersion);

			resultSet = ps.executeQuery();

			if (resultSet.next()) {
				apiId = resultSet.getString(1);
			}

		} catch (SQLException ex) {
			log.error("####STATINJECTION#### Error while retrieving API ID", ex);
			throw ex;
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving API ID", ex);
			throw ex;
		} finally {
			DbUtils.closeAllConnections(ps, connection, resultSet);
		}

		return apiId;
	}

	public String getServiceProviderId(String spUsername) throws Exception {

		Connection connection = null;
		PreparedStatement ps = null;
		String publisherId = null;
		ResultSet resultSet = null;

		try {
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);

			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append("select SUBSCRIBER_ID ");
			queryBuilder.append(" FROM am_subscriber ");
			queryBuilder.append(" WHERE USER_ID = ? ");

			ps = connection.prepareStatement(queryBuilder.toString());

			ps.setString(1, spUsername);

			resultSet = ps.executeQuery();

			if (resultSet.next()) {
				publisherId = resultSet.getString(1);
			}

		} catch (SQLException ex) {
			log.error("####STATINJECTION#### Error while retrieving Service Provider Id", ex);
			throw ex;
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving Service Provider Id", ex);
			throw ex;
		} finally {
			DbUtils.closeAllConnections(ps, connection, resultSet);
		}

		return publisherId;
	}

	public String getOperatorName(String operatorId) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		String operatorName = null;
		ResultSet resultSet = null;

		try {
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append("SELECT operatorname");
			queryBuilder.append(" FROM operators ");
			queryBuilder.append(" WHERE id = ?");

			ps = connection.prepareStatement(queryBuilder.toString());

			ps.setString(1, operatorId);

			resultSet = ps.executeQuery();

			if (resultSet.next()) {
				operatorName = resultSet.getString(1);
			}

		} catch (SQLException ex) {
			log.error("####STATINJECTION#### Error while retrieving Operator name", ex);
			throw ex;
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving Operator name", ex);
			throw ex;
		} finally {
			DbUtils.closeAllConnections(ps, connection, resultSet);
		}

		return operatorName;
	}

	public String getServiceProviderConsumerKey(String applicationId) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		String consumerKey = null;

		try {
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);

			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append(" SELECT CONSUMER_KEY ");
			queryBuilder.append(" FROM am_application_key_mapping ");
			queryBuilder.append(" WHERE APPLICATION_ID=? ");

			ps = connection.prepareStatement(queryBuilder.toString());
			
			ps.setString(1, applicationId);
			
			resultSet = ps.executeQuery();
			
			if (resultSet.next()) {
				consumerKey = resultSet.getString(1);
			}

		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving Service Provider Consumer Key", ex);
			throw ex;
		} finally {
			DbUtils.closeAllConnections(ps, connection, resultSet);
		}

		return consumerKey;
	}
	
	public String getAPIPublisherId (String apiPublisherUserName) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		String apiPublisherId = null;
		
		try {
			connection = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
			
			StringBuilder queryBuilder = new StringBuilder();
			
			queryBuilder.append("select um_id ");
			queryBuilder.append(" from um_user ");
			queryBuilder.append(" where UM_USER_NAME=?");
			
			ps = connection.prepareStatement(queryBuilder.toString());
			
			ps.setString(1, apiPublisherUserName);
			
			resultSet = ps.executeQuery();
			
			if (resultSet.next()) {
				apiPublisherId = resultSet.getString(1);
			}
			
		} catch (Exception ex) {
			log.error("####STATINJECTION#### Error while retrieving Service Provider Consumer Key", ex);
			throw ex;
		} finally {
			DbUtils.closeAllConnections(ps, connection, resultSet);
		}
		
		
		return apiPublisherId;
	}

}
