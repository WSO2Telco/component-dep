/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.dep.mediator.publisher;

import com.wso2telco.dep.mediator.dao.PaymentDAO;
import com.wso2telco.dep.mediator.internal.PaymentType;
import com.wso2telco.dep.mediator.internal.messageenum.ClientReference;
import com.wso2telco.dep.mediator.internal.messageenum.MessageType;
import com.wso2telco.dep.mediator.model.MessageDTO;
import com.wso2telco.dep.mediator.unmarshaler.GroupDTO;
import com.wso2telco.dep.mediator.util.DataPublisherConstants;
import com.wso2telco.dep.mediator.util.MessagePersistor;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.json.JSONObject;

import java.util.Calendar;

class RefundPublisher extends AbstractPublisher {
    {
        LOG = LogFactory.getLog(RefundPublisher.class);
        paymentType = PaymentType.REFUND;
        refCode = ClientReference.PAYMENT_RESPONSE_REFCODE;
        dbservice = new PaymentDAO();
    }

    private PaymentType paymentType;
    private ClientReference refCode;

    private PaymentDAO dbservice;


    @Override
    protected MessageContext modifyMessageContext(MessageContext messageContext,
                                                  Long orginalPaymentTime,
                                                  GroupDTO groupDTO) throws Exception {
        MessageContext returnMessageContext = null;

        if (orginalPaymentTime != null && orginalPaymentTime != 0) {


            final Calendar calendar = Calendar.getInstance();
            int refundDayOFYear = calendar.get(Calendar.DAY_OF_YEAR);
            int refundYear = calendar.get(Calendar.YEAR);
            int refundMonth = calendar.get(Calendar.MONTH);
            String charegeStatus = null;

            LOG.debug("Current day of Year - Refund " + refundDayOFYear);
            LOG.debug("Current Year - Refund " + refundYear);

            calendar.setTimeInMillis(orginalPaymentTime);
            int paymentYear = calendar.get(Calendar.YEAR);
            int paymentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
            int paymentMonth = calendar.get(Calendar.MONTH);

            LOG.debug("day of Year - Payment" + paymentDayOfYear);
            LOG.debug("Payment Year - Payment " + paymentYear);

            if (Double.parseDouble(groupDTO.getMonthAmount()) > 0.0) {
                if(refundYear == paymentYear){
                    if (refundMonth == paymentMonth) {
                        returnMessageContext = messageContext;
                        returnMessageContext.setProperty(DataPublisherConstants.PAYMENT_TYPE, MessageType.REFUND_RESPONSE.getMessageDid
                                ());
                    } else {
                        LOG.info("Refund response not publish to CEP because of refund is not matching to current month");
                    }
                } else {
                    LOG.info("Refund response not publish to CEP because of refund is not matching to current Year");
                }


            }

        }

        return returnMessageContext;
    }

    @Override
    protected String getRefvalue(JSONObject paymentRes) throws Exception {
        return paymentRes.getString("originalServerReferenceCode");
    }


    synchronized public void publish(MessageContext messageContext,
                                     JSONObject paymentRes) throws Exception {
        //persist messages into depdb database table

        String refvalue = getRefvalue(paymentRes);
        String refundclientrefcode = dbservice.getRefundDetails(MessageType.REFUND_RESPONSE.getMessageDid(), refvalue);
        String refundclientcorrelator = paymentRes.getString("clientCorrelator");

        if (refundclientrefcode == null || (refundclientrefcode != null && !refundclientcorrelator.equalsIgnoreCase(refundclientrefcode))) {
            super.publish(messageContext, paymentRes);
        } else {
            LOG.debug("WILL NOT publish to database as refund ");
        }

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMsgId(MessageType.REFUND_RESPONSE.getMessageDid());
        messageDTO.setMdtrequestId((String) messageContext.getProperty(DataPublisherConstants.REQUEST_ID));
        messageDTO.setClienString(refundclientcorrelator);
        messageDTO.setRefcode(refCode);
        messageDTO.setRefval(getRefvalue(paymentRes));
        messageDTO.setMessage(paymentRes.toString());
        messageDTO.setReportedTime(System.currentTimeMillis());

        try {
            MessagePersistor.getInstance().publishMessage(messageDTO);
        } catch (Exception e) {
            LOG.debug("error occurred while persisting messages ");
        }


    }


}
