/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p/>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.ratecardservice.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.ratecardservice.dao.model.TaxDTO;
import com.wso2telco.dep.ratecardservice.dao.model.TaxValidityDTO;
import com.wso2telco.dep.ratecardservice.util.DatabaseTables;

public class TaxDAO {

	private final Log log = LogFactory.getLog(TaxDAO.class);
	public static final String DBERRORMSG = "unable to open";
	public static final String DBCONERRORMSG = "database connection";
	public static final String TAXID = "taxid";

	public List<TaxDTO> getTaxes() throws BusinessException {

		List<TaxDTO> taxes = new ArrayList<TaxDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error(DBERRORMSG + DataSourceNames.WSO2TELCO_RATE_DB + DBCONERRORMSG);
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder("select taxid, taxcode, taxname, createdby from ");
			query.append(DatabaseTables.TAX.getTObject());

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getTaxes : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				TaxDTO tax = new TaxDTO();

				tax.setTaxId(rs.getInt(TAXID));
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

	public List<TaxValidityDTO> getTaxValidityDates(List<Integer> taxids) throws BusinessException {

		List<TaxValidityDTO> taxes = new ArrayList<TaxValidityDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error(DBERRORMSG + DataSourceNames.WSO2TELCO_RATE_DB + DBCONERRORMSG);
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder();
			query.append("select taxid,idtax_validityid,");
			query.append(" tax_validityactdate, tax_validitydisdate, tax_validityval from ");
			query.append(DatabaseTables.TAX_VALIDITY.getTObject());
			query.append(" where taxid in(  ");

			Iterator<Integer> iterator = taxids.iterator();
			int[] values = new int[taxids.size()];
			int index = 0;
			while (iterator.hasNext()) {
				values[index] = iterator.next();
				query.append("?");
				if (iterator.hasNext()) {
					query.append(",");
				}
				index++;

			}

			query.append(")");
			ps = con.prepareStatement(query.toString());

			for (int x = 0; x < values.length; x++) {
				ps.setInt(x + 1, values[x]);
			}
			log.debug("sql query in getTaxes : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				TaxValidityDTO tax = new TaxValidityDTO();
				tax.setIdtaxValidityId(rs.getInt("idtax_validityid"));
				tax.setTaxValidityactdate(rs.getString("tax_validityactdate"));
				tax.setTaxValiditydisdate(rs.getString("tax_validitydisdate"));
				tax.setTaxValidityval(rs.getString("tax_validityval"));
				tax.setTaxid(rs.getInt(TAXID));
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

	public TaxDTO addTax(TaxDTO tax) throws BusinessException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer taxId = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error(DBERRORMSG + DataSourceNames.WSO2TELCO_RATE_DB + DBCONERRORMSG);
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
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
	
	public TaxDTO insertTax(TaxDTO tax) throws BusinessException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer taxId = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error(DBERRORMSG + DataSourceNames.WSO2TELCO_RATE_DB + DBCONERRORMSG);
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			if(!addTaxValidity(tax.getTaxesValidityDates()[0])) {
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}
			
			StringBuilder query = new StringBuilder("insert into ");
			query.append(DatabaseTables.TAX.getTObject());
			query.append(" (taxcode, taxname, createdby)");
			query.append(" values");
			query.append(" (?, ?, ?)");

			ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in insertTax : " + ps);

			ps.setString(1, tax.getTaxCode());
			ps.setString(2, tax.getTaxName());
			ps.setString(3, tax.getCreatedBy());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				taxId = rs.getInt(1);
			}
			if(!addTaxValidity(tax.getTaxesValidityDates()[0])) {
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}
			
			tax.setTaxId(taxId);
		} catch (SQLException e) {

			log.error("database operation error in insertTax : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in insertTax : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return tax;
	}
	public boolean addTaxValidity(TaxValidityDTO taxValidityDTO) throws BusinessException {
		
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error(DBERRORMSG + DataSourceNames.WSO2TELCO_RATE_DB + DBCONERRORMSG);
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}
			


			StringBuilder query = new StringBuilder("insert into ");
			query.append(DatabaseTables.TAX_VALIDITY.getTObject());
			query.append(" (tax_validityactdate, tax_validitydisdate, tax_validityval,taxid)");
			query.append(" values");
			query.append(" (?, ?, ?, ?)");

			ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in addTax : " + ps);

			String actDate[]=taxValidityDTO.getTaxValidityactdate().split("-");

			String disDate[]=taxValidityDTO.getTaxValiditydisdate().split("-");
			
			ps.setDate(1, new Date(new Integer(actDate[0]), new Integer(actDate[1]), new Integer(actDate[2])));
			ps.setDate(2, new Date(new Integer(disDate[0]), new Integer(disDate[1]), new Integer(disDate[2])));
			ps.setDouble(3, new Double(taxValidityDTO.getTaxValidityval()));
			ps.setInt(4, taxValidityDTO.getTaxid());

			ps.executeUpdate();

			
			
		} catch (SQLException e) {

			log.error("database operation error in addTax : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in addTax : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		
		
		return true;
		
	}

	public TaxDTO getTax(int taxId) throws BusinessException {

		TaxDTO tax = null;

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error(DBERRORMSG + DataSourceNames.WSO2TELCO_RATE_DB + DBCONERRORMSG);
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
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

				tax.setTaxId(rs.getInt(TAXID));
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
