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
package com.wso2telco.mediator.entity.nb;

 
// TODO: Auto-generated Javadoc
/**
 * The Class InboundSMSMessages.
 */
public class InboundSMSMessages {
    
    /** The registrations. */
    private Registrations [] registrations;

     
    /**
     * Gets the registrations.
     *
     * @return the registrations
     */
    public Registrations[] getRegistrations() {
        return registrations;
    }
    
     
    /**
     * Sets the registrations.
     *
     * @param registrations the new registrations
     */
    public void setRegistrations(Registrations[] registrations) {
        this.registrations = registrations;
    }

    /** The max batch size. */
    private String maxBatchSize;

     
    /**
     * Gets the max batch size.
     *
     * @return the max batch size
     */
    public String getMaxBatchSize() {
        return maxBatchSize;
    }

     
    /**
     * Sets the max batch size.
     *
     * @param maxBatchSize the new max batch size
     */
    public void setMaxBatchSize(String maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
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
