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
package com.wso2telco.dep.mediator;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.datapublisher.DataPublisherClient;
import com.wso2telco.datapublisher.DataPublisherConstants;
import com.wso2telco.dep.mediator.entity.smsmessaging.southbound.InboundSMSMessage;
import com.wso2telco.dep.mediator.entity.smsmessaging.southbound.InboundSMSMessageList;
import com.wso2telco.dep.mediator.entity.smsmessaging.southbound.SouthboundRetrieveResponse;
import com.wso2telco.dep.operatorservice.model.OperatorApplicationDTO;
import com.wso2telco.dep.operatorservice.service.OparatorService;
import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.exceptions.RequestError;
import com.wso2telco.oneapivalidation.exceptions.ResponseError;
import com.wso2telco.publisheventsdata.publisher.EventsDataPublisherClient;

// TODO: Auto-generated Javadoc
/**
 * The Class RequestExecutor.
 */
public abstract class RequestExecutor {

	/** The log. */
	private static Log log = LogFactory.getLog(RequestExecutor.class);

	/** The validoperators. */
	List<OperatorApplicationDTO> validoperators = null;

	/** The http method. */
	private String httpMethod;

	/** The json body. */
	private JSONObject jsonBody;

	/** The sub resource path. */
	private String subResourcePath;
	// others methods to be implementd
	/** The str err. */
	// <TO-DO>
	private String strErr;

	/** The publisher client. */
	private DataPublisherClient publisherClient;

	/** The events publisher client. */
	private EventsDataPublisherClient eventsPublisherClient;

	/**
	 * Gets the str err.
	 *
	 * @return the str err
	 */
	public String getStrErr() {
		return strErr;
	}

	/**
	 * Sets the str err.
	 *
	 * @param strErr
	 *            the new str err
	 */
	public void setStrErr(String strErr) {
		this.strErr = strErr;
	}

	/**
	 * Gets the access token.
	 *
	 * @param operator
	 *            the operator
	 * @return the access token
	 * @throws Exception
	 *             the exception
	 */
	protected String getAccessToken(String operator) throws Exception {
		OperatorApplicationDTO op = null;
		String token = null;

		if (operator == null) {
			return token;
		}

		String applicationid = getApplicationid();
		OparatorService dbservice = new OparatorService();
		if (applicationid == null) {
			throw new CustomException("SVC0001", "",new String[] { "Requested service is not provisioned" });
		}
		validoperators = dbservice.getApplicationOperators(Integer.valueOf(applicationid));
		if (validoperators.isEmpty()) {
			throw new CustomException("SVC0001", "",new String[] { "Requested service is not provisioned" });
		}
		for (OperatorApplicationDTO d : validoperators) {
			if (d.getOperatorname() != null && d.getOperatorname().contains(operator)) {
				op = d;
				break;
			}
		}
		//
		
		log.debug("Token time : " + op.getTokentime());
		log.debug("Token validity : " + op.getTokenvalidity());
		
		long timeexpires = (long) (op.getTokentime() + (op.getTokenvalidity() * 1000));
		log.debug("Expire time : " + timeexpires);
		
		long currtime = new Date().getTime();
		log.debug("Current time : " + currtime);
		
		
		 
		

		if (timeexpires > currtime) {
			token = op.getToken();
			log.debug("Token of " + op.getOperatorname() + " operator is active");
		} else {
			
			log.debug("Regenerating the token of " + op.getOperatorname() + " operator");
			String Strtoken = makeTokenrequest(op.getTokenurl(), "grant_type=refresh_token&refresh_token=" + op.getRefreshtoken(), ("" + op.getTokenauth()));
			
			if (Strtoken != null && Strtoken.length() > 0) {
				log.debug("Token regeneration response of " + op.getOperatorname() + " operator : " + Strtoken);
				JSONObject jsontoken = new JSONObject(Strtoken);
				token = jsontoken.getString("access_token");
				new OparatorService().updateOperatorToken(op.getOperatorid(), jsontoken.getString("refresh_token"),
						Long.parseLong(jsontoken.getString("expires_in")), new Date().getTime(), token);

			} else {
				log.error("Token regeneration response of " + op.getOperatorname() + " operator is invalid.");
			}
		}

		return token;
	}

	/** The resource url. */
	protected String resourceUrl;

	/**
	 * Gets the resource url.
	 *
	 * @return the resource url
	 */
	public String getResourceUrl() {
		return resourceUrl;
	}

	/**
	 * Sets the resource url.
	 *
	 * @param resourceUrl
	 *            the new resource url
	 */
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	/** The applicationid. */
	String applicationid;

