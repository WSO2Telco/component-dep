/*
 * ProvisionService.java
 * Apr 2, 2013  11:20:38 AM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package com.wso2telco.aggregatorblacklist;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.wso2telco.aggregatorblacklist.model.ProvisionReq;
import com.wso2telco.core.dbutils.AxiataDbService;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.operatorservice.model.OperatorSearchDTO;
import com.wso2telco.dep.operatorservice.service.OparatorService;
// TODO: Auto-generated Javadoc
/**
 * The Class ProvisionService.
 */
public class ProvisionService {

    /** The Constant LOG. */
    private static final Logger LOG = Logger.getLogger(ProvisionService.class.getName());
    
     /**
      * Provisionapp.
      *
      * @param provisionreq the provisionreq
      * @throws Exception the exception
      */
     public void provisionapp(ProvisionReq provisionreq) throws Exception {
           
           Integer applicationid = (provisionreq.getProvisioninfo().getApplicationid() != null) ? Integer.parseInt(provisionreq.getProvisioninfo().getApplicationid()) : null;
           
           String subscriber = provisionreq.getProvisioninfo().getSubscriber();
           String operator = provisionreq.getProvisioninfo().getOperatorcode();
           String[] merchants = (String[])(provisionreq.getProvisioninfo().getMerchantcode()).toArray(new String[(provisionreq.getProvisioninfo().getMerchantcode()).size()]);
           OparatorService opService = new OparatorService();
           OperatorSearchDTO searchDTO = new OperatorSearchDTO();
           searchDTO.setName(operator);
           opService.loadOperators(searchDTO);
           
          new AxiataDbService().insertMerchantProvision(applicationid,subscriber,operator, merchants);
    }
     
     
     public boolean insertMerchantProvision(Integer appID, String subscriber, String operator,
             String[] merchants) throws Exception {

         Connection con = null;
         Statement st = null;
         ResultSet rs = null;
         PreparedStatement pst = null;

         try {
             con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

             st = con.createStatement();
             String sql = "SELECT id "
                     + "FROM operators "
                     + "WHERE operatorname = '" + operator + "'";

             rs = st.executeQuery(sql);

             int operatorid = 0;
             if (rs.next()) {
                 operatorid = rs.getInt("id");
             } else {
                 throw new Exception("Operator Not Found");
             }

             pst = null;
             for (int i = 0; i < merchants.length; i++) {
                 sql = "INSERT INTO merchantopco_blacklist (application_id, operator_id, subscriber, merchant) VALUES "
                         + "(?, ?, ?, ?)";

                 pst = con.prepareStatement(sql);
                 if (appID == null) {
                     pst.setNull(1, Types.INTEGER);
                 } else {
                     pst.setInt(1, appID);
                 }
                 pst.setInt(2, operatorid);
                 pst.setString(3, subscriber);
                 pst.setString(4, merchants[i]);
                 pst.executeUpdate();
             }

         } catch (Exception e) {
             DbUtils.handleException("Error while Provisioning Merchant", e);
         } finally {
             DbUtils.closeAllConnections(st, con, rs);
             DbUtils.closeAllConnections(pst, null, null);
         }
         return true;
     }
    
}
