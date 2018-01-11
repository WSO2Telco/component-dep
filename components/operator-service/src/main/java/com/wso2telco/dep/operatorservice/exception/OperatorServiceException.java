package com.wso2telco.dep.operatorservice.exception;

public class OperatorServiceException extends Exception {

    public OperatorServiceException(Exception exception) {
        super(exception);
    }

    public OperatorServiceException(String exception) {
        super(exception);
    }
}
