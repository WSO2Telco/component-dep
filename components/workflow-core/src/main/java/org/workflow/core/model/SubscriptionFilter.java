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

package org.workflow.core.model;

/**
 * Class Subscription
 */
public class SubscriptionFilter {

    /** The subscription_id. */
    private int subscriptionId;

    /** The tier_id. */
    private String tierId;

    /** The API ID. */
    private int apiId;

    /** The API NAME. */
    private String apiName;

    /** The APPLICATION ID. */
    private int appId;

    /** The APPLICATION NAME. */
    private String appName;

    /** The subscription status. */
    private String subStatus;

    /** The subscription create state. */
    private String subsCreateState;

    /** The created by. */
    private String createdBy;

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getTierId() {
        return tierId;
    }

    public void setTierId(String tierId) {
        this.tierId = tierId;
    }

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public String getSubsCreateState() {
        return subsCreateState;
    }

    public void setSubsCreateState(String subsCreateState) {
        this.subsCreateState = subsCreateState;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionId=" + subscriptionId +
                ", tierId='" + tierId + '\'' +
                ", apiId=" + apiId +
                ", apiName='" + apiName + '\'' +
                ", appId=" + appId +
                ", appName='" + appName + '\'' +
                ", subStatus='" + subStatus + '\'' +
                ", subsCreateState='" + subsCreateState + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
