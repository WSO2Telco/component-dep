package com.wso2telco.dep.authorize.token.handler.util;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.authorize.token.handler.dto.TokenDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sheshan on 8/4/17.
 */
public class APIManagerDBUtil {

    private final static Log log = LogFactory.getLog(APIManagerDBUtil.class);

    public static TokenDTO getTokenDetailsFromAPIManagerDB(String consumerKey){
        TokenDTO tokenDto = new TokenDTO();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DbUtils.getDbConnection(DataSourceNames.WSO2AM_DB);
            if (con == null) {
                throw new Exception("Connection not found");
            }
            StringBuilder queryString = new StringBuilder("SELECT cp.CONSUMER_SECRET,it.ACCESS_TOKEN , it.REFRESH_TOKEN , it.VALIDITY_PERIOD  ");
            queryString.append("FROM idn_oauth_consumer_apps cp ");
            queryString.append("inner join idn_oauth2_access_token it ");
            queryString.append("on it.CONSUMER_KEY_ID = cp.ID ");
            queryString.append("where cp.CONSUMER_KEY= ?  ");
            queryString.append("AND it.TOKEN_STATE='ACTIVE' ");

            ps = con.prepareStatement(queryString.toString());
            ps.setString(1, consumerKey);
            log.debug("sql query in APIManagerDBUtil : " + ps);
            rs = ps.executeQuery();
            rs.next();
            tokenDto.setTokenAuth(consumerKey+":"+rs.getString(1));
            tokenDto.setAccessToken(rs.getString(2));
            tokenDto.setRefreshToken(rs.getString(3));
            tokenDto.setTokenValidity(rs.getInt(4));

        } catch (SQLException e) {
            log.error("database operation error in APIManagerDBUtil : ", e);
        } catch (Exception e) {
            log.error("error in APIManagerDBUtil : ", e);
        } finally {

            DbUtils.closeAllConnections(ps, con, rs);
        }
        return tokenDto;
    }


}
