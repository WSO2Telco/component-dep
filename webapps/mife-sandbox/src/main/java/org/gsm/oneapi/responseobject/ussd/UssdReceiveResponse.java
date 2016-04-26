/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gsm.oneapi.responseobject.ussd;

/**
 *
 * @author User
 */
public class UssdReceiveResponse {
    
    private String address;
    private String keyword;
    private String shortCode;
    private String inboundUSSDMessage;
    private String clientCorrelator;
    
    private ResponseRequest responseRequest;
    
    private String ussdAction;

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
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * @return the shortCode
     */
    public String getShortCode() {
        return shortCode;
    }

    /**
     * @param shortCode the shortCode to set
     */
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    /**
     * @return the inboundUSSDMessage
     */
    public String getInboundUSSDMessage() {
        return inboundUSSDMessage;
    }

    /**
     * @param inboundUSSDMessage the inboundUSSDMessage to set
     */
    public void setInboundUSSDMessage(String inboundUSSDMessage) {
        this.inboundUSSDMessage = inboundUSSDMessage;
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
     * @return the responseRequest
     */
    public ResponseRequest getResponseRequest() {
        return responseRequest;
    }

    /**
     * @param responseRequest the responseRequest to set
     */
    public void setResponseRequest(ResponseRequest responseRequest) {
        this.responseRequest = responseRequest;
    }

    /**
     * @return the ussdAction
     */
    public String getUssdAction() {
        return ussdAction;
    }

    /**
     * @param ussdAction the ussdAction to set
     */
    public void setUssdAction(String ussdAction) {
        this.ussdAction = ussdAction;
    }
    
    
    
    public class ResponseRequest{
        
        private String notifyURL;
        private String callbackData;

        /**
         * @return the notifyURL
         */
        public String getNotifyURL() {
            return notifyURL;
        }

        /**
         * @param notifyURL the notifyURL to set
         */
        public void setNotifyURL(String notifyURL) {
            this.notifyURL = notifyURL;
        }

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
        
    }
    
}
