package com.wso2telco.workflow.service;


import com.wso2telco.workflow.application.ApplicationApproval;
import com.wso2telco.workflow.application.ApplicationApprovalImpl;
import com.wso2telco.workflow.dao.WorkflowStatsDbService;
import com.wso2telco.workflow.model.ApplicationApprovalAuditRecord;
import com.wso2telco.workflow.model.Subscription;
import com.wso2telco.workflow.model.SubscriptionApprovalAuditRecord;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/audit")
public class WorkflowApprovalAuditService {

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
