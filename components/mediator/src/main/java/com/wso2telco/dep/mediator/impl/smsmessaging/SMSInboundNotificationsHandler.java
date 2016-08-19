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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.datapublisher.DataPublisherConstants;
import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.util.FileNames;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.entity.smsmessaging.InboundRequest;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.service.SMSMessagingService;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.mnc.resolver.MNCQueryClient;
import java.io.File;
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
import org.wso2.carbon.apimgt.gateway.APIMgtGatewayConstants;
import org.wso2.carbon.utils.CarbonUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class SMSInboundNotificationsHandler.
 */
public class SMSInboundNotificationsHandler implements SMSHandler {

	/** The smsMessagingDAO. */
	private SMSMessagingService smsMessagingService;

	/** The executor. */
	private SMSExecutor executor;

	/** The mnc queryclient. */
	MNCQueryClient mncQueryclient = null;

	/**
	 * Instantiates a new SMS inbound notifications handler.
	 *
	 * @param executor
	 *            the executor
	 */
	public SMSInboundNotificationsHandler(SMSExecutor executor) {

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

		String requestPath = executor.getSubResourcePath();
		String moSubscriptionId = requestPath.substring(requestPath.lastIndexOf("/") + 1);

		FileReader fileReader = new FileReader();
		String filePath = CarbonUtils.getCarbonConfigDirPath() + File.separator;
		
		HashMap<String, String> mediatorConfMap = fileReader
				.readPropertyFile(filePath, FileNames.MEDIATOR_CONF_FILE.getFileName());

		HashMap<String, String> subscriptionDetails = (HashMap<String, String>) smsMessagingService
				.subscriptionNotifiMap(Integer.valueOf(moSubscriptionId));
		String notifyurl = subscriptionDetails.get("notifyurl");
		String serviceProvider = subscriptionDetails.get("serviceProvider");

		String notifyurlRoute = notifyurl;
		String requestRouterUrl = mediatorConfMap.get("requestRouterUrl");
		if (requestRouterUrl != null) {
			notifyurlRoute = requestRouterUrl + notifyurlRoute;
		}

		// Date Time issue
		Gson gson = new GsonBuilder().serializeNulls().create();
		InboundRequest inboundRequest = gson.fromJson(executor.getJsonBody().toString(), InboundRequest.class);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// get current date time with Date()
		Date date = new Date();
		String currentDate = dateFormat.format(date);
		String formattedDate = currentDate.replace(' ', 'T');

		inboundRequest.getInboundSMSMessageRequest().getInboundSMSMessage().setdateTime(formattedDate);
		String formattedString = gson.toJson(inboundRequest);
		String mcc = null;
		String operatormar = "+";
		String operator = mncQueryclient.QueryNetwork(mcc, operatormar
				.concat(inboundRequest.getInboundSMSMessageRequest().getInboundSMSMessage().getSenderAddress()));
		context.setProperty(DataPublisherConstants.MSISDN,
				inboundRequest.getInboundSMSMessageRequest().getInboundSMSMessage().getSenderAddress());
		context.setProperty(DataPublisherConstants.OPERATOR_ID, operator);
		context.setProperty(APIMgtGatewayConstants.USER_ID, serviceProvider);

		int notifyret = executor.makeNorthBoundRequest(new OperatorEndpoint(new EndpointReference(notifyurl), null),
				notifyurlRoute, formattedString, true, context, false);

		executor.removeHeaders(context);

		if (notifyret == 0) {
			throw new CustomException("SVC1000", "", new String[] { null });
		}

		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 200);

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
