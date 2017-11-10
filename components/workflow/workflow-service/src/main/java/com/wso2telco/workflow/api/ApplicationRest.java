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

package com.wso2telco.workflow.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.workflow.core.model.TaskSerchDTO;
import org.workflow.core.service.WorkFlowDelegator;

import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.core.dbutils.util.Callback;


@Path("/applications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ApplicationRest {
	 @GET
	 @Path("/search")
	 public Response load(@HeaderParam("authorization") String authHeader) {
		 System.out.println("++++++++++++++++++++++++++++++++ ApplicationRest:applications/search STARTED");
		        try {
		    	WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
		    	TaskSerchDTO serchD = new TaskSerchDTO();
		    	UserProfileDTO UserProfileDTO = new UserProfileDTO();
		    	Callback callback = workFlowDelegator.getPendingApplicationApprovals(serchD, UserProfileDTO);
		    	/* Callback callback = applicationDetailService.getDetails(authHeader, detailRequestDAO);
		    	return Response.status(Response.Status.OK).entity(callback).build();*/
		     System.out.println("++++++++++++++++++++++++++++++++ ApplicationRest:applications/search ENDED");
		            return Response.status(Response.Status.OK).build();
		        } catch(Exception e) {
		            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		        }
		 //return null;
		 }

}
