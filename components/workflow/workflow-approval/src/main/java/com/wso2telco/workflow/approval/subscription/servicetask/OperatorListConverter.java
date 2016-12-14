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

import com.wso2telco.workflow.approval.subscription.initiate.SubscriptionInitiate;
import com.wso2telco.workflow.approval.subscription.initiate.SubscriptionInitiateFactory;
import com.wso2telco.workflow.approval.util.Constants;

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
        String deploymentType = arg0.getVariable(Constants.DEPLOYMENT_TYPE).toString();
        Collection<String> operatorNames = new ArrayList<String>();
        Collection<String> operatorsRoles = new ArrayList<String>();
        for (String operator : operatorList) {
            operatorNames.add(operator.trim());
            operatorsRoles.add(operator.trim()+Constants.ADMIN_ROLE);
            // TODO: make debug
            log.info("Operator '" + operator.trim() + "' added to operatorList");
        }
        arg0.setVariable("operatorList", operatorNames);
        arg0.setVariable("operatorRoles", operatorsRoles);
        SubscriptionInitiateFactory subscriptionInitiateFactory = new SubscriptionInitiateFactory();
        SubscriptionInitiate subscriptionInitiate = subscriptionInitiateFactory.getInstance(deploymentType);
        subscriptionInitiate.execute(arg0);

    }
}