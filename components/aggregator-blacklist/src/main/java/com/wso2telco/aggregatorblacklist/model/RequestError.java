/*
 * RequestError.java
 * Apr 2, 2013  11:20:38 AM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */

package com.wso2telco.aggregatorblacklist.model;

/**
 * <TO-DO>
 * <code>RequestError</code>
 *
 * @version $Id: RequestError.java,v 1.00.000
 */

public class RequestError{

    private ErrorReturn errorreturn;

    public RequestError(ErrorReturn errorreturn) {
        this.errorreturn = errorreturn;
    }
        
    public ErrorReturn getErrorreturn() {
        return errorreturn;
    }

    public void setErrorreturn(ErrorReturn errorreturn) {
        this.errorreturn = errorreturn;
    }

    
}