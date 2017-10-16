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

import java.util.Date;
import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.workflow.approval.application.rest.client.CreditPlanApi;
import com.wso2telco.workflow.approval.exception.ApprovalWorkflowException;
import com.wso2telco.workflow.approval.exception.HubWorkflowCallbackApiErrorDecoder;
import com.wso2telco.workflow.approval.model.WorkflowCreditPlan;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import com.wso2telco.workflow.approval.util.Constants;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class CreditApprovalTask implements WorkflowApprovalTask {

	private static final Log log = LogFactory.getLog(CreditApprovalTask.class);

	@Override
	public void executeHubAdminApplicationApproval(DelegateExecution arg0) throws ApprovalWorkflowException {

		try {

			String creditPlan = arg0.getVariable(Constants.CREDIT_PLAN) != null
					? arg0.getVariable(Constants.CREDIT_PLAN).toString() : null;
			if (creditPlan != null && !creditPlan.trim().isEmpty()) {
				String mandateServiceUrl = arg0.getVariable(Constants.MANDATE_SERVICE_URL) != null
						? arg0.getVariable(Constants.MANDATE_SERVICE_URL).toString() : null;
				String adminUserName = arg0.getVariable(Constants.ADMIN_USER_NAME) != null
						? arg0.getVariable(Constants.ADMIN_USER_NAME).toString() : null;
				String adminPassword = arg0.getVariable(Constants.ADMIN_PASSWORD) != null
						? arg0.getVariable(Constants.ADMIN_PASSWORD).toString() : null;
				int applicationId = arg0.getVariable(Constants.APPLICATION_ID) != null
						? Integer.parseInt(arg0.getVariable(Constants.APPLICATION_ID).toString()) : null;
				String serviceProvider = arg0.getVariable(Constants.USER_NAME) != null
						? arg0.getVariable(Constants.USER_NAME).toString() : null;
				String applicationName = arg0.getVariable(Constants.APPLICATION_NAME) != null
						? arg0.getVariable(Constants.APPLICATION_NAME).toString() : null;
				String status = "A";
				boolean isCooperate = false;
				Date date = new Date();
				long timeMilli = date.getTime();
				String lastModifiedTimestamp = Long.toString(timeMilli);

				AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();
				CreditPlanApi creditPlanApi = Feign.builder().encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
						.errorDecoder(new HubWorkflowCallbackApiErrorDecoder())
						.requestInterceptor(
								authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName, adminPassword))
						.target(CreditPlanApi.class, mandateServiceUrl);

				WorkflowCreditPlan workflowCreditPlan = new WorkflowCreditPlan();
				workflowCreditPlan.setAppId(String.valueOf(applicationId));
				workflowCreditPlan.setAppName(applicationName);
				workflowCreditPlan.setSp(serviceProvider);
				workflowCreditPlan.setCreditPlan(creditPlan);
				workflowCreditPlan.setStatus(status);
				workflowCreditPlan.setCooperate(isCooperate);
				workflowCreditPlan.setLastModifiedTimestamp(lastModifiedTimestamp);

				creditPlanApi.creditPlanApprovalHub(workflowCreditPlan);
			} else {
				//Do not send the request to
			}
		} catch (Exception e) {

			String errorMessage = "Error in CreditApprovalTask.executeHubAdminApplicationApproval";
			log.error(errorMessage, e);
			throw new ApprovalWorkflowException(errorMessage, e);
		}
	}

	@Override
	public void executeHubAdminSubscriptionApproval(DelegateExecution arg0) throws ApprovalWorkflowException {
		// not related to hub admin subscription approval
	}

	@Override
	public void executeOperatorAdminApplicationApproval(DelegateExecution arg0) throws ApprovalWorkflowException {
		// not related to operator admin application approval
	}

	@Override
	public void executeOperatorAdminSubscriptionApproval(DelegateExecution arg0) throws ApprovalWorkflowException {
		// not related to operator admin subscription approval
	}
}
