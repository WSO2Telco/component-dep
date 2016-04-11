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
package com.wso2telco.mediator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.dbutils.AxiataDbService;
import com.wso2telco.mediator.mediationrule.OriginatingCountryCalculatorIDD;

// TODO: Auto-generated Javadoc
/**
 * The Class Route.
 */
public class Route {

    /** The log. */
    private static Log log = LogFactory.getLog(Route.class);
    
    /** The Constant smsDummyurl. */
    //Get the end point location from mediation rule handler
    private static final String smsDummyurl = "http://localhost:18080/MediationTest/tnspoints/enpoint/dummysend";
    
    /** The Constant sendError. */
    private static final String sendError = "http://localhost:18080/MediationTest/tnspoints/enpoint/RequestError/error";
    
    /** The occi. */
    private OriginatingCountryCalculatorIDD occi;
    
    /** The dbservice. */
    private AxiataDbService dbservice;
    
    /** The validate. */
    private boolean validate = true;
    
    /** The response handler. */
    private ResponseHandler responseHandler;
    
    /** The subscriber. */
    private String subscriber;
    
    /** The isaggrigator. */
    private boolean isaggrigator = false;

    /**
     * Instantiates a new route.
     */
    public Route() {
        occi = new OriginatingCountryCalculatorIDD();
        dbservice = new AxiataDbService();
        responseHandler = new ResponseHandler();
    }
     
     
}
