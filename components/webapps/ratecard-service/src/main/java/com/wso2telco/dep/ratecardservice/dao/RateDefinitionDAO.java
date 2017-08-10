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

	public List<RateDefinitionDTO> getRateDefinitions() throws Exception {

		List<RateDefinitionDTO> rateDefinitions = new ArrayList<RateDefinitionDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select * from ");
			query.append(DatabaseTables.RATE_DEF.getTObject());

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getRateDefinitions : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				RateDefinitionDTO rateDefinition = new RateDefinitionDTO();

				rateDefinition.setRateDefId(rs.getInt("rate_defid"));
				rateDefinition.setRateDefName(rs.getString("rate_defname"));
				rateDefinition.setRateDefDesc(rs.getString("rate_defdesc"));
				rateDefinition.setRateDefDefault(rs.getInt("rate_defdefault"));
				rateDefinition.setRateDefCategoryBase(rs.getInt("rate_defcategorybase"));
				rateDefinition.setCreatedBy(rs.getString("createdby"));
				rateDefinition.setCreatedDate(rs.getTimestamp("createddate").toString());
				rateDefinition.setUpdatedBy(rs.getString("updatedby"));
				rateDefinition.setUpdatedDate(rs.getTimestamp("updateddate").toString());

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

	public RateDefinitionDTO addRateDefinition(RateDefinitionDTO rateDefinition) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer rateDefId = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
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
			ps.setString(2, rateDefinition.getRateDefDesc());
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

	public RateDefinitionDTO getRateDefinition(int rateDefId) throws Exception {

		RateDefinitionDTO rateDefinition = null;

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select * from ");
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
				rateDefinition.setRateDefDesc(rs.getString("rate_defdesc"));
				rateDefinition.setRateDefDefault(rs.getInt("rate_defdefault"));
				rateDefinition.setRateDefCategoryBase(rs.getInt("rate_defcategorybase"));
				rateDefinition.setCreatedBy(rs.getString("createdby"));
				rateDefinition.setCreatedDate(rs.getTimestamp("createddate").toString());
				rateDefinition.setUpdatedBy(rs.getString("updatedby"));
				rateDefinition.setUpdatedDate(rs.getTimestamp("updateddate").toString());

				CurrencyDTO currency = new CurrencyDTO();
				currency.setCurrencyId(rs.getInt("currencyid"));;
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

	public boolean deleteRateDefinition(int rateDefId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
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
}
