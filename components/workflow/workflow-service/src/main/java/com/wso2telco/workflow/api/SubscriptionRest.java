/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.workflow.api;

import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.AssignRequest;
import com.wso2telco.core.dbutils.util.Callback;
import com.wso2telco.core.userprofile.UserProfileRetriever;
import com.wso2telco.core.userprofile.dto.UserProfileDTO;
import com.wso2telco.workflow.model.SubscriptionEditDTO;
import com.wso2telco.workflow.notification.Notification;
import com.wso2telco.workflow.notification.NotificationImpl;
import com.wso2telco.workflow.service.SubscriptionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.workflow.core.model.SubSearchResponse;
import org.workflow.core.model.SubscriptionTask;
import org.workflow.core.model.TaskSearchDTO;
import org.workflow.core.service.WorkFlowDelegator;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

@Path("/subscriptions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SubscriptionRest {

	SubscriptionService subscriptionService = new SubscriptionService();
    private static final Log log = LogFactory.getLog(SubscriptionRest.class);

    @GET
    @Path("/search")
    public Response load(@HeaderParam("user-name") String userName,
                         @QueryParam("batchSize") byte batchSize,
                         @QueryParam("start") int start,
                         @QueryParam("orderBy") String orderBy,
                         @QueryParam("sortBy") String sortBy,
                         @QueryParam("filterBy") String filterBy) {
        Response response;
        try {
            WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
            TaskSearchDTO searchD = new TaskSearchDTO();
            searchD.setStart(start);
            searchD.setFilterBy(filterBy);
            UserProfileRetriever userProfileRetriever = new UserProfileRetriever();
            UserProfileDTO userProfile = userProfileRetriever.getUserProfile(userName);
            Callback callback = workFlowDelegator.getPendingSubscriptionApprovals(searchD, userProfile);
            response = Response.status(Response.Status.OK).entity(callback).build();
        } catch (Exception e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    @GET
    @Path("/search/{assignee}")
    public Response load(@HeaderParam("user-name") String userName, @QueryParam("batchSize") int batchSize,
                         @QueryParam("start") int start, @QueryParam("orderBy") String orderBy,
                         @QueryParam("sortBy") String sortBy, @QueryParam("filterBy") String filterBy, @PathParam("assignee") String assignee) {
        Response response;
        try {
            WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
            TaskSearchDTO searchD = new TaskSearchDTO();
            searchD.setStart(start);
            searchD.setFilterBy(filterBy);
            UserProfileRetriever userProfileRetriever = new UserProfileRetriever();
            UserProfileDTO userProfile = userProfileRetriever.getUserProfile(userName);
            Callback callback = workFlowDelegator.getPendingAssignedSubscriptionApprovals(searchD, userProfile, assignee);
            response = Response.status(Response.Status.OK).entity(callback).build();
        } catch (Exception e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    @GET
    @Path("/graph")
    public Response loadGraph(@HeaderParam("user-name") String userName) {
        Response response;
        try {
            WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
            UserProfileRetriever userProfileRetriever = new UserProfileRetriever();
            UserProfileDTO userProfile = userProfileRetriever.getUserProfile(userName);
            Callback callback = workFlowDelegator.getSubscriptionGraphData(userProfile);
            response = Response.status(Response.Status.OK).entity(callback).build();
        } catch (Exception e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    @POST
    @Path("/assign")
    public Response assign(@HeaderParam("user-name") String userName, AssignRequest assignRequest) {
        Response response;
        try {
            WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
            UserProfileRetriever userProfileRetriever = new UserProfileRetriever();
            UserProfileDTO userProfile = userProfileRetriever.getUserProfile(userName);
            Callback callback = workFlowDelegator.assignSubscriptionTask(assignRequest, userProfile);
            response = Response.status(Response.Status.OK).entity(callback).build();
        } catch (Exception e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    @POST
    @Path("/approve")
    public Response approve(@HeaderParam("user-name") String userName,
                            @HeaderParam("Authorization") String authString,
                            ApprovalRequest approvalRequest) {
        Response response;
        try {
            UserProfileRetriever userProfileRetriever = new UserProfileRetriever();
            UserProfileDTO userProfile = userProfileRetriever.getUserProfile(userName);
            Object subSearchResponse = loadByTaskId(approvalRequest.getTaskId(), userProfile).getPayload();
            WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
            Callback callback = workFlowDelegator.approveSubscription(approvalRequest, userProfile);
            response = Response.status(Response.Status.OK).entity(callback).build();

            this.subscriptionApprovalAuditLog(
                    approvalRequest, subSearchResponse, callback.getSuccess(), extractLoggedInUser(authString)
            );
        } catch (Exception e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    private Callback loadByTaskId(String taskId, UserProfileDTO userProfile) throws BusinessException {
        WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
        return workFlowDelegator.getPendingSubscriptionApproval(taskId, userProfile);
    }

    private String extractLoggedInUser(String authString) {
        final String AUTH_STR_DELIMITER_REGX = "\\s+";
        final String UN_PW_DELIMITER = ":";

        return new StringTokenizer(new String(
                DatatypeConverter.parseBase64Binary(authString.split(AUTH_STR_DELIMITER_REGX)[1]),
                StandardCharsets.UTF_8
        ), UN_PW_DELIMITER ).nextToken();
    }

    private void subscriptionApprovalAuditLog(
            ApprovalRequest approvalRequest, Object payload, boolean success, String loggedInUser) {
        if (payload instanceof SubSearchResponse) {
            SubscriptionTask subscriptionTask = ((SubSearchResponse) payload).getApplicationTasks().get(0);
            JSONObject subWorkflow = new JSONObject();
            subWorkflow.put("workflow_id", subscriptionTask.getWorkflowRefId());
            subWorkflow.put(APIConstants.AuditLogConstants.STATUS, (success ? "APPROVED" : "FAILED"));
            subWorkflow.put(APIConstants.AuditLogConstants.API_NAME, subscriptionTask.getApiName());
            subWorkflow.put("api_version", subscriptionTask.getApiVersion());
            subWorkflow.put(APIConstants.AuditLogConstants.APPLICATION_ID, subscriptionTask.getApplicationId());
            subWorkflow.put(APIConstants.AuditLogConstants.APPLICATION_NAME, subscriptionTask.getApplicationName());
            subWorkflow.put("req_tier", subscriptionTask.getTier());
            subWorkflow.put("approved_tier", approvalRequest.getSelectedTier());
            subWorkflow.put("subscriber", subscriptionTask.getUserName());

            APIUtil.logAuditMessage(
                "SubscriptionApprovalWorkflow",
                subWorkflow.toString(),
                APIConstants.AuditLogConstants.UPDATED,
                loggedInUser
            );
        } else {
            String error = "Error logging application approval: task is not an subscription approval task. | expected: "
                    + SubSearchResponse.class.getName() + ", actual: " + payload.getClass().getName();
            log.error(error);
        }
    }

    @PUT
	public Response editSubscription(SubscriptionEditDTO subscription) {
		try {
            Notification notification = new NotificationImpl();
			Response response = subscriptionService.editSubscriptionTier(subscription);
			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                notification.sendsubscriptionTierEditNotification(subscription);
            } else {
			    throw new Exception();
            }
            return Response.status(Response.Status.OK).entity(subscription).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}