	/**
	 * Gets the applicationid.
	 *
	 * @return the applicationid
	 */
	public String getApplicationid() {
		return applicationid;
	}

	/**
	 * Sets the applicationid.
	 *
	 * @param applicationid
	 *            the new applicationid
	 */
	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}

	/**
	 * Gets the validoperators.
	 *
	 * @return the validoperators
	 */
	public List<OperatorApplicationDTO> getValidoperators() {
		return validoperators;
	}

	/**
	 * Gets the http method.
	 *
	 * @return the http method
	 */
	public String getHttpMethod() {
		return httpMethod;
	}

	/**
	 * Gets the json body.
	 *
	 * @return the json body
	 */
	public JSONObject getJsonBody() {
		return jsonBody;
	}

	/**
	 * Gets the sub resource path.
	 *
	 * @return the sub resource path
	 */
	public String getSubResourcePath() {
		return subResourcePath;
	}

	/**
	 * Initialize.
	 *
	 * @param context
	 *            the context
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public boolean initialize(MessageContext context) throws Exception {

		// Get valid operators
		
		String applicationid = getApplicationid();
		OparatorService operatorService = new OparatorService();
		if (applicationid == null) {
			throw new CustomException("SVC0001", "", new String[] { "Requested service is not provisioned" });
		}
		validoperators = operatorService.getApplicationOperators(Integer.valueOf(applicationid));
		if (validoperators.isEmpty()) {
			throw new CustomException("SVC0001", "", new String[] { "Requested service is not provisioned" });
		}

		String apiName = (String) context.getProperty("API_NAME");
		List<Integer> activeoperators = operatorService.getActiveApplicationOperators(Integer.valueOf(applicationid),
				apiName);
		List<OperatorApplicationDTO> validoperatorsDup = new ArrayList<OperatorApplicationDTO>();
		
		for (OperatorApplicationDTO operator : validoperators) {
			if (activeoperators.contains(operator.getOperatorid())) {
				validoperatorsDup.add(operator);
			}
		}
		
		if (validoperatorsDup.isEmpty()) {
			throw new CustomException("SVC0001", "", new String[] { "Requested service is not provisioned" });
		}

		validoperators = validoperatorsDup;
		subResourcePath = (String) context.getProperty("REST_SUB_REQUEST_PATH");
		resourceUrl = (String) context.getProperty("REST_FULL_REQUEST_PATH");
		httpMethod = (String) context.getProperty("REST_METHOD");


		/*String jsonPayloadToString = JsonUtil
				.jsonPayloadToString(((Axis2MessageContext) context).getAxis2MessageContext());
		log.debug("DEBUG LOGS FOR LBS 13 : jsonPayloadToString = " + jsonPayloadToString);
		jsonBody = new JSONObject(jsonPayloadToString);
*/
		try {
			String jsonPayloadToString = JsonUtil
					.jsonPayloadToString(((Axis2MessageContext) context)
							.getAxis2MessageContext());
			jsonBody = new JSONObject(jsonPayloadToString);
		} catch (JSONException e) {
			log.error(e.getMessage());
			// ((Axis2MessageContext) context).setAxis2MessageContext("");
			// JsonUtil.newJsonPayload(((Axis2MessageContext)
			// context).getAxis2MessageContext(), "{}", true, true);
			// JsonUtil.removeJsonPayload(((Axis2MessageContext)
			// context).getAxis2MessageContext());

			// Axis2MessageContext axctx = ((Axis2MessageContext) context);
			// AxisMessage axmsg =
			// axctx.getAxis2MessageContext().getAxisMessage();
			//
			// axctx.getAxis2MessageContext().setAxisMessage(new AxisMessage());
			throw new CustomException(
					"SVC0001",
					"Json Error",
					new String[] { "Request is missing required URI components" });
		}
		return true;
	}

	/**
	 * Execute.
	 *
	 * @param context
	 *            the context
	 * @return true, if successful
	 * @throws CustomException
	 *             the custom exception
	 * @throws AxisFault
	 *             the axis fault
	 * @throws Exception
	 *             the exception
	 */
	public abstract boolean execute(MessageContext context) throws CustomException, AxisFault, Exception;

	/**
	 * Validate request.
	 *
	 * @param httpMethod
	 *            the http method
	 * @param requestPath
	 *            the request path
	 * @param jsonBody
	 *            the json body
	 * @param context
	 *            the context
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public abstract boolean validateRequest(String httpMethod, String requestPath, JSONObject jsonBody,
			MessageContext context) throws Exception;

	/**
	 * Sets the response.
	 *
	 * @param mc
	 *            the mc
	 * @param responseStr
	 *            the response str
	 * @throws AxisFault
	 *             the axis fault
	 */
	public void setResponse(MessageContext mc, String responseStr) throws AxisFault {
		JsonUtil.newJsonPayload(((Axis2MessageContext) mc).getAxis2MessageContext(), responseStr, true, true);
	}

	/**
	 * Removes the headers.
	 *
	 * @param context
	 *            the context
	 */
	public void removeHeaders(MessageContext context) {
		Object headers = ((Axis2MessageContext) context).getAxis2MessageContext()
				.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
		if (headers != null && headers instanceof Map) {
			Map headersMap = (Map) headers;
			headersMap.clear();
		}
	}

	/**
	 * Handle plugin exception.
	 *
	 * @param errResp
	 *            the err resp
	 * @throws CustomException
	 *             the custom exception
	 */
	public void handlePluginException(String errResp) throws CustomException {
		Gson gson = new GsonBuilder().serializeNulls().create();
		String messagid = null;
		String variables = null;

		if (errResp.contains("requestError")) {
			errResp = errResp.replace("[", "").replace("]", "");
		}

		ResponseError responseError = gson.fromJson(errResp, ResponseError.class);
		if (responseError == null) {
			return;
		}
		RequestError reqerror = responseError.getRequestError();
		if (reqerror == null) {
			return;
		}

		if (reqerror.getPolicyException() != null) {
			messagid = reqerror.getPolicyException().getMessageId();
			variables = reqerror.getPolicyException().getVariables();
		} else if (reqerror.getServiceException() != null) {
			messagid = reqerror.getServiceException().getMessageId();
			variables = reqerror.getServiceException().getVariables();
		} else {
			return;
		}
		throw new CustomException(messagid, "", new String[] { variables });
	}

	/**
	 * Make request.
	 *
	 * @param operatorendpoint
	 *            the operatorendpoint
	 * @param url
	 *            the url
	 * @param requestStr
	 *            the request str
	 * @param auth
	 *            the auth
	 * @param messageContext
	 *            the message context
	 * @return the string
	 */
	public String makeRequest(OperatorEndpoint operatorendpoint, String url, String requestStr, boolean auth, MessageContext messageContext , boolean inclueHeaders) {

		publishRequestData(operatorendpoint, url, requestStr, messageContext);

		 //MO Callback
        boolean isMoCallBack=false;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(requestStr);
		} catch (JSONException error) {
			error.printStackTrace();
		}
		Iterator<String> keys = jsonObject.keys();
		if( keys.hasNext() ){
		   String key = (String)keys.next();
		   if (key.equals("inboundSMSMessageNotification")||key.equals("deliveryInfoNotification")) {
		   isMoCallBack=true;
	   }		   
		}
		ICallresponse icallresponse = null;
		String retStr = "";
		int statusCode = 0;

		URL neturl;
		HttpURLConnection connection = null;

		try {
			// String Authtoken = AccessToken;
			// //FileUtil.getApplicationProperty("wow.api.bearer.token");

			// String encodeurl = URLEncoder.encode(url, "UTF-8");
			neturl = new URL(url);
			connection = (HttpURLConnection) neturl.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			//connection.setRequestProperty("charset", "utf-8");
			if (auth) {
				connection.setRequestProperty("Authorization",
						"Bearer " + getAccessToken(operatorendpoint.getOperator()));

				// Add JWT token header
				org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
						.getAxis2MessageContext();
				Object headers = axis2MessageContext
						.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);

				if (headers != null && headers instanceof Map) {
					Map headersMap = (Map) headers;
					String jwtparam = (String) headersMap.get("x-jwt-assertion");
					if (jwtparam != null) {
						connection.setRequestProperty("x-jwt-assertion", jwtparam);
					}
				}
			}

			
			if (inclueHeaders) {
				org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
						.getAxis2MessageContext();
				Object headers = axis2MessageContext
						.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
				if (headers != null && headers instanceof Map) {
					Map headersMap = (Map) headers;
					Iterator it = headersMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						connection.setRequestProperty((String) entry.getKey(),
								(String) entry.getValue()); // avoids a
															// ConcurrentModificationException
					}
				}
			}
			 
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			if (log.isDebugEnabled()) {
				log.debug("Southbound Request URL: " + connection.getRequestMethod() + " " + connection.getURL());
				log.debug("Southbound Request Headers: " + connection.getRequestProperties());
				log.debug("Southbound Request Body: " + requestStr);
			}

			
			//========================UNICODE PATCH=========================================
			BufferedOutputStream wr = new BufferedOutputStream(connection.getOutputStream());
			wr.write(requestStr.getBytes("UTF-8"));

			wr.flush();
			wr.close();
			//========================UNICODE PATCH=========================================
			 
           /*DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
             wr.writeBytes(requestStr);
            wr.flush();
            wr.close();*/
			statusCode = connection.getResponseCode();
			if ((statusCode != 200) && (statusCode != 201) && (statusCode != 400) && (statusCode != 401)) {
				throw new RuntimeException("Failed : HTTP error code : " + statusCode);
			}

			InputStream is = null;
			if ((statusCode == 200) || (statusCode == 201)) {
				is = connection.getInputStream();
			} else {
				is = connection.getErrorStream();
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String output;
			while ((output = br.readLine()) != null) {
				retStr += output;
			}
			br.close();

			if (log.isDebugEnabled()) {
				log.debug("Southbound Response Status: " + statusCode + " " + connection.getResponseMessage());
				log.debug("Southbound Response Headers: " + connection.getHeaderFields());
				log.debug("Southbound Response Body: " + retStr);
			}
		} catch (Exception e) {
			log.error("[WSRequestService ], makerequest, " + e.getMessage(), e);
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			 
			log.debug("HHHHHHHHHHHHHHHHHHHHHHHH            Mo OR DN CallBack : " + isMoCallBack);
			log.debug("HHHHHHHHHHHHHHHHHHHHHHHH            requestStr : " + requestStr);
			log.debug("HHHHHHHHHHHHHHHHHHHHHHHH            retStr : " + retStr);
			 
			  if (isMoCallBack) {
              	publishResponseData(statusCode, requestStr, messageContext);
  			}else {
  				publishResponseData(statusCode, retStr, messageContext);
  			}
		}
		return retStr;
	}

	/**
	 * Make tokenrequest.
	 *
	 * @param tokenurl
	 *            the tokenurl
	 * @param urlParameters
	 *            the url parameters
	 * @param authheader
	 *            the authheader
	 * @return the string
	 */
	protected String makeTokenrequest(String tokenurl, String urlParameters, String authheader) {
		ICallresponse icallresponse = null;
		String retStr = "";

		URL neturl;
		HttpURLConnection connection = null;

		log.debug("url : " + tokenurl + " | urlParameters : " + urlParameters + " | authheader : " + authheader);

		if ((tokenurl != null && tokenurl.length() > 0) && (urlParameters != null && urlParameters.length() > 0)
				&& (authheader != null && authheader.length() > 0)) {
			try {

				byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
				int postDataLength = postData.length;
				URL url = new URL(tokenurl);
				
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setInstanceFollowRedirects(false);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Authorization", authheader);
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("charset", "utf-8");
				connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
				connection.setUseCaches(false);

				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.write(postData);
				wr.flush();
				wr.close();

				if ((connection.getResponseCode() != 200) && (connection.getResponseCode() != 201)
						&& (connection.getResponseCode() != 400) && (connection.getResponseCode() != 401)) {
					log.debug("connection.getResponseMessage() : " + connection.getResponseMessage());
					throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
				}

				InputStream is = null;
				if ((connection.getResponseCode() == 200) || (connection.getResponseCode() == 201)) {
					is = connection.getInputStream();
				} else {
					is = connection.getErrorStream();
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String output;
				while ((output = br.readLine()) != null) {
					retStr += output;
				}
				br.close();
			} catch (Exception e) {
				log.error("[WSRequestService ], makerequest, " + e.getMessage(), e);
				return null;
			} finally {

				if (connection != null) {
					connection.disconnect();
				}
			}
		} else {
			log.error("Token refresh details are invalid.");
		}

		return retStr;
	}

	/**
	 * Make get request.
	 *
	 * @param operatorendpoint
	 *            the operatorendpoint
	 * @param url
	 *            the url
	 * @param requestStr
	 *            the request str
	 * @param auth
	 *            the auth
	 * @param messageContext
	 *            the message context
	 * @return the string
	 */
	public String makeGetRequest(OperatorEndpoint operatorendpoint, String url, String requestStr, boolean auth,
			MessageContext messageContext, boolean inclueHeaders) {

		publishRequestData(operatorendpoint, url, null, messageContext);
		int statusCode = 0;
		String retStr = "";
		URL neturl;
		HttpURLConnection connection = null;

		try {

			// String Authtoken = AccessToken;
			// //FileUtil.getApplicationProperty("wow.api.bearer.token");
			// DefaultHttpClient httpClient = new DefaultHttpClient();
			String encurl = (requestStr != null) ? url + requestStr : url;
			neturl = new URL(encurl);
			connection = (HttpURLConnection) neturl.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");

			if (auth) {
				connection.setRequestProperty("Authorization",
						"Bearer " + getAccessToken(operatorendpoint.getOperator()));

				// Add JWT token header
				org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
						.getAxis2MessageContext();
				Object headers = axis2MessageContext
						.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
				if (headers != null && headers instanceof Map) {
					Map headersMap = (Map) headers;
					String jwtparam = (String) headersMap.get("x-jwt-assertion");
					if (jwtparam != null) {
						connection.setRequestProperty("x-jwt-assertion", jwtparam);
					}
				}
			}
			
			if (inclueHeaders) {
				org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
						.getAxis2MessageContext();
				Object headers = axis2MessageContext
						.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
				if (headers != null && headers instanceof Map) {
					Map headersMap = (Map) headers;
					Iterator it = headersMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						connection.setRequestProperty((String) entry.getKey(),
								(String) entry.getValue()); // avoids a
															// ConcurrentModificationException
					}
				}
			}
				             
				 
			connection.setUseCaches(false);

			if (log.isDebugEnabled()) {
				log.debug("Southbound Request URL: " + connection.getRequestMethod() + " " + connection.getURL());
				log.debug("Southbound Request Headers: " + connection.getRequestProperties());
				log.debug("Southbound Request Body: " + requestStr);
			}

			statusCode = connection.getResponseCode();
			if ((statusCode != 200) && (statusCode != 201) && (statusCode != 400) && (statusCode != 401)) {
				throw new RuntimeException("Failed : HTTP error code : " + statusCode);
			}

			InputStream is = null;
			if ((statusCode == 200) || (statusCode == 201)) {
				is = connection.getInputStream();
			} else {
				is = connection.getErrorStream();
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String output;
			while ((output = br.readLine()) != null) {
				retStr += output;
			}
			br.close();

			if (log.isDebugEnabled()) {
				log.debug("Southbound Response Status: " + statusCode + " " + connection.getResponseMessage());
				log.debug("Southbound Response Headers: " + connection.getHeaderFields());
				log.debug("Southbound Response Body: " + retStr);
			}
		} catch (Exception e) {
			log.error("[WSRequestService ], makerequest, " + e.getMessage(), e);
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			publishResponseData(statusCode, retStr, messageContext);
		}
		return retStr;
	}

	/**
	 * Make delete request.
	 *
	 * @param operatorendpoint
	 *            the operatorendpoint
	 * @param url
	 *            the url
	 * @param requestStr
	 *            the request str
	 * @param auth
	 *            the auth
	 * @param messageContext
	 *            the message context
	 * @return the string
	 */
	public String makeDeleteRequest(OperatorEndpoint operatorendpoint, String url, String requestStr, boolean auth,
			MessageContext messageContext , boolean inclueHeaders) {

		publishRequestData(operatorendpoint, url, requestStr, messageContext);
		int statusCode = 0;
		String retStr = "";
		URL neturl;
		HttpURLConnection connection = null;

		try {

			// String Authtoken = AccessToken;
			// //FileUtil.getApplicationProperty("wow.api.bearer.token");
			// DefaultHttpClient httpClient = new DefaultHttpClient();

			String encurl = (requestStr != null) ? url + requestStr : url;
			neturl = new URL(encurl);
			connection = (HttpURLConnection) neturl.openConnection();
			connection.setRequestMethod("DELETE");
			connection.setRequestProperty("Content-Type", "application/json");

			if (auth) {
				connection.setRequestProperty("Authorization",
						"Bearer " + getAccessToken(operatorendpoint.getOperator()));

				// Add JWT token header
				org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
						.getAxis2MessageContext();
				Object headers = axis2MessageContext
						.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
				if (headers != null && headers instanceof Map) {
					Map headersMap = (Map) headers;
					String jwtparam = (String) headersMap.get("x-jwt-assertion");
					if (jwtparam != null) {
						connection.setRequestProperty("x-jwt-assertion", jwtparam);
					}
				}
			}
			
			
			if (inclueHeaders) {
				org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
						.getAxis2MessageContext();
				Object headers = axis2MessageContext
						.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
				if (headers != null && headers instanceof Map) {
					Map headersMap = (Map) headers;
					Iterator it = headersMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						connection.setRequestProperty((String) entry.getKey(),
								(String) entry.getValue()); // avoids a
															// ConcurrentModificationException
					}
				}
			}
			
			connection.setUseCaches(false);

			if (log.isDebugEnabled()) {
				log.debug("Southbound Request URL: " + connection.getRequestMethod() + " " + connection.getURL());
				log.debug("Southbound Request Headers: " + connection.getRequestProperties());
				log.debug("Southbound Request Body: " + requestStr);
			}

			if (requestStr != null) {
				connection.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(requestStr);
				wr.flush();
				wr.close();
			}

			statusCode = connection.getResponseCode();
			log.debug("response code: " + statusCode);

			if ((statusCode != 200) && (statusCode != 201) && (statusCode != 400) && (statusCode != 401)
					&& (statusCode != 204)) {
				throw new RuntimeException("Failed : HTTP error code : " + statusCode);
			}

			InputStream is = null;
			if ((statusCode == 200) || (statusCode == 201) || (statusCode == 204)) {
				is = connection.getInputStream();
			} else {
				is = connection.getErrorStream();
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String output;
			while ((output = br.readLine()) != null) {
				retStr += output;
			}
			br.close();

			if (log.isDebugEnabled()) {
				log.debug("Southbound Response Status: " + statusCode + " " + connection.getResponseMessage());
				log.debug("Southbound Response Headers: " + connection.getHeaderFields());
				log.debug("Southbound Response Body: " + retStr);
			}
		} catch (Exception e) {
			log.error("[WSRequestService ], makerequest, " + e.getMessage(), e);
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			publishResponseData(statusCode, retStr, messageContext);
		}
		return retStr;
	}

	/**
	 * Make north bound request.
	 *
	 * @param operatorendpoint
	 *            the operatorendpoint
	 * @param url
	 *            the url
	 * @param requestStr
	 *            the request str
	 * @param auth
	 *            the auth
	 * @param messageContext
	 *            the message context
	 * @param inclueHeaders
	 *            the inclue headers
	 * @return the int
	 */
	public int makeNorthBoundRequest(OperatorEndpoint operatorendpoint, String url, String requestStr, boolean auth,
			MessageContext messageContext, boolean inclueHeaders) {

		publishRequestData(operatorendpoint, url, requestStr, messageContext);

		ICallresponse icallresponse = null;
		String retStr = "";
		int statusCode = 0;

		URL neturl;
		HttpURLConnection connection = null;

		try {

			neturl = new URL(url);
			connection = (HttpURLConnection) neturl.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Accept-Charset", "UTF-8");// ADDED
			if (auth) {
				connection.setRequestProperty("Authorization",
						"Bearer " + getAccessToken(operatorendpoint.getOperator()));

				// Add JWT token header
				org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
						.getAxis2MessageContext();
				Object headers = axis2MessageContext
						.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
				if (headers != null && headers instanceof Map) {
					Map headersMap = (Map) headers;
					String jwtparam = (String) headersMap.get("x-jwt-assertion");
					if (jwtparam != null) {
						connection.setRequestProperty("x-jwt-assertion", jwtparam);
					}
				}
			}

			if (inclueHeaders) {
				org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
						.getAxis2MessageContext();
				Object headers = axis2MessageContext
						.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
				if (headers != null && headers instanceof Map) {
					Map headersMap = (Map) headers;
					Iterator it = headersMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						connection.setRequestProperty((String) entry.getKey(), (String) entry.getValue()); // avoids
																											// a
																											// ConcurrentModificationException
					}
				}
			}
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			if (log.isDebugEnabled()) {
				log.debug("Northbound Request URL: " + connection.getRequestMethod() + " " + connection.getURL());
				log.debug("Northbound Request Headers: " + connection.getRequestProperties());
				log.debug("Northbound Request Body: " + requestStr);
			}

			// ========================UNICODE
			// PATCH=========================================
			BufferedOutputStream wr = new BufferedOutputStream(connection.getOutputStream());
			wr.write(requestStr.getBytes("UTF-8"));
			wr.flush();
			wr.close();
			// ========================UNICODE
			// PATCH=========================================

			statusCode = connection.getResponseCode();

			if (log.isDebugEnabled()) {
				log.debug("Northbound Response Status: " + statusCode + " " + connection.getResponseMessage());
				log.debug("Northbound Response Headers: " + connection.getHeaderFields());
				log.debug("Northbound Response Body: " + retStr);
			}

			if (statusCode != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + statusCode);
			}

		} catch (Exception e) {
			log.error("[WSRequestService ], makerequest, " + e.getMessage(), e);
			return 0;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			publishResponseData(statusCode, retStr, messageContext);
		}

		return statusCode;
	}
	
	
	public String makeRetrieveSMSGetRequest(OperatorEndpoint operatorendpoint, String url, String requestStr, boolean auth,
			MessageContext messageContext, boolean inclueHeaders) {

		Gson gson = new GsonBuilder().serializeNulls().create();
		publishRequestData(operatorendpoint, url, null, messageContext);
		int statusCode = 0;
		String retStr = "";
		URL neturl;
		HttpURLConnection connection = null;

		try {

			// String Authtoken = AccessToken;
			// //FileUtil.getApplicationProperty("wow.api.bearer.token");
			// DefaultHttpClient httpClient = new DefaultHttpClient();
			String encurl = (requestStr != null) ? url + requestStr : url;
			neturl = new URL(encurl);
			connection = (HttpURLConnection) neturl.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");

			if (auth) {
				connection.setRequestProperty("Authorization", "Bearer "
						+ getAccessToken(operatorendpoint.getOperator()));

				// Add JWT token header
				org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
						.getAxis2MessageContext();
				Object headers = axis2MessageContext
						.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
				if (headers != null && headers instanceof Map) {
					Map headersMap = (Map) headers;
					String jwtparam = (String) headersMap
							.get("x-jwt-assertion");
					if (jwtparam != null) {
						connection.setRequestProperty("x-jwt-assertion",
								jwtparam);
					}
				}
			}

			if (inclueHeaders) {
				org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
						.getAxis2MessageContext();
				Object headers = axis2MessageContext
						.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
				if (headers != null && headers instanceof Map) {
					Map headersMap = (Map) headers;
					Iterator it = headersMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						connection.setRequestProperty((String) entry.getKey(),
								(String) entry.getValue()); // avoids a
															// ConcurrentModificationException
					}
				}
			}

			connection.setUseCaches(false);

			if (log.isDebugEnabled()) {
				log.debug("Southbound Request URL: " + connection.getRequestMethod() + " "+ connection.getURL());
				log.debug("Southbound Request Headers: " + connection.getRequestProperties());
				log.debug("Southbound Request Body: " + requestStr);
			}

			statusCode = connection.getResponseCode();
			if ((statusCode != 200) && (statusCode != 201) && (statusCode != 400) && (statusCode != 401)) {
				throw new RuntimeException("Failed : HTTP error code : " + statusCode);
			}

			InputStream is = null;
			if ((statusCode == 200) || (statusCode == 201)) {
				is = connection.getInputStream();
			} else {
				is = connection.getErrorStream();
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String output;
			while ((output = br.readLine()) != null) {
				retStr += output;
			}
			br.close();

			if (log.isDebugEnabled()) {
				log.debug("Southbound Response Status: " + statusCode + " "
						+ connection.getResponseMessage());
				log.debug("Southbound Response Headers: "
						+ connection.getHeaderFields());
				log.debug("Southbound Response Body: " + retStr);
			}

			SouthboundRetrieveResponse sbRetrieveResponse = gson.fromJson(retStr,SouthboundRetrieveResponse.class);
			if (sbRetrieveResponse != null) {
				
				if (sbRetrieveResponse.getInboundSMSMessageList().getInboundSMSMessage()!=null && sbRetrieveResponse.getInboundSMSMessageList().getInboundSMSMessage().length!=0) {
           		InboundSMSMessage[] inboundSMSMessageResponses = sbRetrieveResponse.getInboundSMSMessageList().getInboundSMSMessage();
                    messageContext.setProperty(DataPublisherConstants.RESPONSE, String.valueOf(inboundSMSMessageResponses.length));
        		}else{
        			InboundSMSMessage[] inboundSMSMessageResponses = new InboundSMSMessage[0];
        			InboundSMSMessageList inboundSMSMessageList=new InboundSMSMessageList();
        			inboundSMSMessageList.setInboundSMSMessage(inboundSMSMessageResponses);
        			inboundSMSMessageList.setNumberOfMessagesInThisBatch("0");
        			inboundSMSMessageList.setResourceURL("Not Available");
        			inboundSMSMessageList.setTotalNumberOfPendingMessages("0");
        			sbRetrieveResponse.setInboundSMSMessageList(inboundSMSMessageList);
				
				InboundSMSMessage[] inboundSMSMessageResponsesN = sbRetrieveResponse.getInboundSMSMessageList().getInboundSMSMessage();
				messageContext.setProperty(DataPublisherConstants.RESPONSE,String.valueOf(inboundSMSMessageResponsesN.length));
        		} 
			}else {
				messageContext.setProperty(DataPublisherConstants.RESPONSE,String.valueOf(0));
			}
		} catch (Exception e) {
			log.error("[WSRequestService ], makerequest, " + e.getMessage(), e);
			messageContext.setProperty(DataPublisherConstants.RESPONSE,String.valueOf(0));
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			publishResponseData(statusCode, retStr, messageContext);
		}

		return retStr;
	}

	/**
	 * Publish request data.
	 *
	 * @param operatorendpoint
	 *            the operatorendpoint
	 * @param url
	 *            the url
	 * @param requestStr
	 *            the request str
	 * @param messageContext
	 *            the message context
	 */
	private void publishRequestData(OperatorEndpoint operatorendpoint, String url, String requestStr,
			MessageContext messageContext) {
		// set properties for request data publisher
		messageContext.setProperty(DataPublisherConstants.OPERATOR_ID, operatorendpoint.getOperator());
		messageContext.setProperty(DataPublisherConstants.SB_ENDPOINT, url);
		
		if (requestStr != null) {
			// get chargeAmount property for payment API request
			JSONObject paymentReq = null;
			try {
				paymentReq = new JSONObject(requestStr).optJSONObject("amountTransaction");
				if (paymentReq != null) {
					String chargeAmount = paymentReq.getJSONObject("paymentAmount").getJSONObject("chargingInformation")
							.optString("amount");
					messageContext.setProperty(DataPublisherConstants.CHARGE_AMOUNT, chargeAmount);
					String payCategory = paymentReq.getJSONObject("paymentAmount").getJSONObject("chargingMetaData")
							.optString("purchaseCategoryCode");
					messageContext.setProperty(DataPublisherConstants.PAY_CATEGORY, payCategory);
				}
			} catch (JSONException e) {
				log.error("Error in converting request to json. " + e.getMessage(), e);
			}
		}

		// publish data
		if (publisherClient == null) {
			publisherClient = new DataPublisherClient();
		}
		publisherClient.publishRequest(messageContext, requestStr);
	}

	/**
	 * Publish response data.
	 *
	 * @param statusCode
	 *            the status code
	 * @param retStr
	 *            the ret str
	 * @param messageContext
	 *            the message context
	 */
	private void publishResponseData(int statusCode, String retStr, MessageContext messageContext) {
		// set properties for response data publisher
		messageContext.setProperty(DataPublisherConstants.RESPONSE_CODE, Integer.toString(statusCode));
		messageContext.setProperty(DataPublisherConstants.MSISDN,
				messageContext.getProperty(MSISDNConstants.USER_MSISDN));

		boolean isPaymentReq = false;


		if (retStr != null && !retStr.isEmpty()) {
			// get serverReferenceCode property for payment API response
			JSONObject paymentRes = null;
			// get exception property for exception response
			JSONObject exception = null;
			try {
				JSONObject response = new JSONObject(retStr);
				paymentRes = response.optJSONObject("amountTransaction");
				if (paymentRes != null) {
					if (paymentRes.has("serverReferenceCode")) {
						messageContext.setProperty(DataPublisherConstants.OPERATOR_REF,
								paymentRes.optString("serverReferenceCode"));
					} else if (paymentRes.has("originalServerReferenceCode")) {
						messageContext.setProperty(DataPublisherConstants.OPERATOR_REF,
								paymentRes.optString("originalServerReferenceCode"));
					}
					isPaymentReq = true;
				}

				exception = response.optJSONObject("requestError");
				if (exception != null) {
					JSONObject exception_body = exception.optJSONObject("serviceException");
					if (exception_body == null) {
						exception_body = exception.optJSONObject("policyException");
					}

					if (exception_body != null) {
						log.info("exception id: " + exception_body.optString("messageId"));
						log.info("exception message: " + exception_body.optString("text"));
						messageContext.setProperty(DataPublisherConstants.EXCEPTION_ID,
								exception_body.optString("messageId"));
						messageContext.setProperty(DataPublisherConstants.EXCEPTION_MESSAGE,
								exception_body.optString("text"));
						messageContext.setProperty(DataPublisherConstants.RESPONSE, "1");
					}
				}
			} catch (JSONException e) {
				log.error("Error in converting response to json. " + e.getMessage(), e);
			}
		}

		// publish data to BAM
		publisherClient.publishResponse(messageContext, retStr);

		// publish to CEP only the successful payment requests
		if (isPaymentReq && statusCode >= 200 && statusCode < 300) {
			publishToCEP(messageContext);
		}
	}

	/**
	 * Publish to cep.
	 *
	 * @param messageContext
	 *            the message context
	 */
	private void publishToCEP(MessageContext messageContext) {
		if (eventsPublisherClient == null) {
			eventsPublisherClient = new EventsDataPublisherClient();
		}
		eventsPublisherClient.publishEvent(messageContext);
	}

}
