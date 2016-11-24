package com.wso2telco.dep.mediator.dao;

import com.wso2telco.dbutils.DbUtils;
import com.wso2telco.dep.mediator.model.MessageDTO;
import com.wso2telco.dep.mediator.model.SpendChargeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by sasikala on 11/24/16.
 */
public class RequestDAO {
    public void publishMessage(MessageDTO messageDAO) throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            con = DbUtils.getAxiataDBConnection();

            String sql = "INSERT INTO mdtrequestmessage (msgtypeId,mdtrequestId,internalclientrefcode,message,clientrefcode,clientrefval,reportedtime) VALUES (?,?,?,?,?,?,?) ";

            ps = con.prepareStatement(sql);
            ps.setInt(1, messageDAO.getMsgId());
            ps.setString(2, messageDAO.getMdtrequestId());
            ps.setString(3, messageDAO.getClienString());
            ps.setString(4, messageDAO.getMessage());
            ps.setString(5, messageDAO.getRefcode().getRefCode());
            ps.setString(6, messageDAO.getRefval());
            ps.setLong(7, messageDAO.getReportedTime());
            ps.executeUpdate();

        } catch (Exception e) {
            DbUtils.handleException("Error while inserting data into messat table", e);
        } finally {
            DbUtils.closeAllConnections(ps, con, rs);
        }
    }

    public void persistSpendCharge(SpendChargeDTO spendChargeDTO) throws Exception{
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;

        try {
            con = DbUtils.getAxiataDBConnection();

            String sql = "INSERT INTO spendlimitdata (msgtype,groupName,consumerKey,operatorId,msisdn,amount,currentDateTime, effectiveTime) VALUES (?,?,?,?,?,?,?,?) ";

            ps = con.prepareStatement(sql);
            ps.setInt(1,spendChargeDTO.getMessageType());
            ps.setString(2, spendChargeDTO.getGroupName());
            ps.setString(3, spendChargeDTO.getConsumerKey());
            ps.setString(4, spendChargeDTO.getOperatorId());
            ps.setString(5, spendChargeDTO.getMsisdn());
            ps.setDouble(6, spendChargeDTO.getAmount());
            ps.setLong(7, spendChargeDTO.getCurrentTime());
            ps.setLong(8, spendChargeDTO.getOrginalTime());

            ps.executeUpdate();

        }catch (Exception e){
            DbUtils.handleException("Error while inserting data into spendlimitdata table", e);
        } finally {
            DbUtils.closeAllConnections(ps,con,rs);
        }
    }
}
