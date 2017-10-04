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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import org.activiti.engine.delegate.DelegateExecution;

import com.wso2telco.workflow.approval.application.ApplicationTask;
import com.wso2telco.workflow.approval.application.ApplicationTaskFactory;
import com.wso2telco.workflow.approval.application.rest.client.HubWorkflowApi;
import com.wso2telco.workflow.approval.exception.ApprovalWorkflowException;
import com.wso2telco.workflow.approval.exception.HubWorkflowCallbackApiErrorDecoder;
import com.wso2telco.workflow.approval.model.Application;
import com.wso2telco.workflow.approval.model.ApplicationApprovalAuditRecord;
import com.wso2telco.workflow.approval.model.Subscription;
import com.wso2telco.workflow.approval.subscription.SubscriptionTaskFactory;
import com.wso2telco.workflow.approval.subscription.rest.client.SubscriptionWorkflowApi;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import com.wso2telco.workflow.approval.util.Constants;

public class ProcessApprovalTask implements WorkflowApprovalTask {

	private static final Log log = LogFactory.getLog(ProcessApprovalTask.class);

	@Override
	public void executeHubAdminApplicationApproval(DelegateExecution arg0) throws ApprovalWorkflowException {
		try {
			String status = arg0.getVariable(Constants.HUB_ADMIN_APPROVAL) != null ? arg0.getVariable(Constants.HUB_ADMIN_APPROVAL).toString() :null;
	        String operatorName = arg0.getVariable(Constants.OPERATOR) != null ? arg0.getVariable(Constants.OPERATOR).toString() : null;
	        int applicationId = arg0.getVariable(Constants.APPLICATION_ID) != null ? Integer.parseInt(arg0.getVariable(Constants.APPLICATION_ID).toString()) : null;
	        String serviceUrl = arg0.getVariable(Constants.SERVICE_URL) != null ? arg0.getVariable(Constants.SERVICE_URL).toString() : null;
	        String completedByUser = arg0.getVariable(Constants.COMPLETE_BY_USER) != null ? arg0.getVariable(Constants.COMPLETE_BY_USER).toString() : null;
	        String completedOn = arg0.getVariable(Constants.COMPLETED_ON) != null ? arg0.getVariable(Constants.COMPLETED_ON).toString() : null;
	        String applicationName = arg0.getVariable(Constants.APPLICATION_NAME) != null ? arg0.getVariable(Constants.APPLICATION_NAME).toString() : null;
	        String userName = arg0.getVariable(Constants.USER_NAME) != null ? arg0.getVariable(Constants.USER_NAME).toString() : null;
	        String adminUserName = arg0.getVariable(Constants.ADMIN_USER_NAME) != null ? arg0.getVariable(Constants.ADMIN_USER_NAME).toString() : null;
	        String adminPassword = arg0.getVariable(Constants.ADMIN_PASSWORD) != null ? arg0.getVariable(Constants.ADMIN_PASSWORD).toString() : null;
	        String selectedTier = arg0.getVariable(Constants.SELECTED_TIER) != null ? arg0.getVariable(Constants.SELECTED_TIER).toString() : null;
	        String completedByRole = Constants.ADMIN_ROLE;
	        arg0.setVariable(Constants.ADMIN_SELECTED_TIER,selectedTier);

	        log.info("In HubDataUpdater, Hub admin approval status: " + status);

	        AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();
	        HubWorkflowApi api = Feign.builder()
	                .encoder(new JacksonEncoder())
	                .decoder(new JacksonDecoder())
	                .errorDecoder(new HubWorkflowCallbackApiErrorDecoder())
	                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName,adminPassword))
	                .target(HubWorkflowApi.class,serviceUrl);

