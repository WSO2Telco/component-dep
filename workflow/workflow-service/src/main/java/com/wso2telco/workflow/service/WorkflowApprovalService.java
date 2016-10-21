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
