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



import java.util.ArrayList;
import java.util.Collection;

public class HubAdminDbUpdater implements JavaDelegate {

	private static final Log log = LogFactory.getLog(HubAdminDbUpdater.class);

	public void execute(DelegateExecution arg0) throws Exception {

        final String status =arg0.getVariable("status") != null ? arg0.getVariable("status").toString() :null;
        final String adminUserName = arg0.getVariable(Constants.ADMIN_USER_NAME) != null ? arg0.getVariable(Constants.ADMIN_USER_NAME).toString() : null;
        final String adminPassword = arg0.getVariable(Constants.ADMIN_PASSWORD) != null ? arg0.getVariable(Constants.ADMIN_PASSWORD).toString() : null;
        final String serviceUrl = arg0.getVariable(Constants.SERVICE_URL) != null ? arg0.getVariable(Constants.SERVICE_URL).toString() : null;
        final String apiName = arg0.getVariable(Constants.API_NAME) != null ? arg0.getVariable(Constants.API_NAME).toString() : null;
        final String operatorName = arg0.getVariable(Constants.OPERATOR) != null ? arg0.getVariable(Constants.OPERATOR).toString() : null;
        final int applicationId = arg0.getVariable(Constants.APPLICATION_ID) != null ? Integer.parseInt(arg0.getVariable(Constants.APPLICATION_ID).toString()) : 0;
        final ArrayList operatorRoles= (ArrayList)arg0.getVariable(Constants.OPERATOR_ROLES);
        final String apiVersion = arg0.getVariable(Constants.API_VERSION) != null ? arg0.getVariable(Constants.API_VERSION).toString() : null;
        final String applicationName = arg0.getVariable(Constants.APPLICATION_NAME) != null ? arg0.getVariable(Constants.APPLICATION_NAME).toString() : null;
        final String description = arg0.getVariable(Constants.APPLICATION_DESCRIPTION) != null ? arg0.getVariable(Constants.APPLICATION_DESCRIPTION).toString() : null;
        final String apiContext = arg0.getVariable(Constants.API_CONTEXT) != null ? arg0.getVariable(Constants.API_CONTEXT).toString() : null;
        final String subscriber = arg0.getVariable(Constants.SUBSCRIBER) != null ? arg0.getVariable(Constants.SUBSCRIBER).toString() : null;
        final String apiProvider = arg0.getVariable(Constants.API_PROVIDER)!=null?arg0.getVariable(Constants.API_PROVIDER).toString():null;
        final String selectedTier = arg0.getVariable(Constants.SELECTED_TIER)!=null? status.equalsIgnoreCase(Constants.APPROVE)?arg0.getVariable(Constants.SELECTED_TIER).toString():Constants.REJECTED_TIER:null;
        final String workflowRefId = arg0.getVariable(Constants.WORK_FLOW_REF)!=null?arg0.getVariable(Constants.WORK_FLOW_REF).toString():null;
        arg0.setVariable("hubAdminApproval", status); //hub admin approval status is null. Check jag. remove before deployment.

        AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();
        SubscriptionWorkflowApi api = Feign.builder()
                    .encoder(new JacksonEncoder())
                    .decoder(new JacksonDecoder())
                    .errorDecoder(new WorkflowCallbackErrorDecoder())
                    .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName,adminPassword))
                    .target(SubscriptionWorkflowApi.class, serviceUrl);
        String operators = api.subscriptionGetOperators(apiName, apiVersion, apiProvider, applicationId);
        String[] operatorList = operators.split(",");
        Collection<String> operatorNames = new ArrayList<String>();
        Collection<String> operatorsRoles = new ArrayList<String>();

            for (String operator : operatorList) {
                    operatorNames.add(operator.trim().toLowerCase());
                    operatorsRoles.add(operator.trim()+Constants.ADMIN_ROLE);

                    // TODO: make debug
                    log.info("Operator '" + operator.trim() + "' added to operatorList");
            }

            arg0.setVariable("operatorList", operatorNames);
            arg0.setVariable("operatorRoles", operatorsRoles);

        
        log.info("In HubDataUpdater, Hub admin approval status: " + status);

        Subscription subscription = new Subscription();
        subscription.setApiName(apiName);
        subscription.setApplicationID(applicationId);
        subscription.setOperatorName(operatorName);
        subscription.setWorkflowRefId(workflowRefId);
        api.subscriptionApprovalHub(subscription);

        NotificationApi apiNotification = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName, adminPassword))
                .target(NotificationApi.class, serviceUrl);

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setApiName(apiName);
        notificationRequest.setOperatorsRoles((ArrayList)arg0.getVariable(Constants.OPERATOR_ROLES));
        notificationRequest.setApprovalStatus(status);
        notificationRequest.setApiVersion(apiVersion);
        notificationRequest.setApiContext(apiContext);
        notificationRequest.setApiProvider(apiProvider);
        notificationRequest.setSubscriber(subscriber);
        notificationRequest.setApplicationName(applicationName);
        notificationRequest.setApplicationDescription(description);
        notificationRequest.setSubscriptionTier(selectedTier);

        if (status.equalsIgnoreCase(Constants.APPROVE)) {
            apiNotification.sendPLUGINAdminSubApprovalNotification(notificationRequest);
        } else {
            apiNotification.subscriptionNotificationSp(notificationRequest);
        }


      



	}

}
