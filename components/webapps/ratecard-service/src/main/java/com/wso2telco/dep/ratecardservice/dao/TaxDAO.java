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
import com.wso2telco.dep.ratecardservice.dao.model.TaxDTO;
import com.wso2telco.dep.ratecardservice.util.DatabaseTables;

public class TaxDAO {

	private final Log log = LogFactory.getLog(TaxDAO.class);

	public List<TaxDTO> getTaxes() throws Exception {

		List<TaxDTO> taxes = new ArrayList<TaxDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select taxid, taxcode, taxname, createdby from ");
			query.append(DatabaseTables.TAX.getTObject());

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getTaxes : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				TaxDTO tax = new TaxDTO();

				tax.setTaxId(rs.getInt("taxid"));
				tax.setTaxCode(rs.getString("taxcode"));
				tax.setTaxName(rs.getString("taxname"));
				tax.setCreatedBy(rs.getString("createdby"));

				taxes.add(tax);
			}
		} catch (SQLException e) {

			log.error("database operation error in getTaxes : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getTaxes : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return taxes;
	}

	public TaxDTO addTax(TaxDTO tax) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer taxId = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("insert into ");
			query.append(DatabaseTables.TAX.getTObject());
			query.append(" (taxcode, taxname, createdby)");
			query.append(" values");
			query.append(" (?, ?, ?)");

			ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in addTax : " + ps);

			ps.setString(1, tax.getTaxCode());
			ps.setString(2, tax.getTaxName());
			ps.setString(3, tax.getCreatedBy());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				taxId = rs.getInt(1);
			}

			tax.setTaxId(taxId);
		} catch (SQLException e) {

			log.error("database operation error in addTax : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in addTax : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return tax;
	}

	public TaxDTO getTax(int taxId) throws Exception {

		TaxDTO tax = null;

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select taxid, taxcode, taxname, createdby from ");
			query.append(DatabaseTables.TAX.getTObject());
			query.append(" where taxid = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getTax : " + ps);

			ps.setInt(1, taxId);

			rs = ps.executeQuery();

			while (rs.next()) {

				tax = new TaxDTO();

				tax.setTaxId(rs.getInt("taxid"));
				tax.setTaxCode(rs.getString("taxcode"));
				tax.setTaxName(rs.getString("taxname"));
				tax.setCreatedBy(rs.getString("createdby"));
			}
		} catch (SQLException e) {

			log.error("database operation error in getTax : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getTax : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return tax;
	}
}
