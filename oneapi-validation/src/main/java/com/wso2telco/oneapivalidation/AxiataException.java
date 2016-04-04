/*
 * AxiataException.java
 * Nov 28, 2013  10:52:43 AM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package com.wso2telco.oneapivalidation;

/**
 * <TO-DO>
 * <code>AxiataException</code>
 *
 * @version $Id: AxiataException.java,v 1.00.000
 */
public class AxiataException extends RuntimeException {

    private String errcode;
    private String errmsg;
    private String[] errvar;

    /**
     * Creates a new instance of
     * <code>AxiataException</code> without detail message.
     */
    public AxiataException() {
    }

    /**
     * Constructs an instance of
     * <code>AxiataException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public AxiataException(String errcode, String errmsg, String[] errvar) {
        super(errcode);
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.errvar = errvar;
    }
    
    public AxiataException(String errcode, String[] errvar) {
        super(errcode);
        this.errcode = errcode;
        this.errvar = errvar;
    }

    public String getErrcode() {
        return errcode;
    }

    public String[] getErrvar() {
        return errvar;
    }

    /**
     * @return the errmsg
     */
    public String getErrmsg() {
        return errmsg;
    }

    /**
     * @param errmsg the errmsg to set
     */
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}