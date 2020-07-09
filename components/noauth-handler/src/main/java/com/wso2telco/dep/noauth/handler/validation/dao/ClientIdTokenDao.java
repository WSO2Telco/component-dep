package com.wso2telco.dep.noauth.handler.validation.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.noauth.handler.validation.dto.ClientIdToken;

public class ClientIdTokenDao {
	private static final Log log = LogFactory.getLog(ClientIdTokenDao.class);

	public String getClientToken(Long clientId) throws Exception {
		log.debug("get Client Token " + clientId);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet results = null;
		String clientIdToken = null;

		String sql = "select ACCESS_TOKEN from idn_oauth2_access_token where CONSUMER_KEY_ID = ? and TOKEN_STATE = 'ACTIVE'";
		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
			ps = conn.prepareStatement(sql);
			ps.setLong(1, clientId);
			results = ps.executeQuery();

			while (results.next()) {
				clientIdToken = results.getString("ACCESS_TOKEN");
			}
		} catch (Exception e) {
			log.error("Error while getting summary ID list for client " + e);
			throw e;
		} finally {
			APIMgtDBUtil.closeAllConnections(ps, conn, results);
		}
		return clientIdToken;
	}

}
