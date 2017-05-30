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

package com.wso2telco.workflow.service;

import com.wso2telco.workflow.application.ApplicationApproval;
import com.wso2telco.workflow.application.ApplicationApprovalImpl;
import com.wso2telco.workflow.dao.WorkflowDbService;
import com.wso2telco.workflow.model.Application;
import com.wso2telco.workflow.model.Subscription;
import com.wso2telco.workflow.model.SubscriptionValidation;
import com.wso2telco.workflow.publisher.WorkflowApprovalRatePublisher;
import com.wso2telco.workflow.subscription.SubscriptionApproval;
import com.wso2telco.workflow.subscription.SubscriptionApprovalImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/approval")
public class WorkflowApprovalService {

	private ApplicationApproval applicationApproval = new ApplicationApprovalImpl();
	private SubscriptionApproval subscriptionApproval = new SubscriptionApprovalImpl();
	private WorkflowDbService workflowDbService = new WorkflowDbService();

	@POST
	@Path("application/hub")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response applicationApprovalHub(Application application) {
		try {
			applicationApproval.updateDBAppHubApproval(application);
			return Response.status(Response.Status.CREATED).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	@Path("subscription/hub")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response subscriptionApprovalHub(Subscription subscription) {

		try {

			WorkflowApprovalRatePublisher workflowApprovalRatePublisher = new WorkflowApprovalRatePublisher();
			String selectedRate = subscription.getSelectedRate();
			int appID = subscription.getApplicationID();
			String apiName = subscription.getApiName();

			subscriptionApproval.updateDBSubHubApproval(subscription);

			String selectedRateArray[] = selectedRate.split("-");
			for (int i = 0; i < selectedRateArray.length; i++) {

				String rate = selectedRateArray[i];
				
				if (rate != null && rate.trim().length() > 0) {

					int rateId = Integer.parseInt(rate);
					if (rateId != 0) {

						//workflowApprovalRatePublisher.publishHubAPIRate(rateId, appID, apiName);
						workflowApprovalRatePublisher.publishHubAPIRate(rateId, appID);
					}
				}
			}

			return Response.status(Response.Status.CREATED).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

	}

	@PUT
	@Path("application/operator")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response applicationApprovalOperator(Application application) {
		try {
			applicationApproval.updateDBAppOpApproval(application);
			return Response.status(Response.Status.CREATED).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

	}

	@PUT
	@Path("subscription/operator")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response subscriptionApprovalOperator(Subscription subscription) {
		
		try {
			
			WorkflowApprovalRatePublisher workflowApprovalRatePublisher = new WorkflowApprovalRatePublisher();
			String selectedRate = subscription.getSelectedRate();
			int appID = subscription.getApplicationID();
			String operatorId = subscription.getOperatorName();
			String operationId = subscription.getOpID();
			
			subscriptionApproval.updateDBSubOpApproval(subscription);
			
			String selectedRateArray[] = selectedRate.split("-");
			for (int i = 0; i < selectedRateArray.length; i++) {

				String rate = selectedRateArray[i];
				
				if (rate != null && rate.trim().length() > 0) {

					int rateId = Integer.parseInt(rate);
					if (rateId != 0) {
					
						//workflowApprovalRatePublisher.publishOperatorAPIRate(rateId, appID, operatorId, operationId);
						workflowApprovalRatePublisher.publishOperatorAPIRate(rateId, appID, operatorId, operationId);
					}
				}
			}			
			
			return Response.status(Response.Status.CREATED).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	@Path("subscription/validator")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertValidatorForSubscription(SubscriptionValidation subscriptionValidation) {
		try {
			subscriptionApproval.insertValidatorForSubscription(subscriptionValidation);
			return Response.status(Response.Status.CREATED).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("subscription/getoperators/{apiname}/{apiversion}/{apiprovider}/{appid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response subscriptionGetOperators(@PathParam("apiname") String apiName,
			@PathParam("apiversion") String apiVersion, @PathParam("apiprovider") String apiProvider,
			@PathParam("appid") int appId) {
		try {
			String t;
			t = workflowDbService.getSubApprovalOperators(apiName, apiVersion, apiProvider, appId);

			return Response.status(Response.Status.OK).entity("\"" + t + "\"").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}
