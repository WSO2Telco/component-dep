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


import com.wso2telco.workflow.model.*;
import com.wso2telco.workflow.notification.Notification;
import com.wso2telco.workflow.notification.NotificationImpl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/notification")
public class NotificationAPI {

    Notification notification=new NotificationImpl();

    @POST
    @Path("application/hub")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendHUBAdminAppApprovalNotification(NotificationRequest request){
        try {
            notification.sendHUBAdminAppApprovalNotification(request);
            return Response.status(Response.Status.OK).build();
        }catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("subscription/hub")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendHUBAdminSubrovalNotification(NotificationRequest request){
        try {
            notification.sendHUBAdminSubrovalNotification(request);
            return Response.status(Response.Status.OK).build();
        }catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("application/plugin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendPLUGINAdminAppApprovalNotification(NotificationRequest request){
        try {
            notification.sendPLUGINAdminAppApprovalNotification(request);
            return Response.status(Response.Status.OK).build();
        }catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("subscription/plugin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendPLUGINAdminSubApprovalNotification(NotificationRequest request){
        try {
            notification.sendPLUGINAdminSubApprovalNotification(request);
            return Response.status(Response.Status.OK).build();
        }catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("application/sp")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendAppApprovalStatusSPNotification(NotificationRequest request){
        try {
            notification.sendAppApprovalStatusSPNotification(request);
            return Response.status(Response.Status.OK).build();
        }catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("subscription/sp")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendSubApprovalStatusSPNotification(NotificationRequest request){
        try {
            notification.sendSubApprovalStatusSPNotification(request);
        return Response.status(Response.Status.OK).build();
    }catch(Exception ex){
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }}

    @POST
    @Path("subscription/gateway")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendSubApprovalStatusGatewayNotification(NotificationRequest request){
        try {
            notification.sendInternalAdminSubrovalNotification(request);
            return Response.status(Response.Status.OK).build();
        }catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }}

    @POST
    @Path("subscription/publisher")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendSubApprovalStatusApiPublisherNotification(NotificationRequest request){
        try {
            notification.sendPublisherSubApprovalNotification(request);
            return Response.status(Response.Status.OK).build();
        }catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }}


}
