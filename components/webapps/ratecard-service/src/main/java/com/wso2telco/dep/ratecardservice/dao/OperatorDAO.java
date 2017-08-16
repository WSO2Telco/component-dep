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
import com.wso2telco.dep.ratecardservice.dao.model.OperatorDTO;
import com.wso2telco.dep.ratecardservice.util.DatabaseTables;

public class OperatorDAO {

	private final Log log = LogFactory.getLog(OperatorDAO.class);

	public OperatorDTO getOperator(int operatorId) throws Exception {

		OperatorDTO operator = null;

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select * from ");
			query.append(DatabaseTables.OPERATOR.getTObject());
			query.append(" where operatorId = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getOperator : " + ps);

			ps.setInt(1, operatorId);

			rs = ps.executeQuery();

			while (rs.next()) {

				operator = new OperatorDTO();
				
				operator.setOperatorId(rs.getInt("operatorId"));
				operator.setOperatorName(rs.getString("operatorname"));
				operator.setOperatorDescription(rs.getString("operatordesc"));
				operator.setCreatedBy(rs.getString("createdby"));
				operator.setCreatedDate(rs.getTimestamp("createddate").toString());
				operator.setUpdatedBy(rs.getString("updatedby"));
				operator.setUpdatedDate(rs.getTimestamp("updateddate").toString());
			}
		} catch (SQLException e) {

			log.error("database operation error in getOperator : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getOperator : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return operator;
	}
	
	public List<OperatorDTO> getOperators() throws Exception {

		List<OperatorDTO> operators = new ArrayList<OperatorDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select * from ");
			query.append(DatabaseTables.OPERATOR.getTObject());

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getOperators : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				OperatorDTO operator = new OperatorDTO();
				
				operator.setOperatorId(rs.getInt("operatorId"));
				operator.setOperatorName(rs.getString("operatorname"));
				operator.setOperatorDescription(rs.getString("operatordesc"));
				operator.setCreatedBy(rs.getString("createdby"));
				operator.setCreatedDate(rs.getTimestamp("createddate").toString());
				operator.setUpdatedBy(rs.getString("updatedby"));
				operator.setUpdatedDate(rs.getTimestamp("updateddate").toString());

				operators.add(operator);
			}
		} catch (SQLException e) {

			log.error("database operation error in getOperators : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getOperators : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return operators;
	}
}
