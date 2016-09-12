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
package com.wso2telco.dep.mediator.entity.smsmessaging;

import java.util.List;

 
// TODO: Auto-generated Javadoc
/**
 * The Class outboundSMSMessageRequest.
 */
public class OutboundSMSMessageRequest {
    

    /** The client correlator. */
    private String clientCorrelator = "";

    /** The address. */
    private List<String> address;
    
    /** The sender address. */
    private String senderAddress = "";
    
    /** The sender name. */
    private String senderName = "";
    
    /** The outbound sms text message. */
    private OutboundSMSTextMessage outboundSMSTextMessage;
    
    /** The receipt request. */
    private ReceiptRequest receiptRequest;
    
    
    
    /**
     * Instantiates a new outbound sms message request.
     */
    public OutboundSMSMessageRequest() {
    }


    /**
     * Gets the client correlator.
     *
     * @return the client correlator
     */
    public String getClientCorrelator() {
            return clientCorrelator;
    }

    /**
     * Sets the client correlator.
     *
     * @param clientCorrelator the new client correlator
     */
    public void setClientCorrelator(String clientCorrelator) {
            this.clientCorrelator = clientCorrelator;
    }
    
    /**
     * Gets the sender address.
     *
     * @return the sender address
     */
    public String getSenderAddress() {
            return senderAddress;
    }

    /**
     * Sets the sender address.
     *
     * @param senderAddress the new sender address
     */
    public void setSenderAddress(String senderAddress) {
            this.senderAddress = senderAddress;
    }
    
    /**
     * Gets the address.
     *
     * @return the address
     */
    public List<String> getAddress() {
            return address;
    }

    /**
     * Sets the address.
     *
     * @param address the new address
     */
    public void setAddress(List<String> address) {
            this.address = address;
    }
    
    /**
     * Gets the outbound text message.
     *
     * @return the outbound text message
     */
    public OutboundSMSTextMessage getOutboundTextMessage() {
            return outboundSMSTextMessage;
    }

    /**
     * Sets the outbound text message.
     *
     * @param outboundSMSTextMessage the new outbound text message
     */
    public void setOutboundTextMessage(OutboundSMSTextMessage outboundSMSTextMessage) {
            this.outboundSMSTextMessage = outboundSMSTextMessage;
    }
    
    /**
     * Gets the receipt request.
     *
     * @return the receipt request
     */
    public ReceiptRequest getReceiptRequest() {
            return receiptRequest;
    }

    /**
     * Sets the receipt request.
     *
     * @param receiptRequest the new receipt request
     */
    public void setReceiptRequest(ReceiptRequest receiptRequest) {
            this.receiptRequest = receiptRequest;
    }
    
    /**
     * Gets the sender name.
     *
     * @return the sender name
     */
    public String getSenderName() {
            return senderName;
    }

    /**
     * Sets the sender name.
     *
     * @param senderName the new sender name
     */
    public void setSenderName(String senderName) {
            this.senderName = senderName;
    }
    
}
