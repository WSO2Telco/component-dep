package com.wso2telco.workflow.utils;

public class WorkflowServiceException extends Exception {

    public WorkflowServiceException(Exception exception) {
        super(exception);
    }

    public WorkflowServiceException(String exception) {
        super(exception);

    }

}
