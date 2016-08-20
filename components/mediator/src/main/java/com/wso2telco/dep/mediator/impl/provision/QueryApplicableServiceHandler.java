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
package com.wso2telco.dep.mediator.impl.provision;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import java.io.File;
import org.json.JSONObject;
import org.wso2.carbon.utils.CarbonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.util.FileNames;
import com.wso2telco.dep.mediator.MSISDNConstants;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.entity.provision.QueryApplicableServiceResponse;
import com.wso2telco.dep.mediator.entity.provision.ServiceList;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.service.impl.provision.ValidateQueryApplicableServices;
import com.wso2telco.subscriptionvalidator.util.ValidatorUtils;

public class QueryApplicableServiceHandler implements ProvisionHandler {

	private Log log = LogFactory.getLog(QueryApplicableServiceHandler.class);

	private static final String API_TYPE = "provision";
	private OriginatingCountryCalculatorIDD occi;
	private ProvisionExecutor executor;

	public QueryApplicableServiceHandler(ProvisionExecutor executor) {

		this.executor = executor;
		occi = new OriginatingCountryCalculatorIDD();
	}

	@Override
	public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context)
			throws Exception {

		if (!httpMethod.equalsIgnoreCase("GET")) {

			log.debug("invalid http method in query applicable service validate : " + httpMethod);
			((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 405);
			throw new Exception("Method not allowed");
		}

		String[] params = executor.getSubResourcePath().split("/");
		IServiceValidate validator = new ValidateQueryApplicableServices();

		validator.validateUrl(requestPath);
		validator.validate(params);

		return true;
	}

	@Override
	public boolean handle(MessageContext context) throws Exception {

		String[] params = executor.getSubResourcePath().split("/");
		context.setProperty(MSISDNConstants.USER_MSISDN, params[1].substring(5));

		OperatorEndpoint endpoint = null;

		if (ValidatorUtils.getValidatorForSubscription(context).validate(context)) {
			endpoint = occi.getAPIEndpointsByMSISDN(params[1].replace("tel:", ""), API_TYPE,
					executor.getSubResourcePath(), true, executor.getValidoperators());
		}

		String sendingAddress = endpoint.getEndpointref().getAddress();

		String responseStr = executor.makeGetRequest(endpoint, sendingAddress, executor.getSubResourcePath(), true,
				context);

		executor.removeHeaders(context);

		if (responseStr == null || responseStr.trim().length() <= 0) {

			log.error("service list is empty in makeQueryApplicableServiceResponse");
			throw new CustomException("SVC1000", "", new String[] { null });
		} else {

			executor.handlePluginException(responseStr);
			responseStr = makeQueryApplicableServiceResponse(responseStr);
		}

		executor.setResponse(context, responseStr);
		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("messageType", "application/json");
		((Axis2MessageContext) context).getAxis2MessageContext().setProperty("ContentType", "application/json");

		return true;
	}

	private String makeQueryApplicableServiceResponse(String responseStr) {

		Gson gson = new GsonBuilder().serializeNulls().create();
		String jsonResponse = null;
		QueryApplicableServiceResponse queryApplicableServiceResponse = null;
		String requestResourceURL = executor.getResourceUrl();

		FileReader fileReader = new FileReader();
		String file = CarbonUtils.getCarbonConfigDirPath() + File.separator
				+ FileNames.MEDIATOR_CONF_FILE.getFileName();

		Map<String, String> mediatorConfMap = fileReader.readPropertyFile(file);

		try {

			String ResourceUrlPrefix = mediatorConfMap.get("hubGateway");
			queryApplicableServiceResponse = gson.fromJson(responseStr, QueryApplicableServiceResponse.class);

			if (queryApplicableServiceResponse != null) {

				ServiceList serviceList = queryApplicableServiceResponse.getServiceList();

				if (serviceList != null) {

					serviceList.setResourceURL(ResourceUrlPrefix);
					queryApplicableServiceResponse.setServiceList(serviceList);

					jsonResponse = gson.toJson(queryApplicableServiceResponse);
				} else {

					log.error("service list is empty in makeQueryApplicableServiceResponse");
					throw new CustomException("SVC1000", "", new String[] { null });
				}
			} else {

				log.error("query applicable service response is empty in makeQueryApplicableServiceResponse");
				throw new CustomException("SVC1000", "", new String[] { null });
			}
		} catch (Exception e) {

			log.error("error in generating query applicable service response in makeQueryApplicableServiceResponse");
			throw new CustomException("SVC1000", "", new String[] { null });
		}

		return jsonResponse;
	}
}
