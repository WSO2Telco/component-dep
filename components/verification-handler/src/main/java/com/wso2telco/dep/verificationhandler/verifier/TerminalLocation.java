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
package com.wso2telco.dep.verificationhandler.verifier;


// TODO: Auto-generated Javadoc
/**
 * The Class TerminalLocation.
 */
public class TerminalLocation {

    /** The address. */
    private String address;

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
    
    /** The current location. */
    private CurrentLocation currentLocation;

    /**
     * Gets the current location.
     *
     * @return the current location
     */
    public CurrentLocation getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Sets the current location.
     *
     * @param currentLocation the new current location
     */
    public void setCurrentLocation(CurrentLocation currentLocation) {
        this.currentLocation = currentLocation;
    }
    
    /** The location retrieval status. */
    private String locationRetrievalStatus;

    /**
     * Gets the location retrieval status.
     *
     * @return the location retrieval status
     */
    public String getLocationRetrievalStatus() {
        return locationRetrievalStatus;
    }

    /**
     * Sets the location retrieval status.
     *
     * @param locationRetrievalStatus the new location retrieval status
     */
    public void setLocationRetrievalStatus(String locationRetrievalStatus) {
        this.locationRetrievalStatus = locationRetrievalStatus;
    }
}
