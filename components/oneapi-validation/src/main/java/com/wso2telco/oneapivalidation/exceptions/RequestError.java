/*
 * RequestError.java
 * Apr 2, 2013  11:20:38 AM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package com.wso2telco.oneapivalidation.exceptions;

/**
 * <TO-DO>
 * <code>RequestError</code>
 *
 * @version $Id: RequestError.java,v 1.00.000
 */
public class RequestError {

    private ServiceException serviceException;
    private PolicyException policyException;
    
    public ServiceException getServiceException() {
        return serviceException;
    }

    public void setServiceException(ServiceException serviceException) {
        this.serviceException = serviceException;
    }
    

    public PolicyException getPolicyException() {
        return policyException;
    }

    public void setPolicyException(PolicyException policyException) {
        this.policyException = policyException;
    }
}