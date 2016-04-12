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
package com.wso2telco.mediator;


import java.util.Arrays;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.apimgt.gateway.APIMgtGatewayConstants;

import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import com.wso2telco.datapublisher.DataPublisherConstants;
import com.wso2telco.datapublisher.SouthboundDataPublisher;
import com.wso2telco.datapublisher.dto.NorthboundResponsePublisherDTO;
import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.exceptions.PolicyException;
import com.wso2telco.oneapivalidation.exceptions.RequestError;
import com.wso2telco.oneapivalidation.exceptions.ServiceException;
import com.wso2telco.publisheventsdata.publisher.EventsDataPublisherClient;

 
// TODO: Auto-generated Javadoc
/**
 * The Class NorthboundPublisher.
 */
public class NorthboundPublisher {

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(NorthboundPublisher.class);
    
    /** The enabled. */
    private boolean enabled = true;//SouthboundDataComponent.getApiMgtConfigReaderService().isEnabled();
    
    /** The publisher. */
    private volatile SouthboundDataPublisher publisher;
    
    /** The publisher class. */
    private String publisherClass = "com.axiata.dialog.mife.southbound.data.publisher.SouthboundDataPublisher";
    
    /** The events publisher client. */
    private EventsDataPublisherClient eventsPublisherClient;

    /**
     * Publish nb error response data.
     *
     * @param ax the ax
     * @param retStr the ret str
     * @param messageContext the message context
     */
    public void publishNBErrorResponseData(CustomException ax, String retStr, MessageContext messageContext) {
        //set properties for response data publisher
        //messageContext.setProperty(SouthboundPublisherConstants.RESPONSE_CODE, Integer.toString(statusCode));
        //messageContext.setProperty(SouthboundPublisherConstants.MSISDN, messageContext.getProperty(MSISDNConstants.USER_MSISDN));

        boolean isPaymentReq = false;

        if (retStr != null && !retStr.isEmpty()) {
            //get serverReferenceCode property for payment API response
            JSONObject paymentRes = null;
            //get exception property for exception response
            JSONObject exception = null;
            try {
                JSONObject response = new JSONObject(retStr);
                paymentRes = response.optJSONObject("amountTransaction");
                if (paymentRes != null) {
                    if(paymentRes.has("serverReferenceCode")){
                        messageContext.setProperty(DataPublisherConstants.OPERATOR_REF, paymentRes.optString("serverReferenceCode"));
                    } else if (paymentRes.has("originalServerReferenceCode")){
                        messageContext.setProperty(DataPublisherConstants.OPERATOR_REF, paymentRes.optString("originalServerReferenceCode"));
                    }
                    isPaymentReq = true;
                }


            } catch (JSONException e) {
                log.error("Error in converting response to json. " + e.getMessage(), e);
            }
        }
        
        String exMsg = ax.getErrmsg()+" "+Arrays.toString(ax.getErrvar()).replace("[", "").replace("]", "");
        log.info("exception id: " + ax.getErrcode());
        log.info("exception message: " + exMsg);
        messageContext.setProperty(DataPublisherConstants.EXCEPTION_ID, ax.getErrcode());
        messageContext.setProperty(DataPublisherConstants.EXCEPTION_MESSAGE, exMsg);

        String buildedExeption = buildErrorResponse(ax); 
        
        //publish data to BAM
        publishNBResponse(messageContext, buildedExeption);

    }
    
    /**
     * Builds the error response.
     *
     * @param ax the ax
     * @return the string
     */
    private String buildErrorResponse(CustomException ax){
        String exceptionString = ""; //Build axiata exeption to normal exception here
        if(ax.getErrcode().length() > 3){
            if(ax.getErrcode().substring(0, 3).equals("POL")){
                //Policy Exception
                PolicyException pol = new PolicyException(ax.getErrcode(), ax.getErrmsg(), Arrays.toString(ax.getErrvar()).replace("[", "").replace("]", ""));
                RequestError error = new RequestError();
                error.setPolicyException(pol);
                exceptionString = getErrorJSONString(error);
            } else if (ax.getErrcode().substring(0, 3).equals("SVC")){
                //Service Exception
                ServiceException svc = new ServiceException(ax.getErrcode(), ax.getErrmsg(), Arrays.toString(ax.getErrvar()).replace("[", "").replace("]", ""));
                RequestError error = new RequestError();
                error.setServiceException(svc);
                exceptionString = getErrorJSONString(error);
            } else {
                exceptionString = ax.toString();
            }
        }
        return exceptionString;
    }
    
    /**
     * Gets the error json string.
     *
     * @param error the error
     * @return the error json string
     */
    private static String getErrorJSONString(RequestError error) {

        String errorObjectString = "";
        try {
            errorObjectString = new JSONObject(error).toString();
            //JSONObject j = new JSONObject(errorObjectString);
            JSONObject innerObj = new JSONObject(error);
            if (innerObj.isNull("serviceException")) {
                //.out.println("serviceException is null");
                innerObj.remove("serviceException");
            } else if (innerObj.isNull("policyException")) {
                //System.out.println("serviceException is null");
                innerObj.remove("policyException");
            }
            //System.out.println(j.toJSONString());
            errorObjectString = innerObj.toString();

        } catch (Exception ex) {
            System.out.println("NortboundPublisher>getErrorJSONString: " + ex);
        }
        return errorObjectString;
    }

