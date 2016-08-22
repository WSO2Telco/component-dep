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
package com.wso2telco.dep.verificationhandler.model.payment;

 
// TODO: Auto-generated Javadoc
/**
 * The Class ChargingMetaData.
 */
public class ChargingMetaData {
    
    /** The on behalf of. */
    String onBehalfOf;
    
    /** The channel. */
    String channel;
    
    /** The tax amount. */
    Double taxAmount;
    
    /** The purchase category code. */
    String purchaseCategoryCode;
    
    /** The service id. */
    String serviceID;

    /**
     * Gets the tax amount.
     *
     * @return the tax amount
     */
    public Double getTaxAmount() {
        return taxAmount;
    }

    /**
     * Sets the tax amount.
     *
     * @param taxAmount the new tax amount
     */
    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    /**
     * Sets the tax amount.
     *
     * @param taxAmount the new tax amount
     */
    public void setTaxAmount(String taxAmount) {

        try {
            this.taxAmount = Double.parseDouble(taxAmount);
        } catch (Exception e) {
        }
    }

    /**
     * Gets the on behalf of.
     *
     * @return the on behalf of
     */
    public String getOnBehalfOf() {
        return onBehalfOf;
    }

    /**
     * Sets the on behalf of.
     *
     * @param onBehalfOf the new on behalf of
     */
    public void setOnBehalfOf(String onBehalfOf) {
        this.onBehalfOf = onBehalfOf;
    }

    /**
     * Gets the channel.
     *
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets the channel.
     *
     * @param channel the new channel
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * Gets the purchase category code.
     *
     * @return the purchase category code
     */
    public String getPurchaseCategoryCode() {
        return purchaseCategoryCode;
    }

    /**
     * Sets the purchase category code.
     *
     * @param purchaseCategoryCode the new purchase category code
     */
    public void setPurchaseCategoryCode(String purchaseCategoryCode) {
        this.purchaseCategoryCode = purchaseCategoryCode;
    }

    /**
     * Gets the service id.
     *
     * @return the service id
     */
    public String getServiceID() {
        return serviceID;
    }

    /**
     * Sets the service id.
     *
     * @param serviceID the new service id
     */
    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
}
