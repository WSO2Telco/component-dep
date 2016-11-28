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

class ChargePublisher extends AbstractPublisher {
	{
		LOG = LogFactory.getLog(ChargePublisher.class);
		paymentType  = PaymentType.CHARGED;
		refCode = ClientReference.PAYMENT_RESPONSE_REFCODE;
	}
	
	private PaymentType paymentType ;
	private ClientReference refCode ;
	
	
	@Override
	protected MessageContext modifyMessageContext(MessageContext messageContext, final Long orginalPaymentTime, final GroupDTO groupDTO)
			throws Exception {
		MessageContext returnContext= null;
		if(orginalPaymentTime!=null && orginalPaymentTime==0){
			returnContext =messageContext;
            messageContext.setProperty(DataPublisherConstants.PAYMENT_TYPE, MessageType.PAYMENT_RESPONSE.getMessageDid());
		}
		
		return returnContext;		
	}


	@Override
	protected String getRefvalue(JSONObject paymentRes) throws Exception {
		return paymentRes.getString("serverReferenceCode");
	}
 
	synchronized public void publish(MessageContext messageContext,
			JSONObject paymentRes) throws Exception {
		 //persist messages into Depdb database table

        super.publish(messageContext, paymentRes);

        MessageDTO  messageDTO = new MessageDTO();
        messageDTO.setMsgId(MessageType.PAYMENT_RESPONSE.getMessageDid());
        messageDTO.setMdtrequestId((String) messageContext.getProperty(DataPublisherConstants.REQUEST_ID));
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
