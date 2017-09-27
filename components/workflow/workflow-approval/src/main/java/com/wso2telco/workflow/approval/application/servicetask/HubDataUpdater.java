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

package com.wso2telco.workflow.approval.application.servicetask;

import com.wso2telco.workflow.approval.application.rest.client.HubWorkflowApi;
import com.wso2telco.workflow.approval.approvaltask.WorkflowApprovalTask;
import com.wso2telco.workflow.approval.approvaltask.WorkflowApprovalTaskListReader;
import com.wso2telco.workflow.approval.exception.HubWorkflowCallbackApiErrorDecoder;
import com.wso2telco.workflow.approval.model.Application;
import com.wso2telco.workflow.approval.model.ApplicationApprovalAuditRecord;
import com.wso2telco.workflow.approval.model.NotificationRequest;
import com.wso2telco.workflow.approval.subscription.rest.client.NotificationApi;
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

public class HubDataUpdater implements JavaDelegate {

	private static final Log log = LogFactory.getLog(HubDataUpdater.class);
	ArrayList<WorkflowApprovalTask> taskClassesObjList = null;
	
	public HubDataUpdater() {
		try {
			taskClassesObjList = WorkflowApprovalTaskListReader.getWorkflowClassObjList();
		} catch(Exception e) {
			log.error("error in getWorkflowClassObjList : ", e);
		}
	}

	public void execute(DelegateExecution arg0) throws Exception {
		for (WorkflowApprovalTask taskObj: taskClassesObjList) {
			taskObj.executeHubAdminApplicationApproval(arg0);
		}
	}
	/*
	public void execute(DelegateExecution arg0) throws Exception {

		String status =arg0.getVariable(Constants.HUB_ADMIN_APPROVAL) != null ? arg0.getVariable(Constants.HUB_ADMIN_APPROVAL).toString() :null;
        String operatorName = arg0.getVariable(Constants.OPERATOR) != null ? arg0.getVariable(Constants.OPERATOR).toString() : null;
        int applicationId = arg0.getVariable(Constants.APPLICATION_ID) != null ? Integer.parseInt(arg0.getVariable(Constants.APPLICATION_ID).toString()) : null;
        String serviceUrl = arg0.getVariable(Constants.SERVICE_URL) != null ? arg0.getVariable(Constants.SERVICE_URL).toString() : null;
        String deploymentType = arg0.getVariable(Constants.DEPLOYMENT_TYPE) != null ? arg0.getVariable(Constants.DEPLOYMENT_TYPE).toString() : null;
        String completedByUser = arg0.getVariable(Constants.COMPLETE_BY_USER) != null ? arg0.getVariable(Constants.COMPLETE_BY_USER).toString() : null;
        String completedOn = arg0.getVariable(Constants.COMPLETED_ON) != null ? arg0.getVariable(Constants.COMPLETED_ON).toString() : null;
        String applicationName = arg0.getVariable(Constants.APPLICATION_NAME) != null ? arg0.getVariable(Constants.APPLICATION_NAME).toString() : null;
        String userName = arg0.getVariable(Constants.USER_NAME) != null ? arg0.getVariable(Constants.USER_NAME).toString() : null;
        String adminUserName = arg0.getVariable(Constants.ADMIN_USER_NAME) != null ? arg0.getVariable(Constants.ADMIN_USER_NAME).toString() : null;
        String adminPassword = arg0.getVariable(Constants.ADMIN_PASSWORD) != null ? arg0.getVariable(Constants.ADMIN_PASSWORD).toString() : null;
        String operatorAdminApprovalStatus = arg0.getVariable(Constants.OPERATOR_ADMIN_APPROVAL) != null ? arg0.getVariable(Constants.OPERATOR_ADMIN_APPROVAL).toString() : null;
        String apiVersion = arg0.getVariable(Constants.API_VERSION)!=null?arg0.getVariable(Constants.API_VERSION).toString():null;
        String apiProvider = arg0.getVariable(Constants.API_PROVIDER)!=null?arg0.getVariable(Constants.API_PROVIDER).toString():null;
        String apiContext = arg0.getVariable(Constants.API_CONTEXT)!=null?arg0.getVariable(Constants.API_CONTEXT).toString():null;
        String description = arg0.getVariable(Constants.DESCRIPTION)!=null?arg0.getVariable(Constants.DESCRIPTION).toString():null;
        String selectedTier = arg0.getVariable(Constants.SELECTED_TIER) != null ? arg0.getVariable(Constants.SELECTED_TIER).toString() : null;
        ArrayList operatorRoles= (ArrayList)arg0.getVariable("operatorRoles");
        String completedByRole=Constants.ADMIN_ROLE;
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

        NotificationApi apiNotification = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowCallbackErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName, adminPassword))
                .target(NotificationApi.class,serviceUrl);

        ApplicationApprovalAuditRecord applicationApprovalAuditRecord = new ApplicationApprovalAuditRecord();
        applicationApprovalAuditRecord.setAppApprovalType("ADMIN");
        applicationApprovalAuditRecord.setAppCreator(userName);
        applicationApprovalAuditRecord.setAppName(applicationName);
        applicationApprovalAuditRecord.setAppStatus(status);
        applicationApprovalAuditRecord.setCompletedByUser(completedByUser);
        applicationApprovalAuditRecord.setCompletedByRole(completedByRole);
        applicationApprovalAuditRecord.setCompletedOn(completedOn);

        apiAudit.applicationApprovalAudit(applicationApprovalAuditRecord);

        Application application=new Application();
        application.setApplicationID(applicationId);
        application.setStatus(status);
        application.setOperatorName(operatorName);
        application.setSelectedTier(selectedTier);
        api.applicationApprovalHub(application);

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
	}*/

}
