package com.wso2telco.dep.operatorservice.exception;

import com.wso2telco.core.dbutils.exception.BusinessException;

/**
 * Generic Exception for Throwing errors from operator-service class.
 */
public class OperatorServiceException extends BusinessException{
    public OperatorServiceException(String cause) {
        super(cause);
    }
}
