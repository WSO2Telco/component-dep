package com.wso2telco.dep.ipvalidate.handler.validation.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.ClientIPPool;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.ClientIPRange;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.ClientIPSummary;

public class IPValidationDao {

	private static final Log log = LogFactory.getLog(IPValidationDao.class);

	public ArrayList<String> getClientIdList() throws Exception {
		log.debug("Get Client id list ");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet results = null;
		ArrayList<String> clientKeyList = new ArrayList<String>();

		String sql = "select distinct client_id from client_id_summary";
		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			ps = conn.prepareStatement(sql);
			results = ps.executeQuery();

			while (results.next()) {
				clientKeyList.add(results.getString("client_id"));
			}
		} catch (Exception e) {
			log.error("Error while getting id list " + e);
			throw e;
		} finally {
			APIMgtDBUtil.closeAllConnections(ps, conn, results);
		}
		return clientKeyList;
	}

	public List<ClientIPSummary> getSumaryListForClient(String clientId) throws Exception {
		log.debug("Get summary ID list for client : " + clientId);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet results = null;
		List<ClientIPSummary> clientIPSummaryList = new ArrayList<ClientIPSummary>();
		String sql = "select * from client_id_summary where client_id = ?";
		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			ps = conn.prepareStatement(sql);
			ps.setString(1, clientId);
			results = ps.executeQuery();

			while (results.next()) {
				ClientIPSummary clientIpSummary = new ClientIPSummary();

				clientIpSummary.setClientId(results.getString("client_id"));
				clientIpSummary.setSummaryId(results.getInt("ID"));
				clientIpSummary.setClientToken(results.getString("client_token"));
				clientIpSummary.setValidationEnabled(results.getBoolean("ip_validation_enabled"));

				clientIPSummaryList.add(clientIpSummary);
			}
		} catch (Exception e) {
			log.error("Error while getting summary ID list for client " + e);
			throw e;
		} finally {
			APIMgtDBUtil.closeAllConnections(ps, conn, results);
		}
		return clientIPSummaryList;
	}

	public ClientIPPool getPoolIPListBySummaryId(int summaryId) throws Exception {
		log.debug("Get get pool IP list for summary id : " + summaryId);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet results = null;
		ClientIPPool clientIPPool = new ClientIPPool();
		clientIPPool.setSummaryId(summaryId);

		String sql = "select ip from client_ip_pool_mapping m, ip_pool p where m.ip_pool_id = p.ip_pool_id and m.summary_id = ?";
		try {
			ArrayList<String> poolIPList = new ArrayList<String>();
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, summaryId);
			results = ps.executeQuery();
			while (results.next()) {
				poolIPList.add(results.getString("ip"));
			}
			clientIPPool.setIp(poolIPList);
		} catch (Exception e) {
			log.error("Error while getting pool IP list for summary id " + e);
			throw e;
		} finally {
			APIMgtDBUtil.closeAllConnections(ps, conn, results);
		}
		return clientIPPool;
	}

	public List<ClientIPRange> getRangeIPListBySummaryId(int summaryId) throws Exception {
		log.debug("Get get range IP list for summary id : " + summaryId);
		log.info("Get get range IP list for summary id : " + summaryId);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet results = null;
		List<ClientIPRange> clientIPRangeList = new ArrayList<ClientIPRange>();

		String sql = "select start_ip,end_ip from client_ip_range_mapping m, ip_range p where m.ip_range_id = p.ip_range_id and m.summary_id = ?";
		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, summaryId);
			results = ps.executeQuery();
			while (results.next()) {
				ClientIPRange clientIPRange = new ClientIPRange();
				clientIPRange.setSummaryId(summaryId);
				clientIPRange.setStartIP(results.getString("start_ip"));
				clientIPRange.setEndIP(results.getString("end_ip"));

				clientIPRangeList.add(clientIPRange);
			}
		} catch (Exception e) {
			log.error("Error while getting range IP list for summary id " + e);
			throw e;
		} finally {
			APIMgtDBUtil.closeAllConnections(ps, conn, results);
		}
		return clientIPRangeList;
	}

}
