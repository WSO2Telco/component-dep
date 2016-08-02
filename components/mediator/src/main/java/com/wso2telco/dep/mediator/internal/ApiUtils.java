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
package com.wso2telco.dep.mediator.internal;

import com.wso2telco.dep.mediator.entity.smsmessaging.northbound.InboundSMSMessage;
import com.wso2telco.oneapivalidation.exceptions.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class ApiUtils.
 */
public class ApiUtils {

	/** The log. */
	private Log log = LogFactory.getLog(ApiUtils.class);

	/** The processors. */
	private static Hashtable<String, Processor> processors = null;

	/** The prop. */
	private static Properties prop;

	/**
	 * Instantiates a new api utils.
	 */
	public ApiUtils() {
		loadProperties();
		processors = new Hashtable<String, Processor>();
	}

	/**
	 * Load properties.
	 */
	@Deprecated
	public static void loadProperties() {
		try {
			prop = new Properties();
			prop.load(ApiUtils.class.getResourceAsStream("/processors.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the processor.
	 *
	 * @param apiType
	 *            the api type
	 * @return the processor
	 */
	private Processor getProcessor(String apiType) {
		try {
			Processor p = processors.get(apiType);
			if (p == null) {
				String className = prop.getProperty(apiType);
				@SuppressWarnings("unchecked")
				Class<Processor> clazz = (Class<Processor>) Class.forName(className);
				p = clazz.newInstance();
				processors.put(apiType, p);
			}
			return p;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the app id.
	 *
	 * @param context
	 *            the context
	 * @param apiType
	 *            the api type
	 * @return the app id
	 */
	public String getAppID(MessageContext context, String apiType) {
		try {
			Processor p = getProcessor(apiType);
			return p.getAppID(context);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Sets the batch size.
	 *
	 * @param uri
	 *            the uri
	 * @param body
	 *            the body
	 * @param apiType
	 *            the api type
	 * @param batchSize
	 *            the batch size
	 * @return the API call
	 */
	public APICall setBatchSize(String uri, String body, String apiType, int batchSize) {
		try {
			Processor p = getProcessor(apiType);
			return p.setBatchSize(uri, body, batchSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the results.
	 *
	 * @param apiType
	 *            the api type
	 * @param response
	 *            the response
	 * @return the results
	 */
	public JSONArray getResults(String apiType, String response) {
		try {
			Processor p = getProcessor(apiType);
			return p.getResultList(response);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Generate response.
	 *
	 * @param context
	 *            the context
	 * @param apiType
	 *            the api type
	 * @param results
	 *            the results
	 * @param responses
	 *            the responses
	 * @param requestid
	 *            the requestid
	 * @return the JSON object
	 */
	public JSONObject generateResponse(MessageContext context, String apiType, JSONArray results,
			ArrayList<String> responses, String requestid) {
		try {
			String resourceURL = prop.getProperty(apiType + "_resource_url");
			Processor p = getProcessor(apiType);
			return p.generateResponse(context, results, UID.resourceURLWithappend(resourceURL, requestid, "messages"),
					responses);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Generate response.
	 *
	 * @param context
	 *            the context
	 * @param apiType
	 *            the api type
	 * @param inboundSMSMessageList
	 *            the inbound sms message list
	 * @param responses
	 *            the responses
	 * @param requestid
	 *            the requestid
	 * @return the JSON object
	 */
	public JSONObject generateResponse(MessageContext context, String apiType,
			List<InboundSMSMessage> inboundSMSMessageList, ArrayList<String> responses, String requestid) {
		try {
			String resourceURL = prop.getProperty(apiType + "_resource_url_post");
			Processor p = getProcessor(apiType);
			return p.generateResponse(context, inboundSMSMessageList,
					UID.resourceURLWithappend(resourceURL, requestid, "messages"), responses);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the jwt token details.
	 *
	 * @param context
	 *            the context
	 * @return the jwt token details
	 */
	public HashMap<String, String> getJwtTokenDetails(MessageContext context) {

		HashMap<String, String> jwtDetails = new HashMap<String, String>();

		org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) context)
				.getAxis2MessageContext();
		Object headers = axis2MessageContext.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);

		if (headers != null && headers instanceof Map) {

			try {

				Map headersMap = (Map) headers;
				String jwtparam = (String) headersMap.get("x-jwt-assertion");
				String[] jwttoken = jwtparam.split("\\.");
				String jwtbody = Base64Coder.decodeString(jwttoken[1]);
				JSONObject jwtobj = new JSONObject(jwtbody);
				jwtDetails.put("applicationid", jwtobj.getString("http://wso2.org/claims/applicationid"));
				jwtDetails.put("subscriber", jwtobj.getString("http://wso2.org/claims/subscriber"));
				jwtDetails.put("version", jwtobj.getString("http://wso2.org/claims/version"));
			} catch (JSONException ex) {

				log.error("Error in getJwtTokenDetails : " + ex.getMessage());
				throw new CustomException("SVC1000", "", new String[] { null });
			}
		}

		return jwtDetails;
	}
}
