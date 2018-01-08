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

import com.google.gson.Gson;
import com.wso2telco.dep.reportingservice.southbound.SbHostObjectUtils;
import com.wso2telco.workflow.model.ApplicationStatusDTO;
import com.wso2telco.workflow.model.ApprovalDTO;
import com.wso2telco.workflow.service.WorkflowHistoryService;
import com.wso2telco.workflow.utils.WorkflowServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.model.Application;
import org.wso2.carbon.apimgt.api.model.Subscriber;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/history")
public class WorkflowHistoryAPI {

    private static final Log log = LogFactory.getLog(WorkflowHistoryAPI.class);

    @POST
    @Path("/approval")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApprovalHistory(ApprovalDTO approvalDTO) {

        String fromDate = approvalDTO.getFromDate();
        String toDate = approvalDTO.getToDate();
        String subscriber = approvalDTO.getSubscriber();
        String api = approvalDTO.getApi();
        int applicationId = approvalDTO.getApplicationId();
        String operator = approvalDTO.getOperator();
        int offset = approvalDTO.getOffset();
        int count = approvalDTO.getCount();

        String jsonPayload;

		try {
			List<String[]> apiRequests = SbHostObjectUtils.getApprovalHistory(fromDate, toDate, subscriber, api, applicationId, operator, offset, count);
			jsonPayload = new Gson().toJson(apiRequests);
		} catch (Exception e) {
			log.error(e);
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
		}
		return Response.status(HttpServletResponse.SC_OK).entity(jsonPayload).build();
	}

    @GET
    @Path("/approval/{app_id}")
    @Produces("application/json")
    public Response getAppApprovalHistory(@PathParam("app_id") int appID) {

        Gson gson = new Gson();
        WorkflowHistoryService service = new WorkflowHistoryService();
        ApplicationStatusDTO app = null;
        try {
            app = service.getAppApprovalHistory(appID);
        } catch (WorkflowServiceException e) {
            log.error("",e);
            return Response.serverError().entity(gson.toJson("An error has occurred. ")).build();
        }

        return Response.ok().entity(gson.toJson(app)).build();
    }

    @GET
    @Path("/subscribers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSubscribers() {
        String jsonPayload;
        try {
            List<String> subscribers = SbHostObjectUtils.getAllSubscribers();
            jsonPayload = new Gson().toJson(subscribers);
        } catch (Exception e) {
            log.error(e);
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(HttpServletResponse.SC_OK).entity(jsonPayload).build();
    }


    @GET
    @Path("/apis/{subscriberName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAPIsBySubscriber(@PathParam("subscriberName") String subscriberName) {

        String jsonPayload;
        try {
            List<String> apis = SbHostObjectUtils.getAPIsBySubscriber(subscriberName);
            jsonPayload = new Gson().toJson(apis);
        } catch (Exception e) {
            log.error(e);
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(HttpServletResponse.SC_OK).entity(jsonPayload).build();
    }


    @GET
    @Path("/operators")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOperators() {

        String jsonPayload;
        try {
            List<String> operators = SbHostObjectUtils.getAllOperators();
            jsonPayload = new Gson().toJson(operators);
        } catch (Exception e) {
            log.error(e);
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(HttpServletResponse.SC_OK).header("Content-Type", "application/json").entity(jsonPayload).build();
    }


    @GET
    @Path("/applications/{subscriberName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApplications(@PathParam("subscriberName") String subscriberName) {
        String jsonPayload;
        try {
            APIConsumer apiConsumer = APIManagerFactory.getInstance().getAPIConsumer();
            Subscriber subscriber = new Subscriber(subscriberName);
            Application[] applicationList = apiConsumer.getApplications(subscriber, "");
            jsonPayload = new Gson().toJson(applicationList);
        } catch (Exception e) {
            log.error(e);
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(HttpServletResponse.SC_OK).header("Content-Type", "application/json").entity(jsonPayload).build();
    }

}
