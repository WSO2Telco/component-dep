package com.wso2telco.dep.ratecardservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mysql.jdbc.Statement;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.ratecardservice.dao.model.ApplicationSubcriptionsDTO;


public class ApplicationSubcriptionsDAO {

	private final Log log = LogFactory.getLog(ApplicationSubcriptionsDAO.class);

	public List<ApplicationSubcriptionsDTO> getSBRates(String appId, String operatorId,String apiName, String version) throws BusinessException{

		List<ApplicationSubcriptionsDTO> applicationSubcriptionsDTOs = new ArrayList<ApplicationSubcriptionsDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder("select rate_def_temp.operatorid,api_operation_temp.api_operationid,rate_def_temp.applicationid,rate_def_temp.rate_defid,api_operation_temp.api_operation,rate_def_temp.rate_defname,rate_def_temp.updatedby,rate_def_temp.createdby  from "
					+"( " 
					+"	select * from api_operation "
					+ "inner join api on api_operation.apiid=api.apiid  where apiname=?  and apiversion=?"
					+") as api_operation_temp "
					+"left join ( "
					+"select sub_rate_sb.api_operationid,rate_def.rate_defname,sub_rate_sb.applicationid,sub_rate_sb.rate_defid,sub_rate_sb.operatorid,sub_rate_sb.updatedby,sub_rate_sb.createdby from sub_rate_sb " 
					+"inner join rate_def on sub_rate_sb.rate_defid=rate_def.rate_defid  where sub_rate_sb.operatorid=? and sub_rate_sb.applicationid=? "
					+") as rate_def_temp on api_operation_temp.api_operationid=rate_def_temp.api_operationid");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getAPI : " + ps);

			ps.setString(1, apiName);
			ps.setString(2, version);
			ps.setString(3, operatorId);
			ps.setString(4, appId);

			rs = ps.executeQuery();

			while (rs.next()) {

				ApplicationSubcriptionsDTO applicationSubcriptionsDTO = new ApplicationSubcriptionsDTO();

				applicationSubcriptionsDTO.setApiOperation(rs.getString("api_operation"));
				applicationSubcriptionsDTO.setRateDefname(rs.getString("rate_defname"));

				applicationSubcriptionsDTO.setApiOperationId(rs.getInt("api_operationid"));
				applicationSubcriptionsDTO.setApplicationId(rs.getInt("applicationid"));
				applicationSubcriptionsDTO.setOperatorId(rs.getInt("operatorid"));
				applicationSubcriptionsDTO.setRateDefId(rs.getInt("rate_defid"));
				applicationSubcriptionsDTO.setCreateBy(rs.getString("updatedby"));
				applicationSubcriptionsDTO.setUpdateBy(rs.getString("createdby"));

				applicationSubcriptionsDTOs.add(applicationSubcriptionsDTO);
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

		return applicationSubcriptionsDTOs;
	} 


	public List<ApplicationSubcriptionsDTO> getNBRates(String appId,String apiName, String version) throws BusinessException{

		List<ApplicationSubcriptionsDTO> applicationSubcriptionsDTOs = new ArrayList<ApplicationSubcriptionsDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder("select api_operation_temp.api_operationid,rate_def_temp.applicationid,rate_def_temp.rate_defid,api_operation_temp.api_operation,rate_def_temp.rate_defname,rate_def_temp.updatedby,rate_def_temp.createdby  from "
					+"(" 
					+"	select api_operation.api_operationid,api_operation.api_operation from api_operation "
					+ "inner join api on api_operation.apiid=api.apiid  where apiname=? and apiversion=? "
					+") as api_operation_temp "
					+"left join ( "
					+"select sub_rate_nb.applicationid,sub_rate_nb.api_operationid,rate_def.rate_defname,sub_rate_nb.rate_defid,sub_rate_nb.updatedby,sub_rate_nb.createdby from sub_rate_nb " 
					+"inner join rate_def on sub_rate_nb.rate_defid=rate_def.rate_defid  where sub_rate_nb.applicationid=? "
					+") as rate_def_temp on api_operation_temp.api_operationid=rate_def_temp.api_operationid");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getAPI : " + ps);

			ps.setString(1, apiName);
			ps.setString(2, version);
			ps.setString(3, appId);

			rs = ps.executeQuery();

			while (rs.next()) {

				ApplicationSubcriptionsDTO applicationSubcriptionsDTO = new ApplicationSubcriptionsDTO();

				applicationSubcriptionsDTO.setApiOperation(rs.getString("api_operation"));
				applicationSubcriptionsDTO.setRateDefname(rs.getString("rate_defname"));

				applicationSubcriptionsDTO.setApiOperationId(rs.getInt("api_operationid"));
				applicationSubcriptionsDTO.setApplicationId(rs.getInt("applicationid"));
				applicationSubcriptionsDTO.setRateDefId(rs.getInt("rate_defid"));
				applicationSubcriptionsDTO.setCreateBy(rs.getString("updatedby"));
				applicationSubcriptionsDTO.setUpdateBy(rs.getString("createdby"));

				applicationSubcriptionsDTOs.add(applicationSubcriptionsDTO);
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

		return applicationSubcriptionsDTOs;
	}

	public ApplicationSubcriptionsDTO updateSBRates(ApplicationSubcriptionsDTO applicationSubcriptionsRate) throws BusinessException{

		ApplicationSubcriptionsDTO applicationSubcriptionsDTO = new ApplicationSubcriptionsDTO();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}


			

			StringBuilder query = new StringBuilder("UPDATE sub_rate_sb ");
			query.append("UPDATE sub_rate_sb ");
			query.append("SET rate_defid = ?, ");
			query.append("updatedby = ? ");
			query.append("WHERE operatorid=? ");
			query.append("and api_operationid=? ");
			query.append("and applicationid=?");
	 
			ps = con.prepareStatement(query.toString(),Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in getAPI : " + ps);

			ps.setInt(1, applicationSubcriptionsRate.getRateDefId());
			ps.setString(2, applicationSubcriptionsRate.getUpdateBy());
			ps.setInt(3, applicationSubcriptionsRate.getOperatorId());
			ps.setInt(4, applicationSubcriptionsRate.getApiOperationId());
			ps.setInt(5, applicationSubcriptionsRate.getApplicationId());
			
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				applicationSubcriptionsDTO.setApiOperationId(rs.getInt(1));
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

		return applicationSubcriptionsDTO;
	} 
	
	
	public ApplicationSubcriptionsDTO insertSBRates(ApplicationSubcriptionsDTO applicationSubcriptionsRate) throws BusinessException{

		ApplicationSubcriptionsDTO applicationSubcriptionsDTO = new ApplicationSubcriptionsDTO();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder("INSERT INTO sub_rate_sb (operatorid, api_operationid, applicationid, rate_defid,updatedby) ");
			query.append("VALUES (?, ?, ?, ?)");
	 
	 
			ps = con.prepareStatement(query.toString(),Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in getAPI : " + ps);

			ps.setInt(1, applicationSubcriptionsRate.getOperatorId());
			ps.setInt(2, applicationSubcriptionsRate.getApiOperationId());
			ps.setInt(3, applicationSubcriptionsRate.getApplicationId());
			ps.setInt(4, applicationSubcriptionsRate.getRateDefId());
			ps.setString(5, applicationSubcriptionsRate.getUpdateBy());
			
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				applicationSubcriptionsDTO.setOperatorId(rs.getInt(1));
				
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

		return applicationSubcriptionsDTO;
	}


	public ApplicationSubcriptionsDTO updateNBRates(ApplicationSubcriptionsDTO applicationSubcriptionsRate) throws BusinessException{

		ApplicationSubcriptionsDTO applicationSubcriptionsDTO = new ApplicationSubcriptionsDTO();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}


			

			StringBuilder query = new StringBuilder("UPDATE sub_rate_nb ");
			query.append("SET rate_defid = ?, ");
			query.append("updatedby = ? ");
			query.append("WHERE applicationid=? ");
			query.append("and api_operationid=?");

	 
			ps = con.prepareStatement(query.toString(),Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in getAPI : " + ps);

			ps.setInt(1, applicationSubcriptionsRate.getRateDefId());
			ps.setString(2, applicationSubcriptionsRate.getUpdateBy());
			ps.setInt(3, applicationSubcriptionsRate.getApplicationId());
			ps.setInt(4, applicationSubcriptionsRate.getApiOperationId());

			
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				applicationSubcriptionsDTO.setApiOperationId(rs.getInt(1));
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

		return applicationSubcriptionsDTO;
	} 
	
	
	public ApplicationSubcriptionsDTO insertNBRates(ApplicationSubcriptionsDTO applicationSubcriptionsRate) throws BusinessException{

		ApplicationSubcriptionsDTO applicationSubcriptionsDTO = new ApplicationSubcriptionsDTO();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder("INSERT INTO sub_rate_nb ( api_operationid, applicationid, rate_defid,updatedby) ");
			query.append("VALUES ( ?, ?, ?,?)");
	 
	 
			ps = con.prepareStatement(query.toString(),Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in getAPI : " + ps);

			ps.setInt(1, applicationSubcriptionsRate.getApiOperationId());
			ps.setInt(2, applicationSubcriptionsRate.getApplicationId());
			ps.setInt(3, applicationSubcriptionsRate.getRateDefId());
			ps.setString(4, applicationSubcriptionsRate.getUpdateBy());
			
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {
				applicationSubcriptionsDTO.setApiOperationId(rs.getInt(1));
				
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

		return applicationSubcriptionsDTO;
	}
	
	public ApplicationSubcriptionsDTO insertUpdatedNBRates(ApplicationSubcriptionsDTO applicationSubcriptionsRate) throws BusinessException{

		ApplicationSubcriptionsDTO applicationSubcriptionsDTO = new ApplicationSubcriptionsDTO();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder("INSERT INTO sub_rate_nb_updated ( api_operationid, applicationid, rate_defid, comment) ");
			query.append("VALUES ( ?, ?, ?, ?)");
	 
	 
			ps = con.prepareStatement(query.toString(),Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in getAPI : " + ps);

			ps.setInt(1, applicationSubcriptionsRate.getApiOperationId());
			ps.setInt(2, applicationSubcriptionsRate.getApplicationId());
			ps.setInt(3, applicationSubcriptionsRate.getRateDefId());
			ps.setString(4, applicationSubcriptionsRate.getComment());
			
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {
				applicationSubcriptionsDTO.setApiOperationId(rs.getInt(1));

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

		return applicationSubcriptionsDTO;
	}

	
	
	
	public ApplicationSubcriptionsDTO insertUpdatedSBRates(ApplicationSubcriptionsDTO applicationSubcriptionsRate) throws BusinessException{

		ApplicationSubcriptionsDTO applicationSubcriptionsDTO = new ApplicationSubcriptionsDTO();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder("INSERT INTO sub_rate_sb_updated (operatorid, api_operationid, applicationid, rate_defid, comment) ");
			query.append("VALUES (?, ?, ?, ?, ?)");
	 
	 
			ps = con.prepareStatement(query.toString(),Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in getAPI : " + ps);

			ps.setInt(1, applicationSubcriptionsRate.getOperatorId());
			ps.setInt(2, applicationSubcriptionsRate.getApiOperationId());
			ps.setInt(3, applicationSubcriptionsRate.getApplicationId());
			ps.setInt(4, applicationSubcriptionsRate.getRateDefId());
			ps.setString(5, applicationSubcriptionsRate.getComment());
			
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				applicationSubcriptionsDTO.setOperatorId(rs.getInt(1));
				
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

		return applicationSubcriptionsDTO;
	}
	
	
	
	public List<ApplicationSubcriptionsDTO> getExistingSBRates(String appId, String operatorId,String apiName, String version) throws BusinessException{

		List<ApplicationSubcriptionsDTO> applicationSubcriptionsDTOs = new ArrayList<ApplicationSubcriptionsDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder("select rate_def_temp.operatorid,api_operation_temp.api_operationid,rate_def_temp.applicationid,rate_def_temp.rate_defid,api_operation_temp.api_operation,rate_def_temp.rate_defname,rate_def_temp.updatedby,rate_def_temp.createdby  from "
					+"( " 
					+"	select api_operation.api_operationid,api_operation.api_operation from api_operation"
					+ "inner join api on api_operation.apiid=api.apiid  where apiname=? and apiversion=? "
					+") as api_operation_temp "
					+"inner join ( "
					+"select sub_rate_sb.api_operationid,rate_def.rate_defname,sub_rate_sb.applicationid,sub_rate_sb.rate_defid,sub_rate_sb.operatorid,sub_rate_sb.updatedby,sub_rate_sb.createdby from sub_rate_sb " 
					+"inner join rate_def on sub_rate_sb.rate_defid=rate_def.rate_defid  where sub_rate_sb.operatorid=? and sub_rate_sb.applicationid=? "
					+") as rate_def_temp on api_operation_temp.api_operationid=rate_def_temp.api_operationid");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getAPI : " + ps);

			ps.setString(1, apiName);
			ps.setString(2, version);
			ps.setString(2, operatorId);
			ps.setString(3, appId);

			rs = ps.executeQuery();

			while (rs.next()) {

				ApplicationSubcriptionsDTO applicationSubcriptionsDTO = new ApplicationSubcriptionsDTO();

				applicationSubcriptionsDTO.setApiOperation(rs.getString("api_operation"));
				applicationSubcriptionsDTO.setRateDefname(rs.getString("rate_defname"));

				applicationSubcriptionsDTO.setApiOperationId(rs.getInt("api_operationid"));
				applicationSubcriptionsDTO.setApplicationId(rs.getInt("applicationid"));
				applicationSubcriptionsDTO.setOperatorId(rs.getInt("operatorid"));
				applicationSubcriptionsDTO.setRateDefId(rs.getInt("rate_defid"));
				applicationSubcriptionsDTO.setCreateBy(rs.getString("updatedby"));
				applicationSubcriptionsDTO.setUpdateBy(rs.getString("createdby"));

				applicationSubcriptionsDTOs.add(applicationSubcriptionsDTO);
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

		return applicationSubcriptionsDTOs;
	} 


	public List<ApplicationSubcriptionsDTO> getExistingNBRates(String appId,String apiName,String version) throws BusinessException{

		List<ApplicationSubcriptionsDTO> applicationSubcriptionsDTOs = new ArrayList<ApplicationSubcriptionsDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder("select api_operation_temp.api_operationid,rate_def_temp.applicationid,rate_def_temp.rate_defid,api_operation_temp.api_operation,rate_def_temp.rate_defname,rate_def_temp.updatedby,rate_def_temp.createdby  from "
					+"(" 
					+"	select api_operation.api_operationid,api_operation.api_operation from api_operation"
					+ "inner join api on api_operation.apiid=api.apiid  where apiname=? and apiversion=? "
					+") as api_operation_temp "
					+"inner join ( "
					+"select sub_rate_nb.applicationid,sub_rate_nb.api_operationid,rate_def.rate_defname,sub_rate_nb.rate_defid,sub_rate_nb.updatedby,sub_rate_nb.createdby from sub_rate_nb " 
					+"inner join rate_def on sub_rate_nb.rate_defid=rate_def.rate_defid  where sub_rate_nb.applicationid=? "
					+") as rate_def_temp on api_operation_temp.api_operationid=rate_def_temp.api_operationid");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getAPI : " + ps);

			ps.setString(1, apiName);
			ps.setString(2, version);
			ps.setString(3, appId);

			rs = ps.executeQuery();

			while (rs.next()) {

				ApplicationSubcriptionsDTO applicationSubcriptionsDTO = new ApplicationSubcriptionsDTO();

				applicationSubcriptionsDTO.setApiOperation(rs.getString("api_operation"));
				applicationSubcriptionsDTO.setRateDefname(rs.getString("rate_defname"));

				applicationSubcriptionsDTO.setApiOperationId(rs.getInt("api_operationid"));
				applicationSubcriptionsDTO.setApplicationId(rs.getInt("applicationid"));
				applicationSubcriptionsDTO.setRateDefId(rs.getInt("rate_defid"));
				applicationSubcriptionsDTO.setCreateBy(rs.getString("updatedby"));
				applicationSubcriptionsDTO.setUpdateBy(rs.getString("createdby"));

				applicationSubcriptionsDTOs.add(applicationSubcriptionsDTO);
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

		return applicationSubcriptionsDTOs;
	}
	
}
