package com.wso2telco.application.creation.approval.workflow.util;

import feign.auth.BasicAuthRequestInterceptor;


public class AuthRequestInterceptor {

    public BasicAuthRequestInterceptor getBasicAuthRequestInterceptor (String adminPassword) {
        String username;
        String password;

        // check java system properties first
        username = System.getProperty(Constants.HUB_WORKFLOW_CALLBACK_USERNAME_PROPERTY);
        // if not found, check environment variables
        if (username == null) {
            username = System.getenv(Constants.HUB_WORKFLOW_CALLBACK_USERNAME_PROPERTY);
        }
        // if still not found, use 'admin' :D
        if (username == null) {
            username = "admin";
        }

        // check java system properties first
        password = System.getProperty(Constants.HUB_WORKFLOW_CALLBACK_PASSWORD_PROPERTY);
        // if not found, check environment variables
        if (password == null) {
            password = System.getenv(Constants.HUB_WORKFLOW_CALLBACK_PASSWORD_PROPERTY);
        }
        // if still not found, use 'admin' :D
        if (password == null) {
            password = adminPassword;
        }

        return new BasicAuthRequestInterceptor(username, password);
    }
}
