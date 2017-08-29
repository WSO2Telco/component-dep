package com.wso2telco.dep.ratecardservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.ratecardservice.dao.model.APIDTO;
import com.wso2telco.dep.ratecardservice.util.DatabaseTables;

public class APIDAO {

	private final Log log = LogFactory.getLog(APIDAO.class);

	public List<APIDTO> getAPIs() throws Exception {

		List<APIDTO> apis = new ArrayList<APIDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select apiid, apiname, apidesc, createdby from ");
			query.append(DatabaseTables.API.getTObject());

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getAPIs : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				APIDTO api = new APIDTO();

				api.setApiId(rs.getInt("apiid"));
				api.setApiName(rs.getString("apiname"));
				api.setApiDescription(rs.getString("apidesc"));
				api.setCreatedBy(rs.getString("createdby"));

				apis.add(api);
			}
		} catch (SQLException e) {

			log.error("database operation error in getAPIs : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getAPIs : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return apis;
	}

	public APIDTO getAPI(int apiId) throws Exception {

		APIDTO api = null;

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select apiid, apiname, apidesc, createdby from ");
			query.append(DatabaseTables.API.getTObject());
			query.append(" where apiid = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getAPI : " + ps);

			ps.setInt(1, apiId);

			rs = ps.executeQuery();

			while (rs.next()) {

				api = new APIDTO();

				api.setApiId(rs.getInt("apiid"));
				api.setApiName(rs.getString("apiname"));
				api.setApiDescription(rs.getString("apidesc"));
				api.setCreatedBy(rs.getString("createdby"));
			}
		} catch (SQLException e) {

			log.error("database operation error in getAPI : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getAPI : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return api;
	}
}
