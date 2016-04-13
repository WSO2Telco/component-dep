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
package com.wso2telco.verificationhandler.model.USSD;

 
// TODO: Auto-generated Javadoc
/**
 * The Class OutboundUSSDMessageRequest.
 */
public class OutboundUSSDMessageRequest {

    /** The address. */
    String address;
    
    /** The short code. */
    String shortCode;
    
    /** The keyword. */
    String keyword;
    
    /** The outbound ussd message. */
    String outboundUSSDMessage;
    
    /** The client correlator. */
    String clientCorrelator;
    
    /** The ussd action. */
    String ussdAction;
    
    /** The response request. */
    ResponseRequest responseRequest;

    /**
     * Instantiates a new outbound ussd message request.
     */
    public OutboundUSSDMessageRequest() {
    }

    /**
     * Instantiates a new outbound ussd message request.
     *
     * @param outboundUSSDMessage the outbound ussd message
     * @param address the address
     * @param shortCode the short code
     * @param keyword the keyword
     */
    public OutboundUSSDMessageRequest(String outboundUSSDMessage, String address, String shortCode, String keyword) {
        this.outboundUSSDMessage = outboundUSSDMessage;
        this.address = address;
        this.shortCode = shortCode;
        this.keyword = keyword;
        responseRequest  = new ResponseRequest("","");
    }

    /**
     * Gets the address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address.
     *
     * @param address the new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the short code.
     *
     * @return the short code
     */
    public String getShortCode() {
        return shortCode;
    }

    /**
     * Sets the short code.
     *
     * @param shortCode the new short code
     */
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    /**
     * Gets the keyword.
     *
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Sets the keyword.
     *
     * @param keyword the new keyword
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Gets the outbound ussd message.
     *
     * @return the outbound ussd message
     */
    public String getOutboundUSSDMessage() {
        return outboundUSSDMessage;
    }

    /**
     * Sets the outbound ussd message.
     *
     * @param outboundUSSDMessage the new outbound ussd message
     */
    public void setOutboundUSSDMessage(String outboundUSSDMessage) {
        this.outboundUSSDMessage = outboundUSSDMessage;
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
     * Gets the ussd action.
     *
     * @return the ussd action
     */
    public String getUssdAction() {
        return ussdAction;
    }

    /**
     * Sets the ussd action.
     *
     * @param ussdAction the new ussd action
     */
    public void setUssdAction(String ussdAction) {
        this.ussdAction = ussdAction;
    }

    /**
     * Gets the response request.
     *
     * @return the response request
     */
    public ResponseRequest getResponseRequest() {
        return responseRequest;
    }

    /**
     * Sets the response request.
     *
     * @param responseRequest the new response request
     */
    public void setResponseRequest(ResponseRequest responseRequest) {
        this.responseRequest = responseRequest;
    }
}
