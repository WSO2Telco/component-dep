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
package com.wso2telco.dep.mediator.entity;

 
// TODO: Auto-generated Javadoc
/**
 * The Class inboundSMSMessage.
 */
public class inboundSMSMessage {
    
    /** The date time. */
    private String dateTime = "";
    
    /** The destination address. */
    private String destinationAddress = "";
    
    /** The message id. */
    private String messageId = "";
    
    /** The message. */
    private String message = "";
    
    /** The sender address. */
    private String senderAddress = "";
    
    
    /**
     * Instantiates a new inbound sms message.
     */
    public inboundSMSMessage() {
    }
    
    /**
     * Gets the date time.
     *
     * @return the date time
     */
    public String getdateTime() {
            return dateTime;
    }

    /**
     * Sets the date time.
     *
     * @param dateTime the new date time
     */
    public void setdateTime(String dateTime) {
            this.dateTime = dateTime;
    }
    
    /**
     * Gets the destination address.
     *
     * @return the destination address
     */
    public String getdestinationAddress() {
            return destinationAddress;
    }

    /**
     * Sets the destination address.
     *
     * @param destinationAddress the new destination address
     */
    public void setDestinationAddress(String destinationAddress) {
            this.destinationAddress= destinationAddress;
    }
    
    /**
     * Gets the message id.
     *
     * @return the message id
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the message id.
     *
     * @param messageId the new message id
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
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
            this.senderAddress= senderAddress;
    }
}
