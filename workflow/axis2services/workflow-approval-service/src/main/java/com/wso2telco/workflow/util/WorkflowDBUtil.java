package com.wso2telco.workflow.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorkflowDBUtil {
	
	private static final Log log = LogFactory.getLog(WorkflowDBUtil.class);
	
	private static final String statsDataSourceName = "jdbc/WSO2AM_STATS_DB";
	private static final String axiataDataSourceName = "jdbc/AXIATA_MIFE_DB";
	private static volatile DataSource statDatasource = null;
	private static volatile DataSource axiataDatasource = null;
	
	public static void initializeDataSource() throws Exception {
		getStatDataSource();
		getAxiataDataSource();

	}
	
	public static void getAxiataDataSource() throws Exception {
		if (axiataDatasource != null) {
			return;
		}

		if (axiataDataSourceName != null) {
			try {
				Context ctx = new InitialContext();
				axiataDatasource = (DataSource) ctx
						.lookup(axiataDataSourceName);
			} catch (NamingException e) {
				throw new Exception("Error while looking up the data " + "source: "
								+ axiataDataSourceName);
			}

		}
	}
	
	public static void getStatDataSource() throws Exception {
		if (statDatasource != null) {
			return;
		}

		if (statsDataSourceName != null) {
			try {
				Context ctx = new InitialContext();
				statDatasource = (DataSource) ctx
						.lookup(statsDataSourceName);
			} catch (NamingException e) {
				throw new Exception("Error while looking up the data " + "source: "
								+ statsDataSourceName);
			}

		}
	}
	
	public static Connection getStatsDBConnection() throws SQLException, Exception {
		initializeDataSource();
		if (statDatasource != null) {
			return statDatasource.getConnection();
		} else {
			throw new SQLException(
					"Statistics Datasource not initialized properly.");
		}
	}

	public static Connection getAxiataDBConnection() throws SQLException, Exception {
		initializeDataSource();
		if (axiataDatasource != null) {
			return axiataDatasource.getConnection();
		} else {
			throw new SQLException(
					"Axiata Datasource not initialized properly.");
		}
	}
	
	public static void closeAllConnections(PreparedStatement preparedStatement, 
			Connection connection, ResultSet resultSet) {
		
		closeConnection(connection);
		closeStatement(preparedStatement);
		closeResultSet(resultSet);
	}
	
    /**
     * Close Connection
     * @param dbConnection Connection
     */
    private static void closeConnection(Connection dbConnection) {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException e) {
                log.warn("Database error. Could not close database connection. Continuing with " +
                        "others. - " + e.getMessage(), e);
            }
        }
    }

    /**
     * Close ResultSet
     * @param resultSet ResultSet
     */
    private static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.warn("Database error. Could not close ResultSet  - " + e.getMessage(), e);
            }
        }

    }

    /**
     * Close PreparedStatement
     * @param preparedStatement PreparedStatement
     */
    private static void closeStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                log.warn("Database error. Could not close PreparedStatement. Continuing with" +
                        " others. - " + e.getMessage(), e);
            }
        }

    }

}
