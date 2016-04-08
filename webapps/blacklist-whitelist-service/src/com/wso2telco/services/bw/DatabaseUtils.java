package com.wso2telco.services.bw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.security.validator.ValidatorException;


/**
 * Created with IntelliJ IDEA.
 * User: Tharanga Ranaweera
 * Date: 2/25/14
 * Time: 11:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseUtils {

	
	//======================================PRIYANKA_06608==============================================================================
	
    private static volatile DataSource axiataDatasource = null;
    private static final String AXIATA_DATA_SOURCE = "jdbc/AXIATA_MIFE_DB";
    private static volatile DataSource amDatasource = null;
    private static final String AM_DATA_SOURCE = "jdbc/WSO2AM_DB";
    private static volatile DataSource statDatasource = null;
    private static final String STAT_DATA_SOURCE = "jdbc/WSO2AM_STATS_DB";
    
    private static final Log log = LogFactory.getLog(DatabaseUtils.class);
	
    public static void initializeDatasources() throws ValidatorException {
        if (axiataDatasource != null) {
            return;
        }

        try {
            Context ctx = new InitialContext();
            axiataDatasource = (DataSource) ctx.lookup(AXIATA_DATA_SOURCE);
        } catch (NamingException e) {
            handleException("Error while looking up the data source: " + AXIATA_DATA_SOURCE, e);
        }
    }

    public static Connection getAxiataDBConnection() throws SQLException, ValidatorException {
        initializeDatasources();

        if (axiataDatasource != null) {
            return axiataDatasource.getConnection();
        }
        throw new SQLException("Axiata Datasource not initialized properly");
    }
    
    public static void initializeAMDatasources() throws ValidatorException {
        if (amDatasource != null) {
            return;
        }

        try {
            Context ctx = new InitialContext();
            amDatasource = (DataSource) ctx.lookup(AM_DATA_SOURCE);
        } catch (NamingException e) {
            handleException("Error while looking up the data source: " + AM_DATA_SOURCE, e);
        }
    }

    public static Connection getAMDBConnection() throws SQLException, ValidatorException {
    	initializeAMDatasources();

        if (amDatasource != null) {
            return amDatasource.getConnection();
        }
        throw new SQLException("AM Datasource not initialized properly");
    }

    public static void initializeStatDatasources() throws ValidatorException {
        if (statDatasource != null) {
            return;
        }

        try {
            Context ctx = new InitialContext();
            statDatasource = (DataSource) ctx.lookup(STAT_DATA_SOURCE);
        } catch (NamingException e) {
            handleException("Error while looking up the data source: " + STAT_DATA_SOURCE, e);
        }
    }

    public static Connection getStatDBConnection() throws SQLException, ValidatorException {
    	initializeStatDatasources();

        if (statDatasource != null) {
            return statDatasource.getConnection();
        }
        throw new SQLException("STAT Datasource not initialized properly");
    }    
    
    private static void handleException(String msg, Throwable t) throws ValidatorException {
    	log.error(msg, t);
    	throw new ValidatorException(msg, t);
    }
	
	//====================================================================================================================	
	
    private static List<String> msisdn = new ArrayList<String>();
    private static int currentNo = 14;
    
     public static List<String> WriteBlacklistNumbers(String userMSISDN,String apiID,String apiName, String userID) throws Exception {
        System.out.println((userMSISDN));
        
        String sql = "INSERT INTO `blacklistmsisdn` (`MSISDN`,`API_ID`,`API_NAME`,`USER_ID`) VALUES (?,?,?,?);";
        Connection conn = null;
        PreparedStatement ps = null;
        
       
        try {
            conn = getStatDBConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1,userMSISDN);
            ps.setString(2,apiID);
            ps.setString(3,apiName);
            ps.setString(4,userID);
            
            ps.execute();
            closeAllConnections(ps, conn, null);
            
        } catch (SQLException e) {
            System.out.println(e.toString());
            closeAllConnections(ps, conn, null);
            throw e;
        } catch(Exception e){
        	closeAllConnections(ps, conn, null);
        	throw e;
        }
        
        
        /*finally {
        	
        	closeAllConnections(ps, conn, null);
        	
            if(msisdn.isEmpty()){
                return null;
            }
            else{
                return msisdn;
            }
        }*/
        
        return msisdn;
        
    }
    
    public static List<String> writeBlacklistNumbersBulk(String[] userMSISDN, String apiID, String apiName, String userID) throws Exception {
        System.out.println(Arrays.toString(userMSISDN));

        String sql = "INSERT INTO `blacklistmsisdn` (`MSISDN`,`API_ID`,`API_NAME`,`USER_ID`) VALUES ";
        for (int i = 0; i < userMSISDN.length; i++) {
            if (i == 0) {
                sql += "(?,?,?,?)";
            } else {
                sql += " ,(?,?,?,?)";
            }
        }
        sql += ";";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getStatDBConnection();
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < userMSISDN.length; i++) {
                int queryStartPos = i * 4 + 1;
                
                boolean isNumber=false;
                Long userMSISDNLong=0L;
                String userMSISDNStr=userMSISDN[i].split("\\+")[1];
        		try {
        			userMSISDNLong=Long.valueOf(userMSISDNStr);
        			isNumber=true;
        			
        		} catch (NumberFormatException e) {
        			System.out.println(e.toString());
        			throw e;
        		}
                if (isNumber && userMSISDNLong>0) {
                	ps.setString(queryStartPos, userMSISDN[i]);
                    ps.setString(queryStartPos + 1, apiID);
                    ps.setString(queryStartPos + 2, apiName);
                    ps.setString(queryStartPos + 3, userID);                	
				}                
            }

            ps.execute();
            closeAllConnections(ps, conn, null);

        } catch (SQLException e) {
            System.out.println("test "+e.toString());
            closeAllConnections(ps, conn, null);
            throw e;
        } catch(Exception ex){
        	closeAllConnections(ps, conn, null);
        	throw ex;
        }
        
     /*finally {
        	
        	closeAllConnections(ps, conn, null);
        	
            if (msisdn.isEmpty()) {
                return null;
            } else {
                return msisdn;
            }
        }*/
        return msisdn;
        
    }

    public static String[] getBlacklistNumbers(String apiName) throws SQLException, NamingException {

        String sql = "SELECT `MSISDN` FROM `blacklistmsisdn` WHERE `API_NAME`=?;";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> blacklist = new ArrayList<String>();

        try {
            conn = getStatDBConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, apiName);
            rs = ps.executeQuery();

            while (rs.next()) {
                blacklist.add(rs.getString("MSISDN"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
        	
        	closeAllConnections(ps, conn, rs);
        	
            if (blacklist.isEmpty()) {
                return null;
            } else {
                return blacklist.toArray(new String[blacklist.size()]);
            }
        }
    }

    public static List<String> removeBlacklistNumbers(String apiName, String userMSISDN) throws SQLException, NamingException {

        String sql = "DELETE FROM `blacklistmsisdn` WHERE `API_NAME`=? && `MSISDN`=?;";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getStatDBConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, apiName);
            ps.setString(2, userMSISDN);
            
            ps.execute();
        } catch (SQLException e) {
            throw e;
        } finally {
        	
        	closeAllConnections(ps, conn, null);
        	
            if (msisdn.isEmpty()) {
                return null;
            } else {
                return msisdn;
            }
        }
    }

    public static List<String> WriteWhitelistNumbers(String userMSISDN, String SubscriptionID, String apiID, String applicationID) throws SQLException, NamingException {
        System.out.println((userMSISDN));

        String sql = "INSERT INTO `subscription_WhiteList` (`MSISDN`,`subscriptionID`,`api_id`,`application_id`) VALUES (?,?,?,?);";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getStatDBConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, userMSISDN);
            ps.setString(2, SubscriptionID);
            ps.setString(3, apiID);
            ps.setString(4, applicationID);

            ps.execute();

        } catch (SQLException e) {
            System.out.println(e.toString());
            throw e;
        } finally {
        	
        	closeAllConnections(ps, conn, null);
            
        	if (msisdn.isEmpty()) {
                return null;
            } else {
                return msisdn;
            }
        }

    }
    
    public static int next() {
        if(currentNo==Integer.MAX_VALUE) {
            currentNo = 0;
        }
        return currentNo++;
    }

    
