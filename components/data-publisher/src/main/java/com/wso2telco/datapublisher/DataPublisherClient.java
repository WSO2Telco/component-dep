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
package com.wso2telco.datapublisher;

import com.google.gson.Gson;
import com.wso2telco.datapublisher.dto.SouthboundRequestPublisherDTO;
import com.wso2telco.datapublisher.dto.SouthboundResponsePublisherDTO;
import com.wso2telco.datapublisher.internal.SouthboundDataComponent;

import org.apache.axis2.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.RESTConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.apimgt.gateway.APIMgtGatewayConstants;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;
import org.wso2.carbon.apimgt.usage.publisher.DataPublisherUtil;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class DataPublisherClient.
 */
public class DataPublisherClient {

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(DataPublisherClient.class);

    /** The enabled. */
    private boolean enabled = SouthboundDataComponent.getApiMgtConfigReaderService().isEnabled();
    
    /** The publisher. */
    private volatile SouthboundDataPublisher publisher;
    
    /** The publisher class. */
    private String publisherClass = "com.wso2telco.datapublisher.SouthboundDataPublisher";

    /**
     * Publish request.
     *
     * @param mc the mc
     * @param jsonBody the json body
     */
    public void publishRequest(MessageContext mc, String jsonBody) {

        if (!enabled) {
            return;
        }

        long currentTime = System.currentTimeMillis();

        if (publisher == null) {
            synchronized (this) {
                if (publisher == null) {
                    log.debug("Instantiating Data Publisher");
                    publisher = new SouthboundDataPublisher();
                    publisher.init();
                }
            }
        }
        AuthenticationContext authContext = APISecurityUtils.getAuthenticationContext(mc);
        String consumerKey = "";
        String username = "";
        String applicationName = "";
        String applicationId = "";
        if (authContext != null) {
            consumerKey = authContext.getConsumerKey();
            username = authContext.getUsername();
            applicationName = authContext.getApplicationName();
            applicationId = authContext.getApplicationId();
        }
        String hostName = DataPublisherUtil.getHostAddress();
        String context = (String) mc.getProperty(RESTConstants.REST_API_CONTEXT);
        String api_version = (String) mc.getProperty(RESTConstants.SYNAPSE_REST_API);
        String fullRequestPath = (String) mc.getProperty(RESTConstants.REST_FULL_REQUEST_PATH);

        int tenantDomainIndex = fullRequestPath.indexOf("/t/");
        String apiPublisher = MultitenantConstants.SUPER_TENANT_DOMAIN_NAME;
        if (tenantDomainIndex != -1) {
            String temp = fullRequestPath.substring(tenantDomainIndex + 3, fullRequestPath.length());
            apiPublisher = temp.substring(0, temp.indexOf("/"));
        }

        int index = api_version.indexOf("--");
        if (index != -1) {
            api_version = api_version.substring(index + 2);
        }

        String api = api_version.split(":")[0];
        index = api.indexOf("--");
        if (index != -1) {
            api = api.substring(index + 2);
        }
        String version = (String) mc.getProperty(RESTConstants.SYNAPSE_REST_API_VERSION);
        String resource = extractResource(mc);
        String method = (String) ((Axis2MessageContext) mc).getAxis2MessageContext().getProperty(
                Constants.Configuration.HTTP_METHOD);
        String tenantDomain = MultitenantUtils.getTenantDomain(username);
        
        JSONObject amountTransaction = null;
        String taxAmount = null;
        String channel= null; 
        String onBehalfOf = null;
        String description = null;
        String transactionOperationStatus = null;
        String referenceCode =null;
        if (jsonBody != null && !jsonBody.isEmpty()) {
       		try {
        		amountTransaction = new JSONObject(jsonBody).optJSONObject("amountTransaction");
        		if (amountTransaction != null) {
        			JSONObject chargingMetaData = amountTransaction.getJSONObject("paymentAmount").getJSONObject("chargingMetaData");
		        	taxAmount = chargingMetaData.optString("taxAmount");
		        	channel = chargingMetaData.optString("channel"); 
		        	onBehalfOf = chargingMetaData.optString("onBehalfOf");
		        	JSONObject chargingInformation = amountTransaction.getJSONObject("paymentAmount").getJSONObject("chargingInformation");
		        	description = chargingInformation.optString("description");
		        	transactionOperationStatus = amountTransaction.optString("transactionOperationStatus");
                    referenceCode = amountTransaction.optString("referenceCode");
        			}		            
				} catch (JSONException e) {
                log.error("Error in converting request to json. " + e.getMessage(), e);
            }
        } 

        SouthboundRequestPublisherDTO requestPublisherDTO = new SouthboundRequestPublisherDTO();
        requestPublisherDTO.setConsumerKey(consumerKey);
        requestPublisherDTO.setContext(context);
        requestPublisherDTO.setApi_version(api_version);
        requestPublisherDTO.setApi(api);
        requestPublisherDTO.setVersion(version);
        requestPublisherDTO.setResourcePath(resource);
        requestPublisherDTO.setMethod(method);
        requestPublisherDTO.setRequestTime(currentTime);
        if (mc.getProperty(APIMgtGatewayConstants.USER_ID) != null) {
            String serviceProvider = (String) mc.getProperty(APIMgtGatewayConstants.USER_ID);
            requestPublisherDTO.setUsername(serviceProvider);
        } else {
            requestPublisherDTO.setUsername(username);
            mc.setProperty(APIMgtGatewayConstants.USER_ID, username);
        }
        requestPublisherDTO.setTenantDomain(tenantDomain);
        requestPublisherDTO.setHostName(hostName);
        requestPublisherDTO.setApiPublisher(apiPublisher);
        requestPublisherDTO.setApplicationName(applicationName);
        requestPublisherDTO.setApplicationId(applicationId);
        
        requestPublisherDTO.setRequestId((String) mc.getProperty(DataPublisherConstants.REQUEST_ID));
        requestPublisherDTO.setOperatorId((String) mc.getProperty(DataPublisherConstants.OPERATOR_ID));
        requestPublisherDTO.setSbEndpoint((String) mc.getProperty(DataPublisherConstants.SB_ENDPOINT));
        requestPublisherDTO.setChargeAmount((String) mc.getProperty(DataPublisherConstants.CHARGE_AMOUNT));
        requestPublisherDTO.setPurchaseCategoryCode((String) mc.getProperty(DataPublisherConstants.PAY_CATEGORY));
        requestPublisherDTO.setJsonBody(jsonBody);
        requestPublisherDTO.setTaxAmount(taxAmount);
        requestPublisherDTO.setChannel(channel);
        requestPublisherDTO.setOnBehalfOf(onBehalfOf);
        requestPublisherDTO.setDescription(description);
        requestPublisherDTO.setTransactionOperationStatus(transactionOperationStatus);
        requestPublisherDTO.setReferenceCode(referenceCode);

        //added to get Subscriber in end User request scenario 
       /* String userIdToPublish = requestPublisherDTO.getUsername();
        if (userIdToPublish != null && userIdToPublish.contains("@")) {
            String[] userIdArray = userIdToPublish.split("@");
            userIdToPublish = userIdArray[0];
            requestPublisherDTO.setUsername(userIdToPublish);
        }*/

        publisher.publishEvent(requestPublisherDTO);

        mc.setProperty(APIMgtGatewayConstants.CONSUMER_KEY, consumerKey);
        mc.setProperty(APIMgtGatewayConstants.CONTEXT, context);
        mc.setProperty(APIMgtGatewayConstants.API_VERSION, api_version);
        mc.setProperty(APIMgtGatewayConstants.API, api);
        mc.setProperty(APIMgtGatewayConstants.VERSION, version);
        mc.setProperty(APIMgtGatewayConstants.RESOURCE, resource);
        mc.setProperty(APIMgtGatewayConstants.HTTP_METHOD, method);
        mc.setProperty(DataPublisherConstants.REQUEST_TIME, currentTime);
        mc.setProperty(APIMgtGatewayConstants.HOST_NAME, hostName);
        mc.setProperty(APIMgtGatewayConstants.API_PUBLISHER, apiPublisher);
        mc.setProperty(APIMgtGatewayConstants.APPLICATION_NAME, applicationName);
        mc.setProperty(APIMgtGatewayConstants.APPLICATION_ID, applicationId);
    }