	        com.wso2telco.workflow.approval.application.rest.client.WorkflowApprovalAuditApi apiAudit = Feign.builder()
	                .encoder(new JacksonEncoder())
	                .decoder(new JacksonDecoder())
	                .errorDecoder(new HubWorkflowCallbackApiErrorDecoder())
	                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName, adminPassword))
	                .target(com.wso2telco.workflow.approval.application.rest.client.WorkflowApprovalAuditApi.class, serviceUrl);

	        ApplicationApprovalAuditRecord applicationApprovalAuditRecord = new ApplicationApprovalAuditRecord();
	        applicationApprovalAuditRecord.setAppApprovalType("ADMIN");
	        applicationApprovalAuditRecord.setAppCreator(userName);
	        applicationApprovalAuditRecord.setAppName(applicationName);
	        applicationApprovalAuditRecord.setAppStatus(status);
	        applicationApprovalAuditRecord.setCompletedByUser(completedByUser);
	        applicationApprovalAuditRecord.setCompletedByRole(completedByRole);
	        applicationApprovalAuditRecord.setCompletedOn(completedOn);

	        apiAudit.applicationApprovalAudit(applicationApprovalAuditRecord);

	        Application application = new Application();
	        application.setApplicationID(applicationId);
	        application.setStatus(status);
	        application.setOperatorName(operatorName);
	        application.setSelectedTier(selectedTier);
	        api.applicationApprovalHub(application);
		} catch (Exception e) {
			String errorMessage = "Error in ProcessApprovalTask.executeHubAdminApplicationApproval";
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
	        final String operatorName = arg0.getVariable(Constants.OPERATOR) != null ? arg0.getVariable(Constants.OPERATOR).toString() : null;
	        final int applicationId = arg0.getVariable(Constants.APPLICATION_ID) != null ? Integer.parseInt(arg0.getVariable(Constants.APPLICATION_ID).toString()) : 0;
	        final String apiVersion = arg0.getVariable(Constants.API_VERSION) != null ? arg0.getVariable(Constants.API_VERSION).toString() : null;
	        final String applicationName = arg0.getVariable(Constants.APPLICATION_NAME) != null ? arg0.getVariable(Constants.APPLICATION_NAME).toString() : null;
	        final String subscriber = arg0.getVariable(Constants.SUBSCRIBER) != null ? arg0.getVariable(Constants.SUBSCRIBER).toString() : null;
	        final String apiProvider = arg0.getVariable(Constants.API_PROVIDER)!=null?arg0.getVariable(Constants.API_PROVIDER).toString():null;
	        final String selectedTier = arg0.getVariable(Constants.SELECTED_TIER)!=null? status.equalsIgnoreCase(Constants.APPROVE)?arg0.getVariable(Constants.SELECTED_TIER).toString():Constants.REJECTED_TIER:null;
	        final String workflowRefId = arg0.getVariable(Constants.WORK_FLOW_REF)!=null?arg0.getVariable(Constants.WORK_FLOW_REF).toString():null;
	        final String selectedRate = arg0.getVariable(Constants.SELECTED_RATE)!=null?arg0.getVariable(Constants.SELECTED_RATE).toString():null;
	        arg0.setVariable("hubAdminApproval", status); //hub admin approval status is null. Check jag. remove before deployment.arg0.setVariable(Constants.ADMIN_SELECTED_TIER,selectedTier);
	        arg0.setVariable(Constants.ADMIN_SELECTED_TIER,selectedTier);

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

	        // Get approved or pending operator list
	        String validOperators = api.getApplicationApprovedOrPendingOperators(applicationId);
	        List<String> validOperatorList = Arrays.asList(validOperators.split(","));

	        for (String operator : operatorList) {
	            if(validOperatorList.contains(operator)) {
	                operatorNames.add(operator.trim().toLowerCase());
	                operatorsRoles.add(operator.trim() + Constants.ADMIN_ROLE);

	                log.info("Operator '" + operator.trim() + "' added to operatorList");
	            }
	        }

	        arg0.setVariable("operatorList", operatorNames);
	        arg0.setVariable("operatorRoles", operatorsRoles);
	        
	        log.info("In HubDataUpdater, Hub admin approval status: " + status);

	        Subscription subscription = new Subscription();
	            subscription.setApiName(apiName);
	            subscription.setApplicationID(applicationId);
	            subscription.setOperatorName(operatorName);
	            subscription.setWorkflowRefId(workflowRefId);
	            subscription.setSelectedRate(selectedRate);
	            subscription.setSelectedTier(selectedTier);
	            subscription.setApiProvider(apiProvider);
	            subscription.setApiVersion(apiVersion);
	        api.subscriptionApprovalHub(subscription);
		} catch (Exception e) {
			String errorMessage = "Error in ProcessApprovalTask.executeHubAdminSubscriptionApproval";
			log.error(errorMessage, e);
			throw new ApprovalWorkflowException(errorMessage, e);
		}
	}

	@Override
	public void executeOperatorAdminApplicationApproval(DelegateExecution arg0) throws ApprovalWorkflowException {
		try {
			String deploymentType = arg0.getVariable(Constants.DEPLOYMENT_TYPE) != null ? arg0.getVariable(Constants.DEPLOYMENT_TYPE).toString() : null;
	        ApplicationTaskFactory applicationTaskFactory = new ApplicationTaskFactory();
	        ApplicationTask applicationTask = applicationTaskFactory.getInstance(deploymentType);
	        applicationTask.execute(arg0);
		} catch (Exception e) {
			String errorMessage = "Error in ProcessApprovalTask.executeOperatorAdminApplicationApproval";
			log.error(errorMessage, e);
			throw new ApprovalWorkflowException(errorMessage, e);
		}
	}

	@Override
	public void executeOperatorAdminSubscriptionApproval(DelegateExecution arg0) throws ApprovalWorkflowException {
		try {
			String deploymentType = arg0.getVariable(Constants.DEPLOYMENT_TYPE) != null ? arg0.getVariable(Constants.DEPLOYMENT_TYPE).toString() : null;
	        SubscriptionTaskFactory subscriptionTaskFactory = new SubscriptionTaskFactory();
	        com.wso2telco.workflow.approval.subscription.SubscriptionTask subscriptionTask = subscriptionTaskFactory.getInstance(deploymentType);
	        subscriptionTask.execute(arg0);
		} catch (Exception e) {
			String errorMessage = "Error in ProcessApprovalTask.executeOperatorAdminSubscriptionApproval";
			log.error(errorMessage, e);
			throw new ApprovalWorkflowException(errorMessage, e);
		}
	}

}
