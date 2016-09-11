package com.wso2telco.dep.publisheventsdata.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.publisheventsdata.util.DatabaseTables;

public class SpendLimitDAO {

	private final Log log = LogFactory.getLog(SpendLimitDAO.class);
	
	public Double getGroupTotalDayAmount(String groupName, String operator, String msisdn) throws SQLException, Exception {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Double groupTotalDayAmount = 0.0;

        try {
            con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

			StringBuilder queryString = new StringBuilder(" SELECT amount FROM ")
            .append(DatabaseTables.DAY_SPEND_LIMIT_DATA.getTableName())
			.append(" WHERE groupName=? and  operatorId=? and msisdn=? ")
			.append( " ORDER by responseTime desc ")
			.append(" LIMIT 1 ");
            
            ps = con.prepareStatement(queryString.toString());
            ps.setString(1, groupName);
            ps.setString(2, operator);
            ps.setString(3, msisdn);
            rs = ps.executeQuery();

            if (rs.next() && rs.getString("amount") != null) {
                groupTotalDayAmount = Double.parseDouble(rs.getString("amount"));
            }
        } catch (SQLException e) {

			log.error("database operation error in getGroupTotalDayAmount : ", e);
			throw e;
        } catch (Exception e) {

			log.error("error in getGroupTotalMonthAmount : ", e);
			throw e;
        } finally {
            DbUtils.closeAllConnections(ps, con, rs);
        }
        return groupTotalDayAmount;

    }
	
	public Double getGroupTotalMonthAmount(String groupName, String operator, String msisdn) throws SQLException, Exception {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Double groupTotalMonthAmount = 0.0;

        try {
            con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

           StringBuilder queryString = new StringBuilder(" SELECT amount FROM ")
           .append(DatabaseTables.MONTH_SPEND_LIMIT_DATA.getTableName())
           .append(" WHERE groupName=? and  operatorId=? and msisdn=? ")
           .append( " ORDER by responseTime desc ")
           .append(" LIMIT 1 ");
           
            ps = con.prepareStatement(queryString.toString());
            ps.setString(1, groupName);
            ps.setString(2, operator);
            ps.setString(3, msisdn);
            rs = ps.executeQuery();

            if (rs.next() && rs.getString("amount") != null) {
                groupTotalMonthAmount = Double.parseDouble(rs.getString("amount"));
            }
        } catch (SQLException e) {

			log.error("database operation error in getGroupTotalMonthAmount : ", e);
			throw e;
        } catch (Exception e) {

			log.error("error in getGroupTotalMonthAmount : ", e);
			throw e;
        } finally {
            DbUtils.closeAllConnections(ps, con, rs);
        }
        return groupTotalMonthAmount;

    }

}
