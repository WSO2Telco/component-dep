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
import com.wso2telco.dep.ratecardservice.dao.model.TariffDTO;
import com.wso2telco.dep.ratecardservice.util.DatabaseTables;

public class TariffDAO {

	private final Log log = LogFactory.getLog(TariffDAO.class);

	public List<TariffDTO> getTariffs() throws Exception {

		List<TariffDTO> tariffs = new ArrayList<TariffDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select * from ");
			query.append(DatabaseTables.TARIFF.getTObject());

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getTariffs : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				TariffDTO tariff = new TariffDTO();

				tariff.setTariffId(rs.getInt("tariffid"));
				tariff.setTariffName(rs.getString("tariffname"));
				tariff.setTariffDescription(rs.getString("tariffdesc"));
				tariff.setTariffDefaultVal(rs.getDouble("tariffdefaultval"));
				tariff.setTariffMaxCount(rs.getInt("tariffmaxcount"));
				tariff.setTariffExcessRate(rs.getDouble("tariffexcessrate"));
				tariff.setTariffDefRate(rs.getDouble("tariffdefrate"));
				tariff.setTariffSPCommission(rs.getDouble("tariffspcommission"));
				tariff.setTariffAdsCommission(rs.getDouble("tariffadscommission"));
				tariff.setTariffOpcoCommission(rs.getDouble("tariffopcocommission"));
				tariff.setTariffSurChargeval(rs.getDouble("tariffsurchargeval"));
				tariff.setTariffSurChargeAds(rs.getDouble("tariffsurchargeAds"));
				tariff.setTariffSurChargeOpco(rs.getDouble("tariffsurchargeOpco"));
				tariff.setCreatedBy(rs.getString("createdby"));
				tariff.setCreatedDate(rs.getTimestamp("createddate").toString());
				tariff.setUpdatedBy(rs.getString("updatedby"));
				tariff.setUpdatedDate(rs.getTimestamp("updateddate").toString());

				tariffs.add(tariff);
			}
		} catch (SQLException e) {

			log.error("database operation error in getTariffs : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getTariffs : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return tariffs;
	}

	public TariffDTO addTariff(TariffDTO tariff) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer tariffId = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("insert into ");
			query.append(DatabaseTables.TARIFF.getTObject());
			query.append(
					" (tariffname, tariffdesc, tariffdefaultval, tariffmaxcount, tariffexcessrate, tariffdefrate, tariffspcommission, tariffadscommission, tariffopcocommission, tariffsurchargeval, tariffsurchargeAds, tariffsurchargeOpco, createdby)");
			query.append(" values");
			query.append(" (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in addTariff : " + ps);

			ps.setString(1, tariff.getTariffName());
			ps.setString(2, tariff.getTariffDescription());
			ps.setDouble(3, tariff.getTariffDefaultVal());
			ps.setInt(4, tariff.getTariffMaxCount());
			ps.setDouble(5, tariff.getTariffExcessRate());
			ps.setDouble(6, tariff.getTariffDefRate());
			ps.setDouble(7, tariff.getTariffSPCommission());
			ps.setDouble(8, tariff.getTariffAdsCommission());
			ps.setDouble(9, tariff.getTariffOpcoCommission());
			ps.setDouble(10, tariff.getTariffSurChargeval());
			ps.setDouble(11, tariff.getTariffSurChargeAds());
			ps.setDouble(12, tariff.getTariffSurChargeOpco());
			ps.setString(13, tariff.getCreatedBy());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				tariffId = rs.getInt(1);
			}

			tariff.setTariffId(tariffId);
		} catch (SQLException e) {

			log.error("database operation error in addTariff : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in addTariff : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return tariff;
	}

	public TariffDTO getTariff(int tariffId) throws Exception {

		TariffDTO tariff = null;

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select * from ");
			query.append(DatabaseTables.TARIFF.getTObject());
			query.append(" where tariffid = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getTariff : " + ps);
			
			ps.setInt(1, tariffId);

			rs = ps.executeQuery();

			while (rs.next()) {

				tariff = new TariffDTO();

				tariff.setTariffId(rs.getInt("tariffid"));
				tariff.setTariffName(rs.getString("tariffname"));
				tariff.setTariffDescription(rs.getString("tariffdesc"));
				tariff.setTariffDefaultVal(rs.getDouble("tariffdefaultval"));
				tariff.setTariffMaxCount(rs.getInt("tariffmaxcount"));
				tariff.setTariffExcessRate(rs.getDouble("tariffexcessrate"));
				tariff.setTariffDefRate(rs.getDouble("tariffdefrate"));
				tariff.setTariffSPCommission(rs.getDouble("tariffspcommission"));
				tariff.setTariffAdsCommission(rs.getDouble("tariffadscommission"));
				tariff.setTariffOpcoCommission(rs.getDouble("tariffopcocommission"));
				tariff.setTariffSurChargeval(rs.getDouble("tariffsurchargeval"));
				tariff.setTariffSurChargeAds(rs.getDouble("tariffsurchargeAds"));
				tariff.setTariffSurChargeOpco(rs.getDouble("tariffsurchargeOpco"));
				tariff.setCreatedBy(rs.getString("createdby"));
				tariff.setCreatedDate(rs.getTimestamp("createddate").toString());
				tariff.setUpdatedBy(rs.getString("updatedby"));
				tariff.setUpdatedDate(rs.getTimestamp("updateddate").toString());
			}
		} catch (SQLException e) {

			log.error("database operation error in getTariff : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getTariff : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return tariff;
	}
}
