/*
 * IServiceValidate.java
 * Nov 28, 2013  12:52:39 PM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */

package com.wso2telco.oneapivalidation.service;

import com.wso2telco.oneapivalidation.exceptions.AxiataException;

/**
 * <TO-DO> <code>IServiceValidate</code>
 * @version $Id: IServiceValidate.java,v 1.00.000
 */
public interface IServiceValidate {

    //public abstract void validate(Object objectvalues) throws AxiataException;
    public abstract void validate(String json) throws AxiataException;
    public abstract void validateUrl(String pathInfo) throws AxiataException;
    public abstract void validate(String[] params) throws AxiataException;
    
}
