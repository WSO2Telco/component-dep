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
import com.wso2telco.application.creation.approval.workflow.rest.client.HubWorkflowApi;
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

		String operatorName = arg0.getVariable(Constants.OPERATOR).toString();
		String applicationId=arg0.getVariable("applicationId").toString();
        String serviceUrl = "https://10.10.12.16:9443/workflow-service/";

        HubWorkflowApi api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new HubWorkflowCallbackApiErrorDecoder())
                .requestInterceptor(getBasicAuthRequestInterceptor())
                .target(HubWorkflowApi.class, serviceUrl);
		
		String operatorAdminApprovalStatus = arg0.getVariable(Constants.OPERATOR_ADMIN_APPROVAL).toString();
		log.info("In OperatorDataUpdater, Operator admin approval status: " + operatorAdminApprovalStatus +
				" Operator: " + operatorName +"applicationId :"+applicationId);

        Application application=new Application();
        application.setApplicationID(Integer.parseInt(applicationId));
        application.setStatus(operatorAdminApprovalStatus);
        application.setOperatorID(1);
        application.setOperatorName(operatorName);


        try {
            api.applicationApprovalHub(application);
            api.applicationApprovalOperator(application);
        } catch (Exception e) {
           e.printStackTrace();
        }
	}

    private BasicAuthRequestInterceptor getBasicAuthRequestInterceptor () {
        String username;
        String password;

        // check java system properties first
        username = System.getProperty(Constants.HUB_WORKFLOW_CALLBACK_USERNAME_PROPERTY);
        // if not found, check environment variables
        if (username == null) {
            username = System.getenv(Constants.HUB_WORKFLOW_CALLBACK_USERNAME_PROPERTY);
        }
        // if still not found, use 'admin' :D
        if (username == null) {
            username = "admin";
        }

        // check java system properties first
        password = System.getProperty(Constants.HUB_WORKFLOW_CALLBACK_PASSWORD_PROPERTY);
        // if not found, check environment variables
        if (password == null) {
            password = System.getenv(Constants.HUB_WORKFLOW_CALLBACK_PASSWORD_PROPERTY);
        }
        // if still not found, use 'admin' :D
        if (password == null) {
            password = "admin";
        }

        return new BasicAuthRequestInterceptor(username, password);
    }

}
