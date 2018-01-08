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

import com.wso2telco.workflow.approval.approvaltask.WorkflowApprovalTask;
import com.wso2telco.workflow.approval.approvaltask.WorkflowApprovalTaskListReader;
import com.wso2telco.workflow.approval.model.NotificationRequest;
import com.wso2telco.workflow.approval.model.Subscription;
import com.wso2telco.workflow.approval.subscription.rest.client.NotificationApi;
import com.wso2telco.workflow.approval.subscription.rest.client.SubscriptionWorkflowApi;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class HubAdminDbUpdater implements JavaDelegate {

	private static final Log log = LogFactory.getLog(HubAdminDbUpdater.class);
	private ArrayList<WorkflowApprovalTask> taskClassesObjList = null;
	
	public HubAdminDbUpdater() {
		try {
			taskClassesObjList = WorkflowApprovalTaskListReader.getWorkflowClassObjList();
		} catch(Exception e) {
			log.error("error in getWorkflowClassObjList : ", e);
		}
	}

	public void execute(DelegateExecution arg0) throws Exception {
		if (taskClassesObjList != null) {
			for (WorkflowApprovalTask taskObj: taskClassesObjList) {
				taskObj.executeHubAdminSubscriptionApproval(arg0);
			}
		} else {
			throw new Exception("Empty workflow task classes list");
		}
	}

}
