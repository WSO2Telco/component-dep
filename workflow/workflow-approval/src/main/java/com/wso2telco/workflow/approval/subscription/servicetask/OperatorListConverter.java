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

import com.wso2telco.workflow.approval.model.HUBAdminSubApprovalNotificationRequest;
import com.wso2telco.workflow.approval.subscription.rest.client.NotificationApi;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import com.wso2telco.workflow.approval.util.Constants;

import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collection;

public class OperatorListConverter implements JavaDelegate {

	private static final Log log = LogFactory.getLog(OperatorListConverter.class);

	public void execute(DelegateExecution arg0) throws Exception {

		String[] operatorList = arg0.getVariable("operators").toString().split(",");
        String adminPassword= arg0.getVariable(Constants.ADMIN_PASSWORD).toString();
        String apiName=arg0.getVariable(Constants.API_NAME).toString();
        String deploymentType = arg0.getVariable(Constants.DEPLOYMENT_TYPE).toString();

		Collection<String> operatorNames = new ArrayList<String>();
		for(String operator : operatorList) {
			operatorNames.add(operator.trim());
			// TODO: make debug
			log.info("Operator '" + operator.trim() + "' added to operatorList");
		}
		arg0.setVariable("operatorList", operatorNames);
        if(deploymentType.equalsIgnoreCase(Constants.INTERNAL_GATEWAY)) {

            AuthRequestInterceptor authRequestInterceptor = new AuthRequestInterceptor();
            String serviceUrl = arg0.getVariable(Constants.SERVICE_URL).toString();
            String apiVersion = arg0.getVariable(Constants.API_VERSION).toString();
            String apiProvider = arg0.getVariable(Constants.API_PROVIDER).toString();
            String completedByRole = operatorList[0] + Constants.ADMIN_ROLE;
            String applicationName = arg0.getVariable(Constants.APPLICATION_NAME).toString();
            String apiContext = arg0.getVariable(Constants.API_CONTEXT).toString();
            String apiTiers = arg0.getVariable("apiTiers").toString();
            String applicationDescription = arg0.getVariable("applicationDescription").toString();

            NotificationApi apiNotification = Feign.builder()
                    .encoder(new JacksonEncoder())
                    .decoder(new JacksonDecoder())
                    .errorDecoder(new WorkflowCallbackErrorDecoder())
                    .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminPassword))
                    .target(NotificationApi.class, serviceUrl);

            HUBAdminSubApprovalNotificationRequest hUBAdminSubApprovalNotificationRequest = new HUBAdminSubApprovalNotificationRequest();
            hUBAdminSubApprovalNotificationRequest.setApiContext(apiContext);
            hUBAdminSubApprovalNotificationRequest.setApiName(apiName);
            hUBAdminSubApprovalNotificationRequest.setSubscriber(applicationName);
            hUBAdminSubApprovalNotificationRequest.setSubscriptionTier(apiTiers);
            hUBAdminSubApprovalNotificationRequest.setReceiverRole(completedByRole);
            hUBAdminSubApprovalNotificationRequest.setApiVersion(apiVersion);
            hUBAdminSubApprovalNotificationRequest.setApplicationDescription(applicationDescription);
            hUBAdminSubApprovalNotificationRequest.setApiProvider(apiProvider);
            hUBAdminSubApprovalNotificationRequest.setApplicationName(applicationName);

            apiNotification.subscriptionNotificationAdminService(hUBAdminSubApprovalNotificationRequest);
        }


	}
}