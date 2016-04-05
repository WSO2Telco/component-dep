/*
 * ServiceException.java
 * Apr 2, 2013  11:20:38 AM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */

package com.wso2telco.oneapivalidation.exceptions;

/**
 * <TO-DO>
 * <code>ServiceException</code>
 *
 * @version $Id: ServiceException.java,v 1.00.000
 */

public class ServiceException {

    public ServiceException(String messageId, String text, String variables) {
        this.messageId = messageId;
        this.text = text;
        this.variables = variables;
    }
    
    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    private String variables;

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }
}