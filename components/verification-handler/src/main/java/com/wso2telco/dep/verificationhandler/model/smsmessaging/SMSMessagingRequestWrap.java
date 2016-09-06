/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.verificationhandler.model.smsmessaging;

 
// TODO: Auto-generated Javadoc
/**
 * The Class SMSMessagingRequestWrap.
 */
public class SMSMessagingRequestWrap {
    
    /** The outbound sms message request. */
    OutboundSMSMessageRequest outboundSMSMessageRequest;

    /**
     * Instantiates a new SMS messaging request wrap.
     */
    public SMSMessagingRequestWrap() {
    }

    /**
     * Instantiates a new SMS messaging request wrap.
     *
     * @param outboundSMSMessageRequest the outbound sms message request
     */
    public SMSMessagingRequestWrap(OutboundSMSMessageRequest outboundSMSMessageRequest) {
        this.outboundSMSMessageRequest = outboundSMSMessageRequest;
    }

    /**
     * Gets the outbound sms message request.
     *
     * @return the outbound sms message request
     */
    public OutboundSMSMessageRequest getOutboundSMSMessageRequest() {
        return outboundSMSMessageRequest;
    }

    /**
     * Sets the outbound sms message request.
     *
     * @param outboundSMSMessageRequest the new outbound sms message request
     */
    public void setOutboundSMSMessageRequest(OutboundSMSMessageRequest outboundSMSMessageRequest) {
        this.outboundSMSMessageRequest = outboundSMSMessageRequest;
    }
}
