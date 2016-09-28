package com.wso2telco.workflow.service;

import com.wso2telco.workflow.application.ApplicationApproval;
import com.wso2telco.workflow.application.ApplicationApprovalImpl;
import com.wso2telco.workflow.model.Application;
import com.wso2telco.workflow.model.Subscription;
import com.wso2telco.workflow.model.SubscriptionValidation;
import com.wso2telco.workflow.subscription.SubscriptionApproval;
import com.wso2telco.workflow.subscription.SubscriptionApprovalImpl;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/approval")
public class WorkflowApprovalService {

    private ApplicationApproval applicationApproval =new ApplicationApprovalImpl();
    private SubscriptionApproval subscriptionApproval=new SubscriptionApprovalImpl();


    @POST
    @Path("application/hub")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response applicationApprovalHub(Application application){
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
    public Response subscriptionApprovalHub(Subscription subscription){
        try {
            subscriptionApproval.updateDBSubHubApproval(subscription);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }


    @PUT
    @Path("application/operator")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response applicationApprovalOperator(Application application){
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
    public Response subscriptionApprovalOperator(Subscription subscription){
        try {
            subscriptionApproval.updateDBSubOpApproval(subscription);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


    @POST
    @Path("subscription/validator")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertValidatorForSubscription(SubscriptionValidation subscriptionValidation){
        try {
            subscriptionApproval.insertValidatorForSubscription(subscriptionValidation);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
