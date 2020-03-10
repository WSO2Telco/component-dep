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


import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.AssignRequest;
import com.wso2telco.core.dbutils.util.Callback;
import com.wso2telco.core.userprofile.UserProfileRetriever;
import com.wso2telco.core.userprofile.dto.UserProfileDTO;
import com.wso2telco.workflow.model.ApplicationEditDTO;
import com.wso2telco.workflow.service.ApplicationService;
import org.workflow.core.model.TaskSearchDTO;
import org.workflow.core.service.WorkFlowDelegator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/applications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)


public class ApplicationRest {

	ApplicationService applicationService = new ApplicationService();

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
    public Response approve(@HeaderParam("user-name") String userName, ApprovalRequest approvalRequest) {
        Response response;
        try {
            WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
            UserProfileRetriever userProfileRetriever = new UserProfileRetriever();
            UserProfileDTO userProfile = userProfileRetriever.getUserProfile(userName);
            Callback callback = workFlowDelegator.approveApplication(approvalRequest, userProfile);
            response = Response.status(Response.Status.OK).entity(callback).build();
        } catch (Exception e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
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

			applicationService.editApplicationTier(application);

			return Response.status(Response.Status.OK).entity(application).build();
		} catch (Exception e) {

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
}
