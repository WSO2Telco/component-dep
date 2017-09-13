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

import com.wso2telco.workflow.dao.WorkflowStatsDbService;
import com.wso2telco.workflow.model.ApplicationApprovalAuditRecord;
import com.wso2telco.workflow.model.SubscriptionApprovalAuditRecord;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/audit")
public class WorkflowApprovalAuditAPI {

    private WorkflowStatsDbService workflowStatsDbService =new WorkflowStatsDbService();

    @POST
    @Path("application")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response appApprovalAuditRecord(ApplicationApprovalAuditRecord applicationApprovalAuditRecord){
        try {
            workflowStatsDbService.insertAppApprovalAuditRecord(applicationApprovalAuditRecord);
            return Response.status(Response.Status.CREATED).build();
        }catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("subscription")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response subscriptionApprovalAuditRecord(SubscriptionApprovalAuditRecord subscriptionApprovalAuditRecord){
        try {
            workflowStatsDbService.insertSubApprovalAuditRecord(subscriptionApprovalAuditRecord);
        return Response.status(Response.Status.CREATED).build();
        }catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


}
