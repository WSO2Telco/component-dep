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
import com.wso2telco.dep.ratecardservice.util.DatabaseTables;

public class CurrencyDAO {

	private final Log log = LogFactory.getLog(CurrencyDAO.class);

	public List<CurrencyDTO> getCurrencies() throws BusinessException {

		List<CurrencyDTO> currencies = new ArrayList<CurrencyDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder("select currencyid, currencycode, currencydesc, createdby from ");
			query.append(DatabaseTables.CURRENCY.getTObject());

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getCurrencies : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				CurrencyDTO currency = new CurrencyDTO();

				currency.setCurrencyId(rs.getInt("currencyid"));
				currency.setCurrencyCode(rs.getString("currencycode"));
				currency.setCurrencyDescription(rs.getString("currencydesc"));
				currency.setCreatedBy(rs.getString("createdby"));

				currencies.add(currency);
			}
		} catch (SQLException e) {

			log.error("database operation error in getCurrencies : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getCurrencies : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return currencies;
	}

	public CurrencyDTO addCurrency(CurrencyDTO currency) throws BusinessException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer currencyId = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder("insert into ");
			query.append(DatabaseTables.CURRENCY.getTObject());
			query.append(" (currencycode, currencydesc, createdby)");
			query.append(" values");
			query.append(" (?, ?, ?)");

			ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in addCurrency : " + ps);

			ps.setString(1, currency.getCurrencyCode());
			ps.setString(2, currency.getCurrencyDescription());
			ps.setString(3, currency.getCreatedBy());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				currencyId = rs.getInt(1);
			}

			currency.setCurrencyId(currencyId);
		} catch (SQLException e) {

			log.error("database operation error in addCurrency : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in addCurrency : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return currency;
	}

	public CurrencyDTO getCurrency(int currencyId) throws BusinessException {

		CurrencyDTO currency = null;

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}

			StringBuilder query = new StringBuilder("select currencyid, currencycode, currencydesc, createdby from ");
			query.append(DatabaseTables.CURRENCY.getTObject());
			query.append(" where currencyid = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getCurrency : " + ps);

			ps.setInt(1, currencyId);

			rs = ps.executeQuery();

			while (rs.next()) {

				currency = new CurrencyDTO();

				currency.setCurrencyId(rs.getInt("currencyid"));
				currency.setCurrencyCode(rs.getString("currencycode"));
				currency.setCurrencyDescription(rs.getString("currencydesc"));
				currency.setCreatedBy(rs.getString("createdby"));
			}
		} catch (SQLException e) {

			log.error("database operation error in getCurrency : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getCurrency : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return currency;
	}
}
