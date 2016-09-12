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
package com.wso2telco.dep.mediator.entity.smsmessaging.southbound;

 
// TODO: Auto-generated Javadoc
/**
 * The Class InboundSMSMessageList.
 */
public class InboundSMSMessageList {
    
    /** The inbound sms message. */
    private InboundSMSMessage [] inboundSMSMessage;

     
    /**
     * Gets the inbound sms message.
     *
     * @return the inbound sms message
     */
    public InboundSMSMessage[] getInboundSMSMessage() {
        return inboundSMSMessage;
    }

     
    /**
     * Sets the inbound sms message.
     *
     * @param inboundSMSMessage the new inbound sms message
     */
    public void setInboundSMSMessage(InboundSMSMessage[] inboundSMSMessage) {
        this.inboundSMSMessage = inboundSMSMessage;
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
    
    /** The total number of pending messages. */
    private String totalNumberOfPendingMessages;

     
    /**
     * Gets the total number of pending messages.
     *
     * @return the total number of pending messages
     */
    public String getTotalNumberOfPendingMessages() {
        return totalNumberOfPendingMessages;
    }

     
    /**
     * Sets the total number of pending messages.
     *
     * @param totalNumberOfPendingMessages the new total number of pending messages
     */
    public void setTotalNumberOfPendingMessages(String totalNumberOfPendingMessages) {
        this.totalNumberOfPendingMessages = totalNumberOfPendingMessages;
    }
    
    /** The number of messages in this batch. */
    private String numberOfMessagesInThisBatch;

     
    /**
     * Gets the number of messages in this batch.
     *
     * @return the number of messages in this batch
     */
    public String getNumberOfMessagesInThisBatch() {
        return numberOfMessagesInThisBatch;
    }

     
    /**
     * Sets the number of messages in this batch.
     *
     * @param numberOfMessagesInThisBatch the new number of messages in this batch
     */
    public void setNumberOfMessagesInThisBatch(String numberOfMessagesInThisBatch) {
        this.numberOfMessagesInThisBatch = numberOfMessagesInThisBatch;
    }
}
