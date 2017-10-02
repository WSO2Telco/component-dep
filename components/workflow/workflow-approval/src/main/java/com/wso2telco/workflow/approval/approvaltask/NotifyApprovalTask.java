/**
 * Copyright (c) 2017, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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

package com.wso2telco.workflow.approval.approvaltask;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import org.activiti.engine.delegate.DelegateExecution;

import com.wso2telco.workflow.approval.exception.ApprovalWorkflowException;
import com.wso2telco.workflow.approval.model.NotificationRequest;
import com.wso2telco.workflow.approval.subscription.rest.client.NotificationApi;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import com.wso2telco.workflow.approval.util.Constants;

public class NotifyApprovalTask implements WorkflowApprovalTask {

	private static final Log log = LogFactory.getLog(NotifyApprovalTask.class);

	@Override
	public void executeHubAdminApplicationApproval(DelegateExecution arg0) throws ApprovalWorkflowException {
		try {
			String status = arg0.getVariable(Constants.HUB_ADMIN_APPROVAL) != null ? arg0.getVariable(Constants.HUB_ADMIN_APPROVAL).toString() :null;
	        String serviceUrl = arg0.getVariable(Constants.SERVICE_URL) != null ? arg0.getVariable(Constants.SERVICE_URL).toString() : null;
	        String applicationName = arg0.getVariable(Constants.APPLICATION_NAME) != null ? arg0.getVariable(Constants.APPLICATION_NAME).toString() : null;
	        String userName = arg0.getVariable(Constants.USER_NAME) != null ? arg0.getVariable(Constants.USER_NAME).toString() : null;
	        String adminUserName = arg0.getVariable(Constants.ADMIN_USER_NAME) != null ? arg0.getVariable(Constants.ADMIN_USER_NAME).toString() : null;
	        String adminPassword = arg0.getVariable(Constants.ADMIN_PASSWORD) != null ? arg0.getVariable(Constants.ADMIN_PASSWORD).toString() : null;
	        String apiVersion = arg0.getVariable(Constants.API_VERSION)!=null?arg0.getVariable(Constants.API_VERSION).toString():null;
	        String apiProvider = arg0.getVariable(Constants.API_PROVIDER)!=null?arg0.getVariable(Constants.API_PROVIDER).toString():null;
	        String apiContext = arg0.getVariable(Constants.API_CONTEXT)!=null?arg0.getVariable(Constants.API_CONTEXT).toString():null;
	        String description = arg0.getVariable(Constants.DESCRIPTION)!=null?arg0.getVariable(Constants.DESCRIPTION).toString():null;
	        String selectedTier = arg0.getVariable(Constants.SELECTED_TIER) != null ? arg0.getVariable(Constants.SELECTED_TIER).toString() : null;
	        ArrayList operatorRoles = (ArrayList)arg0.getVariable("operatorRoles");

	        AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();

	        NotificationApi apiNotification = Feign.builder()
	                .encoder(new JacksonEncoder())
	                .decoder(new JacksonDecoder())
	                .errorDecoder(new WorkflowCallbackErrorDecoder())
	                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName, adminPassword))
	                .target(NotificationApi.class,serviceUrl);

	        NotificationRequest notificationRequest = new NotificationRequest();
	        notificationRequest.setOperatorsRoles(operatorRoles);
	        notificationRequest.setApplicationName(applicationName);
	        notificationRequest.setApplicationTier(selectedTier);
	        notificationRequest.setApplicationDescription(description);
	        notificationRequest.setUserName(userName);
	        notificationRequest.setApprovalStatus(status);

	        if (status.equalsIgnoreCase(Constants.APPROVE)) {
	            apiNotification.sendPLUGINAdminAppApprovalNotification(notificationRequest);
	        } else {
	            apiNotification.sendAppApprovalStatusSPNotification(notificationRequest);
	        }
		} catch (Exception e) {
			String errorMessage = "Error in NotifyApprovalTask.executeHubAdminApplicationApproval";
			log.error(errorMessage, e);
			throw new ApprovalWorkflowException(errorMessage, e);
		}
	}

	@Override
	public void executeHubAdminSubscriptionApproval(DelegateExecution arg0) throws ApprovalWorkflowException {
		try {
			final String status = arg0.getVariable("status") != null ? arg0.getVariable("status").toString() :null;
	        final String adminUserName = arg0.getVariable(Constants.ADMIN_USER_NAME) != null ? arg0.getVariable(Constants.ADMIN_USER_NAME).toString() : null;
	        final String adminPassword = arg0.getVariable(Constants.ADMIN_PASSWORD) != null ? arg0.getVariable(Constants.ADMIN_PASSWORD).toString() : null;
	        final String serviceUrl = arg0.getVariable(Constants.SERVICE_URL) != null ? arg0.getVariable(Constants.SERVICE_URL).toString() : null;
	        final String apiName = arg0.getVariable(Constants.API_NAME) != null ? arg0.getVariable(Constants.API_NAME).toString() : null;
	        final String apiVersion = arg0.getVariable(Constants.API_VERSION) != null ? arg0.getVariable(Constants.API_VERSION).toString() : null;
	        final String applicationName = arg0.getVariable(Constants.APPLICATION_NAME) != null ? arg0.getVariable(Constants.APPLICATION_NAME).toString() : null;
	        final String description = arg0.getVariable(Constants.APPLICATION_DESCRIPTION) != null ? arg0.getVariable(Constants.APPLICATION_DESCRIPTION).toString() : null;
	        final String apiContext = arg0.getVariable(Constants.API_CONTEXT) != null ? arg0.getVariable(Constants.API_CONTEXT).toString() : null;
	        final String subscriber = arg0.getVariable(Constants.SUBSCRIBER) != null ? arg0.getVariable(Constants.SUBSCRIBER).toString() : null;
	        final String apiProvider = arg0.getVariable(Constants.API_PROVIDER)!=null?arg0.getVariable(Constants.API_PROVIDER).toString():null;
	        final String selectedTier = arg0.getVariable(Constants.SELECTED_TIER)!=null? status.equalsIgnoreCase(Constants.APPROVE)?arg0.getVariable(Constants.SELECTED_TIER).toString():Constants.REJECTED_TIER:null;
			
	        AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();

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
		} catch (Exception e) {
			String errorMessage = "Error in NotifyApprovalTask.executeHubAdminSubscriptionApproval";
			log.error(errorMessage, e);
			throw new ApprovalWorkflowException(errorMessage, e);
		}
	}

	@Override
	public void executeOperatorAdminApplicationApproval(DelegateExecution arg0) throws ApprovalWorkflowException {
		try {
			String deploymentType = arg0.getVariable(Constants.DEPLOYMENT_TYPE) != null ? arg0.getVariable(Constants.DEPLOYMENT_TYPE).toString() : null;
			if (deploymentType.equals(Constants.HUB)) {
		        String applicationName = arg0.getVariable(Constants.APPLICATION_NAME) != null ? arg0.getVariable(Constants.APPLICATION_NAME).toString() : null;
		        String adminSelectedTier = arg0.getVariable(Constants.ADMIN_SELECTED_TIER) != null ? arg0.getVariable(Constants.ADMIN_SELECTED_TIER).toString() : null;
		        String applicationDescription = arg0.getVariable(Constants.DESCRIPTION) != null ? arg0.getVariable(Constants.DESCRIPTION).toString() : null;
		        String userName = arg0.getVariable(Constants.USER_NAME) != null ? arg0.getVariable(Constants.USER_NAME).toString() : null;
		        String operatorName = arg0.getVariable(Constants.OPERATOR) != null ? arg0.getVariable(Constants.OPERATOR).toString() : null;
		        String operatorAdminApprovalStatus = arg0.getVariable(Constants.OPERATOR_ADMIN_APPROVAL) != null ? arg0.getVariable(Constants.OPERATOR_ADMIN_APPROVAL).toString() : null;
		        String adminUserName = arg0.getVariable(Constants.ADMIN_USER_NAME) != null ? arg0.getVariable(Constants.ADMIN_USER_NAME).toString() : null;
		        String adminPassword = arg0.getVariable(Constants.ADMIN_PASSWORD) != null ? arg0.getVariable(Constants.ADMIN_PASSWORD).toString() : null;
		        String serviceUrl = arg0.getVariable(Constants.SERVICE_URL) != null ? arg0.getVariable(Constants.SERVICE_URL).toString() : null;
				
		        AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();
		        
		        NotificationApi apiNotification = Feign.builder()
		                .encoder(new JacksonEncoder())
		                .decoder(new JacksonDecoder())
		                .errorDecoder(new WorkflowCallbackErrorDecoder())
		                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName, adminPassword))
		                .target(NotificationApi.class, serviceUrl);

		        NotificationRequest notificationRequest = new NotificationRequest();
		        notificationRequest.setApplicationName(applicationName);
		        notificationRequest.setApplicationTier(adminSelectedTier);
		        notificationRequest.setApplicationDescription(applicationDescription);
		        notificationRequest.setUserName(userName);
		        notificationRequest.setApprovalStatus(operatorName.toUpperCase() + "-" + operatorAdminApprovalStatus);

		        apiNotification.sendAppApprovalStatusSPNotification(notificationRequest);
	        } else {
	        	//No notification should be sent
	        }
		} catch (Exception e) {
			String errorMessage = "Error in NotifyApprovalTask.executeOperatorAdminApplicationApproval";
			log.error(errorMessage, e);
			throw new ApprovalWorkflowException(errorMessage, e);
		}
	}

	@Override
	public void executeOperatorAdminSubscriptionApproval(DelegateExecution arg0) throws ApprovalWorkflowException {
		try {
			String deploymentType = arg0.getVariable(Constants.DEPLOYMENT_TYPE) != null ? arg0.getVariable(Constants.DEPLOYMENT_TYPE).toString() : null;
			
	        final String adminUserName = arg0.getVariable(Constants.ADMIN_USER_NAME) != null ? arg0.getVariable(Constants.ADMIN_USER_NAME).toString() : null;
	        final String adminPassword = arg0.getVariable(Constants.ADMIN_PASSWORD) != null ? arg0.getVariable(Constants.ADMIN_PASSWORD).toString() : null;
	        final String serviceUrl = arg0.getVariable(Constants.SERVICE_URL) != null ? arg0.getVariable(Constants.SERVICE_URL).toString() : null;
	        
	        final String apiName = arg0.getVariable(Constants.API_NAME) != null ? arg0.getVariable(Constants.API_NAME).toString() : null;
	        String operatorName = arg0.getVariable(Constants.OPERATOR) != null ? arg0.getVariable(Constants.OPERATOR).toString() : null;
	        int applicationId = arg0.getVariable(Constants.APPLICATION_ID) != null ? Integer.parseInt(arg0.getVariable(Constants.APPLICATION_ID).toString()) : 0;
	        int apiID = arg0.getVariable(Constants.API_ID) != null ? Integer.parseInt(arg0.getVariable(Constants.API_ID).toString()) : 0;
	        String apiVersion = arg0.getVariable(Constants.API_VERSION) != null ? arg0.getVariable(Constants.API_VERSION).toString() : null;
	        String apiProvider = arg0.getVariable(Constants.API_PROVIDER) != null ? arg0.getVariable(Constants.API_PROVIDER).toString() : null;
	        String completedByUser = arg0.getVariable(Constants.COMPLETE_BY_USER) != null ? arg0.getVariable(Constants.COMPLETE_BY_USER).toString() : null;
	        String completedOn = arg0.getVariable(Constants.COMPLETED_ON) != null ? arg0.getVariable(Constants.COMPLETED_ON).toString() : null;
	        String completedByRole = arg0.getVariable(Constants.OPERATOR) != null ? arg0.getVariable(Constants.OPERATOR).toString() + Constants.ADMIN_ROLE : null;
	        String applicationName = arg0.getVariable(Constants.APPLICATION_NAME) != null ? arg0.getVariable(Constants.APPLICATION_NAME).toString() : null;
	        String description = arg0.getVariable(Constants.APPLICATION_DESCRIPTION) != null ? arg0.getVariable(Constants.APPLICATION_DESCRIPTION).toString() : null;
	        String selectedTier = arg0.getVariable(Constants.SELECTED_TIER) != null ? arg0.getVariable(Constants.SELECTED_TIER).toString() : null;
	        String apiContext = arg0.getVariable(Constants.API_CONTEXT) != null ? arg0.getVariable(Constants.API_CONTEXT).toString() : null;
	        String subscriber = arg0.getVariable(Constants.SUBSCRIBER) != null ? arg0.getVariable(Constants.SUBSCRIBER).toString() : null;
	        String operatorAdminApprovalStatus = arg0.getVariable(Constants.OPERATOR_ADMIN_APPROVAL) != null ? arg0.getVariable(Constants.OPERATOR_ADMIN_APPROVAL).toString() : null;
	        String apiProviderRole = arg0.getVariable(Constants.API_PROVIDER_ROLE) != null ? arg0.getVariable(Constants.API_PROVIDER_ROLE).toString() : null;
	        String adminSelectedTier = arg0.getVariable(Constants.ADMIN_SELECTED_TIER) != null ? arg0.getVariable(Constants.ADMIN_SELECTED_TIER).toString() : null;
	        String selectedRate = arg0.getVariable(Constants.SELECTED_RATE) != null ? arg0.getVariable(Constants.SELECTED_RATE).toString() : null;

	        if (deploymentType.startsWith(Constants.INTERNAL_GATEWAY)) {
	            AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();

	            NotificationApi apiNotification = Feign.builder()
	                    .encoder(new JacksonEncoder())
	                    .decoder(new JacksonDecoder())
	                    .errorDecoder(new WorkflowCallbackErrorDecoder())
	                    .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName, adminPassword))
	                    .target(NotificationApi.class, serviceUrl);

	            NotificationRequest notificationRequest = new NotificationRequest();
	            notificationRequest.setApiVersion(apiVersion);
	            notificationRequest.setApiContext(apiContext);
	            notificationRequest.setApiName(apiName);
	            notificationRequest.setReceiverRole(apiProviderRole);
	            notificationRequest.setApiProvider(apiProvider);
	            notificationRequest.setSubscriber(subscriber);
	            //notificationRequest.setApiPublisher(args.getApiPublisher()); //Check args.setApiPublisher();
	            notificationRequest.setApplicationName(applicationName);
	            notificationRequest.setApplicationDescription(description);
	            notificationRequest.setSubscriptionTier(selectedTier);

	            if (operatorAdminApprovalStatus.equalsIgnoreCase(Constants.APPROVE)) {
	              apiNotification.subscriptionNotificationAdminService(notificationRequest);
	            } else {
	              apiNotification.subscriptionNotificationSp(notificationRequest);
	            }
	        } else if (deploymentType.equals(Constants.EXTERNAL_GATEWAY)) {
	            //No notification should be sent
	        } else if (deploymentType.equals(Constants.HUB)) {           
	            AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();

	            NotificationApi apiNotification = Feign.builder()
	                    .encoder(new JacksonEncoder())
	                    .decoder(new JacksonDecoder())
	                    .errorDecoder(new WorkflowCallbackErrorDecoder())
	                    .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName, adminPassword))
	                    .target(NotificationApi.class, serviceUrl);

	            NotificationRequest notificationRequest = new NotificationRequest();
	            notificationRequest.setApiContext(apiContext);
	            notificationRequest.setApiName(apiName);
	            notificationRequest.setSubscriber(subscriber);
	            notificationRequest.setSubscriptionTier(adminSelectedTier);
	            notificationRequest.setReceiverRole(completedByRole);
	            notificationRequest.setApiVersion(apiVersion);
	            notificationRequest.setApplicationDescription(description);
	            notificationRequest.setApiProvider(apiProvider);
	            notificationRequest.setApplicationName(apiContext);
	            notificationRequest.setApprovalStatus(operatorName.toUpperCase() + "-" + operatorAdminApprovalStatus);

	            apiNotification.subscriptionNotificationSp(notificationRequest);
	        }
		} catch (Exception e) {
			String errorMessage = "Error in NotifyApprovalTask.executeOperatorAdminSubscriptionApproval";
			log.error(errorMessage, e);
			throw new ApprovalWorkflowException(errorMessage, e);
		}
	}

}
