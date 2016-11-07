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

import com.wso2telco.workflow.approval.application.rest.client.HubWorkflowCallbackApi;
import com.wso2telco.workflow.approval.exception.HubWorkflowCallbackApiErrorDecoder;
import com.wso2telco.workflow.approval.exception.HubWorkflowCallbackApiException;
import com.wso2telco.workflow.approval.util.AuthRequestInterceptor;
import com.wso2telco.workflow.approval.util.Constants;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class HubWorkflowCallback implements JavaDelegate {

    private static final Log log = LogFactory.getLog(HubWorkflowCallback.class);

	public void execute(DelegateExecution arg0) throws Exception {

        AuthRequestInterceptor authRequestInterceptor=new AuthRequestInterceptor();
        String refId = arg0.getVariable(Constants.WORKFLOW_REF_ID).toString();
        String hubAdminApprovalStatus = (String) arg0.getVariable(Constants.HUB_ADMIN_APPROVAL);
        String callbackUrl = (String) arg0.getVariable(Constants.CALL_BACK_URL);
        String adminUserName = arg0.getVariable(Constants.ADMIN_USER_NAME) != null ? arg0.getVariable(Constants.ADMIN_USER_NAME).toString() : null;
        String adminPassword= arg0.getVariable(Constants.ADMIN_PASSWORD).toString();
		
        HubWorkflowCallbackApi api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new HubWorkflowCallbackApiErrorDecoder())
                .requestInterceptor(authRequestInterceptor.getBasicAuthRequestInterceptor(adminUserName,adminPassword))
                .target(HubWorkflowCallbackApi.class, callbackUrl);

        log.info("Application creation workflow reference Id: " + refId + ", Hub Admin Approval Status: " +
                hubAdminApprovalStatus);

        try {
        	api.invokeCallback(refId, hubAdminApprovalStatus);
        } catch (HubWorkflowCallbackApiException e) {
        	throw new Exception(e);
        }
	}


}
