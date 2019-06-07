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


import com.wso2telco.workflow.approval.model.NotificationRequest;
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
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.CarbonConstants;

public class APIPublisherApprovalDBUpdater implements JavaDelegate {

	private static final Log log = LogFactory.getLog(APIPublisherApprovalDBUpdater.class);
    private static final Log auditLog = CarbonConstants.AUDIT_LOG;

    public void execute(DelegateExecution arg0) throws Exception {

        AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();
        int applicationId = arg0.getVariable(Constants.APPLICATION_ID)!=null?Integer.parseInt(arg0.getVariable(Constants.APPLICATION_ID).toString()):0;
        int apiID = arg0.getVariable(Constants.API_ID) != null ? Integer.parseInt(arg0.getVariable(Constants.API_ID).toString()) : 0;
        String serviceUrl = arg0.getVariable(Constants.SERVICE_URL)!=null?arg0.getVariable(Constants.SERVICE_URL).toString():null;
        String apiName = arg0.getVariable(Constants.API_NAME)!=null?arg0.getVariable(Constants.API_NAME).toString():null;
        String apiVersion = arg0.getVariable(Constants.API_VERSION)!=null?arg0.getVariable(Constants.API_VERSION).toString():null;
        String apiProvider = arg0.getVariable(Constants.API_PROVIDER)!=null?arg0.getVariable(Constants.API_PROVIDER).toString():null;
        String completedByUser = arg0.getVariable(Constants.COMPLETE_BY_USER)!=null?arg0.getVariable(Constants.COMPLETE_BY_USER).toString():null;
        String completedOn = arg0.getVariable(Constants.COMPLETED_ON)!=null?arg0.getVariable(Constants.COMPLETED_ON).toString():null;
        String completedByRole = Constants.API_PUBLISHER_APPROVAL;
        String applicationName =  arg0.getVariable(Constants.APPLICATION_NAME)!=null?arg0.getVariable(Constants.APPLICATION_NAME).toString():null;
        String description = arg0.getVariable(Constants.APPLICATION_DESCRIPTION)!=null?arg0.getVariable(Constants.APPLICATION_DESCRIPTION).toString():null;
        String publisherApprovalStatus = arg0.getVariable(Constants.API_PUBLISHER_APPROVAL)!=null?(arg0.getVariable(Constants.API_PUBLISHER_APPROVAL).toString()):null;
        String adminUserName = arg0.getVariable(Constants.ADMIN_USER_NAME) != null ? arg0.getVariable(Constants.ADMIN_USER_NAME).toString() : null;
        String adminPassword = arg0.getVariable(Constants.ADMIN_PASSWORD)!=null?arg0.getVariable(Constants.ADMIN_PASSWORD).toString():null;
        String apiContext = arg0.getVariable(Constants.API_CONTEXT)!=null?arg0.getVariable(Constants.API_CONTEXT).toString():null;
        String subscriber = arg0.getVariable(Constants.SUBSCRIBER)!=null?arg0.getVariable(Constants.SUBSCRIBER).toString():null;
        String operatorName = arg0.getVariable(Constants.OPERATOR) != null ? arg0.getVariable(Constants.OPERATOR).toString() : null;
        String approvalStatus = arg0.getVariable(Constants.API_PUBLISHER_APPROVAL) != null ? arg0.getVariable(Constants.API_PUBLISHER_APPROVAL).toString() : null;
        String workflowRefId = arg0.getVariable(Constants.WORK_FLOW_REF)!=null?arg0.getVariable(Constants.WORK_FLOW_REF).toString():null;
        String adminSelectedTier = arg0.getVariable(Constants.ADMIN_SELECTED_TIER) != null ? arg0.getVariable(Constants.ADMIN_SELECTED_TIER).toString() : null;

        NotificationApi apiNotification = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName,adminPassword))
                .target(NotificationApi.class, serviceUrl);

        WorkflowApprovalAuditApi apiAudit = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName, adminPassword))
                .target(WorkflowApprovalAuditApi.class, serviceUrl);


        SubscriptionWorkflowApi api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName, adminPassword))
                .target(SubscriptionWorkflowApi.class, serviceUrl);

        Subscription subscription = new Subscription();
        subscription.setApiName(apiName);
        subscription.setApplicationID(applicationId);
        subscription.setOperatorName(operatorName);
        subscription.setStatus(approvalStatus);
        subscription.setWorkflowRefId(workflowRefId);
        subscription.setSelectedTier(adminSelectedTier);
        api.subscriptionApprovalHub(subscription);  //tier is set at here
        api.subscriptionApprovalOperator(subscription);

        SubscriptionValidation subscriptionValidation = new SubscriptionValidation();
        subscriptionValidation.setApiID(apiID);
        subscriptionValidation.setApplicationID(applicationId);
        api.subscriptionApprovalValidator(subscriptionValidation);

        SubscriptionApprovalAuditRecord subscriptionApprovalAuditRecord = new SubscriptionApprovalAuditRecord();
        subscriptionApprovalAuditRecord.setApiName(apiName);
        subscriptionApprovalAuditRecord.setApiProvider(apiProvider);
        subscriptionApprovalAuditRecord.setApiVersion(apiVersion);
        subscriptionApprovalAuditRecord.setAppId(applicationId);
        subscriptionApprovalAuditRecord.setCompletedByRole(completedByRole);
        subscriptionApprovalAuditRecord.setCompletedByUser(completedByUser);
        subscriptionApprovalAuditRecord.setCompletedOn(completedOn);
        subscriptionApprovalAuditRecord.setSubApprovalType(Constants.PUBLISHER_APPROVAL);
        subscriptionApprovalAuditRecord.setSubStatus(publisherApprovalStatus);

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setApprovalStatus(publisherApprovalStatus);
        notificationRequest.setApplicationName(applicationName);
        notificationRequest.setApiProvider(apiProvider);
        notificationRequest.setApiName(apiName);
        notificationRequest.setSubscriber(subscriber);
        notificationRequest.setSubscriptionTier(adminSelectedTier);
        notificationRequest.setApplicationDescription(description);
        notificationRequest.setApiVersion(apiVersion);
        notificationRequest.setApiContext(apiContext);

        apiAudit.subscriptionApprovalAudit(subscriptionApprovalAuditRecord);
        apiNotification.subscriptionNotificationSp(notificationRequest);

        //Detailed log entry for the update done
		String logEntry = "Subscription approval api publisher :"
				+ " Completed by - " + completedByUser
				+ ", Application name - " + applicationName
				+ ", API name - " + apiName
				+ ", API version - " + apiVersion
				+ ", Tier - " + adminSelectedTier;
        log.info(logEntry);
        auditLog.info(logEntry);

    }
}
