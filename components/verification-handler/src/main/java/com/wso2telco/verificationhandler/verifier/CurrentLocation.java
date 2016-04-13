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
package com.wso2telco.verificationhandler.verifier;


// TODO: Auto-generated Javadoc
/**
 * The Class CurrentLocation.
 */
public class CurrentLocation {

    /**
     * Instantiates a new current location.
     *
     * @param accuracy the accuracy
     * @param altitude the altitude
     * @param latitude the latitude
     * @param longitude the longitude
     */
    public CurrentLocation(String accuracy, String altitude, String latitude, String longitude) {
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;        
    }
 
    /**
     * Instantiates a new current location.
     */
    public CurrentLocation() {}    
    
    /** The accuracy. */
    private String accuracy;

    /**
     * Gets the accuracy.
     *
     * @return the accuracy
     */
    public String getAccuracy() {
        return accuracy;
    }

    /**
     * Sets the accuracy.
     *
     * @param accuracy the new accuracy
     */
    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }
    
    /** The altitude. */
    private String altitude;

    /**
     * Gets the altitude.
     *
     * @return the altitude
     */
    public String getAltitude() {
        return altitude;
    }

    /**
     * Sets the altitude.
     *
     * @param altitude the new altitude
     */
    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }
    
    /** The latitude. */
    private String latitude;

    /**
     * Gets the latitude.
     *
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude.
     *
     * @param latitude the new latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    
    /** The longitude. */
    private String longitude;

    /**
     * Gets the longitude.
     *
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude.
     *
     * @param longitude the new longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
    /** The timestamp. */
    private String timestamp;

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp.
     *
     * @param timestamp the new timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
