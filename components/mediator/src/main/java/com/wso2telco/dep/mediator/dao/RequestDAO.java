/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.mediator.dao;

import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.mediator.model.MessageDTO;
import com.wso2telco.dep.mediator.model.SpendChargeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RequestDAO {
    public void publishMessage(MessageDTO messageDAO) throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

            String sql = "INSERT INTO mdtrequestmessage ("
                         + "msgtypeId,"
                         + "mdtrequestId,"
                         + "internalclientrefcode,"
                         + "message,"
                         + "clientrefcode,"
                         + "clientrefval,"
                         + "reportedtime) "
                         + "VALUES (?,?,?,?,?,?,?) ";

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

    public void persistSpendCharge(SpendChargeDTO spendChargeDTO) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;

        try {
            con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

            String sql = "INSERT INTO spendlimitdata ("
                         + "msgtype,"
                         + "groupName,"
                         + "consumerKey,"
                         + "operatorId,"
                         + "msisdn,"
                         + "amount,"
                         + "currentDateTime, "
                         + "effectiveTime) "
                         + "VALUES (?,?,?,?,?,?,?,?) ";

            ps = con.prepareStatement(sql);
            ps.setInt(1, spendChargeDTO.getMessageType());
            ps.setString(2, spendChargeDTO.getGroupName());
            ps.setString(3, spendChargeDTO.getConsumerKey());
            ps.setString(4, spendChargeDTO.getOperatorId());
            ps.setString(5, spendChargeDTO.getMsisdn());
            ps.setDouble(6, spendChargeDTO.getAmount());
            ps.setLong(7, spendChargeDTO.getCurrentTime());
            ps.setLong(8, spendChargeDTO.getOrginalTime());

            ps.executeUpdate();

        } catch (Exception e) {
            DbUtils.handleException("Error while inserting data into spendlimitdata table", e);
        } finally {
            DbUtils.closeAllConnections(ps, con, rs);
        }
    }
}