    /**
     * Publish nb response.
     *
     * @param mc the mc
     * @param jsonBody the json body
     */
    private void publishNBResponse(MessageContext mc, String jsonBody) {
        if (!enabled) {
            return;
        }

        if (publisher == null) {
            synchronized (this) {
                if (publisher == null) {
                    log.debug("Instantiating Data Publisher");
                    publisher = new SouthboundDataPublisher();
                    publisher.init();
                }
            }
        }
        
        Long currentTime = System.currentTimeMillis();
        Long serviceTime = currentTime - (Long) mc.getProperty(DataPublisherConstants.REQUEST_TIME);

        NorthboundResponsePublisherDTO nbResponseDTO = new NorthboundResponsePublisherDTO();
        nbResponseDTO.setConsumerKey((String) mc.getProperty(APIMgtGatewayConstants.CONSUMER_KEY));
        nbResponseDTO.setUsername((String) mc.getProperty(APIMgtGatewayConstants.USER_ID));
        nbResponseDTO.setTenantDomain(MultitenantUtils.getTenantDomain(nbResponseDTO.getUsername()));
        nbResponseDTO.setContext((String) mc.getProperty(APIMgtGatewayConstants.CONTEXT));
        nbResponseDTO.setApi_version((String) mc.getProperty(APIMgtGatewayConstants.API_VERSION));
        nbResponseDTO.setApi((String) mc.getProperty(APIMgtGatewayConstants.API));
        nbResponseDTO.setVersion((String) mc.getProperty(APIMgtGatewayConstants.VERSION));
        nbResponseDTO.setResourcePath((String) mc.getProperty(APIMgtGatewayConstants.RESOURCE));
        nbResponseDTO.setMethod((String) mc.getProperty(APIMgtGatewayConstants.HTTP_METHOD));
        nbResponseDTO.setResponseTime(currentTime);
        nbResponseDTO.setServiceTime(serviceTime);
        nbResponseDTO.setHostName((String) mc.getProperty(APIMgtGatewayConstants.HOST_NAME));
        nbResponseDTO.setApiPublisher((String) mc.getProperty(APIMgtGatewayConstants.API_PUBLISHER));
        nbResponseDTO.setApplicationName((String) mc.getProperty(APIMgtGatewayConstants.APPLICATION_NAME));
        nbResponseDTO.setApplicationId((String) mc.getProperty(APIMgtGatewayConstants.APPLICATION_ID));

        nbResponseDTO.setRequestId((String) mc.getProperty(DataPublisherConstants.REQUEST_ID));
        nbResponseDTO.setResponseCode((String) mc.getProperty(DataPublisherConstants.RESPONSE_CODE));
        nbResponseDTO.setMsisdn((String) mc.getProperty(DataPublisherConstants.MSISDN));
        nbResponseDTO.setChargeAmount((String) mc.getProperty(DataPublisherConstants.CHARGE_AMOUNT));
        nbResponseDTO.setPurchaseCategoryCode((String) mc.getProperty(DataPublisherConstants.PAY_CATEGORY));
        nbResponseDTO.setOperatorRef((String) mc.getProperty(DataPublisherConstants.OPERATOR_REF));
        nbResponseDTO.setExceptionId((String) mc.getProperty(DataPublisherConstants.EXCEPTION_ID));
        nbResponseDTO.setExceptionMessage((String) mc.getProperty(DataPublisherConstants.EXCEPTION_MESSAGE));
        nbResponseDTO.setJsonBody(jsonBody);

        nbResponseDTO.setOperationType((Integer) mc.getProperty(DataPublisherConstants.OPERATION_TYPE));
        nbResponseDTO.setMerchantId((String) mc.getProperty(DataPublisherConstants.MERCHANT_ID));
        nbResponseDTO.setCategory((String) mc.getProperty(DataPublisherConstants.CATEGORY));
        nbResponseDTO.setSubCategory((String) mc.getProperty(DataPublisherConstants.SUB_CATEGORY));
        
        
        //Hira added to get Subscriber in end User request scienario 
        String userIdToPublish = nbResponseDTO.getUsername();
        if(userIdToPublish != null && userIdToPublish.contains("@")){
            String[] userIdArray = userIdToPublish.split("@");
            userIdToPublish = userIdArray[0];
            nbResponseDTO.setUsername(userIdToPublish);
        }


        publisher.publishEvent(nbResponseDTO);
    }

    /**
     * Publish to cep.
     *
     * @param messageContext the message context
     */
    private void publishToCEP(MessageContext messageContext) {
        if (eventsPublisherClient == null) {
            eventsPublisherClient = new EventsDataPublisherClient();
        }
        eventsPublisherClient.publishEvent(messageContext);
    }
}
