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

	public List<RateTypeDTO> getRateTypes() throws Exception {

		List<RateTypeDTO> rateTypes = new ArrayList<RateTypeDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select * from ");
			query.append(DatabaseTables.RATE_TYPE.getTObject());

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getRateTypes : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				RateTypeDTO rateType = new RateTypeDTO();

				rateType.setRateTypeId(rs.getInt("rate_typeid"));
				rateType.setRateTypeCode(rs.getString("rate_typecode"));
				rateType.setRateTypeDesc(rs.getString("rate_typedesc"));

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
}
