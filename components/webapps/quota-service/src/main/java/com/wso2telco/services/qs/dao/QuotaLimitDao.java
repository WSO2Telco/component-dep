package com.wso2telco.services.qs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.services.qs.entity.QuotaBean;

public class QuotaLimitDao {
	private static final Log log = LogFactory.getLog(QuotaLimitDao.class);

	public void addQuotaLimit(QuotaBean quotaBean) throws SQLException, Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO `sp_quota_limit` (`serviceProvider`,`operatorName`,`application`,`apiName`,`quota_limit`) VALUES (?,?,?,?,?);");
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			ps = conn.prepareStatement(sql.toString());
			conn.setAutoCommit(false);

			ps.setString(1, quotaBean.getServiceProvider());
			ps.setString(2, quotaBean.getOperator());
			ps.setString(3, quotaBean.getApplicationName());
			ps.setString(4, quotaBean.getApiName());
			ps.setString(5, quotaBean.getQuotaLimit());
			ps.executeUpdate();
			conn.commit();

		} catch (Exception e) {
			conn.rollback();
			log.error("error when inserting records to QuotaLimit table", e);
			throw e;
		} finally {
			DbUtils.closeAllConnections(ps, conn, null);
		}
	}

	public List<QuotaBean> getQuotaLimitInfoByServiceProvider(String info) throws Exception {
		StringBuilder sqlByServiceProvider=new StringBuilder();
		sqlByServiceProvider.append("SELECT * FROM sp_quota_limit where serviceProvider =? and application is null and apiName is null;");

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<QuotaBean> returnObjList=new ArrayList<QuotaBean>();

			try {
				conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
				ps = conn.prepareStatement(sqlByServiceProvider.toString());
				ps.setString(1, info);
				rs = ps.executeQuery();
				while (rs.next()) {
					QuotaBean returnObj_ = new QuotaBean();
					returnObj_.setServiceProvider(rs.getString("serviceProvider"));
					returnObj_.setOperator(rs.getString("operatorName"));
					returnObj_.setApplicationName(rs.getString("application"));
					returnObj_.setApiName(rs.getString("apiName"));
					returnObj_.setQuotaLimit(rs.getString("quota_limit"));
					returnObjList.add(returnObj_);
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				DbUtils.closeAllConnections(ps, conn, rs);
			}
		return returnObjList;
	}

	public List<QuotaBean> getQuotaLimitInfoByApplication(String info) throws Exception {
		StringBuilder sqlByApplication=new StringBuilder();
		sqlByApplication.append("SELECT * FROM sp_quota_limit where serviceProvider is not null and application =? and apiName is null;");

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<QuotaBean> returnObjList=new ArrayList<QuotaBean>();
			try {
				conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
				ps = conn.prepareStatement(sqlByApplication.toString());
				ps.setString(1, info);
				rs = ps.executeQuery();
				while (rs.next()) {
					QuotaBean returnObj_ = new QuotaBean();
					returnObj_.setServiceProvider(rs.getString("serviceProvider"));
					returnObj_.setOperator(rs.getString("operatorName"));
					returnObj_.setApplicationName(rs.getString("application"));
					returnObj_.setApiName(rs.getString("apiName"));
					returnObj_.setQuotaLimit(rs.getString("quota_limit"));
					returnObjList.add(returnObj_);
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				DbUtils.closeAllConnections(ps, conn, rs);
			}
		return returnObjList;
	}

	public List<QuotaBean> getQuotaLimitInfoByApi(String info) throws Exception {
		StringBuilder sqlByApi=new StringBuilder();
		sqlByApi.append("SELECT * FROM sp_quota_limit where serviceProvider is not null and application is not null and apiName =?; ");

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<QuotaBean> returnObjList=new ArrayList<QuotaBean>();
			try {
				conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
				ps = conn.prepareStatement(sqlByApi.toString());
				ps.setString(1, info);
				rs = ps.executeQuery();
				while (rs.next()) {
					QuotaBean returnObj_ = new QuotaBean();
					returnObj_.setServiceProvider(rs.getString("serviceProvider"));
					returnObj_.setOperator(rs.getString("operatorName"));
					returnObj_.setApplicationName(rs.getString("application"));
					returnObj_.setApiName(rs.getString("apiName"));
					returnObj_.setQuotaLimit(rs.getString("quota_limit"));
					returnObjList.add(returnObj_);
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				DbUtils.closeAllConnections(ps, conn, rs);

			}

		return returnObjList;
	}
}
