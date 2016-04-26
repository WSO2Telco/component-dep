/*
 * InboundSMSMessageNotification.java
 * Apr 2, 2013  11:20:38 AM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package org.dialog.mife.responsebean.sms;


/**
 * InboundSMSMessageNotificatin container
 * <code>InboundSMSMessageNotification</code>
 *
 * @version $Id: InboundSMSMessageNotification.java,v 1.00.000
 */
public class InboundSMSMessageNotification {

    private String callbackData;

    public String getCallbackData() {
        return callbackData;
    }

    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }
    private InboundSMSMessage inboundSMSMessage;

    public InboundSMSMessage getInboundSMSMessage() {
        return inboundSMSMessage;
    }

    public void setInboundSMSMessage(InboundSMSMessage inboundSMSMessage) {
        this.inboundSMSMessage = inboundSMSMessage;
    }
}