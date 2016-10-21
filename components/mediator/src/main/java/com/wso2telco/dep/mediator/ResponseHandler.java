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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.utils.CarbonUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.core.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.entity.smsmessaging.DeliveryInfo;
import com.wso2telco.dep.mediator.entity.smsmessaging.DeliveryInfoList;
import com.wso2telco.dep.mediator.entity.smsmessaging.QuerySMSStatusResponse;
import com.wso2telco.dep.mediator.entity.smsmessaging.SendSMSResponse;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.util.DataPublisherConstants;
import com.wso2telco.dep.mediator.util.FileNames;;

// TODO: Auto-generated Javadoc
/**
 * The Class ResponseHandler.
 */
public class ResponseHandler {

	/** The log. */
	private Log log = LogFactory.getLog(ResponseHandler.class);

	/**
	 * Make sms send response.
	 *
	 * @param mc
	 *            the mc
	 * @param requestBody
	 *            the request body
	 * @param responseMap
	 *            the response map
	 * @param requestid
	 *            the requestid
	 * @return the string
	 */
	public String makeSmsSendResponse(MessageContext mc, String requestBody, Map<String, SendSMSResponse> responseMap, String requestid) {
		log.info("Building Send SMS Response: " + requestBody+ " Request ID: " + UID.getRequestID(mc));
		Gson gson = new GsonBuilder().create();
		SendSMSResponse finalResponse = gson.fromJson(requestBody, SendSMSResponse.class);

		DeliveryInfoList deliveryInfoListObj = new DeliveryInfoList();
		List<DeliveryInfo> deliveryInfoList = new ArrayList<DeliveryInfo>(responseMap.size());

		for (Map.Entry<String, SendSMSResponse> entry : responseMap.entrySet()) {
			SendSMSResponse smsResponse = entry.getValue();
			if (smsResponse != null) {
				DeliveryInfoList resDeliveryInfoList = smsResponse.getOutboundSMSMessageRequest().getDeliveryInfoList();
				DeliveryInfo[] resDeliveryInfos = resDeliveryInfoList.getDeliveryInfo();
				Collections.addAll(deliveryInfoList, resDeliveryInfos);
			} else {
				DeliveryInfo deliveryInfo = new DeliveryInfo();
				deliveryInfo.setAddress(entry.getKey());
				deliveryInfo.setDeliveryStatus("DeliveryImpossible");
				deliveryInfoList.add(deliveryInfo);
			}
		}
		deliveryInfoListObj.setDeliveryInfo(deliveryInfoList.toArray(new DeliveryInfo[deliveryInfoList.size()]));

		String senderAddress = finalResponse.getOutboundSMSMessageRequest().getSenderAddress();
		try {
			senderAddress = URLEncoder.encode(senderAddress, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
		String resourceURL = getResourceURL(mc, senderAddress) + "/requests/" + requestid;

		deliveryInfoListObj.setResourceURL(resourceURL + "/deliveryInfos");
		finalResponse.getOutboundSMSMessageRequest().setDeliveryInfoList(deliveryInfoListObj);
		finalResponse.getOutboundSMSMessageRequest().setResourceURL(resourceURL);

		((Axis2MessageContext) mc).getAxis2MessageContext().setProperty("HTTP_SC", 201);

		// Set Location Header
		Object headers = ((Axis2MessageContext) mc).getAxis2MessageContext()
				.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
		if (headers != null && headers instanceof Map) {
			Map headersMap = (Map) headers;
			headersMap.put("Location", resourceURL);
		}

		return gson.toJson(finalResponse);
	}

	/**
	 * Gets the resource url.
	 *
	 * @param mc
	 *            the mc
	 * @param senderAddress
	 *            the sender address
	 * @return the resource url
	 */
	private String getResourceURL(MessageContext mc, String senderAddress) {

		FileReader fileReader = new FileReader();
		String file = CarbonUtils.getCarbonConfigDirPath() + File.separator
				+ FileNames.MEDIATOR_CONF_FILE.getFileName();

		Map<String, String> mediatorConfMap = fileReader.readPropertyFile(file);

		String resourceURL = mediatorConfMap.get("sendSMSResourceURL");
		if (resourceURL != null && !resourceURL.isEmpty()) {
			resourceURL = resourceURL.substring(1, resourceURL.length() - 1) + senderAddress;
		} else {
			resourceURL = (String) mc.getProperty("REST_URL_PREFIX") + mc.getProperty("REST_FULL_REQUEST_PATH");
			resourceURL = resourceURL.substring(0, resourceURL.indexOf("/requests"));
		}
		return resourceURL;
	}

	/**
	 * Make payment response.
	 *
	 * @param jsonBody
	 *            the json body
	 * @param requestid
	 *            the requestid
	 * @return the string
	 * @throws JSONException
	 *             the JSON exception
	 */
	public String makePaymentResponse(String jsonBody, String clientCorrelator, String requestResourceURL, String requestid) throws JSONException {

		String jsonPayload = null;

		FileReader fileReader = new FileReader();
		String file = CarbonUtils.getCarbonConfigDirPath() + File.separator
				+ FileNames.MEDIATOR_CONF_FILE.getFileName();

		Map<String, String> mediatorConfMap = fileReader.readPropertyFile(file);

		Gson gson = new GsonBuilder().serializeNulls().create();
		org.json.JSONObject jsonObj = new org.json.JSONObject(jsonBody);
		JSONObject objPay = jsonObj.getJSONObject("amountTransaction");

		String endUserId = objPay.get("endUserId").toString();
		log.debug("Creating payment charge response -> endUserId : " + endUserId);
		String pluginResourceUrl = objPay.getString("resourceURL");
		log.debug("Creating payment charge response -> pluginResourceUrl : " + pluginResourceUrl);
		String pluginResourceUrlParts[] = pluginResourceUrl.split("/");
/*		String hubResourceURL = mediatorConfMap.get("hubGateway") + "/payment/v1/" + endUserId + "/transactions/amount/" + pluginResourceUrlParts[pluginResourceUrlParts.length - 1];
*/
		String hubResourceURL = mediatorConfMap.get("hubGateway")  + requestResourceURL + "/" + pluginResourceUrlParts[pluginResourceUrlParts.length - 1];
		log.debug("Creating payment charge response -> hubResourceURL : " + hubResourceURL);
		log.debug("Creating payment charge response -> requestid : " + requestid);

		try {
			//objPay.put("clientCorrelator", objPay.get("clientCorrelator").toString().split(":")[0]);
			objPay.put("clientCorrelator", clientCorrelator);
			objPay.remove("resourceURL");
			objPay.put("resourceURL", UID.resourceURL(hubResourceURL, requestid));
		} catch (Exception e) {
			log.error("Error in creating payment charge response : " + e.getMessage());
		}
		return jsonObj.toString();
	}
	
	
	
	 public String makePaymentResponseContext(MessageContext context, String jsonBody, String clientCorrelator, String requestResourceURL, String requestid) throws JSONException {
 
         String jsonPayload = null;
         FileReader fileReader = new FileReader();
         String file = CarbonUtils.getCarbonConfigDirPath() + File.separator
 				+ FileNames.MEDIATOR_CONF_FILE.getFileName();
 		Map<String, String> mediatorConfMap = fileReader.readPropertyFile(file);
 
         Gson gson = new GsonBuilder().serializeNulls().create();
         org.json.JSONObject jsonObj = new org.json.JSONObject(jsonBody);
         JSONObject objPay = jsonObj.getJSONObject("amountTransaction");
 
         String endUserId = objPay.get("endUserId").toString();
         log.debug("Creating payment charge response -> endUserId : " + endUserId);
         String pluginResourceUrl = objPay.getString("resourceURL");
         log.debug("Creating payment charge response -> pluginResourceUrl : " + pluginResourceUrl);
         String pluginResourceUrlParts[] = pluginResourceUrl.split("/");
         /*String hubResourceURL = Util.getApplicationProperty("hubGateway") + "/payment/v1/" + endUserId + "/transactions/amount/" + pluginResourceUrlParts[pluginResourceUrlParts.length - 1];*/
         String hubResourceURL = mediatorConfMap.get("hubGateway") + requestResourceURL + "/" + pluginResourceUrlParts[pluginResourceUrlParts.length - 1];
         log.debug("Creating payment charge response -> hubResourceURL : " + hubResourceURL);
         log.debug("Creating payment charge response -> requestid : " + requestid);
 
         try {
             /*objPay.put("clientCorrelator", objPay.get("clientCorrelator").toString().split(":")[0]);*/
             objPay.put("clientCorrelator", clientCorrelator);
             objPay.remove("resourceURL");
             objPay.put("resourceURL", UID.resourceURL(hubResourceURL, requestid));
         } catch (Exception e) {
             log.error("Error in creating payment charge response : " + e.getMessage());
         }
         
         String RESPONSE_CODE=context.getProperty(DataPublisherConstants.RESPONSE_CODE).toString();
         ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", Integer.valueOf(RESPONSE_CODE));
         
         return jsonObj.toString();
		     }

	/**
	 * Make query sms status response.
	 *
	 * @param mc
	 *            the mc
	 * @param senderAddress
	 *            the sender address
	 * @param requestid
	 *            the requestid
	 * @param responseMap
	 *            the response map
	 * @return the string
	 */
	public String makeQuerySmsStatusResponse(MessageContext mc, String senderAddress, String requestid, Map<String, QuerySMSStatusResponse> responseMap) {
		log.info("Building Query SMS Status Response" + " Request ID: " + UID.getRequestID(mc));
		Gson gson = new GsonBuilder().create();
		QuerySMSStatusResponse finalResponse = new QuerySMSStatusResponse();

		DeliveryInfoList deliveryInfoListObj = new DeliveryInfoList();
		List<DeliveryInfo> deliveryInfoList = new ArrayList<DeliveryInfo>(responseMap.size());

		for (Map.Entry<String, QuerySMSStatusResponse> entry : responseMap.entrySet()) {
			QuerySMSStatusResponse statusResponse = entry.getValue();
			if (statusResponse != null) {
				DeliveryInfo[] resDeliveryInfos = statusResponse.getDeliveryInfoList().getDeliveryInfo();
				Collections.addAll(deliveryInfoList, resDeliveryInfos);
			} else {
				DeliveryInfo deliveryInfo = new DeliveryInfo();
				deliveryInfo.setAddress(entry.getKey());
				deliveryInfo.setDeliveryStatus("DeliveryImpossible");
				deliveryInfoList.add(deliveryInfo);
			}
		}
		deliveryInfoListObj.setDeliveryInfo(deliveryInfoList.toArray(new DeliveryInfo[deliveryInfoList.size()]));
		try {
			senderAddress = URLEncoder.encode(senderAddress, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
		String resourceURL = getResourceURL(mc, senderAddress) + "/requests/" + requestid + "/deliveryInfos";
		deliveryInfoListObj.setResourceURL(resourceURL);

		finalResponse.setDeliveryInfoList(deliveryInfoListObj);

		((Axis2MessageContext) mc).getAxis2MessageContext().setProperty("HTTP_SC", 200);
		((Axis2MessageContext) mc).getAxis2MessageContext().setProperty("messageType", "application/json");
		return gson.toJson(finalResponse);
	}
	
	public String walletPaymentResponseContext(MessageContext context, String jsonBody, String clientCorrelator, String requestResourceURL, String requestid) throws JSONException {
		
        org.json.JSONObject jsonObj = new org.json.JSONObject(jsonBody);
        JSONObject objPay = jsonObj.getJSONObject("makePayment");
        FileReader fileReader = new FileReader();
        String file = CarbonUtils.getCarbonConfigDirPath() + File.separator
				+ FileNames.MEDIATOR_CONF_FILE.getFileName();
		Map<String, String> mediatorConfMap = fileReader.readPropertyFile(file);
        String endUserId = objPay.get("endUserId").toString();
        log.debug("Creating wallet payment charge response -> endUserId : " + endUserId);
        String pluginResourceUrl = objPay.getString("notifyURL");
        log.debug("Creating wallet payment charge response -> notifyURL : " + pluginResourceUrl);
        String pluginResourceUrlParts[] = pluginResourceUrl.split("/");
        String hubResourceURL = mediatorConfMap.get("hubGateway") + requestResourceURL + "/" + pluginResourceUrlParts[pluginResourceUrlParts.length - 1];
        log.debug("Creating wallet payment charge response -> hubResourceURL : " + hubResourceURL);
       log.debug("Creating wallet payment charge response -> requestid : " + requestid);

        return jsonObj.toString();
    }


    public String walletRefundResponseContext(MessageContext context, String jsonBody, String clientCorrelator, String requestResourceURL, String requestid) throws JSONException {

        org.json.JSONObject jsonObj = new org.json.JSONObject(jsonBody);
        JSONObject objPay = jsonObj.getJSONObject("refundTransaction");

        String endUserId = objPay.get("endUserId").toString();
        log.debug("Creating refund response -> endUserId : " + endUserId);

        return jsonObj.toString();
    }
    
    public String creditApplyResponseContext(MessageContext context, String jsonBody, String clientCorrelator, String requestResourceURL, String requestid) throws JSONException {

        org.json.JSONObject jsonObj = new org.json.JSONObject(jsonBody);
        JSONObject objPay = jsonObj.getJSONObject("creditApplyResponse");
        objPay.put("clientCorrelator", clientCorrelator);
        log.debug("JSONObject credit apply response : " + objPay);

        return jsonObj.toString();
    	    }
    	
    public String creditRefundResponseContext(MessageContext context, String jsonBody, String clientCorrelator, String requestResourceURL, String requestid) throws JSONException {

        org.json.JSONObject jsonObj = new org.json.JSONObject(jsonBody);
        JSONObject objPay = jsonObj.getJSONObject("refundResponse");
        objPay.put("clientCorrelator", clientCorrelator);
        log.debug("JSONObject credit refund response  : " + objPay);

        return jsonObj.toString();
    }
    	
		

}
