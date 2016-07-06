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
package com.wso2telco.dep.mediator.entity.nb;

import com.wso2telco.dep.mediator.entity.CallbackReference;

 
// TODO: Auto-generated Javadoc
/**
 * The Class DeliveryReceiptSubscription.
 */
public class DeliveryReceiptSubscription {
    
    /** The callback reference. */
    private CallbackReference callbackReference;

     
    /**
     * Gets the callback reference.
     *
     * @return the callback reference
     */
    public CallbackReference getCallbackReference() {
        return callbackReference;
    }

     
    /**
     * Sets the callback reference.
     *
     * @param callbackReference the new callback reference
     */
    public void setCallbackReference(CallbackReference callbackReference) {
        this.callbackReference = callbackReference;
    }
    
    /** The sender addresses. */
    private SenderAddresses[] senderAddresses;

     
    /**
     * Gets the sender addresses.
     *
     * @return the sender addresses
     */
    public SenderAddresses[] getSenderAddresses() {
        return senderAddresses;
    }

     
    /**
     * Sets the sender addresses.
     *
     * @param senderAddresses the new sender addresses
     */
    public void setSenderAddresses(SenderAddresses[] senderAddresses) {
        this.senderAddresses = senderAddresses;
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
    
    /** The client correlator. */
    private String clientCorrelator;

     
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
}
