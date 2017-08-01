/*******************************************************************************
 * Copyright  (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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
package com.wso2telco.dep.scope.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.rest.AbstractHandler;
import org.json.JSONObject;
import org.wso2.carbon.apimgt.gateway.handlers.Utils;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityConstants;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.core.dbutils.fileutils.PropertyFileReader;
import com.wso2telco.dep.scope.handler.dto.InboundMessage;
import com.wso2telco.dep.scope.handler.dto.ScopeValidationRequest;

/**
 * This class is a synapse handler that would set API ID information to the
 * header before passing the request to the backend. This handler need to be
 * added at the <handlers> section of your created api configuration.
 */
public class ScopeInfoHandler extends AbstractHandler implements ManagedLifecycle {

	private static final Log log = LogFactory.getLog(ScopeInfoHandler.class);

	private static final String MEDIATOR_CONF_FILE = "mediator-conf.properties";

	private static final String HUB_GATEWAY_ID = "hub_gateway_id";

	private static final String API_NAME = "api.ut.api";

	private static final String API_VERSION = "api.ut.version";

	private static final String IDS_REQUEST_URL = "ids_request_url";

	private static String REQUEST_URL = null;

	private static String HUB_GATEWAY_ID_VALUE = null;

	private static int errCode = 900908;

	@Override
	public void init(SynapseEnvironment synapseEnvironment) {
		getHubGatewayId();
	}

	@Override
	public void destroy() {

	}

	@Override
	public boolean handleRequest(MessageContext messageContext) {
		AuthenticationContext authContext = APISecurityUtils.getAuthenticationContext(messageContext);
		String apiName = (String) messageContext.getProperty(API_NAME);
		String token = authContext.getApiKey();
		InboundMessage inboundMessage = new InboundMessage();
		ScopeValidationRequest scopeValidationRequest = new ScopeValidationRequest();
		scopeValidationRequest.setToken(token);
		inboundMessage.setScopeValidationRequest(scopeValidationRequest);
		Gson gson = new GsonBuilder().serializeNulls().create();
		String jsonObjIDGW = gson.toJson((Object) inboundMessage);

		try {
			if (getIdsRequestUrl() != null && !getIdsRequestUrl().isEmpty()) {
				HttpPost post = new HttpPost(REQUEST_URL);
				CloseableHttpClient httpclient = null;
				CloseableHttpResponse response = null;
				httpclient = HttpClients.createDefault();
				StringEntity strEntity = new StringEntity(jsonObjIDGW.toString(), "UTF-8");
				strEntity.setContentType("application/json");
				post.setEntity((HttpEntity) strEntity);
				log.info((Object) ("Request URL: " + REQUEST_URL));
				response = httpclient.execute((HttpUriRequest) post);
				HttpEntity entity = response.getEntity();
				InputStream instream = entity.getContent();
				Scanner result = new Scanner(instream).useDelimiter("\\A");
				String jsonBody = result.hasNext() ? result.next() : "";
				JSONObject jsonObj = new JSONObject(jsonBody);
				log.info("JSON body Recieved " + jsonBody);
				if (!jsonObj.getJSONObject("scopeValidationResponse").isNull("scopes")) {
					jsonResponseParsing(jsonObj, messageContext, apiName);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	private void jsonResponseParsing(JSONObject jsonObj, MessageContext messageContext, String apiName) {

		String scopeResult = jsonObj.getJSONObject("scopeValidationResponse").getString("scopes");
		scopeResult = scopeResult.substring(1, scopeResult.length() - 1);
		String[] scopes = scopeResult.split(",");
		String[] trimmedScopes = new String[scopes.length];
		for (int i = 0; i < scopes.length; i++)
			trimmedScopes[i] = scopes[i].trim();
		if (!Arrays.asList(trimmedScopes).contains(apiName)) {
			int status;
			status = HttpStatus.SC_UNAUTHORIZED;
			Utils.setFaultPayload(messageContext,
					getFaultPayload(apiName, (String) messageContext.getProperty(API_VERSION)));
			Utils.sendFault(messageContext, status);

		}
	}

	private OMElement getFaultPayload(String apiName, String version) {
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace ns = fac.createOMNamespace(APISecurityConstants.API_SECURITY_NS,
				APISecurityConstants.API_SECURITY_NS_PREFIX);
		OMElement payload = fac.createOMElement("fault", ns);

		OMElement errorCode = fac.createOMElement("code", ns);
		errorCode.setText(String.valueOf(errCode));
		OMElement errorMessage = fac.createOMElement("message", ns);
		errorMessage.setText(APISecurityConstants.getAuthenticationFailureMessage(errCode));
		OMElement errorDetail = fac.createOMElement("description", ns);
		errorDetail.setText(APISecurityConstants.getFailureMessageDetailDescription(errCode, "Access failure for API: "
				+ apiName + ", version: " + version + " status: (" + errCode + ") - User consent not given for API"));

		payload.addChild(errorCode);
		payload.addChild(errorMessage);
		payload.addChild(errorDetail);
		return payload;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext) {
		return true;
	}

	private void getHubGatewayId() {

		PropertyFileReader fileReader = PropertyFileReader.getFileReader();
		Properties properties = fileReader.getProperties(MEDIATOR_CONF_FILE);

		HUB_GATEWAY_ID_VALUE = properties.getProperty(HUB_GATEWAY_ID);

		if (HUB_GATEWAY_ID_VALUE == null) {
			log.error("Property : " + HUB_GATEWAY_ID + " in file " + MEDIATOR_CONF_FILE + " is missing");
		}
	}

	private String getIdsRequestUrl() {

		PropertyFileReader fileReader = PropertyFileReader.getFileReader();
		Properties properties = fileReader.getProperties(MEDIATOR_CONF_FILE);

		REQUEST_URL = properties.getProperty(IDS_REQUEST_URL);

		if (REQUEST_URL == null) {
			log.error("Property : " + IDS_REQUEST_URL + " in file " + MEDIATOR_CONF_FILE + " is missing");
		}
		return REQUEST_URL;
	}

}
