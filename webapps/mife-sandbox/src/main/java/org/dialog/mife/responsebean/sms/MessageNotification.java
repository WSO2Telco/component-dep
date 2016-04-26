/*
 * MessageNotification.java
 * Apr 2, 2013  11:20:38 AM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package org.dialog.mife.responsebean.sms;

/**
 * Inbound SMS Message Notification container
 * <code>MessageNotification</code>
 *
 * @version $Id: MessageNotification.java,v 1.00.000
 */
public class MessageNotification {

    private InboundSMSMessageNotification inboundSMSMessageNotification;

    public InboundSMSMessageNotification getInboundSMSMessageNotification() {
        return inboundSMSMessageNotification;
    }

    public void setInboundSMSMessageNotification(InboundSMSMessageNotification inboundSMSMessageNotification) {
        this.inboundSMSMessageNotification = inboundSMSMessageNotification;
    }
}