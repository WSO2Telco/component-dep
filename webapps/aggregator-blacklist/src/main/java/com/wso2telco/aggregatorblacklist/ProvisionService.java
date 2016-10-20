/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
     

