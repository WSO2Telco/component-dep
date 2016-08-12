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

package com.wso2telco.dep.mediator.impl.payment;

import com.wso2telco.dep.mediator.service.PaymentService;
import com.wso2telco.dep.mediator.MSISDNConstants;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.ResponseHandler;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.oneapivalidation.service.impl.payment.ValidateQueryPaymentStatus;
import com.wso2telco.subscriptionvalidator.util.ValidatorUtils;
import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;

public class QueryPaymentStatusHandler implements PaymentHandler {

	private Log log = LogFactory.getLog(AmountChargeHandler.class);

	private static final String API_TYPE = "payment";
	private OriginatingCountryCalculatorIDD occi;
	private ResponseHandler responseHandler;
	private PaymentExecutor executor;
	private PaymentService dbservice;

	public QueryPaymentStatusHandler(PaymentExecutor executor) {
		this.executor = executor;
		occi = new OriginatingCountryCalculatorIDD();
		responseHandler = new ResponseHandler();
		dbservice = new PaymentService();
	}

	@Override
	public boolean handle(MessageContext context) throws Exception {

		String[] params = executor.getSubResourcePath().split("/");
		context.setProperty(MSISDNConstants.USER_MSISDN, params[1].substring(5));
		OperatorEndpoint endpoint = null;
		if (ValidatorUtils.getValidatorForSubscription(context).validate(context)) {
			endpoint = occi.getAPIEndpointsByMSISDN(params[1].replace("tel:", ""), API_TYPE, executor.getSubResourcePath(), true, executor.getValidoperators());
		}

		String sending_add = endpoint.getEndpointref().getAddress();

		String responseStr = executor.makeGetRequest(endpoint, sending_add,	executor.getSubResourcePath(), true, context, false);

		executor.removeHeaders(context);

		if (responseStr == null || responseStr.equals("") || responseStr.isEmpty()) {
			throw new CustomException("SVC1000", "", new String[] { null });
		} else {
			executor.handlePluginException(responseStr);
		}

		// set response re-applied
		executor.setResponse(context, responseStr);
		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("messageType", "application/json");
		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("ContentType", "application/json");

		return true;
	}

	@Override
	public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context) throws Exception {
		if (!httpMethod.equalsIgnoreCase("GET")) {
			((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 405);

			throw new Exception("Method not allowed");
		}

		String[] params = executor.getSubResourcePath().split("/");
		IServiceValidate validator = new ValidateQueryPaymentStatus();
		validator.validateUrl(requestPath);
		validator.validate(params);

		return true;
	}

}
