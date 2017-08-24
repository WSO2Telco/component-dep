package com.wso2telco.services.qs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.usage.client.exception.APIMgtUsageQueryServiceClientException;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.services.qs.entity.QuotaBean;

public class QuotaLimitDao {
	private static final Log log = LogFactory.getLog(QuotaLimitDao.class);

	public void addQuotaLimit(QuotaBean quotaBean) throws SQLException, Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO `sp_quota_limit` (`serviceProvider`,`operatorName`,`application`,`apiName`,`quota_limit`,`fromDate`,`toDate`) VALUES (?,?,?,?,?,?,?);");
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
			ps.setString(6, quotaBean.getFromDate());
			ps.setString(7, quotaBean.getToDate());

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

	public List<QuotaBean> getQuotaLimitInfoByServiceProvider(String info, String operator) throws Exception {
		StringBuilder sqlByServiceProvider=new StringBuilder();

		if (operator.equalsIgnoreCase("_ALL_")) {
			sqlByServiceProvider.append("SELECT * FROM sp_quota_limit where serviceProvider =? and application is null and apiName is null");
		}else {
			sqlByServiceProvider.append("SELECT * FROM sp_quota_limit where serviceProvider =? and application is null and apiName is null and operatorName=?;");
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<QuotaBean> returnObjList=new ArrayList<QuotaBean>();

			try {
				conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
				ps = conn.prepareStatement(sqlByServiceProvider.toString());
				ps.setString(1, info);
				if (!operator.equalsIgnoreCase("_ALL_")) {
					ps.setString(2, operator);
				}
				rs = ps.executeQuery();
				while (rs.next()) {
					QuotaBean returnObj_ = new QuotaBean();
					returnObj_.setServiceProvider(rs.getString("serviceProvider"));
					returnObj_.setOperator(rs.getString("operatorName"));
					returnObj_.setApplicationName(rs.getString("application"));
					returnObj_.setApiName(rs.getString("apiName"));
					returnObj_.setQuotaLimit(rs.getString("quota_limit"));
					returnObj_.setFromDate(rs.getString("fromDate"));
					returnObj_.setToDate(rs.getString("toDate"));

					returnObjList.add(returnObj_);
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				DbUtils.closeAllConnections(ps, conn, rs);
			}
		return returnObjList;
	}

	public List<QuotaBean> getQuotaLimitInfoByApplication(String info, String operator) throws Exception {
		StringBuilder sqlByApplication=new StringBuilder();
		if (operator.equalsIgnoreCase("_ALL_")) {
			sqlByApplication.append("SELECT * FROM sp_quota_limit where serviceProvider is not null and application =? and apiName is null");
		}else {
			sqlByApplication.append("SELECT * FROM sp_quota_limit where serviceProvider is not null and application =? and apiName is null and operatorName=?;");
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<QuotaBean> returnObjList=new ArrayList<QuotaBean>();
			try {
				conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
				ps = conn.prepareStatement(sqlByApplication.toString());
				ps.setString(1, info);
				if (!operator.equalsIgnoreCase("_ALL_")) {
					ps.setString(2, operator);
				}
				rs = ps.executeQuery();
				while (rs.next()) {
					QuotaBean returnObj_ = new QuotaBean();
					returnObj_.setServiceProvider(rs.getString("serviceProvider"));
					returnObj_.setOperator(rs.getString("operatorName"));
					returnObj_.setApplicationName(rs.getString("application"));
					returnObj_.setApiName(rs.getString("apiName"));
					returnObj_.setQuotaLimit(rs.getString("quota_limit"));
					returnObj_.setFromDate(rs.getString("fromDate"));
					returnObj_.setToDate(rs.getString("toDate"));
					returnObjList.add(returnObj_);
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				DbUtils.closeAllConnections(ps, conn, rs);
			}
		return returnObjList;
	}

	public List<QuotaBean> getQuotaLimitInfoByApi(String info, String operator) throws Exception {
		StringBuilder sqlByApi=new StringBuilder();
		if (operator.equalsIgnoreCase("_ALL_")) {
			sqlByApi.append("SELECT * FROM sp_quota_limit where serviceProvider is not null and application is not null and apiName =? ");
		}else {
			sqlByApi.append("SELECT * FROM sp_quota_limit where serviceProvider is not null and application is not null and apiName =? and operatorName=?; ");
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<QuotaBean> returnObjList=new ArrayList<QuotaBean>();
			try {
				conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
				ps = conn.prepareStatement(sqlByApi.toString());
				ps.setString(1, info);
				if (!operator.equalsIgnoreCase("_ALL_")) {
					ps.setString(2, operator);
				}
				rs = ps.executeQuery();
				while (rs.next()) {
					QuotaBean returnObj_ = new QuotaBean();
					returnObj_.setServiceProvider(rs.getString("serviceProvider"));
					returnObj_.setOperator(rs.getString("operatorName"));
					returnObj_.setApplicationName(rs.getString("application"));
					returnObj_.setApiName(rs.getString("apiName"));
					returnObj_.setQuotaLimit(rs.getString("quota_limit"));
					returnObj_.setFromDate(rs.getString("fromDate"));
					returnObj_.setToDate(rs.getString("toDate"));
					returnObjList.add(returnObj_);
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				DbUtils.closeAllConnections(ps, conn, rs);

			}

		return returnObjList;
	}

	public Boolean checkQuotaLimitInfoByServiceProviderWithDateRange(String info,String fromDate, String toDate, String operator) throws Exception {
		StringBuilder sqlByServiceProvider=new StringBuilder();
		if (operator.equalsIgnoreCase("_ALL_")) {
			sqlByServiceProvider.append("SELECT * FROM sp_quota_limit where serviceProvider =? and application is null and apiName is null and ((? <= toDate and ? >= fromDate));");
		}else {
			sqlByServiceProvider.append("SELECT * FROM sp_quota_limit where serviceProvider =? and application is null and apiName is null and ((? <= toDate and ? >= fromDate)) and operatorName=?;");
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Boolean ifOverlapped=false;

			try {
				conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
				ps = conn.prepareStatement(sqlByServiceProvider.toString());
				ps.setString(1, info);
				ps.setString(2, fromDate);
				ps.setString(3, toDate);
				if (!operator.equalsIgnoreCase("_ALL_")) {
					ps.setString(4, operator);
				}
				rs = ps.executeQuery();
				if (rs.next()) {
					ifOverlapped=true;
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				DbUtils.closeAllConnections(ps, conn, rs);
			}
		return ifOverlapped;
	}

	public Boolean checkQuotaLimitInfoByApplicationWithDateRange(String info,String fromDate, String toDate, String operator) throws Exception {
		StringBuilder sqlByApplication=new StringBuilder();
		if (operator.equalsIgnoreCase("_ALL_")) {
			sqlByApplication.append("SELECT * FROM sp_quota_limit where serviceProvider is not null and application =? and apiName is null and ((? <= toDate and ? >= fromDate));");
		}else {
			sqlByApplication.append("SELECT * FROM sp_quota_limit where serviceProvider is not null and application =? and apiName is null and ((? <= toDate and ? >= fromDate)) and operatorName=?;");
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Boolean ifOverlapped=false;
			try {
				conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
				ps = conn.prepareStatement(sqlByApplication.toString());
				ps.setString(1, info);
				ps.setString(2, fromDate);
				ps.setString(3, toDate);
				if (!operator.equalsIgnoreCase("_ALL_")) {
					ps.setString(4, operator);
				}
				rs = ps.executeQuery();
				if (rs.next()) {
					ifOverlapped=true;
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				DbUtils.closeAllConnections(ps, conn, rs);
			}
		return ifOverlapped;
	}

	public Boolean checkQuotaLimitInfoByApiWithDateRange(String info,String fromDate, String toDate, String operator) throws Exception {
		StringBuilder sqlByApi=new StringBuilder();
		if (operator.equalsIgnoreCase("_ALL_")) {
			sqlByApi.append("SELECT * FROM sp_quota_limit where serviceProvider is not null and application is not null and apiName =? and ((? <= toDate and ? >= fromDate)); ");
		}else {
			sqlByApi.append("SELECT * FROM sp_quota_limit where serviceProvider is not null and application is not null and apiName =? and ((? <= toDate and ? >= fromDate)) and operatorName=?; ");
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Boolean ifOverlapped=false;
			try {
				conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
				ps = conn.prepareStatement(sqlByApi.toString());
				ps.setString(1, info);
				ps.setString(2, fromDate);
				ps.setString(3, toDate);
				if (!operator.equalsIgnoreCase("_ALL_")) {
					ps.setString(4, operator);
				}
				rs = ps.executeQuery();
				if (rs.next()) {
					ifOverlapped=true;
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				DbUtils.closeAllConnections(ps, conn, rs);

			}

		return ifOverlapped;
	}

    public static List<String> getOperatorNamesByApplication(int applicationId) throws Exception, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT opco.operatorname FROM operatorapps opcoApp INNER JOIN operators opco ON opcoApp.operatorid = opco.id  WHERE opcoApp.applicationid =? AND opcoApp.isactive = 1";
        List<String> operatorNames = new ArrayList<String>();
        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, applicationId);
            log.debug("getOperatorNamesByApplication");
            results = ps.executeQuery();
            while (results.next()) {
                String temp = results.getString("operatorname");
                operatorNames.add(temp);
            }
        } catch (Exception e) {
        	log.error("Error occured while getting operator names from the database" + e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }

        return operatorNames;
    }

    public static List<Integer> getApplicationsByOperator(String operatorName) throws Exception, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String sql = "SELECT opcoApp.applicationid FROM operatorapps opcoApp INNER JOIN operators opco ON opcoApp.operatorid = opco.id WHERE opco.operatorname =? AND opcoApp.isactive = 1";
        List<Integer> applicationIds = new ArrayList<Integer>();
        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
            ps = conn.prepareStatement(sql);
            ps.setString(1, operatorName);
            log.debug("getApplicationsByOperator");
            results = ps.executeQuery();
            while (results.next()) {
                int temp = results.getInt("applicationid");
                applicationIds.add(temp);
            }
        } catch (Exception e) {
        	log.error("Error occured while getting application ids from the database" + e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, results);
        }
        return applicationIds;
    }

	public static List<String> getAllSubscribers() {
		StringBuilder sql = new StringBuilder();
        sql.append("select USER_ID from AM_SUBSCRIBER");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> subscriber = new ArrayList<String>();
        try {
            conn = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                subscriber.add(rs.getString("USER_ID"));
            }
        } catch (Exception e) {
        	log.error("getAllSubscriptions", e);
        } finally {
            DbUtils.closeAllConnections(ps, conn, rs);
        }
        return subscriber;
	}
}
