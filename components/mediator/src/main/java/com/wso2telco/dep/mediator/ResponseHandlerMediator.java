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

import com.wso2telco.dep.datapublisher.DataPublisherClient;
import com.wso2telco.dep.datapublisher.DataPublisherConstants;
import com.wso2telco.dep.mediator.entity.cep.ConsumerSecretWrapperDTO;
import com.wso2telco.dep.mediator.internal.PaymentType;
import com.wso2telco.dep.mediator.unmarshaler.GroupDTO;
import com.wso2telco.dep.mediator.unmarshaler.GroupEventUnmarshaller;
import com.wso2telco.dep.publisheventsdata.PublishEventsConstants;
import com.wso2telco.dep.publisheventsdata.publisher.EventsDataPublisherClient;
import org.apache.http.HttpStatus;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ResponseHandlerMediator extends AbstractMediator {

    private DataPublisherClient publisherClient;

    /** The events publisher client. */
    private EventsDataPublisherClient eventsPublisherClient;

    /* (non-Javadoc)
     * @see org.apache.synapse.Mediator#mediate(org.apache.synapse.MessageContext)
     */
    public boolean mediate(MessageContext context) {

        String statusCode = (String) context.getProperty("$axis2:HTTP_SC");
//        publishResponseData(statusCode, requestStr, context);

        return true;
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
        String paymentType=null;


        if (retStr != null && !retStr.isEmpty()) {
            // get serverReferenceCode property for payment API response
            JSONObject paymentRes = null;
            // get exception property for exception response
            JSONObject exception = null;
            JSONObject response = null;
            try {
                //JSONObject response = new JSONObject(retStr);
                response  = new JSONObject(retStr);
                paymentRes = response.optJSONObject("amountTransaction");
                if (paymentRes != null && !response.optJSONObject("amountTransaction").isNull("originalServerReferenceCode")) {
                    paymentType = PaymentType.REFUND.getType();
                } else {
                    paymentType =PaymentType.CHARGED.getType();
                }
                messageContext.setProperty(PublishEventsConstants.PAYMENT_TYPE,paymentType);
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
        //Response.Status.BAD_REQUEST.getStatusCode();
        // publish to CEP only the successful payment requests

        if (isPaymentReq && statusCode >= HttpStatus.SC_OK && statusCode < HttpStatus.SC_MULTIPLE_CHOICES ) {
            log.debug("Publish to CEP");
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
        try {
            AuthenticationContext authContext = APISecurityUtils.getAuthenticationContext(messageContext);
            String consumerKey = "";
            if (authContext != null) {
                consumerKey = authContext.getConsumerKey();

            }
            log.debug("Publish Group data into CEP ");
            GroupEventUnmarshaller unmarshaller = GroupEventUnmarshaller.getInstance();
            ConsumerSecretWrapperDTO consumerSecretWrapperDTO = unmarshaller.getGroupEventDetailDTO(consumerKey);
            List<GroupDTO> groupDTOList = consumerSecretWrapperDTO.getConsumerKeyVsGroup();

            if (groupDTOList.size() > 0) {
                for (GroupDTO groupDTO : groupDTOList) {

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String currentDate =  dateFormat.format(date);
                    messageContext.setProperty(PublishEventsConstants.CURRENT_DATE_TIME,currentDate);
                    messageContext.setProperty(PublishEventsConstants.GROUP_NAME, groupDTO.getGroupName());
                    eventsPublisherClient.publishEvent(messageContext);
                }
            }
        } catch (Exception e) {
            log.error("error occurred when Unmarshaling ");
        }
    }


}
