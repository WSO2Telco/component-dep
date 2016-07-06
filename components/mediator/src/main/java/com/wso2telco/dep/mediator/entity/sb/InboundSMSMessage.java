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
package com.wso2telco.dep.mediator.entity.sb;

 
// TODO: Auto-generated Javadoc
/**
 * The Class InboundSMSMessage.
 */
public class InboundSMSMessage {
    
    /** The date time. */
    private String dateTime;

     
    /**
     * Gets the date time.
     *
     * @return the date time
     */
    public String getDateTime() {
        return dateTime;
    }

     
    /**
     * Sets the date time.
     *
     * @param dateTime the new date time
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    
    /** The destination address. */
    private String destinationAddress;

     
    /**
     * Gets the destination address.
     *
     * @return the destination address
     */
    public String getDestinationAddress() {
        return destinationAddress;
    }

     
    /**
     * Sets the destination address.
     *
     * @param destinationAddress the new destination address
     */
    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }
    
    /** The message id. */
    private String messageId;

     
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
    
    /** The message. */
    private String message;

     
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
    
    /** The resource url. */
    private String resourceURL;

     
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
    
    /** The sender address. */
    private String senderAddress;

     
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
}
