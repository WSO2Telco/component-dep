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

package com.wso2telco.workflow.approval.subscription.servicetask;

import com.wso2telco.workflow.approval.model.PLUGINAdminSubApprovalNotificationRequest;
import com.wso2telco.workflow.approval.model.SubApprovalStatusSPNotificationRequest;
import com.wso2telco.workflow.approval.model.Subscription;
import com.wso2telco.workflow.approval.model.SubscriptionApprovalAuditRecord;
import com.wso2telco.workflow.approval.model.SubscriptionValidation;
import com.wso2telco.workflow.approval.subscription.rest.client.NotificationApi;
import com.wso2telco.workflow.approval.subscription.rest.client.SubscriptionWorkflowApi;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowApprovalAuditApi;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import com.wso2telco.workflow.approval.util.Constants;

import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OperatorAdminDbUpdater implements JavaDelegate {

    private static final Log log = LogFactory.getLog(OperatorAdminDbUpdater.class);

    public void execute(DelegateExecution arg0) throws Exception {
        AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();
        String operatorName =arg0.getVariable(Constants.OPERATOR)!=null? arg0.getVariable(Constants.OPERATOR).toString():null;
        int applicationId =arg0.getVariable(Constants.APPLICATION_ID)!=null?Integer.parseInt(arg0.getVariable(Constants.APPLICATION_ID).toString()):0;
        String serviceUrl =arg0.getVariable(Constants.SERVICE_URL)!=null?arg0.getVariable(Constants.SERVICE_URL).toString():null;
        String apiName = arg0.getVariable(Constants.API_NAME)!=null?arg0.getVariable(Constants.API_NAME).toString():null;
        int apiID = arg0.getVariable(Constants.API_ID)!=null?Integer.parseInt(arg0.getVariable(Constants.API_ID).toString()):0;
        String deploymentType = arg0.getVariable(Constants.DEPLOYMENT_TYPE)!=null?arg0.getVariable(Constants.DEPLOYMENT_TYPE).toString():null;
        String apiVersion = arg0.getVariable(Constants.API_VERSION)!=null?arg0.getVariable(Constants.API_VERSION).toString():null;
        String apiProvider =  arg0.getVariable(Constants.API_PROVIDER)!=null?arg0.getVariable(Constants.API_PROVIDER).toString():null;
        String completedByUser = arg0.getVariable(Constants.COMPLETE_BY_USER)!=null?arg0.getVariable(Constants.COMPLETE_BY_USER).toString():null;
        String completedOn = arg0.getVariable(Constants.COMPLETED_ON)!=null?arg0.getVariable(Constants.COMPLETED_ON).toString():null;
        String completedByRole =arg0.getVariable(Constants.OPERATOR)!=null?arg0.getVariable(Constants.OPERATOR).toString() + Constants.ADMIN_ROLE:null;
        String applicationName =arg0.getVariable(Constants.APPLICATION_NAME)!=null ? arg0.getVariable(Constants.APPLICATION_NAME).toString():null;
        String description =arg0.getVariable(Constants.APPLICATION_DESCRIPTION)!=null? arg0.getVariable(Constants.APPLICATION_DESCRIPTION).toString():null;
        String selectedTier =arg0.getVariable(Constants.SELECTED_TIER)!=null ? arg0.getVariable(Constants.SELECTED_TIER).toString():null;
        String adminPassword =arg0.getVariable(Constants.ADMIN_PASSWORD)!=null?arg0.getVariable(Constants.ADMIN_PASSWORD).toString():null;
        String apiContext = arg0.getVariable(Constants.API_CONTEXT)!=null?arg0.getVariable(Constants.API_CONTEXT).toString():null;
        String subscriber = arg0.getVariable(Constants.SUBSCRIBER)!=null? arg0.getVariable(Constants.SUBSCRIBER).toString():null;
        String operatorAdminApprovalStatus = arg0.getVariable(Constants.OPERATOR_ADMIN_APPROVAL)!=null ? arg0.getVariable(Constants.OPERATOR_ADMIN_APPROVAL).toString():null;

        log.info("In OperatorDataUpdater, Operator admin approval status: " + operatorAdminApprovalStatus +
                " Operator: " + operatorName);

        SubscriptionWorkflowApi api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminPassword))
                .target(SubscriptionWorkflowApi.class, serviceUrl);

        WorkflowApprovalAuditApi apiAudit = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminPassword))
                .target(WorkflowApprovalAuditApi.class, serviceUrl);

        NotificationApi apiNotification = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminPassword))
                .target(NotificationApi.class, serviceUrl);

        Subscription subscription = new Subscription();
        subscription.setApiName(apiName);
        subscription.setApplicationID(applicationId);
        subscription.setStatus(operatorAdminApprovalStatus);
        subscription.setOperatorName(operatorName);

        SubscriptionValidation subscriptionValidation = new SubscriptionValidation();
        subscriptionValidation.setApiID(apiID);
        subscriptionValidation.setApplicationID(applicationId);

        SubscriptionApprovalAuditRecord subscriptionApprovalAuditRecord = new SubscriptionApprovalAuditRecord();
        subscriptionApprovalAuditRecord.setApiName(apiName);
        subscriptionApprovalAuditRecord.setApiProvider(apiProvider);
        subscriptionApprovalAuditRecord.setApiVersion(apiVersion);
        subscriptionApprovalAuditRecord.setAppId(applicationId);
        subscriptionApprovalAuditRecord.setCompletedByRole(completedByRole);
        subscriptionApprovalAuditRecord.setCompletedByUser(completedByUser);
        subscriptionApprovalAuditRecord.setCompletedOn(completedOn);
        subscriptionApprovalAuditRecord.setSubApprovalType("OPERATOR_ADMIN_APPROVAL");
        subscriptionApprovalAuditRecord.setSubStatus(operatorAdminApprovalStatus);

        try {
            apiAudit.subscriptionApprovalAudit(subscriptionApprovalAuditRecord);
            if (!deploymentType.equalsIgnoreCase(Constants.HUB)) {
                api.subscriptionApprovalHub(subscription);
            }
            if (!deploymentType.equalsIgnoreCase(Constants.INTERNAL_GATEWAY)) {
                api.subscriptionApprovalOperator(subscription);
                api.subscriptionApprovalValidator(subscriptionValidation);
            }
            //send email notification
            if (deploymentType.equalsIgnoreCase(Constants.INTERNAL_GATEWAY)) {
                if (operatorAdminApprovalStatus.equalsIgnoreCase(Constants.APPROVE)) {
                    PLUGINAdminSubApprovalNotificationRequest pLUGINAdminSubApprovalNotificationRequest = new PLUGINAdminSubApprovalNotificationRequest();
                    pLUGINAdminSubApprovalNotificationRequest.setApiVersion(apiVersion);
                    pLUGINAdminSubApprovalNotificationRequest.setApiContext(apiContext);
                    pLUGINAdminSubApprovalNotificationRequest.setApiName(apiName);
                    pLUGINAdminSubApprovalNotificationRequest.setApiProvider(apiProvider);
                    pLUGINAdminSubApprovalNotificationRequest.setSubscriber(subscriber);
                    pLUGINAdminSubApprovalNotificationRequest.setApiPublisher(apiProvider);
                    pLUGINAdminSubApprovalNotificationRequest.setApplicationName(applicationName);
                    pLUGINAdminSubApprovalNotificationRequest.setApplicationDescription(description);
                    pLUGINAdminSubApprovalNotificationRequest.setSubscriptionTier(selectedTier);
                    apiNotification.subscriptionNotificationApiCreator(pLUGINAdminSubApprovalNotificationRequest);
                } else {
                    SubApprovalStatusSPNotificationRequest subApprovalStatusSPNotificationRequest = new SubApprovalStatusSPNotificationRequest();
                    subApprovalStatusSPNotificationRequest.setApprovalStatus(operatorAdminApprovalStatus);
                    subApprovalStatusSPNotificationRequest.setApplicationName(applicationName);
                    subApprovalStatusSPNotificationRequest.setApiProvider(apiProvider);
                    subApprovalStatusSPNotificationRequest.setSubscriber(subscriber);
                    subApprovalStatusSPNotificationRequest.setApiName(apiName);
                    subApprovalStatusSPNotificationRequest.setSubscriptionTier(selectedTier);
                    subApprovalStatusSPNotificationRequest.setApplicationDescription(description);
                    subApprovalStatusSPNotificationRequest.setApiVersion(apiVersion);
                    subApprovalStatusSPNotificationRequest.setApiContext(apiContext);
                    apiNotification.subscriptionNotificationSp(subApprovalStatusSPNotificationRequest);
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}

