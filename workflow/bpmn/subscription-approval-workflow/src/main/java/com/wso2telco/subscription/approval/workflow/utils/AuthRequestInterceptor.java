package com.wso2telco.subscription.approval.workflow.utils;

import feign.auth.BasicAuthRequestInterceptor;


public class AuthRequestInterceptor {

    public BasicAuthRequestInterceptor getBasicAuthRequestInterceptor () {
        String username;
        String password;

        // check java system properties first
        username = System.getProperty(WorkflowConstants.WORKFLOW_CALLBACK_USERNAME_PROPERTY);
        // if not found, check environment variables
        if (username == null) {
            username = System.getenv(WorkflowConstants.WORKFLOW_CALLBACK_USERNAME_PROPERTY);
        }
        // if still not found, use 'admin'
        if (username == null) {
            username = "admin";
        }

        // check java system properties first
        password = System.getProperty(WorkflowConstants.WORKFLOW_CALLBACK_PASSWORD_PROPERTY);
        // if not found, check environment variables
        if (password == null) {
            password = System.getenv(WorkflowConstants.WORKFLOW_CALLBACK_PASSWORD_PROPERTY);
        }
        // if still not found, use 'admin'
        if (password == null) {
            password = "admin";
        }

        return new BasicAuthRequestInterceptor(username, password);
    }
}
