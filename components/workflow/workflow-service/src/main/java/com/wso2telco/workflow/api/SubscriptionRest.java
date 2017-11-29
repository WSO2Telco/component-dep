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
import javax.ws.rs.QueryParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.workflow.core.model.TaskSerchDTO;
import org.workflow.core.service.WorkFlowDelegator;

import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.core.dbutils.util.Callback;

@Path("/subscriptions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SubscriptionRest {

	@GET
	@Path("/search")
	public Response load(@HeaderParam("authorization") String authHeader,
			 @QueryParam ("batchSize") byte batchSize,
			 @QueryParam ("start") int start,
			 @QueryParam ("orderBy")String  orderBy,
			 @QueryParam ("sortBy")String  sortBy,
			 @QueryParam ("filterBy")String  filterBy) {
		try {
			WorkFlowDelegator workFlowDelegator = new WorkFlowDelegator();
			TaskSerchDTO serchD = new TaskSerchDTO();
			UserProfileDTO UserProfileDTO = new UserProfileDTO();
			Callback callback = workFlowDelegator.getPendingSubscriptionApprovals(serchD, UserProfileDTO);
			return Response.status(Response.Status.OK).entity(callback).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}
