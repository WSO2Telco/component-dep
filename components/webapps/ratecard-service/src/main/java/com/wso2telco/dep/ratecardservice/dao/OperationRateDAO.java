package com.wso2telco.dep.ratecardservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.ratecardservice.dao.model.APIOperationDTO;
import com.wso2telco.dep.ratecardservice.dao.model.OperationRateDTO;
import com.wso2telco.dep.ratecardservice.dao.model.OperatorDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;
import com.wso2telco.dep.ratecardservice.util.DatabaseTables;

public class OperationRateDAO {

	private final Log log = LogFactory.getLog(OperationRateDAO.class);

	public List<OperationRateDTO> getOperationRates(String apiName) throws Exception {

		List<OperationRateDTO> operationRates = new ArrayList<OperationRateDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder(
					"select operation_rateid, operator_id, api_operationid, rate_defid, createdby from ");
			query.append(DatabaseTables.OPERATION_RATE.getTObject());
			query.append(" operationrate, ");
			query.append(DatabaseTables.API.getTObject());
			query.append(" api, ");
			query.append(DatabaseTables.API_OPERATION.getTObject());
			query.append(" apioperation ");
			query.append(
					" where api.apiid = apioperation.apiid and operationrate.api_operationid = apioperation.api_operationid and api.apiname = ? and operationrate.operator_id is null");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getOperationRatesByAPIName : " + ps);

			ps.setString(1, apiName);

			rs = ps.executeQuery();

			while (rs.next()) {

				OperationRateDTO operationRate = new OperationRateDTO();

				operationRate.setOperationRateId(rs.getInt("operation_rateid"));
				operationRate.setCreatedBy(rs.getString("createdby"));

				OperatorDTO operator = new OperatorDTO();
				operator.setOperatorId(rs.getInt("operator_id"));
				operationRate.setOperator(operator);

				APIOperationDTO apiOperation = new APIOperationDTO();
				apiOperation.setApiOperationId(rs.getInt("api_operationid"));
				operationRate.setApiOperation(apiOperation);

				RateDefinitionDTO rateDefinition = new RateDefinitionDTO();
				rateDefinition.setRateDefId(rs.getInt("rate_defid"));
				operationRate.setRateDefinition(rateDefinition);

				operationRates.add(operationRate);
			}
		} catch (SQLException e) {

			log.error("database operation error in getOperationRatesByAPIName : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getOperationRatesByAPIName : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return operationRates;
	}

	public List<OperationRateDTO> getOperationRates(String apiName, String operatorName) throws Exception {

		List<OperationRateDTO> operationRates = new ArrayList<OperationRateDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder(
					"select operation_rateid, operator_id, api_operationid, rate_defid, createdby from ");
			query.append(DatabaseTables.OPERATION_RATE.getTObject());
			query.append(" operationrate, ");
			query.append(DatabaseTables.API.getTObject());
			query.append(" api, ");
			query.append(DatabaseTables.OPERATOR.getTObject());
			query.append(" operator, ");
			query.append(DatabaseTables.API_OPERATION.getTObject());
			query.append(" apioperation ");
			query.append(
					" where api.apiid = apioperation.apiid and operationrate.api_operationid = apioperation.api_operationid and operationrate.operator_id = operator.operatorId and api.apiname = ? and operator.operatorname = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getOperationRatesByAPIName : " + ps);

			ps.setString(1, apiName);
			ps.setString(2, operatorName);

			rs = ps.executeQuery();

			while (rs.next()) {

				OperationRateDTO operationRate = new OperationRateDTO();

				operationRate.setOperationRateId(rs.getInt("operation_rateid"));
				operationRate.setCreatedBy(rs.getString("createdby"));

				OperatorDTO operator = new OperatorDTO();
				operator.setOperatorId(rs.getInt("operator_id"));
				operationRate.setOperator(operator);

				APIOperationDTO apiOperation = new APIOperationDTO();
				apiOperation.setApiOperationId(rs.getInt("api_operationid"));
				operationRate.setApiOperation(apiOperation);

				RateDefinitionDTO rateDefinition = new RateDefinitionDTO();
				rateDefinition.setRateDefId(rs.getInt("rate_defid"));
				operationRate.setRateDefinition(rateDefinition);

				operationRates.add(operationRate);
			}
		} catch (SQLException e) {

			log.error("database operation error in getOperationRatesByAPIName : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getOperationRatesByAPIName : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return operationRates;
	}

	public OperationRateDTO addOperationRate(OperationRateDTO operationRate) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer operationRateId = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("insert into ");
			query.append(DatabaseTables.OPERATION_RATE.getTObject());
			query.append(" (operator_id, api_operationid, rate_defid, createdby)");
			query.append(" values");
			query.append(" (?, ?, ?, ?)");

			ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in addOperationRate : " + ps);

			Integer operatorId = operationRate.getOperator().getOperatorId();
			if (operatorId != null) {
				ps.setInt(1, operatorId);
			} else {
				ps.setNull(1, Types.INTEGER);
			}

			ps.setInt(2, operationRate.getApiOperation().getApiOperationId());
			ps.setInt(3, operationRate.getRateDefinition().getRateDefId());
			ps.setString(4, operationRate.getCreatedBy());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				operationRateId = rs.getInt(1);
			}

			operationRate.setOperationRateId(operationRateId);
		} catch (SQLException e) {

			log.error("database operation error in addOperationRate : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in addOperationRate : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return operationRate;
	}

	public OperationRateDTO getOperationRate(int operationRateId) throws Exception {

		OperationRateDTO operationRate = null;

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder(
					"select operation_rateid, operator_id, api_operationid, rate_defid, createdby from ");
			query.append(DatabaseTables.OPERATION_RATE.getTObject());
			query.append(" where operation_rateid = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getOperationRate : " + ps);

			ps.setInt(1, operationRateId);

			rs = ps.executeQuery();

			while (rs.next()) {

				operationRate = new OperationRateDTO();

				operationRate.setOperationRateId(rs.getInt("operation_rateid"));

				OperatorDTO operator = new OperatorDTO();
				operator.setOperatorId(rs.getInt("operator_id"));
				operationRate.setOperator(operator);

				APIOperationDTO apiOperation = new APIOperationDTO();
				apiOperation.setApiOperationId(rs.getInt("api_operationid"));
				operationRate.setApiOperation(apiOperation);

				RateDefinitionDTO rateDefinition = new RateDefinitionDTO();
				rateDefinition.setRateDefId(rs.getInt("rate_defid"));
				operationRate.setRateDefinition(rateDefinition);

				operationRate.setCreatedBy(rs.getString("createdby"));
			}
		} catch (SQLException e) {

			log.error("database operation error in getOperationRate : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getOperationRate : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return operationRate;
	}
}
