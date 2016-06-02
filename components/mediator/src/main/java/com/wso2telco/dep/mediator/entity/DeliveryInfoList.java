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
 * The Class DeliveryInfoList.
 */
public class DeliveryInfoList {

    /** The delivery info. */
    private DeliveryInfo[] deliveryInfo;
    
    /** The resource url. */
    private String resourceURL;

    /**
     * Gets the delivery info.
     *
     * @return the delivery info
     */
    public DeliveryInfo[] getDeliveryInfo() {
        return deliveryInfo;
    }

    /**
     * Sets the delivery info.
     *
     * @param deliveryInfo the new delivery info
     */
    public void setDeliveryInfo(DeliveryInfo[] deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

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
}
