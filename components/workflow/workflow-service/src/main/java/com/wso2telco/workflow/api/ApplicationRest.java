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
import com.wso2telco.workflow.model.ApplicationEditDTO;
import com.wso2telco.workflow.notification.Notification;
import com.wso2telco.workflow.notification.NotificationImpl;
import com.wso2telco.workflow.service.ApplicationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.workflow.core.model.AppSearchResponse;
import org.workflow.core.model.ApplicationTask;
import org.workflow.core.model.TaskSearchDTO;
import org.workflow.core.service.WorkFlowDelegator;
import org.wso2.carbon.CarbonConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;


@Path("/applications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)


public class ApplicationRest {

	ApplicationService applicationService = new ApplicationService();
    private static final Log AUDIT_LOG = CarbonConstants.AUDIT_LOG;
    private static final Log LOG = LogFactory.getLog(ApplicationRest.class);

    @GET
    @Path("/search")
    public Response load(@HeaderParam("user-name") String userName, @QueryParam("batchSize") int batchSize,
                         @QueryParam("start") int start, @QueryParam("orderBy") String orderBy,
                         @QueryParam("sortBy") String sortBy, @QueryParam("filterBy") String filterBy) {

        Response response;
        try {
            WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
            TaskSearchDTO searchD = new TaskSearchDTO();
            searchD.setStart(start);
            searchD.setFilterBy(filterBy);
            UserProfileRetriever userProfileRetriever = new UserProfileRetriever();
            UserProfileDTO userProfile = userProfileRetriever.getUserProfile(userName);
            Callback callback = workFlowDelegator.getPendingApplicationApprovals(searchD, userProfile);
            response = Response.status(Response.Status.OK).entity(callback).build();
        } catch (Exception e) {
            response =  Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
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
            Callback callback = workFlowDelegator.getPendingAssignedApplicationApprovals(searchD, userProfile, assignee);
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
            Callback callback = workFlowDelegator.getApplicationGraphData(userProfile);
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
            UserProfileDTO userProfileDTO = userProfileRetriever.getUserProfile(userName);
            Callback callback = workFlowDelegator.assignApplicationTask(assignRequest, userProfileDTO);
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
            Object appSearchResponse = loadByTaskId(approvalRequest.getTaskId(), userProfile).getPayload();
            WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
            Callback callback = workFlowDelegator.approveApplication(approvalRequest, userProfile);
            response = Response.status(Response.Status.OK).entity(callback).build();

            this.appApprovalAuditLog(
                approvalRequest, appSearchResponse, callback.getSuccess(), extractLoggedInUser(authString)
            );
        } catch (Exception e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            LOG.error("error while executing application approval task: " + approvalRequest.getTaskId() +
                    ", selected tier: " + approvalRequest.getSelectedTier() + ", username: " + userName);
        }
        return response;
    }

    private Callback loadByTaskId(String taskId, UserProfileDTO userProfile) throws BusinessException {
        WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
        return workFlowDelegator.getPendingApplicationApproval(taskId, userProfile);
    }

    private String extractLoggedInUser(String authString) {
        final String AUTH_STR_DELIMITER_REGX = "\\s+";
        final String UN_PW_DELIMITER = ":";

        return new StringTokenizer(new String(
            DatatypeConverter.parseBase64Binary(authString.split(AUTH_STR_DELIMITER_REGX)[1]),
            StandardCharsets.UTF_8
        ), UN_PW_DELIMITER ).nextToken();
    }

    private void appApprovalAuditLog(
            ApprovalRequest approvalRequest, Object payload, boolean success, String loggedInUser) {
        if (payload instanceof AppSearchResponse) {
            ApplicationTask applicationTask = ((AppSearchResponse) payload).getApplicationTasks().get(0);
            String msgOnCompletion = "Application creation approval process completed." +
                " | Workflow ID: " + applicationTask.getWorkflowRefId() +
                " | Workflow State: " + (success ? "APPROVED" : "FAILED") +
                " | Application Name: " + applicationTask.getApplicationName() +
                " | Application ID: " + applicationTask.getApplicationId() +
                " | Subscriber: " + applicationTask.getUserName() +
                " | Requested Tier: " + applicationTask.getTier() +
                " | Approved Tier: " + approvalRequest.getSelectedTier() +
                " | Performed By: " + loggedInUser;
            AUDIT_LOG.info(msgOnCompletion);
            LOG.info(msgOnCompletion);
        } else {
            String error = "Error logging application approval: task is not an application approval task. | expected: "
                + AppSearchResponse.class.getName() + ", actual: " + payload.getClass().getName();
            AUDIT_LOG.error(error);
            LOG.error(error);
        }
    }

    @GET
    @Path("/history")
    public Response approvalHistory(@HeaderParam("user-name") String userName,
                                    @QueryParam("start") int start, @QueryParam("filterBy") String filterBy) {
        Response response;
        try {
            WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
            TaskSearchDTO searchD = new TaskSearchDTO();
            searchD.setStart(start);
            searchD.setFilterBy(filterBy);
            UserProfileRetriever userProfileRetriever = new UserProfileRetriever();
            UserProfileDTO userProfile = userProfileRetriever.getUserProfile(userName);
            Callback callback = workFlowDelegator.getApplicationApprovalHistory(searchD, userProfile);
            response = Response.status(Response.Status.OK).entity(callback).build();
        } catch (Exception e) {
            response =  Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    @PUT
	public Response editApplication(ApplicationEditDTO application) {

		try {
            Notification notification = new NotificationImpl();
			applicationService.editApplicationTier(application);
            notification.sendApplicationTierEditNotification(application);

			return Response.status(Response.Status.OK).entity(application).build();
		} catch (Exception e) {

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
}
