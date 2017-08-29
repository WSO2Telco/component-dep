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
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateTaxDTO;
import com.wso2telco.dep.ratecardservice.dao.model.TaxDTO;
import com.wso2telco.dep.ratecardservice.util.DatabaseTables;

public class RateTaxDAO {

	private final Log log = LogFactory.getLog(RateTaxDAO.class);

	public RateTaxDTO addRateTax(RateTaxDTO rateTax) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer rateTaxId = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("insert into ");
			query.append(DatabaseTables.RATE_TAX.getTObject());
			query.append(" (rate_defid, taxid, createdby)");
			query.append(" values");
			query.append(" (?, ?, ?)");

			ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in addRateTax : " + ps);

			ps.setInt(1, rateTax.getRateDefinition().getRateDefId());
			ps.setInt(2, rateTax.getTax().getTaxId());
			ps.setString(3, rateTax.getCreatedBy());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				rateTaxId = rs.getInt(1);
			}

			rateTax.setRateTaxId(rateTaxId);
		} catch (SQLException e) {

			log.error("database operation error in addRateTax : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in addRateTax : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateTax;
	}

	public RateTaxDTO getRateTax(int rateTaxId) throws Exception {

		RateTaxDTO rateTax = null;

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select rate_taxesid, rate_defid, taxid, createdby from ");
			query.append(DatabaseTables.RATE_TAX.getTObject());
			query.append(" where rate_taxesid = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getRateTax : " + ps);

			ps.setInt(1, rateTaxId);

			rs = ps.executeQuery();

			while (rs.next()) {

				rateTax = new RateTaxDTO();

				rateTax.setRateTaxId(rs.getInt("rate_taxesid"));

				RateDefinitionDTO rateDefinition = new RateDefinitionDTO();
				rateDefinition.setRateDefId(rs.getInt("rate_defid"));
				rateTax.setRateDefinition(rateDefinition);

				TaxDTO tax = new TaxDTO();
				tax.setTaxId(rs.getInt("taxid"));
				rateTax.setTax(tax);

				rateTax.setCreatedBy(rs.getString("createdby"));
			}
		} catch (SQLException e) {

			log.error("database operation error in getRateTax : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getRateTax : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateTax;
	}

	public List<RateTaxDTO> getRateTaxes(int taxId) throws Exception {

		List<RateTaxDTO> rateTaxes = new ArrayList<RateTaxDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select rate_taxesid, rate_defid, taxid, createdby from ");
			query.append(DatabaseTables.RATE_TAX.getTObject());
			query.append(" where taxid = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getRateTaxes : " + ps);

			ps.setInt(1, taxId);

			rs = ps.executeQuery();

			while (rs.next()) {

				RateTaxDTO rateTax = new RateTaxDTO();

				rateTax.setRateTaxId(rs.getInt("rate_taxesid"));

				RateDefinitionDTO rateDefinition = new RateDefinitionDTO();
				rateDefinition.setRateDefId(rs.getInt("rate_defid"));
				rateTax.setRateDefinition(rateDefinition);

				TaxDTO tax = new TaxDTO();
				tax.setTaxId(rs.getInt("taxid"));
				rateTax.setTax(tax);

				rateTax.setCreatedBy(rs.getString("createdby"));

				rateTaxes.add(rateTax);
			}
		} catch (SQLException e) {

			log.error("database operation error in getRateTaxes : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getRateTaxes : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateTaxes;
	}

	public boolean deleteRateTax(int rateTaxId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("delete from ");
			query.append(DatabaseTables.RATE_TAX.getTObject());
			query.append(" where rate_taxesid = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in deleteRateTax : " + ps);

			ps.setInt(1, rateTaxId);

			ps.executeUpdate();
		} catch (SQLException e) {

			log.error("database operation error in deleteRateTax : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in deleteRateTax : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return true;
	}
}
