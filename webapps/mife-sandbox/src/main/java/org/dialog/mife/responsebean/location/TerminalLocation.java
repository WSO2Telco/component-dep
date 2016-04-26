/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dialog.mife.responsebean.location;

/**
 *
 * @author Dialog
 */
public class TerminalLocation {
    
    private String address=null;
    private CurrentLocation currentLocation;
    private String locationRetrievalStatus=null;
    private RequestError errorInformation;

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the currentLocation
     */
    public CurrentLocation getCurrentLocation() {
        return currentLocation;
    }

    /**
     * @param currentLocation the currentLocation to set
     */
    public void setCurrentLocation(CurrentLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * @return the locationRetrievalStatus
     */
    public String getLocationRetrievalStatus() {
        return locationRetrievalStatus;
    }

    /**
     * @param locationRetrievalStatus the locationRetrievalStatus to set
     */
    public void setLocationRetrievalStatus(String locationRetrievalStatus) {
        this.locationRetrievalStatus = locationRetrievalStatus;
    }

    /**
     * @return the errorInformation
     */
    public RequestError getErrorInformation() {
        return errorInformation;
    }

    /**
     * @param errorInformation the errorInformation to set
     */
    public void setErrorInformation(RequestError errorInformation) {
        this.errorInformation = errorInformation;
    }
    
    public static class CurrentLocation {
        
        private Double accuracy=0.0;
        private Double altitude=0.0;
        private Double latitude=0.0;
        private Double longitude=0.0;
        private String timestamp;

        /**
         * @return the accuracy
         */
        public Double getAccuracy() {
            return accuracy;
        }

        /**
         * @param accuracy the accuracy to set
         */
        public void setAccuracy(Double accuracy) {
            this.accuracy = accuracy;
        }

        /**
         * @return the altitude
         */
        public Double getAltitude() {
            return altitude;
        }

        /**
         * @param altitude the altitude to set
         */
        public void setAltitude(Double altitude) {
            this.altitude = altitude;
        }

        /**
         * @return the latitude
         */
        public Double getLatitude() {
            return latitude;
        }

        /**
         * @param latitude the latitude to set
         */
        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        /**
         * @return the longitude
         */
        public Double getLongitude() {
            return longitude;
        }

        /**
         * @param longitude the longitude to set
         */
        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        /**
         * @return the timestamp
         */
        public String getTimestamp() {
            return timestamp;
        }

        /**
         * @param timestamp the timestamp to set
         */
        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
    
    public static class RequestError{
        
        private String messageId;
        private String text;
        private String variables;

        /**
         * @return the messageId
         */
        public String getMessageId() {
            return messageId;
        }

        /**
         * @param messageId the messageId to set
         */
        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        /**
         * @return the text
         */
        public String getText() {
            return text;
        }

        /**
         * @param text the text to set
         */
        public void setText(String text) {
            this.text = text;
        }

        /**
         * @return the variables
         */
        public String getVariables() {
            return variables;
        }

        /**
         * @param variables the variables to set
         */
        public void setVariables(String variables) {
            this.variables = variables;
        }
        
        
    }
}
