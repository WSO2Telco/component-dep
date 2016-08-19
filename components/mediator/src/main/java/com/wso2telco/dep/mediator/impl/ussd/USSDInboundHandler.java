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

import com.wso2telco.datapublisher.DataPublisherConstants;
import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.service.USSDService;
import com.wso2telco.oneapivalidation.exceptions.CustomException;

import java.util.List;
import java.util.Map;

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

	/** The ussdDAO. */
	private USSDService ussdService;

	/** The executor. */
	private USSDExecutor executor;

	/** The occi. */
	private OriginatingCountryCalculatorIDD occi;

	/**
	 * Instantiates a new USSD inbound handler.
	 *
	 * @param executor
	 *            the executor
	 */
	public USSDInboundHandler(USSDExecutor executor) {

		this.executor = executor;
		ussdService = new USSDService();
		occi = new OriginatingCountryCalculatorIDD();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.mediator.impl.ussd.USSDHandler#handle(org.apache.synapse.
	 * MessageContext)
	 */
	@Override
	public boolean handle(MessageContext context) throws CustomException, AxisFault, Exception {

		String requestid = UID.getUniqueID(Type.RETRIEVE_USSD.getCode(), context, executor.getApplicationid());
		String requestPath = executor.getSubResourcePath();
		String subscriptionId = requestPath.substring(requestPath.lastIndexOf("/") + 1);
		FileReader fileReader = new FileReader();

		// remove non numeric chars
		subscriptionId = subscriptionId.replaceAll("[^\\d.]", "");
		log.debug("subscriptionId - " + subscriptionId);
		
		
		List<String> ussdSPDetails = ussdService.getUSSDNotify(Integer.valueOf(subscriptionId));
       // String notifyurl = dbservice.getUSSDNotify(Integer.valueOf(axiataid));
        log.info("notifyUrl found -  " + ussdSPDetails.get(0));
        log.info("consumerKey found - " + ussdSPDetails.get(1));
		
		

		Map<String, String> mediatorConfMap = fileReader.readMediatorConfFile();

		JSONObject jsonBody = executor.getJsonBody();
		jsonBody.getJSONObject("inboundUSSDMessageRequest").getJSONObject("responseRequest").put("notifyURL", ussdSPDetails.get(0));
		// context.setProperty(APIMgtUsagePublisherConstants.CONSUMER_KEY ,ussdSPDetails.get(1) );
        context.setProperty(DataPublisherConstants.SP_CONSUMER_KEY, ussdSPDetails.get(1));
        String notifyret = executor.makeRequest(new OperatorEndpoint(new EndpointReference(ussdSPDetails.get(0)), null), ussdSPDetails.get(0),jsonBody.toString(), true, context,false);

		log.debug(notifyret);
		if (notifyret == null) {
			throw new CustomException("POL0299", "", new String[] { "Error invoking Endpoint" });
		}

		JSONObject replyobj = new JSONObject(notifyret);
		String action = replyobj.getJSONObject("outboundUSSDMessageRequest").getString("ussdAction");

		if (action.equalsIgnoreCase("mtcont")) {

			String subsEndpoint = mediatorConfMap.get("ussdGatewayEndpoint") + subscriptionId;
			log.info("Subsendpoint - " + subsEndpoint);
			replyobj.getJSONObject("outboundUSSDMessageRequest").getJSONObject("responseRequest").put("notifyURL",
					subsEndpoint);

		}

		if (action.equalsIgnoreCase("mtfin")) {
			String subsEndpoint = mediatorConfMap.get("ussdGatewayEndpoint") + subscriptionId;
			log.info("Subsendpoint - " + subsEndpoint);
			replyobj.getJSONObject("outboundUSSDMessageRequest").getJSONObject("responseRequest").put("notifyURL",
					subsEndpoint);

			boolean deleted = ussdService.ussdEntryDelete(Integer.valueOf(subscriptionId));
			log.info("Entry deleted " + deleted);

		}
		
		if(action.equalsIgnoreCase("mocont")){
			
            String subsEndpoint = mediatorConfMap.get("ussdGatewayEndpoint")+subscriptionId;
            log.info("Subsendpoint - " +subsEndpoint);
            replyobj.getJSONObject("outboundUSSDMessageRequest").getJSONObject("responseRequest").put("notifyURL", subsEndpoint);

		}
			
		if(action.equalsIgnoreCase("mofin")){
			
            String subsEndpoint = mediatorConfMap.get("ussdGatewayEndpoint")+subscriptionId;
            log.info("Subsendpoint - " +subsEndpoint);
            replyobj.getJSONObject("outboundUSSDMessageRequest").getJSONObject("responseRequest").put("notifyURL", subsEndpoint);
	
	        //boolean deleted = dbservice.ussdEntryDelete(Integer.valueOf(axiataid));
	        //log.info("Entry deleted after session expired" + deleted);
            
		}
			 
		executor.removeHeaders(context);

		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 201);
		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("messageType", "application/json");
		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("ContentType", "application/json");
		executor.setResponse(context, replyobj.toString());

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.mediator.impl.ussd.USSDHandler#validate(java.lang.String,
	 * java.lang.String, org.json.JSONObject, org.apache.synapse.MessageContext)
	 */
	@Override
	public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context)
			throws Exception {

		context.setProperty(DataPublisherConstants.OPERATION_TYPE, 407);
		return true;
	}
}
