/*
 * ProvisionService.java
 * Apr 2, 2013  11:20:38 AM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package com.wso2telco.aggregatorblacklist;

import com.wso2telco.aggregatorblacklist.model.ProvisionReq;
import com.wso2telco.aggregatorblacklist.dao.ProvisionDAO;
import com.wso2telco.dep.operatorservice.model.OperatorSearchDTO;
import com.wso2telco.dep.operatorservice.service.OparatorService;

// TODO: Auto-generated Javadoc
/**
 * The Class ProvisionService.
 */
public class ProvisionService {
   
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
           
          new ProvisionDAO().insertMerchantProvision(applicationid,subscriber,operator, merchants);
    }
}
     

