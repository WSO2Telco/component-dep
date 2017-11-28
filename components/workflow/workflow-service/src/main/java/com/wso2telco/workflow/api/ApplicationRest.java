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

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.core.dbutils.util.AppAssignRequest;
import com.wso2telco.core.dbutils.util.Callback;
import com.wso2telco.core.dbutils.util.AppApprovalRequest;
import org.workflow.core.model.TaskSerchDTO;
import org.workflow.core.service.WorkFlowDelegator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/applications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)


public class ApplicationRest {

	 @GET
	 @Path("/search")
	 public Response load(@HeaderParam("authorization") String authHeader) {
		 try {
		    	WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
		    	TaskSerchDTO serchD = new TaskSerchDTO();
		    	UserProfileDTO userProfileDTO = new UserProfileDTO();
		    	userProfileDTO.setUserName("admin");
		    	Callback callback = workFlowDelegator.getPendingApplicationApprovals(serchD, userProfileDTO);
		        return Response.status(Response.Status.OK).entity(callback).build();
		 } catch(Exception e) {
		            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		 }
	}

    @GET
    @Path("/graph")
    public Response loadGraph(@HeaderParam("authorization") String authHeader)  {
        try {
            WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
            TaskSerchDTO serchD = new TaskSerchDTO();
            UserProfileDTO userProfileDTO = new UserProfileDTO();
            userProfileDTO.setUserName("admin");
            Callback callback = workFlowDelegator.getApplicationGraphData(userProfileDTO);
            return Response.status(Response.Status.OK).entity(callback).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/assign")
    public Response assign(@HeaderParam("authorization") String authHeader, AppAssignRequest appAssignRequest)  {
        try {
            WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
            TaskSerchDTO serchD = new TaskSerchDTO();
            UserProfileDTO userProfileDTO = new UserProfileDTO();
            userProfileDTO.setUserName("admin");
            Callback callback = workFlowDelegator.assignApplication(appAssignRequest);
            return Response.status(Response.Status.OK).entity(callback).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/approve")
    public Response approve(@HeaderParam("authorization") String authHeader, AppApprovalRequest appApprovalRequest )  {
        try {
            WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
            TaskSerchDTO serchD = new TaskSerchDTO();
            UserProfileDTO userProfileDTO = new UserProfileDTO();
            userProfileDTO.setUserName("admin");
            Callback callback = workFlowDelegator.approveApplication(appApprovalRequest);
            return Response.status(Response.Status.OK).entity(callback).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
