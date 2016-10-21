/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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

import java.util.List;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;

import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.internal.ApiUtils;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.service.SMSMessagingService;
import com.wso2telco.dep.mediator.util.DataPublisherConstants;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.service.impl.smsmessaging.ValidateCancelSubscription;
import com.wso2telco.dep.operatorservice.model.OperatorSubscriptionDTO;

public class StopInboundSMSSubscriptionsHandler implements SMSHandler {

	/** The log. */
	private static Log log = LogFactory.getLog(StopInboundSMSSubscriptionsHandler.class);

	/** The Constant API_TYPE. */
	private static final String API_TYPE = "sms";

	/** The occi. */
	private OriginatingCountryCalculatorIDD occi;

	/** The smsMessagingDAO. */
	private SMSMessagingService smsMessagingService;

	/** The executor. */
	private SMSExecutor executor;

	/** The api utils. */
	private ApiUtils apiUtils;

	public StopInboundSMSSubscriptionsHandler(SMSExecutor executor) {

		this.executor = executor;
		occi = new OriginatingCountryCalculatorIDD();
		smsMessagingService = new SMSMessagingService();
		apiUtils = new ApiUtils();
	}

	/**
	 * Instantiates a new retrieve sms subscriptions handler.
	 *
	 * @param executor
	 *            the executor
	 */

	@Override
	public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context)
			throws Exception {

		if (!httpMethod.equalsIgnoreCase("DELETE")) {

			((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 405);
			throw new Exception("Method not allowed");
		}

		context.setProperty(DataPublisherConstants.OPERATION_TYPE, 205);
		IServiceValidate validator;

		String moSubscriptionId = requestPath.substring(requestPath.lastIndexOf("/") + 1);
		String[] params = { moSubscriptionId };
		validator = new ValidateCancelSubscription();
		validator.validateUrl(requestPath);
		validator.validate(params);

		return true;
	}

	@Override
	public boolean handle(MessageContext context) throws Exception {

		String requestPath = executor.getSubResourcePath();
		String moSubscriptionId = requestPath.substring(requestPath.lastIndexOf("/") + 1);

		String requestid = UID.getUniqueID(Type.DELRETSUB.getCode(), context, executor.getApplicationid());

		List<OperatorSubscriptionDTO> domainsubs = (smsMessagingService.subscriptionQuery(Integer.valueOf(moSubscriptionId)));
		if (domainsubs.isEmpty()) {

			throw new CustomException("POL0001", "",
					new String[] { "SMS eceipt Subscription Not Found: " + moSubscriptionId });
		}

		String resStr = "";

		for (OperatorSubscriptionDTO subs : domainsubs) {

			resStr = executor.makeDeleteRequest(
					new OperatorEndpoint(new EndpointReference(subs.getDomain()), subs.getOperator()), subs.getDomain(),
					null, true, context,false);
		}

		smsMessagingService.subscriptionDelete(Integer.valueOf(moSubscriptionId));
		executor.removeHeaders(context);
		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 204);

		return true;
	}
}
