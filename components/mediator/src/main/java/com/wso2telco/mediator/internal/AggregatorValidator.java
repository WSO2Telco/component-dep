/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.mediator.internal;

import com.wso2telco.dbutils.AxiataDbService;
import com.wso2telco.oneapivalidation.exceptions.CustomException;

// TODO: Auto-generated Javadoc
/**
 * The Class AggregatorValidator.
 */
public class AggregatorValidator {

    /** The Db service. */
    private AxiataDbService DbService;
    
    /**
     * Instantiates a new aggregator validator.
     */
    public AggregatorValidator() {
        DbService = new AxiataDbService();        
    }
    
    /**
     * Validate merchant.
     *
     * @param appid the appid
     * @param operatorid the operatorid
     * @param subscriber the subscriber
     * @param merchant the merchant
     * @throws Exception the exception
     */
    public void validateMerchant(int appid,String operatorid, String subscriber, String merchant) throws Exception {
         
        String merchantid =  DbService.blacklistedmerchant(appid, operatorid, subscriber, merchant);
        if ( merchantid != null) {
            throw new CustomException("SVC0001", "", new String[]{merchant +" Not provisioned for "+operatorid});
        }                      
    }    
    
}
