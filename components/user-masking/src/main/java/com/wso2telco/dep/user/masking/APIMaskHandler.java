/*******************************************************************************
 * Copyright  (c) 2019, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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
package com.wso2telco.dep.user.masking;

import com.wso2telco.dep.user.masking.configuration.UserMaskingConfiguration;
import com.wso2telco.dep.user.masking.exceptions.UserMaskingException;
import com.wso2telco.dep.user.masking.utils.APIType;
import com.wso2telco.dep.user.masking.utils.MaskingUtils;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.AbstractHandler;
import org.apache.synapse.rest.RESTConstants;
import org.apache.synapse.transport.passthru.util.RelayUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.API;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.context.CarbonContext;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

public class APIMaskHandler extends AbstractHandler {

	private static final Log log = LogFactory.getLog(APIMaskHandler.class);

	@Override
	public boolean handleRequest(MessageContext messageContext) {
		try {
            String apiId = (String) messageContext.getProperty(RESTConstants.SYNAPSE_REST_API);
            apiId = apiId.replace("--", "_").replace(":v", "_");
            APIIdentifier apiIdentifier = new APIIdentifier(apiId);
            String apiProductionEndpoint = getAPIInfoProductionEndpointConfigCache().get(apiIdentifier);

            if (apiProductionEndpoint == null) {
				APIConsumer apiConsumer = getLoggedInUserConsumer();
				API api = apiConsumer.getAPI(apiIdentifier);
				apiProductionEndpoint = api.getEndpointConfig();
				getAPIInfoProductionEndpointConfigCache().put(apiIdentifier, apiProductionEndpoint);
			}

            JSONObject response = new JSONObject(apiProductionEndpoint);
			String productionEndpoint = response.getJSONObject("production_endpoints").get("url").toString();

		    // Validate this requires masking
			if (MaskingUtils.isUserAnonymizationEnabled() && isMaskingAllowedAPI(messageContext)) {
				maskRequestData(messageContext);
				setEndpointURL(messageContext, productionEndpoint);
			} else {
				messageContext.setProperty("CUSTOM_ENDPOINT", "false");
			}

		} catch (APIManagementException e) {
			log.error("Error while creating the APIIdentifier and retrieving api id from database", e);
		} catch (UserMaskingException ume) {
			log.error("Error while masking user data", ume);
			return  false;
		} catch (XMLStreamException xmlSEx){
			log.error("Error while getting validator class", xmlSEx);
		} catch (IOException ioEx){
			log.error("Error while getting validator class", ioEx);
		}

		return true;
	}

	private void maskRequestData(MessageContext messageContext) throws UserMaskingException {

		try {
			// Getting the json payload to string
			String jsonString = JsonUtil.jsonPayloadToString(((Axis2MessageContext) messageContext).getAxis2MessageContext());
			JSONObject jsonBody = new JSONObject(jsonString);

			Object headers = ((Axis2MessageContext) messageContext).getAxis2MessageContext().getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
			Map<String, String> headersMap = (Map) headers;
			String resource = headersMap.get("RESOURCE");
			APIType apiType = MaskingUtils.getAPIType(messageContext);
			String maskingSecretKey = UserMaskingConfiguration.getInstance().getSecretKey();

			if (APIType.PAYMENT.equals(apiType)) {
				// Extract user ID with request
				JSONObject objAmountTransaction = (JSONObject) jsonBody.get("amountTransaction");
				String userId = (String) objAmountTransaction.get("endUserId");
				//Convert User ID to masked User ID
				if (!UserMaskHandler.isMaskedUserId(userId)) {
					userId = UserMaskHandler.transcryptUserId(userId, true, maskingSecretKey);
				}
				// Replace resource property with updated user ID since resource path contains user ID
				if(!resource.contains(userId)) {
					String urlUserId = resource.substring(resource.indexOf("/") + 1, resource.lastIndexOf("/transactions/amount"));
					if (!UserMaskHandler.isMaskedUserId(URLDecoder.decode(urlUserId, "UTF-8"))) {
						String resourceUserId = UserMaskHandler.transcryptUserId(URLDecoder.decode(urlUserId, "UTF-8"), true, maskingSecretKey);
						resource = "/" + URLEncoder.encode(resourceUserId, "UTF-8") + "/transactions/amount";
					}
					headersMap.put("RESOURCE", resource);
				}
				// Updated payload with Masked user ID
				objAmountTransaction.put("endUserId", userId);
				jsonBody.put("amountTransaction", objAmountTransaction);
				updateJsonPayload(jsonBody.toString(), messageContext);

			} else if (APIType.SMS.equals(apiType)) {
				// Extract user IDs with request
				JSONObject outboundSMSMessageRequest = (JSONObject) jsonBody.get("outboundSMSMessageRequest");
				JSONArray addressArray = outboundSMSMessageRequest.getJSONArray("address");
				// New masked  user ID array
				JSONArray newAddressArray = new JSONArray();
				for (int i = 0; i < addressArray.length(); i++) {
					//Convert UserID to Masked User ID
					String userId = addressArray.getString(i);
					if (!UserMaskHandler.isMaskedUserId(userId) && MaskingUtils.isUnmaskedUserId(userId)) {
						String maskedAddress = UserMaskHandler.transcryptUserId(userId, true, maskingSecretKey);
						newAddressArray.put(maskedAddress);
					} else {
						newAddressArray.put(userId);
					}
				}
				// Set updated address list to request
				outboundSMSMessageRequest.put("address", newAddressArray);
				jsonBody.put("outboundSMSMessageRequest", outboundSMSMessageRequest);
				updateJsonPayload(jsonBody.toString(), messageContext);
			}

			// Set transport header indicating the request uses user anonymization
			headersMap.put("USER_ANONYMIZATION", "true");
		} catch (UnsupportedEncodingException e) {
			log.error("Error while masking user request", e);
			throw new UserMaskingException(e);
		}
	}

	/**
	 * Un mask user ID to original values before sending to service provider
	 * @param messageContext Message Context
	 * @return boolean
	 */
	@Override
	public boolean handleResponse(MessageContext messageContext) {
		Object headers = ((Axis2MessageContext) messageContext).getAxis2MessageContext().getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
		Map headersMap = (Map) headers;
		if(Boolean.valueOf((String)headersMap.get("USER_ANONYMIZATION"))) {
			// Getting the json payload to string
			String jsonString = JsonUtil.jsonPayloadToString(((Axis2MessageContext) messageContext).getAxis2MessageContext());
			JSONObject jsonBody = new JSONObject(jsonString);
			APIType apiType = MaskingUtils.getAPIType(messageContext);
			String maskingSecretKey = UserMaskingConfiguration.getInstance().getSecretKey();
			if (APIType.PAYMENT.equals(apiType)) {
				// Extract user ID with request
				if (!jsonBody.isNull("amountTransaction")) {
					JSONObject objAmountTransaction = (JSONObject) jsonBody.get("amountTransaction");
					String userId = (String) objAmountTransaction.get("endUserId");
					String resourceURL = (String) objAmountTransaction.get("resourceURL");
					//Convert Masked UserID to User ID
					if (UserMaskHandler.isMaskedUserId(userId)) {
						String maskedUserId = userId;
						try {
							userId = UserMaskHandler.transcryptUserId(userId, false, maskingSecretKey);
						} catch (Exception e) {
							log.error("Error occurred while unmasking user id ", e);
						}
						// Updated payload with Masked user ID
						objAmountTransaction.put("endUserId", userId);
						try {
							userId = URLEncoder.encode(userId, "UTF-8");
							maskedUserId = URLEncoder.encode(maskedUserId, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							log.error("Error occurred while encoding user id. Can't add decrypted user mask.");
						}
						resourceURL = resourceURL.replace(maskedUserId, userId);
					}

					objAmountTransaction.put("resourceURL", resourceURL);
					jsonBody.put("amountTransaction", objAmountTransaction);
					updateJsonPayload(jsonBody.toString(), messageContext);
				}
			} else if (APIType.SMS.equals(apiType)) {
				// Extract user IDs with request
				if (!jsonBody.isNull("outboundSMSMessageRequest")) {
					JSONObject outboundSMSMessageRequest = (JSONObject) jsonBody.get("outboundSMSMessageRequest");
					JSONArray addressArray = outboundSMSMessageRequest.getJSONArray("address");
					// New masked  user ID array
					JSONArray newAddressArray = new JSONArray();
					for (int i = 0; i < addressArray.length(); i++) {
						//Convert Masked UserID to User ID
						String userId = addressArray.getString(i);
						if (UserMaskHandler.isMaskedUserId(userId)) {
							try {
								userId = UserMaskHandler.transcryptUserId(userId, false, maskingSecretKey);
							} catch (UserMaskingException e) {
								log.error("Error occurred while unmasking user id ", e);
							}
							newAddressArray.put(userId);
						} else {
							newAddressArray.put(userId);
						}
					}
					// Set updated address list to request
					outboundSMSMessageRequest.put("address", newAddressArray);

					// Update delivery information with response
					if (!outboundSMSMessageRequest.isNull("deliveryInfoList")) {
						JSONObject deliveryInfoList = (JSONObject) outboundSMSMessageRequest.get("deliveryInfoList");
						if (!deliveryInfoList.isNull("deliveryInfo")) {
							JSONArray deliveryInfoArray = (JSONArray) deliveryInfoList.get("deliveryInfo");
							JSONArray newDeliveryInfoArray = new JSONArray();
							for (int i = 0; i < deliveryInfoArray.length(); i++) {
								JSONObject deliveryInfo = (JSONObject) deliveryInfoArray.get(i);
								JSONObject newDeliveryInfo = new JSONObject();
								newDeliveryInfo.put("deliveryStatus", deliveryInfo.get("deliveryStatus"));
								try {
									newDeliveryInfo.put("address", UserMaskHandler.transcryptUserId((String) deliveryInfo.get("address"), false, maskingSecretKey));
								} catch (Exception e) {
									log.error("Error occurred while unmasking user id ", e);
								}
								newDeliveryInfoArray.put(i, newDeliveryInfo);
							}
							deliveryInfoList.put("deliveryInfo", newDeliveryInfoArray);
						}
						outboundSMSMessageRequest.put("deliveryInfoList", deliveryInfoList);
					}

					jsonBody.put("outboundSMSMessageRequest", outboundSMSMessageRequest);
					updateJsonPayload(jsonBody.toString(), messageContext);
				}
			}
		}
		return true;
	}


    /**
     *  This method is to decide whether the request need to mask user ID
     * @param messageContext Message Context
     * @return true if masking is allowed for the API, false otherwise
     * @throws IOException
     * @throws XMLStreamException
     */
	private boolean isMaskingAllowedAPI(MessageContext messageContext) throws IOException, XMLStreamException {

		boolean isMaskingAllowAPI = false;
		org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
				.getAxis2MessageContext();
		RelayUtils.buildMessage(axis2MessageContext);

		String jsonString = JsonUtil.jsonPayloadToString(((Axis2MessageContext) messageContext).getAxis2MessageContext());
		JSONObject jsonBody = new JSONObject(jsonString);

		// Get Requested API type
		APIType apiType = MaskingUtils.getAPIType(messageContext);

		if (APIType.PAYMENT.equals(apiType) ) {
			JSONObject objAmountTransaction = (JSONObject) jsonBody.get("amountTransaction");
			String transactionOperationStatus = objAmountTransaction.get("transactionOperationStatus").toString();
            String userId = objAmountTransaction.get("endUserId").toString();
			if ((transactionOperationStatus.equalsIgnoreCase("Charged") || transactionOperationStatus.equalsIgnoreCase("Refunded"))
					&& MaskingUtils.isUnmaskedUserId(userId)) {
				isMaskingAllowAPI = true;
			}

		} else if (APIType.SMS.equals(apiType) ) {
			if (!jsonBody.isNull("outboundSMSMessageRequest")) {
				JSONObject outboundSMSMessageRequest = (JSONObject) jsonBody.get("outboundSMSMessageRequest");
				if(!outboundSMSMessageRequest.isNull("address")) {
					JSONArray addressArray =  outboundSMSMessageRequest.getJSONArray("address");
					for (int i = 0; i < addressArray.length(); i++) {
						if (MaskingUtils.isUnmaskedUserId(addressArray.getString(i))) {
							isMaskingAllowAPI = true;
							break;
						}
					}
				}
			}
		}
		return isMaskingAllowAPI;
	}

    /**
     *  Update JSON payload
     * @param jsonBody
     * @param messageContext
     */
	private void updateJsonPayload(String jsonBody, MessageContext messageContext) {
		if(jsonBody != null) {
			try {
				JsonUtil.getNewJsonPayload(((Axis2MessageContext) messageContext).getAxis2MessageContext(), jsonBody,
						true, true);
			} catch (AxisFault axisFault) {
				log.error("Error updating JSON payload. ", axisFault);
			}
		}
	}

    /**
     *  Set API endpoint  URL
	 *  This should be ONLY called when UserAnonymization is Enabled
     * @param messageContext
     */
	private void setEndpointURL(MessageContext messageContext, String endpointURL) throws UserMaskingException {
		try {

			if(endpointURL.endsWith("/")) {
				endpointURL = endpointURL.substring(0,endpointURL.length() - 1);
			}
			Object headers = ((Axis2MessageContext) messageContext).getAxis2MessageContext().getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
			Map headersMap = (Map) headers;
			String resource = (String)headersMap.get("RESOURCE");
			// Properties from API-manager.xml
			APIType type = MaskingUtils.getAPIType(messageContext);
			if(APIType.PAYMENT.equals(type)) {
				endpointURL += resource;
				EndpointReference endpointReference = new EndpointReference();
				endpointReference.setAddress(endpointURL);
				messageContext.setTo(endpointReference);
				messageContext.setProperty("CUSTOM_ENDPOINT", "true");
			} else if (APIType.SMS.equals(type))  {
				// SMS API is not required change endpoint address
				messageContext.setProperty("CUSTOM_ENDPOINT", "false");
			}
		} catch (Exception e) {
			log.error("Error while masking user request", e);
			throw new UserMaskingException(e);
		}
	}

	/** Returns an APIConsumer which is corresponding to the current logged in user taken from the carbon context
	 *
	 * @return an APIConsumer which is corresponding to the current logged in user
	 * @throws APIManagementException
	 */
	private APIConsumer getLoggedInUserConsumer() throws APIManagementException {
		String loggedInUser = CarbonContext.getThreadLocalCarbonContext().getUsername();
		if (loggedInUser == null) {
			return APIManagerFactory.getInstance().getAPIConsumer();
		}
		return APIManagerFactory.getInstance().getAPIConsumer(loggedInUser);
	}

	private Cache<APIIdentifier, String> getAPIInfoProductionEndpointConfigCache() {
		return Caching.getCacheManager(APIConstants.API_MANAGER_CACHE_MANAGER)
				.getCache("API_INFO_PRODUCTION_ENDPOINT_CONFIG_CACHE");
	}
}
