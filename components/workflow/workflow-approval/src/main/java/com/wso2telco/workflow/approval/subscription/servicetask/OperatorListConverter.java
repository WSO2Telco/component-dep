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
import com.wso2telco.workflow.approval.subscription.initiate.SubscriptionInitiate;
import com.wso2telco.workflow.approval.subscription.initiate.SubscriptionInitiateFactory;
import com.wso2telco.workflow.approval.subscription.rest.client.NotificationApi;
import com.wso2telco.workflow.approval.subscription.rest.client.SubscriptionWorkflowApi;
import com.wso2telco.workflow.approval.subscription.rest.client.WorkflowCallbackErrorDecoder;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import com.wso2telco.workflow.approval.util.Constants;

import feign.Feign;
import feign.Feign.Builder;
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

        String deploymentType = arg0.getVariable(Constants.DEPLOYMENT_TYPE).toString();
        String[] operatorList = arg0.getVariable(Constants.OPERATORS).toString().split(",");
        String adminUserName = arg0.getVariable(Constants.ADMIN_USER_NAME) != null ? arg0.getVariable(Constants.ADMIN_USER_NAME).toString() : null;
        String adminPassword= arg0.getVariable(Constants.ADMIN_PASSWORD).toString();
        String apiName=arg0.getVariable(Constants.API_NAME).toString();

        SubscriptionInitiateFactory subscriptionInitiateFactory = new SubscriptionInitiateFactory();
        SubscriptionInitiate subscriptionInitiate = subscriptionInitiateFactory.getInstance(deploymentType);
        subscriptionInitiate.execute(arg0);

        Collection<String> operatorNames = new ArrayList<String>();
        for(String operator : operatorList) {
            operatorNames.add(operator.trim());
            log.info("Operator '" + operator.trim() + "' added to operatorList");
        }
        arg0.setVariable("operatorList", operatorNames);
    }
}