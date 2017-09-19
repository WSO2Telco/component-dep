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
import java.sql.Types;
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

	public List<TariffDTO> getTariffs() throws BusinessException {

		List<TariffDTO> tariffs = new ArrayList<TariffDTO>();

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
					"select tariffid, tariffname, tariffdesc, tariffdefaultval, tariffmaxcount, tariffexcessrate, tariffdefrate, tariffspcommission, tariffadscommission, tariffopcocommission, tariffsurchargeval, tariffsurchargeAds, tariffsurchargeOpco, createdby from ");
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

	public TariffDTO addTariff(TariffDTO tariff) throws BusinessException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer tariffId = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				log.error("unable to open " + DataSourceNames.WSO2TELCO_RATE_DB + " database connection");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
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

			Double tariffDefaultVal = tariff.getTariffDefaultVal();
			if (tariffDefaultVal != null) {
				ps.setDouble(3, tariffDefaultVal);
			} else {
				ps.setNull(3, Types.DOUBLE);
			}

			Integer tariffMaxCount = tariff.getTariffMaxCount();
			if (tariffMaxCount != null) {
				ps.setInt(4, tariffMaxCount);
			} else {
				ps.setNull(4, Types.INTEGER);
			}

			Double tariffExcessRate = tariff.getTariffExcessRate();
			if (tariffExcessRate != null) {
				ps.setDouble(5, tariffExcessRate);
			} else {
				ps.setNull(5, Types.DOUBLE);
			}

			Double tariffDefRate = tariff.getTariffDefRate();
			if (tariffDefRate != null) {
				ps.setDouble(6, tariffDefRate);
			} else {
				ps.setNull(6, Types.DOUBLE);
			}

			Double tariffSPCommission = tariff.getTariffSPCommission();
			if (tariffSPCommission != null) {
				ps.setDouble(7, tariffSPCommission);
			} else {
				ps.setNull(7, Types.DOUBLE);
			}

			Double tariffAdsCommission = tariff.getTariffAdsCommission();
			if (tariffAdsCommission != null) {
				ps.setDouble(8, tariffAdsCommission);
			} else {
				ps.setNull(8, Types.DOUBLE);
			}

			Double tariffOpcoCommission = tariff.getTariffOpcoCommission();
			if (tariffOpcoCommission != null) {
				ps.setDouble(9, tariffOpcoCommission);
			} else {
				ps.setNull(9, Types.DOUBLE);
			}

			Double tariffSurChargeval = tariff.getTariffSurChargeval();
			if (tariffSurChargeval != null) {
				ps.setDouble(10, tariffSurChargeval);
			} else {
				ps.setNull(10, Types.DOUBLE);
			}

			Double tariffSurChargeAds = tariff.getTariffSurChargeAds();
			if (tariffSurChargeAds != null) {
				ps.setDouble(11, tariffSurChargeAds);
			} else {
				ps.setNull(11, Types.DOUBLE);
			}

			Double tariffSurChargeOpco = tariff.getTariffSurChargeOpco();
			if (tariffSurChargeOpco != null) {
				ps.setDouble(12, tariffSurChargeOpco);
			} else {
				ps.setNull(12, Types.DOUBLE);
			}

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

	public TariffDTO getTariff(int tariffId) throws BusinessException {

		TariffDTO tariff = null;

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
					"select tariffid, tariffname, tariffdesc, tariffdefaultval, tariffmaxcount, tariffexcessrate, tariffdefrate, tariffspcommission, tariffadscommission, tariffopcocommission, tariffsurchargeval, tariffsurchargeAds, tariffsurchargeOpco, createdby from ");
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
