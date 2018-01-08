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
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.ratecardservice.dao.model.RateTypeDTO;
import com.wso2telco.dep.ratecardservice.util.DatabaseTables;

public class RateTypeDAO {

	private final Log log = LogFactory.getLog(RateTypeDAO.class);

	public List<RateTypeDTO> getRateTypes() throws BusinessException {

		List<RateTypeDTO> rateTypes = new ArrayList<RateTypeDTO>();

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
					"select rate_typeid, rate_typecode, rate_typedesc, createdby from ");
			query.append(DatabaseTables.RATE_TYPE.getTObject());

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getRateTypes : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				RateTypeDTO rateType = new RateTypeDTO();

				rateType.setRateTypeId(rs.getInt("rate_typeid"));
				rateType.setRateTypeCode(rs.getString("rate_typecode"));
				rateType.setRateTypeDescription(rs.getString("rate_typedesc"));
				rateType.setCreatedBy(rs.getString("createdby"));

				rateTypes.add(rateType);
			}
		} catch (SQLException e) {

			log.error("database operation error in getRateTypes : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getRateTypes : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateTypes;
	}

	public RateTypeDTO getRateType(int rateTypeId) throws BusinessException {

		RateTypeDTO rateType = null;

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
					"select rate_typeid, rate_typecode, rate_typedesc, createdby from ");
			query.append(DatabaseTables.RATE_TYPE.getTObject());
			query.append(" where rate_typeid = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getRateType : " + ps);

			ps.setInt(1, rateTypeId);

			rs = ps.executeQuery();

			while (rs.next()) {

				rateType = new RateTypeDTO();

				rateType.setRateTypeId(rs.getInt("rate_typeid"));
				rateType.setRateTypeCode(rs.getString("rate_typecode"));
				rateType.setRateTypeDescription(rs.getString("rate_typedesc"));
				rateType.setCreatedBy(rs.getString("createdby"));
			}
		} catch (SQLException e) {

			log.error("database operation error in getRateType : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getRateType : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateType;
	}
}
