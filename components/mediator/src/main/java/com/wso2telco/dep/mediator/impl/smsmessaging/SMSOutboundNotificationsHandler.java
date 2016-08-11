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
package com.wso2telco.dep.mediator.impl.smsmessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.datapublisher.DataPublisherConstants;
import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.entity.smsmessaging.InboundRequest;
import com.wso2telco.dep.mediator.entity.smsmessaging.OutboundRequest;
import com.wso2telco.dep.mediator.entity.smsmessaging.OutboundRequestOp;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.service.SMSMessagingService;
import com.wso2telco.oneapivalidation.exceptions.CustomException;

// TODO: Auto-generated Javadoc
/**
 * The Class SMSOutboundNotificationsHandler.
 */
public class SMSOutboundNotificationsHandler implements SMSHandler {

	/** The smsMessagingDAO. */
	private SMSMessagingService smsMessagingService;

	/** The executor. */
	private SMSExecutor executor;

	/**
	 * Instantiates a new SMS outbound notifications handler.
	 *
	 * @param executor
	 *            the executor
	 */
	public SMSOutboundNotificationsHandler(SMSExecutor executor) {
		this.executor = executor;
		smsMessagingService = new SMSMessagingService();
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.mediator.impl.sms.SMSHandler#handle(org.apache.synapse.
	 * MessageContext)
	 */
	@Override
	public boolean handle(MessageContext context) throws CustomException, AxisFault, Exception {

		String requestid = UID.getUniqueID(Type.ALERTINBOUND.getCode(), context, executor.getApplicationid());

		String requestPath = executor.getSubResourcePath();
		String moSubscriptionId = requestPath.substring(requestPath.lastIndexOf("/") + 1);

		HashMap<String, String> dnSubscriptionDetails =(HashMap<String, String>) smsMessagingService.subscriptionDNNotifiMap(Integer.valueOf(moSubscriptionId));
		String notifyurl = dnSubscriptionDetails.get("notifyurl");
		
		String notifyurlRoute = notifyurl;
		FileReader fileReader = new FileReader();
		Map<String, String> mediatorConfMap = fileReader.readMediatorConfFile();
		String requestRouterUrl = mediatorConfMap.get("requestRouterUrl");
		if (requestRouterUrl != null) {
			notifyurlRoute = requestRouterUrl + notifyurlRoute;
		}

        //Date Time issue
		String formattedString = null;
        Gson gson = new GsonBuilder().serializeNulls().create();
        /*InboundRequest inboundRequest = gson.fromJson(executor.getJsonBody().toString(), InboundRequest.class);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// get current date time with Date()
		Date date = new Date();
		String currentDate = dateFormat.format(date);
		String formattedDate = currentDate.replace(' ', 'T');

		inboundRequest.getInboundSMSMessageRequest().getInboundSMSMessage().setdateTime(formattedDate);
		String formattedString = gson.toJson(inboundRequest);
		String notifyret = executor.makeRequest(new OperatorEndpoint(new EndpointReference(notifyurl), null), notifyurl,formattedString, true, context);
*/
        if (executor.getJsonBody().toString().contains("operatorCode")) {
        	        	OutboundRequestOp outboundRequestOp = gson.fromJson(executor.getJsonBody().toString(), OutboundRequestOp.class);
        	        	formattedString = gson.toJson(outboundRequestOp);
        			}else {
        				OutboundRequest outboundRequest = gson.fromJson(executor.getJsonBody().toString(), OutboundRequest.class);
        				formattedString = gson.toJson(outboundRequest);
        			}
        	        
        String notifyret = executor.makeRequest(new OperatorEndpoint(new EndpointReference(notifyurl), null), notifyurlRoute,formattedString, true, context);        	
		executor.removeHeaders(context);
		if (notifyret == null) {
			throw new CustomException("POL0299", "",new String[] { "Error invoking SMSOutboundNotifications Endpoint" });
		}
		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 201);
		executor.setResponse(context, new JSONObject(notifyret).toString());

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.mediator.impl.sms.SMSHandler#validate(java.lang.String,
	 * java.lang.String, org.json.JSONObject, org.apache.synapse.MessageContext)
	 */
	@Override
	public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context)
			throws Exception {

		context.setProperty(DataPublisherConstants.OPERATION_TYPE, 207);
		return true;
	}
}
