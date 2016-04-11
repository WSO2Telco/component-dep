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
package com.wso2telco.mediator.entity;


// TODO: Auto-generated Javadoc
/**
 * The Class outboundSMSMessageResponse.
 */
public class outboundSMSMessageResponse {

    /** The address. */
    private String[] address;
    
    /** The delivery info list. */
    private DeliveryInfoList deliveryInfoList;
    
    /** The sender address. */
    private String senderAddress;
    
    /** The outbound sms text message. */
    private outboundSMSTextMessage outboundSMSTextMessage;
    
    /** The client correlator. */
    private String clientCorrelator;
    
    /** The receipt request. */
    private ReceiptRequest receiptRequest;
    
    /** The sender name. */
    private String senderName;
    
    /** The resource url. */
    private String resourceURL;

    /**
     * Gets the address.
     *
     * @return the address
     */
    public String[] getAddress() {
        return address;
    }

    /**
     * Sets the address.
     *
     * @param address the new address
     */
    public void setAddress(String[] address) {
        this.address = address;
    }

    /**
     * Gets the delivery info list.
     *
     * @return the delivery info list
     */
    public DeliveryInfoList getDeliveryInfoList() {
        return deliveryInfoList;
    }

    /**
     * Sets the delivery info list.
     *
     * @param deliveryInfoList the new delivery info list
     */
    public void setDeliveryInfoList(DeliveryInfoList deliveryInfoList) {
        this.deliveryInfoList = deliveryInfoList;
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
     * Gets the outbound sms text message.
     *
     * @return the outbound sms text message
     */
    public com.wso2telco.mediator.entity.outboundSMSTextMessage getOutboundSMSTextMessage() {
        return outboundSMSTextMessage;
    }

    /**
     * Sets the outbound sms text message.
     *
     * @param outboundSMSTextMessage the new outbound sms text message
     */
    public void setOutboundSMSTextMessage(com.wso2telco.mediator.entity.outboundSMSTextMessage
                                                  outboundSMSTextMessage) {
        this.outboundSMSTextMessage = outboundSMSTextMessage;
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

    /**
     * Gets the resource url.
     *
     * @return the resource url
     */
    public String getResourceURL() {
        return resourceURL;
    }

    /**
     * Sets the resource url.
     *
     * @param resourceURL the new resource url
     */
    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }
}
