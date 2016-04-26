/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dialog.mife.server;

/**
 *
 * @author User
 */
public class MsgReciptNotification {
    private String callbackData;
    private InboundSMSMessage inboundSMSMessage;
    
    
    public static class InboundSMSMessage{
        private String dateTime;
        private String destinationAddress;
        private String messageId;
        private String message;
        private String senderAddress;

        /**
         * @return the dateTime
         */
        public String getDateTime() {
            return dateTime;
        }

        /**
         * @param dateTime the dateTime to set
         */
        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

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
         * @return the message
         */
        public String getMessage() {
            return message;
        }

        /**
         * @param message the message to set
         */
        public void setMessage(String message) {
            this.message = message;
        }

        /**
         * @return the senderAddress
         */
        public String getSenderAddress() {
            return senderAddress;
        }

        /**
         * @param senderAddress the senderAddress to set
         */
        public void setSenderAddress(String senderAddress) {
            this.senderAddress = senderAddress;
        }
        
        
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

    /**
     * @return the inboundSMSMessage
     */
    public InboundSMSMessage getInboundSMSMessage() {
        return inboundSMSMessage;
    }

    /**
     * @param inboundSMSMessage the inboundSMSMessage to set
     */
    public void setInboundSMSMessage(InboundSMSMessage inboundSMSMessage) {
        this.inboundSMSMessage = inboundSMSMessage;
    }
    
}
