/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.ratecardservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.ratecardservice.dao.model.CurrencyDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateTypeDTO;
import com.wso2telco.dep.ratecardservice.dao.model.TariffDTO;
import com.wso2telco.dep.ratecardservice.util.DatabaseTables;

public class RateDefinitionDAO {

	private final Log log = LogFactory.getLog(RateDefinitionDAO.class);

	public List<RateDefinitionDTO> getRateDefinitions() throws BusinessException {

		List<RateDefinitionDTO> rateDefinitions = new ArrayList<RateDefinitionDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder(
					"select rate_defid, rate_defname, rate_defdesc, rate_defdefault, rate_defcategorybase, currencyid, rate_typeid, tariffid, createdby from ");
			query.append(DatabaseTables.RATE_DEF.getTObject());

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getRateDefinitions : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				RateDefinitionDTO rateDefinition = new RateDefinitionDTO();

				rateDefinition.setRateDefId(rs.getInt("rate_defid"));
				rateDefinition.setRateDefName(rs.getString("rate_defname"));
				rateDefinition.setRateDefDescription(rs.getString("rate_defdesc"));
				rateDefinition.setRateDefDefault(rs.getInt("rate_defdefault"));
				rateDefinition.setRateDefCategoryBase(rs.getInt("rate_defcategorybase"));
				rateDefinition.setCreatedBy(rs.getString("createdby"));

				CurrencyDTO currency = new CurrencyDTO();
				currency.setCurrencyId(rs.getInt("currencyid"));
				rateDefinition.setCurrency(currency);

				RateTypeDTO rateType = new RateTypeDTO();
				rateType.setRateTypeId(rs.getInt("rate_typeid"));
				rateDefinition.setRateType(rateType);

				TariffDTO tariff = new TariffDTO();
				tariff.setTariffId(rs.getInt("tariffid"));
				rateDefinition.setTariff(tariff);

				rateDefinitions.add(rateDefinition);
			}
		} catch (SQLException e) {

			log.error("database operation error in getRateDefinitions : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getRateDefinitions : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateDefinitions;
	}

	public RateDefinitionDTO addRateDefinition(RateDefinitionDTO rateDefinition) throws BusinessException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer rateDefId = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder("insert into ");
			query.append(DatabaseTables.RATE_DEF.getTObject());
			query.append(
					" (rate_defname, rate_defdesc, rate_defdefault, currencyid, rate_typeid, rate_defcategorybase, tariffid, createdby)");
			query.append(" values");
			query.append(" (?, ?, ?, ?, ?, ?, ?, ?)");

			ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in addRateDefinition : " + ps);

			ps.setString(1, rateDefinition.getRateDefName());
			ps.setString(2, rateDefinition.getRateDefDescription());
			ps.setInt(3, rateDefinition.getRateDefDefault());
			ps.setInt(4, rateDefinition.getCurrency().getCurrencyId());
			ps.setInt(5, rateDefinition.getRateType().getRateTypeId());
			ps.setInt(6, rateDefinition.getRateDefCategoryBase());
			ps.setInt(7, rateDefinition.getTariff().getTariffId());
			ps.setString(8, rateDefinition.getCreatedBy());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				rateDefId = rs.getInt(1);
			}

			rateDefinition.setRateDefId(rateDefId);
		} catch (SQLException e) {

			log.error("database operation error in addRateDefinition : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in addRateDefinition : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateDefinition;
	}

	public RateDefinitionDTO getRateDefinition(int rateDefId) throws BusinessException {

		RateDefinitionDTO rateDefinition = null;

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder(
					"select rate_defid, rate_defname, rate_defdesc, rate_defdefault, rate_defcategorybase, currencyid, rate_typeid, tariffid, createdby from ");
			query.append(DatabaseTables.RATE_DEF.getTObject());
			query.append(" where rate_defid = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getRateDefinition : " + ps);

			ps.setInt(1, rateDefId);

			rs = ps.executeQuery();

			while (rs.next()) {

				rateDefinition = new RateDefinitionDTO();

				rateDefinition.setRateDefId(rs.getInt("rate_defid"));
				rateDefinition.setRateDefName(rs.getString("rate_defname"));
				rateDefinition.setRateDefDescription(rs.getString("rate_defdesc"));
				rateDefinition.setRateDefDefault(rs.getInt("rate_defdefault"));
				rateDefinition.setRateDefCategoryBase(rs.getInt("rate_defcategorybase"));
				rateDefinition.setCreatedBy(rs.getString("createdby"));

				CurrencyDTO currency = new CurrencyDTO();
				currency.setCurrencyId(rs.getInt("currencyid"));
				rateDefinition.setCurrency(currency);

				RateTypeDTO rateType = new RateTypeDTO();
				rateType.setRateTypeId(rs.getInt("rate_typeid"));
				rateDefinition.setRateType(rateType);

				TariffDTO tariff = new TariffDTO();
				tariff.setTariffId(rs.getInt("tariffid"));
				rateDefinition.setTariff(tariff);
			}
		} catch (SQLException e) {

			log.error("database operation error in getRateDefinition : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getRateDefinition : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateDefinition;
	}

	public boolean deleteRateDefinition(int rateDefId) throws BusinessException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder("delete from ");
			query.append(DatabaseTables.RATE_DEF.getTObject());
			query.append(" where rate_defid = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in deleteRateDefinition : " + ps);

			ps.setInt(1, rateDefId);

			ps.executeUpdate();
		} catch (SQLException e) {

			log.error("database operation error in deleteRateDefinition : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in deleteRateDefinition : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return true;
	}

	public List<RateDefinitionDTO> getRateDefinitions(int apiOperationId) throws BusinessException {

		List<RateDefinitionDTO> rateDefinitions = new ArrayList<RateDefinitionDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder(
					"select rate_defid, rate_defname, rate_defdesc, rate_defdefault, rate_defcategorybase, currencyid, rate_typeid, tariffid, createdby from ");
			query.append(DatabaseTables.RATE_DEF.getTObject());
			query.append(" where rate_defid NOT IN (");
			query.append("select rate_defid from ");
			query.append(DatabaseTables.OPERATION_RATE.getTObject());
			query.append(" where api_operationid = ? and operator_id is null)");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getRateDefinitions : " + ps);

			ps.setInt(1, apiOperationId);

			rs = ps.executeQuery();

			while (rs.next()) {

				RateDefinitionDTO rateDefinition = new RateDefinitionDTO();

				rateDefinition.setRateDefId(rs.getInt("rate_defid"));
				rateDefinition.setRateDefName(rs.getString("rate_defname"));
				rateDefinition.setRateDefDescription(rs.getString("rate_defdesc"));
				rateDefinition.setRateDefDefault(rs.getInt("rate_defdefault"));
				rateDefinition.setRateDefCategoryBase(rs.getInt("rate_defcategorybase"));
				rateDefinition.setCreatedBy(rs.getString("createdby"));

				CurrencyDTO currency = new CurrencyDTO();
				currency.setCurrencyId(rs.getInt("currencyid"));
				rateDefinition.setCurrency(currency);

				RateTypeDTO rateType = new RateTypeDTO();
				rateType.setRateTypeId(rs.getInt("rate_typeid"));
				rateDefinition.setRateType(rateType);

				TariffDTO tariff = new TariffDTO();
				tariff.setTariffId(rs.getInt("tariffid"));
				rateDefinition.setTariff(tariff);

				rateDefinitions.add(rateDefinition);
			}
		} catch (SQLException e) {

			log.error("database operation error in getRateDefinitions : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getRateDefinitions : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateDefinitions;
	}

	public List<RateDefinitionDTO> getRateDefinitions(int apiOperationId, int operatorId) throws BusinessException {

		List<RateDefinitionDTO> rateDefinitions = new ArrayList<RateDefinitionDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder(
					"select rate_defid, rate_defname, rate_defdesc, rate_defdefault, rate_defcategorybase, currencyid, rate_typeid, tariffid, createdby from ");
			query.append(DatabaseTables.RATE_DEF.getTObject());
			query.append(" where rate_defid NOT IN (");
			query.append("select rate_defid from ");
			query.append(DatabaseTables.OPERATION_RATE.getTObject());
			query.append(" where api_operationid = ? and operator_id = ?)");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getRateDefinitions : " + ps);

			ps.setInt(1, apiOperationId);
			ps.setInt(2, operatorId);

			rs = ps.executeQuery();

			while (rs.next()) {

				RateDefinitionDTO rateDefinition = new RateDefinitionDTO();

				rateDefinition.setRateDefId(rs.getInt("rate_defid"));
				rateDefinition.setRateDefName(rs.getString("rate_defname"));
				rateDefinition.setRateDefDescription(rs.getString("rate_defdesc"));
				rateDefinition.setRateDefDefault(rs.getInt("rate_defdefault"));
				rateDefinition.setRateDefCategoryBase(rs.getInt("rate_defcategorybase"));
				rateDefinition.setCreatedBy(rs.getString("createdby"));

				CurrencyDTO currency = new CurrencyDTO();
				currency.setCurrencyId(rs.getInt("currencyid"));
				rateDefinition.setCurrency(currency);

				RateTypeDTO rateType = new RateTypeDTO();
				rateType.setRateTypeId(rs.getInt("rate_typeid"));
				rateDefinition.setRateType(rateType);

				TariffDTO tariff = new TariffDTO();
				tariff.setTariffId(rs.getInt("tariffid"));
				rateDefinition.setTariff(tariff);

				rateDefinitions.add(rateDefinition);
			}
		} catch (SQLException e) {

			log.error("database operation error in getRateDefinitions : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getRateDefinitions : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateDefinitions;
	}

	public List<RateDefinitionDTO> getAssignedRateDefinitions(int apiOperationId) throws BusinessException {

		List<RateDefinitionDTO> rateDefinitions = new ArrayList<RateDefinitionDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder(
					"select rate_defid, rate_defname, rate_defdesc, rate_defdefault, rate_defcategorybase, currencyid, rate_typeid, tariffid, createdby from ");
			query.append(DatabaseTables.RATE_DEF.getTObject());
			query.append(" where rate_defid IN (");
			query.append("select rate_defid from ");
			query.append(DatabaseTables.OPERATION_RATE.getTObject());
			query.append(" where api_operationid = ? and operator_id is null)");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getAssignedRateDefinitions : " + ps);

			ps.setInt(1, apiOperationId);

			rs = ps.executeQuery();

			while (rs.next()) {

				RateDefinitionDTO rateDefinition = new RateDefinitionDTO();

				rateDefinition.setRateDefId(rs.getInt("rate_defid"));
				rateDefinition.setRateDefName(rs.getString("rate_defname"));
				rateDefinition.setRateDefDescription(rs.getString("rate_defdesc"));
				rateDefinition.setRateDefDefault(rs.getInt("rate_defdefault"));
				rateDefinition.setRateDefCategoryBase(rs.getInt("rate_defcategorybase"));
				rateDefinition.setCreatedBy(rs.getString("createdby"));

				CurrencyDTO currency = new CurrencyDTO();
				currency.setCurrencyId(rs.getInt("currencyid"));
				rateDefinition.setCurrency(currency);

				RateTypeDTO rateType = new RateTypeDTO();
				rateType.setRateTypeId(rs.getInt("rate_typeid"));
				rateDefinition.setRateType(rateType);

				TariffDTO tariff = new TariffDTO();
				tariff.setTariffId(rs.getInt("tariffid"));
				rateDefinition.setTariff(tariff);

				rateDefinitions.add(rateDefinition);
			}
		} catch (SQLException e) {

			log.error("database operation error in getAssignedRateDefinitions : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getAssignedRateDefinitions : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateDefinitions;
	}

	public List<RateDefinitionDTO> getAssignedRateDefinitions(int apiOperationId, int operatorId) throws BusinessException {

		List<RateDefinitionDTO> rateDefinitions = new ArrayList<RateDefinitionDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder(
					"select rate_defid, rate_defname, rate_defdesc, rate_defdefault, rate_defcategorybase, currencyid, rate_typeid, tariffid, createdby from ");
			query.append(DatabaseTables.RATE_DEF.getTObject());
			query.append(" where rate_defid IN (");
			query.append("select rate_defid from ");
			query.append(DatabaseTables.OPERATION_RATE.getTObject());
			query.append(" where api_operationid = ? and operator_id = ?)");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getAssignedRateDefinitions : " + ps);

			ps.setInt(1, apiOperationId);
			ps.setInt(2, operatorId);

			rs = ps.executeQuery();

			while (rs.next()) {

				RateDefinitionDTO rateDefinition = new RateDefinitionDTO();

				rateDefinition.setRateDefId(rs.getInt("rate_defid"));
				rateDefinition.setRateDefName(rs.getString("rate_defname"));
				rateDefinition.setRateDefDescription(rs.getString("rate_defdesc"));
				rateDefinition.setRateDefDefault(rs.getInt("rate_defdefault"));
				rateDefinition.setRateDefCategoryBase(rs.getInt("rate_defcategorybase"));
				rateDefinition.setCreatedBy(rs.getString("createdby"));

				CurrencyDTO currency = new CurrencyDTO();
				currency.setCurrencyId(rs.getInt("currencyid"));
				rateDefinition.setCurrency(currency);

				RateTypeDTO rateType = new RateTypeDTO();
				rateType.setRateTypeId(rs.getInt("rate_typeid"));
				rateDefinition.setRateType(rateType);

				TariffDTO tariff = new TariffDTO();
				tariff.setTariffId(rs.getInt("tariffid"));
				rateDefinition.setTariff(tariff);

				rateDefinitions.add(rateDefinition);
			}
		} catch (SQLException e) {

			log.error("database operation error in getAssignedRateDefinitions : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getAssignedRateDefinitions : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}
		return rateDefinitions;
	}

	public List<RateDefinitionDTO> getAssignedRateDefinitionsForOperator(int operatorId) throws BusinessException {

		List<RateDefinitionDTO> rateDefinitions = new ArrayList<RateDefinitionDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder(
					"select rate_defid, rate_defname, rate_defdesc, rate_defdefault, rate_defcategorybase, currencyid, rate_typeid, tariffid, createdby from ");
			query.append(DatabaseTables.RATE_DEF.getTObject());
			query.append(" where rate_defid IN (");
			query.append("select rate_defid from ");
			query.append(DatabaseTables.OPERATION_RATE.getTObject());
			query.append(" where operator_id = ?)");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getAssignedRateDefinitions : " + ps);


			ps.setInt(1, operatorId);

			rs = ps.executeQuery();

			while (rs.next()) {

				RateDefinitionDTO rateDefinition = new RateDefinitionDTO();

				rateDefinition.setRateDefId(rs.getInt("rate_defid"));
				rateDefinition.setRateDefName(rs.getString("rate_defname"));
				rateDefinition.setRateDefDescription(rs.getString("rate_defdesc"));
				rateDefinition.setRateDefDefault(rs.getInt("rate_defdefault"));
				rateDefinition.setRateDefCategoryBase(rs.getInt("rate_defcategorybase"));
				rateDefinition.setCreatedBy(rs.getString("createdby"));

				CurrencyDTO currency = new CurrencyDTO();
				currency.setCurrencyId(rs.getInt("currencyid"));
				rateDefinition.setCurrency(currency);

				RateTypeDTO rateType = new RateTypeDTO();
				rateType.setRateTypeId(rs.getInt("rate_typeid"));
				rateDefinition.setRateType(rateType);

				TariffDTO tariff = new TariffDTO();
				tariff.setTariffId(rs.getInt("tariffid"));
				rateDefinition.setTariff(tariff);
				boolean isNew=true;
				for(RateDefinitionDTO definitionDTO:rateDefinitions){
					if(definitionDTO.getRateDefId().intValue()==rateDefinition.getRateDefId().intValue()){
						isNew=false;
					}
				}
				if(isNew){
					rateDefinitions.add(rateDefinition);
				}
			}
		} catch (SQLException e) {

			log.error("database operation error in getAssignedRateDefinitions : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getAssignedRateDefinitions : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateDefinitions;
	}
}
