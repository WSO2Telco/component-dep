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

        if(Constants.HUB.equalsIgnoreCase(deploymentType)) {
            buildNotificationApi(arg0).sendHUBAdminAppApprovalNotification(buildNotificationRequest(arg0));
        } else if (Constants.INTERNAL_GATEWAY_TYPE2.equalsIgnoreCase(deploymentType)) {
            buildNotificationApi(arg0).sendInternalAdminAppApprovalNotification(buildNotificationRequest(arg0));
        }

	}

    private NotificationRequest buildNotificationRequest(DelegateExecution args) {
        String applicationName = args.getVariable(Constants.APPLICATION_NAME) != null ? args.getVariable(Constants.APPLICATION_NAME).toString() : null;
        String selectedTier = args.getVariable(Constants.TIER) != null ? args.getVariable(Constants.TIER).toString() : null;
        String applicationDescription = args.getVariable(Constants.DESCRIPTION) != null ? args.getVariable(Constants.DESCRIPTION).toString() : null;
        String userName = args.getVariable(Constants.USER_NAME) != null ? args.getVariable(Constants.USER_NAME).toString() : null;
        String completedByRole = Constants.WORKFLOW_ADMIN_ROLE;

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setApplicationName(applicationName);
        notificationRequest.setApplicationTier(selectedTier);
        notificationRequest.setApplicationDescription(applicationDescription);
        notificationRequest.setUserName(userName);
        notificationRequest.setReceiverRole(completedByRole);
        return notificationRequest;
    }

    private NotificationApi buildNotificationApi(DelegateExecution args) {
        return Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .errorDecoder(new WorkflowCallbackErrorDecoder())
            .requestInterceptor(new AuthRequestInterceptor().getBasicAuthRequestInterceptor(
                args.getVariable(Constants.ADMIN_USER_NAME) != null ? args.getVariable(Constants.ADMIN_USER_NAME).toString() : null,
                args.getVariable(Constants.ADMIN_PASSWORD).toString())
            )
            .target(
                NotificationApi.class,
                args.getVariable(Constants.SERVICE_URL) != null ? args.getVariable(Constants.SERVICE_URL).toString() : null
            );
    }
}
