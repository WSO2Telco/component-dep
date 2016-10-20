/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.workflow.approval.model;


/**
 * PLUGINAdminSubApprovalEmailNotificationRequestType bean class
 */


public class PLUGINAdminSubApprovalNotificationRequest {

    /**
     * field for ReceiverRole
     */
    protected String receiverRole;

    /**
     * field for ApiName
     */
    protected String apiName;

    /**
     * field for ApiVersion
     */
    protected String apiVersion;

    /**
     * field for ApiContext
     */
    protected String apiContext;

    /**
     * field for ApiProvider
     */
    protected String apiProvider;

    /**
     * field for Subscriber
     */
    protected String subscriber;

    /**
     * field for ApplicationName
     */
    protected String applicationName;

    /**
     * field for ApplicationDescription
     */
    protected String applicationDescription;

    /**
     * field for SubscriptionTier
     */
    protected String subscriptionTier;

    /**
     * field for apiPublisher
     */
    protected String apiPublisher;


    public String getApiPublisher() {
        return apiPublisher;
    }

    public void setApiPublisher(String apiPublisher) {
        this.apiPublisher = apiPublisher;
    }


    /**
     *
     * @return java.lang.String
     */
    public String getReceiverRole() {
        return receiverRole;
    }


    /**
     *
     * @param param ReceiverRole
     */
    public void setReceiverRole(String param) {
        this.receiverRole = param;
     }

    /**
     *
     * @return java.lang.String
     */
    public String getApiName() {
        return apiName;
    }


    /**
     *
     * @param param ApiName
     */
    public void setApiName(String param) {
        this.apiName = param;
    }


    /**
     *
     * @return java.lang.String
     */
    public String getApiVersion() {
        return apiVersion;
    }


    /**
     *
     * @param param ApiVersion
     */
    public void setApiVersion(String param) {
        this.apiVersion = param;
    }


    /**
     *
     * @return java.lang.String
     */
    public String getApiContext() {
        return apiContext;
    }


    /**
     *
     * @param param ApiContext
     */
    public void setApiContext(String param) {
        this.apiContext = param;
    }

    /**
     *
     * @return java.lang.String
     */
    public String getApiProvider() {
        return apiProvider;
    }


    /**
     *
     * @param param ApiProvider
     */
    public void setApiProvider(String param) {
        this.apiProvider = param;
    }


    /**
     *
     * @return java.lang.String
     */
    public String getSubscriber() {
        return subscriber;
    }


    /**
     *
     * @param param Subscriber
     */
    public void setSubscriber(String param) {
        this.subscriber = param;
    }


    /**
     *
     * @return java.lang.String
     */
    public String getSubscriptionTier() {
        return subscriptionTier;
    }


    /**
     *
     * @param param SubscriptionTier
     */
    public void setSubscriptionTier(String param) {
        this.subscriptionTier = param;
    }

    /**
     *
     * @return java.lang.String
     */
    public String getApplicationName() {
        return applicationName;
    }


    /**
     *
     * @param param ApplicationName
     */
    public void setApplicationName(String param) {
        this.applicationName = param;
    }

    /**
     *
     * @return java.lang.String
     */
    public String getApplicationDescription() {
        return applicationDescription;
    }


    /**
     *
     * @param param ApplicationDescription
     */
    public void setApplicationDescription(String param) {
        this.applicationDescription = param;
    }


}
           
    