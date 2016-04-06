/*
 * ProvisionService.java
 * Apr 2, 2013  11:20:38 AM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package com.wso2telco.aggregatorblacklist;


import org.apache.log4j.Logger;

import com.wso2telco.aggregatorblacklist.model.ProvisionReq;
import com.wso2telco.dbutils.AxiataDbService;


/**
 * <TO-DO>
 * <code>ProvisionService</code>
 *
 * @version $Id: ProvisionService.java,v 1.00.000
 */
public class ProvisionService {

    private static final Logger LOG = Logger.getLogger(ProvisionService.class.getName());
    
     public void provisionapp(ProvisionReq provisionreq) throws Exception {
           
           Integer applicationid = (provisionreq.getProvisioninfo().getApplicationid() != null) ? Integer.parseInt(provisionreq.getProvisioninfo().getApplicationid()) : null;
           
           String subscriber = provisionreq.getProvisioninfo().getSubscriber();
           String operator = provisionreq.getProvisioninfo().getOperatorcode();
           String[] merchants = (String[])(provisionreq.getProvisioninfo().getMerchantcode()).toArray(new String[(provisionreq.getProvisioninfo().getMerchantcode()).size()]);
           
          new AxiataDbService().insertMerchantProvision(applicationid,subscriber,operator, merchants);
    }
    
}
