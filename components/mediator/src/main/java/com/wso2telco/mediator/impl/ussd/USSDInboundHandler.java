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
package com.wso2telco.mediator.impl.ussd;

import com.wso2telco.datapublisher.DataPublisherConstants;
import com.wso2telco.dbutils.AxiataDbService;
import com.wso2telco.mediator.OperatorEndpoint;
import com.wso2telco.mediator.internal.Util;
import com.wso2telco.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.oneapivalidation.exceptions.CustomException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class USSDInboundHandler.
 */
public class USSDInboundHandler implements USSDHandler {

    /** The log. */
    private Log log = LogFactory.getLog(USSDInboundHandler.class);
    
    /** The Constant API_TYPE. */
    private static final String API_TYPE = "ussd";
    
    /** The dbservice. */
    private AxiataDbService dbservice;
    
    /** The executor. */
    private USSDExecutor executor;
    
    /** The occi. */
    private OriginatingCountryCalculatorIDD occi;

    /**
     * Instantiates a new USSD inbound handler.
     *
     * @param executor the executor
     */
    public USSDInboundHandler(USSDExecutor executor) {
        this.executor = executor;
        dbservice = new AxiataDbService();
        occi = new OriginatingCountryCalculatorIDD();
    }

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.impl.ussd.USSDHandler#handle(org.apache.synapse.MessageContext)
     */
    @Override
    public boolean handle(MessageContext context) throws CustomException, AxisFault, Exception {

        String requestPath = executor.getSubResourcePath();
        String axiataid = requestPath.substring(requestPath.lastIndexOf("/") + 1);
        //remove non numeric chars
        axiataid = axiataid.replaceAll("[^\\d.]", "");
        log.debug("axiataId - "+axiataid);
        String notifyurl = dbservice.getUSSDNotify(Integer.valueOf(axiataid));
        log.info("notifyUrl found -  " + notifyurl);

        String carbonHome = System.getProperty("user.dir");
        log.debug("conf carbonHome - " + carbonHome);
        String fileLocation = carbonHome + "/repository/conf/axiataMediator_conf.properties";
        Util.getPropertyFileByPath(fileLocation);

        JSONObject jsonBody = executor.getJsonBody();
        jsonBody.getJSONObject("inboundUSSDMessageRequest").getJSONObject("responseRequest").put("notifyURL", notifyurl);

        String notifyret = executor.makeRequest(new OperatorEndpoint(new EndpointReference(notifyurl), null), notifyurl,
                jsonBody.toString(), true, context);

        log.debug(notifyret);
        if (notifyret == null) {
            throw new CustomException("POL0299", "", new String[]{"Error invoking Endpoint"});
        }

        JSONObject replyobj = new JSONObject(notifyret);
        String action = replyobj.getJSONObject("outboundUSSDMessageRequest").getString("ussdAction");

        if(action.equalsIgnoreCase("mtcont")){

            String subsEndpoint = Util.propMap.get("ussdGatewayEndpoint")+axiataid;
            log.info("Subsendpoint - " +subsEndpoint);
            replyobj.getJSONObject("outboundUSSDMessageRequest").getJSONObject("responseRequest").put("notifyURL", subsEndpoint);

        }

        if(action.equalsIgnoreCase("mtfin")){
            String subsEndpoint = Util.propMap.get("ussdGatewayEndpoint")+axiataid;
            log.info("Subsendpoint - " +subsEndpoint);
            replyobj.getJSONObject("outboundUSSDMessageRequest").getJSONObject("responseRequest").put("notifyURL", subsEndpoint);

            boolean deleted = dbservice.ussdEntryDelete(Integer.valueOf(axiataid));
            log.info("Entry deleted " + deleted);

        }

        executor.removeHeaders(context);

        ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 201);
        ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("messageType", "application/json");
        ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("ContentType", "application/json");
        executor.setResponse(context, replyobj.toString());

        return true;
    }

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.impl.ussd.USSDHandler#validate(java.lang.String, java.lang.String, org.json.JSONObject, org.apache.synapse.MessageContext)
     */
    @Override
    public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext
            context) throws Exception {
        context.setProperty(DataPublisherConstants.OPERATION_TYPE, 407);
        return true;
    }
}
