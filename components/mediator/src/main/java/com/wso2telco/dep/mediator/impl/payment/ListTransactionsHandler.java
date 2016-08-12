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

import org.apache.synapse.MessageContext;
import org.json.JSONObject;

import com.wso2telco.dep.mediator.ResponseHandler;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.service.PaymentService;

public class ListTransactionsHandler implements PaymentHandler {

	private OriginatingCountryCalculatorIDD occi;
	private ResponseHandler responseHandler;
	private PaymentExecutor executor;
	private PaymentService dbservice;

	public ListTransactionsHandler(PaymentExecutor executor) {
		this.executor = executor;
		occi = new OriginatingCountryCalculatorIDD();
		responseHandler = new ResponseHandler();
		dbservice = new PaymentService();
	}

	@Override
	public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context) throws Exception {
		throw new UnsupportedOperationException("Not supported yet."); 

	}

	@Override
	public boolean handle(MessageContext context) throws Exception {
		throw new UnsupportedOperationException("Not supported yet."); 
	}

}
