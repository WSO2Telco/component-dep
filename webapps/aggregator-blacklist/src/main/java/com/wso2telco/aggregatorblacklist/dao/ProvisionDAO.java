package com.wso2telco.aggregatorblacklist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.aggregatorblacklist.util.DatabaseTables;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;

public class ProvisionDAO {
	
	/** The Constant log. */
	private final Log log = LogFactory.getLog(ProvisionDAO.class);
    public boolean insertMerchantProvision(Integer appID, String subscriber, String operator,String[] merchants) throws SQLException , Exception {
    	
    	Connection con = null;
        ResultSet rs = null;	
		PreparedStatement insertStatement = null;
		PreparedStatement selectStatement = null;

        try {
        	
			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			/**
			 * Set autocommit off to handle the transaction
			 */
            con.setAutoCommit(false);
            
            StringBuilder selectQueryString = new StringBuilder(" SELECT id ");
            selectQueryString.append(" FROM ");
            selectQueryString.append(DatabaseTables.OPERATORS.getTableName());
            selectQueryString.append(" WHERE operatorname = '" + operator + "'");
            
            selectStatement = con.prepareStatement(selectQueryString.toString());
            rs=selectStatement.executeQuery();         
            
            int operatorid = 0;
            if (rs.next()) {
                operatorid = rs.getInt("id");
            } else {
                throw new Exception("Operator Not Found");
            }


            for (int i = 0; i < merchants.length; i++) {
            	StringBuilder insertQueryString = new StringBuilder("INSERT INTO ");
            	insertQueryString.append(DatabaseTables.MERCHANTOPCO_BLACKLIST.getTableName());
            	insertQueryString.append(" (application_id, operator_id, subscriber, merchant) ");
            	insertQueryString.append("VALUES (?, ?, ?, ?)");

            	insertStatement = con.prepareStatement(insertQueryString.toString());
                if (appID == null) {
                	insertStatement.setNull(1, Types.INTEGER);
                } else {
                	insertStatement.setInt(1, appID);
                }
                insertStatement.setInt(2, operatorid);
                insertStatement.setString(3, subscriber);
                insertStatement.setString(4, merchants[i]);
                insertStatement.executeUpdate();
                
    			/**
    			 * commit the transaction if all success
    			 */
    			con.commit();
            }
            
        }catch (SQLException e)
        {
			/**
			 * rollback if Exception occurs
			 */
			con.rollback();

			log.error("database operation error in Merachantopco Blacklist Entry : ", e);
			throw e;	
        }catch (Exception e) {
			/**
			 * rollback if Exception occurs
			 */
			con.rollback();
			log.error("Error while Provisioning Merchant : ", e);
            
            throw e;
        } finally {
            DbUtils.closeAllConnections(selectStatement, con, rs);
            DbUtils.closeAllConnections(insertStatement, null, null);
        }
        return true;
    }
   

}
