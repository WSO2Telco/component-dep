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
public class PaymentAmount {

    /** The charging information. */
    @SerializedName("chargingInformation")
    @Expose
    private ChargingInformation chargingInformation;
    
    /** The charging meta data. */
    @SerializedName("chargingMetaData")
    @Expose
    private ChargingMetaData chargingMetaData;

    /**
     * Gets the charging information.
     *
     * @return     The chargingInformation
     */
    public ChargingInformation getChargingInformation() {
        return chargingInformation;
    }

    /**
     * Sets the charging information.
     *
     * @param chargingInformation     The chargingInformation
     */
    public void setChargingInformation(ChargingInformation chargingInformation) {
        this.chargingInformation = chargingInformation;
    }

    /**
     * Gets the charging meta data.
     *
     * @return     The chargingMetaData
     */
    public ChargingMetaData getChargingMetaData() {
        return chargingMetaData;
    }

    /**
     * Sets the charging meta data.
     *
     * @param chargingMetaData     The chargingMetaData
     */
    public void setChargingMetaData(ChargingMetaData chargingMetaData) {
        this.chargingMetaData = chargingMetaData;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