//    @SuppressWarnings("finally")
	public static/* List<String> */void writeWhitelistNumbersBulk(
			String[] userMSISDN, String apiId, String userID, String appId)
			throws Exception {
		System.out.println(Arrays.toString(userMSISDN));

		String subscriptionId = findSubscriptionId(appId, apiId);
		String sql = "INSERT INTO `subscription_WhiteList` (`msisdn`,`api_id`,`application_id`,`subscriptionID`) VALUES ";//(`msisdn`,`api_id`,`application_id`,`subscriptionID`) 
		for (int i = 0; i < userMSISDN.length; i++) {
			if (i == 0) {
				sql += "(?,?,?,?)";
			} else {
				sql += " ,(?,?,?,?)";
			}
		}
		sql += ";";

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = getStatDBConnection();
			ps = conn.prepareStatement(sql);

			for (int i = 0; i < userMSISDN.length; i++) {
				int queryStartPos = i * 4 + 1;
				ps.setString(queryStartPos, userMSISDN[i]);
				ps.setString(queryStartPos + 1, apiId);
				// ps.setString(queryStartPos + 2, apiName);
				ps.setString(queryStartPos + 2, appId);
				ps.setString(queryStartPos + 3, subscriptionId);
			}

			ps.execute();

		} catch (SQLException e) {
			System.out.println(e.toString());
			throw e;
		}finally {

			closeAllConnections(ps, conn, null);

		}

	}
    
	private static String findSubscriptionId(String appId, String apiId) throws Exception {
		String sql = "SELECT SUBSCRIPTION_ID from AM_SUBSCRIPTION where APPLICATION_ID = ? and API_ID = ?";
		Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs =null;
        try {
            conn = getAMDBConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, appId);
            ps.setString(2, apiId);
            rs = ps.executeQuery();
            while (rs.next()) {
            	return rs.getString("SUBSCRIPTION_ID");
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            throw e;
        }finally {

			closeAllConnections(ps, conn, rs);

		}
        throw new Exception("No record found in table AM_SUBSCRIPTION for APPLICATION_ID = " + appId + " and API_ID = " + apiId);
	}


	public static String getSubscribers() throws SQLException, NamingException, ValidatorException {
		String sql = "SELECT SUBSCRIBER_ID, USER_ID from AM_SUBSCRIBER;";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs=null;
        try {
            conn = getAMDBConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            String json = "{";
            while (rs.next()) {
            	String subscriberId = rs.getString("SUBSCRIBER_ID");
            	String username = rs.getString("USER_ID");
            	json += "\"" + subscriberId + "\":\"" + username + "\",";
            }
            if(json.endsWith(",")) {
            	json = json.substring(0, json.length() - 1);
            }
            json += "}";
            return json;
        } catch (SQLException e) {
            System.out.println(e.toString());
            throw e;
        }finally {

			closeAllConnections(ps, conn, rs);

		}
    }
	
	public static String getApps(String subscriberId) throws SQLException, NamingException, ValidatorException {
		String sql = "SELECT APPLICATION_ID, NAME from AM_APPLICATION where SUBSCRIBER_ID = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			conn = getAMDBConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, subscriberId);
			rs = ps.executeQuery();
			String json = "{";
			while (rs.next()) {
				String appId = rs.getString("APPLICATION_ID");
				String appName = rs.getString("NAME");
				json += "\"" + appId + "\":\"" + appName + "\",";
			}
			if(json.endsWith(",")) {
				json = json.substring(0, json.length() - 1);
			}
			json += "}";
			return json;
		} catch (SQLException e) {
			System.out.println(e.toString());
			throw e;
		}finally {

			closeAllConnections(ps, conn, rs);

		}
	}
	
	public static String getApis(String appId) throws SQLException, NamingException, ValidatorException {
		
		String appSql = "SELECT API_ID,API_NAME from AM_API where API_ID IN";
		String subSql = "SELECT DISTINCT API_ID from AM_SUBSCRIPTION where APPLICATION_ID = ?";
		String sql = appSql + "(" + subSql + ");";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			conn = getAMDBConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, appId);
			rs = ps.executeQuery();
			String json = "{";
			while (rs.next()) {
				String apiId = rs.getString("API_ID");
				String apiName = rs.getString("API_NAME");
				json += "\"" + apiId + "\":\"" + apiName + "\",";
			}
			if(json.endsWith(",")) {
				json = json.substring(0, json.length() - 1);
			}
			json += "}";
			return json;
		} catch (SQLException e) {
			System.out.println(e.toString());
			throw e;
		}finally {

			closeAllConnections(ps, conn, rs);

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
	 * 
	 * @param dbConnection
	 *            Connection
	 */
	private static void closeConnection(Connection dbConnection) {
		if (dbConnection != null) {
			try {
				dbConnection.close();
			} catch (SQLException e) {
				log.warn(
						"Database error. Could not close database connection. Continuing with "
								+ "others. - " + e.getMessage(), e);
			}
		}
	}

	/**
	 * Close ResultSet
	 * 
	 * @param resultSet
	 *            ResultSet
	 */
	private static void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				log.warn(
						"Database error. Could not close ResultSet  - "
								+ e.getMessage(), e);
			}
		}

	}

	/**
	 * Close PreparedStatement
	 * 
	 * @param preparedStatement
	 *            PreparedStatement
	 */
	private static void closeStatement(PreparedStatement preparedStatement) {
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				log.warn(
						"Database error. Could not close PreparedStatement. Continuing with"
								+ " others. - " + e.getMessage(), e);
			}
		}

	}

    /**
     *
     * @return whitelist
     * @throws SQLException
     * @throws NamingException
     */

    public static String[] getWhiteListNumbers() throws SQLException, NamingException {

     String sql = "SELECT `MSISDN` FROM `subscription_WhiteList`;";

     Connection conn = null;
     PreparedStatement ps = null;
     ResultSet rs = null;
     List<String> whiteList = new ArrayList<String>();

     try {
         conn = getStatDBConnection();
         ps = conn.prepareStatement(sql);
         rs = ps.executeQuery();

         while (rs.next()) {
         whiteList.add(rs.getString("MSISDN"));
         }
     } catch (SQLException e) {
         throw e;
     } finally {

        closeAllConnections(ps, conn, rs);

         if (whiteList.isEmpty()) {
         return null;
         } else {
         return whiteList.toArray(new String[whiteList.size()]);
         }
     }
     }

    /**
     *
     * @param userMSISDN
     * @return
     * @throws SQLException
     * @throws NamingException
     */
    public static List<String> removeWhitelistNumber(String userMSISDN) throws SQLException, NamingException {

        String sql = "DELETE FROM `subscription_WhiteList` WHERE `MSISDN`=?;";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getStatDBConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, userMSISDN);
            ps.execute();
        } catch (SQLException e) {
            throw e;
        } finally {

            closeAllConnections(ps, conn, null);

            if (msisdn.isEmpty()) {
                return null;
            } else {
                return msisdn;
            }
        }
    }


	
}
