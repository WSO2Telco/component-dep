/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gsm.oneapi.responseobject.ussd;

/**
 *
 * @author User
 */
public class UssdSubscriptionResponse {

    private String destinationAddress;
    private String clientCorrelator;
    private String resourceURL;
    private CallbackReference callbackReference;

    /**
     * @return the destinationAddress
     */
    public String getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * @param destinationAddress the destinationAddress to set
     */
    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    /**
     * @return the clientCorrelator
     */
    public String getClientCorrelator() {
        return clientCorrelator;
    }

    /**
     * @param clientCorrelator the clientCorrelator to set
     */
    public void setClientCorrelator(String clientCorrelator) {
        this.clientCorrelator = clientCorrelator;
    }

    /**
     * @return the resourceUrl
     */
    public String getResourceUrl() {
        return resourceURL;
    }

    /**
     * @param resourceUrl the resourceUrl to set
     */
    public void setResourceUrl(String resourceUrl) {
        this.resourceURL = resourceUrl;
    }

    /**
     * @return the callbackReference
     */
    public CallbackReference getCallbackReference() {
        return callbackReference;
    }

    /**
     * @param callbackReference the callbackReference to set
     */
    public void setCallbackReference(CallbackReference callbackReference) {
        this.callbackReference = callbackReference;
    }
    
    

    public static class CallbackReference {
        private String callbackData;
        private String notifyURL;

        /**
         * @return the callbackData
         */
        public String getCallbackData() {
            return callbackData;
        }

        /**
         * @param callbackData the callbackData to set
         */
        public void setCallbackData(String callbackData) {
            this.callbackData = callbackData;
        }

        /**
         * @return the notifyUrl
         */
        public String getNotifyUrl() {
            return notifyURL;
        }

        /**
         * @param notifyUrl the notifyUrl to set
         */
        public void setNotifyUrl(String notifyUrl) {
            this.notifyURL = notifyUrl;
        }
        
        
    }
}
