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

import java.io.File;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;
import org.wso2.carbon.utils.CarbonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.MSISDNConstants;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.entity.provision.CallbackReference;
import com.wso2telco.dep.mediator.entity.provision.ProvisionServiceRequest;
import com.wso2telco.dep.mediator.entity.provision.ProvisionServiceResponse;
import com.wso2telco.dep.mediator.entity.provision.ServiceProvisionRequest;
import com.wso2telco.dep.mediator.entity.provision.ServiceProvisionResponse;
import com.wso2telco.dep.mediator.internal.ApiUtils;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.service.ProvisionService;
import com.wso2telco.dep.mediator.util.FileNames;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.service.impl.provision.ValidateProvisionService;

public class ProvisionServiceHandler implements ProvisionHandler {

	private Log log = LogFactory.getLog(ProvisionServiceHandler.class);

	private static final String API_TYPE = "provision";
	private OriginatingCountryCalculatorIDD occi;
	private OperatorEndpoint endpoint;
	private ProvisionExecutor executor;
	private ApiUtils apiUtils;
	private ProvisionService provisionService;

	public ProvisionServiceHandler(ProvisionExecutor executor) {

		this.executor = executor;
		occi = new OriginatingCountryCalculatorIDD();
		provisionService = new ProvisionService();
	}

	@Override
	public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context)
			throws Exception {

		if (!httpMethod.equalsIgnoreCase("POST")) {

			log.debug("invalid http method in provision service validate : " + httpMethod);
			((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 405);
			throw new Exception("Method not allowed");
		}

		String[] params = executor.getSubResourcePath().split("/");
		IServiceValidate validator = new ValidateProvisionService();

		validator.validateUrl(requestPath);
		validator.validate(params);
		validator.validate(jsonBody.toString());

		return true;
	}

	@Override
	public boolean handle(MessageContext context) throws Exception {

		Gson gson = new GsonBuilder().serializeNulls().create();
		String requestid = UID.getUniqueID(Type.PROVISION.getCode(), context, executor.getApplicationid());
		String orgclientcl = null;
		String origNotifyUrl = null;
		String msisdn = null;
		String notifyUrl = null;
		String requestBody = null;
		String responseResourcrUrl = null;
		
		FileReader fileReader = new FileReader();
		String filePath = CarbonUtils.getCarbonConfigDirPath() + File.separator;
		
		HashMap<String, String> mediatorConfMap = fileReader.readPropertyFile(filePath, FileNames.MEDIATOR_CONF_FILE.getFileName());

		ProvisionServiceResponse provisionServiceResponse;

		String hub_gateway_id = mediatorConfMap.get("hub_gateway_id");
		String hub_gateway_provision_notify_url = mediatorConfMap.get("hub_gateway_provision_notify_url");
		String resourceUrlPrefix = mediatorConfMap.get("hubGateway");

		HashMap<String, String> jwtDetails = apiUtils.getJwtTokenDetails(context);
		String appId = jwtDetails.get("applicationid");
		log.debug("application is : " + appId);
		String serviceProvider = jwtDetails.get("subscriber");
		log.debug("service provider name : " + serviceProvider);
		JSONObject jsonBody = executor.getJsonBody();

		ProvisionServiceRequest provisionServiceRequest = gson.fromJson(jsonBody.toString(),
				ProvisionServiceRequest.class);

		if (provisionServiceRequest != null) {

			ServiceProvisionRequest serviceProvisionRequest = provisionServiceRequest.getServiceProvisionRequest();
			orgclientcl = serviceProvisionRequest.getClientCorrelator();

			CallbackReference callbackReference = serviceProvisionRequest.getCallbackReference();
			origNotifyUrl = callbackReference.getNotifyURL();

			if (orgclientcl == null || orgclientcl.trim().length() <= 0) {

				log.debug(
						"clientCorrelator not provided by application and hub / gateway generating clientCorrelator on behalf of application");
				String hashString = apiUtils.getHashString(jsonBody.toString());
				log.debug("hash string : " + hashString);
				serviceProvisionRequest
						.setClientCorrelator(hashString + "-" + requestid + ":" + hub_gateway_id + ":" + appId);
			} else {

				log.debug("clientCorrelator provided by application");
				serviceProvisionRequest.setClientCorrelator(orgclientcl + ":" + hub_gateway_id + ":" + appId);
			}

			Integer provisionServiceId = provisionService.provisionServiceEntry(origNotifyUrl, serviceProvider);
			notifyUrl = hub_gateway_provision_notify_url + "/" + provisionServiceId;

			callbackReference.setNotifyURL(notifyUrl);
			serviceProvisionRequest.setCallbackReference(callbackReference);
			provisionServiceRequest.setServiceProvisionRequest(serviceProvisionRequest);

			requestBody = gson.toJson(provisionServiceRequest);

			String resourcrPath[] = executor.getSubResourcePath().split("/");
			msisdn = resourcrPath[1];

			context.setProperty(MSISDNConstants.USER_MSISDN, msisdn.substring(5));
			endpoint = occi.getAPIEndpointsByMSISDN(msisdn.replace("tel:", ""), API_TYPE, executor.getSubResourcePath(),
					false, executor.getValidoperators());

			String sendingAddress = endpoint.getEndpointref().getAddress();

			String responseString = executor.makeRequest(endpoint, sendingAddress, requestBody, true, context);

			if (responseString == null | responseString.trim().length() <= 0) {

				throw new CustomException("POL0299", "", new String[] { "Error registering subscription" });
			} else {

				executor.handlePluginException(responseString);

				provisionServiceResponse = gson.fromJson(responseString, ProvisionServiceResponse.class);

				ServiceProvisionResponse serviceProvisionResponse = provisionServiceResponse
						.getServiceProvisionResponse();

				responseResourcrUrl = provisionServiceResponse.getServiceProvisionResponse().getCallbackReference()
						.getResourceURL();
				provisionService.provisionServiceOperatorEntry(responseResourcrUrl, provisionServiceId,
						endpoint.getOperator());
			}
		}

		return true;
	}
}
