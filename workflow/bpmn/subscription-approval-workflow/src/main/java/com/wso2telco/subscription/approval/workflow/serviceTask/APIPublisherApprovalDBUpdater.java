/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.wso2telco.subscription.approval.workflow.serviceTask;

import com.wso2telco.subscription.approval.workflow.model.SubApprovalStatusSPNotificationRequest;
import com.wso2telco.subscription.approval.workflow.model.Subscription;
import com.wso2telco.subscription.approval.workflow.model.SubscriptionApprovalAuditRecord;
import com.wso2telco.subscription.approval.workflow.model.SubscriptionValidation;
import com.wso2telco.subscription.approval.workflow.rest.client.NotificationApi;
import com.wso2telco.subscription.approval.workflow.rest.client.SubscriptionWorkflowApi;
import com.wso2telco.subscription.approval.workflow.rest.client.WorkflowApprovalAuditApi;
import com.wso2telco.subscription.approval.workflow.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.subscription.approval.workflow.utils.AuthRequestInterceptor;
import com.wso2telco.subscription.approval.workflow.utils.WorkflowConstants;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class APIPublisherApprovalDBUpdater implements JavaDelegate {

    public void execute(DelegateExecution arg0) throws Exception {

        AuthRequestInterceptor authRequestInterceptor=new AuthRequestInterceptor();
        String operatorName = arg0.getVariable("operators").toString();
        int applicationId=Integer.parseInt(arg0.getVariable(WorkflowConstants.APPLICATION_ID).toString());
        String serviceUrl =arg0.getVariable(WorkflowConstants.SERVICE_URL).toString();
        String apiName=arg0.getVariable(WorkflowConstants.API_NAME).toString();
        int apiID=Integer.parseInt(arg0.getVariable(WorkflowConstants.API_ID).toString());
        String deploymentType = arg0.getVariable(WorkflowConstants.DEPLOYMENT_TYPE).toString();
        String apiVersion = arg0.getVariable(WorkflowConstants.API_VERSION).toString();
        String apiProvider= arg0.getVariable(WorkflowConstants.API_PROVIDER).toString();
        String completedByUser= arg0.getVariable(WorkflowConstants.COMPLETE_BY_USER).toString();
        String completedOn= arg0.getVariable(WorkflowConstants.COMPLETED_ON).toString();
        String completedByRole=operatorName+WorkflowConstants.ADMIN_ROLE;
        String applicationName= arg0.getVariable(WorkflowConstants.APPLICATION_NAME).toString();
        String description= arg0.getVariable(WorkflowConstants.DESCRIPTION).toString();
        String selectedTier= arg0.getVariable(WorkflowConstants.SELECTED_TIER).toString();
        String operatorAdminApprovalStatus = arg0.getVariable(WorkflowConstants.OPERATOR_ADMIN_APPROVAL).toString();
        String adminPassword= arg0.getVariable(WorkflowConstants.ADMIN_PASSWORD).toString();
        String apiContext= arg0.getVariable(WorkflowConstants.API_CONTEXT).toString();
        String subscriber=  arg0.getVariable(WorkflowConstants.SUBSCRIBER).toString();

        SubscriptionWorkflowApi api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminPassword))
                .target(SubscriptionWorkflowApi.class, serviceUrl);

        NotificationApi apiNotification = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminPassword))
                .target(NotificationApi.class, serviceUrl);


        Subscription subscription=new Subscription();
        subscription.setApiName(apiName);
        subscription.setApplicationID(applicationId);
        subscription.setStatus(operatorAdminApprovalStatus);
        subscription.setOperatorName(operatorName);

        SubscriptionValidation subscriptionValidation=new SubscriptionValidation();
        subscriptionValidation.setApiID(apiID);
        subscriptionValidation.setApplicationID(applicationId);

        SubscriptionApprovalAuditRecord subscriptionApprovalAuditRecord=new SubscriptionApprovalAuditRecord();
        subscriptionApprovalAuditRecord.setApiName(apiName);
        subscriptionApprovalAuditRecord.setApiProvider(apiProvider);
        subscriptionApprovalAuditRecord.setApiVersion(apiVersion);
        subscriptionApprovalAuditRecord.setAppId(applicationId);
        subscriptionApprovalAuditRecord.setCompletedByRole(completedByRole);
        subscriptionApprovalAuditRecord.setCompletedByUser(completedByUser);
        subscriptionApprovalAuditRecord.setCompletedOn(completedOn);
        subscriptionApprovalAuditRecord.setSubApprovalType("PUBLISHER_APPROVAL");
        subscriptionApprovalAuditRecord.setSubStatus(operatorAdminApprovalStatus);

        SubApprovalStatusSPNotificationRequest subApprovalStatusSPNotificationRequest=new SubApprovalStatusSPNotificationRequest();
        subApprovalStatusSPNotificationRequest.setApprovalStatus(operatorAdminApprovalStatus);
        subApprovalStatusSPNotificationRequest.setApplicationName(applicationName);
        subApprovalStatusSPNotificationRequest.setApiProvider(apiProvider);
        subApprovalStatusSPNotificationRequest.setApiName(apiName);
        subApprovalStatusSPNotificationRequest.setSubscriber(subscriber);
        subApprovalStatusSPNotificationRequest.setSubscriptionTier(selectedTier);
        subApprovalStatusSPNotificationRequest.setApplicationDescription(description);
        subApprovalStatusSPNotificationRequest.setApiVersion(apiVersion);
        subApprovalStatusSPNotificationRequest.setApiContext(apiContext);

        api.subscriptionApprovalOperator(subscription);
        api.subscriptionApprovalValidator(subscriptionValidation);
        apiNotification.subscriptionNotificationSp(subApprovalStatusSPNotificationRequest);

    }
}
