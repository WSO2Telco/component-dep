package com.wso2telco.dep.user.masking;

import com.wso2telco.dep.user.masking.exceptions.UserMaskingException;
import com.wso2telco.dep.user.masking.utils.APIType;
import com.wso2telco.dep.user.masking.utils.MaskingUtils;
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
import org.wso2.carbon.apimgt.impl.APIManagerFactory;

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
            APIConsumer apiConsumer = APIManagerFactory.getInstance().getAPIConsumer();
            API api = apiConsumer.getAPI(apiIdentifier);
			JSONObject response = new JSONObject(api.getEndpointConfig());
			String productionEndpoint = response.getJSONObject("production_endpoints").get("url").toString();

		    // Valudate this requires masking
			if (MaskingUtils.isUserAnonymizationEnabled() && isMaskingAllowedAPI(messageContext)) {
				maskRequestData(messageContext);
				setEndpointURL(messageContext, productionEndpoint);
			} else {
				messageContext.setProperty("CUSTOM_ENDPOINT", "false");
			}

		} catch (APIManagementException e) {
			log.error("Error while creating the APIIdentifier and retreiving api id from database", e);
		} catch (UserMaskingException e) {
			log.error("Error while masking user data", e);
			return  false;
		} catch (Exception e) {
			log.error("Error while getting validator class", e);
		}
		return true;
	}

	private void maskRequestData(MessageContext messageContext) throws Exception {

		try {
			// Getting the json payload to string
			String jsonString = JsonUtil.jsonPayloadToString(((Axis2MessageContext) messageContext).getAxis2MessageContext());
			JSONObject jsonBody = new JSONObject(jsonString);

			Object headers = ((Axis2MessageContext) messageContext).getAxis2MessageContext().getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
			Map headersMap = (Map) headers;
			String resource = (String)headersMap.get("RESOURCE");
			APIType apiType = MaskingUtils.getAPIType(messageContext);
			String maskingSecretKey = MaskingUtils.getUserMaskingConfiguration("user.masking.feature.masking.secret.key");

			if (APIType.PAYMENT.equals(apiType)) {
				// Extract user ID with request
				JSONObject objAmountTransaction = (JSONObject) jsonBody.get("amountTransaction");
				String transactionOperationStatus = objAmountTransaction.get("transactionOperationStatus").toString();
				String originalMSISDN = (String) objAmountTransaction.get("endUserId");
				// User ID for request
				String userId = originalMSISDN;
				//Convert User ID to masked User ID
				if (!UserMaskHandler.isMaskedUserId(userId)) {
					userId = UserMaskHandler.maskUserId(userId, true, maskingSecretKey);
				}
				// Replace resource property with updated user ID since resource path contains user ID
				if(!resource.contains(userId)) {
					String urlUserId = resource.substring(resource.indexOf("/") + 1, resource.lastIndexOf("/transactions/amount"));
					if (!UserMaskHandler.isMaskedUserId(URLDecoder.decode(urlUserId, "UTF-8"))) {
						String resourceUserId = UserMaskHandler.maskUserId(URLDecoder.decode(urlUserId, "UTF-8"), true, maskingSecretKey);
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
					if (!UserMaskHandler.isMaskedUserId(userId) && isMaskingAllowedUserId(userId)) {
						String maskedAddress = UserMaskHandler.maskUserId(userId, true, maskingSecretKey);
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
		} catch (Exception e) {
			log.error("Error while masking user request", e);
			throw new UserMaskingException(e);
		}
	}

	/**
	 * Un mask user ID to orignal values before sending to service provider
	 * @param messageContext
	 * @return boolean
	 */
	@Override
	public boolean handleResponse(MessageContext messageContext) {
		Object headers = ((Axis2MessageContext) messageContext).getAxis2MessageContext().getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
		Map headersMap = (Map) headers;
		if(Boolean.valueOf((String)headersMap.get("USER_ANONYMIZATION")).booleanValue()) {
			// Getting the json payload to string
			String jsonString = JsonUtil.jsonPayloadToString(((Axis2MessageContext) messageContext).getAxis2MessageContext());
			JSONObject jsonBody = new JSONObject(jsonString);
			APIType apiType = MaskingUtils.getAPIType(messageContext);
			String maskingSecretKey = MaskingUtils.getUserMaskingConfiguration("user.masking.feature.masking.secret.key");
			if (APIType.PAYMENT.equals(apiType)) {
				// Extract user ID with request
				if (!jsonBody.isNull("amountTransaction")) {
					JSONObject objAmountTransaction = (JSONObject) jsonBody.get("amountTransaction");
					String transactionOperationStatus = objAmountTransaction.get("transactionOperationStatus").toString();
					String userId = (String) objAmountTransaction.get("endUserId");
					String resourceURL = (String) objAmountTransaction.get("resourceURL");
					//Convert Masked UserID to User ID
					if (UserMaskHandler.isMaskedUserId(userId)) {
						String maskedUserId = userId;
						try {
							userId = UserMaskHandler.maskUserId(userId, false, maskingSecretKey);
						} catch (Exception e) {
							log.error("Error occurred while unmaksing user id ", e);
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
								userId = UserMaskHandler.maskUserId(userId, false, maskingSecretKey);
							} catch (Exception e) {
								log.error("Error occurred while unmaksing user id ", e);
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
								newDeliveryInfo.put("deliveryStatus", (String) deliveryInfo.get("deliveryStatus"));
								try {
									newDeliveryInfo.put("address", UserMaskHandler.maskUserId((String) deliveryInfo.get("address"), false, maskingSecretKey));
								} catch (Exception e) {
									log.error("Error occurred while unmaksing user id ", e);
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
     * @param messageContext
     * @return
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
			if (transactionOperationStatus.equalsIgnoreCase("Charged") && isMaskingAllowedUserId(userId)) {
				isMaskingAllowAPI = true;
			}

		} else if (APIType.SMS.equals(apiType) ) {
			if (!jsonBody.isNull("outboundSMSMessageRequest")) {
				JSONObject outboundSMSMessageRequest = (JSONObject) jsonBody.get("outboundSMSMessageRequest");
				if(!outboundSMSMessageRequest.isNull("address")) {
					JSONArray addressArray =  outboundSMSMessageRequest.getJSONArray("address");
					for (int i = 0; i < addressArray.length(); i++) {
						if (isMaskingAllowedUserId(addressArray.getString(i))) {
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
	 * This method is to filter the user ID bases on country or prefix in order to
	 * decide whether this number is eligible for user masking
	 * @param userId
	 * @return true if masking allowed user ID
	 */
	private boolean isMaskingAllowedUserId(String userId) {
		boolean isMaskingAllowedUserId = false;
		String regex = MaskingUtils.getUserMaskingConfiguration("user.masking.feature.user.Id.filter.regex");
		if (userId != null && regex != null) {
			isMaskingAllowedUserId = userId.matches(regex);
		}
		return isMaskingAllowedUserId;
	}

    /**
     *  Update JSON payload
     * @param jsonBody
     * @param messageContext
     */
	private void updateJsonPayload(String jsonBody, MessageContext messageContext) {
		if(jsonBody != null) {
			JsonUtil.newJsonPayload(((Axis2MessageContext) messageContext).getAxis2MessageContext(), jsonBody,
					true, true);
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
}
