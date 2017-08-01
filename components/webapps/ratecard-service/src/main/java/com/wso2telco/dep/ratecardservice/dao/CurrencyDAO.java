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

	public List<CurrencyDTO> getCurrencies() throws Exception {

		List<CurrencyDTO> currencies = new ArrayList<CurrencyDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select * from ");
			query.append(DatabaseTables.CURRENCY.getTObject());

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getCurrencies : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				CurrencyDTO currency = new CurrencyDTO();

				currency.setCurrencyId(rs.getInt("currencyid"));
				currency.setCurrencyCode(rs.getString("currencycode"));
				currency.setCurrencyDescription(rs.getString("currencydesc"));

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

	public CurrencyDTO addCurrency(CurrencyDTO currency) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer currencyId = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("insert into ");
			query.append(DatabaseTables.CURRENCY.getTObject());
			query.append(" (currencycode, currencydesc)");
			query.append(" values");
			query.append(" (?, ?)");

			ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in addCurrency : " + ps);

			ps.setString(1, currency.getCurrencyCode());
			ps.setString(2, currency.getCurrencyDescription());

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
}
