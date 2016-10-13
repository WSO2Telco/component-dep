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

package com.wso2telco.application.creation.approval.workflow.servicetask;

import com.wso2telco.application.creation.approval.workflow.exception.HubWorkflowCallbackApiErrorDecoder;
import com.wso2telco.application.creation.approval.workflow.model.Application;
import com.wso2telco.application.creation.approval.workflow.model.ApplicationApprovalAuditRecord;
import com.wso2telco.application.creation.approval.workflow.rest.client.HubWorkflowApi;
import com.wso2telco.application.creation.approval.workflow.rest.client.WorkflowApprovalAuditApi;
import com.wso2telco.application.creation.approval.workflow.util.AuthRequestInterceptor;
import com.wso2telco.application.creation.approval.workflow.util.Constants;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OperatorDataUpdater implements JavaDelegate {

    private static final Log log = LogFactory.getLog(OperatorDataUpdater.class);

	public void execute(DelegateExecution arg0) throws Exception {

        AuthRequestInterceptor authRequestInterceptor=new AuthRequestInterceptor();
		String operatorName = arg0.getVariable(Constants.OPERATOR).toString();
		String applicationId=arg0.getVariable(Constants.APPLICATION_ID).toString();
        String serviceUrl = arg0.getVariable(Constants.SERVICE_URL).toString();
        String deploymentType = arg0.getVariable(Constants.DEPLOYMENT_TYPE).toString();
        String completedByUser= arg0.getVariable(Constants.COMPLETE_BY_USER).toString();
        String completedOn= arg0.getVariable(Constants.COMPLETED_ON).toString();
        String completedByRole=arg0.getVariable(Constants.OPERATOR).toString()+Constants.ADMIN_ROLE;
        String applicationName= arg0.getVariable(Constants.APPLICATION_NAME).toString();
        String userName= arg0.getVariable(Constants.USER_NAME).toString();
        String adminPassword= arg0.getVariable(Constants.ADMIN_PASSWORD).toString();

        HubWorkflowApi api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new HubWorkflowCallbackApiErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminPassword))
                .target(HubWorkflowApi.class, serviceUrl);

        WorkflowApprovalAuditApi apiAudit = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new HubWorkflowCallbackApiErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminPassword))
                .target(WorkflowApprovalAuditApi.class, serviceUrl);
		
		String operatorAdminApprovalStatus = arg0.getVariable(Constants.OPERATOR_ADMIN_APPROVAL).toString();
		log.info("In OperatorDataUpdater, Operator admin approval status: " + operatorAdminApprovalStatus +
				" Operator: " + operatorName +"applicationId :"+applicationId);

        Application application=new Application();
        application.setApplicationID(Integer.parseInt(applicationId));
        application.setStatus(operatorAdminApprovalStatus);
        application.setOperatorID(1);
        application.setOperatorName(operatorName);
           try {
            if(deploymentType.equalsIgnoreCase(Constants.GATEWAY)){
            api.applicationApprovalHub(application);
            }
            api.applicationApprovalOperator(application);

            ApplicationApprovalAuditRecord applicationApprovalAuditRecord=new ApplicationApprovalAuditRecord();
            applicationApprovalAuditRecord.setAppApprovalType("ADMIN");
            applicationApprovalAuditRecord.setAppCreator(userName);
            applicationApprovalAuditRecord.setAppName(applicationName);
            applicationApprovalAuditRecord.setAppStatus(operatorAdminApprovalStatus);
            applicationApprovalAuditRecord.setCompletedByUser(completedByUser);
            applicationApprovalAuditRecord.setCompletedByRole(completedByRole);
            applicationApprovalAuditRecord.setCompletedOn(completedOn);

            apiAudit.applicationApprovalAudit(applicationApprovalAuditRecord);

        } catch (Exception e) {
               throw new Exception(e);
        }
	}


}