    /**
     * Publish response.
     *
     * @param mc the mc
     * @param jsonBody the json body
     */
    public void publishResponse(MessageContext mc, String jsonBody) {
        if (!enabled) {
            return;
        }

        Long currentTime = System.currentTimeMillis();

        Long serviceTime = currentTime - (Long) mc.getProperty(DataPublisherConstants.REQUEST_TIME);
        
        JSONObject amountTransaction = null;
        JSONObject outboundUSSDMessageRequest = null;
        String taxAmount = null;
        String channel= null; 
        String onBehalfOf = null;
        String description = null;    
        String ussdAction = null;
        String ussdSessionId = null;
		String transactionOperationStatus = null;
		String referenceCode = null;
        if (jsonBody != null && !jsonBody.isEmpty()) {
    		   try {
        		   amountTransaction = new JSONObject(jsonBody).optJSONObject("amountTransaction");
        		   if (amountTransaction != null) {
			        	JSONObject chargingMetaData = amountTransaction.getJSONObject("paymentAmount").getJSONObject("chargingMetaData");
			        	taxAmount = chargingMetaData.optString("taxAmount");
			        	channel = chargingMetaData.optString("channel"); 
			        	onBehalfOf = chargingMetaData.optString("onBehalfOf");
			        	JSONObject chargingInformation = amountTransaction.getJSONObject("paymentAmount").getJSONObject("chargingInformation");
			        	description = chargingInformation.optString("description");
			        	transactionOperationStatus = amountTransaction.optString("transactionOperationStatus");
	                    referenceCode = amountTransaction.optString("referenceCode");
        		   }
        		   outboundUSSDMessageRequest = new JSONObject(jsonBody).optJSONObject("outboundUSSDMessageRequest");
                   if(outboundUSSDMessageRequest != null) {
                       ussdAction = outboundUSSDMessageRequest.optString("ussdAction");
                       ussdSessionId = outboundUSSDMessageRequest.optString("sessionID");
                   }
    		   }catch (JSONException e) {
        	            log.error("Error in converting request to json. " + e.getMessage(), e);
        	        }
    		   try {
                   amountTransaction = new JSONObject(jsonBody).optJSONObject("makePayment");
                   if (amountTransaction != null) {
                       JSONObject chargingMetaData = amountTransaction.getJSONObject("paymentAmount").getJSONObject("chargingMetaData");
                       taxAmount = chargingMetaData.optString("taxAmount");
                      channel = chargingMetaData.optString("channel");
                       onBehalfOf = chargingMetaData.optString("onBehalfOf");
                       JSONObject chargingInformation = amountTransaction.getJSONObject("paymentAmount").getJSONObject("chargingInformation");
                       description = chargingInformation.optString("description");
                       transactionOperationStatus = amountTransaction.optString("transactionOperationStatus");
                       referenceCode = amountTransaction.optString("referenceCode");
                   }
   
                   outboundUSSDMessageRequest = new JSONObject(jsonBody).optJSONObject("outboundUSSDMessageRequest");
                   if(outboundUSSDMessageRequest != null) {
                       ussdAction = outboundUSSDMessageRequest.optString("ussdAction");
                       ussdSessionId = outboundUSSDMessageRequest.optString("sessionID");
                   }
   
	               } catch (JSONException e) {
	                   log.error("Error in converting request to json. " + e.getMessage(), e);
	               }
   
               try {
                   amountTransaction = new JSONObject(jsonBody).optJSONObject("refundTransaction");
   
                   if (amountTransaction != null) {
                       JSONObject chargingMetaData = amountTransaction.getJSONObject("paymentAmount").getJSONObject("chargingMetaData");
                       taxAmount = chargingMetaData.optString("taxAmount");
                       channel = chargingMetaData.optString("channel");
                       onBehalfOf = chargingMetaData.optString("onBehalfOf");
                       JSONObject chargingInformation = amountTransaction.getJSONObject("paymentAmount").getJSONObject("chargingInformation");
                       description = chargingInformation.optString("description");
                       transactionOperationStatus = amountTransaction.optString("transactionOperationStatus");
                       referenceCode = amountTransaction.optString("referenceCode");
                  }
   
                   outboundUSSDMessageRequest = new JSONObject(jsonBody).optJSONObject("outboundUSSDMessageRequest");
                   if(outboundUSSDMessageRequest != null) {
                       ussdAction = outboundUSSDMessageRequest.optString("ussdAction");
                       ussdSessionId = outboundUSSDMessageRequest.optString("sessionID");
                   }
   
               } catch (JSONException e) {
                   log.error("Error in converting request to json. " + e.getMessage(), e);
               }
        	  
        } 

        SouthboundResponsePublisherDTO responsePublisherDTO = new SouthboundResponsePublisherDTO();
        
        responsePublisherDTO.setRequestId((String) mc.getProperty(DataPublisherConstants.REQUEST_ID));
		responsePublisherDTO.setOperatorId((String) mc.getProperty(DataPublisherConstants.OPERATOR_ID));
		responsePublisherDTO.setResponseCode((String) mc.getProperty(DataPublisherConstants.RESPONSE_CODE));
		responsePublisherDTO.setMsisdn((String) mc.getProperty(DataPublisherConstants.MSISDN));
		responsePublisherDTO.setChargeAmount((String) mc.getProperty(DataPublisherConstants.CHARGE_AMOUNT));
		responsePublisherDTO.setPurchaseCategoryCode((String) mc.getProperty(DataPublisherConstants.PAY_CATEGORY));
		responsePublisherDTO.setOperatorRef((String) mc.getProperty(DataPublisherConstants.OPERATOR_REF));
		responsePublisherDTO.setExceptionId((String) mc.getProperty(DataPublisherConstants.EXCEPTION_ID));
		responsePublisherDTO.setExceptionMessage((String) mc.getProperty(DataPublisherConstants.EXCEPTION_MESSAGE));
        
        
        responsePublisherDTO.setConsumerKey((String) mc.getProperty(APIMgtGatewayConstants.CONSUMER_KEY));
        responsePublisherDTO.setUsername((String) mc.getProperty(APIMgtGatewayConstants.USER_ID));
        
        responsePublisherDTO.setContext((String) mc.getProperty(APIMgtGatewayConstants.CONTEXT));
        responsePublisherDTO.setApi_version((String) mc.getProperty(APIMgtGatewayConstants.API_VERSION));
        responsePublisherDTO.setApi((String) mc.getProperty(APIMgtGatewayConstants.API));
        responsePublisherDTO.setVersion((String) mc.getProperty(APIMgtGatewayConstants.VERSION));
        responsePublisherDTO.setResourcePath((String) mc.getProperty(APIMgtGatewayConstants.RESOURCE));
        responsePublisherDTO.setMethod((String) mc.getProperty(APIMgtGatewayConstants.HTTP_METHOD));
        
        responsePublisherDTO.setHostName((String) mc.getProperty(APIMgtGatewayConstants.HOST_NAME));
        responsePublisherDTO.setApiPublisher((String) mc.getProperty(APIMgtGatewayConstants.API_PUBLISHER));
        
        responsePublisherDTO.setApplicationName((String) mc.getProperty(APIMgtGatewayConstants.APPLICATION_NAME));
        responsePublisherDTO.setApplicationId((String) mc.getProperty(APIMgtGatewayConstants.APPLICATION_ID));
        responsePublisherDTO.setSpConsumerKey((String) mc.getProperty(DataPublisherConstants.SP_CONSUMER_KEY));

        if (mc.getProperty(DataPublisherConstants.RESPONSE) != null) {
				responsePublisherDTO.setResponse(Integer.parseInt((String) mc.getProperty(DataPublisherConstants.RESPONSE)));
			} else {
				responsePublisherDTO.setResponse(1);
		}
			
		responsePublisherDTO.setTenantDomain(MultitenantUtils.getTenantDomain(responsePublisherDTO.getUsername()));
		responsePublisherDTO.setResponseTime(currentTime);
		responsePublisherDTO.setServiceTime(serviceTime);
        	
        responsePublisherDTO.setJsonBody(jsonBody);
        responsePublisherDTO.setTaxAmount(taxAmount);
        responsePublisherDTO.setChannel(channel);
        responsePublisherDTO.setOnBehalfOf(onBehalfOf);
        responsePublisherDTO.setDescription(description);
        responsePublisherDTO.setUssdAction(ussdAction);
        responsePublisherDTO.setUssdSessionId(ussdSessionId);
		responsePublisherDTO.setTransactionOperationStatus(transactionOperationStatus);
        responsePublisherDTO.setReferenceCode(referenceCode);
        //log.info("HHHHHHHHHHHHHHHHHHHHHHHHHHHH			responsePublisherDTO.getApi() : " + responsePublisherDTO.getApi());
        //log.info("HHHHHHHHHHHHHHHHHHHHHHHHHHHH			responsePublisherDTO.getResourcePath() : " + responsePublisherDTO.getResourcePath()
        
       
        if (responsePublisherDTO.getApi().equals("smsmessaging") && responsePublisherDTO.getResourcePath().contains("/inbound/registrations")) {
			Gson gson = new Gson();
			if (isJSONValid(jsonBody)) {
				//log.info("HHHHHHHHHHHHHHHHHHHHHHHHHHHH			jsonBody : " + jsonBody);
				JsonPOJO jsonPOJO = gson.fromJson(jsonBody, JsonPOJO.class);
				InboundSMSMessage[] inboundSMSMessageList=jsonPOJO.getInboundSMSMessageList().getInboundSMSMessage();
				//log.info("HHHHHHHHHHHHHHHHHHHHHHHHHHHH			inboundSMSMessageList.length : " + inboundSMSMessageList.length);
				if (inboundSMSMessageList.length > 0) {
					for (InboundSMSMessage inboundSMSMessage : inboundSMSMessageList) {
						//log.info("XXXXXXXXXXXXXXXXXXXXXXXX inboundSMSMessageList : " + inboundSMSMessageList);
						responsePublisherDTO.setDateTime(inboundSMSMessage.getDateTime());
						//log.info("XXXXXXXXXXXXXXXXXXXXXXXX			inboundSMSMessageList : " + inboundSMSMessage.getDateTime());
						responsePublisherDTO.setDestinationAddress(inboundSMSMessage.getDestinationAddress());
						//log.info("XXXXXXXXXXXXXXXXXXXXXXXX			inboundSMSMessageList : " + inboundSMSMessage.getDestinationAddress());
						responsePublisherDTO.setMessage(inboundSMSMessage.getMessage());
						//log.info("XXXXXXXXXXXXXXXXXXXXXXXX			inboundSMSMessageList : " + inboundSMSMessage.getMessage());
						responsePublisherDTO.setMessageId(inboundSMSMessage.getMessageId());
						//log.info("XXXXXXXXXXXXXXXXXXXXXXXX			inboundSMSMessageList : " + inboundSMSMessage.getMessageId());
						responsePublisherDTO.setResourceURL(inboundSMSMessage.getResourceURL());
						//log.info("XXXXXXXXXXXXXXXXXXXXXXXX			inboundSMSMessageList : " + inboundSMSMessage.getResourceURL());
						responsePublisherDTO.setSenderAddress(inboundSMSMessage.getSenderAddress());
						//log.info("XXXXXXXXXXXXXXXXXXXXXXXX			inboundSMSMessageList : " + inboundSMSMessage.getSenderAddress());

						//log.info("XXXXXXXXXXXXXXXXXXXXXXXX PUBLISHED");
				        publisher.publishEvent(responsePublisherDTO);
					}
				}else {
					//log.info("YYYYYYYYYYYYYYYYYYYYYYYYY PUBLISHED");
					publisher.publishEvent(responsePublisherDTO);
				}
			}else{
				//log.info("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ PUBLISHED");
			publisher.publishEvent(responsePublisherDTO);
			}

		}else {
			//log.info("UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU PUBLISHED");
			publisher.publishEvent(responsePublisherDTO);			
		}
       
    }
    public boolean isJSONValid(String jsonString) {
		try {
			new JSONObject(jsonString);
		} catch (JSONException ex) {

			try {
				new JSONArray(jsonString);
			} catch (JSONException exN) {
				return false;
			}
		}
		return true;
	}
    /**
     * Extract resource.
     *
     * @param mc the mc
     * @return the string
     */
    private String extractResource(MessageContext mc) {
        String resource = "/";
        Pattern pattern = Pattern.compile("^/.+?/.+?([/?].+)$");
        Matcher matcher = pattern.matcher((String) mc.getProperty(RESTConstants.REST_FULL_REQUEST_PATH));
        if (matcher.find()) {
            resource = matcher.group(1);
        }
        return resource;
    }
}
