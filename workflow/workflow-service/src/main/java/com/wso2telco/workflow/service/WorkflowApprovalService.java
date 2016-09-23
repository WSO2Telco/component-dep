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
        String result = null;
        applicationApproval.updateDBAppHubApproval(application);
        return Response.status(200).entity(result).build();
    }

    @POST
    @Path("subscription/hub")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response subscriptionApprovalHub(Subscription subscription){
        String result = null;
        subscriptionApproval.updateDBSubHubApproval(subscription);
        return Response.status(200).entity(result).build();
    }


    @PUT
    @Path("application/operator")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response applicationApprovalOperator(Application application){
        String result = null;
        applicationApproval.updateDBAppOpApproval(application);
        return Response.status(200).entity(result).build();
    }


    @PUT
    @Path("subscription/operator")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response subscriptionApprovalOperator(Subscription subscription){

        String result = null;
        subscriptionApproval.updateDBSubOpApproval(subscription);
        return Response.status(200).entity(result).build();
    }


    @POST
    @Path("subscription/validator")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertValidatorForSubscription(SubscriptionValidation subscriptionValidation){
        String result = null;
        subscriptionApproval.insertValidatorForSubscription(subscriptionValidation);
        return Response.status(200).entity(result).build();

    }
}
