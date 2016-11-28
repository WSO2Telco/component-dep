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

import com.wso2telco.core.dbutils.DbService;
import com.wso2telco.dep.mediator.internal.messageenum.MessageType;
import com.wso2telco.dep.mediator.model.SpendChargeDTO;
import com.wso2telco.dep.mediator.unmarshaler.GroupDTO;
import com.wso2telco.dep.mediator.unmarshaler.GroupEventUnmarshaller;
import com.wso2telco.dep.mediator.unmarshaler.OparatorNotinListException;
import com.wso2telco.dep.mediator.util.DataPublisherConstants;
import com.wso2telco.dep.mediator.util.MessagePersistor;
import org.apache.commons.logging.Log;
import org.apache.synapse.MessageContext;
import org.json.JSONObject;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

abstract class AbstractPublisher implements Publishable {

	protected Log LOG ;
	
	protected DbService dbservice = new DbService();

	protected String getCurrentDate() {
		Date currentDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return dateFormat.format(currentDate);
	}

	@Override
	synchronized public void publish(MessageContext messageContext,
			JSONObject paymentRes) throws Exception {
		
		AuthenticationContext authContext = APISecurityUtils
				.getAuthenticationContext(messageContext);
		String consumerKey = "";
		if (authContext != null) {
			consumerKey = authContext.getConsumerKey();

		}

		GroupEventUnmarshaller unmarshaller = GroupEventUnmarshaller.getInstance();
        try {

            GroupDTO groupDTO=   unmarshaller.getGroupDTO((String)messageContext.getProperty(DataPublisherConstants.OPERATOR_ID),
                                                          consumerKey);
            final Long orginalPaymentTime = dbservice.getPaymentTime(MessageType.PAYMENT_RESPONSE.getMessageDid(), getRefvalue(paymentRes));

                MessageContext modifiedContext = modifyMessageContext(messageContext, orginalPaymentTime, groupDTO);
                //publish the message if not null
                if (modifiedContext != null) {

                    long currentTime = System.currentTimeMillis();
                    SpendChargeDTO spendChargeDTO  = new SpendChargeDTO();
                    spendChargeDTO.setGroupName(groupDTO.getGroupName());
                    spendChargeDTO.setOperatorId((String)modifiedContext.getProperty(DataPublisherConstants.OPERATOR_ID));
                    spendChargeDTO.setConsumerKey(consumerKey);
                    spendChargeDTO.setMsisdn((String)messageContext.getProperty(DataPublisherConstants.MSISDN));
                    spendChargeDTO.setCurrentTime(currentTime);
                    if(orginalPaymentTime>0 && (MessageType.REFUND_RESPONSE.getMessageDid() == (Integer)messageContext.getProperty(DataPublisherConstants.PAYMENT_TYPE))){
                        spendChargeDTO.setOrginalTime(orginalPaymentTime);
                        spendChargeDTO.setAmount(-Double.parseDouble((String)messageContext.getProperty(DataPublisherConstants.CHARGE_AMOUNT)));
                    } else {
                        spendChargeDTO.setAmount(Double.parseDouble((String)messageContext.getProperty(DataPublisherConstants.CHARGE_AMOUNT)));
                        spendChargeDTO.setOrginalTime(currentTime);
                    }
                    spendChargeDTO.setMessageType((Integer)messageContext.getProperty(DataPublisherConstants.PAYMENT_TYPE));

                    MessagePersistor.getInstance().persistSpendDate(spendChargeDTO);
                    //eventsPublisherClient.publishEvent(messageContext);
                }

        } catch (OparatorNotinListException e){
            LOG.debug("NOt publish to database");
        }



        /* GroupEventUnmarshaller groupEventUnmarshaller = GroupEventUnmarshaller.getInstance();

        try {
            GroupDTO groupDTO= groupEventUnmarshaller.getGroupDTO(operator,consumerKey);*/

		//nothing to publish


	}

	abstract protected MessageContext modifyMessageContext(MessageContext messageContext,
                                                           final Long orginalPaymentTime, GroupDTO groupDTO) throws Exception;
	
	abstract protected String getRefvalue(JSONObject paymentRes) throws Exception;
	/*//TODO :this need to move into response handling class
	abstract protected void persistsMessage(MessageContext messageContext,
											String refvalue) throws Exception;*/

}
