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

import java.util.ArrayList;
import java.util.Collection;

import com.wso2telco.workflow.approval.model.NotificationRequest;
import com.wso2telco.workflow.approval.subscription.rest.client.NotificationApi;
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

public class OperatorListConverter implements JavaDelegate {
	
	private static final Log log = LogFactory.getLog(OperatorListConverter.class);

	public void execute(DelegateExecution arg0) throws Exception {

		String[] operatorList = arg0.getVariable("operators").toString().split(",");
		Collection<String> operatorNames = new ArrayList<String>();
        Collection<String> operatorsRoles = new ArrayList<String>();
		for(String operator : operatorList) {
			operatorNames.add(operator.trim());
            operatorsRoles.add(operator.trim()+Constants.ADMIN_ROLE);
			// TODO: make debug
			log.info("Operator '" + operator.trim() + "' added to operatorList");
		}
	    arg0.setVariable("operatorList", operatorNames);
        arg0.setVariable("operatorRoles", operatorsRoles);
        String deploymentType = arg0.getVariable(Constants.DEPLOYMENT_TYPE) != null ? arg0.getVariable(Constants.DEPLOYMENT_TYPE).toString() : null;

        if(deploymentType.equalsIgnoreCase(Constants.HUB)) {

            AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();
            String adminUserName = arg0.getVariable(Constants.ADMIN_USER_NAME) != null ? arg0.getVariable(Constants.ADMIN_USER_NAME).toString() : null;
            String adminPassword = arg0.getVariable(Constants.ADMIN_PASSWORD).toString();
            String serviceUrl = arg0.getVariable(Constants.SERVICE_URL) != null ? arg0.getVariable(Constants.SERVICE_URL).toString() : null;
            String selectedTier = arg0.getVariable(Constants.TIER) != null ? arg0.getVariable(Constants.TIER).toString() : null;
            String applicationName = arg0.getVariable(Constants.APPLICATION_NAME) != null ? arg0.getVariable(Constants.APPLICATION_NAME).toString() : null;
            String applicationDescription = arg0.getVariable(Constants.DESCRIPTION) != null ? arg0.getVariable(Constants.DESCRIPTION).toString() : null;
            String userName = arg0.getVariable(Constants.USER_NAME) != null ? arg0.getVariable(Constants.USER_NAME).toString() : null;
            String completedByRole = Constants.WORKFLOW_ADMIN_ROLE;

            NotificationApi apiNotification = Feign.builder()
                    .encoder(new JacksonEncoder())
                    .decoder(new JacksonDecoder())
                    .errorDecoder(new WorkflowCallbackErrorDecoder())
                    .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName, adminPassword))
                    .target(NotificationApi.class, serviceUrl);

            NotificationRequest notificationRequest = new NotificationRequest();
            notificationRequest.setApplicationName(applicationName);
            notificationRequest.setApplicationTier(selectedTier);
            notificationRequest.setApplicationDescription(applicationDescription);
            notificationRequest.setUserName(userName);
            notificationRequest.setReceiverRole(completedByRole);
            apiNotification.sendHUBAdminAppApprovalNotification(notificationRequest);

        }


	}
}
