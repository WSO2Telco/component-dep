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
import com.wso2telco.dep.ratecardservice.dao.model.APIOperationDTO;
import com.wso2telco.dep.ratecardservice.util.DatabaseTables;

public class APIOperationDAO {

	private final Log log = LogFactory.getLog(APIOperationDAO.class);

	public APIOperationDTO getAPIOperation(int apiOperationId) throws Exception {

		APIOperationDTO apiOperation = null;

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select api_operation, api_operationcode, createdby from ");
			query.append(DatabaseTables.API_OPERATION.getTObject());
			query.append(" where api_operationid = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getAPIOperation : " + ps);

			ps.setInt(1, apiOperationId);

			rs = ps.executeQuery();

			while (rs.next()) {

				apiOperation = new APIOperationDTO();

				apiOperation.setApiOperationId(rs.getInt("api_operationid"));

				APIDTO api = new APIDTO();
				api.setApiId(rs.getInt("apiid"));
				apiOperation.setApi(api);

				apiOperation.setApiOperation(rs.getString("api_operation"));
				apiOperation.setApiOperationCode(rs.getString("api_operationcode"));
				apiOperation.setCreatedBy(rs.getString("createdby"));
			}
		} catch (SQLException e) {

			log.error("database operation error in getAPIOperation : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getAPIOperation : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return apiOperation;
	}

	public List<APIOperationDTO> getAPIOperations(String apiName) throws Exception {

		List<APIOperationDTO> apiOperations = new ArrayList<APIOperationDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder(
					"select apioperation.api_operationid, apioperation.apiid, apioperation.api_operation, apioperation.api_operationcode, apioperation.createdby from ");
			query.append(DatabaseTables.API_OPERATION.getTObject());
			query.append(" apioperation, ");
			query.append(DatabaseTables.API.getTObject());
			query.append(" api ");
			query.append(" where apioperation.apiid = api.apiid and api.apiname = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getAPIOperations : " + ps);

			ps.setString(1, apiName);

			rs = ps.executeQuery();

			while (rs.next()) {

				APIOperationDTO apiOperation = new APIOperationDTO();

				apiOperation.setApiOperationId(rs.getInt("api_operationid"));

				APIDTO api = new APIDTO();
				api.setApiId(rs.getInt("apiid"));
				apiOperation.setApi(api);

				apiOperation.setApiOperation(rs.getString("api_operation"));
				apiOperation.setApiOperationCode(rs.getString("api_operationcode"));
				apiOperation.setCreatedBy(rs.getString("createdby"));

				apiOperations.add(apiOperation);
			}
		} catch (SQLException e) {

			log.error("database operation error in getAPIOperations : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getAPIOperations : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return apiOperations;
	}
}
