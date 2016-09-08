/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.refund.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class ChargingMetaData {

    /** The on behalf of. */
    @SerializedName("onBehalfOf")
    @Expose
    private String onBehalfOf;
    
    /** The purchase category code. */
    @SerializedName("purchaseCategoryCode")
    @Expose
    private String purchaseCategoryCode;
    
    /** The channel. */
    @SerializedName("channel")
    @Expose
    private String channel;
    
    /** The tax amount. */
    @SerializedName("taxAmount")
    @Expose
    private String taxAmount;

    /**
     * Gets the on behalf of.
     *
     * @return     The onBehalfOf
     */
    public String getOnBehalfOf() {
        return onBehalfOf;
    }

    /**
     * Sets the on behalf of.
     *
     * @param onBehalfOf     The onBehalfOf
     */
    public void setOnBehalfOf(String onBehalfOf) {
        this.onBehalfOf = onBehalfOf;
    }

    /**
     * Gets the purchase category code.
     *
     * @return     The purchaseCategoryCode
     */
    public String getPurchaseCategoryCode() {
        return purchaseCategoryCode;
    }

    /**
     * Sets the purchase category code.
     *
     * @param purchaseCategoryCode     The purchaseCategoryCode
     */
    public void setPurchaseCategoryCode(String purchaseCategoryCode) {
        this.purchaseCategoryCode = purchaseCategoryCode;
    }

    /**
     * Gets the channel.
     *
     * @return     The channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets the channel.
     *
     * @param channel     The channel
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * Gets the tax amount.
     *
     * @return     The taxAmount
     */
    public String getTaxAmount() {
        return taxAmount;
    }

    /**
     * Sets the tax amount.
     *
     * @param taxAmount     The taxAmount
     */
    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
