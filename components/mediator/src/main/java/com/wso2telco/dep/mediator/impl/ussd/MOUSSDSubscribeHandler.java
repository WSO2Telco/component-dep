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
package com.wso2telco.dep.mediator.impl.ussd;

import com.wso2telco.dbutils.AxiataDbService;
import com.wso2telco.dep.mediator.MSISDNConstants;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.internal.Util;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;

import java.util.List;

 
// TODO: Auto-generated Javadoc
/**
 * The Class MOUSSDSubscribeHandler.
 */
public class MOUSSDSubscribeHandler implements USSDHandler {

    /** The log. */
    private Log log = LogFactory.getLog(MOUSSDSubscribeHandler.class);
    
    /** The Constant API_TYPE. */
    private static final String API_TYPE ="ussd";
    
    /** The occi. */
    private OriginatingCountryCalculatorIDD occi;
    
    /** The executor. */
    private USSDExecutor executor;
    
    /** The dbservice. */
    private AxiataDbService dbservice;

    /**
     * Instantiates a new MOUSSD subscribe handler.
     *
     * @param ussdExecutor the ussd executor
     */
    public MOUSSDSubscribeHandler(USSDExecutor ussdExecutor){

        log.info("------------------------MOUSSDSubscribeHandler Constructor-------------------");
        occi = new OriginatingCountryCalculatorIDD();
        this.executor = ussdExecutor;
        dbservice = new AxiataDbService();
    }

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.impl.ussd.USSDHandler#handle(org.apache.synapse.MessageContext)
     */
    @Override
    public boolean handle(MessageContext context) throws Exception {

        JSONObject jsonBody = executor.getJsonBody();
        String notifyUrl = jsonBody.getJSONObject("subscription").getJSONObject("callbackReference").getString("notifyURL");

        String carbonHome = System.getProperty("user.dir");
        log.debug("conf carbonHome - " + carbonHome);

        String fileLocation = carbonHome + "/repository/conf/axiataMediator_conf.properties";

        Util.getPropertyFileByPath(fileLocation);

        Integer axiataid = dbservice.ussdRequestEntry(notifyUrl);
        log.info("created axiataId  -  " + axiataid);

        String subsEndpoint = Util.propMap.get("ussdGatewayEndpoint")+axiataid;
        log.info("Subsendpoint - " +subsEndpoint);

        jsonBody.getJSONObject("subscription").getJSONObject("callbackReference").put("notifyURL", subsEndpoint);
       // jsonBody.getJSONObject("subscription").getJSONObject("callbackReference").put("notifyURL", "http://localhost:8080/DemoService/Priyan/Root/Service");


        List<OperatorEndpoint> endpoints = occi.getAPIEndpointsByApp(API_TYPE, executor.getSubResourcePath(),
                executor.getValidoperators());

        executor.removeHeaders(context);

        String responseStr ="";
        for (OperatorEndpoint endpoint : endpoints) {

            System.out.println("-------------------------endpoint---------------------------------" + endpoint.getEndpointref().getAddress() );
            responseStr = executor.makeRequest(endpoint, endpoint.getEndpointref().getAddress(), jsonBody.toString(), true, context);
            executor.handlePluginException(responseStr);

        }

        executor.setResponse(context, responseStr);
        ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("messageType", "application/json");


        return true;

    }

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.impl.ussd.USSDHandler#validate(java.lang.String, java.lang.String, org.json.JSONObject, org.apache.synapse.MessageContext)
     */
    @Override
    public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context) throws Exception {
        return false;
    }
}
