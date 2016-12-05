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
import com.wso2telco.workflow.approval.subscription.rest.client.NotificationApi;
import com.wso2telco.workflow.approval.subscription.rest.client.SubscriptionWorkflowApi;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.workflow.approval.util.Constants;

public class HubAdminDbUpdater implements JavaDelegate {

	private static final Log log = LogFactory.getLog(HubAdminDbUpdater.class);

	public void execute(DelegateExecution arg0) throws Exception {

        String status =arg0.getVariable(Constants.HUB_ADMIN_APPROVAL) != null ? arg0.getVariable(Constants.HUB_ADMIN_APPROVAL).toString() :null;
		log.info("In HubDataUpdater, Hub admin approval status: " + status);
        final String adminUserName = arg0.getVariable(Constants.ADMIN_USER_NAME) != null ? arg0.getVariable(Constants.ADMIN_USER_NAME).toString() : null;
        final String adminPassword = arg0.getVariable(Constants.ADMIN_PASSWORD) != null ? arg0.getVariable(Constants.ADMIN_PASSWORD).toString() : null;
        final String serviceUrl = arg0.getVariable(Constants.SERVICE_URL) != null ? arg0.getVariable(Constants.SERVICE_URL).toString() : null;
        final String apiName = arg0.getVariable(Constants.API_NAME) != null ? arg0.getVariable(Constants.API_NAME).toString() : null;
        String operatorName = arg0.getVariable(Constants.OPERATOR) != null ? arg0.getVariable(Constants.OPERATOR).toString() : null;
        int applicationId = arg0.getVariable(Constants.APPLICATION_ID) != null ? Integer.parseInt(arg0.getVariable(Constants.APPLICATION_ID).toString()) : 0;

        AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();

        SubscriptionWorkflowApi api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName,adminPassword))
                .target(SubscriptionWorkflowApi.class, serviceUrl);

        Subscription subscription = new Subscription();
        subscription.setApiName(apiName);
        subscription.setApplicationID(applicationId);
        subscription.setOperatorName(operatorName);
        api.subscriptionApprovalHub(subscription);

        NotificationApi apiNotification = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName, adminPassword))
                .target(NotificationApi.class, serviceUrl);

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setApiName(apiName);
      /*notificationRequest.setApplicationName(applicationName);
        notificationRequest.setApplicationTier(selectedTier);
        notificationRequest.setApplicationDescription(applicationDescription);
        notificationRequest.setUserName(userName);
        notificationRequest.setReceiverRole(completedByRole);*/
        apiNotification.sendHUBAdminSubrovalNotification(notificationRequest);



	}

}
