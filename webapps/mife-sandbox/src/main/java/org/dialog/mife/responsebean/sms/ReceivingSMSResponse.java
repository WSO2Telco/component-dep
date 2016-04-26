/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dialog.mife.responsebean.sms;

import java.util.List;

/**
 *
 * @author User
 */
public class ReceivingSMSResponse {
    private String totalNumberOfPendingMessages;
    private String numberOfMessagesInThisBatch;
    private String resourceURL;
    private List<InboundSMSMessage> inboundSMSMessage;

    /**
     * @return the numberOfMessagesInThisBatch
     */
    public String getNumberOfMessagesInThisBatch() {
        return numberOfMessagesInThisBatch;
    }

    /**
     * @param numberOfMessagesInThisBatch the numberOfMessagesInThisBatch to set
     */
    public void setNumberOfMessagesInThisBatch(String numberOfMessagesInThisBatch) {
        this.numberOfMessagesInThisBatch = numberOfMessagesInThisBatch;
    }

    /**
     * @return the resourceURL
     */
    public String getResourceURL() {
        return resourceURL;
    }

    /**
     * @param resourceURL the resourceURL to set
     */
    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }

    /**
     * @return the inboundSMSMessage
     */
    public List<InboundSMSMessage> getInboundSMSMessage() {
        return inboundSMSMessage;
    }

    /**
     * @param inboundSMSMessage the inboundSMSMessage to set
     */
    public void setInboundSMSMessage(List<InboundSMSMessage> inboundSMSMessage) {
        this.inboundSMSMessage = inboundSMSMessage;
    }

    /**
     * @return the totalNumberOfPendingMessages
     */
    public String getTotalNumberOfPendingMessages() {
        return totalNumberOfPendingMessages;
    }

    /**
     * @param totalNumberOfPendingMessages the totalNumberOfPendingMessages to set
     */
    public void setTotalNumberOfPendingMessages(String totalNumberOfPendingMessages) {
        this.totalNumberOfPendingMessages = totalNumberOfPendingMessages;
    }
}
