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

import java.util.HashMap;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;
import org.wso2.carbon.apimgt.gateway.APIMgtGatewayConstants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.datapublisher.DataPublisherConstants;
import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.entity.smsmessaging.OutboundRequest;
import com.wso2telco.dep.mediator.entity.smsmessaging.OutboundRequestOp;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.service.SMSMessagingService;
import com.wso2telco.mnc.resolver.MNCQueryClient;
import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.service.impl.sms.nb.ValidateNBDeliveryInfoNotification;
import com.wso2telco.oneapivalidation.service.impl.sms.sb.ValidateSBDeliveryInfoNotification;

// TODO: Auto-generated Javadoc
/**
 * The Class SMSOutboundNotificationsHandler.
 */
public class SMSOutboundNotificationsHandler implements SMSHandler {

	/** The smsMessagingDAO. */
	private SMSMessagingService smsMessagingService;

	/** The executor. */
	private SMSExecutor executor;

	MNCQueryClient mncQueryclient = null;

	private static Log log = LogFactory.getLog(SMSOutboundNotificationsHandler.class);

	/**
	 * Instantiates a new SMS outbound notifications handler.
	 *
	 * @param executor
	 *            the executor
	 */
	public SMSOutboundNotificationsHandler(SMSExecutor executor) {
		this.executor = executor;
		smsMessagingService = new SMSMessagingService();
		mncQueryclient = new MNCQueryClient();
		
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
		log.debug("Incoming DN Notification from Gateway : " + executor.getJsonBody().toString());
		String requestPath = executor.getSubResourcePath();
		String moSubscriptionId = requestPath.substring(requestPath.lastIndexOf("/") + 1);

		HashMap<String, String> dnSubscriptionDetails =(HashMap<String, String>) smsMessagingService.subscriptionDNNotifiMap(Integer.valueOf(moSubscriptionId));
		String notifyurl = dnSubscriptionDetails.get("notifyurl");
		String serviceProvider = dnSubscriptionDetails.get("serviceProvider");
		
		String notifyurlRoute = notifyurl;
		FileReader fileReader = new FileReader();
		Map<String, String> mediatorConfMap = fileReader.readMediatorConfFile();
		String requestRouterUrl = mediatorConfMap.get("requestRouterUrl");
		if (requestRouterUrl != null) {
			notifyurlRoute = requestRouterUrl + notifyurlRoute;
		}

        //Date Time issue
		String formattedString = null;
		String mcc = null;
        String operatormar = "+";

        //String[] params = executor.getSubResourcePath().split("/");
        Gson gson = new GsonBuilder().serializeNulls().create();
       
		if (executor.getJsonBody().toString().contains("operatorCode")) {
			OutboundRequestOp outboundRequestOp = gson.fromJson(executor.getJsonBody().toString(), OutboundRequestOp.class);
			formattedString = gson.toJson(outboundRequestOp);
			String msisdn = outboundRequestOp.getDeliveryInfoNotification().getDeliveryInfo().getAddress();
			if (msisdn.startsWith("tel:")) {
				String[] params = outboundRequestOp.getDeliveryInfoNotification().getDeliveryInfo().getAddress().split(":");
				if (params[1].startsWith("+")) {
					msisdn = params[1];
				} else {
					msisdn = operatormar.concat(params[1]);
				}
			}
			String operator = mncQueryclient.QueryNetwork(mcc, msisdn);
			context.setProperty(DataPublisherConstants.MSISDN, msisdn);
			context.setProperty(DataPublisherConstants.OPERATOR_ID,operator);
			context.setProperty(APIMgtGatewayConstants.USER_ID, serviceProvider);
		} else {
			OutboundRequest outboundRequest = gson.fromJson(executor.getJsonBody().toString(), OutboundRequest.class);
			formattedString = gson.toJson(outboundRequest);
			String msisdn = outboundRequest.getDeliveryInfoNotification().getDeliveryInfo().getAddress();
			if (msisdn.startsWith("tel:")) {
				String[] params = outboundRequest.getDeliveryInfoNotification().getDeliveryInfo().getAddress().split(":");
				if (params[1].startsWith("+")) {
					msisdn = params[1];
				} else {
					msisdn = operatormar.concat(params[1]);
				}
			}
			String operator = mncQueryclient.QueryNetwork(mcc, msisdn);
			context.setProperty(DataPublisherConstants.MSISDN, msisdn);
			context.setProperty(DataPublisherConstants.OPERATOR_ID,operator);
			context.setProperty(APIMgtGatewayConstants.USER_ID, serviceProvider);
		}
        	        
        int notifyret = executor.makeNorthBoundRequest(new OperatorEndpoint(new EndpointReference(notifyurl), null), notifyurlRoute,formattedString, true, context,false);        	
		executor.removeHeaders(context);
		if (notifyret == 0) {
			throw new CustomException("SVC1000", "", new String[]{null});
		}
		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 201);
		//executor.setResponse(context, new JSONObject(notifyret).toString());

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
		
		if (!httpMethod.equalsIgnoreCase("POST")) {
			((Axis2MessageContext) context).getAxis2MessageContext()
					.setProperty("HTTP_SC", 405);
			throw new Exception("Method not allowed");
		}

		if (jsonBody.toString().contains("operatorCode")) {
			IServiceValidate validator = new ValidateNBDeliveryInfoNotification();
			validator.validateUrl(requestPath);
			validator.validate(jsonBody.toString());
		} else {
			IServiceValidate validator = new ValidateSBDeliveryInfoNotification();
			validator.validateUrl(requestPath);
			validator.validate(jsonBody.toString());
		}
			
		return true;
	}
}
